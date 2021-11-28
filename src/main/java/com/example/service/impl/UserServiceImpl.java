package com.example.service.impl;

import com.example.model.Car;
import com.example.model.User;
import com.example.service.CarService;
import com.example.service.UserService;

public class UserServiceImpl implements UserService {

    private CarService carService;

    @Override
    public void printUser(User user) {
        System.out.println(user);
    }

    @Override
    public void addCarModel(Car car, User user) {
        user.setCarModel(carService.returnCarModel(car));
        System.out.println("from UserServiceImpl");
        System.out.println(user);
        System.out.println("--------------------------");
    }

}
