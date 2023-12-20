package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.bo.custom.impl.CustomerBoImpl;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.view.tm.CustomerTm;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CustomerFormController {
    public AnchorPane context;
    public JFXTextField txtEmail;
    public JFXTextField txtName;
    public JFXTextField txtContact;
    public JFXTextField txtSalary;
    public JFXButton btnSaveUpdate;
    public TextField txtSearch;
    public TableView<CustomerTm> tbl;
    public TableColumn colId;
    public TableColumn colEmail;
    public TableColumn colName;
    public TableColumn colContact;
    public TableColumn colSalary;
    public TableColumn colOperate;

    String searchText = "";

    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);

    public void initialize() throws SQLException, ClassNotFoundException {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOperate.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));

        loadAllCustomers(searchText);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            try {
                loadAllCustomers(searchText);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });

        tbl.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (null != newValue) {
                        setData(newValue);
                    }
                });
    }

    private void setData(CustomerTm newValue) {
        txtEmail.setEditable(false);
        btnSaveUpdate.setText("Update Customer");

        txtEmail.setText(newValue.getEmail());
        txtName.setText(newValue.getName());
        txtContact.setText(newValue.getContact());
        txtSalary.setText(String.valueOf(newValue.getSalary()));
    }

    private void loadAllCustomers(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();

        int counter = 1;

        for (CustomerDto customerDto :
                searchText.length() > 0
                        ? customerBo.searchCustomers(searchText)
                        : customerBo.findAllCustomers()) {

            Button deleteButton = new Button("Delete");
            CustomerTm tm = new CustomerTm(
                    counter,
                    customerDto.getEmail(),
                    customerDto.getName(),
                    customerDto.getContact(),
                    customerDto.getSalary(),
                    deleteButton
            );
            deleteButton.setOnAction(e -> {
                Alert alert = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "Are you sure?",
                        ButtonType.YES, ButtonType.NO
                );
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get().equals(ButtonType.YES)) {
                    try {
                        if (customerBo.deleteCustomer(customerDto.getEmail())) {
                            new Alert(Alert.AlertType.INFORMATION, "Customer Deleted!").show();
                            loadAllCustomers(searchText);
                            clearFields();
                            btnSaveUpdate.setText("Save Customer");
                        } else {
                            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                        }

                    } catch (ClassNotFoundException | SQLException e1) {
                        new Alert(Alert.AlertType.WARNING, e1.toString()).show();
                    }


                }
            });

            counter++;
            obList.add(tm);
        }
        tbl.setItems(obList);

    }


    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm", "Dashboard");
    }

    public void btnNewCustomerOnAction(ActionEvent actionEvent) {
        clearFields();
        txtEmail.setEditable(true);
        btnSaveUpdate.setText("Save Customer");
    }

    public void btnSaveUpdateOnAction(ActionEvent actionEvent) {
        try {

            if (btnSaveUpdate.getText().equals("Save Customer")) {
                if (customerBo.saveCustomer(
                        new CustomerDto(
                                txtEmail.getText(),
                                txtName.getText(),
                                txtContact.getText(),
                                Double.parseDouble(txtSalary.getText())
                        )
                )) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer Saved!").show();
                    clearFields();
                    loadAllCustomers(searchText);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            } else {
                if (customerBo.updateCustomer(
                        new CustomerDto(
                                txtEmail.getText(),
                                txtName.getText(),
                                txtContact.getText(),
                                Double.parseDouble(txtSalary.getText()))
                )) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer Updated!").show();
                    clearFields();
                    loadAllCustomers(searchText);
                    txtEmail.setEditable(true);
                    btnSaveUpdate.setText("Save Customer");
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtEmail.clear();
        txtName.clear();
        txtContact.clear();
        txtSalary.clear();
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
