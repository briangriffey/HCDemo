package com.briangriffey.responses.featureextraction;

import com.briangriffey.extraction.exception.ErrorExtraction;
import com.briangriffey.extraction.Extraction;
import com.briangriffey.extraction.ExtractionVisitor;
import com.briangriffey.extraction.extractors.emoticons.EmoticonExtraction;
import com.briangriffey.extraction.extractors.mentions.MentionExtraction;
import com.briangriffey.extraction.extractors.url.HtmlTitleExtraction;
import com.briangriffey.extraction.extractors.url.UrlExtraction;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Service response that will match the format of
 * <p>
 * {
 * "mentions": [
 * "bob",
 * "john"
 * ],
 * "emoticons": [
 * "success"
 * ],
 * "links": [
 * {
 * "url": "https://twitter.com/jdorfman/status/430511497475670016",
 * "title": "Justin Dorfman on Twitter: &quot;nice @littlebigdetail from @HipChat (shows hex colors when pasted in chat). http://t.co/7cI6Gjy5pq&quot;"
 * }
 * ],
 * "errors": [
 *      "UrlConverter: We couldn't find the host"
 * ]
 * }
 */
public class FeatureExtractionResponse {

    private List<String> mentions;
    private List<String> emoticons;
    private List<UrlInfo> links;
    private List<String> errors;

    /**
     * Use the {@link FeatureExtractionResponse.Builder Builder class}
     */
    private FeatureExtractionResponse(List<String> mentions, List<String> emoticons, List<UrlInfo> links, List<String> errors) {
        this.mentions = Collections.unmodifiableList(mentions);
        this.emoticons = Collections.unmodifiableList(emoticons);
        this.links = Collections.unmodifiableList(links);
        this.errors = Collections.unmodifiableList(errors);
    }

    /**
     * @return An unmodifiable list of mentions
     */
    public List<String> getMentions() {
        return mentions;
    }

    /**
     * @return an unmodifiable list of emoticons
     */
    public List<String> getEmoticons() {
        return emoticons;
    }

    /**
     * @return An unmodifiable list of links
     */
    public List<UrlInfo> getLinks() {
        return links;
    }

    /**
     * @return An unmodifiable list of error descriptions
     */
    public List<String> getErrors() {
        return errors;
    }

    public static class Builder implements ExtractionVisitor{

        private List<String> mentions;
        private List<String> emoticons;
        private List<UrlInfo> links;
        private List<String> errors;

        public Builder() {
            mentions = new LinkedList<>();
            emoticons = new LinkedList<>();
            links = new LinkedList<>();
            errors = new LinkedList<>();
        }

        /**
         * Include this extraction in the response when you finally build()
         * @param extraction the extraction to include
         * @return this object, so you can chain method calls together
         */
        public Builder withExtraction(Extraction extraction) {
            if(extraction != null) {
                extraction.acceptVisitor(this);
            }
            return this;
        }

        @Override
        public void visitExtraction(UrlExtraction urlExtraction) {
            //we are not going to be doing anything with url extractions in this object
            //but we could if we wanted to
        }

        @Override
        public void visitExtraction(HtmlTitleExtraction htmlTitleExtraction) {

            if(htmlTitleExtraction == null || htmlTitleExtraction.getExtraction() == null || htmlTitleExtraction.getSource() == null) {
                return;
            }

            UrlInfo info = new UrlInfo(htmlTitleExtraction.getSource().toString(), htmlTitleExtraction.getExtraction().getTitle());
            links.add(info);
        }

        @Override
        public void visitExtraction(MentionExtraction mentionExtraction) {
            if(mentionExtraction == null) {
                return;
            }
            //add the no @ symbol version of the mention
            mentions.add(mentionExtraction.getNoAtSymbolExtraction());
        }

        @Override
        public void visitExtraction(EmoticonExtraction emoticonExtraction) {
            if(emoticonExtraction == null) {
                return;
            }
            //add the no parenthesis version of the emoticon
            emoticons.add(emoticonExtraction.getEmoticonText());
        }

        @Override
        public void visitExtraction(ErrorExtraction errorExtraction) {
            if(errorExtraction == null) {
                return;
            }

            //TODO: put fancy logic in here for errors that make sense
            String errorString = errorExtraction.getSource().getSimpleName();
            if(errorExtraction.getExtraction() instanceof UnknownHostException) {
                errorString += " Unknown Host: ";
            }

            if(errorExtraction.getExtraction().getMessage() != null) {
                errorString += ": " + errorExtraction.getExtraction().getMessage();
            }
            errors.add(errorString);
        }

        public FeatureExtractionResponse build() {
            return new FeatureExtractionResponse(mentions, emoticons, links, errors);
        }
    }
}
