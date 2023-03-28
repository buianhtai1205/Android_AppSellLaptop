package com.appbanlaptop.model;

public class Laptop {
    private int id;
    private String name;
    private String screen;
    private String cpu;
    private String card;
    private String ram;
    private String rom;
    private String pin;
    private float weight;
    private String os;
    private String connector;
    private int price;
    private int sale_price;
    private String special;
    private int year_launch;
    private String image_url;
    private String description;
    private Brand brand;

    public Laptop(int id, String name, String screen, String cpu, String card, String ram, String rom, String pin, float weight, String os, String connector, int price, int sale_price, String special, int year_launch, String image_url, String description, Brand brand) {
        this.id = id;
        this.name = name;
        this.screen = screen;
        this.cpu = cpu;
        this.card = card;
        this.ram = ram;
        this.rom = rom;
        this.pin = pin;
        this.weight = weight;
        this.os = os;
        this.connector = connector;
        this.price = price;
        this.sale_price = sale_price;
        this.special = special;
        this.year_launch = year_launch;
        this.image_url = image_url;
        this.description = description;
        this.brand = brand;
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

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getRom() {
        return rom;
    }

    public void setRom(String rom) {
        this.rom = rom;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSale_price() {
        return sale_price;
    }

    public void setSale_price(int sale_price) {
        this.sale_price = sale_price;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public int getYear_launch() {
        return year_launch;
    }

    public void setYear_launch(int year_launch) {
        this.year_launch = year_launch;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
