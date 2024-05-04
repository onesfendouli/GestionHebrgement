package com.example.descovertunisia.controllers.user;

import com.example.descovertunisia.HelloApplication;
import com.example.descovertunisia.entities.Hebergement;
import com.example.descovertunisia.entities.Reservation;
import com.example.descovertunisia.services.ReservationServices;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ShowUserReservation {
    @FXML
    private ScrollPane reservationScrollPane;
    @FXML
    Label PT;
    @FXML
    Label RM;
    @FXML
    Label PTAR;
    private final ReservationServices reservationServices = new ReservationServices();

    boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    @FXML
    private TextField recherchefld;

    @FXML
    public void initialize() {

        recherchefld.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            if(recherchefld.getText().isEmpty()){loadAcceptedProduits(getAllreservtaion());}
            else{List<Reservation> series;
                series=rechercher(getAllreservtaion(),newValue);
                loadAcceptedProduits(series);}
        });
        loadAcceptedProduits(getAllreservtaion());
        try {
            PT.setText(String.valueOf(reservationServices.getprixtotale())+" DT");
            PTAR.setText(String.valueOf(reservationServices.getprixtotaleapresREmise())+" DT");
            RM.setText(String.valueOf(100-(reservationServices.getprixtotaleapresREmise()*100/reservationServices.getprixtotale()))+" %");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(getAllreservtaion().size()>1){

        }
    }
    public static List<Reservation> rechercher(List<Reservation> liste, String recherche) {
        List<Reservation> resultats = new ArrayList<>();

        for (Reservation element : liste) {
            if (element.getDestination().contains(recherche) ||element.getPhone().contains(recherche)||String.valueOf(element.getEmail()).contains(recherche) ) {
                resultats.add(element);
            }
        }

        return resultats;
    }
   List<Reservation> getAllreservtaion(){
       List<Reservation> reservationList = null;
       try {
           reservationList = reservationServices.getAll();
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
       return reservationList;
   }
    private void loadAcceptedProduits(List<Reservation> reservationList) {
        FlowPane reservationFlowPane = new FlowPane();
        reservationFlowPane.setStyle("-fx-pref-width: 950px; " +
                "-fx-pref-height: 547px;");



        for (Reservation reservation : reservationList) {
            VBox cardContainer = createProduitCard(reservation);
            reservationFlowPane.getChildren().add(cardContainer);
        }

        reservationScrollPane.setContent(reservationFlowPane);
    }

    private VBox createProduitCard(Reservation reservation) {
        VBox cardContainer = new VBox();
        cardContainer.setStyle("-fx-padding: 10px 0 0 50px;");

        AnchorPane card = new AnchorPane();
        card.setStyle("-fx-padding: 10 0 0 50; " +
                "-fx-border-radius: 5px; " +
                "-fx-border-color: #000000; " +
                "-fx-background-radius: 5px; " +
                "-fx-border-width: 1px; " +
                "-fx-pref-width: 200px; " +
                "-fx-pref-height: 10px;" +
                "-fx-background-color: #EFFCFF;");
        card.setPrefSize(200, 10);

        // Ajout de l'icône de suppression
        ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/Css/delete_icon.png")));
        deleteIcon.setLayoutX(10);
        deleteIcon.setLayoutY(10);
        deleteIcon.setFitWidth(20);
        deleteIcon.setFitHeight(20);
        deleteIcon.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer cette réservation ?");
            alert.setContentText("Cette action est irréversible.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    reservationServices.supprimer(reservation.getId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Ajout de l'icône de modification
        ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/Css/edit_icon.png")));
        editIcon.setLayoutX(40);
        editIcon.setLayoutY(10);
        editIcon.setFitWidth(20);
        editIcon.setFitHeight(20);
        editIcon.setOnMouseClicked(event -> {
            modifierReservation(reservation);
        });

        Label destinationLabel = new Label("Destination: " + reservation.getDestination());
        destinationLabel.setLayoutX(70); // Ajuster la position en conséquence
        destinationLabel.setLayoutY(10);
        destinationLabel.setFont(Font.font("Arial", 12));

        Label checkInLabel = new Label("Check-in: " + reservation.getCheck_in());
        checkInLabel.setLayoutX(10);
        checkInLabel.setLayoutY(50);
        checkInLabel.setFont(Font.font("Arial", 12));

        Label checkOutLabel = new Label("Check-out: " + reservation.getCheck_out());
        checkOutLabel.setLayoutX(10);
        checkOutLabel.setLayoutY(90);
        checkOutLabel.setFont(Font.font("Arial", 12));

        Label roomsLabel = new Label("Rooms: " + reservation.getRooms());
        roomsLabel.setLayoutX(10);
        roomsLabel.setLayoutY(130);
        roomsLabel.setFont(Font.font("Arial", 12));

        Label adultsLabel = new Label("Adults: " + reservation.getAdults());
        adultsLabel.setLayoutX(10);
        adultsLabel.setLayoutY(170);
        adultsLabel.setFont(Font.font("Arial", 12));

        Label childrenLabel = new Label("Children: " + reservation.getChildren());
        childrenLabel.setLayoutX(10);
        childrenLabel.setLayoutY(210);
        childrenLabel.setFont(Font.font("Arial", 12));

        Label emailLabel = new Label("Email: " + reservation.getEmail());
        emailLabel.setLayoutX(10);
        emailLabel.setLayoutY(250);
        emailLabel.setFont(Font.font("Arial", 12));

        Label phoneLabel = new Label("Phone: " + reservation.getPhone());
        phoneLabel.setLayoutX(10);
        phoneLabel.setLayoutY(290);
        phoneLabel.setFont(Font.font("Arial", 12));

        card.getChildren().addAll(deleteIcon, editIcon, destinationLabel, checkInLabel, checkOutLabel, roomsLabel, adultsLabel, childrenLabel, emailLabel, phoneLabel);
        cardContainer.getChildren().add(card);


        return cardContainer;
    }


    private void modifierReservation(Reservation reservation) {
        Dialog<Reservation> dialog = new Dialog<>();
        dialog.setTitle("Modifier Réservation");
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 200, 200, 10));

        TextField destinationField = new TextField(reservation.getDestination());
        Label checktype = new Label("*inserer un type");
        checktype.setTextFill(Color.RED);
        checktype.setVisible(false);

        DatePicker checkInField = new DatePicker(Instant.ofEpochMilli(reservation.getCheck_in().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        Label checkdtein = new Label("*inserer une date d'entrer");
        checkdtein.setTextFill(Color.RED);
        checkdtein.setVisible(false);

        DatePicker checkOutField = new DatePicker(Instant.ofEpochMilli(reservation.getCheck_out().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        Label checkdteout = new Label("*inserer une date de sortie");
        checkdteout.setTextFill(Color.RED);
        checkdteout.setVisible(false);

        TextField roomsField = new TextField(String.valueOf(reservation.getRooms()));
        Label checkroom = new Label("*inserer un nombre valide");
        checkroom.setTextFill(Color.RED);
        checkroom.setVisible(false);

        TextField adultsField = new TextField(String.valueOf(reservation.getAdults()));
        Label checkadults = new Label("*inserer un nombre valide");
        checkadults.setTextFill(Color.RED);
        checkadults.setVisible(false);

        TextField childrenField = new TextField(String.valueOf(reservation.getChildren()));
        Label checkchildren = new Label("*inserer un nombre valide");
        checkchildren.setTextFill(Color.RED);
        checkchildren.setVisible(false);

        TextField emailField = new TextField(reservation.getEmail());
        Label checkmail = new Label("*inserer un mail valide");
        checkmail.setTextFill(Color.RED);
        checkmail.setVisible(false);

        TextField phoneField = new TextField(reservation.getPhone());
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
        grid.add(checkadults, 2, 4);

        grid.add(new Label("Children:"), 0, 5);
        grid.add(childrenField, 1, 5);
        grid.add(checkchildren, 2, 5);

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
                    checktype.setVisible(true);
                }


                if (checkInField.getValue() == null) {
                    checkdtein.setVisible(true);
                }


                if (checkOutField.getValue() == null) {
                    checkdteout.setVisible(true);
                }

                if (emailField.getText().isEmpty()) {
                    checkmail.setVisible(true);
                }

                if (phoneField.getText().isEmpty()) {
                    checkphone.setVisible(true);
                }

                if (!isStringInt(roomsField.getText())) {
                    checkroom.setVisible(true);
                }

                if (!isStringInt(adultsField.getText())) {
                    checkadults.setVisible(true);
                }

                if (!isStringInt(childrenField.getText())) {
                    checkchildren.setVisible(true);
                }

                if (checkInField.getValue() != null && checkOutField.getValue() != null) {
                    if (checkInField.getValue().isAfter(checkOutField.getValue())) {
                        checkdteout.setText("*La date de fin doit être supérieure à la date de début.");
                        checkdteout.setVisible(true);
                    }
                }

                reservation.setDestination(destinationField.getText());
                reservation.setCheck_in(Date.from(checkInField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                reservation.setCheck_out(Date.from(checkOutField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                reservation.setRooms(Integer.parseInt(roomsField.getText()));
                reservation.setAdults(Integer.parseInt(adultsField.getText()));
                reservation.setChildren(Integer.parseInt(childrenField.getText()));
                reservation.setEmail(emailField.getText());
                reservation.setPhone(phoneField.getText());
                return reservation;

            }
            return null;
        });

        Optional<Reservation> result = dialog.showAndWait();

        result.ifPresent(editreservation -> {
            if (!editreservation.getDestination().isEmpty()) {
                editreservation.setHebergement_id(editreservation.getHebergement_id());
                try {
                    editreservation.setHebergement_id(reservation.getHebergement_id());
                    System.out.println(editreservation);
                    reservationServices.modifier(editreservation);
                    showSuccessAlert("Reservation Modifier avec succès !");
                    loadAcceptedProduits(getAllreservtaion());

                } catch (SQLException e) {
                    showErrorAlert("Erreur lors de Modification de Reservation : " + e.getMessage());
                    System.out.println(e.getMessage());
                }
            }

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
    void goAjouterreservation() throws IOException {
        Scene scenefer = reservationScrollPane.getScene();
        Stage stagefer = (Stage) scenefer.getWindow();
        stagefer.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/user/ShowHebergement.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mes Reservations");
        stage.setScene(scene);
        stage.show();
    }
}