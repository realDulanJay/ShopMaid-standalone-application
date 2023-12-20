package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.CustomerDao;
import com.devstack.pos.db.DbConnection;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.entity.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {


    @Override
    public List<Customer> searchCustomers(String searchText) throws SQLException, ClassNotFoundException {
        List<Customer> customers = new ArrayList<>();

        searchText = "%" + searchText + "%";

        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM customer WHERE email LIKE ? || name LIKE ?",
                searchText,
                searchText
        );

        while (resultSet.next()) {
            customers.add(new Customer(
                    resultSet.getString("email"),
                    resultSet.getString("name"),
                    resultSet.getString("contact"),
                    resultSet.getDouble("salary")
            ));
        }
        return customers;
    }

    @Override
    public boolean save(Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO customer VALUES (?,?,?,?)",
                customer.getEmail(),
                customer.getName(),
                customer.getContact(),
                customer.getSalary()
                );
    }

    @Override
    public boolean update(Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE customer SET name=?, contact=?, salary=? WHERE email=?",
                customer.getName(),
                customer.getContact(),
                customer.getSalary(),
                customer.getEmail()
        );
    }

    @Override
    public boolean delete(String email) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "DELETE FROM customer WHERE email=?",
                email
        );
    }

    @Override
    public Customer find(String email) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM customer WHERE email=?",
                email
        );

        if (resultSet.next()) {
            return new Customer(
                    resultSet.getString("email"),
                    resultSet.getString("name"),
                    resultSet.getString("contact"),
                    resultSet.getDouble("salary")
            );
        }
        return null;
    }

    @Override
    public List<Customer> findAll() throws SQLException, ClassNotFoundException {
        List<Customer> customers = new ArrayList<>();

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer");

        while (resultSet.next()) {
            customers.add(new Customer(
                    resultSet.getString("email"),
                    resultSet.getString("name"),
                    resultSet.getString("contact"),
                    resultSet.getDouble("salary")
            ));
        }
        return customers;
    }
}
