package com.briangriffey.extraction.extractors.regex;

import com.briangriffey.extraction.Extraction;
import com.briangriffey.extraction.DataFeatureExtractor;
import rx.Observable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by briangriffey on 8/25/16.
 */
public abstract class RegexExtractor<T extends Extraction> implements DataFeatureExtractor<String, T> {

    @Override
    public final Observable<T> getExtractedFeatures(final String text) {

        if(text == null) {
            return Observable.empty();
        }

        return Observable.create(subscriber -> {
            Matcher matcher = getRegexPattern().matcher(text);
            try {
                //Go through and find all of the matches in the the string
                while (matcher.find()) {
                    //every time that we find an emoticon, then output it to the stream
                    T extraction = createExtractionForMatch(matcher.start(), matcher.end(), text.substring(matcher.start(), matcher.end()), text);
                    subscriber.onNext(extraction);
                }
                //when we are done make sure to mark the stream as complete.
                subscriber.onCompleted();
            } catch(Throwable e) {
                //here we will catch all exception and ensure to mark the stream as encoutnering an error
                subscriber.onError(e);
            }
        });
    }

    public abstract Pattern getRegexPattern();

    protected abstract T createExtractionForMatch(int start, int end, String substringMatch, String originalText);
}
