package com.briangriffey.responses;

import com.briangriffey.extraction.extractors.emoticons.EmoticonExtraction;
import com.briangriffey.extraction.extractors.emoticons.EmoticonExtractionTest;
import com.briangriffey.extraction.extractors.mentions.MentionExtraction;
import com.briangriffey.extraction.extractors.mentions.MentionExtractionTest;
import com.briangriffey.extraction.extractors.url.HtmlTitleExtraction;
import com.briangriffey.extraction.extractors.url.HtmlTitleExtractionTest;
import com.briangriffey.extraction.extractors.url.UrlExtraction;
import com.briangriffey.responses.featureextraction.FeatureExtractionResponse;
import com.briangriffey.responses.featureextraction.UrlInfo;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;

public class FeatureExtractionResponseTest {

    public FeatureExtractionResponse getGoodResponse() throws MalformedURLException {
        MentionExtraction extraction = new MentionExtractionTest().getGoodConstruction();
        HtmlTitleExtraction htmlTitleExtraction = new HtmlTitleExtractionTest().getGoodConstruction();
        EmoticonExtraction emoticon = new EmoticonExtractionTest().getGoodConstruction();

        FeatureExtractionResponse response = new FeatureExtractionResponse.Builder()
                .withExtraction(extraction)
                .withExtraction(htmlTitleExtraction)
                .withExtraction(emoticon)
                .build();

        return response;
    }

    @Test
    public void testConstructionOfGoodItems() throws MalformedURLException {
       FeatureExtractionResponse response = getGoodResponse();

        assertEquals(1, response.getLinks().size());
        assertEquals(1, response.getEmoticons().size());
        assertEquals(1, response.getMentions().size());

        assertEquals("brian", response.getMentions().get(0));
        assertEquals(new UrlInfo("http://www.google.com", "title"), response.getLinks().get(0));
        assertEquals("barf", response.getEmoticons().get(0));
    }

    @Test
    public void testNull() {
        FeatureExtractionResponse.Builder responseBuilder = new FeatureExtractionResponse.Builder()
                .withExtraction(null);

        responseBuilder.visitExtraction((UrlExtraction) null);
        responseBuilder.visitExtraction((EmoticonExtraction) null);
        responseBuilder.visitExtraction((MentionExtraction) null);
        responseBuilder.visitExtraction((HtmlTitleExtraction) null);

        FeatureExtractionResponse response = responseBuilder.build();

        assertEquals(0, response.getLinks().size());
        assertEquals(0, response.getEmoticons().size());
        assertEquals(0, response.getMentions().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableMentions() throws MalformedURLException {
        FeatureExtractionResponse response = getGoodResponse();

        response.getMentions().add("sneaky mention");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableEmoticons() throws MalformedURLException {
        FeatureExtractionResponse response = getGoodResponse();

        response.getEmoticons().add("sneaky mention");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableUrlInfo() throws MalformedURLException {
        FeatureExtractionResponse response = getGoodResponse();

        response.getLinks().add(new UrlInfo("", ""));
    }

}
