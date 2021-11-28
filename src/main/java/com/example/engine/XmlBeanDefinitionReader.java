package com.example.engine;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class XmlBeanDefinitionReader {

    private final String FILEPATH;
    private final Map<String, Map<String, List<String>>> attributes;

    public XmlBeanDefinitionReader(String xmlFile) {
        this.FILEPATH = xmlFile;
        this.attributes = new HashMap<>();
    }

    private void scanXmlApplicationContext() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(FILEPATH));
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("bean");
            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String injectedSource = element.getAttribute("class").replaceAll("\\s+", "");
                    attributes.put(injectedSource, new HashMap<>());
                    Node constructor = ((Element) node).getElementsByTagName("constructor-args").item(0);
                    if (constructor != null) {
                        findConstructorArgs(constructor, injectedSource);
                    }
                    Node fields = ((Element) node).getElementsByTagName("fields").item(0);
                    if (fields != null) {
                        findFieldArgs(fields, injectedSource);
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void findConstructorArgs(Node node, String bean) {
        Element arg = (Element) node;
        String argument = arg.getTextContent().replaceAll("\\s+", "");
        if (attributes.containsKey(bean)) {
            Map<String, List<String>> constructorArguments = attributes.get(bean);
            if (constructorArguments.containsKey("constructor-args")) {
                List<String> arguments = constructorArguments.get("constructor-args");
                arguments.add(argument);
            } else {
                List<String> arguments = new LinkedList<>();
                arguments.add(argument);
                constructorArguments.put("constructor-args", arguments);
                attributes.put(bean, constructorArguments);
            }
        } else {
            Map<String, List<String>> constructorArguments = new HashMap<>();
            List<String> arguments = new LinkedList<>();
            arguments.add(argument);
            constructorArguments.put("constructor-args", arguments);
            attributes.put(bean, constructorArguments);
            System.out.println(attributes);
        }
    }

    private void findFieldArgs(Node node, String bean) {
        Element arg = (Element) node;
        String argument = arg.getTextContent().replaceAll("\\s+", "");
        if (attributes.containsKey(bean)) {
            Map<String, List<String>> fieldArgs = attributes.get(bean);
            if (fieldArgs.containsKey("fields")) {
                List<String> arguments = fieldArgs.get("fields");
                arguments.add(argument);
            } else {
                List<String> arguments = new LinkedList<>();
                arguments.add(argument);
                fieldArgs.put("fields", arguments);
                attributes.put(bean, fieldArgs);
            }
        } else {
            Map<String, List<String>> fieldArgs = new HashMap<>();
            List<String> arguments = new LinkedList<>();
            arguments.add(argument);
            fieldArgs.put("fields", arguments);
            attributes.put(bean, fieldArgs);
            System.out.println(attributes);
        }
    }

    public Map<String, Map<String, List<String>>> getAttributes() {
        scanXmlApplicationContext();
        return attributes;
    }

}