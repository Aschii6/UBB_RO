module com.example.sem10 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sem10 to javafx.fxml;
    exports com.example.sem10;
}