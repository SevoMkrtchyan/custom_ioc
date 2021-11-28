package com.example.engine;

import com.example.components.BeanFactory;
import com.example.components.Scanner;

import java.util.List;
import java.util.Map;

public class Application {

    public static ApplicationContext runWithXmlConfiguration(String packageToScan, String fileName) {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(fileName);
        Scanner scanner = new Scanner(packageToScan);
        BeanFactory beanFactory = new BeanFactory(scanner.getScanner());
        ApplicationContext context = new ApplicationContext();
        context.setFactory(beanFactory);
        Map<String, Map<String, List<String>>> beans = reader.getAttributes();
        beanFactory.setBeans(beans);
        beanFactory.createBeanFromXml();
        return context;
    }

}