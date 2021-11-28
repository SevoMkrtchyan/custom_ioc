package com.example.service.impl;

import com.example.model.Car;
import com.example.service.CarService;

public class CarServiceImpl implements CarService {

    private BikeService bikeService;

    @Override
    public void printCar(Car car) {
        System.out.println(car);
    }

    @Override
    public String returnCarModel(Car car) {
        System.out.println("from CarServiceImpl");
        bikeService.printBikes();
        System.out.println("--------------------------");
        return car.getModel();
    }


}
