package com.onthehouse.details;

public class Reservation {

    private Integer id;
    private Integer event_id;
    private Integer show_id;
    private String event_name;
    private String date;
    private Integer num_tickets;
    private Integer venue_id;
    private String venue_name;
    private String type;
    private boolean can_cancel;
    private boolean can_rate;
    private boolean has_rated;

    public Reservation() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Integer event_id) {
        this.event_id = event_id;
    }

    public Integer getShow_id() {
        return show_id;
    }

    public void setShow_id(Integer show_id) {
        this.show_id = show_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getNum_tickets() {
        return num_tickets;
    }

    public void setNum_tickets(Integer num_tickets) {
        this.num_tickets = num_tickets;
    }

    public Integer getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(Integer venue_id) {
        this.venue_id = venue_id;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenue_name(String venue_name) {
        this.venue_name = venue_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isCan_cancel() {
        return can_cancel;
    }

    public void setCan_cancel(boolean can_cancel) {
        this.can_cancel = can_cancel;
    }

    public boolean isCan_rate() {
        return can_rate;
    }

    public void setCan_rate(boolean can_rate) {
        this.can_rate = can_rate;
    }

    public boolean isHas_rated() {
        return has_rated;
    }

    public void setHas_rated(boolean has_rated) {
        this.has_rated = has_rated;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", event_id=" + event_id +
                ", show_id=" + show_id +
                ", event_name='" + event_name + '\'' +
                ", date='" + date + '\'' +
                ", num_tickets=" + num_tickets +
                ", venue_id=" + venue_id +
                ", venue_name='" + venue_name + '\'' +
                ", type='" + type + '\'' +
                ", can_cancel=" + can_cancel +
                ", can_rate=" + can_rate +
                ", has_rated=" + has_rated +
                '}';
    }
}
