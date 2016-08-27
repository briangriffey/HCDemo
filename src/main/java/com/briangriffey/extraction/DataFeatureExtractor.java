package com.briangriffey.extraction;

import rx.Observable;

/**
 * Interface that will describe all feature extractors
 * A feature extractor takes a piece of data and extracts features from it.
 *
 * The generic type T is the type of feature that you will be extracting
 * The generic type SOURCE_TYPE is the kind of data that you will start with
 *
 * For example: You might get create an extractor that takes text as inputs and outputs emoticons.
 */
public interface DataFeatureExtractor<SOURCE_TYPE, EXTRACTION extends Extraction> {

    /**
     * This method should produce an observable that is capable of parsing the text given in the argument
     * and publishing out a stream of extracted features from that text
     *
     * @param data the piece of data to pull features from
     * @return An {@link rx.Observable observable} that will produce features from the given text
     */
    Observable<EXTRACTION> getExtractedFeatures(SOURCE_TYPE data);
}
