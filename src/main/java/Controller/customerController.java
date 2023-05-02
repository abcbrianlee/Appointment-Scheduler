package Controller;

import DAO.AppointmentDB;
import DAO.CustomerDB;
import DAO.JDBC;
import Model.Customer;
import Model.division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class customerController {
    /**Customer Name TextField*/
    @FXML private TextField tf_Customer_Name;
    /**Customer Address TextField*/
    @FXML private TextField tf_Customer_Address;
    /**Customer Phone Textfield*/
    @FXML private TextField tf_Customer_Phone;
    /**Customer Postal Textfield*/
    @FXML private TextField tf_Customer_Postal;
    /**Country ComboBox*/
    @FXML private ComboBox cb_Country;
    /**Division ComboBox*/
    @FXML private ComboBox cb_Division;
    /**Observable List that opens Mysql Database and sets all appropriate division to the US*/
    public static ObservableList<String> getUSADivisions() throws SQLException {
        ObservableList<String> USAdivision = FXCollections.observableArrayList();
        try {
            Statement stmt = JDBC.openConnection().getConnection().createStatement();
            String query = "SELECT Division FROM first_level_divisions WHERE Division_ID <= 54";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                String name = rs.getString("Division");
                USAdivision.add(name);
            }
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return USAdivision;
    }
    /**Observable List that opens Mysql Database and sets all appropriate divisions to Canada*/
    public static ObservableList<String> getCanadaDivisions() throws SQLException {
        ObservableList<String> canadaDivision = FXCollections.observableArrayList();
        Statement stmt2 = JDBC.openConnection().getConnection().createStatement();
        String query2 = "SELECT Division FROM first_level_divisions WHERE Division_ID <= 72 AND  Division_ID > 54 ";
        ResultSet rs2 = stmt2.executeQuery(query2);
        while(rs2.next()) {
            String name2 = rs2.getString("Division");
            canadaDivision.add(name2);
        }
        return canadaDivision;
    }
    /**Observable List that opens Mysql Database and sets all appropriate divisions to England*/
    public static ObservableList<String> getEnglandDivisions() throws SQLException {
        ObservableList<String> englandDivision = FXCollections.observableArrayList();
        Statement stmt3 = JDBC.openConnection().getConnection().createStatement();
        String query3 = "SELECT Division FROM first_level_divisions WHERE Division_ID >= 101";
        ResultSet rs3 = stmt3.executeQuery(query3);
        while(rs3.next()) {
            String name3 = rs3.getString("Division");
            englandDivision.add(name3);
        }

        return englandDivision;
    }
    /**Takes input and saves Customer.
     * Before saving, customer is validated to make sure that no input is left empty
     * AFter successful save, redirected to Main Menu
     * @param event Save Customer Button*/
    public void onActionSave(ActionEvent event) throws SQLException, IOException {
        Integer addCustomerID = CustomerDB.assignCustomerID();
        String addName = tf_Customer_Name.getText();
        String addAddress = tf_Customer_Address.getText();
        String addPhone = tf_Customer_Phone.getText();
        String addPostal = tf_Customer_Postal.getText();
        String addDivision = "";
        if (cb_Division.getValue() != null) {
            addDivision = cb_Division.getValue().toString();
        }

        if(CustomerDB.validateCustomer(addName, addAddress, addPhone, addPostal, addDivision) == Boolean.FALSE) {
            return;
        } else {
            Integer divisionID = division.getDivision(addDivision);
            CustomerDB.addCustomer(addCustomerID, addName, addAddress, addPhone, addPostal, String.valueOf(divisionID));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/BLMain/MainMenu.fxml")));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        }
        }
    /**Returns page to Main Menu
     * @param event Exit Button*/
    public void onActionCancel(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/BLMain/MainMenu.fxml")));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**Lambda Expression where if ComboBox is set to a country, its respective divisions are populated as ComboBox options.
     * The Lambda Expression is triggered once a user selects its ComboBox and its parameters (event1) is seperated from its body.
     * */
    public void initialize() throws SQLException {
        ObservableList<String> countries = FXCollections.observableArrayList("USA", "England", "Canada");
        cb_Country.setItems(countries);
        cb_Country.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("USA")) {
                    ObservableList<String> USAdivision = null;
                    try {
                        USAdivision = getUSADivisions();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    cb_Division.setItems(USAdivision);
                } else if (newValue.equals("England")) {
                    ObservableList<String> englandDivision = null;
                    try {
                        englandDivision = getEnglandDivisions();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    cb_Division.setItems(englandDivision);
                } else if (newValue.equals("Canada")) {
                    ObservableList<String> canadaDivision = null;
                    try {
                        canadaDivision = getCanadaDivisions();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    cb_Division.setItems(canadaDivision);
                } else {
                    cb_Division.setItems(null);
                }
            } else {
                cb_Division.setItems(null);
            }
        });
    }
}

