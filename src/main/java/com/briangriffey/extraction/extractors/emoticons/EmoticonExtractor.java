package com.briangriffey.extraction.extractors.emoticons;

import com.briangriffey.extraction.extractors.regex.HipchatRegex;
import com.briangriffey.extraction.extractors.regex.RegexExtractor;

import java.util.regex.Pattern;

/**
 * Class that will extract emoticon features from a piece of text.
 * The definition of a valid emoticon comes from the exercise definition:
 * <p>
 * For this exercise, you only need to consider 'custom' extractors which are alphanumeric strings, no longer than 15
 * characters, contained in parenthesis. You can assume that anything matching this format is an emoticon.
 * (https://www.hipchat.com/emoticons)
 */
public class EmoticonExtractor extends RegexExtractor<EmoticonExtraction> {

    @Override
    public Pattern getRegexPattern() {
        return HipchatRegex.EMOTICON_PATTERN;
    }

    @Override
    protected EmoticonExtraction createExtractionForMatch(int start, int end, String substringMatch, String originalText) {
        return new EmoticonExtraction(start, end, originalText, substringMatch);
    }

}
