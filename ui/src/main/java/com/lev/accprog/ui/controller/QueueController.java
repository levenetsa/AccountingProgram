package com.lev.accprog.ui.controller;

import com.lev.accprog.ui.Food;
import com.lev.accprog.ui.FoodX;
import com.lev.accprog.ui.HttpClient;
import com.lev.accprog.ui.view.ConfirmCallback;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class QueueController {

    private HttpClient mHttpClient;

    public QueueController(){
        mHttpClient = new HttpClient();
    }

    public void handleCommand(String s, Food food, ConfirmCallback callback) {
        if (food != null)
            s += " " + new JSONObject(new FoodX(food)).toString();
        String message = s;
        System.out.println(message);
        mHttpClient.write(message);
        try {
            String response = mHttpClient.read();
            if (s.trim().equals("info")){
                callback.onConfirm(response, null);
            } else {
                callback.onConfirm(response, parseResponse(response));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Food> parseResponse(String response) {
        JSONArray jsonArray = new JSONArray(response);
        ArrayList<Food> foods = new ArrayList<>();
        jsonArray.forEach(o -> {
            try {
                foods.add(castToFood((JSONObject) o));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return foods;
    }

    private Food castToFood(JSONObject parser) throws ParseException {
        Food res = new Food();
        res.setDate(parser.getString("expirationDate"));
        res.setName(parser.getString("name"));
        res.setTaste(Food.TASTE.valueOf(parser.getString("taste")));
        res.setCreated(parser.getString("created"));
        return res;
    }
}
