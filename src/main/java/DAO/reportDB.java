package DAO;

import Model.Appointment;
import Model.Reports;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static DAO.JDBC.connection;


public class reportDB {
    /**Observable List that contains all Months*/
    static ObservableList<String> months = FXCollections.observableArrayList(
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    );
    @FXML
    static ObservableList<Appointment> data = FXCollections.observableArrayList();
    /**Selects all appointments associated with England selected as country*/
    public static void updateEnglandCountryTable(TableView<Appointment>reportTableView, String division) throws SQLException {
    data.clear();

    Statement stmt = JDBC.openConnection().getConnection().createStatement();
    String query = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, a.Type, a.Start, a.End, a.Customer_ID, a.User_ID, a.Contact_ID "
            + "FROM appointments a "
            + "INNER JOIN customers c ON a.Customer_ID = c.Customer_ID "
            + "INNER JOIN first_level_divisions d ON c.Division_ID = d.Division_ID "
            + "WHERE d.Division_ID >= 101";

    PreparedStatement st = connection.prepareStatement(query);
    ResultSet rs = stmt.executeQuery(query);
    while(rs.next()) {
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
                endLocal
        ));
    }
    rs.close();
    stmt.close();

    reportTableView.setItems(data);
}
    /**Opens MySQL and counts all appointments associated with England*/
    public static int updateEnglandNumber(Label label_customer, String contact) throws SQLException {
        Statement stmt = JDBC.openConnection().getConnection().createStatement();
        String query = "SELECT COUNT(*) "
                + "FROM appointments a "
                + "INNER JOIN customers c ON a.Customer_ID = c.Customer_ID "
                + "INNER JOIN first_level_divisions d ON c.Division_ID = d.Division_ID "
                + "WHERE d.Division_ID >= 101";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        label_customer.setText(String.valueOf(count));
        return count;
    }
    /**Opens MySQL and selects all appointments associated with Canada selected as country*/
    public static void updateCanadaCountryTable(TableView<Appointment>reportTableView, String division) throws SQLException {
    data.clear();

    Statement stmt = JDBC.openConnection().getConnection().createStatement();
    String query = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, a.Type, a.Start, a.End, a.Customer_ID, a.User_ID, a.Contact_ID "
            + "FROM appointments a "
            + "INNER JOIN customers c ON a.Customer_ID = c.Customer_ID "
            + "INNER JOIN first_level_divisions d ON c.Division_ID = d.Division_ID "
            + "WHERE d.Division_ID > 54 and d.Division_ID <=72";

    PreparedStatement st = connection.prepareStatement(query);
    ResultSet rs = stmt.executeQuery(query);
    while(rs.next()) {
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
                endLocal
        ));
    }
    rs.close();
    stmt.close();

    reportTableView.setItems(data);
}
    /**Opens Mysql sets label to display total count of all appointments associated with canada*/
    public static int updateCanadaNumber(Label label_customer, String contact) throws SQLException {
        Statement stmt = JDBC.openConnection().getConnection().createStatement();
        String query = "SELECT COUNT(*) "
                + "FROM appointments a "
                + "INNER JOIN customers c ON a.Customer_ID = c.Customer_ID "
                + "INNER JOIN first_level_divisions d ON c.Division_ID = d.Division_ID "
                + "WHERE d.Division_ID > 54 and d.Division_ID <=72";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        label_customer.setText(String.valueOf(count));
        return count;
    }
    /**Opens MySQL and selects all appointments associated with USA selected as country*/
    public static void updateUSACountryTable(TableView<Appointment>reportTableView, String division) throws SQLException {
        data.clear();

        Statement stmt = JDBC.openConnection().getConnection().createStatement();
        String query = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, a.Type, a.Start, a.End, a.Customer_ID, a.User_ID, a.Contact_ID "
                + "FROM appointments a "
                + "INNER JOIN customers c ON a.Customer_ID = c.Customer_ID "
                + "INNER JOIN first_level_divisions d ON c.Division_ID = d.Division_ID "
                + "WHERE d.Division_ID <= 54";

        PreparedStatement st = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
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
                    endLocal
            ));
        }
        rs.close();
        stmt.close();

        reportTableView.setItems(data);
    }
    /**Opens MySQL and sets label to display total Count of all appointments associated with USA*/
    public static int updateUSANumber(Label label_customer, String contact) throws SQLException {
        Statement stmt = JDBC.openConnection().getConnection().createStatement();
        String query = "SELECT COUNT(*) "
                + "FROM appointments a "
                + "INNER JOIN customers c ON a.Customer_ID = c.Customer_ID "
                + "INNER JOIN first_level_divisions d ON c.Division_ID = d.Division_ID "
                + "WHERE d.Division_ID <= 54";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        label_customer.setText(String.valueOf(count));
        return count;
    }
    /**Opens MYSQL and sets tableview to display all appointments associated with their respective month*/
    public static void updateMonthTable(TableView<Appointment>reportTableView, String month) throws SQLException {
        data.clear();

        Statement stmt = JDBC.openConnection().getConnection().createStatement();
        String query = "SELECT * FROM appointments WHERE MONTH(Start) = " + (months.indexOf(month) + 1);
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
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
                    endLocal
            ));
        }
        rs.close();
        stmt.close();

        reportTableView.setItems(data);
    }
    /**Opens MYSQL and updates label to show total count of all appointments associated with their respective month*/
    public static int updateMonthNumber(Label label_customer, String month) throws SQLException {
        Statement stmt = JDBC.openConnection().getConnection().createStatement();
        String query = "SELECT COUNT(*) FROM appointments WHERE MONTH(Start) = " + (months.indexOf(month) + 1);
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        label_customer.setText(String.valueOf(count));
        return count;
    }
    /**Opens MYSQL and sets comboBox to display all types from appointments*/
    public static void getTypeOption(ComboBox<String> cb_option) throws SQLException {
        try {
            ObservableList<String> type = FXCollections.observableArrayList();
            Statement stmt = JDBC.openConnection().getConnection().createStatement();
            String query = "SELECT Type FROM appointments";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("Type");
                type.add(name);
            }
            rs.close();
            stmt.close();
            cb_option.setItems(type);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**Opens MySQL and sets Label to display all counts of their respective type*/
    public static int updateTypeNumber(Label label_customer, String type) throws SQLException {
        Statement stmt = JDBC.openConnection().getConnection().createStatement();
        String query = "SELECT COUNT(*) FROM appointments WHERE Type = '" + type + "'";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        label_customer.setText(String.valueOf(count));
        return count;
    }
    /**Opens MySQL and updates Tableview to display all appointments respective to their type*/
    public static void updateTypeTable(TableView<Appointment>reportTableView, String type) throws SQLException {
        data.clear();
        Statement stmt = JDBC.openConnection().getConnection().createStatement();
        String query = "SELECT * FROM appointments WHERE Type = '" + type + "'";
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
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
        rs.close();
        stmt.close();

        reportTableView.setItems(data);
    }
    /**Sets ComboBox with all contacts*/
    @FXML
    public static void getContactOption(ComboBox<String> cb_option) throws SQLException {
        try {
            ObservableList<String> contact = FXCollections.observableArrayList();
            Statement stmt = JDBC.openConnection().getConnection().createStatement();
            String query = "SELECT Contact_ID FROM appointments";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name1 = rs.getString("Contact_ID");
                contact.add(name1);
            }
            rs.close();
            stmt.close();
            cb_option.setItems(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**Sets Label to display total count of contacts*/
    public static int updateContactNumber(Label label_customer, String contact) throws SQLException {
        Statement stmt = JDBC.openConnection().getConnection().createStatement();
        String query = "SELECT COUNT(*) FROM appointments WHERE Contact_ID = '" + contact + "'";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        label_customer.setText(String.valueOf(count));
        return count;
    }
    /**Sets Tableview to display all contacts*/
    public static void updateContactTable(TableView<Appointment>reportTableView, String contact) throws SQLException {
        data.clear();
        Statement stmt = JDBC.openConnection().getConnection().createStatement();
        String query = "SELECT * FROM appointments WHERE Contact_ID = '" + contact + "'";
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
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
        rs.close();
        stmt.close();

        reportTableView.setItems(data);
    }


    }


