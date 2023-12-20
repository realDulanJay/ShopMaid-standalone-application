package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.ProductDao;
import com.devstack.pos.db.DbConnection;
import com.devstack.pos.entity.Product;
import com.devstack.pos.entity.User;
import com.devstack.pos.util.PasswordManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {

    @Override
    public int getProductId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT code FROM product ORDER BY  code DESC LIMIT 1"
        );

        if (resultSet.next()) {
            return resultSet.getInt("code")+1;
        }
        return 1;
    }

    @Override
    public boolean save(Product product) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO product VALUES (?,?)",
                product.getCode(),
                product.getDescription()
        );
    }

    @Override
    public boolean update(Product product) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE product SET description=? WHERE code=?",
                product.getDescription(),
                product.getCode()
        );
    }

    @Override
    public boolean delete(Integer integer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "DELETE FROM product WHERE code=?",
                integer
        );
    }

    @Override
    public Product find(Integer integer) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM product WHERE code=?",
                integer
        );

        if (resultSet.next()) {
            return new Product(resultSet.getInt("code"), resultSet.getString("description"));
        }
        return null;
    }

    @Override
    public List<Product> findAll() throws SQLException, ClassNotFoundException {
        List<Product> products = new ArrayList<>();

        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM product"
        );

        while (resultSet.next()) {
            products.add(new Product(
                    resultSet.getInt("code"),
                    resultSet.getString("description")

            ));
        }
        return products;
    }
}
