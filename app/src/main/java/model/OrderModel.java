package model;

import java.io.Serializable;

/**
 * Created by HH on 1/9/2018.
 */

public class OrderModel {
    private String foodName;
    private double foodPrice;
    private int quantity;
    private double amount;
    public OrderModel(String foodName, double foodPrice, int quantity){
        this.foodName=foodName;
        this.foodPrice=foodPrice;
        this.quantity=quantity;
        this.amount=foodPrice*quantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }
}
