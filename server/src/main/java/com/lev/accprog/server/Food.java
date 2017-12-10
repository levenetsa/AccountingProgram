package com.lev.accprog.server;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Food implements Comparable<Food> {
    private TASTE taste;
    private GregorianCalendar expirationDate;
    private String name;

    public Food() {
        this.name = "default";
        this.expirationDate = new GregorianCalendar(2000, 1, 1);
    }

    public void setDate(int year, int month, int day) {
        month--;
        this.expirationDate = new GregorianCalendar(year, month, day);
    }

    public void setDate(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        date = sdf.parse(str);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        this.expirationDate = new GregorianCalendar(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
    }

    public GregorianCalendar getExpirationDate() {
        return expirationDate;
    }

    public TASTE getTaste() {
        return taste;
    }

    public void setTaste(String taste) throws IllegalArgumentException {
        this.taste = TASTE.valueOf(taste);
    }

    public void setTaste(TASTE teaste) {
        this.taste = teaste;
    }

    public String getName() {
        return name;
    }

    public enum TASTE {
        SALTY,
        SPICY,
        SWEET
    }

    @Override
    public int compareTo(Food other) {
        return -this.getExpirationDate().compareTo(other.getExpirationDate());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Food
                && this.getExpirationDate().equals(((Food) obj).getExpirationDate())
                && this.getTaste().equals(((Food) obj).getTaste())
                && this.getName().equals(((Food) obj).getName());
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {

        JSONObject object = new JSONObject(this);

        return object.toString();
    }
}
