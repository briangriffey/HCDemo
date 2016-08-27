package com.briangriffey;

import com.briangriffey.health.FeatureExtractionHealthCheck;
import com.briangriffey.resources.FeatureExtractionResource;
import com.briangriffey.responses.GsonJerseyProvider;
import com.google.gson.Gson;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import okhttp3.OkHttpClient;

public class BrianGriffeyApplication extends Application<BrianGriffeyConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BrianGriffeyApplication().run(args);
    }

    @Override
    public String getName() {
        return "Brian Griffey Hipchat Demo";
    }

    @Override
    public void initialize(final Bootstrap<BrianGriffeyConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final BrianGriffeyConfiguration configuration,
                    final Environment environment) {

        Gson gson = new Gson();
        environment.jersey().register(new GsonJerseyProvider(gson));

        //we can set lots of things here like timeouts and various other things
        //this is dependent on SLAs and other things. Configure them here for your outbound http requests
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        FeatureExtractionResource resource = new FeatureExtractionResource(client);

        environment.jersey().register(resource);
        environment.healthChecks().register("featureextraction", new FeatureExtractionHealthCheck(resource));
    }

}
