package com.briangriffey.health;

import com.briangriffey.resources.FeatureExtractionResource;
import com.briangriffey.responses.featureextraction.FeatureExtractionResponse;
import com.codahale.metrics.health.HealthCheck;

/**
 * Simple health check that will run the the FeatureExtractionResource to make sure everything
 * is working well
 *
 */
public class FeatureExtractionHealthCheck extends HealthCheck{

    private final FeatureExtractionResource resource;

    public FeatureExtractionHealthCheck(FeatureExtractionResource resource) {
        this.resource = resource;
    }

    @Override
    protected Result check() throws Exception {
        FeatureExtractionResponse featureExtractionResponse = resource.extractFeaturesFromText("Check http://www.google.com");
        if(featureExtractionResponse != null
                && featureExtractionResponse.getLinks() != null
                && featureExtractionResponse.getLinks().get(0) != null
                && featureExtractionResponse.getLinks().get(0).getTitle().equals("Google")
                && featureExtractionResponse.getLinks().get(0).getUrl().equals("http://www.google.com")) {
            return Result.healthy();
        }

        return Result.unhealthy("http://www.google.com did not produce the title Google");

    }
}
