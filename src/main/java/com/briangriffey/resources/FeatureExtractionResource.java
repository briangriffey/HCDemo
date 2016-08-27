package com.briangriffey.resources;


import com.briangriffey.extraction.CompositeTextExtractor;
import com.briangriffey.responses.featureextraction.FeatureExtractionResponse;
import com.codahale.metrics.annotation.Timed;
import okhttp3.OkHttpClient;
import rx.schedulers.Schedulers;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/extractfeatures")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.TEXT_PLAIN)
public class FeatureExtractionResource {

    private final CompositeTextExtractor extractor;

    public FeatureExtractionResource(OkHttpClient client) {
        this.extractor = new CompositeTextExtractor(client, Schedulers.io());
    }

    /**
     * Endpoint that will extract all of the features from a given item of text. Currently this uses the
     * {@link CompositeTextExtractor composite text extractor} to pull out emoticons, mentions, and html information
     * from a piece of text
     * @param text Any string that you want to extract features from
     * @return A {@link FeatureExtractionResponse Feature extraction response} that contains the extracted emoticon,
     * mentions, and html information.
     */
    @POST
    @Timed
    public FeatureExtractionResponse extractFeaturesFromText(String text) {

        if(text == null) {
            throw new IllegalArgumentException("the post body can not be null");
        }

        FeatureExtractionResponse.Builder builder = new FeatureExtractionResponse.Builder();

        extractor.getExtractedFeatures(text).toBlocking().forEach(extraction -> {
            builder.withExtraction(extraction);
        });

        FeatureExtractionResponse response = builder.build();
        return response;
    }
}
