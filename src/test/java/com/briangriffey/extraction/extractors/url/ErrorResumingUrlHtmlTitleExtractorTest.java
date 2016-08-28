package com.briangriffey.extraction.extractors.url;

import com.briangriffey.extraction.Extraction;
import com.briangriffey.extraction.exception.ErrorExtraction;
import com.briangriffey.health.ErrorReporter;
import okhttp3.OkHttpClient;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ErrorResumingUrlHtmlTitleExtractorTest extends MockWebServerTest{

    private MockWebServer server;
    private ErrorResumingUrlHtmlTitleExtractor resumingExtractor;

    @Before
    public void setup() throws IOException {
        OkHttpClient client = new OkHttpClient();
        UrlHtmlTitleExtractor extractor  = new UrlHtmlTitleExtractor(client);
        this.resumingExtractor = new ErrorResumingUrlHtmlTitleExtractor(extractor, mock(ErrorReporter.class));

        this.server = newMockWebServer();

        // Start the server.
        server.start();
    }

    @After
    public void teardown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testEmitsError() throws MalformedURLException {
        //give it a url that you know doesn't exist
        Extraction extraction = resumingExtractor.getExtractedFeatures(new URL("http://www.aiaosdmnfqwperoiud.com")).toBlocking().first();
        assertTrue(extraction instanceof ErrorExtraction);

        ErrorExtraction errorExtraction = (ErrorExtraction)extraction;

        assertTrue(errorExtraction.getExtraction() instanceof UnknownHostException);
        assertTrue(errorExtraction.getSource().equals(UrlHtmlTitleExtractor.class));
    }

    @Test
    public void testSendsOutGoodResult() throws MalformedURLException {
        URL url = queueWootResponse(this.server);

        Iterator<Extraction> iterator = resumingExtractor.getExtractedFeatures(url).toBlocking().getIterator();
        Extraction extraction = iterator.next();
        assertTrue(extraction instanceof HtmlTitleExtraction);
        assertEquals("Woot woot woot", ((HtmlTitleExtraction)extraction).getExtraction().getTitle());
    }
}
