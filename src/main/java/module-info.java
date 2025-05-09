module com.example.miniproyecto3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.miniproyecto3 to javafx.fxml;
    exports com.example.miniproyecto3;
}