package com.briangriffey.extraction.extractors.url;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.net.MalformedURLException;
import java.net.URL;

public class MockWebServerTest {

    /**
     * Create a new mock web server. This is a centralized places to create one since we might need to set a different
     * port for all of our tests
     * @return
     */
    public MockWebServer newMockWebServer() {
        return new MockWebServer();
    }

    /**
     * Queues a response that will have a title of Woot woot woot
     * @param server the mock web server to use
     * @return the URL location of the mock web server
     * @throws MalformedURLException
     */
    public URL queueWootResponse(MockWebServer server) throws MalformedURLException {
        server.enqueue(new MockResponse().setBody("<!DOCTYPE html>\n" +
                "<html>" +
                "<title>Woot woot woot</title>\n" +
                "<body>\n" +
                "\n" +
                "<h1>My First Heading</h1>\n" +
                "\n" +
                "<p>My first paragraph.</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>"));

        return new URL(server.url("/amazing.html").toString());
    }

    public URL queue404Response(MockWebServer server) throws MalformedURLException {
        server.enqueue(new MockResponse().setResponseCode(404));
        return new URL(server.url("/amazing.html").toString());

    }
}
