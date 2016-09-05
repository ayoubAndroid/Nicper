package com.example.ayoub.nicper.Object.Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Admin on 02/06/2016.
 */
@SuppressWarnings("serial")
public class PlaceInfo implements Serializable {
    private double priceHour;
    private String info;
    private List<String> listDay;
    private List<String> listTime;
    private double lat, lng;
    private String username;
    private String userId;

    public PlaceInfo() {
    }



    public PlaceInfo(double priceHour, String info, List<String> listDay, List<String> listTime, double lat, double lng,
                     String username, String userId) {
        this.priceHour = priceHour;
        this.info = info;
        this.lat = lat;
        this.lng = lng;
        this.listDay = new ArrayList<>(listDay);
        this.listTime = new ArrayList<>(listTime);
        this.userId = userId;
        this.username = username;

    }




    public List<String> getListDay() {
        return listDay;
    }

    public List<String> getListTime() {
        return listTime;
    }

    public double getPriceHour() {
        return priceHour;
    }

    public String getinfo() {
        return info;
    }



    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public void setPriceHour(double priceHour) {
        this.priceHour = priceHour;
    }

    public void setinfo(String info) {
        this.info = info;
    }

    public void setListDay(List<String> listDay) {
        this.listDay = listDay;
    }

    public void setListTime(List<String> listTime) {
        this.listTime = listTime;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
