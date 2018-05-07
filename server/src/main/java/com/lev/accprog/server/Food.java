package com.lev.accprog.server;

import org.json.JSONObject;

import java.text.ParseException;
import java.time.LocalDateTime;

public class Food implements Comparable<Food> {
    private TASTE taste;
    private String time;
    private String name;
    private LocalDateTime created;

    public Food() {
        this.name = "default";
        this.time = "2000-01-01";
        created = LocalDateTime.now();
    }


    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public TASTE getTaste() {
        return taste;
    }

    public void setTaste(TASTE taste) {
        this.taste = taste;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Food(String name, String taste, String date) throws ParseException {
        this.name = name;
        this.taste = TASTE.valueOf(taste);
        this.setDate(date);
    }

    public void setDate(int year, int month, int day) {
        this.time = year + '-' + rround(month) + '-' + rround(day) ;
    }

    private String rround(int month) {
        return month < 10? "0" + month : month + "";
    }

    public void setDate(String str) throws ParseException {
        time = str;
    }

    public enum TASTE {
        SALTY,
        SPICY,
        SWEET
    }

    @Override
    public int compareTo(Food other) {
        return -this.getTime().compareTo(other.getTime());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Food
                && this.getTime().equals(((Food) obj).getTime())
                && this.getTaste().equals(((Food) obj).getTaste())
                && this.getName().equals(((Food) obj).getName());
    }

    @Override
    public String toString() {

        JSONObject object = new JSONObject(this);

        return object.toString();
    }
}
