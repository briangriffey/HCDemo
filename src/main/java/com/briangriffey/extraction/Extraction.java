package com.briangriffey.extraction;

/**
 * An object that describe a data extraction from a source piece of data. For example you could
 * extract a username from a piece of text like "Did you see that @briangriffey", where the extraction
 * would be @briangriffey. Another example would be extracting a title from a web page
 */
public interface Extraction<SOURCE, TYPE> {
    SOURCE getSource();

    TYPE getExtraction();

    void acceptVisitor(ExtractionVisitor visitor);
}
