package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.SuperBo;
import com.devstack.pos.bo.custom.UserBo;
import com.devstack.pos.bo.custom.impl.UserBoImpl;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.enums.BoType;
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

public class SignupFormController {
    public AnchorPane context;
    public JFXTextField txtEmail;
    public JFXPasswordField txtPassword;

    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);

    public void btnRegisterNowOnAction(ActionEvent actionEvent) {

        try {
            if (userBo.saveUser(
                    new UserDto(
                            txtEmail.getText(), txtPassword.getText()
                    )
            )) {
                new Alert(Alert.AlertType.CONFIRMATION, "User Saved!").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtEmail.clear();
        txtPassword.clear();
    }

    public void btnAlreadyHaveAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoginForm", "Login");
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
