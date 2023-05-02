module com.example.software2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens BLMain to javafx.fxml;
    exports BLMain;
    exports Controller;
    opens Controller to javafx.fxml;
    exports Model;

}