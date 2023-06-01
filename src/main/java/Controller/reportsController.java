package Controller;

import DAO.JDBC;
import DAO.reportDB;
import Model.Appointment;
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

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.ResourceBundle;

import static DAO.JDBC.connection;

public class reportsController implements Initializable {
    /**Reports TableView*/
@FXML private TableView <Appointment> reportTableView;
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
    /**Appointment Start Date TableColumn*/
@FXML private TableColumn<Appointment, LocalDateTime> tcStartDate;
    /**Appointment End Date TableColumn*/
@FXML private TableColumn<Appointment, LocalDateTime> tcEndDate;
    /**Appointment Contact TableColumn*/
@FXML private TableColumn<Appointment, String> tcContact;
    /**Appointment Customer ID TableColumn*/
@FXML private TableColumn<Appointment, Integer> tcCustomerID;
    /**Appointment USER ID TableColumn*/
@FXML private TableColumn<Appointment, Integer> tcUserID;
    /**Type RadioButton*/
@FXML private RadioButton rb_type;
    /**Contact RadioButton*/
@FXML private RadioButton rb_contact;
    /**Month RadioButton*/
@FXML private RadioButton rb_month;
    /**Country RadioButton*/
@FXML private RadioButton rb_country;
    /**View All RadioButton*/
@FXML private RadioButton rb_viewAll;
    /**Customer Label*/
@FXML private Label label_customer;
    /**Customer Option Label*/
@FXML private Label label_options;
    /**ComboBox Options*/
@FXML private ComboBox <String> cb_option;
    /**Observable List with all Appointments*/
@FXML ObservableList<Appointment> data = FXCollections.observableArrayList();
    /**Lambda Expression used here. Lambda Expression is used for all 4 ComboBox selections that is triggered when the user selects an item. The Lambda expression separates the parameters (event1) from its body.
     * If RadioButton Type is selected, populates ComboBox with all entries via MySQL search. Sets Label to "Type" and inserts total count of appointments and populates tableview with Appointments
     * If RadioButton Contact is selected, populates ComboBox with all entries via MySQL search. Sets label to "Contact" and inserts total count of appointments and sets tableview with all appointments associated with contact
     * If radiobutton Month is selected, populates ComboBox with all months of the year. Upon selection of month, populates and counts total appointments associated with that month.
     * If Radiobutton Country is selected, populates ComboBo with all 3 countries and upon selection, counts and populates tableview with all appointments associated with that country via Customer ID
     * @param event RadioButton Selection Button*/
    public void onActionRadio(ActionEvent event) throws SQLException {
        if (rb_type.isSelected()) {
            reportDB.getTypeOption(cb_option);
            label_options.setText("Type");
            cb_option.setOnAction(event1 -> {
                String selectedOption = cb_option.getSelectionModel().getSelectedItem();
                if (selectedOption != null) {
                    System.out.println("S: " + selectedOption);
                    try {
                        reportDB.updateTypeTable(reportTableView, selectedOption);
                        reportDB.updateTypeNumber(label_customer, selectedOption);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        if (rb_contact.isSelected()) {
            try {
                reportDB.getContactOption(cb_option);
                label_options.setText("Contact");
                cb_option.setOnAction(event1 -> {
                    String selectedContact = cb_option.getSelectionModel().getSelectedItem();
                    if (selectedContact != null) {
                        System.out.println("Contact: " + selectedContact);
                        try {
                            reportDB.updateContactTable(reportTableView, selectedContact);
                            reportDB.updateContactNumber(label_customer, selectedContact);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
            if (rb_month.isSelected()) {
               ObservableList<String> months = FXCollections.observableArrayList(
     "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
            );
            cb_option.setItems(months);
            label_options.setText("Month");

            cb_option.setOnAction(event1 -> {
                String selectedMonth = cb_option.getSelectionModel().getSelectedItem();
                if(selectedMonth != null) {
                    System.out.println("Month: " + selectedMonth);
                    try{
                        reportDB.updateMonthTable(reportTableView,selectedMonth);
                        reportDB.updateMonthNumber(label_customer,selectedMonth);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                    }
                    );
            }

        if (rb_country.isSelected()) {
            ObservableList<String> Country = FXCollections.observableArrayList("USA","Canada","England");
            cb_option.setItems(Country);
            cb_option.setOnAction(event1 -> {
                        String selectedDivision = cb_option.getSelectionModel().getSelectedItem();
                        if(selectedDivision != null) {
                            try{
                                if(selectedDivision == "USA"){
                                    reportDB.updateUSACountryTable(reportTableView,selectedDivision);
                                    reportDB.updateUSANumber(label_customer,selectedDivision);
                                }
                                if(selectedDivision == "Canada"){
                                    reportDB.updateCanadaCountryTable(reportTableView,selectedDivision);
                                    reportDB.updateCanadaNumber(label_customer,selectedDivision);
                                }
                                if(selectedDivision == "England"){
                                    reportDB.updateEnglandCountryTable(reportTableView,selectedDivision);
                                    reportDB.updateEnglandNumber(label_customer,selectedDivision);
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
            );
        }
        if(rb_viewAll.isSelected()){
            reportTableView.setItems(data);
        }
        }
    /**Exit Button returns to BLMain.Main Menu
     * @param event Exit Button*/
    public void onActionExit(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainMenu.fxml")));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**Initializable that defines and sets Tableview to load all appointments*/
@Override
public void initialize(URL url, ResourceBundle resourceBundle) {
    try{
        JDBC.openConnection().getConnection().createStatement();
        Statement stmt = connection.createStatement();
        try (ResultSet rs = stmt.executeQuery("SELECT * FROM appointments")) {
            while (rs.next()) {
                Timestamp startTimeStamp = rs.getTimestamp("Start");
                Timestamp endTimeStamp = rs.getTimestamp("End");
                ZoneId localZone = ZoneId.systemDefault();
                LocalDateTime startLocal = startTimeStamp.toLocalDateTime().atZone(ZoneOffset.UTC).withZoneSameInstant(localZone).toLocalDateTime();
                LocalDateTime endLocal = endTimeStamp.toLocalDateTime().atZone(ZoneOffset.UTC).withZoneSameInstant(localZone).toLocalDateTime();

                data.add(new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"),
                        startLocal,
                        endLocal));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    reportTableView.setItems(data);
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
    reportTableView.getSelectionModel().selectFirst();
    }
}
