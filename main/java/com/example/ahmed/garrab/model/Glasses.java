package com.example.ahmed.garrab.model;

import java.io.Serializable;

/**
 * Created by ESC on 6/18/2016.
 */
public class Glasses implements Serializable {

    private int id;
    private String url;
    private String modelName;
    private Double price;
    private String brand;

    // Constructors
    public Glasses() {
        this.id = 0;
        this.url = "";
        this.modelName = "";
        this.price = 0.0;
        this.brand = "";
    }

    public Glasses(int id, String url, String modelName, Double price, String brand) {
        this.id = id;
        this.url = url;
        this.modelName = modelName;
        this.price = price;
        this.brand = brand;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}


