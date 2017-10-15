package com.onthehouse.details;

public class PastOffers {

    private Integer id;
    private String name;
    private String description;
    private Integer rating;

    public PastOffers(Integer id, String name, String description, Integer rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
    }

    public PastOffers() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
