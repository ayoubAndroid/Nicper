package com.example.ayoub.nicper.Object.AppGeneral;

/**
 * Created by Ayoub on 5/21/2016.
 */
public class User {
    private String email;
    private String lastName;
    private String firstName;
    private int post;

    public User(){}



    public User(String email, String lastName, String firstName, int post) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.post = post;
    }

    public int getPost() {
        return post;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPost(int post) {
        this.post = post;
    }

    /**
     * Created by Admin on 03/06/2016.
     */
    public static class LatLngApp {
        private double lat, lon;

        public LatLngApp() {
        }

        public LatLngApp(double x, double y) {
            this.lat = x;
            this.lon = y;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }
    }
}
