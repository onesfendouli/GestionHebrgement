package com.example.descovertunisia.entities;

import java.util.Date;

public class Hebergement {
    private int id ;
    private String lieu;
    private Date date;
    private float prix;
    private  String type;
    private int nbr_personne;
    private  int nbr_nuit;

    public Hebergement() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNbr_personne() {
        return nbr_personne;
    }

    public void setNbr_personne(int nbr_personne) {
        this.nbr_personne = nbr_personne;
    }

    public int getNbr_nuit() {
        return nbr_nuit;
    }

    public void setNbr_nuit(int nbr_nuit) {
        this.nbr_nuit = nbr_nuit;
    }

    @Override
    public String toString() {
        return "Hebergement{" +
                "id=" + id +
                ", lieu='" + lieu + '\'' +
                ", date=" + date +
                ", prix=" + prix +
                ", type='" + type + '\'' +
                ", nbr_personne=" + nbr_personne +
                ", nbr_nuit=" + nbr_nuit +
                '}';
    }
}
