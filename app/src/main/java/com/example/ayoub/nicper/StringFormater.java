package com.example.ayoub.nicper;

/**
 * Created by Admin on 19/06/2016.
 */
public class StringFormater {
    public StringFormater(){}

    public String availabilityFormater(String time){
        String temp = time.substring(0, 3);
        int lastInt = Integer.parseInt(time.substring(0, 2));
        int secondInt;
        for(int i =0; i < time.length(); i = i + 6){
            secondInt = Integer.parseInt(time.substring(i+6, i+8));
            if(lastInt+1 != secondInt){
                temp += ""+(lastInt+1)+" "+ secondInt+"-";
            }else{
                lastInt = secondInt;
            }
        }
        return temp;
    }

}
