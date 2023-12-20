package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.util.QRDataGenerator;
import com.devstack.pos.view.tm.ProductDetailTm;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jfoenix.controls.JFXButton;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class NewBatchFormController {
    public ImageView barcodeImage;
    public TextField txtQty;
    public TextField txtSellingPrice;
    public TextField txtShowPrice;
    public TextField txtBuyingPrice;
    public TextField txtProductCode;
    public TextArea txtSelectedProdDescription;
    public RadioButton rBtnYes;
    public ToggleGroup discount;
    public JFXButton btnSaveUpdate;

    private String uniqueData = null;
    private BufferedImage bufferedImage = null;

    private Stage stage = null;

    private String batchCode = null;

    private final ProductDetailBo productDetailBo = BoFactory.getInstance().getBo(BoType.PRODUCT_DETAIL);

    private void setQRCode() throws WriterException {

        uniqueData = QRDataGenerator.generate(25);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        bufferedImage =
                MatrixToImageWriter.toBufferedImage(
                        qrCodeWriter.encode(
                                uniqueData,
                                BarcodeFormat.QR_CODE,
                                160, 160
                        )
                );

        Image image = SwingFXUtils.toFXImage(bufferedImage, null);

        barcodeImage.setImage(image);
    }

    public void setDetails(int productCode, String description, Stage stage, boolean state, ProductDetailTm tm)
            throws WriterException, SQLException, ClassNotFoundException {
        this.stage = stage;

        if (state) {
            ProductDetailDto productDetailDto = productDetailBo.findProductDetail(tm.getCode());

            if (productDetailDto != null) {
                btnSaveUpdate.setText("Update Batch");

                byte[] data = Base64.decodeBase64(productDetailDto.getBarcode());
                barcodeImage.setImage(
                        new Image(new ByteArrayInputStream(data))
                );

                txtQty.setText(String.valueOf(productDetailDto.getQtyOnHand()));
                txtBuyingPrice.setText(String.valueOf(productDetailDto.getBuyingPrice()));
                txtSellingPrice.setText(String.valueOf(productDetailDto.getSellingPrice()));
                txtShowPrice.setText(String.valueOf(productDetailDto.getShowPrice()));
                rBtnYes.setSelected(productDetailDto.isDiscountAvailability());

                batchCode = tm.getCode();


            } else {
                stage.close();
            }

        } else {
            setQRCode();
        }

        txtProductCode.setText(String.valueOf(productCode));
        txtSelectedProdDescription.setText(description);


    }

    public void saveBatch(ActionEvent actionEvent)
            throws SQLException, ClassNotFoundException, IOException, InterruptedException {

        if (btnSaveUpdate.getText().equals("Save Batch")) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(bufferedImage, "png", baos);
            byte[] arr = baos.toByteArray();

            if (productDetailBo.saveProductDetail(
                    new ProductDetailDto(
                            uniqueData,
                            Base64.encodeBase64String(arr),
                            Integer.parseInt(txtQty.getText()),
                            Double.parseDouble(txtSellingPrice.getText()),
                            Double.parseDouble(txtShowPrice.getText()),
                            Double.parseDouble(txtBuyingPrice.getText()),
                            Integer.parseInt(txtProductCode.getText()),
                            rBtnYes.isSelected()
                    )
            )) {
                new Alert(Alert.AlertType.CONFIRMATION, "New Batch Saved!").show();
                Thread.sleep(3000);
                stage.close();

            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }
        } else {
            if (productDetailBo.updateProductDetail(
                    new ProductDetailDto(
                            batchCode,
                            null,
                            Integer.parseInt(txtQty.getText()),
                            Double.parseDouble(txtSellingPrice.getText()),
                            Double.parseDouble(txtShowPrice.getText()),
                            Double.parseDouble(txtBuyingPrice.getText()),
                            Integer.parseInt(txtProductCode.getText()),
                            rBtnYes.isSelected()
                    )
            )) {
                new Alert(Alert.AlertType.CONFIRMATION, "Batch Updated!").show();
                Thread.sleep(3000);
                stage.close();

            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }
        }


    }
}
