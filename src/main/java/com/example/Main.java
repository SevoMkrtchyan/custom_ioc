package com.example;

import com.example.engine.Application;
import com.example.engine.ApplicationContext;
import com.example.model.Car;
import com.example.model.User;
import com.example.service.CarService;
import com.example.service.UserService;

public class Main {

    public static void main(String[] args) {
        User user = new User("Poxos", "Poxosyan", "poxos@mail.ru", "poxos");
        Car car = new Car(5000, "Mercedes", "w140");
        ApplicationContext context =
                Application.runWithXmlConfiguration("com.example");
        UserService userService = context.getBean(UserService.class);
        userService.printUser(user);
        CarService carService = context.getBean(CarService.class);
        carService.printCar(car);
        userService.addCarModel(car, user);

    }

}
