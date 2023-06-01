package Controller;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class modifyCustomerController {
    /**Customer Name Textfield*/
@FXML TextField tf_Customer_Name;
    /**Customer Address TextField*/
@FXML TextField tf_Customer_Address;
    /**Customer Postal TextField*/
@FXML TextField tf_Customer_Postal;
    /**Customer Phone Textfield*/
@FXML TextField tf_Customer_Phone;
    /**Customer ID TextField*/
@FXML TextField tf_Customer_ID;
    /**Customer Country ComboBox*/
@FXML ComboBox cb_Country;
    /**Customer Division ComboBox*/
@FXML ComboBox cb_Division;
    /**Observable List that returns Divisions that belong to USA*/
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
    /**Observable List that returns Divisions that belong to Canada*/
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
    /**Observable List that returns Divisions that belong to England*/
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
    /**Takes selected Customer and prepopulates with their respective entries*/
    public void sendCustomer(Customer customer) {
    tf_Customer_Name.setText(String.valueOf(customer.getCustomer_Name()));
    tf_Customer_Address.setText(String.valueOf(customer.getCustomer_Address()));
    tf_Customer_Phone.setText(String.valueOf(customer.getCustomer_Phone()));
    tf_Customer_Postal.setText(String.valueOf(customer.getCustomer_Postal()));
    tf_Customer_ID.setText(String.valueOf(customer.getCustomer_ID()));
    cb_Division.setValue(customer.getCustomer_divisionID());
//    cb_Country.setValue(customer.getCustomer_Country());
        if (customer.getCustomer_divisionID() <= 54) {
            cb_Country.setValue("USA");
        }
        if(customer.getCustomer_divisionID() <= 104 && customer.getCustomer_divisionID() >= 101) {
            cb_Country.setValue("England");
        }
        if(customer.getCustomer_divisionID() <= 72 && customer.getCustomer_divisionID() >= 60) {
            cb_Country.setValue("Canada");
        }




    }
    /**Takes input from modification and saves it.
     * Before saving, validates to make sure all entries are not null
     * @param event Save Customer Modification page*/
    public void onActionSave(ActionEvent event) throws SQLException, IOException {
        String name = tf_Customer_Name.getText();
        String address = tf_Customer_Address.getText();
        String phone = tf_Customer_Phone.getText();
        String postal = tf_Customer_Postal.getText();
        Integer customerID = Integer.valueOf(tf_Customer_ID.getText());
        String addDivision = "";
        if (cb_Division.getValue() != null) {
            addDivision = cb_Division.getValue().toString();
        }
        if(CustomerDB.validateCustomer(name, address, phone, postal,addDivision) == Boolean.TRUE)
        {
            Integer divisionID = division.getDivision(addDivision);
            CustomerDB.modifyCustomer(name, address, phone, postal, customerID, divisionID);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainMenu.fxml")));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        } else {
            System.out.println("error");
        }
    }
    /**Exits page and returns to BLMain.Main Menu
     * @param event Exit Button*/
    public void onActionCancel(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainMenu.fxml")));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**Initialization that takes country and sets its comboBox to its respective divisions*/
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

