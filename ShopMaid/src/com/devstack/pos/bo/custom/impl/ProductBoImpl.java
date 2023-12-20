package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.ProductBo;
import com.devstack.pos.dao.DaoFactory;
import com.devstack.pos.dao.custom.ProductDao;
import com.devstack.pos.dto.ProductDto;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.entity.Product;
import com.devstack.pos.entity.Product;
import com.devstack.pos.entity.Product;
import com.devstack.pos.enums.DaoType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductBoImpl implements ProductBo {

    ProductDao productDao = DaoFactory.getInstance().getDao(DaoType.PRODUCT);
    @Override
    public boolean saveProduct(ProductDto productDto) throws SQLException, ClassNotFoundException {
        return productDao.save(
                new Product(productDto.getCode(), productDto.getDescription())
        );
    }

    @Override
    public boolean updateProduct(ProductDto productDto) throws SQLException, ClassNotFoundException {
        return productDao.update(
                new Product(
                        productDto.getCode(),
                        productDto.getDescription()
                )
        );
    }

    @Override
    public boolean deleteProduct(int code) throws SQLException, ClassNotFoundException {
        return productDao.delete(code);
    }

    @Override
    public ProductDto findProduct(int code) throws SQLException, ClassNotFoundException {
        Product product = productDao.find(code);

        if (product != null) {
            return new ProductDto(
                    product.getCode(), product.getDescription()
            );
        }
        return null;
    }

    @Override
    public List<ProductDto> findAllProducts() throws SQLException, ClassNotFoundException {
        List<ProductDto> products = new ArrayList<>();

        for(Product product : productDao.findAll()){
            products.add(new ProductDto(
                            product.getCode(),
                            product.getDescription()
                    )
            );
        }
        return products;
    }

    @Override
    public int getProductId() throws ClassNotFoundException, SQLException {
        return productDao.getProductId();
    }
}
