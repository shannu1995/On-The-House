package com.onthehouse.newUI;

public class Events {

    private int id;
    private String type;
    private String name;
    private int rating;
    private String image_url;
    private String description;
    private double price_from;
    private double price_to;
    private String full_price_string;
    private String our_price_string;
    private String our_price_heading;
    private String membership_levels;
    private boolean sold_out;
    private boolean coming_soon;
    private boolean is_competition;

    public Events() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice_from() {
        return price_from;
    }

    public void setPrice_from(double price_from) {
        this.price_from = price_from;
    }

    public double getPrice_to() {
        return price_to;
    }

    public void setPrice_to(double price_to) {
        this.price_to = price_to;
    }

    public String getFull_price_string() {
        return full_price_string;
    }

    public void setFull_price_string(String full_price_string) {
        this.full_price_string = full_price_string;
    }

    public String getOur_price_string() {
        return our_price_string;
    }

    public void setOur_price_string(String our_price_string) {
        this.our_price_string = our_price_string;
    }

    public String getOur_price_heading() {
        return our_price_heading;
    }

    public void setOur_price_heading(String our_price_heading) {
        this.our_price_heading = our_price_heading;
    }

    public String getMembership_levels() {
        return membership_levels;
    }

    public void setMembership_levels(String membership_levels) {
        this.membership_levels = membership_levels;
    }

    public boolean isSold_out() {
        return sold_out;
    }

    public void setSold_out(boolean sold_out) {
        this.sold_out = sold_out;
    }

    public boolean isComing_soon() {
        return coming_soon;
    }

    public void setComing_soon(boolean coming_soon) {
        this.coming_soon = coming_soon;
    }

    public boolean isIs_competition() {
        return is_competition;
    }

    public void setIs_competition(boolean is_competition) {
        this.is_competition = is_competition;
    }

    @Override
    public String toString() {
        return "Events{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", image_url='" + image_url + '\'' +
                ", description='" + description + '\'' +
                ", price_from=" + price_from +
                ", price_to=" + price_to +
                ", full_price_string='" + full_price_string + '\'' +
                ", our_price_string='" + our_price_string + '\'' +
                ", our_price_heading='" + our_price_heading + '\'' +
                ", membership_levels='" + membership_levels + '\'' +
                ", sold_out=" + sold_out +
                ", coming_soon=" + coming_soon +
                ", is_competition=" + is_competition +
                '}';
    }
}
