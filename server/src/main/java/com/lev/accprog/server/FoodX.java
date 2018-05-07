package com.lev.accprog.server;

public class FoodX {
    private Food.TASTE taste;
    private String expirationDate;
    private String name;

    public FoodX(Food food){
        setName(food.getName());
        setTaste(food.getTaste());
        setExpirationDate(food.getTime());
    }

    public Food.TASTE getTaste() {
        return taste;
    }

    public void setTaste(Food.TASTE taste) {
        this.taste = taste;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
