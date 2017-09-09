package com.onthehouse.details;

/**
 * Created by anashanifm on 7/9/17.
 */

public class Offers {

    public Offers(String name, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    private String name;
    private int thumbnail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
