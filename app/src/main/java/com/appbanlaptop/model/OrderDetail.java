package com.appbanlaptop.model;

public class OrderDetail {
    private int id;
    private int order_id;
    private int laptop_id;
    private int quantity;

    public OrderDetail() {}

    public OrderDetail(int id, int order_id, int laptop_id, int quantity) {
        this.id = id;
        this.order_id = order_id;
        this.laptop_id = laptop_id;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getLaptop_id() {
        return laptop_id;
    }

    public void setLaptop_id(int laptop_id) {
        this.laptop_id = laptop_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
