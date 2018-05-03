package model;

/**
 * Created by bathu on 11/25/2017.
 */

public class RestaurantModel {
    private int resId;
    private String resName;
    private String address;
    private String description;
    private String resImage;

    public RestaurantModel(int resId, String resName, String address, String description, String resImage) {
        this.resId = resId;
        this.resName = resName;
        this.address = address;
        this.description = description;
        this.resImage = resImage;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResImage() {
        return resImage;
    }

    public void setResImage(String resImage) {
        this.resImage = resImage;
    }
}
