package com.intern.ChatApp.service.impl;

import com.intern.ChatApp.entity.User;
import com.intern.ChatApp.service.UserService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserServiceImpl implements UserService {
    @Override
    public void addUser(User user) {

    }

    @Override
    public List<User> getUser() {
        return null;
    }

    @Override
    public User getUser(int id) {
        return null;
    }

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public void updateUser(int id, User user) {

    }
}
