package com.briangriffey.extraction.extractors.url;

import com.briangriffey.extraction.Extraction;
import com.briangriffey.extraction.ExtractionVisitor;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UrlExtractionTest {

    @Test
    public void testGoodConstruction() {
        UrlExtraction extraction = new UrlExtraction(0, 6, "@brian Did you see http://www.reddit.com/r/programming/cooltanktop.jpg", "http://www.reddit.com/r/programming/cooltanktop.jpg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadStartConstruction() {
        UrlExtraction extraction = new UrlExtraction(-1,6,"@brian Did you see http://www.reddit.com/r/programming/cooltanktop.jpg", "http://www.reddit.com/r/programming/cooltanktop.jpg");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testBadEndConstruction() {
        UrlExtraction extraction = new UrlExtraction(0, 71 ,"@brian Did you see http://www.reddit.com/r/programming/cooltanktop.jpg", "http://www.reddit.com/r/programming/cooltanktop.jpg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSourceConstruction() {
        UrlExtraction extraction = new UrlExtraction(0, 6, null, "@brian");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadUrlPull() {
        UrlExtraction extraction = new UrlExtraction(0, 6, "@brian Did you see http://www.reddit.com/r/programming/cooltanktop.jpg", "barf");
    }

    @Test
    public void testVisitor() {
        ExtractionVisitor visitor = mock(ExtractionVisitor.class);
        UrlExtraction extraction = new UrlExtraction(0, 6, "@brian Did you see http://www.reddit.com/r/programming/cooltanktop.jpg", "http://www.reddit.com/r/programming/cooltanktop.jpg");

        extraction.acceptVisitor(visitor);

        verify(visitor, times(1)).visitExtraction(extraction);
    }
}
