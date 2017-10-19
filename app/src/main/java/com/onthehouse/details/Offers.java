package com.onthehouse.details;

public class Offers {
    private String name;
    private String thumbnail;
    private String id;

    public Offers(String name, String thumbnail, String id) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.id = id;
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
}
