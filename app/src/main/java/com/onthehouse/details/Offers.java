package com.onthehouse.details;

/**
 * Created by anashanifm on 7/9/17.
 */

public class Offers {
    private String name;
    private String thumbnail;

    public Offers(String name, String thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
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
