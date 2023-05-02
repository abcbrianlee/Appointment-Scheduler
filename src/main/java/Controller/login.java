package Controller;

import BLMain.Logger;
import DAO.userDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class login implements Initializable {
    /**
     * LoginField Label
     */
    @FXML
    private Label loginField;
    /**
     * UserName Label
     */
    @FXML
    private Label username;
    /**
     * Password Label
     */
    @FXML
    private Label password;
    /**
     * TimeZone Label
     */
    @FXML
    private Label timezone;
    /**
     * Login Button
     */
    @FXML
    private Button button_login;
    /**
     * Exit Button
     */
    @FXML
    private Button exit;
    private String alertTitle;
    private String alertHeader;
    private String alertContext;
    /**
     * Username TextField
     */
    @FXML
    private TextField UserNameTextField;
    /**
     * Password Textfield
     */
    @FXML
    private TextField PasswordTextField;
    /**
     * TimeZone Textfield
     */
    @FXML
    private TextField timezoneTextField;
    ResourceBundle rb = ResourceBundle.getBundle("BLMain/Nat_fr", Locale.getDefault());

    /**
     * Method that takes username and password and passes as parameter to check if Mysql login credentials match
     * If successful, username and success is logged into .txt file
     * If not successful, username and fail is logged into .txt file
     *
     * @param event Login Button
     */
    @FXML
    public void onActionLogin(ActionEvent event) throws Exception {
        if (userDB.loginUser(event, UserNameTextField.getText(), PasswordTextField.getText())) {
            Logger.log(UserNameTextField.getText(), true);
        } else {
            Logger.log(UserNameTextField.getText(), false);
                Alert alert = new Alert(Alert.AlertType.ERROR);

                String message = rb.getString("Incorrect");
            alert.setContentText(message);
                alert.show();
        }
    }

    /**
     * Exit the program
     *
     * @param event Exit Button
     */
    public void onActionExit(ActionEvent event) throws Exception {
        System.exit(0);
    }

    /**
     * Sets the Timezone TextField to store current users timezone.
     * Takes all texts/labels and errors and replaces them in french language if system detects French as software language
     */
    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);
            ZoneId zone = ZoneId.systemDefault();
            timezoneTextField.setText(String.valueOf(zone));

            if (Locale.getDefault().getLanguage().equals("fr")) {
                loginField.setText(rb.getString("Login"));
                username.setText(rb.getString("Username"));
                password.setText(rb.getString("Password"));
                timezone.setText(rb.getString("Location"));
                button_login.setText(rb.getString("Login"));
                exit.setText(rb.getString("Exit"));


            }
            } catch(MissingResourceException e){
                System.out.println("Resource file missing: " + e);
            } catch(Exception e)
            {
                System.out.println(e);
            }

        }
    }

