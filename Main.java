package com.company;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException,  JSONException {
        URL url = new URL("https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responsecode = conn.getResponseCode();
        String inline;
        //Check status response code
        if(responsecode !=200) {
            throw new RuntimeException("HttpResponseCode:"+responsecode);
        }
        else {
            Scanner sc= new Scanner(url.openStream());
             inline = "";
             //Getting JSON Data in String
            while(sc.hasNext())
            {
                inline+=sc.nextLine();
            }
            sc.close();
        }
        //contain whole json file
        JSONObject object = new JSONObject(inline);

        // making hashmap to store index of each date
        HashMap<String,Integer> hashMap = new HashMap<>();
        JSONArray properties = object.getJSONArray("list");
        for (int i = 0; i <properties.length() ; i++) {
            JSONObject firstFeature = properties.getJSONObject(i);
            String date = firstFeature.getString("dt_txt");
            //making date as key and array index as value
            hashMap.put(date, i);
        }

        Scanner scanner = new Scanner(System.in);

        //Variable to check is User want o Terminate Program or not
        boolean terminate = false;

      do{
          System.out.println("1. Get weather\n" +
                    "2. Get Wind Speed\n" +
                    "3. Get Pressure\n" +
                    "0. Exit");

          //Get User Input For Weather / Wind Speed / Pressure
          int request;
       do{
           request = scanner.nextInt();
        if(request==0)System.exit(0);

       else if(request<0 || request>3)
        {
            System.out.println("Enter a valid option");
        }}
       //Validate if input is a valid request
       while (request<0 || request>3);

       // Ask for Timestamp for which User want Data
        System.out.println("Enter the date and Hour of day for which you want to know\n"+
                "(Date & time format : YYYY-MM-DD HH:00:00)\n"+
                "Data availiable from:- 2019-03-27 18:00:00 to 2019-03-31 17:00:00");

        scanner.nextLine();
        String timeStamp;
       do{
           timeStamp = scanner.nextLine();
       if(hashMap.get(timeStamp)==null)
           System.out.println("Enter a valid date format");
       }
       //Validate if TimeStamp format entered is correct or not
        while(hashMap.get(timeStamp)==null);

        JSONObject firstFeature = properties.getJSONObject(hashMap.get(timeStamp));
        switch (request) {

          case 1:
              JSONArray weather = firstFeature.getJSONArray("weather");
              JSONObject weatherdetail = weather.getJSONObject(0);
              String moosam = weatherdetail.getString("main");
              System.out.println("weather is : " +moosam +"\n");
              break;

          case 2 :
              JSONObject wind = firstFeature.getJSONObject("wind");
              Integer speed = wind.getInt("speed");
              System.out.println("Wind speed is: "+speed+"\n");
              break;

            case 3:
                JSONObject main = firstFeature.getJSONObject("main");
                Integer pressure = main.getInt("pressure");
                System.out.println("pressure is : "+pressure +"\n");
                break;

            default:
                System.out.println("wrong input");
      } }
      while (terminate==false);
    }
}

