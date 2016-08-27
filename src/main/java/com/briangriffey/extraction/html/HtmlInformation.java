package com.briangriffey.extraction.html;

/**
 * Object that records attrbites of an html page. Currently this just holds a title because that's all the challenge
 * called for, but you can image that you could easily record more things about an html page
 */
public class HtmlInformation {

    private final String title;

    public HtmlInformation(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HtmlInformation that = (HtmlInformation) o;

        return title != null ? title.equals(that.title) : that.title == null;

    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
}
