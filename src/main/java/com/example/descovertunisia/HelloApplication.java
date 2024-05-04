package com.example.descovertunisia;

import com.example.descovertunisia.services.ReservationServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ReservationServices reservationServices = new ReservationServices();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Admin/AjouterHebergement.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/user/ShowHebergement.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        // Planifier l'exécution de la fonction à 1:46
        LocalTime targetTime = LocalTime.of(1, 0); // 1:46
        LocalTime currentTime = LocalTime.now();
        long initialDelay = Duration.between(currentTime, targetTime).toMinutes();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            try {
                reservationServices.sendMailConfurmation();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };

        ScheduledFuture<?> future = executor.scheduleAtFixedRate(task, initialDelay, 24 * 60, TimeUnit.MINUTES);
    }

    public static void main(String[] args) {
        launch();
    }
}