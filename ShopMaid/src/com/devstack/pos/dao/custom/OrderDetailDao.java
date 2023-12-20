package com.devstack.pos.dao.custom;

import com.devstack.pos.dao.CrudDao;
import com.devstack.pos.entity.ItemDetail;
import com.devstack.pos.entity.OrderDetail;

import java.sql.SQLException;

public interface OrderDetailDao extends CrudDao<OrderDetail, Integer > {

    public int getOrderCode() throws SQLException, ClassNotFoundException;
}
