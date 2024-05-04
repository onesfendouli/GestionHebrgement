package com.example.descovertunisia.controllers.admin;

import com.example.descovertunisia.HelloApplication;
import com.example.descovertunisia.entities.Hebergement;
import com.example.descovertunisia.services.HebergementServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class ListeHebergement {
    @FXML
    private TableView<Hebergement> tableView;

    private HebergementServices hebergementService = new HebergementServices();

    @FXML
    void initialize() {
        refHebergement();

    }
    private void refHebergement() {
        tableView.getItems().clear();
        tableView.getColumns().clear();



        TableColumn<Hebergement, String> lieuCol = new TableColumn<>("Lieu");
        lieuCol.setCellValueFactory(new PropertyValueFactory<>("lieu"));

        TableColumn<Hebergement, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Hebergement, Float> prixCol = new TableColumn<>("Prix");
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));

        TableColumn<Hebergement, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Hebergement, Integer> nbrPersonneCol = new TableColumn<>("Nombre de personnes");
        nbrPersonneCol.setCellValueFactory(new PropertyValueFactory<>("nbr_personne"));

        TableColumn<Hebergement, Integer> nbrNuitCol = new TableColumn<>("Nombre de nuits");
        nbrNuitCol.setCellValueFactory(new PropertyValueFactory<>("nbr_nuit"));

        TableColumn<Hebergement, Void> supprimerCol = new TableColumn<>("Delete");
        supprimerCol.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Delete");

            {
                button.setOnAction(event -> {
                    Hebergement hebergement = getTableView().getItems().get(getIndex());
                    try {
                        hebergementService.supprimer(hebergement.getId());
                        tableView.getItems().remove(hebergement);
                        showSuccessAlert("Hébergement supprimé avec succès !");
                        refHebergement();
                        tableView.refresh();
                    } catch (SQLException e) {
                        showErrorAlert("Erreur lors de la suppression de l'hébergement : " + e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });

        TableColumn<Hebergement, Void> modifierCol = new TableColumn<>("Edit");
        modifierCol.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Edit");
            private int clickCount = 0;

            {
                button.setOnAction(event -> {
                    clickCount++;
                    if (clickCount == 2) {
                        Hebergement hebergement = getTableView().getItems().get(getIndex());
                        modifierHebergement(hebergement);
                        clickCount = 0;
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });

        tableView.getColumns().addAll( lieuCol, dateCol, prixCol, typeCol, nbrPersonneCol, nbrNuitCol, modifierCol, supprimerCol);

        try {
            tableView.getItems().addAll(hebergementService.getAll());
        } catch (SQLException e) {
            showErrorAlert("Erreur lors de la récupération des hébergements : " + e.getMessage());
        }
    }

    private void modifierHebergement(Hebergement hebergement) {
        Dialog<Hebergement> dialog = new Dialog<>();
        dialog.setTitle("Modifier Hébergement");
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField lieuField = new TextField(hebergement.getLieu());
        DatePicker dateField = new DatePicker(Instant.ofEpochMilli(hebergement.getDate().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        TextField prixField = new TextField(Float.toString(hebergement.getPrix()));
        TextField typeField = new TextField(hebergement.getType());
        TextField nbrPersonneField = new TextField(Integer.toString(hebergement.getNbr_personne()));
        TextField nbrNuitField = new TextField(Integer.toString(hebergement.getNbr_nuit()));

        grid.add(new Label("Lieu:"), 0, 0);
        grid.add(lieuField, 1, 0);
        grid.add(new Label("Date:"), 0, 1);
        grid.add(dateField, 1, 1);
        grid.add(new Label("Prix:"), 0, 2);
        grid.add(prixField, 1, 2);
        grid.add(new Label("Type:"), 0, 3);
        grid.add(typeField, 1, 3);
        grid.add(new Label("Nombre de personnes:"), 0, 4);
        grid.add(nbrPersonneField, 1, 4);
        grid.add(new Label("Nombre de nuits:"), 0, 5);
        grid.add(nbrNuitField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                hebergement.setLieu(lieuField.getText());
                hebergement.setDate(Date.from(dateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                hebergement.setPrix(Float.parseFloat(prixField.getText()));
                hebergement.setType(typeField.getText());
                hebergement.setNbr_personne(Integer.parseInt(nbrPersonneField.getText()));
                hebergement.setNbr_nuit(Integer.parseInt(nbrNuitField.getText()));
                return hebergement;
            }
            return null;
        });

        Optional<Hebergement> result = dialog.showAndWait();

        result.ifPresent(editedHebergement -> {
            try {
                hebergementService.modifier(editedHebergement);
                showSuccessAlert("Hébergement modifié avec succès !");
                refHebergement();
            } catch (SQLException e) {
                showErrorAlert("Erreur lors de la modification de l'hébergement : " + e.getMessage());
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
    void goListereservation() throws IOException {
        Scene scenefer = tableView.getScene();
        Stage stagefer = (Stage) scenefer.getWindow();
        stagefer.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Admin/reservationList.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("User Dechets");
        stage.setScene(scene);
        stage.show();
    }
}