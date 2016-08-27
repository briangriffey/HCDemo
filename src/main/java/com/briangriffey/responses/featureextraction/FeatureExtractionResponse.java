package com.briangriffey.responses.featureextraction;

import com.briangriffey.extraction.Extraction;
import com.briangriffey.extraction.ExtractionVisitor;
import com.briangriffey.extraction.extractors.emoticons.EmoticonExtraction;
import com.briangriffey.extraction.extractors.mentions.MentionExtraction;
import com.briangriffey.extraction.extractors.url.HtmlTitleExtraction;
import com.briangriffey.extraction.extractors.url.UrlExtraction;

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
 * ]
 * }
 */
public class FeatureExtractionResponse {

    private List<String> mentions;
    private List<String> emoticons;
    private List<UrlInfo> links;

    /**
     * Use the {@link FeatureExtractionResponse.Builder Builder class}
     */
    private FeatureExtractionResponse(List<String> mentions, List<String> emoticons, List<UrlInfo> links) {
        this.mentions = Collections.unmodifiableList(mentions);
        this.emoticons = Collections.unmodifiableList(emoticons);
        this.links = Collections.unmodifiableList(links);
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

    public static class Builder implements ExtractionVisitor{

        private List<String> mentions;
        private List<String> emoticons;
        private List<UrlInfo> links;

        public Builder() {
            mentions = new LinkedList<>();
            emoticons = new LinkedList<>();
            links = new LinkedList<>();
        }

        public Builder withExtraction(Extraction extraction) {
            if(extraction != null) {
                extraction.acceptVisitor(this);
            }
            return this;
        }

        @Override
        public void visitExtraction(UrlExtraction urlExtraction) {
            //we are not going to be doing anything with url extractions in this object
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

        public FeatureExtractionResponse build() {
            return new FeatureExtractionResponse(mentions, emoticons, links);
        }
    }
}
