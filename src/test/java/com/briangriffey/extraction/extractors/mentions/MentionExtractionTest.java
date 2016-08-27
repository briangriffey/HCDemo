package com.briangriffey.extraction.extractors.mentions;

import com.briangriffey.extraction.ExtractionVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MentionExtractionTest {

    @Test
    public void testGoodConstruction() {
        MentionExtraction extraction = getGoodConstruction();

        assertEquals(0, extraction.getStart());
        assertEquals(6, extraction.getEnd());
        assertEquals("@brian how are you?", extraction.getSource());
        assertEquals("@brian", extraction.getExtraction());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadStartConstruction() {
        MentionExtraction extraction = new MentionExtraction(-1,6,"@brian how are you?", "@brian");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testBadEndConstruction() {
        MentionExtraction extraction = new MentionExtraction(0, 20 ,"@brian how are you?", "@brian");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSourceConstruction() {
        MentionExtraction extraction = new MentionExtraction(0, 6, null, "@brian");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadMentionPull() {
        MentionExtraction extraction = new MentionExtraction(0, 6, "@brian how are you?", "barf");
    }

    @Test
    public void testGetMentionText() {
        MentionExtraction extraction = new MentionExtraction(0, 6, "@brian how are you?", "@brian");

        assertEquals("brian", extraction.getNoAtSymbolExtraction());
    }

    @Test
    public void testVisitor() {
        ExtractionVisitor visitor = mock(ExtractionVisitor.class);
        MentionExtraction extraction = getGoodConstruction();

        extraction.acceptVisitor(visitor);
        verify(visitor, times(1)).visitExtraction(extraction);
    }

    public MentionExtraction getGoodConstruction() {
        return new MentionExtraction(0, 6, "@brian how are you?", "@brian");
    }
}
