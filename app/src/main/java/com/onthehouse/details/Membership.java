package com.onthehouse.details;

public class Membership {

    private int id;
    private int membership_level_id;
    private int member_id;
    private long date_expires;
    private double price;
    private String paypayl_data;
    private String membership_level_name;
    private long date_created;

    public Membership() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMembership_level_id() {
        return membership_level_id;
    }

    public void setMembership_level_id(int membership_level_id) {
        this.membership_level_id = membership_level_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public long getDate_expires() {
        return date_expires;
    }

    public void setDate_expires(long date_expires) {
        this.date_expires = date_expires;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPaypayl_data() {
        return paypayl_data;
    }

    public void setPaypayl_data(String paypayl_data) {
        this.paypayl_data = paypayl_data;
    }

    public String getMembership_level_name() {
        return membership_level_name;
    }

    public void setMembership_level_name(String membership_level_name) {
        this.membership_level_name = membership_level_name;
    }

    public long getDate_created() {
        return date_created;
    }

    public void setDate_created(long date_created) {
        this.date_created = date_created;
    }

    @Override
    public String toString() {
        return "Membership{" +
                "id=" + id +
                ", membership_level_id=" + membership_level_id +
                ", member_id=" + member_id +
                ", date_expires=" + date_expires +
                ", price=" + price +
                ", paypayl_data='" + paypayl_data + '\'' +
                ", membership_level_name='" + membership_level_name + '\'' +
                ", date_created=" + date_created +
                '}';
    }
}
