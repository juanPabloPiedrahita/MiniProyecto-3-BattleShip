module com.example.miniproyecto3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.miniproyecto3 to javafx.fxml;
    exports com.example.miniproyecto3;
    exports com.example.miniproyecto3.controller;
    opens com.example.miniproyecto3.controller to javafx.fxml;
    exports com.example.miniproyecto3.model;
    opens com.example.miniproyecto3.model to javafx.fxml;
}