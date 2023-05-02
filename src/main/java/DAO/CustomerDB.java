package DAO;

import Controller.MainMenu;
import Model.Customer;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

import static DAO.JDBC.connection;

public class CustomerDB {
private static String error;
    /**Observable list of all customers is returned*/
    public static ObservableList<Customer> getAllCustomer() {
        Customer.data2.clear();
        try{

            JDBC.openConnection().getConnection().createStatement();
            Statement stmt = connection.createStatement();
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {
                while (rs.next()) {
                    Customer customer = (new Customer(
                            rs.getInt("Customer_ID"),
                            rs.getString("Customer_Name"),
                            rs.getString("Address"),
                            rs.getString("Phone"),
                            rs.getInt("Division_ID"),
                            rs.getString("Postal_Code")));
                            Customer.data2.add(customer);
                }
                stmt.close();
                return Customer.data2;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    /**Opens MySQL Database and enters in Customer*/
    public static void addCustomer(Integer customerID,String name, String address, String phone, String postal, String division) throws SQLException {
        try {

            if(validateCustomer(name, address, phone, postal, division)) {
                Statement stmt = JDBC.openConnection();
                String queryAddress = "INSERT INTO customers SET Customer_Name='" + name + "',Customer_ID = '" + customerID + "', Address='" + address + "'," +
                        "  phone='" + phone + "', Postal_Code='" + postal + "', Create_Date=NOW(), Last_Update=NOW(), Created_By=' Script', Last_Updated_By=' Script', Division_ID ='" + division + "' ";
                stmt.executeUpdate(queryAddress);
                stmt.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("Successfully added customer");
    }
    /**Opens Mysql Database and updates Customer*/
    public static void modifyCustomer(String name, String address, String phone, String postal, Integer customerID, Integer division) throws SQLException {
        Statement stmt = JDBC.openConnection();
        String query = "UPDATE customers SET Customer_Name='" + name + "', Address = '" + address + "', Phone = '" + phone + "', Division_ID = '" + division + "', Postal_Code = '" + postal + "', Last_Update=NOW(), Last_Updated_By= 'user'  WHERE Customer_ID='" + customerID + "'";
        stmt.executeUpdate(query);
        System.out.println("Successfully updated");

    }
    /**Opens MySQL database and deletes customer
     * Checks to see if Customer has associated appointment that must be deleted first, otherwise user message is prompt*/
    public static boolean deleteCustomer(Integer customerID) throws SQLException {
        try {
            Statement statement = JDBC.openConnection().getConnection().createStatement();
            String query1 = "SELECT COUNT(*) FROM appointments WHERE Customer_ID = " + customerID;
            ResultSet rs = statement.executeQuery(query1);
            rs.next();
            int appointmentCount = rs.getInt(1);

            if (appointmentCount > 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Delete Customer");
                alert.setHeaderText("Unable to delete customer");
                alert.setContentText("This customer has appointments set up. Please delete appointments first.");
                alert.showAndWait();
                return false;
            } else {
                Statement stmt = JDBC.openConnection().getConnection().createStatement();
                String query = "DELETE FROM customers WHERE Customer_ID = " + customerID;
                stmt.executeUpdate(query);
                System.out.println("Deleted customer successfully!");
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
    /**Checks to see if all inputted information has been properly filled.*/
    public static Boolean validateCustomer(String name, String address, String phone, String postal, String division) {
        error = "";
    if(!validName(name) || !validAddress(address) || !validPhone(phone) || !validPostal(postal) || !validDivision(division)) {
        System.out.println("Error");
        MainMenu.infoDialog("Error!","Error!", error);
        return Boolean.FALSE;
    }
    else {
        return Boolean.TRUE;
    }
}
    /**Method checks to see if name has been entered*/
    private static boolean validName(String name) {
    if(name.isEmpty()) {
        error = "Please enter a name";
        return Boolean.FALSE;
    } else {
        return Boolean.TRUE;
    }
}
    /**Method checks to see if address has been entered*/
    private static boolean validAddress(String address) {
        if(address.isEmpty()) {
            error = "Please enter an address";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    /**Method checks to see if postal has been entered*/
    private static boolean validPostal(String postal) {
        if(postal.isEmpty()) {
            error = "Please enter a zip code";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    /**Method checks to see if phone has been entered*/
    private static boolean validPhone(String phone) {
        if(phone.isEmpty()) {
            error = "Please enter a phone number";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    /**Method checks to see if division has been entered*/
    private static boolean validDivision(String division) {
        if(division == null || division.isEmpty()) {
            error = "Please enter a division";
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    /**Assigns customer ID that is uniquely auto generated by selecting through MySQL database*/
    public static Integer assignCustomerID() throws SQLException {
        Integer customerID = 1;
        try {
            Statement stmt = JDBC.openConnection();
            String query = "SELECT Customer_ID FROM customers ORDER BY Customer_ID";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                if (rs.getInt("Customer_ID") == customerID) {
                    customerID++;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return customerID;
    }
}
