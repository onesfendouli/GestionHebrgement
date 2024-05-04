package com.example.descovertunisia.controllers.admin;

import com.example.descovertunisia.HelloApplication;
import com.example.descovertunisia.entities.Reservation;
import com.example.descovertunisia.services.ReservationServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class ListeReservation {
    @FXML
    private TableView<Reservation> tableView;

    private ReservationServices reservationService = new ReservationServices();

    @FXML
    void initialize() {
        refreshReservations();
    }

    private void refreshReservations() {
        tableView.getItems().clear();
        tableView.getColumns().clear();

        TableColumn<Reservation, String> destinationCol = new TableColumn<>("Destination");
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));

        TableColumn<Reservation, String> checkInCol = new TableColumn<>("Check-in");
        checkInCol.setCellValueFactory(new PropertyValueFactory<>("check_in"));

        TableColumn<Reservation, String> checkOutCol = new TableColumn<>("Check-out");
        checkOutCol.setCellValueFactory(new PropertyValueFactory<>("check_out"));

        TableColumn<Reservation, Integer> roomsCol = new TableColumn<>("Rooms");
        roomsCol.setCellValueFactory(new PropertyValueFactory<>("rooms"));

        TableColumn<Reservation, Integer> adultsCol = new TableColumn<>("Adults");
        adultsCol.setCellValueFactory(new PropertyValueFactory<>("adults"));

        TableColumn<Reservation, Integer> childrenCol = new TableColumn<>("Children");
        childrenCol.setCellValueFactory(new PropertyValueFactory<>("children"));

        TableColumn<Reservation, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Reservation, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));


        tableView.getColumns().addAll(destinationCol, checkInCol, checkOutCol, roomsCol, adultsCol, childrenCol, emailCol, phoneCol);

        try {
            tableView.getItems().addAll(reservationService.getAll());
        } catch (SQLException e) {
            showErrorAlert("Erreur lors de la récupération des réservations : " + e.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void goAjouterhebergement() throws IOException {
        Scene scenefer = tableView.getScene();
        Stage stagefer = (Stage) scenefer.getWindow();
        stagefer.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Admin/AjouterHebergement.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("User Dechets");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void goListeHebergement() throws IOException {
        Scene scenefer = tableView.getScene();
        Stage stagefer = (Stage) scenefer.getWindow();
        stagefer.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Admin/Showhebergement.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("User Dechets");
        stage.setScene(scene);
        stage.show();
    }

}
