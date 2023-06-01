package Controller;

import DAO.AppointmentDB;
import DAO.JDBC;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class appointmentController {
    /**String error prompt*/
    private static String error;
    /**Title Textfield*/
    @FXML
    private TextField tf_app_title;
    /**Description Textfield*/
    @FXML
    private TextField tf_app_description;
    /**Location Textfield*/
    @FXML
    private TextField tf_app_location;
    /**Type Textfield*/
    @FXML
    private TextField tf_app_type;
    /**Contact ComboBox*/
    @FXML
    private ComboBox cb_contact;
    /** CustomerID ComboBox*/
    @FXML
    private ComboBox cb_customerID;
    /** UserID ComboBox*/
    @FXML
    private ComboBox cb_UserID;
    /**Starting Time ComboBox*/
    @FXML
    private ComboBox cb_startTime;
    /**Ending Time ComboBox*/
    @FXML
    private ComboBox cb_endTime;
    /** Starting Date DatePicker*/
    @FXML
    private DatePicker dp_startDate;
    /**Ending Date Datepicker*/
    @FXML
    private DatePicker dp_endDate;
    /**Takes input from textfield/combobox/datepicker and adds appointment to database
     * Before saving, all input is converted to usable form.  Time/date is converted to format that is accepted by Mysql Database
     * Validates all input to make sure inputs are not empty.  Date is checked to see if date is not in the past, and not on weekends.
     * Time is validated to make sure it does not fall before opening hours or after opening hours
     * Generates user error message if any field is left blank.
     * @param event Appointment Save Button*/
    public void onActionAddSave(ActionEvent event) throws SQLException, IOException {
        LocalDate aptDate = dp_startDate.getValue();
        LocalDate endDate = dp_endDate.getValue();
        Integer addAppointmentID = AppointmentDB.assignAppointmentID();
        String addTitle = tf_app_title.getText();
        String addDescription = tf_app_description.getText();
        String addLocation = tf_app_location.getText();
        String addType = tf_app_type.getText();

        String addContact = "";
        if (cb_contact.getValue() != null) {
            addContact = cb_contact.getValue().toString();
        }
        String addCustomer = "";
        if (cb_customerID.getValue() != null) {
            addCustomer = cb_customerID.getValue().toString();
        }
        String addUser = "";
        if (cb_UserID.getValue() != null) {
            addUser = cb_UserID.getValue().toString();
        }


        if (!(aptDate == null) && !(endDate == null)) {
            if (!(cb_startTime.getSelectionModel().getSelectedItem() == null) && !(cb_endTime.getSelectionModel().getSelectedItem() == null)) {

                LocalDateTime formattedStart = AppointmentDB.convertToDateAndTime(aptDate, String.valueOf(cb_startTime.getSelectionModel().getSelectedItem().toString()));
                LocalDateTime formattedEnd = AppointmentDB.convertToDateAndTime(endDate, String.valueOf(cb_endTime.getSelectionModel().getSelectedItem().toString()));
                LocalTime startT = AppointmentDB.convertToTime(String.valueOf(cb_startTime.getSelectionModel().getSelectedItem().toString()));
                LocalTime endT = AppointmentDB.convertToTime(String.valueOf(cb_endTime.getSelectionModel().getSelectedItem().toString()));


                if (AppointmentDB.validateAppointment(addAppointmentID, addTitle, addDescription, addLocation, addType, addContact, addCustomer, addUser, formattedStart, formattedEnd)) {
                    if (!checkDate(aptDate)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText(error);
                        alert.showAndWait();
                        return;
                    } else if (!checkTime(startT) || !checkTime(endT)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText(error);
                        alert.showAndWait();
                        return;

                    } else if (AppointmentDB.overlappingAppointment(addCustomer, formattedStart, formattedEnd) == Boolean.FALSE) {
                        return;
                    } else {
                        AppointmentDB.addAppointment(addAppointmentID, addTitle, addDescription, addLocation, addType, addContact, Customer.getCustomerID(addCustomer), addUser, formattedStart, formattedEnd);
                        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        Object scene = FXMLLoader.load(getClass().getResource("/MainMenu.fxml"));
                        stage.setScene(new Scene((Parent) scene));
                        stage.show();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter time.");
                alert.showAndWait();
                return;
            }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter date.");
                alert.showAndWait();
                return;
            }
        }
    /** Method that makes sure that input time does not fall past closed hours or before opening hours
     * Checks time in EST timezone*/
    private boolean checkTime(LocalTime startTime){
        ZoneId etZone = ZoneId.of("America/New_York");
        ZonedDateTime etNow = ZonedDateTime.now(etZone);
        LocalTime etTimeNow = etNow.toLocalTime();
        LocalTime etStart = LocalTime.of(8,0);
        LocalTime etEnd = LocalTime.of(22,0);

        if(startTime.isBefore(etStart)) {
            error = "Please choose hours after opening.";
            return false;
        } else if(startTime.isAfter(etEnd)) {
            error = "Please choose hours before closing";
            return false;

        } else {
            return true;
        }
    }
    /**Method that checks to make sure date is not in the past and that the date falls on a weekday*/
    private boolean checkDate(LocalDate aptDate) {
            if (aptDate.isBefore(LocalDate.now())) {
                error = "Please choose a future date";
                return false;
            }
            if (aptDate.getDayOfWeek() == DayOfWeek.SATURDAY || aptDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                error = "Closed on weekdays. Please choose a weekday";
                return false;
            }
            return true;
        }

    /**Opens Database and adds Customer ID as a comboBox selection.
     * Sets up ObservableList that will generate options for ComboBox for starting time, ending time, contact and userID*/
    public void initialize() throws SQLException {
        ObservableList<String> customer = FXCollections.observableArrayList();
        Statement stmt = JDBC.openConnection().getConnection().createStatement();
        String query = "SELECT Customer_Name FROM customers";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String name = rs.getString("Customer_Name");
            customer.add(name);
        }
        rs.close();
        stmt.close();
        for(String name : customer) {
            System.out.println(name);
            cb_customerID.setItems(customer);
        }
        ObservableList<String> contact = FXCollections.observableArrayList("1", "2", "3");
        cb_contact.setItems(contact);
        ObservableList<String> user = FXCollections.observableArrayList("1", "2", "3");
        cb_UserID.setItems(user);
        ObservableList<String> start = FXCollections.observableArrayList("6:00","7:00", "8:00", "9:00", "10:00","11:00", "12:30", "13:00", "14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00", "22:00");
        cb_startTime.setItems(start);
        ObservableList<String> end = FXCollections.observableArrayList("6:00","7:00", "8:00", "9:00", "10:00","11:00", "12:00", "13:00", "14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00", "22:00","23:00");
        cb_endTime.setItems(end);
    }
    /**Exits page and returns to BLMain.Main Menu
     * @param event  Exit Button*/
    public void onActionExit(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainMenu.fxml")));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
}
