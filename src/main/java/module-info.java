module com.example.lines {
    requires javafx.controls;

    opens com.example.lines to javafx.fxml;
    exports com.example.lines;
}