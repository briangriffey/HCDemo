package com.briangriffey.extraction.extractors.url;

import com.briangriffey.extraction.ExtractionVisitor;
import com.briangriffey.extraction.html.HtmlInformation;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class HtmlTitleExtractionTest {

    @Test
    public void testBothThingsComeOut() throws MalformedURLException {
        HtmlInformation info = new HtmlInformation("title");
        HtmlTitleExtraction extraction = getGoodConstruction();

        assertEquals(info, extraction.getExtraction());
        assertEquals(new URL("http://www.google.com"), extraction.getSource());
    }

    @Test
    public void testVisitor() throws MalformedURLException {
        ExtractionVisitor visitor = mock(ExtractionVisitor.class);

        HtmlInformation info = new HtmlInformation("title");
        URL url = new URL("http://www.google.com");
        HtmlTitleExtraction extraction = new HtmlTitleExtraction(url, info);

        extraction.acceptVisitor(visitor);
        verify(visitor, times(1)).visitExtraction(extraction);
    }

    public HtmlTitleExtraction getGoodConstruction() throws MalformedURLException {
        HtmlInformation info = new HtmlInformation("title");
        URL url = new URL("http://www.google.com");

        HtmlTitleExtraction extraction = new HtmlTitleExtraction(url, info);
        return extraction;
    }
}
