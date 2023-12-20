package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductBo;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.dto.ProductDto;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.view.tm.ProductDetailTm;
import com.devstack.pos.view.tm.ProductTm;
import com.google.zxing.WriterException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ProductMainFormController {
    public AnchorPane context;
    public JFXTextField txtProductCode;
    public TextArea txtProductDescription;
    public JFXButton btnSaveUpdate;
    public TableView<ProductTm> tbl;
    public TableColumn colProductId;
    public TableColumn colProductDesc;
    public TableColumn colProductShowMore;
    public TableColumn colProductDelete;
    public TableView<ProductDetailTm> tblDetail;
    public TableColumn colPDId;
    public TableColumn colPDQty;
    public TableColumn colPDSellingPrice;
    public TableColumn colPDBuyingPrice;
    public TableColumn colPDDAvailability;
    public TableColumn colPDShowPrice;
    public TableColumn colPDDelete;
    public TextField txtSelectedProdId;
    public TextArea txtSelectedProdDescription;
    public JFXButton btnNewBatch;

    private String searchText = "";

    private ProductBo productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);
    private ProductDetailBo productDetailBo = BoFactory.getInstance().getBo(BoType.PRODUCT_DETAIL);

    public void initialize() throws SQLException, ClassNotFoundException {
        // load new product id
        loadProductId();
        //product table
        colProductId.setCellValueFactory(new PropertyValueFactory<>("code"));
        colProductDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colProductShowMore.setCellValueFactory(new PropertyValueFactory<>("showMore"));
        colProductDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));

        loadAllProducts(searchText);

        //Product detail table
        colPDId.setCellValueFactory(new PropertyValueFactory<>("code"));
        colPDQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPDBuyingPrice.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));
        colPDDAvailability.setCellValueFactory(new PropertyValueFactory<>("discountAvailability"));
        colPDShowPrice.setCellValueFactory(new PropertyValueFactory<>("showPrice"));
        colPDSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colPDDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));

        tbl.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (null != newValue) {
                        try {
                            setProductData(newValue);
                        } catch (SQLException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        tblDetail.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (null != newValue) {
                        try {
                            loadExternalUi(true,newValue);
                        } catch (IOException | WriterException | SQLException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
    }



    private void setProductData(ProductTm newValue) throws SQLException, ClassNotFoundException {
        txtProductCode.setEditable(false);
        btnSaveUpdate.setText("Update Product");
        txtProductCode.setText(String.valueOf(newValue.getCode()));
        txtProductDescription.setText(newValue.getDescription());

        txtSelectedProdId.setText(String.valueOf(newValue.getCode()));
        txtSelectedProdDescription.setText(newValue.getDescription());
        btnNewBatch.setDisable(false);

        loadAllBatches(newValue.getCode());
    }

    private void loadProductId() {
        try {
            txtProductCode.setText(String.valueOf(productBo.getProductId()));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm", "Dashboard");
    }

    public void btnAddNewOnAction(ActionEvent actionEvent) {
        btnSaveUpdate.setText("Save Product");
        txtProductCode.setEditable(true);
        clearFields();
    }

    public void btnNewProductOnAction(ActionEvent actionEvent) {
        try {
            if (btnSaveUpdate.getText().equals("Save Product")) {
                if (
                        productBo.saveProduct(
                                new ProductDto(Integer.parseInt(txtProductCode.getText()),
                                        txtProductDescription.getText()))
                ) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Product Saved!").show();
                    clearFields();
                    loadAllProducts(searchText);

                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            } else {
                if (productBo.updateProduct(
                        new ProductDto(Integer.parseInt(txtProductCode.getText()),
                                txtProductDescription.getText()))

                ) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Product Updated!").show();
                    clearFields();
                    txtProductCode.setEditable(true);
                    loadAllProducts(searchText);
                    btnSaveUpdate.setText("Save Product");
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    private void loadAllProducts(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<ProductTm> obList = FXCollections.observableArrayList();

        for (ProductDto productDto : productBo.findAllProducts()) {

            Button deleteButton = new Button("Delete");
            Button showMoreButton = new Button("Show More");
            ProductTm tm = new ProductTm(

                    productDto.getCode(),
                    productDto.getDescription(),
                    showMoreButton,
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
                        if (productBo.deleteProduct(productDto.getCode())) {
                            new Alert(Alert.AlertType.INFORMATION, "Product Deleted!").show();
                            loadAllProducts(searchText);
                            clearFields();
                            btnSaveUpdate.setText("Save Product");
                        } else {
                            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                        }

                    } catch (ClassNotFoundException | SQLException e1) {
                        new Alert(Alert.AlertType.WARNING, e1.toString()).show();
                    }


                }
            });
            obList.add(tm);
        }
        tbl.setItems(obList);
    }

    private void clearFields() {
        txtProductCode.clear();
        txtProductDescription.clear();
        txtSelectedProdId.clear();
        txtSelectedProdDescription.clear();
        loadProductId();
    }

    public void newBatchOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException, WriterException {
        loadExternalUi(false,null);
    }

    private void loadExternalUi(boolean state, ProductDetailTm tm)
            throws IOException, WriterException, SQLException, ClassNotFoundException {
        if (!txtSelectedProdId.getText().isEmpty()) {

            Stage stage = new Stage();
            FXMLLoader fxmlLoader =
                    new FXMLLoader(getClass().getResource("../view/NewBatchForm.fxml"));
            Parent parent = fxmlLoader.load();
            NewBatchFormController newBatchFormController = fxmlLoader.getController();
            newBatchFormController.setDetails(
                    Integer.parseInt(txtSelectedProdId.getText()),
                    txtSelectedProdDescription.getText(),
                    stage,
                    state,
                    tm
            );


            stage.setScene(new Scene(parent));
            stage.show();
            stage.centerOnScreen();
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a valid Product").show();
        }
    }

    private void loadAllBatches(int code) throws SQLException, ClassNotFoundException {
        ObservableList<ProductDetailTm> obList = FXCollections.observableArrayList();

        for (ProductDetailDto productDetailDto : productDetailBo.findAllProductDetails(Integer.parseInt(txtSelectedProdId.getText()))) {

            Button deleteButton = new Button("Delete");
            ProductDetailTm tm = new ProductDetailTm(
                    productDetailDto.getCode(),
                    productDetailDto.getQtyOnHand(),
                    productDetailDto.getSellingPrice(),
                    productDetailDto.getBuyingPrice(),
                    productDetailDto.isDiscountAvailability(),
                    productDetailDto.getShowPrice(),
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
                        if (productDetailBo.deleteProductDetail(productDetailDto.getCode())) {
                            new Alert(Alert.AlertType.INFORMATION, "Product Batch Deleted!").show();
                            loadAllBatches(Integer.parseInt(txtSelectedProdId.getText()));

                        } else {
                            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                        }

                    } catch (ClassNotFoundException | SQLException e1) {
                        new Alert(Alert.AlertType.WARNING, e1.toString()).show();
                    }


                }
            });
            obList.add(tm);
        }
        tblDetail.setItems(obList);
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
