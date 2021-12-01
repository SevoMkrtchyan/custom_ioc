package com.example.service.impl;

import com.example.model.User;
import com.example.service.UserService;

public class BikeService {

    private UserService userService;

    public void printBikes() {
        System.out.println("Yamaha : Aston Martin : Bugatti : Rolls Royce");
        userService.printUser(new User("petros","petrosyan","petros@mail.ru","petros"));
    }

}