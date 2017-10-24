package com.onthehouse.details;

import org.json.JSONArray;

import java.util.ArrayList;

public class OfferDetail
{
    private static ArrayList<OfferDetail> instance = null;

    private int offerId;
    private String type;
    private String name;
    private String pageTitle;
    private int rating;
    private String imageURL;
    private String description;
    private double priceFrom;
    private double priceTo;
    private String fullPrice;
    private String ourPrice;
    private String ourPriceHeading;
    private String memberShipLevel;
    private boolean soldOut;
    private boolean comingSoon;
    private boolean isCompt;
    private String venueDetails;
    private String showsHeading;
   // private ArrayList<String> dates;
    private boolean delivery;
    private String question;
    private ArrayList<Integer> quantities;
    private String show_id;
    private JSONArray showsArray;

    public OfferDetail()
    {

    }

    public static ArrayList<OfferDetail> getInstance()
    {
        if(instance == null)
        {
            instance = new ArrayList< OfferDetail>();
        }

        return instance;
    }

    public static void clearInstance()
    {
        if(instance != null)
            instance.clear();
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
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

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(String fullPrice) {
        this.fullPrice = fullPrice;
    }

    public String getOurPrice() {
        return ourPrice;
    }

    public void setOurPrice(String ourPrice) {
        this.ourPrice = ourPrice;
    }

    public String getOurPriceHeading() {
        return ourPriceHeading;
    }

    public void setOurPriceHeading(String ourPriceHeading) {
        this.ourPriceHeading = ourPriceHeading;
    }

    public String getMemberShipLevel() {
        return memberShipLevel;
    }

    public void setMemberShipLevel(String memberShipLevel) {
        this.memberShipLevel = memberShipLevel;
    }

    public boolean isSoldOut() {
        return soldOut;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
    }

    public boolean isComingSoon() {
        return comingSoon;
    }

    public void setComingSoon(boolean comingSoon) {
        this.comingSoon = comingSoon;
    }

    public boolean isCompt() {
        return isCompt;
    }

    public void setCompt(boolean compt) {
        isCompt = compt;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(double priceFrom) {
        this.priceFrom = priceFrom;
    }

    public double getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(double priceTo) {
        this.priceTo = priceTo;
    }

    public void setVenueDetails(String venueDetails){this.venueDetails = venueDetails;}
    public String getVenueDetails(){return this.venueDetails;}

    public void setShowsHeading(String showsHeading){this.showsHeading = showsHeading;}
    public String getShowsHeading(){return this.showsHeading;}

    //public void setDates(ArrayList<String> dates){this.dates = dates;}
    //public ArrayList<String> getDates(){return this.dates;}

    public void setDelivery(boolean delivery){this.delivery = delivery;}
    public boolean isDelivery(){return this.delivery;}

    public void setQuestion(String question){this.question = question;}
    public String getQuestion(){return this.question;}

    public void setQuantities(ArrayList<Integer> quantities){this.quantities = quantities;}
    public ArrayList<Integer> getQuantities(){return this.quantities;}

    public void setShow_id(String show_id){this.show_id = show_id;}
    public String getShow_id(){return this.show_id;}

    public void setShowsArray(JSONArray showsArray){
        this.showsArray = showsArray;
    }
    public JSONArray getShowsArray(){
        return this.showsArray;
    }
}
