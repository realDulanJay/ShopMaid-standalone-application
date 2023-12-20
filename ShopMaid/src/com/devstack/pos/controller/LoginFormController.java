package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.UserBo;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.util.PasswordManager;
import com.devstack.pos.util.UserSessionData;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {
    public AnchorPane context;
    public JFXTextField txtEmail;
    public JFXPasswordField txtPassword;

    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);

    public void btnSignInOnAction(ActionEvent actionEvent) {
        try {
            UserDto ud = userBo.findUser(txtEmail.getText());

            if (ud != null) {
                if (PasswordManager.checkPassword(txtPassword.getText(), ud.getPassword())) {
                    UserSessionData.email=txtEmail.getText();
                    setUi("DashboardForm", "Dashboard");
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Invalid Password!").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "User email not found!").show();
            }

        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnCreateAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUi("SignupForm", "Signup");
    }

    private void clearFields() {
        txtEmail.clear();
        txtPassword.clear();
    }

    private void setUi(String url, String title) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/" + url + ".fxml")))
        );
        stage.setTitle(title);
        stage.centerOnScreen();
    }
}
