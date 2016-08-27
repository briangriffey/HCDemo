package com.briangriffey.extraction.extractors.mentions;

import com.briangriffey.extraction.ExtractionVisitor;
import com.briangriffey.extraction.extractions.StringExtraction;
import com.briangriffey.extraction.extractors.regex.HipchatRegex;

/**
 * Data class that describes an mention extraction from a sentence. The source type is a string and the extraction
 * is a string that is the content of the emoticon.
 */
public class MentionExtraction extends StringExtraction<String> {

    private final String extraction;

    /**
     * Constructor that is purposely package level. Package level is enforced to make sure that the parameter args
     * come from a well conceived source and match the actual string output of a emoticon extractor
     * @param start the start position of the extraction in the source
     * @param end the end of position of the extraction in the source string
     * @param source the source string
     * @param extraction The extracted mention, should be in the format @brian
     *
     * @throws IllegalArgumentException thrown if the extraction does not match the HipchatRegex.MENTION_PATTERN
     */
    MentionExtraction(int start, int end, String source, String extraction) {
        super(start, end, source);
        this.extraction = extraction;

        if(!HipchatRegex.MENTION_PATTERN.matcher(extraction).matches()) {
            throw new IllegalArgumentException("The mention extraction does not match the mention regex pattern and is not a valid mention");
        }
    }

    /**
     *
     * @return Returns the extracted username from the text including the @ symbol. For example if you were to write
     * "Hello @michael, you're the best", the extraction would be @michael
     */
    @Override
    public String getExtraction() {
        return extraction;
    }

    /**
     *
     * @return  Returns just the name portion of the extraction. So if your extraction is @michael, then this method will simply
     * return "michael".
     */
    public String getNoAtSymbolExtraction() {
        return extraction.substring(1);
    }

    @Override
    public void acceptVisitor(ExtractionVisitor visitor) {
        visitor.visitExtraction(this);
    }
}
