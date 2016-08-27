package com.briangriffey.extraction.extractors.url;

import android.util.Patterns;
import com.briangriffey.extraction.extractors.regex.RegexExtractor;

import java.util.regex.Pattern;

/**
 * Extractor that will parse out all URLs from a given piece of text.
 *
 * For example given "I love going to http://www.reddit.com", then the extraction would be a url
 * extraction containing the uri http://www.reddit.com
 */
public class StringUrlExtractor extends RegexExtractor<UrlExtraction> {

    @Override
    public Pattern getRegexPattern() {
        //the best url matcher out there actually belongs to android. It's insane that java doesn't have a
        //better one after all of these years. I added this method that requires http or https before web links
        return Patterns.WEB_URL_HTTP_REQUIRED;
    }

    @Override
    protected UrlExtraction createExtractionForMatch(int start, int end, String substringMatch, String originalText) {
        return new UrlExtraction(start, end, originalText, substringMatch);
    }
}
