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
    String sellerId;
    String lastUpdated;

    public Product() { }

    public Product(String id, Helper.ProductType type, String description, int price, Helper.Customers forWhom, String sellerId, Bitmap imageProduct)
     {
        this.id = id;
        this.type = type;
        this.description = description;
        this.price = price;
        this.forWhom = forWhom;
        this.imageProduct = imageProduct;
        this.sellerId = sellerId;
    }

    public String getId() { return id; }

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
}

