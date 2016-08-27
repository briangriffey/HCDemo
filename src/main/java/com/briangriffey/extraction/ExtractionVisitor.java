package com.briangriffey.extraction;

import com.briangriffey.extraction.extractors.emoticons.EmoticonExtraction;
import com.briangriffey.extraction.extractors.mentions.MentionExtraction;
import com.briangriffey.extraction.extractors.url.HtmlTitleExtraction;
import com.briangriffey.extraction.extractors.url.UrlExtraction;

/**
 * There can be many types of extractions given by our {@link DataFeatureExtractor data feature extractors}. All
 * extractors will output an implementation of {@link Extraction an extraction} that may eventually be aggregated into
 * a collection. In order to reveal the subclass of a collection of abstract Extractions we will use the visitor pattern.
 * This interface defined a type of object that can visit every subclass of the Extension type.
 */
public interface ExtractionVisitor {

    void visitExtraction(UrlExtraction urlExtraction);
    void visitExtraction(HtmlTitleExtraction htmlTitleExtraction);
    void visitExtraction(MentionExtraction mentionExtration);
    void visitExtraction(EmoticonExtraction emoticonExtraction);
}
