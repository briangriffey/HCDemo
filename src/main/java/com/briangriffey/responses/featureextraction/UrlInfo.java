package com.briangriffey.responses.featureextraction;

/**
 * Created by briangriffey on 8/27/16.
 */
public class UrlInfo {

    private final String url;
    private final String title;

    public UrlInfo(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrlInfo urlInfo = (UrlInfo) o;

        if (url != null ? !url.equals(urlInfo.url) : urlInfo.url != null) return false;
        return title != null ? title.equals(urlInfo.title) : urlInfo.title == null;

    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
