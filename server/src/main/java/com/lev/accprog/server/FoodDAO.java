package com.lev.accprog.server;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FoodDAO {

//    CREATE TABLE FOOD(
//            NAME TEXT,
//            TASTE TEXT,
//            EDATE TEXT
//    );

    public void saveFood(List<Food> foods) throws SQLException, ClassNotFoundException {
        Connection con = initConnection();
        StringBuilder sql = new StringBuilder("INSERT INTO food VALUES ");
        foods.forEach(x-> sql.append("('")
        .append(x.getName())
                .append("','").append(x.getTaste().toString()).append("','").append(getDate(x)).append("'),"));
        try {
            Statement stmt = con.createStatement();
            String sql1 = sql.substring(0, sql.length() - 1);
            stmt.executeUpdate("DELETE FROM food;");
            stmt.executeUpdate(sql1);
            stmt.close();
        } finally {
            con.close();
        }

    }

    String getDate(Food x){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return  formatter.format(x.getExpirationDate().getTime());
    }

    public List<Food> getFood() throws ClassNotFoundException, SQLException {
        Connection con = initConnection();
        List<Food> foods = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM food");
            while (rs.next()) {
                try {
                    Food food = new Food(rs.getString(1), rs.getString(2), rs.getString(3));
                    foods.add(food);
                } catch (Exception ignored) {
                }
            }
            rs.close();
            stmt.close();
        } finally {
            con.close();
        }
        return foods;
    }

    private Connection initConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/laba";
        String login = "postgres";
        String password = "Iamken4o";
        return DriverManager.getConnection(url, login, password);
    }
}

