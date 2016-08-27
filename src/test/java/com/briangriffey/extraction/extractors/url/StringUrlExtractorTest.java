package com.briangriffey.extraction.extractors.url;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class StringUrlExtractorTest {

    private StringUrlExtractor extractor;

    @Before
    public void setup() {
        this.extractor = new StringUrlExtractor();
    }

    @Test
    public void testGoodPath() {
        Iterator<UrlExtraction> iterator = extractor.getExtractedFeatures("@brian did you see http://www.funnyvideo.com/html/funstuff#party it's the best. Or how about http://brian:funparty@www.hidemypassword.com")
                .toBlocking().getIterator();

        UrlExtraction extraction = iterator.next();
        assertNotNull(extraction);
        assertEquals("http://www.funnyvideo.com/html/funstuff#party", extraction.getExtraction().toString());

        extraction = iterator.next();
        assertEquals("http://brian:funparty@www.hidemypassword.com", extraction.getExtraction().toString());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNoFalsePositives() {
        Iterator<UrlExtraction> iterator = extractor.getExtractedFeatures("And a man once said://www This stuff .com fun party boombox@boombox.com").toBlocking().getIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNull() {
        Iterator<UrlExtraction> iterator = extractor.getExtractedFeatures(null).toBlocking().getIterator();
        assertFalse(iterator.hasNext());
    }

}
