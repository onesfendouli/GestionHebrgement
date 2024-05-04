package com.example.descovertunisia.entities;

import java.util.Date;

public class Reservation {
    private int id;
    private String destination;
    private Date check_in;
    private Date check_out;
    private int rooms;
    private int adults;
    private int children;
    private String email;
    private String phone;
    private int hebergement_id;

    public Reservation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getCheck_in() {
        return check_in;
    }

    public void setCheck_in(Date check_in) {
        this.check_in = check_in;
    }

    public Date getCheck_out() {
        return check_out;
    }

    public void setCheck_out(Date check_out) {
        this.check_out = check_out;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getHebergement_id() {
        return hebergement_id;
    }

    public void setHebergement_id(int hebergement_id) {
        this.hebergement_id = hebergement_id;
    }

    @Override
    public String toString() {
        return "reservation{" +
                "id=" + id +
                ", destination='" + destination + '\'' +
                ", check_in=" + check_in +
                ", check_out=" + check_out +
                ", rooms=" + rooms +
                ", adults=" + adults +
                ", children=" + children +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", hebergement_id=" + hebergement_id +
                '}';
    }
}
