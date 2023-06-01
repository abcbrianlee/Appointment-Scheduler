package DAO;

import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.MessageFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

import static DAO.JDBC.connection;


public class userDB implements Initializable {
private static User currentUser;
private static Stage stage;
private static Object scene;


    /**Opens MySQL Database and takes user entered input and checks database to see if login credentials match
     * Error is generated if credentials don't match*/
    @FXML
    public static boolean loginUser(ActionEvent event, String username, String password) throws SQLException, IOException, Exception{

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try{
            JDBC.openConnection().getConnection().createStatement();
            preparedStatement = connection.prepareStatement(("SELECT password, User_Name FROM users WHERE User_Name = ?"));
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
            } else {
                while(resultSet.next()) {
                    String retrievePassword = resultSet.getString("Password");
                    String retrieveUsername = resultSet.getString("User_Name");
                    if (retrievePassword.equals(password)) {
                        System.out.println("correct");
                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(userDB.class.getResource("/MainMenu.fxml"));
                        stage.setTitle("Appointment System");
                        stage.setScene(new Scene((Parent) scene));
                        stage.show();
                        return true;
                    }
                    else {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("incorrect");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e ) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (connection != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}


