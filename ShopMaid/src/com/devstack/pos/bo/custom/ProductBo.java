package com.devstack.pos.bo.custom;

import com.devstack.pos.bo.SuperBo;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.ProductDto;

import java.sql.SQLException;
import java.util.List;

public interface ProductBo extends SuperBo {
    public boolean saveProduct(ProductDto productDto) throws SQLException, ClassNotFoundException;
    public boolean updateProduct(ProductDto productDto) throws SQLException, ClassNotFoundException;
    public boolean deleteProduct(int code) throws SQLException, ClassNotFoundException;
    public ProductDto findProduct(int code) throws SQLException, ClassNotFoundException;
    public List<ProductDto> findAllProducts() throws SQLException, ClassNotFoundException;
    public int getProductId() throws ClassNotFoundException, SQLException;
}



