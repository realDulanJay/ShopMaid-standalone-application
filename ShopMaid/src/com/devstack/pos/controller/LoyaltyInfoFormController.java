package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.LoyaltyCardBo;
import com.devstack.pos.entity.LoyaltyCard;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.view.tm.ProductDetailTm;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;

public class LoyaltyInfoFormController {
    public ImageView barcodeImage;
    public JFXTextField txtEmail;
    public JFXTextField txtCode;
    public Label lblLoyaltyType;

    private final LoyaltyCardBo loyaltyCardBo = BoFactory.getInstance().getBo(BoType.LOYALTY_CARD);

    public void setDetails(String email){
        try {
            LoyaltyCard loyaltyCard = loyaltyCardBo.findCard(email);

            txtCode.setText(String.valueOf(loyaltyCard.getCode()));
            txtEmail.setText(loyaltyCard.getEmail());
            lblLoyaltyType.setText(loyaltyCard.getCardType().name());

            byte[] data = Base64.decodeBase64(loyaltyCard.getBarcode());
            barcodeImage.setImage(
                    new Image(new ByteArrayInputStream(data))
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
