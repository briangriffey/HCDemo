package com.briangriffey.extraction.extractors.emoticons;

import com.briangriffey.extraction.ExtractionVisitor;
import com.briangriffey.extraction.extractions.StringExtraction;
import com.briangriffey.extraction.extractors.regex.HipchatRegex;

/**
 * Data class that describes an emoticon extraction from a sentence. The source type is a string and the extraction
 * is a string that is the content of the emoticon.
 *
 */
public class EmoticonExtraction extends StringExtraction<String> {

    private final String extraction;

    /**
     * Constructor that is purposely package level. Package level is enforced to make sure that the parameter args
     * come from a well conceived source and match the actual string output of a emoticon extractor
     * @param start the start position of the extraction in the source
     * @param end the end of position of the extraction in the source string
     * @param source the source string
     * @param extraction The extracted emoticon, should match the pattern found in {@link EmoticonExtractor EmoticonExtractor}
     *
     * @throws IllegalArgumentException thrown if the extraction doesn't match the {@link com.briangriffey.extraction.extractors.regex.HipchatRegex#EMOTICON_PATTERN Emoticon pattern }
     */
    EmoticonExtraction(int start, int end, String source, String extraction) {
        super(start, end, source);
        this.extraction = extraction;

        if(!HipchatRegex.EMOTICON_PATTERN.matcher(extraction).matches()) {
            throw new IllegalArgumentException("Emoticon extraction does not match the emoticon pattern");
        }
    }

    /**
     *
     * @return Get the extracted emoticon text. This will include the parenthesis in the extraction
     * For example: if the source is "I love (coffee)" then the extraction would be "(coffee)"
     */
    @Override
    public String getExtraction() {
        return extraction;
    }

    /**
     * @return Get the actual text of the emoticon. If the emoticon is (coffee) then this method will simply return coffee
     */
    public String getEmoticonText() {
        return extraction.substring(1, extraction.length()-1);
    }

    @Override
    public void acceptVisitor(ExtractionVisitor visitor) {
        visitor.visitExtraction(this);
    }
}
