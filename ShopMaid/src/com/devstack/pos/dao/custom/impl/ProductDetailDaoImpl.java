package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.ProductDetailDao;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.dto.ProductDetailJoinDto;
import com.devstack.pos.entity.Customer;
import com.devstack.pos.entity.ProductDetail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailDaoImpl implements ProductDetailDao {
    @Override
    public boolean save(ProductDetail productDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO product_detail VALUES (?,?,?,?,?,?,?,?)",
                productDetail.getCode(),
                productDetail.getBarcode(),
                productDetail.getQtyOnHand(),
                productDetail.getSellingPrice(),
                productDetail.isDiscountAvailability(),
                productDetail.getShowPrice(),
                productDetail.getBuyingPrice(),
                productDetail.getProductCode()
        );
    }

    @Override
    public boolean update(ProductDetail productDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE product_detail SET " +
                        "qty_on_hand=?, " +
                        "selling_price=?, " +
                        "discount_availability=?, " +
                        "show_price=?, " +
                        "buying_price=? WHERE code=?",

                productDetail.getQtyOnHand(),
                productDetail.getSellingPrice(),
                productDetail.isDiscountAvailability(),
                productDetail.getShowPrice(),
                productDetail.getBuyingPrice(),
                productDetail.getCode()
        );
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "DELETE FROM product_detail WHERE code=?",
                code
        );
    }

    @Override
    public ProductDetail find(String code) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM product_detail WHERE code=?",
                code
        );

        if (resultSet.next()) {
            return new ProductDetail(
                    resultSet.getString("code"),
                    resultSet.getString("barcode"),
                    resultSet.getInt("qty_on_hand"),
                    resultSet.getDouble("selling_price"),
                    resultSet.getDouble("show_price"),
                    resultSet.getDouble("buying_price"),
                    resultSet.getInt("product_code"),
                    resultSet.getBoolean("discount_availability")
            );
        }
        return null;
    }

    @Override
    public List<ProductDetail> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<ProductDetail> findAll(int productCode) throws SQLException, ClassNotFoundException {
        List<ProductDetail> productDetails = new ArrayList<>();

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM product_detail WHERE product_code=?",productCode);

        while (resultSet.next()) {
            productDetails.add(new ProductDetail(
                    resultSet.getString("code"),
                    resultSet.getString("barcode"),
                    resultSet.getInt("qty_on_hand"),
                    resultSet.getDouble("selling_price"),
                    resultSet.getDouble("show_price"),
                    resultSet.getDouble("buying_price"),
                    resultSet.getInt("product_code"),
                    resultSet.getBoolean("discount_availability")
            ));
        }
        return productDetails;

    }

    @Override
    public ProductDetailJoinDto findProductJoinDetail(String code) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM product_detail pd JOIN product p ON pd.code=? AND pd.product_code=p.code",
                code
        );

        if (resultSet.next()) {
            return new ProductDetailJoinDto(
                    resultSet.getInt(9),
                    resultSet.getString(10),
                    new ProductDetailDto(
                            resultSet.getString("code"),
                            resultSet.getString("barcode"),
                            resultSet.getInt("qty_on_hand"),
                            resultSet.getDouble("selling_price"),
                            resultSet.getDouble("show_price"),
                            resultSet.getDouble("buying_price"),
                            resultSet.getInt("product_code"),
                            resultSet.getBoolean("discount_availability")

                    )
            );
        }
        return null;
    }

    @Override
    public boolean manageQty(String barcode, int qty) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE product_detail SET qty_on_hand=(qty_on_hand-?) WHERE code=?",
                qty,
                barcode

        );
    }
}
