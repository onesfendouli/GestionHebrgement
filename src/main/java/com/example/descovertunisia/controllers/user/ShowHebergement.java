package com.example.descovertunisia.controllers.user;

import com.example.descovertunisia.HelloApplication;
import com.example.descovertunisia.entities.Hebergement;
import com.example.descovertunisia.entities.Reservation;
import com.example.descovertunisia.services.HebergementServices;
import com.example.descovertunisia.services.ReservationServices;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

public class ShowHebergement {

    @FXML
    private ScrollPane hebergementScrollPane;
    @FXML
    private TextField recherchefld;
    boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    boolean isValidEmail(String email) {
        // Implémentation de la validation d'email basique
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    @FXML
    public void initialize() {
        recherchefld.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            if(recherchefld.getText().isEmpty()){loadAcceptedProduits(loadAallproduit());}
            else{List<Hebergement> series;
                series=rechercher(loadAallproduit(),newValue);
                loadAcceptedProduits(series);}
        });
        loadAcceptedProduits(loadAallproduit());
    }
    public static List<Hebergement> rechercher(List<Hebergement> liste, String recherche) {
        List<Hebergement> resultats = new ArrayList<>();

        for (Hebergement element : liste) {
            if (element.getLieu().contains(recherche) ||element.getType().contains(recherche)||String.valueOf(element.getPrix()).contains(recherche) ) {
                resultats.add(element);
            }
        }

        return resultats;
    }
   List<Hebergement> loadAallproduit(){
        HebergementServices hebergementServices = new HebergementServices();
        List<Hebergement> hebergementList = null;
        try {
            hebergementList = hebergementServices.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
return hebergementList;
    }
    private void loadAcceptedProduits(List<Hebergement> hebergements) {
        FlowPane hebergementFlowPane=new FlowPane();
        hebergementFlowPane.setStyle("-fx-pref-width: 950px; " +
                "-fx-pref-height: 547px;");

        for (Hebergement hebergement : hebergements){
            VBox cardContainer = createProduitCard(hebergement);
            hebergementFlowPane.getChildren().add(cardContainer);
        }
        hebergementScrollPane.setContent(hebergementFlowPane);
    }
    private VBox createProduitCard(Hebergement hebergement) {
        VBox cardContainer = new VBox();
        cardContainer.setStyle("-fx-padding: 10px 0 0 50px;");

        AnchorPane card = new AnchorPane();
        card.setStyle("-fx-padding: 10 0 0 50; " +
                "-fx-border-radius: 5px; " +
                "-fx-border-color: #000000; " +
                "-fx-background-radius: 5px; " +
                "-fx-border-width: 1px; " +
                "-fx-pref-width: 200px; " +
                "-fx-pref-height: 10px;"+
                "-fx-background-color: #EFFCFF;");
        card.setPrefSize(200, 10);

        Label lieuLabel = new Label("Lieu: " + hebergement.getLieu());
        lieuLabel.setLayoutX(10);
        lieuLabel.setLayoutY(10);
        lieuLabel.setFont(Font.font("Arial", 12));

        Label dateLabel = new Label("Date: " + hebergement.getDate());
        dateLabel.setLayoutX(10);
        dateLabel.setLayoutY(50);
        dateLabel.setFont(Font.font("Arial", 12));

        Label prixLabel = new Label("Prix: " + hebergement.getPrix());
        prixLabel.setLayoutX(10);
        prixLabel.setLayoutY(90);
        prixLabel.setFont(Font.font("Arial", 12));

        Label typeLabel = new Label("Type: " + hebergement.getType());
        typeLabel.setLayoutX(10);
        typeLabel.setLayoutY(130);
        typeLabel.setFont(Font.font("Arial", 12));

        Label nbrPersonneLabel = new Label("Nombre de personnes: " + hebergement.getNbr_personne());
        nbrPersonneLabel.setLayoutX(10);
        nbrPersonneLabel.setLayoutY(170);
        nbrPersonneLabel.setFont(Font.font("Arial", 12));

        Label nbrNuitLabel = new Label("Nombre de nuits: " + hebergement.getNbr_nuit());
        nbrNuitLabel.setLayoutX(10);
        nbrNuitLabel.setLayoutY(210);
        nbrNuitLabel.setFont(Font.font("Arial", 12));

        card.getChildren().addAll(lieuLabel, dateLabel, prixLabel, typeLabel, nbrPersonneLabel, nbrNuitLabel);
        cardContainer.getChildren().add(card);

        card.setOnMouseClicked(event -> {
            ajouterReservation(hebergement);
        });

        return cardContainer;
    }


    private void ajouterReservation(Hebergement hebergement) {
        Dialog<Reservation> dialog = new Dialog<>();
        dialog.setTitle("Ajouter Réservation");
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 200, 200, 10));

        TextField destinationField = new TextField();
        Label checktype = new Label("*inserer un type");
        checktype.setTextFill(Color.RED);
        checktype.setVisible(false);

        DatePicker checkInField = new DatePicker();
        Label checkdtein = new Label("*inserer une date d'entrer");
        checkdtein.setTextFill(Color.RED);
        checkdtein.setVisible(false);

        DatePicker checkOutField = new DatePicker();
        Label checkdteout = new Label("*inserer une date de sortie");
        checkdteout.setTextFill(Color.RED);
        checkdteout.setVisible(false);

        TextField roomsField = new TextField();
        Label checkroom = new Label("*inserer un nombre valide");
        checkroom.setTextFill(Color.RED);
        checkroom.setVisible(false);

        TextField adultsField = new TextField();
        Label checkadults = new Label("*inserer un nombre valide");
        checkadults.setTextFill(Color.RED);
        checkadults.setVisible(false);

        TextField childrenField = new TextField();
        Label checkchildren = new Label("*inserer un nombre valide");
        checkchildren.setTextFill(Color.RED);
        checkchildren.setVisible(false);

        TextField emailField = new TextField();
        Label checkmail = new Label("*inserer un mail valide");
        checkmail.setTextFill(Color.RED);
        checkmail.setVisible(false);

        TextField phoneField = new TextField();
        Label checkphone = new Label("*inserer un numéro valide");
        checkphone.setTextFill(Color.RED);
        checkphone.setVisible(false);

        grid.add(new Label("Destination:"), 0, 0);
        grid.add(destinationField, 1, 0);
        grid.add(checktype, 2, 0);

        grid.add(new Label("Check-in:"), 0, 1);
        grid.add(checkInField, 1, 1);
        grid.add(checkdtein, 2, 1);

        grid.add(new Label("Check-out:"), 0, 2);
        grid.add(checkOutField, 1, 2);
        grid.add(checkdteout, 2, 2);

        grid.add(new Label("Rooms:"), 0, 3);
        grid.add(roomsField, 1, 3);
        grid.add(checkroom, 2, 3);

        grid.add(new Label("Adults:"), 0, 4);
        grid.add(adultsField, 1, 4);
        grid.add(checkadults,2,4);

        grid.add(new Label("Children:"), 0, 5);
        grid.add(childrenField, 1, 5);
        grid.add(checkchildren,2,5);

        grid.add(new Label("Email:"), 0, 6);
        grid.add(emailField, 1, 6);
        grid.add(checkmail, 2, 6);

        grid.add(new Label("Phone:"), 0, 7);
        grid.add(phoneField, 1, 7);
        grid.add(checkphone, 2, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {

                if (destinationField.getText().isEmpty()) {
                    checktype.setVisible(true);}


                if (checkInField.getValue() == null) {
                    checkdtein.setVisible(true);}


                if (checkOutField.getValue() == null) {
                    checkdteout.setVisible(true);}

                if (emailField.getText().isEmpty() && isValidEmail(emailField.getText())) {
                    checkmail.setVisible(true);}

                if (phoneField.getText().isEmpty()) {
                    checkphone.setVisible(true);}

                if (!isStringInt(roomsField.getText())) {
                    checkroom.setVisible(true);}

                if (!isStringInt(adultsField.getText())) {
                    checkadults.setVisible(true);}

                if (!isStringInt(childrenField.getText())) {
                    checkchildren.setVisible(true);}

                if(checkInField.getValue()!=null && checkOutField.getValue()!=null) {
                    if (checkInField.getValue().isBefore(checkOutField.getValue())) {
                        checkdteout.setText("*La date de fin doit être supérieure à la date de début.");
                        checkdteout.setVisible(true);
                    }
                }


                    Reservation reservation = new Reservation();
                    reservation.setDestination(destinationField.getText());
                    reservation.setCheck_in(Date.from(checkInField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    reservation.setCheck_out(Date.from(checkOutField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    reservation.setRooms(Integer.parseInt(roomsField.getText()));
                    reservation.setAdults(Integer.parseInt(adultsField.getText()));
                    reservation.setChildren(Integer.parseInt(childrenField.getText()));
                    reservation.setEmail(emailField.getText());
                    reservation.setPhone(phoneField.getText());
                    reservation.setHebergement_id(hebergement.getId());
                    return reservation;

            }
            return null;
        });

        Optional<Reservation> result = dialog.showAndWait();

        result.ifPresent(reservation -> {
            if(!reservation.getDestination().isEmpty()){
            reservation.setHebergement_id(hebergement.getId());
            ReservationServices rs=new ReservationServices();
            try {
                rs.ajouter(reservation);
                showSuccessAlert("Reservation ajoutée avec succès !");

            } catch (SQLException e) {
                showErrorAlert("Erreur lors de l'ajout de Reservation : " + e.getMessage());
            }}

        });
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
    void goMesReservation() throws IOException {
        Scene scenefer = hebergementScrollPane.getScene();
        Stage stagefer = (Stage) scenefer.getWindow();
        stagefer.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/user/Userreservation.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mes Reservations");
        stage.setScene(scene);
        stage.show();
    }
}
