package com.onthehouse.details;

public class MembershipLevels {

    private int id;
    private String name;
    private int level;
    private double price;
    private int duration_type;
    private int duration_amount;
    private String image;
    private int email_duration_type;
    private int email_duration_amount;
    private int no_offering_admin_fee;
    private int status;
    private String description;
    private int no_offering_fee;

    public MembershipLevels() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration_type() {
        return duration_type;
    }

    public void setDuration_type(int duration_type) {
        this.duration_type = duration_type;
    }

    public int getDuration_amount() {
        return duration_amount;
    }

    public void setDuration_amount(int duration_amount) {
        this.duration_amount = duration_amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getEmail_duration_type() {
        return email_duration_type;
    }

    public void setEmail_duration_type(int email_duration_type) {
        this.email_duration_type = email_duration_type;
    }

    public int getEmail_duration_amount() {
        return email_duration_amount;
    }

    public void setEmail_duration_amount(int email_duration_amount) {
        this.email_duration_amount = email_duration_amount;
    }

    public int getNo_offering_admin_fee() {
        return no_offering_admin_fee;
    }

    public void setNo_offering_admin_fee(int no_offering_admin_fee) {
        this.no_offering_admin_fee = no_offering_admin_fee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNo_offering_fee() {
        return no_offering_fee;
    }

    public void setNo_offering_fee(int no_offering_fee) {
        this.no_offering_fee = no_offering_fee;
    }
}
