package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.UserBo;
import com.devstack.pos.dao.DaoFactory;
import com.devstack.pos.dao.custom.UserDao;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.entity.Customer;
import com.devstack.pos.entity.User;
import com.devstack.pos.enums.DaoType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserBoImpl implements UserBo {

    UserDao userDao = DaoFactory.getInstance().getDao(DaoType.USER);
    @Override
    public boolean saveUser(UserDto userDto) throws SQLException, ClassNotFoundException {
        return userDao.save(
                new User(userDto.getEmail(), userDto.getPassword())
        );
    }

    @Override
    public boolean updateUser(UserDto userDto) throws SQLException, ClassNotFoundException {
        return userDao.update(
                new User(
                        userDto.getEmail(),
                        userDto.getPassword()
                )
        );
    }

    @Override
    public boolean deleteUser(String email) throws SQLException, ClassNotFoundException {
        return userDao.delete(email);
    }

    @Override
    public UserDto findUser(String email) throws SQLException, ClassNotFoundException {
        User user = userDao.find(email);

        if (user != null) {
            return new UserDto(
                    user.getEmail(), user.getPassword()
            );
        }
        return null;
    }

    @Override
    public List<UserDto> findAllUsers() throws SQLException, ClassNotFoundException {
        List<UserDto> users = new ArrayList<>();

        for(User user : userDao.findAll()){
            users.add(new UserDto(
                    user.getEmail(),
                    user.getPassword()
                    )
            );
        }
        return users;
    }
}
