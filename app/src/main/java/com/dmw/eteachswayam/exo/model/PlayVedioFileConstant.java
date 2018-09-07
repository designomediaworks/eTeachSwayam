package com.dmw.eteachswayam.exo.model;


import com.dmw.eteachswayam.exo.activity.Topic;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class PlayVedioFileConstant {

	private static Topic            topic;
	static         ArrayList<Topic> arrayList;
	public static
    ArrayList <Topic> parseTopicXml( String filePath)
	{

		arrayList=new ArrayList<Topic> ();
		try{
			File                   fXmlFile  = new File ( filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        dBuilder  = dbFactory.newDocumentBuilder();
			Document               doc       = dBuilder.parse( fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName( "topic");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item( temp);
				if ( nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element ) nNode;

					String name =eElement.getAttribute( "name");
					String link =eElement.getAttribute( "link");
					topic=new Topic(name, link);
					arrayList.add(topic);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}
	public static
    String generatefoldername( Random random)
	{
		
		int          length       = 10 + ( Math.abs( random.nextInt()) % 3);
		StringBuffer stringBuffer = new StringBuffer ();
		for (int i = 0; i < length; i++) {
			int baseCharNumber = Math.abs( random.nextInt()) % 62;
			int charNumber = 0;
			if (baseCharNumber < 26) {
				charNumber = 65 + baseCharNumber;
			} else if (baseCharNumber < 52) {
				charNumber = 97 + (baseCharNumber - 26);
			} else {
				charNumber = 48 + (baseCharNumber - 52);
			}
			stringBuffer.append((char) charNumber);
		}
		return stringBuffer.toString();
	}
}
