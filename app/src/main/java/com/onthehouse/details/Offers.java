package com.onthehouse.details;

public class Offers {
    private String name;
    private String thumbnail;
    private String id;
    private int rating;

    public Offers(String name, String thumbnail, String id, int rating) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.id = id;
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "Offers{" +
                "name='" + name + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", id='" + id + '\'' +
                ", rating=" + rating +
                '}';
    }
}
