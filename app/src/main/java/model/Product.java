package model;

import android.media.Image;
import android.widget.ImageView;

public class Product {
    String id;
    String type;
    String description;
    String price;
    String forWhom;
    String imageProduct;

    Person seller;

        public Product(String id, String type, String description, String price, String forWhom, String imageProduct, Person seller) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.price = price;
        this.forWhom = forWhom;
        this.imageProduct = imageProduct;
        this.seller = seller;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getForWhom() {
        return forWhom;
    }

    public void setForWhom(String forWhom) {
        this.forWhom = forWhom;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
    }

    public Person getSeller() {
        return seller;
    }

    public void setSeller(Person seller) {
        this.seller = seller;
    }

}

