package com.teama.controllers_refactor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.teama.login.AccessType;
import com.teama.login.LoginInfo;
import com.teama.login.LoginSubsystem;
import com.teama.login.SystemUser;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.swing.*;

public class StaffLoginPopOut extends PopOutController {
    private int xOffset, yOffset;
    private ReadOnlyDoubleProperty xProperty, yProperty;
    @FXML
    private AnchorPane loginPlane;

    @FXML
    private GridPane loginGrid;

    @FXML
    private Text AdminID;

    @FXML
    private Text passWord;

    @FXML
    private TextField IDfield;

    @FXML
    private TextField passwordField;

    @FXML
    private Button login;

    @FXML
    private Button reset;

    @FXML
    private Text admintext;

    @FXML
    private Label loginMessage;

/// new version

    @FXML
    private JFXButton loginButton;
    @FXML
    private JFXTextField uname;
    @FXML
    private JFXPasswordField pword;
    @FXML
    private JFXRadioButton admin, staff;
    @FXML
    private VBox vbox;
    @FXML
    private GridPane gPane;
    @FXML
    private Label errorMsg;

    @FXML
    private JFXButton resetBtn;
    private final BooleanProperty isLoggedIn = new SimpleBooleanProperty(false);

    public void initialize() {
        alignPane(xProperty, xOffset, yProperty, yOffset);

        setLoggedIn(false);
        admin.setVisible(false);
        staff.setVisible(false);
        System.out.println(LoginSubsystem.getInstance());
        SystemUser s = new SystemUser(new LoginInfo("admin", "adminPW"), AccessType.ADMIN);
        System.out.println(s);
        LoginSubsystem.getInstance().addUser(s);
        errorMsg.setVisible(false);
        //gPane.requestFocus();
        uname.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) {
                    uname.setPromptText("");
                }
                else if (newValue.trim().equals("")) {
                    uname.setPromptText("username");
                }
                errorMsg.setVisible(false);
            }
        });
        pword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                errorMsg.setVisible(false);
            }
        });
    }
    @Override
    public void onOpen(ReadOnlyDoubleProperty xProperty, int xOffset, ReadOnlyDoubleProperty yProperty, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xProperty = xProperty;
        this.yProperty = yProperty;
    }

    @Override
    public void onClose() {

    }

    @Override
    public String getFXMLPath() {
        return "/LoginPopOut.fxml";
    }
    //private MainScreenController parent;
    //private LoginInfo loginInfo = null;

    @FXML
    private void onBackClick(ActionEvent event){

    }

    //ToggleGroup radioButtons = new ToggleGroup();

    //radioButtons.getProperties().addListener;
    //admin.getProperties().
    //admin.setSelectedColor(Color.DARKBLUE);
    //staff.setSelectedColor(Color.DARKBLUE);
    //radioButtons.getToggles().setAll(admin, staff);
/*
    private String adminID = "admin";
    private String adminPassword = "adminPW";
    private String inputUserID, inputUserPassword;
    */


    public BooleanProperty getLoggedInProperty() {
        return isLoggedIn;
    }

    public final boolean getLoggedIn() {
        return isLoggedIn.get();
    }

    public final void setLoggedIn(Boolean update) {
        isLoggedIn.set(update);
    }

    @FXML
    private void onResetBtnClick(ActionEvent event){
        uname.setText("");
        pword.setText("");
    }

    @FXML
    private void onLoginClick(ActionEvent event){
        String username = uname.getText().trim();
        String password = pword.getText().trim();
        if (!setErrorMessage(username, password)) {
            validate(new LoginInfo(username, password));
        }
        if(isLoggedIn.getValue() == Boolean.TRUE){

            System.out.println("AAA");

        }
        /*
        inputUserID = IDfield.getText();
        inputUserPassword = passwordField.getText();
        identify();
        */
    }
    private boolean setErrorMessage(String username, String password) {
        boolean error = false;
        if (username.equals("") && password.equals("")) {
            errorMsg.setText("Please enter your Login information.");
            errorMsg.setVisible(true);
            error = true;
        } else if (username.equals("")) {
            errorMsg.setText("Please enter a valid username.");
            errorMsg.setVisible(true);
            error = true;
        } else if (password.equals("")) {
            errorMsg.setText("Please enter a valid password.");
            errorMsg.setVisible(true);
            error = true;
        }
        return error;
    }

    @FXML
    private void onEnterPressed(KeyEvent ke){
        if (ke.getCode().equals(KeyCode.ENTER)) {
            String username = uname.getText().trim();
            String password = pword.getText().trim();
            if (!setErrorMessage(username, password)) {
                validate(new LoginInfo(username, password));
            }
        }
    }


    private void validate(LoginInfo l) {
        //have this set the flag for logged in so the map system knows to close
        if (LoginSubsystem.getInstance().checkCredentials(l)) {
            Notifications notifications = Notifications.create()
                    .title("Log In Complete")
                    .text("Welcome "+ uname.getText())
                    .graphic(null)
                    .hideAfter(Duration.seconds(2))
                    .position(Pos.BOTTOM_CENTER)
                    .onAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            System.out.println("Hi Kent");
                        }
                    });
            notifications.showConfirm();
            setLoggedIn(true);
            errorMsg.setText("Login Successful");
            errorMsg.setVisible(true);
            //parent.hideLoginButton();
        }
        else {
            setLoggedIn(false);
            errorMsg.setText("Incorrect username or password.");
            errorMsg.setVisible(true);
        }
    }

}
 /*
        if (inputUserID.equals(adminID) && inputUserPassword.equals(adminPassword)){
            System.out.println("succeed");
            setLoggedIn(true);
            //this.get
            //isLoggedin = true;
            //SceneEngine.setAdminStatus(true);
            //SceneEngine.closeLogin();
            //SceneEngine.display(MainScreenController_old.class, null);
        } else{
            System.out.println("failed");
            loginMessage.setText("Incorrect user or pw.");
            IDfield.setText("");
            passwordField.setText("");
        }
        */
/*
    @FXML
    private void onResetClick(ActionEvent event){
        IDfield.setText("");
        passwordField.setText("");
    }
*/