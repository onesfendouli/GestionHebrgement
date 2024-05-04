package com.example.descovertunisia.controllers.admin;

import com.example.descovertunisia.HelloApplication;
import com.example.descovertunisia.entities.Adresse;
import com.example.descovertunisia.entities.Hebergement;
import com.example.descovertunisia.services.HebergementServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HebergementController {
    @FXML
    private TextField PrixF;

    @FXML
    private Label checkdate;

    @FXML
    private Label checklieu;

    @FXML
    private Label checknbrnuit;

    @FXML
    private Label checknbrpersonne;

    @FXML
    private Label checkprix;

    @FXML
    private Label chektype;

    @FXML
    private DatePicker dateF;

    @FXML
    private TextField lieuF;

    @FXML
    private TextField nbr_nuitF;

    @FXML
    private TextField nbr_personneF;

    @FXML
    private ComboBox<String> typecambo;

    @FXML
    private ListView<Adresse> lidtecordonner;
    @FXML
    private Label selectedAdresse;

    private HebergementServices hebergementService = new HebergementServices();

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

    boolean checklieu() {
        if (!selectedAdresse.getText().isEmpty()) {
            checklieu.setVisible(false);
            return true;
        } else {
            checklieu.setVisible(true);
            return false;
        }
    }
    boolean chektype() {
        if (!typecambo.getValue().isEmpty()) {
            chektype.setVisible(false);
            return true;
        } else {
            chektype.setVisible(true);
            return false;
        }
    }

    boolean checkprix() {
        if (!PrixF.getText().isEmpty() && isStringFloat(PrixF.getText())) {
            checkprix.setVisible(false);
            return true;
        } else {
            checkprix.setVisible(true);
            return false;
        }
    }

    boolean checknbrnuit() {
        if (!nbr_nuitF.getText().isEmpty() && isStringInt(nbr_nuitF.getText())) {
            checknbrnuit.setVisible(false);
            return true;
        } else {
            checknbrnuit.setVisible(true);
            return false;
        }
    }

    boolean checknbrpersonne() {
        if (!nbr_personneF.getText().isEmpty() && isStringInt(nbr_personneF.getText())) {
            checknbrpersonne.setVisible(false);
            return true;
        } else {
            checknbrpersonne.setVisible(true);
            return false;
        }
    }

    boolean checkdate() {
        if (dateF.getValue() != null) {
            checkdate.setVisible(false);
            return true;
        } else {
            checkdate.setVisible(true);
            return false;
        }
    }



    boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    boolean isStringFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    void ajouterHebergement(ActionEvent event) {
        checklieu(); checkprix(); checknbrnuit(); checknbrpersonne(); checkdate(); chektype();
        if (checklieu() && checkprix() && checknbrnuit() && checknbrpersonne() && checkdate() && chektype()) {
            Hebergement hebergement = new Hebergement();
            hebergement.setLieu(selectedAdresse.getText());
            hebergement.setDate(java.sql.Date.valueOf(dateF.getValue()));
            hebergement.setPrix(Float.parseFloat(PrixF.getText()));
            hebergement.setType(typecambo.getValue());
            hebergement.setNbr_personne(Integer.parseInt(nbr_personneF.getText()));
            hebergement.setNbr_nuit(Integer.parseInt(nbr_nuitF.getText()));

            try {
                hebergementService.ajouter(hebergement);
                showSuccessAlert("Hébergement ajouté avec succès");
                // Réinitialisation des champs
                lieuF.setText("");
                PrixF.setText("");
                typecambo.getSelectionModel().clearSelection();
                nbr_personneF.setText("");
                nbr_nuitF.setText("");
                resetadresse();
                lidtecordonner.getItems().clear();
                lidtecordonner.setVisible(false);
            } catch (SQLException e) {
                showErrorAlert("Erreur lors de l'ajout de l'hébergement : " + e.getMessage());
            }
        }
    }



    @FXML
    void goListeHebergement() throws IOException {
        Scene scenefer = typecambo.getScene();
        Stage stagefer = (Stage) scenefer.getWindow();
        stagefer.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Admin/Showhebergement.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("User Dechets");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void goListereservation() throws IOException {
        Scene scenefer = typecambo.getScene();
        Stage stagefer = (Stage) scenefer.getWindow();
        stagefer.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Admin/reservationList.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("User Dechets");
        stage.setScene(scene);
        stage.show();
    }
    List<Adresse> setadressegeo(String text) {
        List<Adresse> adresseList=new ArrayList<>();
        String API_URL = "https://neutrinoapi.net/geocode-address";
        String API_KEY = "cyEIkNQxPmSmlONcs32Qm2io2pl2aPLi291SlS2YEYU6H6RN";
        String USER_ID = "IsmailChouikhi";

        try {
            String urlParameters = "address=" + URLEncoder.encode(text, "UTF-8") +
                    "&house-number=" +
                    "&street=" +
                    "&city=" +
                    "&county=" +
                    "&state=" +
                    "&postal-code=" +
                    "&country-code=" +
                    "&language-code=en" +
                    "&fuzzy-search=false";

            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-ID", USER_ID);
            connection.setRequestProperty("API-Key", API_KEY);
            connection.setDoOutput(true);

            // Send POST request
            connection.getOutputStream().write(urlParameters.getBytes("UTF-8"));

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // System.out.println("Response: " + response.toString());
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray locationsArray = jsonObject.getJSONArray("locations");
                for (int i = 0; i < locationsArray.length(); i++) {
                    JSONObject location = locationsArray.getJSONObject(i);
                    JSONObject addressComponents = location.getJSONObject("address-components");
                    Adresse adresse =new Adresse();
                    adresse.setLatitude(String.valueOf(location.getDouble("latitude")));
                    adresse.setCountry(addressComponents.getString("country"));
                    if (addressComponents.has("state-district")) {
                        adresse.setState_District(addressComponents.getString("state-district"));
                    } else {
                        adresse.setState_District(""); // ou une valeur par défaut appropriée
                    }                adresse.setCity(addressComponents.getString("city"));
                    adresse.setCounty(addressComponents.getString("county"));
                    adresse.setState(addressComponents.getString("state"));
                    adresseList.add(adresse);
                }
            } else {
                System.out.println("Error: " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adresseList;
    }
    void afficheradresseListe(List<Adresse> list, ListView<Adresse> listView){
        if(list.isEmpty()){
            showErrorAlert("Une erreur s'est produite lors de la recherche d'adresse. Veuillez réessayer plus tard.");
            selectedAdresse.setText(lieuF.getText());
        }else {
            listView.setVisible(true);
            listView.getItems().clear();
            listView.getItems().addAll(list);
            listView.setCellFactory(param -> new ListCell<Adresse>() {
                @Override
                protected void updateItem(Adresse item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                        setOnMouseClicked(event -> {
                            if (event.getClickCount() == 2) {
                                selectedAdresse.setText(item.toString());
                                lidtecordonner.setVisible(false);
                                lieuF.setVisible(false);
                                selectedAdresse.setVisible(true);

                            }
                        });
                    }
                }


            });}}

    @FXML
    void initialize() {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Hotel",
                "Maison d'hote",
                "Appartement"
        );
        typecambo.setItems(options);

        ///fonction recherche sur textfiled
        /*cordonnerf.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {

            afficheradresseListe(setadressegeo("Boughrara"),lidtecordonner);

        });*/
        lieuF.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (lieuF.getText()!=null){
                    afficheradresseListe(setadressegeo(lieuF.getText()), lidtecordonner);}
            }
        });
        selectedAdresse.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                resetadresse();
            }
        });


    }
    void resetadresse(){
        selectedAdresse.setText(null);
        selectedAdresse.setVisible(false);
        lieuF.setVisible(true);
        lidtecordonner.setVisible(true);
    }
}
