package com.example.descovertunisia.services;

import com.example.descovertunisia.entities.Hebergement;
import com.example.descovertunisia.utils.mydatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HebergementServices implements Iservice<Hebergement>{
    private Connection connection;

    public HebergementServices() {
        connection = mydatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Hebergement hebergement) throws SQLException {
        String req = "INSERT INTO hebergement (lieu, date, prix, type, nbr_personne, nbr_nuit) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement os = connection.prepareStatement(req);
        os.setString(1, hebergement.getLieu());
        os.setDate(2, new java.sql.Date(hebergement.getDate().getTime()));
        os.setFloat(3, hebergement.getPrix());
        os.setString(4, hebergement.getType());
        os.setInt(5, hebergement.getNbr_personne());
        os.setInt(6, hebergement.getNbr_nuit());
        os.executeUpdate();
        System.out.println("Hébergement ajouté avec succès");
    }

    @Override
    public void modifier(Hebergement hebergement) throws SQLException {
        String req = "UPDATE hebergement SET lieu = ?, date = ?, prix = ?, type = ?, nbr_personne = ?, nbr_nuit = ? WHERE id = ?";
        PreparedStatement os = connection.prepareStatement(req);
        os.setString(1, hebergement.getLieu());
        os.setDate(2, new java.sql.Date(hebergement.getDate().getTime()));
        os.setFloat(3, hebergement.getPrix());
        os.setString(4, hebergement.getType());
        os.setInt(5, hebergement.getNbr_personne());
        os.setInt(6, hebergement.getNbr_nuit());
        os.setInt(7, hebergement.getId());
        os.executeUpdate();
        System.out.println("Hébergement modifié avec succès");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM hebergement WHERE id = ?";
        PreparedStatement os = connection.prepareStatement(req);
        os.setInt(1, id);
        os.executeUpdate();
        System.out.println("Hébergement supprimé avec succès");
    }

    @Override
    public Hebergement getOneById(int id) throws SQLException {
        String req = "SELECT * FROM hebergement WHERE id = ?";
        PreparedStatement os = connection.prepareStatement(req);
        os.setInt(1, id);
        ResultSet rs = os.executeQuery();
        if (rs.next()) {
            Hebergement hebergement = new Hebergement();
            hebergement.setId(rs.getInt("id"));
            hebergement.setLieu(rs.getString("lieu"));
            hebergement.setDate(rs.getDate("date"));
            hebergement.setPrix(rs.getFloat("prix"));
            hebergement.setType(rs.getString("type"));
            hebergement.setNbr_personne(rs.getInt("nbr_personne"));
            hebergement.setNbr_nuit(rs.getInt("nbr_nuit"));
            return hebergement;
        }
        return null;
    }

    @Override
    public List<Hebergement> getAll() throws SQLException {
        List<Hebergement> hebergements = new ArrayList<>();
        String req = "SELECT * FROM hebergement";
        PreparedStatement os = connection.prepareStatement(req);
        ResultSet rs = os.executeQuery();
        while (rs.next()) {
            Hebergement hebergement = new Hebergement();
            hebergement.setId(rs.getInt("id"));
            hebergement.setLieu(rs.getString("lieu"));
            hebergement.setDate(rs.getDate("date"));
            hebergement.setPrix(rs.getFloat("prix"));
            hebergement.setType(rs.getString("type"));
            hebergement.setNbr_personne(rs.getInt("nbr_personne"));
            hebergement.setNbr_nuit(rs.getInt("nbr_nuit"));
            hebergements.add(hebergement);
        }
        return hebergements;
    }

    @Override
    public List<Hebergement> getByIdUser(int id) {
        // Cette méthode n'est pas implémentée dans votre code original
        return null;
    }
}
