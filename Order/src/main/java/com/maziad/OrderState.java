package com.maziad;

public class OrderState {
    private OrderStatus status;
    private String user;

    private OrderState() {
    }

    public OrderState(OrderStatus status) {
        this.status = status;
    }

    public OrderState(String user, OrderStatus status) {
        this.user = user;
        this.status = status;
    }

    public OrderStatus getStatus() {
        return this.status;
    }


    public String getUser() {
        return this.user;
    }

    @Override
    public String toString() {
        return "Order Status for user: " + user +" is: " + status;
    }
}
