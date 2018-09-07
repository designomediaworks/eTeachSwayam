package com.dmw.eteachswayam.exo.model;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class LayoutXmlParser {
    public String fileName;
    public static ArrayList<ChapterName> chapterarray =null;
    public static ArrayList<Board>  array1;
    public static ArrayList<String> array;
    public LayoutXmlParser(String name) {

        this.fileName = name;

    }
    public
    ArrayList<Board> parse ( final Context ctx){

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser        parser;
            parser = factory.newSAXParser();
            parser.parse( new File ( fileName), new DefaultHandler (){

                String string ;
                private Board board;
                @Override
                public void startDocument() throws SAXException {

                    super.startDocument();
                    array1 = new ArrayList<Board> ();
                }

                @Override
                public void startElement( String uri, String localName, String qName,
                                          Attributes attributes) throws SAXException {
                    if(localName.equals("BOARD")){
                        board=new Board();
                    }
                    super.startElement(uri, localName, qName, attributes);
                }
                @Override
                public void characters(char[] ch, int start, int length)
                        throws SAXException {

                    super.characters(ch, start, length);
                    string= new String ( ch, start, length);
                }
                @Override
                public void endElement( String uri, String localName, String qName)
                        throws SAXException,NullPointerException {

                    super.endElement(uri, localName, qName);
                   
                        if(localName.equals("Actual")){
                            board.setActualName(string);
                        }else if(localName.equals("item")){
                            board.setDisplayName(string);
                        }else if(localName.equals("BOARD")){
                            array1.add(board);
                        }
                    
                }

                @Override
                public void endDocument() throws SAXException {

                    super.endDocument();

                }
            });
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (NullPointerException e) {
            return null;
        }
        return LayoutXmlParser.array1;
    }
    public
    ArrayList<String> parse( final String path) {
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser        parser;
            parser = factory.newSAXParser();
            parser.parse( new File ( fileName), new DefaultHandler (){

                String string ;
                String key;
                @Override
                public void startDocument() throws SAXException {

                    super.startDocument();
                    array = new ArrayList<String> ();
                }


                @Override
                public void characters(char[] ch, int start, int length)
                        throws SAXException {

                    super.characters(ch, start, length);
                    string= new String ( ch, start, length);
                }
                @Override
                public void endElement( String uri, String localName, String qName)
                        throws SAXException {

                    super.endElement(uri, localName, qName);
                    if(localName.equals("key"))
                    {
                        key=string;
                        string="";
                    }
                    if(localName.equals("value")){
                        if(key.equals(path))
                            if(!string.equals(""))
                                array.add(string);
                    }
                }

                @Override
                public void endDocument() throws SAXException {

                    super.endDocument();
                    for ( Iterator<String> iterator = array.iterator() ; iterator.hasNext();) {
                        Log.e( "    " + iterator.next().toString(), "    " + path);
                    }

                }
            });
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;
    }

    public
    ArrayList<ChapterName> parseChapter( final String path, int mode) {

        try{
            ChapterName            name;
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder        docBuilder        = docBuilderFactory.newDocumentBuilder();
            Document               doc               = docBuilder.parse ( new File ( fileName));

            // normalize text representation
            doc.getDocumentElement ().normalize ();
            System.out.println ( "Root element of the doc is " +
                                 doc.getDocumentElement().getNodeName());

            chapterarray = new ArrayList<ChapterName> ();
            NodeList listOfPersons = doc.getElementsByTagName( "item");
            int      totalPersons  = listOfPersons.getLength();
            System.out.println( "Total no of people : " + totalPersons);

            for(int s=0; s<listOfPersons.getLength() ; s++){

                name = new ChapterName();
                Node firstPersonNode = listOfPersons.item( s);
                if( firstPersonNode.getNodeType() == Node.ELEMENT_NODE){


                    Element firstPersonElement = (Element )firstPersonNode;

                    //-------
                    NodeList firstNameList    = firstPersonElement.getElementsByTagName( "key");
                    Element  firstNameElement = (Element )firstNameList.item( 0);

                    NodeList textFNList = firstNameElement.getChildNodes();
                    name.setKey(((Node )textFNList.item( 0)).getNodeValue().trim());
                    //-------
                    NodeList lastNameList    = firstPersonElement.getElementsByTagName( "value");
                    Element  lastNameElement = (Element )lastNameList.item( 0);

                    NodeList textLNList = lastNameElement.getChildNodes();
                    name.setValue(((Node )textLNList.item( 0)).getNodeValue().trim());

                    //----
                    NodeList ageList    = firstPersonElement.getElementsByTagName( "flag");
                    Element  ageElement = (Element )ageList.item( 0);

                    NodeList textAgeList = ageElement.getChildNodes();
                    name.setFlag( Integer.parseInt( ( (Node )textAgeList.item( 0)).getNodeValue().trim()));
                    //------


                }//end of if clause

                chapterarray.add(name);

            }//end of for loop with s var


        }catch (SAXParseException err) {
            System.out.println ( "** Parsing error" + ", line "
                                 + err.getLineNumber () + ", uri " + err.getSystemId ());
            System.out.println( " " + err.getMessage ());

        }catch (SAXException e) {
            Exception x = e.getException ();
            ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
            t.printStackTrace ();
        }

        return chapterarray;
    }
}
