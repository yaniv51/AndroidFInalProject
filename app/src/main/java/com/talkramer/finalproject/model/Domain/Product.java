package com.talkramer.finalproject.model.Domain;

import android.graphics.Bitmap;

import com.talkramer.finalproject.model.Utils.Helper;

public class Product {

    String id;
    Helper.ProductType type;
    String description;
    int price;
    Helper.Customers forWhom;
    Bitmap imageProduct;
    String sellerId, sellerEmail;
    String buyerEmail;
    String lastUpdated;
    boolean deleted;

    public Product() { }

    public Product(String id, Helper.ProductType type, String description, int price, Helper.Customers forWhom, String sellerId, String sellerEmail, Bitmap imageProduct)
     {
        this.id = id;
        this.type = type;
        this.description = description;
        this.price = price;
        this.forWhom = forWhom;
        this.imageProduct = imageProduct;
        this.sellerId = sellerId;
        this.sellerEmail = sellerEmail;
        this.buyerEmail ="";
        deleted = false;
    }

    public String getId() { return id; }

    public void setId(String newId)
    {
        this.id = newId;
    }

    public Helper.ProductType getType() { return type; }

    public String getDescription() { return description; }

    public int getPrice() { return price; }

    public Helper.Customers getForWhom() { return forWhom; }

    public Bitmap getImageProduct(){return imageProduct;}

    public String getSellerId() { return sellerId; }

    public void setType(Helper.ProductType type) { this.type = type; }

    public void setDescription(String description) { this.description = description; }

    public void setPrice(int price) { this.price = price; }

    public void setForWhom(Helper.Customers forWhom) { this.forWhom = forWhom; }

    public void setImageProduct(Bitmap imageProduct) {this.imageProduct = imageProduct;}

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean getDeleted() {return deleted;}

    public void setDeleted(boolean deleted){
        this.deleted = deleted;
    }

    public String getSellerEmail() { return sellerEmail; }

    public String getBuyerEmail() {return  buyerEmail;}

    public void setBuyerEmail(String mail)
    {
        buyerEmail = mail;
    }
}

