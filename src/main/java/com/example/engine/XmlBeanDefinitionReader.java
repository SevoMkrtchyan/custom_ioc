package com.example.engine;

import com.example.attribute.AttributeType;
import com.example.attribute.BeanAttribute;
import com.example.attribute.BeanDefinition;
import com.example.components.Scanner;
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class XmlBeanDefinitionReader {

    private final List<BeanDefinition> beanDefinitions;
    private String FILEPATH;
    private final Scanner scanner = new Scanner();

    public XmlBeanDefinitionReader() {
        scanner.getResources().forEach(s -> {
            if (s.endsWith("applicationContext.xml")) {
                FILEPATH = s;
            }
        });
        this.beanDefinitions = new LinkedList<>();
    }

    private void scanXmlApplicationContext() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        BeanDefinition beanDefinition = null;
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
                    Node fields = ((Element) node).getElementsByTagName("fields").item(0);
                    if (fields != null) {
                        beanDefinition = findFieldArgs(fields, injectedSource);
                        beanDefinition.setId(injectedSource);
                    } else {
                        beanDefinition = new BeanDefinition();
                        beanDefinition.setId(injectedSource);
                    }
                    registerBeanParameters(beanDefinition, node);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private BeanDefinition findFieldArgs(Node node, String bean) {
        BeanDefinition beanDefinition = new BeanDefinition();
        Element arg = (Element) node;
        String[] argument = arg.getTextContent().split("\\s+");
        BeanAttribute beanAttribute = new BeanAttribute();
        List<String> args = Arrays.asList(argument);
        List<String> beanAttributes = new LinkedList<>();
        args.forEach(e -> {
            if (!e.isEmpty() && !e.isBlank()) {
                beanAttributes.add(e);
            }
        });
        beanAttribute.setAttributes(beanAttributes);
        beanAttribute.setType(AttributeType.FIELD);
        beanDefinition.setBeanAttribute(beanAttribute);
        return beanDefinition;
    }

    public List<BeanDefinition> getBeanDefinitions() {
        scanXmlApplicationContext();
        return beanDefinitions;
    }

    private void registerBeanParameters(BeanDefinition beanDefinition, Node node) {
        Node init = ((Element) node).getElementsByTagName("init").item(0);
        Node destroy = ((Element) node).getElementsByTagName("destroy").item(0);
        Node scope = ((Element) node).getElementsByTagName("scope").item(0);
        if (init != null) {
            beanDefinition.setInitMethodName(init.getTextContent());
        }
        if (destroy != null) {
            beanDefinition.setDestroyMethodName(destroy.getTextContent());
        }
        if (scope != null) {
            beanDefinition.setScope(scope.getTextContent());
        }
        beanDefinitions.add(beanDefinition);
    }

}