package com.lev.accprog.ui;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class FoodX {
    private Food.TASTE taste;
    private String expirationDate;
    private String name;

    public FoodX(Food food){
        setName(food.getName());
        setTaste(food.getTaste());
        setExpirationDate(food.getExpirationDate());
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

    public void setExpirationDate(GregorianCalendar expirationDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.expirationDate = formatter.format(expirationDate.getTime());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
