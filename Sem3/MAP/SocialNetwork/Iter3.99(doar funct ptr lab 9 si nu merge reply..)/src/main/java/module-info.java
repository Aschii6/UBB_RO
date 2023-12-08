module com.example.socialnetworkguitwo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.socialnetworkguitwo to javafx.fxml;
    exports com.example.socialnetworkguitwo;

    opens com.example.socialnetworkguitwo.controllers to javafx.fxml;
    exports com.example.socialnetworkguitwo.controllers to javafx.fxml;

    exports com.example.socialnetworkguitwo.service;
    opens com.example.socialnetworkguitwo.service to javafx.fxml;

    exports com.example.socialnetworkguitwo.domain;
    opens com.example.socialnetworkguitwo.domain to javafx.fxml;
}