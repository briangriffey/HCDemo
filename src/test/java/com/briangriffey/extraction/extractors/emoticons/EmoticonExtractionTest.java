package com.briangriffey.extraction.extractors.emoticons;

import com.briangriffey.extraction.ExtractionVisitor;
import com.briangriffey.extraction.extractors.mentions.MentionExtraction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EmoticonExtractionTest {

    @Test
    public void testGoodConstruction() {
        EmoticonExtraction goodConstruction = getGoodConstruction();

        assertEquals(0, goodConstruction.getStart());
        assertEquals(6, goodConstruction.getEnd());
        assertEquals("(barf)", goodConstruction.getExtraction());
        assertEquals("(barf) that is gross", goodConstruction.getSource());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadStartConstruction() {
        EmoticonExtraction extraction = new EmoticonExtraction(-1,6,"(barf) that is gross", "(barf)");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testBadEndConstruction() {
        EmoticonExtraction extraction = new EmoticonExtraction(0, 21 ,"(barf) that is gross", "(barf)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSourceConstruction() {
        EmoticonExtraction extraction = new EmoticonExtraction(0, 6, null, "(barf)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadEmoticonPull() {
        EmoticonExtraction extraction = new EmoticonExtraction(0, 6, "(barf) that is gross", "barf");
    }

    @Test
    public void testGetEmoticonTExt() {
        EmoticonExtraction extraction = new EmoticonExtraction(0, 6, "(barf) that is gross", "(barf)");

        assertEquals("barf", extraction.getEmoticonText());
    }

    @Test
    public void testVisitor() {
        ExtractionVisitor visitor = mock(ExtractionVisitor.class);
        EmoticonExtraction extraction = new EmoticonExtraction(0, 6, "(barf) that is gross", "(barf)");

        extraction.acceptVisitor(visitor);
        verify(visitor, times(1)).visitExtraction(extraction);
    }

    public EmoticonExtraction getGoodConstruction() {
        EmoticonExtraction extraction = new EmoticonExtraction(0, 6, "(barf) that is gross", "(barf)");
        return extraction;
    }

}
