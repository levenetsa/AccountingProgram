package com.lev.accprog.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class DBConnector
{
    public static void main(String[] args) {
        DBConnector m = new DBConnector();
        m.testDatabase();
    }

    private void testDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/laba";
            String login = "postgres";
            String password = "Iamken4o";
            Connection con = DriverManager.getConnection(url, login, password);
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM food");
                while (rs.next()) {
                    String str = rs.getString(2);
                     System.out.println("taste:" + str);
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

