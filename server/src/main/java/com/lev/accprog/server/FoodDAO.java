package com.lev.accprog.server;

import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.postgresql.util.PSQLException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;

public class FoodDAO<T> {

    private String insertStm;
    private String selectStm;
    private String deleteStm;
    private Class<T> clazz;

    public FoodDAO(Class<T> clazz) {
        this.clazz = clazz;
        createTable(clazz);
        generateStatements();
    }

    private void generateStatements() {
        insertStm = generateInsertStm();
        selectStm = generateSelectStm();
        deleteStm = generateDeleteStm();
    }

    private String generateSelectStm() {
        return "SELECT * FROM " + clazz.getSimpleName();
    }

    private String generateDeleteStm() {
        return "DELETE FROM " + clazz.getSimpleName().toLowerCase();
    }

    private String generateInsertStm() {
        StringBuilder namesBuilder = new StringBuilder(" (");
        StringBuilder valuesBuilder = new StringBuilder(" (");
        String insert = "INSERT INTO " + clazz.getSimpleName().toLowerCase();
        for (MethodName methodName : methodNames) {
            namesBuilder.append(methodName.name).append(",");
            valuesBuilder.append('%').append(methodName.name).append(",");
        }
        insert += cutSB(namesBuilder) + ") VALUES " + cutSB(valuesBuilder) + ")";
        return insert;
    }

    public List<T> getFood() throws ClassNotFoundException, SQLException {
        Connection con = initConnection();
        List<T> items = new ArrayList<>();
        DateTimeFormatter df =  DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSSSS");
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectStm);
            while (rs.next()) {
                try {
                    T item = clazz.newInstance();
                    for (MethodName methodName : methodNames) {
                        PropertyDescriptor pd;
                        if (methodName.getter.getReturnType() == String.class) {
                            pd = new PropertyDescriptor(methodName.name, clazz);
                            pd.getWriteMethod().invoke(item, rs.getString(methodName.name));
                        } else if (methodName.getter.getReturnType() == Integer.class) {
                            pd = new PropertyDescriptor(methodName.name, clazz);
                            pd.getWriteMethod().invoke(item, rs.getInt(methodName.name));
                        } else if (methodName.getter.getReturnType() == Boolean.class) {
                            pd = new PropertyDescriptor(methodName.name, clazz);
                            pd.getWriteMethod().invoke(item, rs.getBoolean(methodName.name));
                        } else if (methodName.getter.getReturnType() == Double.class) {
                            pd = new PropertyDescriptor(methodName.name, clazz);
                            pd.getWriteMethod().invoke(item, rs.getDouble(methodName.name));
                        } else if (methodName.getter.getReturnType() == Float.class) {
                            pd = new PropertyDescriptor(methodName.name, clazz);
                            pd.getWriteMethod().invoke(item, rs.getFloat(methodName.name));
                        } else if (methodName.getter.getReturnType() == Long.class) {
                            pd = new PropertyDescriptor(methodName.name, clazz);
                            pd.getWriteMethod().invoke(item, rs.getLong(methodName.name));
                        } else if (methodName.getter.getReturnType() == Food.TASTE.class) {
                            pd = new PropertyDescriptor(methodName.name, clazz);
                            pd.getWriteMethod().invoke(item, Food.TASTE.valueOf(rs.getString(methodName.name)));
                        }else if (methodName.getter.getReturnType() == LocalDateTime.class) {
                            pd = new PropertyDescriptor(methodName.name, clazz);
                            pd.getWriteMethod().invoke(item, LocalDateTime.parse(rs.getString(methodName.name), df));
                        }
                    }
                    items.add(item);
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
            rs.close();
            stmt.close();
        } finally {
            con.close();
        }
        return items;
    }

    private Connection initConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/laba";
        String login = "postgres";
        String password = "Iamken4o";
        return DriverManager.getConnection(url, login, password);
    }

    private class MethodName {
        Method getter;
        String name;

        public MethodName(Method getter, String s) {
            this.getter = getter;
            name = s;
        }
    }

    private ArrayList<MethodName> methodNames = new ArrayList<>();

    private void createTable(Class<T> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS ");
        stringBuilder.append(clazz.getSimpleName().toLowerCase());
        stringBuilder.append(" (");
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            Method getter = null;
            try {
                getter = clazz.getMethod("get" + firstUpperCase(field.getName()));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            String fieldName = field.getName().toLowerCase();
            methodNames.add(new MethodName(getter, fieldName));
            stringBuilder.append(fieldName).append(" ");
            String type = "text";
            if (field.getType() == String.class) {
                type = "text";
            } else if (field.getType() == Integer.class) {
                type = "integer";
            } else if (field.getType() == Boolean.class) {
                type = "boolean";
            } else if (field.getType() == Double.class) {
                type = "double precision";
            } else if (field.getType() == Float.class) {
                type = "real";
            } else if (field.getType() == Long.class) {
                type = "bigint";
            } else if (field.getType() == Food.TASTE.class) {
                type = "text";
            } else if (field.getType() == LocalDateTime.class){
                type = "text";
            }
            stringBuilder.append(type).append(',');
        });
        String build = cutSB(stringBuilder) + ")";
        query(build);
    }

    private String cutSB(StringBuilder stringBuilder) {
        return stringBuilder.substring(0, stringBuilder.toString().length() - 1);
    }

    private void query(String query) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = initConnection();
            statement = connection.createStatement();
            statement.execute(query);
        } catch (PSQLException e) {
            System.out.println("Сделана");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void add(T item) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, SQLException {
        String st = new String(insertStm);
        DateTimeFormatter df =  DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSSSS");
        for (MethodName methodName : methodNames) {
            try {
                if (methodName.getter.getReturnType() == String.class) {
                    st = st.replace('%' + methodName.name, "'" + (String) methodName.getter.invoke(item) + "'");
                } else if (methodName.getter.getReturnType() == Boolean.class) {
                    st = st.replace('%' + methodName.name, ((Boolean) methodName.getter.invoke(item)) ? "1" : "0");
                } else if (methodName.getter.getReturnType() == Integer.class) {
                    st = st.replace('%' + methodName.name, methodName.getter.invoke(item).toString());
                } else if (methodName.getter.getReturnType() == Double.class) {
                    st = st.replace('%' + methodName.name, methodName.getter.invoke(item).toString());
                } else if (methodName.getter.getReturnType() == Float.class) {
                    st = st.replace('%' + methodName.name, methodName.getter.invoke(item).toString());
                } else if (methodName.getter.getReturnType() == Long.class) {
                    st = st.replace('%' + methodName.name, methodName.getter.invoke(item).toString());
                } else if (methodName.getter.getReturnType() == Food.TASTE.class) {
                    st = st.replace('%' + methodName.name, "'" + ((Food.TASTE) methodName.getter.invoke(item)).name() + "'");
                } else if (methodName.getter.getReturnType() == LocalDateTime.class) {
                    st = st.replace('%' + methodName.name, "'" + ((LocalDateTime) methodName.getter.invoke(item)).format(df) + "'");
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        query(st);
    }

    public void remove() throws SQLException, ClassNotFoundException {
       /* Connection con = initConnection();
        Statement statement = con.createStatement();*/
        query(deleteStm);
     /*   statement.close();
        con.close();*/
    }

    public void addAll(List<T> foods) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        remove();
        foods.addAll(getFood());

        for (T food : foods) {
            add(food);
        }
    }

    public String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}

