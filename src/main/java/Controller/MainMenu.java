package Controller;

import DAO.AppointmentDB;
import DAO.CustomerDB;
import DAO.JDBC;
import DAO.userDB;
import Model.Appointment;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import static DAO.JDBC.connection;
import java.time.ZoneId;



public class MainMenu implements Initializable {
private static Stage stage;
private static Object scene;
    /**Appointment TableView*/
@FXML private TableView<Appointment> appointmentTableView;
    /**Customer TableView*/
 @FXML private TableView<Customer> customerTableView;
    /**Appointment ID TableColumn*/
@FXML private TableColumn<Appointment, Integer> tcID;
    /**Appointment Title TableColumn*/
@FXML private TableColumn<Appointment, String> tcTitle;
    /**Appointment Description TableColumn*/
@FXML private TableColumn<Appointment, String> tcDescription;
    /**Appointment Location TableColumn*/
@FXML private TableColumn<Appointment, String> tcLocation;
    /**Appointment Type TableColumn*/
@FXML private TableColumn<Appointment, String> tcType;
    /**Appointment Starting Date TableColumn*/
@FXML private TableColumn<Appointment, LocalDateTime> tcStartDate;
    /**Appointment Ending Date TableColumn*/
@FXML private TableColumn<Appointment, LocalDateTime> tcEndDate;
    /**AAppointment Contact TableColumn*/
@FXML private TableColumn<Appointment, String> tcContact;
    /**Appointment Customer ID TableColumn*/
@FXML private TableColumn<Appointment, Integer> tcCustomerID;
    /**Appointment User ID TableColumn*/
@FXML private TableColumn<Appointment, Integer> tcUserID;
    /**Customer ID TableColumn*/
@FXML private TableColumn<Customer, Integer> customer_ID;
    /**Customer Name TableColumn*/
@FXML private TableColumn<Customer, String> customer_Name;
    /**Customer Address TableColumn*/
@FXML private TableColumn<Customer, String> customer_Address;
    /**Customer Phone TableColumn*/
@FXML private TableColumn<Customer, Integer> customer_Phone;
    /**Customer Postal TableColumn*/
@FXML private TableColumn<Customer, String> customer_Postal;
    /**Customer Division ID TableColumn*/
@FXML private TableColumn<Customer, Integer> customer_divisionID;
    /**Month RadioButton*/

@FXML private RadioButton RB_Month;
    /**Week RadioButton*/
@FXML private RadioButton RB_Week;
    /**View All RadioButton*/
@FXML private RadioButton RB_View;
    /**Observable List that shows all appointments to be loaded to TableView*/


@FXML  ObservableList<Appointment> data = FXCollections.observableArrayList();
    /**Observable List that shows all Customers to be loaded to Tableview*/
@FXML ObservableList<Customer> data2  = FXCollections.observableArrayList();
    /**Takes user selected appointment and deletes it from MYsql Database and tableview.
     * Generates message to user of type and ID that was deleted
     * Generates message if no selection is made.
     * @param event  Appointment Delete Button*/
    @FXML public void onActionAppointmentDelete(ActionEvent event) throws SQLException {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            infoDialog("No selection made", "Please select an appointment", "Please select an appointment");
        } else {
            boolean result = confirmDialog("Would you like to delete this appointment?", "Would you like to delete this appointment?");
            if (result) {
                AppointmentDB.deleteAppointment(selectedAppointment.getAppointmentID());
                appointmentTableView.setItems(AppointmentDB.getAllAppointment());
                infoDialog("Appointment deleted", "Appointment deleted","Appointment (" + selectedAppointment.getType() + ")(ID#" + selectedAppointment.getAppointmentID() + ") successfully deleted.");
            }
        }
        }
    /**Takes User selected Customer and deletes it from MySQL database and TableView
     * If selected customer still has open appointment, an error is generated to user to delete associated appointment
     * If no selection is made, error message is generated
     * Generates confirmation
     * @param event  Customer Delete Button*/
    @FXML public void onActionCustomerDelete(ActionEvent event) throws SQLException, IOException {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if(selectedCustomer == null) {
            infoDialog("No selection made", "Please select a customer", "Please select an customer");
        } else {
            try {
                boolean result = confirmDialog("Would you like to delete this customer?", "Would you like to delete this customer? Before deleting, make sure all associated appointments are deleted first.");
                if (result) {
                    boolean success = CustomerDB.deleteCustomer(selectedCustomer.getCustomer_ID());
                    if (success) {
                        customerTableView.setItems(CustomerDB.getAllCustomer());
                        infoDialog("Customer deleted", "Customer deleted", "Customer " + selectedCustomer.getCustomer_Name() + " successfully deleted.");
                    }
                }
            } catch (SQLIntegrityConstraintViolationException e){
                infoDialog("Deletion error", " Deletion Error", "Please delete appointments associated with customer before deletion.");
            }
        }
    }
    /**Takes selected appointment and transfers data to Modify Appointment Controller page
     * If no selection is made, no action happens
     * @param event Appointment Modification Page*/
    @FXML
    public void onActionAppointmentModify(ActionEvent event) throws IOException, SQLException {
        Appointment selectedappointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if(selectedappointment == null) {
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/modifyAppointment.fxml"));
        loader.load();

        modifyAppointmentController MCController = loader.getController();
        MCController.sendAppointment(appointmentTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Appointment Management System");
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**Takes User to add Appointment Page
     * @param event  Add Appointment Page*/
    @FXML
    public void onActionAddAppointment(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(userDB.class.getResource("/appointment.fxml"));
        stage.setTitle("Add Customer Page");
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**Takes User to Report Page
     * @param event  Report Page*/
    public void onActionReport(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(userDB.class.getResource("/report.fxml"));
        stage.setTitle("Add Customer Page");
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**Takes user selected customer and transfers data to Customer Modification Page
     * If no selection is made, no event occurs
     * @param event Customer Modification page*/
    @FXML
    public void onActionCustomerModify(ActionEvent event) throws IOException {
        Customer selectedPart = customerTableView.getSelectionModel().getSelectedItem();
        if(selectedPart == null) {
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/modifyCustomer.fxml"));
        loader.load();


        modifyCustomerController MCController = loader.getController();
        MCController.sendCustomer(customerTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Customer Management System");
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**Takes User to Add Customer Page
     * @param event  Add Customer Page*/
    @FXML
    public void onActionCustomerAdd(ActionEvent event) throws IOException {
    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    scene = FXMLLoader.load(userDB.class.getResource("/customer.fxml"));
    stage.setTitle("Add Customer Page");
    stage.setScene(new Scene((Parent) scene));
    stage.show();
}
    /**Sets Tableview to return weekly, monthly or all Appointments upon radioButton Selection
     * @param event Tableview Filter*/
    @FXML
    public void radioSelection(ActionEvent event) throws IOException, SQLException {
    if(RB_Week.isSelected()){
        System.out.println("Week selected");
        LocalDate now = LocalDate.now();
        appointmentTableView.setItems(AppointmentDB.getWeeklyAppointment(now));
    }
    if(RB_Month.isSelected()){
        System.out.println("Month selected");
        LocalDate now = LocalDate.now();
        appointmentTableView.setItems(AppointmentDB.getMonthlyAppointment(now));
    }
    if(RB_View.isSelected()) {
        System.out.println("VIEW ALL");

        appointmentTableView.setItems(data);
    }

}
    /**Logs user out and returns User to Login Page
     * @param event Logout Page*/
    @FXML
    public void onActionLogout(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/login.fxml"));
        stage.setTitle("Appointment Management System");
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**Confirmation method*/
    public static boolean confirmDialog(String title, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Confirm");
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            return true;
        }
        else{
            return false;
        }
    }
    /**Alert method*/
    public static void infoDialog(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    /**Initialization that populates Customer and Appointment Tableviews with their respective MySQL Database
     * Upon initialization, checks to see if any appointments are scheduled in the next 15 minutes that will generate message
     * If no appointments are in 15 minutes, a message is still prompt*/
@Override
public void initialize(URL url, ResourceBundle resourceBundle) {

    try {
        JDBC.openConnection().getConnection().createStatement();
        Statement stmt = connection.createStatement();
        try (ResultSet rs = stmt.executeQuery("SELECT * FROM appointments")) {
            while (rs.next()) {
                Timestamp startTimeStamp = rs.getTimestamp("Start");
                Timestamp endTimeStamp = rs.getTimestamp("End");
                Instant startInstant = startTimeStamp.toInstant();
                Instant endInstant = endTimeStamp.toInstant();
                ZoneId localZone = ZoneId.systemDefault();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startInstant, localZone);
                ZonedDateTime endLocal = ZonedDateTime.ofInstant(endInstant, localZone);

                data.add(new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"),
                        startLocal.toLocalDateTime(),
                        endLocal.toLocalDateTime()));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

        try{
            JDBC.openConnection().getConnection().createStatement();
            Statement stmt = connection.createStatement();
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {
                while (rs.next()) {
                    data2.add(new Customer(
                            rs.getInt("Customer_ID"),
                            rs.getString("Customer_Name"),
                            rs.getString("Address"),
                            rs.getString("Phone"),
                            rs.getInt("Division_ID"),
                            rs.getString("Postal_Code")));
                }
            } catch (SQLException e) {
            e.printStackTrace();
        }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        customerTableView.setItems(data2);
        customer_ID.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));
        customer_Name.setCellValueFactory(new PropertyValueFactory<>("customer_Name"));
        customer_Address.setCellValueFactory(new PropertyValueFactory<>("customer_Address"));
        customer_Phone.setCellValueFactory(new PropertyValueFactory<>("customer_Phone"));
        customer_Postal.setCellValueFactory(new PropertyValueFactory<>("customer_Postal"));
        customer_divisionID.setCellValueFactory(new PropertyValueFactory<>("customer_divisionID"));
        customerTableView.getSelectionModel().selectFirst();

        appointmentTableView.setItems(data);
        tcID.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        tcTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tcDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tcLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        tcType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        tcCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        tcContact.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
        tcUserID.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        tcStartDate.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        tcEndDate.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        appointmentTableView.getSelectionModel().selectFirst();

        Appointment appointment = AppointmentDB.get15MinAppt();
        if(appointment != null) {
            Customer customer = (Customer.getCustomer(appointment.getCustomerID()));
            String alertMessage = String.format("You have an appointment with %s  (Appointment ID %s) at %s",
                    customer.getCustomer_Name(),
                    appointment.getAppointmentID(),
                    appointment.getStartTime());
            MainMenu.confirmDialog("Appointment Reminder!", alertMessage);
            System.out.print("You have appt now");
        } else {
            String alertMessage1 = String.format("You have no appointments");
            MainMenu.confirmDialog("No appointment!", alertMessage1);
        }
}


}
