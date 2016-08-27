package com.briangriffey.resources;

import com.briangriffey.BrianGriffeyApplication;
import com.briangriffey.BrianGriffeyConfiguration;
import com.briangriffey.responses.GsonJerseyProvider;
import com.briangriffey.responses.featureextraction.FeatureExtractionResponse;
import com.briangriffey.responses.featureextraction.UrlInfo;
import com.google.gson.Gson;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FeatureExtactionResourceIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<BrianGriffeyConfiguration> RULE =
            new DropwizardAppRule<BrianGriffeyConfiguration>(BrianGriffeyApplication.class);

    private Client client;

    @Before
    public void setup() {
        this.client = ClientBuilder.newBuilder()
                .register(new GsonJerseyProvider(new Gson()))
                .build();

        
    }
    
    @After
    public void teardown() {
        client.close();
    }

    @Test
    public void testOnRealNetwork() {
            FeatureExtractionResponse response = client.target(
                    String.format("http://localhost:%d/extractfeatures", RULE.getLocalPort()))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity("Hey @brian how (now) (brown) (cow) http://www.twitter.com. Isn't this just the best @tom. " +
                                    "I think that it is (areyoukiddingme) http://www.google.com http://reddit.com/r/programming",
                            MediaType.TEXT_PLAIN))
                    .readEntity(FeatureExtractionResponse.class);


            assertEquals(2, response.getMentions().size());
            assertEquals(4, response.getEmoticons().size());
            assertEquals(3, response.getLinks().size());

            assertTrue(response.getMentions().contains("tom"));
            assertTrue(response.getMentions().contains("brian"));

            assertTrue(response.getEmoticons().contains("now"));
            assertTrue(response.getEmoticons().contains("cow"));
            assertTrue(response.getEmoticons().contains("brown"));
            assertTrue(response.getEmoticons().contains("areyoukiddingme"));

            UrlInfo info1 = new UrlInfo("http://www.twitter.com", "Twitter - see what's happening");
            UrlInfo info2 = new UrlInfo("http://reddit.com/r/programming", "programming");
            UrlInfo info3 = new UrlInfo("http://www.google.com", "Google");

            assertTrue(response.getLinks().contains(info1));
            assertTrue(response.getLinks().contains(info2));
            assertTrue(response.getLinks().contains(info3));
    }

    @Test
    public void testBadRequestTypes() {

            int status = client.target(
                    String.format("http://localhost:%d/extractfeatures", RULE.getLocalPort()))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity("{ key:\"somevalue\"}",
                            MediaType.APPLICATION_JSON))
                    .getStatus();

            assertEquals(415, status);
    }
}
