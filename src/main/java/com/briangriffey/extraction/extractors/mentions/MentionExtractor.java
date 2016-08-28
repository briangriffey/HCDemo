package com.briangriffey.extraction.extractors.mentions;

import com.briangriffey.extraction.extractors.regex.RegexExtractor;

import java.util.regex.Pattern;

/**
 * Class that will extract mentions from a string of text. The definition of a mention comes from
 * the project definition document as follows
 *
 *  A way to mention a user. Always starts with an '@' and ends when hitting a non-word character.
 *  (https://confluence.atlassian.com/conf54/confluence-user-s-guide/sharing-content/using-mentions)
 *
 */
public class MentionExtractor extends RegexExtractor<MentionExtraction> {

    /**
     * Pattern matching a string that starts with @ followed by any non-whitespace character, as long as the @ is the
     * beginning of a sentence or is proceeded by a whitespace character. This is important so that we don't match
     * email addresses
     */
    public static final Pattern MENTION_EXTRACTION_PATTERN = Pattern.compile("^@\\w+|\\s@\\w+", Pattern.UNICODE_CHARACTER_CLASS);

    @Override
    public Pattern getRegexPattern() {
        return MENTION_EXTRACTION_PATTERN;
    }

    @Override
    protected MentionExtraction createExtractionForMatch(int start, int end, String substringMatch, String originalText) {
        //there are two patterns that we are matching.
        //Either we are matching the start of a sentence, or we are matching something to where there is whitespace in
        //before the @ symbol

        //If the @ isn't the first char, then it wasn't the start of the sentence, so let's move everything over by one
        if(substringMatch.charAt(0) != '@') {
            //move the start one
            start++;
            //move the substring match to be the substring minus the white space at the front
            substringMatch = substringMatch.substring(1);
        }

        return new MentionExtraction(start, end, originalText, substringMatch);
    }
}
