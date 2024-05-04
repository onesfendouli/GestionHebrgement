package com.example.descovertunisia.services;

import com.example.descovertunisia.entities.Hebergement;
import com.example.descovertunisia.entities.Reservation;
import com.example.descovertunisia.utils.mydatabase;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ReservationServices implements Iservice<Reservation> {
    private Connection connection;
    public ReservationServices() {
        connection = mydatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Reservation reservation) throws SQLException {
        String req = "INSERT INTO reservation (destination, check_in, check_out, rooms, adults, children, email, phone, hebergement_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement os = connection.prepareStatement(req);
        os.setString(1, reservation.getDestination());
        os.setDate(2, new java.sql.Date(reservation.getCheck_in().getTime()));
        os.setDate(3, new java.sql.Date(reservation.getCheck_out().getTime()));
        os.setInt(4, reservation.getRooms());
        os.setInt(5, reservation.getAdults());
        os.setInt(6, reservation.getChildren());
        os.setString(7, reservation.getEmail());
        os.setString(8, reservation.getPhone());
        os.setInt(9, reservation.getHebergement_id());
        os.executeUpdate();
        System.out.println("Réservation ajoutée avec succès");
    }

    @Override
    public void modifier(Reservation reservation) throws SQLException {
        String req = "UPDATE reservation SET destination = ?, check_in = ?, check_out = ?, rooms = ?, adults = ?, children = ?, email = ?, phone = ?, hebergement_id = ? WHERE id = ?";
        PreparedStatement os = connection.prepareStatement(req);
        os.setString(1, reservation.getDestination());
        os.setDate(2, new java.sql.Date(reservation.getCheck_in().getTime()));
        os.setDate(3, new java.sql.Date(reservation.getCheck_out().getTime()));
        os.setInt(4, reservation.getRooms());
        os.setInt(5, reservation.getAdults());
        os.setInt(6, reservation.getChildren());
        os.setString(7, reservation.getEmail());
        os.setString(8, reservation.getPhone());
        os.setInt(9, reservation.getHebergement_id());
        os.setInt(10, reservation.getId());
        os.executeUpdate();
        System.out.println("Réservation modifiée avec succès");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM reservation WHERE id = ?";
        PreparedStatement os = connection.prepareStatement(req);
        os.setInt(1, id);
        os.executeUpdate();
        System.out.println("Réservation supprimée avec succès");
    }

    @Override
    public Reservation getOneById(int id) throws SQLException {
        String req = "SELECT * FROM reservation WHERE id = ?";
        PreparedStatement os = connection.prepareStatement(req);
        os.setInt(1, id);
        ResultSet rs = os.executeQuery();
        if (rs.next()) {
            Reservation reservation = new Reservation();
            reservation.setId(rs.getInt("id"));
            reservation.setDestination(rs.getString("destination"));
            reservation.setCheck_in(rs.getDate("check_in"));
            reservation.setCheck_out(rs.getDate("check_out"));
            reservation.setRooms(rs.getInt("rooms"));
            reservation.setAdults(rs.getInt("adults"));
            reservation.setChildren(rs.getInt("children"));
            reservation.setEmail(rs.getString("email"));
            reservation.setPhone(rs.getString("phone"));
            reservation.setHebergement_id(rs.getInt("hebergement_id"));
            return reservation;
        }
        return null;
    }

    @Override
    public List<Reservation> getAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservation";
        PreparedStatement os = connection.prepareStatement(req);
        ResultSet rs = os.executeQuery();
        while (rs.next()) {
            Reservation reservation = new Reservation();
            reservation.setId(rs.getInt("id"));
            reservation.setDestination(rs.getString("destination"));
            reservation.setCheck_in(rs.getDate("check_in"));
            reservation.setCheck_out(rs.getDate("check_out"));
            reservation.setRooms(rs.getInt("rooms"));
            reservation.setAdults(rs.getInt("adults"));
            reservation.setChildren(rs.getInt("children"));
            reservation.setEmail(rs.getString("email"));
            reservation.setPhone(rs.getString("phone"));
            reservation.setHebergement_id(rs.getInt("hebergement_id"));
            reservations.add(reservation);
        }
        return reservations;
    }

    @Override
    public List<Reservation> getByIdUser(int id) throws SQLException{
        // Cette méthode n'est pas implémentée dans votre code original
        return null;
    }
    public float getprixtotale()throws SQLException{
        float prixTotale=0;
        String req = "SELECT r.rooms, h.nbr_nuit, h.prix FROM reservation r JOIN hebergement h ON r.hebergement_id = h.id";
        PreparedStatement os = connection.prepareStatement(req);
        ResultSet rs = os.executeQuery();
        while (rs.next()) {
            int rooms = rs.getInt("rooms");
            int nbr_nuit = rs.getInt("nbr_nuit");
            float prix = rs.getFloat("prix");

            prixTotale += rooms * nbr_nuit * prix;
        }
            return prixTotale;
    }
    public float getprixtotaleapresREmise() throws SQLException {
        float prixTotale = 0;
        String req = "SELECT r.id, r.email,r.rooms, h.nbr_nuit, h.prix FROM reservation r JOIN hebergement h ON r.hebergement_id = h.id";
        PreparedStatement os = connection.prepareStatement(req);
        ResultSet rs = os.executeQuery();
        Map<String, Integer> emailCount = new HashMap<>();
        float totalPrice = 0;
        while (rs.next()) {
            String email = rs.getString("email");
            int rooms = rs.getInt("rooms");
            int nbr_nuit = rs.getInt("nbr_nuit");
            float prix = rs.getFloat("prix");
            float pt = rooms * nbr_nuit * prix;
            int count = emailCount.getOrDefault(email, 0);
            emailCount.put(email, count + 1);

            if (count % 2 == 1) {
                pt *= 0.8;
            }

            totalPrice += pt;
        }

        prixTotale = totalPrice;
        return prixTotale;
    }
    public void sendMailConfurmation() throws SQLException {
        System.out.println("verefiction en cours");
        LocalDate currentDate = LocalDate.now();
        System.out.println(currentDate.plusDays(2));
        String req = "SELECT * FROM `reservation` WHERE `check_in` = '" + currentDate.plusDays(2) + "'";
        PreparedStatement os = connection.prepareStatement(req);
        ResultSet rs = os.executeQuery();
        while (rs.next()) {
            Reservation reservation = new Reservation();
            reservation.setId(rs.getInt("id"));
            reservation.setDestination(rs.getString("destination"));
            reservation.setCheck_in(rs.getDate("check_in"));
            reservation.setCheck_out(rs.getDate("check_out"));
            reservation.setRooms(rs.getInt("rooms"));
            reservation.setAdults(rs.getInt("adults"));
            reservation.setChildren(rs.getInt("children"));
            reservation.setEmail(rs.getString("email"));
            reservation.setPhone(rs.getString("phone"));
            reservation.setHebergement_id(rs.getInt("hebergement_id"));




                String body = "Cher " + reservation.getEmail() + ",\n\n" +
                        "Nous vous rappelons que votre réservation est prévue pour " + reservation.getCheck_in() + ".\n\n" +
                        "Merci de votre confiance.";
                System.out.println(body);
                sendEmail(reservation.getEmail(), "Confirmation de réservation", body);
            }

    }

    public void sendEmail(String recipient, String subject, String body) {
        // Paramètres SMTP
        String host = "smtp.gmail.com";
        String port = "587";
        String username = "onesfendouli72@gmail.com";
        String password = "rzca mbsx giei gstk";

        // Propriétés de la session
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Créer une session
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Créer un message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            // Ajouter les détails de la réservation dans le corps de l'e-mail
            message.setText(body);

            // Envoyer le message
            Transport.send(message);

            System.out.println("E-mail envoyé avec succès.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
