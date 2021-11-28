package com.example.engine;

import com.example.components.BeanConfigurator;
import com.example.components.BeanFactory;
import com.example.components.FieldInjectWithAnnotationConfig;

public class Application {

    public static ApplicationContext runWithXmlConfiguration(String packageToScan, String fileName) {
        XmlConfigurationBeanScanner reader = new XmlConfigurationBeanScanner(fileName);

        BeanConfigurator fieldInjectConfig = new FieldInjectWithAnnotationConfig();

        BeanConfiguratorProcessor configuratorProcessor = new BeanConfiguratorProcessor();
        configuratorProcessor.setConfigure(fieldInjectConfig);

        ApplicationContext context = new ApplicationContext(packageToScan);

        BeanFactory objectFactory = new BeanFactory(context);

        context.setFactory(objectFactory);

        configuratorProcessor.startupBeanConfigurator(reader.scanXmlApplicationContext(), context);

        return context;
    }

}