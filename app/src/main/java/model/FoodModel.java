package model;

public class FoodModel {
    private int foodId;
    private String foodName;
    private double price;
    private String image;
    private int restaurant_resId;

    public FoodModel(int foodId, String foodName, double price, String image, int restaurant_resId) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
        this.image = image;
        this.restaurant_resId = restaurant_resId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRestaurant_resId() {
        return restaurant_resId;
    }

    public void setRestaurant_resId(int restaurant_resId) {
        this.restaurant_resId = restaurant_resId;
    }
}
