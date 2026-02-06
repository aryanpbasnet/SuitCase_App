package com.example.suit_case;

import android.net.Uri;

public class SuitCaseModel {
    private int id;
    private String item, description;
    private double price;
    private Uri image;
    private boolean purchased;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    @Override
    public String toString(){
        return "SuitCaseModel{" +
                "id=" + id +
                ", item='" + item + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", image=" + image +
                ", purchased=" + purchased +
                '}';
    }
}
