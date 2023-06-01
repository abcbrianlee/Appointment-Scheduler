module com.example.software2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens Main to javafx.fxml;
    opens Controller to javafx.fxml;

    exports Main;
    exports Controller;
    exports Model;


}