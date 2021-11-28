package com.example.engine;

import com.example.components.BeansScanner;
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
import java.util.LinkedList;
import java.util.List;

public class XmlConfigurationBeanScanner implements BeansScanner {

    private String FILEPATH;
    private List<String> beans;

    public XmlConfigurationBeanScanner(String xmlFile) {
        this.FILEPATH = xmlFile;
        this.beans = new LinkedList<>();
    }

    @Override
    public List<String> scanXmlApplicationContext() {
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
                    String injectedSource = element.getAttribute("class");
                    beans.add(injectedSource);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return beans;
    }
}