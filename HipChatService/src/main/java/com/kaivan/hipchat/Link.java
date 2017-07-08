package com.kaivan.hipchat;

/**
 * Created by kaivanwadia
 */
public class Link {
    String url;
    String title;

    public Link() {

    }

    public Link(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Link{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link link = (Link) o;

        if (!getUrl().equals(link.getUrl())) return false;
        return getTitle().equals(link.getTitle());
    }

    @Override
    public int hashCode() {
        int result = getUrl().hashCode();
        result = 31 * result + getTitle().hashCode();
        return result;
    }
}
