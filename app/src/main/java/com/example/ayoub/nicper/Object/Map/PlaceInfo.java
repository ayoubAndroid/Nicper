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
    private String priceInfo;
    private String timeInfo;
    private List<String> listDay;
    private List<String> listTime;
    private double lat, lng;
    private String username;
    private String userId;

    public PlaceInfo() {
    }



    public PlaceInfo(double priceHour, String priceInfo, String timeInfo, List<String> listDay, List<String> listTime, double lat, double lng,
                     String username, String userId) {
        this.priceHour = priceHour;
        this.priceInfo = priceInfo;
        this.timeInfo = timeInfo;
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

    public String getPriceInfo() {
        return priceInfo;
    }

    public String getTimeInfo() {
        return timeInfo;
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
}
