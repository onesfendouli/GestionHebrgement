package com.example.descovertunisia.entities;

public class Adresse {
    private String Latitude;
    private String Country;
    private String State_District;
    private String City;
    private String County;
    private String State;

    public Adresse() {
    }

    public Adresse(String latitude, String country, String state_District, String city, String county, String state) {
        Latitude = latitude;
        Country = country;
        State_District = state_District;
        City = city;
        County = county;
        State = state;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getState_District() {
        return State_District;
    }

    public void setState_District(String state_District) {
        State_District = state_District;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    @Override
    public String toString() {
        return
                "Latitude :" + Latitude + '\'' +
                " Country :" + Country + '\n' +
                "State_District :" + State_District + '\'' +
                " City :" + City + '\n' +
                "County :" + County + '\'' +
                " State :" + State + '\'' ;
    }
}
