package com.talkramer.finalproject.model.Domain;

import android.graphics.Bitmap;

import com.talkramer.finalproject.model.Utils.Helper;

/**
 * Created by Yaniv on 08/08/2016.
 */
public class ProductWrapper {
    String id;
    Helper.ProductType type;
    String description;
    int price;
    Helper.Customers forWhom;
    String sellerId, sellerEmail;
    String buyerEmail;
    String lastUpdated;
    boolean deleted;


    public ProductWrapper() { }

    public ProductWrapper(Product product)
    {
        this.id = product.getId();
        this.type = product.getType();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.forWhom = product.getForWhom();
        this.sellerId = product.getSellerId();
        this.lastUpdated = product.getLastUpdated();
        this.deleted = product.getDeleted();
        this.sellerEmail = product.getSellerEmail();
        this.buyerEmail = product.getBuyerEmail();
    }

    public String getId() { return id; }

    public Helper.ProductType getType() { return type; }

    public String getDescription() { return description; }

    public int getPrice() { return price; }

    public Helper.Customers getForWhom() { return forWhom; }

    public String getSellerId() { return sellerId; }

    public void setType(Helper.ProductType type) { this.type = type; }

    public void setDescription(String description) { this.description = description; }

    public void setPrice(int price) { this.price = price; }

    public void setForWhom(Helper.Customers forWhom) { this.forWhom = forWhom; }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean getDeleted() {return deleted;}

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getSellerEmail() { return sellerEmail; }

    public String getBuyerEmail() {return buyerEmail;}
}
