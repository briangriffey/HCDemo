package com.briangriffey.extraction;

import com.briangriffey.extraction.extractors.emoticons.EmoticonExtraction;
import com.briangriffey.extraction.extractors.emoticons.EmoticonExtractor;
import com.briangriffey.extraction.extractors.mentions.MentionExtractor;
import com.briangriffey.extraction.extractors.url.ErrorResumingUrlHtmlTitleExtractor;
import com.briangriffey.extraction.extractors.url.HtmlTitleExtraction;
import com.briangriffey.extraction.extractors.url.StringUrlExtractor;
import com.briangriffey.extraction.extractors.url.UrlHtmlTitleExtractor;
import com.briangriffey.health.ErrorReporter;
import okhttp3.OkHttpClient;
import rx.Observable;
import rx.Scheduler;

/**
 * A feature extractor that will go through and pull out all of the information it can from a String. The current
 * feature set will include emoticons, mentions, and html title information. For more info on the parts of the extraction
 * see the {@link EmoticonExtractor EmoticonExtractor} {@link MentionExtractor MentionExtractor} and
 * {@link com.briangriffey.extraction.extractors.url.StringHtmlTitleExtractor StringHtmlTitleExtractor}.
 * On any error it will emit an {@link com.briangriffey.extraction.exception.ErrorExtraction ErrorExtraction}
 * and report the error to the {@link com.briangriffey.health.ErrorReporter reporter}
 *
 */
public class ErrorRecoveringCompositeTextExtractor implements DataFeatureExtractor<String, Extraction>{

    private final EmoticonExtractor emoticonExtractor;
    private final MentionExtractor mentionExtractor;
    private final StringUrlExtractor urlExtractor;
    private final ErrorResumingUrlHtmlTitleExtractor stringHtmlTitleExtractor;
    private final Scheduler scheduler;


    /**
     * Construct a composite text extractor. See the class doc for more details on what will be extracted
     * @param httpClient the http client to use if network calls are required
     * @param scheduler the scheduler that the network calls will run on. Can be any scheduler to produce parallel execution
     * @param reporter the error reporter to give all errors to
     */
    public ErrorRecoveringCompositeTextExtractor(OkHttpClient httpClient, Scheduler scheduler, ErrorReporter reporter) {
        emoticonExtractor = new EmoticonExtractor();
        mentionExtractor = new MentionExtractor();
        urlExtractor = new StringUrlExtractor();

        this.scheduler = scheduler;

        UrlHtmlTitleExtractor titleExtractor = new UrlHtmlTitleExtractor(httpClient);
        stringHtmlTitleExtractor = new ErrorResumingUrlHtmlTitleExtractor(titleExtractor, reporter);
    }

    /**
     * Returns an observable that will emit all {{@link com.briangriffey.extraction.extractors.mentions.MentionExtraction mention Extractions}
     * then {@link EmoticonExtraction emoticon extractions} then {@link HtmlTitleExtraction title extractions}
     * @param data The data to extract features from
     * @return An observable described above
     */
    @Override
    public Observable<Extraction> getExtractedFeatures(String data) {
        //Java isn't the most sophisticated with type erasure, so let's make all of these observables
        //simply cast into their super type. We have to do this to use the concat operator below
        Observable<Extraction> mentions = mentionExtractor.getExtractedFeatures(data)
                .map(mentionExtraction -> (Extraction)mentionExtraction);

        Observable<EmoticonExtraction> emoticons = emoticonExtractor.getExtractedFeatures(data);

        //Here you'll notice this subscribe on. This will run on whatever thread pool you assign it to here
        Observable<Extraction> htmlTitles = urlExtractor.getExtractedFeatures(data).flatMap(urlExtraction -> {
             return stringHtmlTitleExtractor.getExtractedFeatures(urlExtraction.getExtraction())
                    .subscribeOn(scheduler);
        });


        return mentions.concatWith(emoticons).concatWith(htmlTitles);
    }
}
