package Controller;

import DAO.AppointmentDB;
import DAO.JDBC;
import Model.Appointment;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.util.Objects;

public class modifyAppointmentController {
    /**Appointment ID Textfield*/
    @FXML private TextField tf_app_ID;
    /**Appointment Title Textfield*/
    @FXML private TextField tf_app_title;
    /**Appointment Description Textfield*/
    @FXML private TextField tf_app_description;
    /**Appointment Location TextField*/
    @FXML private TextField tf_app_location;
    /**Appointment Type Textfield*/
    @FXML private TextField tf_app_type;
    /**Contact ComboBox*/
    @FXML private ComboBox cb_contact;
    /**User ID ComboBox*/
    @FXML private ComboBox cb_UserID;
    /**Customer ID ComboBox*/
    @FXML private ComboBox cb_customerID;
    /**Ending Time ComboBox*/
    @FXML private ComboBox cb_endTime;
    /**Starting Time ComboBox*/
    @FXML private ComboBox cb_startTime;
    /**Starting Date DatePicker*/
    @FXML private DatePicker dp_startDate;
    /**Ending Date DatePicker*/
    @FXML private DatePicker dp_endDate;
    /**Error String*/
    private static String error;
    /**Observable List containing all Contact ID*/
    ObservableList<String> contact = FXCollections.observableArrayList("1", "2", "3");
    /**Observable List that returns Customer ID from Mysql Database Search*/
    public static ObservableList<String> getCustomerID() throws SQLException {
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
            return customer;
        }
        return customer;
    }
    /**Observable List that list all UserID's*/
    ObservableList<String> user = FXCollections.observableArrayList("1", "2", "3");
    /**Observable List that lists all starting time*/
    ObservableList<String> start = FXCollections.observableArrayList("6:00","7:00", "8:00", "9:00", "10:00","11:00", "12:30", "13:00", "14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00", "22:00");
    /**Observable List that lists all Ending Times*/
    ObservableList<String> end = FXCollections.observableArrayList("6:00","7:00", "8:00", "9:00", "10:00","11:00", "12:00", "13:00", "14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00", "22:00","23:00");
    private Appointment selectedappointment;
    /**Method that takes all selected appointment input to be used later in Appointment modification Page*/
    public void sendAppointment(Appointment selectedappointment) throws SQLException {
        this.selectedappointment = selectedappointment;
        tf_app_ID.setText(String.valueOf(selectedappointment.getAppointmentID()));
        tf_app_title.setText(selectedappointment.getTitle());
        tf_app_description.setText(selectedappointment.getDescription());
        tf_app_location.setText(selectedappointment.getLocation());
        tf_app_type.setText(selectedappointment.getType());
        cb_contact.setValue(selectedappointment.getContactID());
        cb_UserID.setValue(selectedappointment.getUserID());
        cb_customerID.setValue(selectedappointment.getCustomerID());

        LocalTime startTime = AppointmentDB.convertToLocalTime(LocalDateTime.parse(String.valueOf(selectedappointment.getStartTime())));
        cb_startTime.setValue(startTime);
        LocalTime endTime = AppointmentDB.convertToLocalTime(LocalDateTime.parse(String.valueOf(selectedappointment.getEndTime())));
        cb_endTime.setValue(endTime);

        LocalDate startDate = AppointmentDB.convertToLocalDate(selectedappointment.getStartTime());
        dp_startDate.setValue(startDate);
        LocalDate endDate = AppointmentDB.convertToLocalDate(selectedappointment.getEndTime());
        dp_endDate.setValue(endDate);

//

    }
    /**Takes User input and Saves appointment
     * Takes time and date and converts to parameters that will be accepted by MYSQL database
     * Checks to make sure all entries have input, otherwise User error message will be generated
     * Checks to see if any entries overlap with previous appointments, will generate user error
     * @param event Save Appointment Page*/
    public void onActionAddSave(ActionEvent event) throws SQLException, IOException {
        Integer addAppointmentID = Integer.valueOf(tf_app_ID.getText());
        LocalDate aptDate = dp_startDate.getValue();
        LocalDate endDate = dp_endDate.getValue();
        String addTitle = tf_app_title.getText();
        String addDescription = tf_app_description.getText();
        String addLocation = tf_app_location.getText();
        String addType = tf_app_type.getText();

        String addContact = "";
        if (cb_contact.getValue() != null) {
            addContact = cb_contact.getValue().toString();
        }
        String addCustomerID = "";
        if (cb_customerID.getValue() != null) {
            addCustomerID = cb_customerID.getValue().toString();
        }
        String addUserID = "";
        if (cb_UserID.getValue() != null) {
            addUserID = cb_UserID.getValue().toString();
        }

        if (!(aptDate == null) && !(endDate == null)) {
            if (!(cb_startTime.getSelectionModel().getSelectedItem() == null) && !(cb_endTime.getSelectionModel().getSelectedItem() == null)) {

                LocalDateTime formattedStart = AppointmentDB.convertToDateAndTime(aptDate, String.valueOf(cb_startTime.getSelectionModel().getSelectedItem().toString()));
                LocalDateTime formattedEnd = AppointmentDB.convertToDateAndTime(endDate, String.valueOf(cb_endTime.getSelectionModel().getSelectedItem().toString()));
                LocalTime startT = AppointmentDB.convertToTime(String.valueOf(cb_startTime.getSelectionModel().getSelectedItem().toString()));
                LocalTime endT = AppointmentDB.convertToTime(String.valueOf(cb_endTime.getSelectionModel().getSelectedItem().toString()));

                if (AppointmentDB.validateAppointment(addAppointmentID, addTitle, addDescription, addLocation, addType, addContact, addCustomerID, addUserID, formattedStart, formattedEnd)) {
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

                    }if (AppointmentDB.overlappingAppointment(addCustomerID, formattedStart, formattedEnd) == Boolean.FALSE) {
                    return;
                } else {
                    AppointmentDB.updateAppointment(addAppointmentID, addTitle, addDescription, addLocation, addType, addContact, addUserID, Customer.getCustomerID(addCustomerID), formattedStart , formattedEnd);
                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    Object scene = FXMLLoader.load(getClass().getResource("/BLMain/MainMenu.fxml"));
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
    /**Method that checks time to make sure it is between opening hours
     * Generates error if it does.*/
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
    /**Method that checks to make sure date falls on a weekday and is not in the past
     * Generates user error if it does.*/
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
    /**Exits the page and returns to Main Menu
     * @param event  Exit Button*/
        public void onActionExit(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/BLMain/MainMenu.fxml")));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**Initialization that takes Customers Name from MYsql Database and loads ComboBox with their respective names*/
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
}
