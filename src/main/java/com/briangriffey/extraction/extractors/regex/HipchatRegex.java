package com.briangriffey.extraction.extractors.regex;

import java.util.regex.Pattern;

/**
 * Patterns that describe common regex patterns that are unique to Hipchat
 */
public final class HipchatRegex {

    //Here we are using the predefined unicode compatability classes. We wouldn't want to limit ourselves to just
    //english with the normal a-zA-Z0-9 pattern
    public static final Pattern EMOTICON_PATTERN = Pattern.compile("\\([\\p{Alnum}]{1,15}\\)");

    /**
     * Pattern matching a string that starts with @ followed by any non-whitespace character, as long as the @ is the
     * beginning of a sentence or is proceeded by a whitespace character. This is important so that we don't match
     * email addresses
     */
    public static final Pattern MENTION_PATTERN = Pattern.compile("@\\w+");

}
