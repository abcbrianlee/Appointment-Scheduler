package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment {
    private  LocalDate startDate;
    private  LocalDate endDate;
    /**Private Integer Appointment ID*/
    private int appointmentID;
    /**Private String Title*/
    private String title;
    /**Private String Description*/
    private String description;
    /**Private String Location*/
    private String location;
    /**Private String Type*/
    private String type;
    /**Private LocalDateTime starting Time*/
    private  LocalDateTime startTime;
    /**Private LocalDateTime Ending Time*/
    private  LocalDateTime endTime;
    /**Private Integer Customer ID*/
    private static int customerID;
    /**Private integer User ID*/
    private static int userID;
    /**Private integer Contact ID*/
    private static int contactID;


    public Appointment(int appointmentID, String title, String description, String location, String type,
                       int customerID, int userID, int contactID, LocalDateTime startTime, LocalDateTime endTime) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        this.startTime = startTime;
        this.endTime = endTime;

    }
//public Appointment(int appointmentID, String title, String description, String location, String type,
//                   int customerID, int userID, int contactID, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
//    this.appointmentID = appointmentID;
//    this.title = title;
//    this.description = description;
//    this.location = location;
//    this.type = type;
//    this.customerID = customerID;
//    this.userID = userID;
//    this.contactID = contactID;
//    this.startTime = LocalDateTime.from(startTime);
//    this.endTime = LocalDateTime.from(endTime);
//    this.startDate = startDate;
//    this.endDate = endDate;
//
//
//
//}
    /**Appointment ID Getter*/
    public int getAppointmentID() {
        return appointmentID;
    }
    /**Appointment ID Setter*/
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }
    /**Title Getter*/

    public String getTitle() {
        return title;
    }
    /**Title Setter*/

    public void setTitle(String title) {
        this.title = title;
    }
    /**Description Getter*/

    public String getDescription() {
        return description;
    }
    /**Description Setter*/

    public void setDescription(String description) {
        this.description = description;
    }
    /**Location Getter*/

    public String getLocation() {
        return location;
    }
    /**Location Setter*/

    public void setLocation(String location) {
        this.location = location;
    }
    /**Type Getter*/

    public String getType() {
        return type;
    }
    /**Type Setter*/

    public void setType(String type) {
        this.type = type;
    }
    /**Customer ID Getter*/
    public static int getCustomerID() {
        return customerID;
    }
    /**Customer ID Setter*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    /**User ID Getter*/
    public static int getUserID() {
        return userID;
    }
    /**User ID Setter*/
    public void setUserID(int userID) {
        this.userID = userID;
    }
    /**Contact ID Getter*/
    public static int getContactID() {
        return contactID;
    }
    /**Contact ID  Setter*/
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
    /**
     * Starting Time Getter
     */
//    public static LocalDateTime getStartTime() {
//        return startTime;
//    }
//    /**Starting Time Setter*/
//    public void setStartTime(LocalDateTime startTime) {
//        this.startTime = startTime;
//    }
//    /**Ending Time Getter*/

    public LocalDateTime getStartTime() {
        return startTime;
    }
    /**Starting Time Setter*/
    public void setStartTime(LocalTime startTime) {
        this.startTime = LocalDateTime.from(startTime);
    }
    /**Ending Time Getter*/


    public  LocalDateTime getEndTime() {
        return endTime;
    }
    /**Ending Time Setter*/
    public void setEndTime(LocalDateTime endTime) {
        this.endTime= endTime;
    }

//    public  LocalDate getStartDate(){return startDate;}
//    public void setStartDate(LocalDate startDate){this.startDate = startDate;}
//    public  LocalDate getEndDate(){return endDate;}
//    public void setEndDate(LocalDate endDate){this.endDate = endDate;}

}
