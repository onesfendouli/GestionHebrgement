module com.example.descovertunisia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires android.json;
    requires java.mail;


    opens com.example.descovertunisia to javafx.fxml;
    opens com.example.descovertunisia.controllers.admin to javafx.fxml;
    opens com.example.descovertunisia.controllers.user to javafx.fxml;

    opens com.example.descovertunisia.entities to javafx.base;

    exports com.example.descovertunisia;
    exports com.example.descovertunisia.controllers.admin to javafx.fxml;
    exports com.example.descovertunisia.controllers.user to javafx.fxml;

}
