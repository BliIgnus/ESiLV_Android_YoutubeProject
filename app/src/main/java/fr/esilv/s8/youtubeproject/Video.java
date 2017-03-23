package fr.esilv.s8.youtubeproject;

import java.io.Serializable;

/**
 * Created by Ignus on 17/03/2017.
 */

public class Video implements Serializable {
    private String title;
    private String description;
    private String thumbnails;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private String author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String id;

    public Video(String title, String description, String thumbnails, String id, String date, String author) {
        this.title = title;
        this.description = description;
        this.thumbnails = thumbnails;
        this.id = id;
        this.date = date;
        this.author = author;
    }
}
