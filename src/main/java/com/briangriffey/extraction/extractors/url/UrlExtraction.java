package com.briangriffey.extraction.extractors.url;

import android.util.Patterns;
import com.briangriffey.extraction.ExtractionVisitor;
import com.briangriffey.extraction.extractions.StringExtraction;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Data class that describes a URI extraction from a sentence. The source type is a string and the extraction
 * is a URI.
 */
public class UrlExtraction extends StringExtraction<URL> {
    private final URL uri;

    /**
     * Constructor that is purposely package level. Package level is enforced to make sure that the parameter args
     * come from a well conceived source and match the actual string output of a url extractor
     * @param start the start position of the extraction in the source
     * @param end the end of position of the extraction in the source string
     * @param source the source string
     * @param webUrl The extracted webUrl, should match any standard web url format
     *
     * @throws IllegalArgumentException thrown if the extraction does not match the {@link Patterns#WEB_URL url format}
     */
    public UrlExtraction(int start, int end, String source, String webUrl) {
        super(start, end, source);

        try {
            this.uri = new URL(webUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @return Returns the URI that was matched out of a piece of text. If your sentence was
     * "I love going to http://www.reddit.com", then the extraction would be http://www.reddit.com
     * reddit
     */
    @Override
    public URL getExtraction() {
        return uri;
    }

    @Override
    public void acceptVisitor(ExtractionVisitor visitor) {
        visitor.visitExtraction(this);
    }
}
