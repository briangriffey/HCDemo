package com.briangriffey.extraction.extractors.url;

import com.briangriffey.extraction.Extraction;
import com.briangriffey.extraction.ExtractionVisitor;
import com.briangriffey.extraction.html.HtmlInformation;

import java.net.URL;

/**
 * An extraction type that holds the title of an html page, that's pointed to by the uri source
 */
public class HtmlTitleExtraction implements Extraction<URL, HtmlInformation> {

    private final URL source;
    private final HtmlInformation information;

    HtmlTitleExtraction(URL source, HtmlInformation information) {
        this.source = source;
        this.information = information;
    }

    @Override
    public URL getSource() {
        return source;
    }

    @Override
    public HtmlInformation getExtraction() {
        return information;
    }

    @Override
    public void acceptVisitor(ExtractionVisitor visitor) {
        visitor.visitExtraction(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HtmlTitleExtraction that = (HtmlTitleExtraction) o;

        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        return information != null ? information.equals(that.information) : that.information == null;

    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (information != null ? information.hashCode() : 0);
        return result;
    }
}
