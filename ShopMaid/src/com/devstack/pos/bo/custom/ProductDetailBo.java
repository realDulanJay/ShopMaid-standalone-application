package com.devstack.pos.bo.custom;

import com.devstack.pos.bo.SuperBo;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.dto.ProductDetailJoinDto;

import java.sql.SQLException;
import java.util.List;

public interface ProductDetailBo extends SuperBo {
    public boolean saveProductDetail(ProductDetailDto productDetailDto) throws SQLException, ClassNotFoundException;
    public boolean updateProductDetail(ProductDetailDto productDetailDto) throws SQLException, ClassNotFoundException;
    public boolean deleteProductDetail(String code) throws SQLException, ClassNotFoundException;
    public ProductDetailDto findProductDetail(String code) throws SQLException, ClassNotFoundException;
    public List<ProductDetailDto> findAllProductDetails() throws SQLException, ClassNotFoundException;
    public List<ProductDetailDto> findAllProductDetails(int productCode) throws SQLException, ClassNotFoundException;

    public ProductDetailJoinDto findProductJoinDetail(String code) throws SQLException, ClassNotFoundException;
    public  boolean manageQty(String barcode, int qty) throws SQLException, ClassNotFoundException;
}
