package com.briangriffey.extraction.extractors.url;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UrlHtmlTitleExtractorTest extends MockWebServerTest{

    private UrlHtmlTitleExtractor extractor;
    private MockWebServer server;

    @Before
    public void setup() throws IOException {
        OkHttpClient client = new OkHttpClient();
        this.extractor = new UrlHtmlTitleExtractor(client);

        this.server = newMockWebServer();

        // Start the server.
        server.start();
    }

    @After
    public void teardown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testGettingBack200Page() throws MalformedURLException {
        URL url = queueWootResponse(this.server);

        Iterator<HtmlTitleExtraction> iterator = extractor.getExtractedFeatures(url).toBlocking().getIterator();
        assertEquals("Woot woot woot", iterator.next().getExtraction().getTitle());
    }

    @Test
    public void testGettingBack404Page() throws MalformedURLException {
        URL url = queue404Response(this.server);

        Iterator<HtmlTitleExtraction> iterator = extractor.getExtractedFeatures(url).toBlocking().getIterator();
        assertEquals("", iterator.next().getExtraction().getTitle());
    }

    //This test will throw a RuntimeException because we are calling toblocking.getiterator
    @Test(expected = RuntimeException .class)
    public void testBadThingsException() throws IOException {

        OkHttpClient okClient = mock(OkHttpClient.class, RETURNS_DEEP_STUBS);
        this.extractor = new UrlHtmlTitleExtractor(okClient);

        when(okClient.newCall(any(Request.class)).execute()).thenThrow(new IOException("All the things are on fire"));

        Iterator<HtmlTitleExtraction> iterator = extractor.getExtractedFeatures(new URL("http://www.onfire.com")).toBlocking().getIterator();
        iterator.next();
    }

    @Test(expected = RuntimeException.class)
    public void testNullThings() {
        Iterator<HtmlTitleExtraction> iterator = extractor.getExtractedFeatures(null).toBlocking().getIterator();
        iterator.next();
    }

    @Test(expected = UnknownHostException.class)
    public void testBadHost() throws Throwable {

        try {
            extractor.getExtractedFeatures(new URL("http://www.suoijwerq.com")).toBlocking().first();
        } catch(Exception e) {
            throw e.getCause();
        }

    }
}
