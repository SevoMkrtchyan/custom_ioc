package com.example.service;

import com.example.model.Car;
import com.example.model.User;

public interface UserService {

    void printUser(User user);

    void addCarModel(Car car, User user);

}
