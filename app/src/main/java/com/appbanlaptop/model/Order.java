package com.appbanlaptop.model;

import java.util.List;

public class Order {
    private int id;
    private int user_id;
    private String name_receive;
    private String address_receive;
    private String phone_receive;
    private String message;
    private int total_price;
    private List<OrderDetail> orderDetails;

    public Order(int id, int user_id, String name_receive, String address_receive, String phone_receive, String message, int total_price, List<OrderDetail> orderDetails) {
        this.id = id;
        this.user_id = user_id;
        this.name_receive = name_receive;
        this.address_receive = address_receive;
        this.phone_receive = phone_receive;
        this.message = message;
        this.total_price = total_price;
        this.orderDetails = orderDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName_receive() {
        return name_receive;
    }

    public void setName_receive(String name_receive) {
        this.name_receive = name_receive;
    }

    public String getAddress_receive() {
        return address_receive;
    }

    public void setAddress_receive(String address_receive) {
        this.address_receive = address_receive;
    }

    public String getPhone_receive() {
        return phone_receive;
    }

    public void setPhone_receive(String phone_receive) {
        this.phone_receive = phone_receive;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
