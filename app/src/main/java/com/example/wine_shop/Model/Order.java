package com.example.wine_shop.Model;

public class Order {
    private String address;
    private String date;
    private String status;
    private String time;
    private String totalAmount;

    public Order() {
        // Пустой конструктор необходим для Firebase
    }

    public Order(String address, String date, String status, String time, String totalAmount) {
        this.address = address;
        this.date = date;
        this.status = status;
        this.time = time;
        this.totalAmount = totalAmount;
    }

    // Геттеры и сеттеры для всех полей
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}

