package com.appbanlaptop.model;

public class Brand {
    int id;
    String name;
    String address;
    String image_url;

    public Brand(int id, String name, String address, String image_url) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.image_url = image_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
