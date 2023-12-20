package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.OrderDetailDao;
import com.devstack.pos.entity.OrderDetail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailDaoImpl implements OrderDetailDao {
    @Override
    public boolean save(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO order_detail VALUES (?,?,?,?,?,?)",
                orderDetail.getCode(),
                orderDetail.getIssuedDate(),
                orderDetail.getTotalCost(),
                orderDetail.getDiscount(),
                orderDetail.getCustomerEmail(),
                orderDetail.getOperatorEmail()
        );
    }

    @Override
    public boolean update(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(Integer code) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public OrderDetail find(Integer code) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<OrderDetail> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public int getOrderCode() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT code FROM order_detail ORDER BY  code DESC LIMIT 1"
        );

        if (resultSet.next()) {
            return resultSet.getInt("code")+1;
        }
        return 1;
    }
}
