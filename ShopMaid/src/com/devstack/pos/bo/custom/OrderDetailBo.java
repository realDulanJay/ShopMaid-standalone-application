package com.devstack.pos.bo.custom;

import com.devstack.pos.bo.SuperBo;
import com.devstack.pos.dto.ItemDetailDto;
import com.devstack.pos.dto.OrderDetailDto;

import java.sql.SQLException;

public interface OrderDetailBo extends SuperBo {
    public boolean makeOrder(OrderDetailDto orderDetailDto) throws SQLException, ClassNotFoundException;

    public int getOrderCode() throws SQLException, ClassNotFoundException;
}
