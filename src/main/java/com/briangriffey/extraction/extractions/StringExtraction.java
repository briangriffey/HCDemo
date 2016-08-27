package com.briangriffey.extraction.extractions;

import com.briangriffey.extraction.Extraction;

/**
 * An abstract class that notes most of the common fields held by a string extraction
 */
public abstract class StringExtraction<TYPE> implements Extraction<String, TYPE> {

    private final int start;
    private final int end;
    private final String source;


    /**
     * Create a string extraction. Should contain the start and end indices of the string
     * @param start Starting position of the match in the string, should be less than end
     * @param end Ending position of the match in the string
     * @param source The source string
     *
     * @throws IllegalArgumentException Will throw an illegal argument exception if: start is greater than end,
     * start or end are out of the string brounds, or is the source is null
     */
    public StringExtraction(int start, int end, String source)  {

        if(source == null) {
            throw new IllegalArgumentException("StringExtraction can not have a null source");
        }

        if(start < 0 || start >=end){
            throw new IllegalArgumentException("Start can not be less than 0 or to or >= end");
        }

        if(end > source.length()) {
            throw new IllegalArgumentException("End can not be greater than or equal to the length of the string");
        }

        this.start = start;
        this.end = end;
        this.source = source;
    }

    /**
     * @return The starting position of the mention in the source string
     */
    public int getStart() {
        return start;
    }

    /**
     * @return The end position of the mention in the source string
     */
    public int getEnd() {
        return end;
    }

    /**
     *
     * @return The string from which we are extracting mentions
     */
    @Override
    public String getSource() {
        return source;
    }
}
