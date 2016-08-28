package com.briangriffey.resources;

import com.briangriffey.extraction.extractors.url.MockWebServerTest;
import com.briangriffey.health.ErrorReporter;
import com.briangriffey.responses.featureextraction.FeatureExtractionResponse;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class FeatureExtractionUnitTest extends MockWebServerTest {

    private MockWebServer webserver;
    private OkHttpClient client;
    private FeatureExtractionResource resource;

    @Before
    public void setup() throws IOException {
        this.webserver = newMockWebServer();
        this.webserver.start();

        this.client = new OkHttpClient();
        this.resource = new FeatureExtractionResource(client, mock(ErrorReporter.class));
    }

    @After
    public void teardown() throws IOException {
        this.webserver.shutdown();
    }

    @Test
    public void testEmotesAndMentions() {

        FeatureExtractionResponse response = resource.extractFeaturesFromText("Hey @brian how (now) (brown) (cow)");
        assertNotNull(response);

        assertEquals(1, response.getMentions().size());
        assertEquals("brian", response.getMentions().get(0));

        assertEquals(3, response.getEmoticons().size());
        assertEquals("now", response.getEmoticons().get(0));
        assertEquals("brown", response.getEmoticons().get(1));
        assertEquals("cow", response.getEmoticons().get(2));
    }

    @Test
    public void testOnlyUrls() throws MalformedURLException {
        URL fakeUrl = queueWootResponse(this.webserver);

        FeatureExtractionResponse response = resource.extractFeaturesFromText("Hey look at " + fakeUrl + ". That article is crazy!");
        assertNotNull(response);

        assertEquals(0, response.getMentions().size());
        assertEquals(0, response.getEmoticons().size());
        assertEquals(1, response.getLinks().size());

        assertEquals("Woot woot woot", response.getLinks().get(0).getTitle());
        assertEquals(fakeUrl.toString(), response.getLinks().get(0).getUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull() {
        resource.extractFeaturesFromText(null);
    }
}
