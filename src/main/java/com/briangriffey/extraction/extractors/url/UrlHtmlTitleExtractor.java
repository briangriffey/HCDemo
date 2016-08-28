package com.briangriffey.extraction.extractors.url;

import com.briangriffey.extraction.DataFeatureExtractor;
import com.briangriffey.extraction.html.HtmlInformation;
import com.codahale.metrics.annotation.Timed;
import okhttp3.*;
import okhttp3.internal.Util;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import rx.Observable;

import java.net.URL;
import java.nio.charset.Charset;

/**
 * An extractor that will take a URI source, then extract the HTML title from the page pointed to by the uri
 */
public class UrlHtmlTitleExtractor implements DataFeatureExtractor<URL, HtmlTitleExtraction> {

    private final OkHttpClient client;

    public UrlHtmlTitleExtractor(OkHttpClient client) {
        this.client = client;
    }

    @Override
    @Timed
    public Observable<HtmlTitleExtraction> getExtractedFeatures(final URL uri) {

        //don't have anything, then just return nothing
        if(uri == null) {
            return Observable.error(new IllegalArgumentException("Uri is null for UrlHtmlTitleExtractor"));
        }

        return Observable.create(subscriber -> {

            try {
                //build an http request
                Request request = new Request.Builder()
                        .url(uri)
                        .build();

                //get the response from the server
                Response response = client.newCall(request).execute();

                //let's cheat and use jsoup to give us the title
                Document doc = Jsoup.parse(response.body().byteStream(), getCharset(response.body()).name(), response.request().url().toString());
                String title = doc.title();

                //build an object that we might expand later
                HtmlInformation information = new HtmlInformation(title);

                //finally shove the extraction through
                HtmlTitleExtraction extraction = new HtmlTitleExtraction(uri, information);
                subscriber.onNext(extraction);
                subscriber.onCompleted();

            } catch (Throwable e) {
                subscriber.onError(e);
            }

        });
    }

    //for some reason they don't expose the charset function to the outside world, so I had to copy and paste it
    //from inside of okhttp
    private Charset getCharset(ResponseBody body) {
        MediaType contentType = body.contentType();
        return contentType != null ? contentType.charset(Util.UTF_8) : Util.UTF_8;
    }
}
