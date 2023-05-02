package DAO;

import Controller.MainMenu;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static DAO.JDBC.connection;

public class AppointmentDB {
    private static String error;
    /**Method that checks current time and searches MYsql Database for any appointments in the next 15 minutes
     * If appointment is within 15 minutes, returns appointment*/
    public static Appointment get15MinAppt() {
        Appointment appointment;
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneID = ZoneId.systemDefault();
        ZonedDateTime zoneddatetime = now.atZone(zoneID);
        LocalDateTime localdt = zoneddatetime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime local15 = localdt.plusMinutes(15);
        try {
            String query = "SELECT * FROM appointments WHERE Start BETWEEN ? AND ? AND Start >= ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTimestamp(1, Timestamp.valueOf(localdt));
            statement.setTimestamp(2, Timestamp.valueOf(local15));
            statement.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
            
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getTimestamp("End").toLocalDateTime());
                return appointment;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    /**Observable List that holds all appointments that occur now and until 1 week later*/
    public static ObservableList<Appointment> getWeeklyAppointment(LocalDate id) throws SQLException {
        ObservableList<Appointment> weeklyAppointment = FXCollections.observableArrayList();
        Appointment appointment;
        LocalDate now = LocalDate.now();
        LocalDate oneWeek = LocalDate.now().plusWeeks(1);
        try {
            Statement stmt = JDBC.openConnection();
            String query = "SELECT * FROM appointments WHERE Start >= '" + now + "' AND Start <= '" + oneWeek + "'\n" +
                    "ORDER BY Start";
            ResultSet rs = stmt.executeQuery(query);
            weeklyAppointment.clear();
            while (rs.next()) {
                Timestamp startTimeStamp = rs.getTimestamp("Start");
                Timestamp endTimeStamp = rs.getTimestamp("End");
                ZoneId localZone = ZoneId.systemDefault();
                LocalDateTime startLocal = startTimeStamp.toLocalDateTime().atZone(ZoneOffset.UTC).withZoneSameInstant(localZone).toLocalDateTime();
                LocalDateTime endLocal = endTimeStamp.toLocalDateTime().atZone(ZoneOffset.UTC).withZoneSameInstant(localZone).toLocalDateTime();
                appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"),
                        startLocal,
                        endLocal);
                weeklyAppointment.add(appointment);
            }
            stmt.close();
            return weeklyAppointment;

        } catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
    /**Observale List that holds all appointments that occur between now and until 1 month later*/
    public static ObservableList<Appointment> getMonthlyAppointment(LocalDate id) throws SQLException {
        ObservableList<Appointment> monthlyAppointment = FXCollections.observableArrayList();
        Appointment appointment;
        LocalDate now = LocalDate.now();
        LocalDate oneMonth = LocalDate.now().plusMonths(1);
        try {
            Statement stmt = JDBC.openConnection();
            String query = "SELECT * FROM appointments WHERE Start >= '" + now + "' AND Start <= '" + oneMonth + "'\n" +
                    "ORDER BY Start";
            ResultSet rs = stmt.executeQuery(query);
            monthlyAppointment.clear();
            while (rs.next()) {
                Timestamp startTimeStamp = rs.getTimestamp("Start");
                Timestamp endTimeStamp = rs.getTimestamp("End");
                ZoneId localZone = ZoneId.systemDefault();
                LocalDateTime startLocal = startTimeStamp.toLocalDateTime().atZone(ZoneOffset.UTC).withZoneSameInstant(localZone).toLocalDateTime();
                LocalDateTime endLocal = endTimeStamp.toLocalDateTime().atZone(ZoneOffset.UTC).withZoneSameInstant(localZone).toLocalDateTime();
                appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"),
                        startLocal,
                        endLocal);
                monthlyAppointment.add(appointment);
            }
            stmt.close();
            return monthlyAppointment;
        } catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
    /**Observable list that holds all appointments scheduled*/
    public static ObservableList<Appointment> getAllAppointment() {

        ObservableList<Appointment> viewAllAppointment = FXCollections.observableArrayList();
        viewAllAppointment.clear();
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
                    Appointment appointment;
                    appointment = (new Appointment(
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
                    viewAllAppointment.add(appointment);
                }
                stmt.close();
                return viewAllAppointment;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    /**Checks MySQL Database to see if any overlapping appointments are made upon saving the appointment
     * If appointment is already present, generates a user message*/
    public static boolean overlappingAppointment(String customer, LocalDateTime start_time, LocalDateTime end_time) throws SQLException {
        ZonedDateTime zdtStart = start_time.atZone(ZoneId.systemDefault());
        ZonedDateTime zdtEnd = end_time.atZone(ZoneId.systemDefault());
        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime startConvert = utcStart.toLocalDateTime();
        LocalDateTime endConvert = utcEnd.toLocalDateTime();

        Statement stmt = JDBC.openConnection().getConnection().createStatement();
        String query = "SELECT * FROM appointments WHERE (? >= Start AND ? <= End) OR (? >= Start AND ? <= End)";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setTimestamp(1, Timestamp.valueOf(startConvert));
        statement.setTimestamp(2, Timestamp.valueOf(startConvert));
        statement.setTimestamp(3, Timestamp.valueOf(endConvert));
        statement.setTimestamp(4, Timestamp.valueOf(endConvert));
        ResultSet rs = statement.executeQuery();

        int overlappingAppts = 0;
        while(rs.next()) {
            overlappingAppts++;
        } if (overlappingAppts > 0) {
            MainMenu.confirmDialog("Unable to create appointment", "This appointment overlaps with another appointment. Please select a different time.");
            return false;
        } else{
            return true;
        }
    }
    /**Opens MySQL database and deletes appointment*/
    public static void deleteAppointment(int appointmentID) throws SQLException {
        try {
            Statement stmt = JDBC.openConnection().getConnection().createStatement();
            String query = "DELETE FROM appointments WHERE Appointment_ID = " + appointmentID;
            stmt.executeUpdate(query);
            System.out.println("Appointment deleted successfully");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    /**Converts user entered time and date that can be properly formatted to be parsed into database*/
    public static LocalDateTime convertToDateAndTime(LocalDate Date, String Time){
            String str = Date + " " + Time + ":00";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
            System.out.println(dateTime);
            return dateTime;
        }
    /**Converts time to a properly formatted time to be parsed*/
    public static LocalTime convertToLocalTime(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        String formattedTime = time.format(DateTimeFormatter.ofPattern("H:mm:ss"));
        System.out.println("Time: " + formattedTime);
        return LocalTime.parse(formattedTime, DateTimeFormatter.ofPattern("H:mm:ss"));
    }
    public static LocalDate convertToLocalDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateTime.format(formatter), formatter);
        System.out.println("Date: " + date);
        return date;
    }

    public static LocalTime convertToTime(String Time){
        String str = Time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        LocalTime startz = LocalTime.parse(str, formatter);
        return startz;

    }
    /**Takes user entered input and updates appointment through MySQL database*/
    public static void updateAppointment(int appointmentID, String title, String description, String location, String type,
                                         String contact, String user, String customer, LocalDateTime apptStart, LocalDateTime apptEnd) throws SQLException {
        ZonedDateTime zdtStart = apptStart.atZone(ZoneId.systemDefault());
        ZonedDateTime zdtEnd = apptEnd.atZone(ZoneId.systemDefault());
        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime startConvert = utcStart.toLocalDateTime();
        LocalDateTime endConvert = utcEnd.toLocalDateTime();
        try {
            String query = "UPDATE appointments SET Customer_ID=?, Title=?, Description=?, Contact_ID=?, Location=?, Type=?, Create_Date=NOW(), Created_By='', Last_Update=NOW(), Last_Updated_By='', User_ID=?, Start=?, End=? WHERE Appointment_ID=?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, customer);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4, contact);
            statement.setString(5, location);
            statement.setString(6, type);
            statement.setString(7, user);
            statement.setTimestamp(8, Timestamp.valueOf(startConvert));
            statement.setTimestamp(9, Timestamp.valueOf(endConvert));
            statement.setInt(10, appointmentID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**takes User entered input and adds appointment into mySQL Database*/
    public static void addAppointment(int appointmentID, String title, String description, String location, String type,
                                      String contact, String customer, String user,
                                      LocalDateTime apptStart, LocalDateTime apptEnd) {

        ZonedDateTime zdtStart = apptStart.atZone(ZoneId.systemDefault());
        ZonedDateTime zdtEnd = apptEnd.atZone(ZoneId.systemDefault());
        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime startConvert = utcStart.toLocalDateTime();
        LocalDateTime endConvert = utcEnd.toLocalDateTime();
        try {
            Statement stmt = JDBC.openConnection();
            String query = "INSERT INTO appointments (Appointment_ID, Customer_ID, Title, Description, Type, Contact_ID, Location, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, User_ID) " +
                    "VALUES ('" + appointmentID + "', '" + customer + "', '" + title + "', '" + description + "', '" + type + "', '" + contact + "', '" + location + "', '" + startConvert + "', '" + endConvert + "', NOW(), '', NOW(), '', '" + user + "')";
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        try {
//            Statement stmt = JDBC.openConnection();
//            String query = "INSERT INTO appointments (Appointment_ID, Customer_ID, Title, Description, Type, Contact_ID, Location, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, User_ID) " +
//                    "VALUES ('" + appointmentID + "', '" + customer + "', '" + title + "', '" + description + "', '" + type + "', '" + contact + "', '" + location + "', '" + apptStart + "', '" + apptEnd + "', NOW(), '', NOW(), '', '" + user + "')";
//            stmt.executeUpdate(query);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }



    }
    /**Assigns appointment ID that is auto generated*/
    public static Integer assignAppointmentID() throws SQLException {
        Integer appointmentID = 1;
        try{
            Statement stmt = JDBC.openConnection();
            String query = "SELECT Appointment_ID FROM appointments ORDER BY Appointment_ID;";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                if(rs.getInt("Appointment_ID") == appointmentID) {
                    appointmentID++;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return appointmentID;
    }
    /**Takes appointment and checks to see if all entries are properly entered
     * Generates user message if not entered correctly*/
    public static Boolean validateAppointment(Integer appointmentID, String title, String description, String location, String type,
                                              String contact, String customer, String user,
                                              LocalDateTime apptStart, LocalDateTime apptEnd) {
        if(!validAppointmentID(String.valueOf(appointmentID)) || !validTitle(title) || !validDescription(description) || !validLocation(location) || !validType(type)
            || !validContact(contact) || !validCustomer(customer) || !validUser(user) || !validStartDate(LocalDate.from(apptStart)) || !validEndDate(LocalDate.from(apptEnd))) {
            System.out.println("Error");
            MainMenu.infoDialog("Error!","Error!", error);
            return Boolean.FALSE;

        }
        else {
            return Boolean.TRUE;
        }
    }

    private static boolean validAppointmentID(String appointmentID) {
        if(appointmentID.isEmpty()) {
            error = "Please enter a name";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    /**Method checks to see if title is entered*/
    private static boolean validTitle(String title) {
        if(title.isEmpty()) {
            error = "Please enter a title";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    /**Method checks to see if Description is entered*/
    private static boolean validDescription(String description) {
        if(description.isEmpty()) {
            error = "Please enter a description";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    /**Method checks to see if Location is entered*/
    private static boolean validLocation(String location) {
        if(location.isEmpty()) {
            error = "Please enter a location";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    /**Method checks to see if type is entered*/
    private static boolean validType(String type) {
        if(type.isEmpty()) {
            error = "Please enter a type";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    /**Method checks to see if contact is entered*/
    private static boolean validContact(String contact) {
        if(contact == null || contact.isEmpty()) {
            error = "Please enter a contact";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    /**Method checks to see if customer is entered*/
    private static boolean validCustomer(String customer) {
        if(customer == null || customer.isEmpty()) {
            error = "Please enter a customer";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    /**Method checks to see if user is entered*/
    private static boolean validUser(String user) {
        if(user == null || user.isEmpty()) {
            error = "Please enter a user";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    /**Method checks to see if Starting Date is entered*/
    private static boolean validStartDate(LocalDate apptStart) {
        if(apptStart == null) {
            error = "Please enter a start date";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    /**Method checks to see if Ending Date is entered*/
    private static boolean validEndDate(LocalDate apptEnd) {
        if(apptEnd == null) {
            error = "Please enter a end date";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
}
