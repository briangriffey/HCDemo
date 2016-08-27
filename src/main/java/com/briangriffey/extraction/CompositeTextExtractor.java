package com.briangriffey.extraction;

import com.briangriffey.extraction.extractors.emoticons.EmoticonExtraction;
import com.briangriffey.extraction.extractors.emoticons.EmoticonExtractor;
import com.briangriffey.extraction.extractors.mentions.MentionExtractor;
import com.briangriffey.extraction.extractors.url.HtmlTitleExtraction;
import com.briangriffey.extraction.extractors.url.StringHtmlTitleExtractor;
import okhttp3.OkHttpClient;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * A feature extractor that will go through and pull out all of the information it can from a String. The current
 * feature set will include emoticons, mentions, and html title information. For more info on the parts of the extraction
 * see the {@link EmoticonExtractor EmoticonExtractor} {@link MentionExtractor MentionExtractor} and
 * {@link com.briangriffey.extraction.extractors.url.StringHtmlTitleExtractor StringHtmlTitleExtractor}
 *
 */
public class CompositeTextExtractor implements DataFeatureExtractor<String, Extraction>{

    private final Scheduler networkScheduler;
    private final EmoticonExtractor emoticonExtractor;
    private final MentionExtractor mentionExtractor;
    private final StringHtmlTitleExtractor stringHtmlTitleExtractor;


    /**
     * Construct a composite text extractor. See the class doc for more details on what will be extracted
     * @param httpClient the http client to use if network calls are required
     * @param scheduler the scheduler that the network calls will run on. Can be any scheduler to produce parallel execution
     */
    public CompositeTextExtractor(OkHttpClient httpClient, Scheduler scheduler) {
        emoticonExtractor = new EmoticonExtractor();
        mentionExtractor = new MentionExtractor();
        stringHtmlTitleExtractor = new StringHtmlTitleExtractor(httpClient, scheduler);

        //construct a network scheduler that will use cached threads
        networkScheduler = Schedulers.io();
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

        //Here you'll notice this subscribe on. This is going to place
        Observable<HtmlTitleExtraction> htmlTitles = stringHtmlTitleExtractor.getExtractedFeatures(data);


        return mentions.concatWith(emoticons).concatWith(htmlTitles);
    }
}
