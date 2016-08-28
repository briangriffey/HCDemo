package com.briangriffey.extraction.extractors.url;

import com.briangriffey.extraction.DataFeatureExtractor;
import okhttp3.OkHttpClient;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Composite class that will take a text string, parse out the urls, and then connects to those urls to parse out the
 * title {@link com.briangriffey.extraction.html.HtmlInformation HtmlInformation} from the page. This will use this
 * {@link StringUrlExtractor StringUrlExtractor} to extract out the urls and then use the {@link StringHtmlTitleExtractor StringHtmlTitleExtractor}
 * to get the html information
 */
public class StringHtmlTitleExtractor implements DataFeatureExtractor<String, HtmlTitleExtraction>{

    private final StringUrlExtractor urlExtractor;
    private final UrlHtmlTitleExtractor htmlInfoExtractor;
    private final Scheduler scheduler;

    /**
     * Construct the string to html info extractor
     * @param client the okhttp client to use to make all of the network calls
     * @param scheduler The scheduler that you would like the network calls to run on. Since there can be multiple
     *                  network calls we wouldn't want to have them all running sequentially. We'll leave the threading
     *                  decisions up to the caller
     */
    public StringHtmlTitleExtractor(OkHttpClient client, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.urlExtractor = new StringUrlExtractor();
        this.htmlInfoExtractor = new UrlHtmlTitleExtractor(client);
    }

    @Override
    public Observable<HtmlTitleExtraction> getExtractedFeatures(String data) {
        return urlExtractor.getExtractedFeatures(data)
                .flatMap(urlExtraction -> htmlInfoExtractor.getExtractedFeatures(urlExtraction.getExtraction()).subscribeOn(scheduler));
    }
}
