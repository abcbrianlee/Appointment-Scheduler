package Model;

import DAO.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer {
    /**Private Integer Customer ID*/
private Integer customer_ID;
    /**Private String Customer Name*/
private String customer_Name;
    /**Private String Customer Address*/
private String customer_Address;
    /**Private String Customer Phone*/
private String customer_Phone;
    /**Private String Customer Postal*/
private String customer_Postal;
    /**Private String Customer Division ID*/
private Integer customer_divisionID;
    /**Observable List for Customers*/
private ObservableList<String> customer_Country;
    @FXML public static ObservableList<Customer> data2  = FXCollections.observableArrayList();

    public Customer(int customer_ID,String customer_Name, String customer_Address, String customer_Phone
            ,Integer customer_divisionID, String customer_Postal, ObservableList<String> customer_Country) {
        this.customer_ID = customer_ID;
        this.customer_Name=customer_Name;
        this.customer_Address=customer_Address;
        this.customer_Phone=customer_Phone;
        this.customer_divisionID = customer_divisionID;
        this.customer_Postal=customer_Postal;
        this.customer_Country= customer_Country;
    }

    public Customer(int customer_ID,String customer_Name, String customer_Address, String customer_Phone
            ,Integer customer_divisionID, String customer_Postal) {
        this.customer_ID = customer_ID;
        this.customer_Name=customer_Name;
        this.customer_Address=customer_Address;
        this.customer_Phone=customer_Phone;
        this.customer_divisionID = customer_divisionID;
        this.customer_Postal=customer_Postal;
    }

    /**Customer ID Getter*/
    public static String getCustomerID(String customer_Name) throws SQLException {
        try {
            Statement stmt = JDBC.openConnection().getConnection().createStatement();
            String query = "SELECT Customer_ID FROM customers WHERE Customer_Name ='" + customer_Name + "'";
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                String customerID = (rs.getString("Customer_ID"));
                stmt.close();
                return customerID;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    /**Customer ID Setter*/
    public static Customer getCustomer(int customer_ID) {
        try {
            Statement statement = JDBC.openConnection().getConnection().createStatement();
            String query = "SELECT * FROM customers WHERE Customer_ID = " + customer_ID;
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                Customer customer = new Customer(rs.getInt("Customer_ID"), rs.getString("Customer_Name"), rs.getString("Address"), rs.getString("Phone"), rs.getInt("Division_ID"), rs.getString("Postal_Code"));
                customer.setCustomer_Name(rs.getString("Customer_Name"));
                statement.close();
                return customer;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    /**Customer ID Getter*/
    public Integer getCustomer_ID() {return customer_ID;}
    /**Customer ID Setter*/
    public void setCustomer_ID(Integer customer_ID) {this.customer_ID=customer_ID;}
    /**Customer Name Getter*/
    public String getCustomer_Name() {return customer_Name;}
    /**Customer Name Setter*/
    public void setCustomer_Name(String customer_Name) {this.customer_Name=customer_Name;}
    /**Customer Address Getter*/
    public String getCustomer_Address() {return customer_Address;}
    /**Customer Adrdress Setter*/
    public void setCustomer_Address(String customer_address) {this.customer_Address=customer_Address;}
    /**Customer Phone Getter*/
    public String getCustomer_Phone() {return customer_Phone;}
    /**Customer Phone Setter*/
    public void setCustomer_Phone(String customer_Phone) {this.customer_Phone=customer_Phone;}
    /**Customer Postal Getter*/
    public String getCustomer_Postal() {return customer_Postal;}
    /**Customer Phone Setter*/
    public void setCustomer_Postal(String customer_Postal) {this.customer_Postal=customer_Postal;}
    /**Customer Division ID Getter*/
    public Integer getCustomer_divisionID() {return customer_divisionID;}
    /**Customer Division ID Setter*/
    public void setCustomer_divisionID(Integer customer_divisionID) {this.customer_divisionID=customer_divisionID;}

    public ObservableList<String> getCustomer_Country() {
        return customer_Country;
    }
    public void setCustomer_Country(ObservableList<String> customer_Country) {
        this.customer_Country=customer_Country;
    }
}
