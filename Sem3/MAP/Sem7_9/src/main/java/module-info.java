module com.example.sem7 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.sem7 to javafx.fxml;
    exports com.example.sem7;

    exports com.example.sem7.controllers;
    exports com.example.sem7.domain;

    opens com.example.sem7.controllers to javafx.fxml;
    opens com.example.sem7.domain to javafx.fxml;
}
