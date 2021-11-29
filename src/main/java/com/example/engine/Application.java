package com.example.engine;


public class Application {

    public static ApplicationContext runWithXmlConfiguration(String packageToScan) {
        return new XmlApplicationContext(packageToScan);
    }

}