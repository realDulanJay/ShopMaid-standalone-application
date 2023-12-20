package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.bo.custom.LoyaltyCardBo;
import com.devstack.pos.bo.custom.OrderDetailBo;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.*;
import com.devstack.pos.entity.LoyaltyCard;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.enums.CardType;
import com.devstack.pos.util.QRDataGenerator;
import com.devstack.pos.util.UserSessionData;
import com.devstack.pos.view.tm.CartTm;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
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
import org.apache.commons.codec.binary.Base64;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class PlaceOrderFormController {
    public AnchorPane context;
    public ToggleGroup model;
    public TextField txtEmail;
    public TextField txtName;
    public TextField txtContact;
    public TextField txtSalary;
    public Hyperlink urlNewLoyalty;
    public Label lblLoyaltyType;
    public TextField txtBarcode;
    public TextField txtDescription;
    public TextField txtSellingPrice;
    public TextField txtDiscount;
    public TextField txtShowPrice;
    public TextField txtQtyOnHand;
    public Label lblDiscountAvl;
    public TextField txtBuyingPrice;
    public TextField txtQty;
    public TableView tblCart;
    public TableColumn colCode;
    public TableColumn colDesc;
    public TableColumn colSelPrice;
    public TableColumn colSelDisc;
    public TableColumn colSelShPrice;
    public TableColumn colSelQty;
    public TableColumn colSelTotal;
    public TableColumn colSelOperation;
    public Label lblTotal;

    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private final ProductDetailBo productDetailBo = BoFactory.getInstance().getBo(BoType.PRODUCT_DETAIL);
    private final OrderDetailBo orderDetailBo = BoFactory.getInstance().getBo(BoType.ORDER_DETAIL);
    private final LoyaltyCardBo loyaltyCardBo = BoFactory.getInstance().getBo(BoType.LOYALTY_CARD);


    ObservableList<CartTm> oblist = FXCollections.observableArrayList();

    public void initialize() {

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSelPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colSelDisc.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colSelShPrice.setCellValueFactory(new PropertyValueFactory<>("showPrice"));
        colSelQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colSelTotal.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colSelOperation.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm", "Place Order", false);
    }

    public void searchCustomer(ActionEvent actionEvent) {

        String email = txtEmail.getText();

        try {
            CustomerDto customerDto = customerBo.findCustomer(email);
            if (customerDto != null) {
                txtName.setText(customerDto.getName());
                txtContact.setText(customerDto.getContact());
                txtSalary.setText(String.valueOf(customerDto.getSalary()));

                fetchLoyaltyCardData(email);
            } else {
                new Alert(Alert.AlertType.WARNING, "Invalid Email!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    private void fetchLoyaltyCardData(String email) {
        try {
            LoyaltyCard loyaltyCard = loyaltyCardBo.findCard(txtEmail.getText());

            if (loyaltyCard != null) {
                urlNewLoyalty.setText("Loyalty Card Info");
                urlNewLoyalty.setVisible(true);
                lblLoyaltyType.setVisible(true);


                CardType cardType = loyaltyCard.getCardType();

                if (cardType.equals(CardType.PLATINUM)) lblLoyaltyType.setText("(PLATINUM member)");
                else if (cardType.equals(CardType.GOLD)) lblLoyaltyType.setText("(GOLD member)");
                else lblLoyaltyType.setText("(SILVER member)");
            } else {
                urlNewLoyalty.setText("+ New Loyalty");
                urlNewLoyalty.setVisible(true);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnAddNewCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CustomerForm", "Customer Management", true);
    }

    public void newLoyaltyOnAction(ActionEvent actionEvent) {
        double salary = Double.parseDouble(txtSalary.getText());
        CardType cardType = null;
        if (salary >= 100000) {
            cardType = CardType.PLATINUM;
        } else if (salary >= 50000) {
            cardType = CardType.GOLD;
        } else {
            cardType = CardType.SILVER;
        }

        try {
            int cardCode = new Random().nextInt(100001);

            if (urlNewLoyalty.getText().equals("+ New Loyalty")) {

                String uniqueData = cardCode + "@loyalty@" + QRDataGenerator.generate(6);
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BufferedImage bufferedImage = null;

                bufferedImage = MatrixToImageWriter.toBufferedImage(
                        qrCodeWriter.encode(
                                uniqueData,
                                BarcodeFormat.QR_CODE,
                                160, 160
                        )
                );
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                javax.imageio.ImageIO.write(bufferedImage, "png", baos);
                byte[] arr = baos.toByteArray();

                if (loyaltyCardBo.saveLoyaltyData(
                        new LoyaltyCardDto(
                                cardCode,
                                cardType,
                                Base64.encodeBase64String(arr),
                                txtEmail.getText()
                        )
                )) {
                    urlNewLoyalty.setText("Loyalty Card Info");
                    new Alert(Alert.AlertType.CONFIRMATION, "Loyalty Card Created Successfully!").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again!").show();
                }

            }else{
                loadExternalUi();
            }
        }catch (SQLException | ClassNotFoundException | WriterException | IOException e) {
            new Alert(Alert.AlertType.WARNING, "Try again!").show();
            throw new RuntimeException(e);
        }

    }

    private void loadExternalUi()
            throws IOException, WriterException, SQLException, ClassNotFoundException {


        Stage stage = new Stage();
        FXMLLoader fxmlLoader =
                new FXMLLoader(getClass().getResource("../view/LoyaltyInfoForm.fxml"));
        Parent parent = fxmlLoader.load();
        LoyaltyInfoFormController loyaltyInfoFormController = fxmlLoader.getController();
        loyaltyInfoFormController.setDetails(txtEmail.getText());

        stage.setScene(new Scene(parent));
        stage.show();
        stage.centerOnScreen();

    }

    public void loadProduct(ActionEvent actionEvent) {
        ProductDetailJoinDto productJoinDetail = null;
        try {
            productJoinDetail = productDetailBo.findProductJoinDetail(txtBarcode.getText());

            if (productJoinDetail != null) {
                txtDescription.setText(productJoinDetail.getDescription());
                txtSellingPrice.setText(String.valueOf(productJoinDetail.getProductDetailDto().getSellingPrice()));
                txtBuyingPrice.setText(String.valueOf(productJoinDetail.getProductDetailDto().getBuyingPrice()));
                txtShowPrice.setText(String.valueOf(productJoinDetail.getProductDetailDto().getShowPrice()));
                txtDiscount.setText(String.valueOf(
                        setDiscount(
                                productJoinDetail.getProductDetailDto().isDiscountAvailability(),
                                productJoinDetail.getProductDetailDto().getSellingPrice()
                        )
                ));
                txtQtyOnHand.setText(String.valueOf(productJoinDetail.getProductDetailDto().getQtyOnHand()));

                txtQty.requestFocus();
            } else {
                new Alert(Alert.AlertType.WARNING, "Unable to find the product!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    private double setDiscount(boolean isDiscountAvailable, double sellingPrice) {
        if (isDiscountAvailable) {
            CardType cardType = getCardType(txtEmail.getText());

            if (cardType != null) {
                switch (cardType) {
                    case PLATINUM:
                        return sellingPrice *= 15.0 / 100;
                    case GOLD:
                        return sellingPrice *= 10.0 / 100;
                    case SILVER:
                        return sellingPrice *= 5.0 / 100;
                    default:
                        return 0;
                }
            } else {
                return 0;
            }

        }
        return 0;
    }

    private CardType getCardType(String email) {

        try {
            LoyaltyCard loyaltyCard = loyaltyCardBo.findCard(email);

            if (loyaltyCard != null) {
                return loyaltyCard.getCardType();
            }
            return null;


        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnAddNewProductOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ProductMainForm", "Product Management", true);
    }

    public void addToCart(ActionEvent actionEvent) throws NumberFormatException {
        int qty = Integer.parseInt(txtQty.getText());


        double sellingPrice = (Double.parseDouble(txtSellingPrice.getText()) - Double.parseDouble(txtDiscount.getText()));
        double totalCost = qty * sellingPrice;

        CartTm selectedCartTm = isExists(txtBarcode.getText());

        if (selectedCartTm != null) {
            selectedCartTm.setQty(qty + selectedCartTm.getQty());
            selectedCartTm.setTotalCost(totalCost + selectedCartTm.getTotalCost());

            tblCart.refresh();
            setTotal();
        } else {
            Button removeButton = new Button("Remove");

            CartTm cartTm = new CartTm(
                    txtBarcode.getText(),
                    txtDescription.getText(),
                    Double.parseDouble(txtDiscount.getText()),
                    sellingPrice,
                    Double.parseDouble(txtShowPrice.getText()),
                    qty,
                    totalCost,
                    removeButton);

            removeButton.setOnAction(e -> {
                Alert alert = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "Are you sure?",
                        ButtonType.YES, ButtonType.NO
                );
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get().equals(ButtonType.YES)) {

                    oblist.remove(cartTm);
                    tblCart.refresh();
                    setTotal();

                }
            });

            oblist.add(cartTm);
            tblCart.setItems(oblist);
            setTotal();
        }
        clearFields();
        txtBarcode.requestFocus();


    }

    private CartTm isExists(String code) {
        for (CartTm cartTm : oblist) {
            if (cartTm.getCode().equals(code)) {
                return cartTm;
            }
        }
        return null;
    }

    private void clearFields() {
        txtBarcode.clear();
        txtDescription.clear();
        txtSellingPrice.clear();
        txtDiscount.clear();
        txtShowPrice.clear();
        txtQtyOnHand.clear();
        txtBuyingPrice.clear();
        txtQty.clear();
    }

    public void btnCompleteOrder(ActionEvent actionEvent) {
        List<ItemDetailDto> list = new ArrayList<>();
        double discount = 0;

        for (CartTm cartTm : oblist) {
            list.add(
                    new ItemDetailDto(
                            cartTm.getCode(),
                            cartTm.getQty(),
                            cartTm.getDiscount(),
                            cartTm.getTotalCost()
                    )
            );
            discount += cartTm.getDiscount();

        }

        OrderDetailDto orderDetailDto = new OrderDetailDto(
                loadOrderCode(),
                new Date(),
                Double.parseDouble(lblTotal.getText().split("/=")[0]),
                txtEmail.getText(),
                discount,
                UserSessionData.email,
                list
        );

        try {
            if (orderDetailBo.makeOrder(orderDetailDto)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Order Completed!").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setTotal() {
        double total = 0;

        for (CartTm cartTm : oblist) {
            total += cartTm.getTotalCost();
        }

        lblTotal.setText(String.valueOf(total) + "/=");
    }

    private int loadOrderCode() {
        try {
            return orderDetailBo.getOrderCode();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        return -1;
    }

    private void setUi(String url, String title, boolean state) throws IOException {
        Stage stage = null;
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/" + url + ".fxml")));

        if (state) {
            stage = new Stage();
            stage.setScene(scene);
            stage.show();

        } else {
            stage = (Stage) context.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.centerOnScreen();
        }


    }
}
