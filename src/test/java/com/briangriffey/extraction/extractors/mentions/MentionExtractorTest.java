package com.briangriffey.extraction.extractors.mentions;

import com.briangriffey.extraction.extractors.mentions.MentionExtraction;
import com.briangriffey.extraction.extractors.mentions.MentionExtractor;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class MentionExtractorTest {

    private MentionExtractor extractor;

    @Before
    public void setup() {
        this.extractor = new MentionExtractor();
    }

    @Test
    public void testNull() {
        //sending in null should yield 0 results
        Iterator<MentionExtraction> iterator = extractor.getExtractedFeatures(null).toBlocking().getIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSimple() {
        Iterator<MentionExtraction> iterator = extractor.getExtractedFeatures("@brian did you tell @ted").toBlocking().getIterator();

        MentionExtraction extraction = iterator.next();
        assertNotNull(extraction);
        assertEquals("@brian", extraction.getExtraction());

        extraction = iterator.next();
        assertEquals("@ted", extraction.getExtraction());
    }

    @Test
    public void testBad() {
        Iterator<MentionExtraction> iterator = extractor.getExtractedFeatures("@brian did you email Jim? His email is jim@jimbo.com. @. @{}[p @++ @@").toBlocking().getIterator();
        MentionExtraction extraction = iterator.next();
        assertEquals("@brian", extraction.getExtraction());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testVeryLong() {
        Iterator<MentionExtraction> iterator = extractor.getExtractedFeatures("Will you match @ohmygodthisissolongwhyisthissolongitshouldntbesolong ").toBlocking().getIterator();
        MentionExtraction extraction = iterator.next();
        assertEquals("@ohmygodthisissolongwhyisthissolongitshouldntbesolong", extraction.getExtraction());
    }
}
