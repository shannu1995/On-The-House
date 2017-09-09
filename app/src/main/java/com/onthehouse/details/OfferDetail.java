package com.onthehouse.details;

import java.util.ArrayList;

/**
 * Created by haseebjehangir on 7/9/17.
 */

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
}
