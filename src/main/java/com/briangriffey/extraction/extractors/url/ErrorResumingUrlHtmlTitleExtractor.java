package com.briangriffey.extraction.extractors.url;

import com.briangriffey.extraction.DataFeatureExtractor;
import com.briangriffey.extraction.Extraction;
import com.briangriffey.extraction.exception.ErrorExtraction;
import com.briangriffey.health.ErrorReporter;
import rx.Observable;

import java.net.URL;

/**
 * An extractor that will pass back an ErrorExtraction if the {@link UrlHtmlTitleExtractor url extractor} fails
 */
public class ErrorResumingUrlHtmlTitleExtractor implements DataFeatureExtractor<URL, Extraction> {

    private final UrlHtmlTitleExtractor titleExtractor;
    private final ErrorReporter reporter;

    public ErrorResumingUrlHtmlTitleExtractor(UrlHtmlTitleExtractor titleExtractor, ErrorReporter reporter) {

        this.titleExtractor = titleExtractor;
        this.reporter = reporter;
    }

    /**
     * A wrapped observer from the source that will call onErrorResumeNext in the case of an exception and pass back an
     * ErrorExtraction.
     *
     * @param data the piece of data to pull features from
     * @return the observable that will get either piece of data
     */
    @Override
    public Observable<Extraction> getExtractedFeatures(URL data) {
        return titleExtractor.getExtractedFeatures(data)
                .map(extraction -> (Extraction) extraction)
                .onErrorResumeNext(throwable -> {
                    //hipchat probably has a service for reporting errors. This is where you would gracefully report
                    //them so that a monitor could be setup to tell if something is going badly or not. I put
                    //in a blank error reporter for now because there's so many options, and I'd rather not go spin
                    //up a crash reporting service for this exercise
                    reporter.reportError(throwable);

                    Extraction extraction = new ErrorExtraction(titleExtractor.getClass(), throwable);

                    return Observable.just(extraction);
                });
    }
}
