package com.lev.accprog.server;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class FileIO {

    PriorityQueue<Food> readQueue(String path) throws FileNotFoundException {
        PriorityQueue<Food> readed = new PriorityQueue<>();
        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNext()){
            String line = scanner.nextLine();
            if (line.equals("")) continue;
            readed.add(parseFood(line));
        }
        scanner.close();
        return readed;
    }

    private Food parseFood(String line) {
        Food food = new Food();
        String[] fields = line.split(",");
        try {
            String[] date = fields[0].trim().split("-");
            food.setDate(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]));
            food.setTaste(fields[2].trim());
            food.setName(fields[1].trim());
        } catch (Exception e){
            System.out.println("Wrong input line : " + line);
        }
        return food;
    }

    void writeQueue(PriorityQueue<Food> queue, String path){
        BufferedWriter bw = null;
        StringBuilder myContent = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (Food foo : queue){
            String date = df.format(foo.getExpirationDate().getTime());
            String name = foo.getName();
            String taste = foo.getTaste().toString();
            myContent.append(date).append(", ").append(name).append(", ").append(taste).append("\r\n");
        }
        try {
            File file = new File(path);

            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(myContent.toString());
            System.out.println("Data saved to " + file.getName());

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (Exception ex) {
                System.out.println("Can not close BufferedReader" + ex);
            }
        }
    }
}
