package com.pey.model;

/**
 * Created by Peysen on 2017/8/10.
 */
public class Articel {

    private String title;
    private String link;
    private String poster;
    private String time;
    private Integer votes;

    public Articel() {
    }

    public Articel(String title, String link, String poster, String time, Integer votes) {
        this.title = title;
        this.link = link;
        this.poster = poster;
        this.time = time;
        this.votes = votes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "Articel{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", poster='" + poster + '\'' +
                ", time='" + time + '\'' +
                ", votes='" + votes + '\'' +
                '}';
    }
}
