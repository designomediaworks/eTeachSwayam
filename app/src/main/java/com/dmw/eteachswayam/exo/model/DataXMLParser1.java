package com.dmw.eteachswayam.exo.model;

import android.content.Context;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class DataXMLParser1 {

	public String fileName;
	Context           context;
	SAXParser         saxParser;
	ArrayList<Items1> Serverfile;
	ArrayList<Items1> clientfile;
	ArrayList<Items1> s;
	public DataXMLParser1(String fileName)
	{
		this.fileName=fileName;
	}
	public DataXMLParser1() {
		super();
		this.fileName="";
	}
	public
    ArrayList<Items1> parser()
	{
		try
		{
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			saxParser=saxParserFactory.newSAXParser();
			saxParser.parse( new File ( fileName), new CompareXMLHandler());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return CompareXMLHandler.arrayList;
	}
	public
    ArrayList<Items1> getDiffArrayList( String serverPath, String clientPath)
	{if(new File ( serverPath).exists()){
		if(!new File ( clientPath).exists()){

			try {
				SAXParserFactory parserFactory = SAXParserFactory.newInstance();
				parserFactory.setNamespaceAware(true);
				SAXParser parser;
				parser = parserFactory.newSAXParser();
				parser.parse( new File ( serverPath), new CompareXMLHandler());
			} catch (ParserConfigurationException e) {

				e.printStackTrace();
			} catch (SAXException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
			return CompareXMLHandler.arrayList;
		}
		try {
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			parserFactory.setNamespaceAware(true);
			SAXParser parser = parserFactory.newSAXParser();

			parser.parse( new File ( serverPath), new CompareXMLHandler());
			Serverfile=CompareXMLHandler.arrayList;

			parser.parse( new File ( clientPath), new CompareXMLHandler());
			clientfile=CompareXMLHandler.arrayList;
			return compare(Serverfile,clientfile);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	return null;
	}
	private
    ArrayList<Items1> compare( ArrayList<Items1> serverfile2, ArrayList<Items1> clientfile2) {
		ArrayList<Items1> result =new ArrayList<Items1> ();
		for (Items1 f1 : serverfile2){
			if(!clientfile2.contains(f1)){
				result.add(f1);
			}
		}
		return result;
	}

}

class CompareXMLHandler extends DefaultHandler {

	public static ArrayList<Items1> arrayList;
	String name;
	double version;
	long   size;
	String string;
	private Items1 i;
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		arrayList=new ArrayList<Items1> ();
	}
	@Override
	public void characters(char[] ch, int start, int length)
	throws SAXException {
		super.characters(ch, start, length);
		string=new String ( ch, start, length).trim();
	}
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		for( Iterator<Items1> iterator = arrayList.iterator() ; iterator.hasNext();)
		{
			Log.e( "Element", "" + iterator.next().toString());
		}
	}
	@Override
	public void endElement( String uri, String localName, String qName)
	throws SAXException {
		super.endElement(uri, localName, qName);
		if(localName.equals("File"))
		{
			arrayList.add(i);
		}
		if(localName.equals("version"))
		{
			 i.setVersion(new Double ( string));
		}
		if(localName.equals("filename"))
		{
			i.setFileName(new String ( string));
		}
		
	}
	@Override
	public void startElement( String uri, String localName, String qName,
                              Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals("File"))
		{
			i=new Items1();
		}
	}
}
