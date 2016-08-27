package com.briangriffey.extraction;

import com.briangriffey.extraction.extractors.emoticons.EmoticonExtraction;
import com.briangriffey.extraction.extractors.mentions.MentionExtraction;
import com.briangriffey.extraction.extractors.url.HtmlTitleExtraction;
import com.briangriffey.extraction.extractors.url.MockWebServerTest;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CompositeTextExtractorTest extends MockWebServerTest{

    private CompositeTextExtractor extractor;
    private MockWebServer server;

    @Before
    public void setup() throws IOException {
        OkHttpClient client = new OkHttpClient();
        this.extractor = new CompositeTextExtractor(client, Schedulers.io());

        this.server = new MockWebServer();

        // Start the server.
        server.start();
    }

    @After
    public void teardown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testCompositeExtraction() throws MalformedURLException {
        URL url = queueWootResponse(this.server);

        String text = "Hey @brian did you get your (coffee) from " + url + " ?";

        Iterator<Extraction> extractionObservable = this.extractor.getExtractedFeatures(text).toBlocking().getIterator();
        Extraction extraction = extractionObservable.next();

        assertTrue(extraction instanceof MentionExtraction);
        MentionExtraction mention = (MentionExtraction)extraction;
        assertEquals("@brian", mention.getExtraction());

        extraction = extractionObservable.next();
        assertTrue(extraction instanceof EmoticonExtraction);
        EmoticonExtraction emoteExtraction = (EmoticonExtraction)extraction;
        assertEquals("(coffee)", emoteExtraction.getExtraction());


        extraction = extractionObservable.next();
        assertTrue(extraction instanceof HtmlTitleExtraction);
        HtmlTitleExtraction title = (HtmlTitleExtraction)extraction;
        assertEquals("Woot woot woot", title.getExtraction().getTitle());
    }

    @Test
    public void testNull() throws MalformedURLException {
        Iterator<Extraction> extractionObservable = this.extractor.getExtractedFeatures(null).toBlocking().getIterator();
        assertFalse(extractionObservable.hasNext());
    }

    @Test
    public void testEmpty() {
        Iterator<Extraction> extractionObservable = this.extractor.getExtractedFeatures("").toBlocking().getIterator();
        assertFalse(extractionObservable.hasNext());
    }
}
