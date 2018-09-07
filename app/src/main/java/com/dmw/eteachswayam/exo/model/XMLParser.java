package com.dmw.eteachswayam.exo.model;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLParser {

    public static String child1Value = "";
    public static String child2Value = "";
    public static NodeList nodeList;
    public static String[] pathArr;
    public static String[] verArr;
    public static String[] SizeArr;
    public static boolean counterComplete = false;


    public XMLParser( String response, String node, String child1, String child2)
    {
        try {
            parseByDOM(response,node,child1,child2);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void parseByDOM( String filePath, String node2, String child1, String child2) throws ParserConfigurationException,
            SAXException, IOException {
        child1Value = "";
        child2Value = "";

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder        db  = dbf.newDocumentBuilder();
        Document               doc = db.parse( new File ( filePath));

        doc.getDocumentElement().normalize();
        nodeList = doc.getElementsByTagName(node2);
        Node node = nodeList.item( 0);

        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node temp = node.getChildNodes().item( i);
            if (temp.getNodeName().equalsIgnoreCase(child1)) {
                child1Value = temp.getTextContent();
            } else if (temp.getNodeName().equalsIgnoreCase(child2)) {
                child2Value = temp.getTextContent();
            }
        }
    }


}

