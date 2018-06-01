package model;

/**
 * Created by harry on 01/06/2018.
 */
public class StatusModel {
    private String time;
    private String food;
    private int status;

    public StatusModel(String time, String food,int status) {
        this.time = time;
        this.food = food;
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public String getFood() {
        return food;
    }


    public int getStatus() {
        return status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
