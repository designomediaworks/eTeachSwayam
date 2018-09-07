package com.dmw.eteachswayam.exo.model;

import android.util.Log;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class XML {

    public static String child1Value = "";
    public static String child2Value = "";
    public static NodeList nodeList;
    public static String[] pathArr;
    public static String[] verArr;
    public static String[] SizeArr;
    public static boolean counterComplete = false;
    public static boolean FRESHACTIVATION=false;
    private static CheckInfo checkInfo;
    private static CheckClientFile checkClientFile;

    //private static ArrayList<String> infoclientArrayList;
    //public static ArrayList<CLASS>classes;
    public  XML( String response, String node, String child1, String child2)
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
    public static int countfile(String filePath)
    {
        File f     = new File ( filePath);
        int  count = 0;
        for (File file : f.listFiles()) {
            if (file.isFile()) {

                if(file.getAbsolutePath().contains("lastAccess"))
                {
                    count--;
                }
                if(file.getAbsolutePath().contains("star.xml"))
                {
                    count--;
                }
                count++;


            }
        }
        return count;

    }
    public static void addNodeToXMLForCapecha( String FileName, String tagName, String value)
    {
        NodeList nodeList;
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setIgnoringComments(true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document        doc     = builder.parse( new File ( FileName));
            doc.getDocumentElement().normalize();

            nodeList = doc.getElementsByTagName("User");
            Node     node  = nodeList.item( 0);
            NodeList nodes = node.getChildNodes();

            Text    b = doc.createTextNode( value);
            Element q = doc.createElement( tagName);
            q.appendChild(b);
            nodes.item(0).getParentNode().insertBefore(q, nodes.item(0));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty( OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource ( doc);

            File file =new File ( FileName);

            StreamResult result = new StreamResult ( file);

            transformer.transform(source, result);

        } catch (DOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private static ArrayList<String> infoclientArrayList;
    public static
    ArrayList<String> countClientfile( String appFileString, String userName)
    {
        infoclientArrayList=new ArrayList<String> ();
        File   folder      = new File ( appFileString);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println( "File " + listOfFiles[i].getName());

                String[] name =listOfFiles[i].getAbsolutePath().split( userName);

                String pathString  =name[1].toString();
                String filename    =pathString.substring( pathString.lastIndexOf( "/"));
                String s           =pathString.substring( 0, pathString.lastIndexOf( "/")).toUpperCase();
                String chapterName =s.substring( s.lastIndexOf( "/"));
                infoclientArrayList.add(s.replace("/", "\\")+chapterName.replace("/", "\\")+filename.replace("/", "\\"));
            }
        }
        return infoclientArrayList;


    }
    public static void createAppXml( String filePath, String path) {




        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder        docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc         = docBuilder.newDocument();
            Element  rootElement = doc.createElement( "FilePath");
            doc.appendChild(rootElement);

            File   folder      = new File ( filePath);
            File[] listOfFiles = folder.listFiles();


            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    System.out.println( "File " + listOfFiles[i].getName());
                    Element staff = doc.createElement( "File");
                    rootElement.appendChild(staff);

                    Element firstname = doc.createElement( "Path");
                    firstname.appendChild(doc.createTextNode(path.replace("/", "\\")+"\\"+listOfFiles[i].getName()));
                    staff.appendChild(firstname);

                    Element lastname = doc.createElement( "Version");
                    lastname.appendChild(doc.createTextNode("1.0"));
                    staff.appendChild(lastname);
                    // lastname elements
                    Element size = doc.createElement( "Size");

                    String sizebyte = String.valueOf( listOfFiles[i].length());
                    Log.e( "", sizebyte);
                    size.appendChild(doc.createTextNode( String.valueOf( listOfFiles[i].length())));
                    staff.appendChild(size);

                }
            }



            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer        transformer        = transformerFactory.newTransformer();
            DOMSource          source             = new DOMSource ( doc);
            StreamResult       result             = new StreamResult ( new File ( filePath + "/info.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);
            new File ( filePath + "/.nomedia").createNewFile();


        } catch (DOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            new Rijndael();
            FileInputStream  fileInputStream  =new FileInputStream ( filePath + "/info.xml");
            FileOutputStream fileOutputStream =new FileOutputStream ( filePath + "/info.app");
            Rijndael.EDR(fileInputStream, fileOutputStream, 1);

            File file =new File ( filePath + "/info.xml");
            if(file.exists())
            {
                file.delete();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    public static void delete(File file)
    {

        if(file.isDirectory()){

            //directory is empty, then delete it
            if(file.list().length==0){

                file.delete();
                System.out.println( "Directory is deleted : "
                                    + file.getAbsolutePath());

            }else{

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File ( file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    if(!file.getAbsolutePath().endsWith(".zip")){
                        file.delete();
                    }
                    System.out.println( "Directory is deleted : "
                                        + file.getAbsolutePath());
                }
            }

        }else{
            //if file, then delete it
            if(!file.getAbsolutePath().endsWith(".zip")){
                file.delete();
            }
            System.out.println( "File is deleted : " + file.getAbsolutePath());
        }
    }



    private static ArrayList<String> infoArrayList;
    public static
    ArrayList<String> readXml( String filePath){
        try {
            infoArrayList=new ArrayList<String> ();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder        docBuilder        = docBuilderFactory.newDocumentBuilder();
            Document               doc               = docBuilder.parse ( new File ( filePath + "/info.xml"));

            // normalize text representation
            doc.getDocumentElement ().normalize ();
            System.out.println ( "Root element of the doc is " +
                                 doc.getDocumentElement().getNodeName());


            NodeList listOfPersons = doc.getElementsByTagName( "File");
            int      totalPersons  = listOfPersons.getLength();
            System.out.println( "Total no of people : " + totalPersons);

            for(int s=0; s<listOfPersons.getLength() ; s++){


                Node firstPersonNode = listOfPersons.item( s);
                if( firstPersonNode.getNodeType() == Node.ELEMENT_NODE){


                    Element firstPersonElement = (Element )firstPersonNode;

                    //-------
                    NodeList firstNameList    = firstPersonElement.getElementsByTagName( "Path");
                    Element  firstNameElement = (Element )firstNameList.item( 0);

                    NodeList textFNList = firstNameElement.getChildNodes();
                    System.out.println( "First Name : " +
                                        ((Node )textFNList.item( 0)).getNodeValue().trim());
                    infoArrayList.add(((Node )textFNList.item( 0)).getNodeValue().trim());

                }//end of if clause


            }//end of for loop with s var
            File file =new File ( filePath + "/info.xml");
            if(file.exists())
            {
                file.delete();
            }

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
        //System.exit (0);
        return infoArrayList;



    }//end of main
    static         ArrayList<String> infoArrayListforsize;
    private static String            size;

    public static
    String readXmlforSize( String filePath, String name){
        try {


            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder        docBuilder        = docBuilderFactory.newDocumentBuilder();
            Document               doc               = docBuilder.parse ( new File ( filePath + "/info.xml"));

            // normalize text representation
            doc.getDocumentElement ().normalize ();
            System.out.println ( "Root element of the doc is " +
                                 doc.getDocumentElement().getNodeName());


            NodeList listOfPersons = doc.getElementsByTagName( "File");
            int      totalPersons  = listOfPersons.getLength();
            System.out.println( "Total no of people : " + totalPersons);

            for(int s=0; s<listOfPersons.getLength() ; s++){


                Node firstPersonNode = listOfPersons.item( s);
                if( firstPersonNode.getNodeType() == Node.ELEMENT_NODE){


                    Element firstPersonElement = (Element )firstPersonNode;

                    //-------
                    NodeList firstNameList    = firstPersonElement.getElementsByTagName( "Path");
                    Element  firstNameElement = (Element )firstNameList.item( 0);

                    NodeList textFNList = firstNameElement.getChildNodes();
                    System.out.println( "First Name : " +
                                        ((Node )textFNList.item( 0)).getNodeValue().trim());

                    String fileName =( (Node )textFNList.item( 0)).getNodeValue().trim();


                    NodeList fileSize                = firstPersonElement.getElementsByTagName( "Size");
                    Element  fileSizefileSizeelement = (Element )fileSize.item( 0);

                    NodeList textFNListsize = fileSizefileSizeelement.getChildNodes();
                    System.out.println( "First Name : " +
                                        ((Node )textFNListsize.item( 0)).getNodeValue().trim());

                    //Constant.infoArrayListforFilesize.add();
                    String zxc       =fileName;
                    String indexfile =zxc.substring( zxc.lastIndexOf( "\\"));
                    String fullPath  =zxc.substring( 0, zxc.lastIndexOf( "\\"));
                    String actual    = fullPath.toUpperCase() + indexfile;
                    if(actual.equals(name)){
                        size=((Node )textFNListsize.item( 0)).getNodeValue().trim();
                    }


                }//end of if clause


            }//end of for loop with s var
            File file =new File ( filePath + "/info.xml");
            if(file.exists())
            {
                //file.delete();
            }

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
        //System.exit (0);
        return size;



    }//end of main
    public static
    String getLength( String name)
    {
        File   file =new File ( name);
        String size = String.valueOf( file.length());
        return size;

    }

    public static void addNodeToXML( String FileName, String tagName, String value)
    {
        NodeList nodeList;


        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setIgnoringComments(true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document        doc     = builder.parse( new File ( FileName));
            doc.getDocumentElement().normalize();

            nodeList = doc.getElementsByTagName("Users");
            Node     node  = nodeList.item( 0);
            NodeList nodes = node.getChildNodes();

            Text    b = doc.createTextNode( value);
            Element q = doc.createElement( tagName);
            q.appendChild(b);
            nodes.item(0).getParentNode().insertBefore(q, nodes.item(0));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty( OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource ( doc);

            File file =new File ( FileName);

            StreamResult result = new StreamResult ( file);

            transformer.transform(source, result);

        } catch (DOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }


    public static void parceBySAX(String response) {
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser        parser  =factory.newSAXParser();
            parser.parse( new File ( response), new DefaultHandler (){
                String str;
                @Override
                public void characters(char[] ch, int start, int length)
                        throws SAXException {
                    super.characters(ch, start, length);
                    str=new String ( ch, start, length);
                }

                @Override
                public void endElement( String uri, String localName,
                                        String qName) throws SAXException {
                    if(localName.equals("Status")){
                        System.out.println( "checking     " + str);
                        int i = Integer.parseInt( str.trim());
                        if(i==1){
                            FRESHACTIVATION=true;
                        }
                    }
                    super.endElement(uri, localName, qName);
                }

            });
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

    public static void parseRepetedly( String filePath, String node2, String child1,
                                       String child2) throws ParserConfigurationException, SAXException,
            IOException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder        db  = dbf.newDocumentBuilder();
        Document               doc = db.parse( new File ( filePath));
        doc.getDocumentElement().normalize();

        nodeList = doc.getElementsByTagName(node2);
        Node node = nodeList.item( 0);

        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node temp = node.getChildNodes().item( i);
            //			String t = temp.getNodeName();
            if (temp.getNodeName().equalsIgnoreCase(child1)) {
                for (int j = 0; j < temp.getChildNodes().getLength(); j++) {
                    Node temp1 = temp.getChildNodes().item( j);
                    //				String t1 = temp1.getNodeName();

                    if (temp1.getNodeName().equalsIgnoreCase(child2)) {
                        child1Value = temp.getTextContent();
                        continue;
                    }
                }  }
        }
    }

    public static boolean dateFound;
    public static void searchContent( String filePath, String data)
    {
        dateFound = false;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder        db;
        Document               doc = null;
        try {
            db = dbf.newDocumentBuilder();

            File file = new File ( filePath);

            doc = db.parse(file);
            Element  root     = doc.getDocumentElement();
            NodeList rootlist = root.getChildNodes();
            for(int i=0; i<rootlist.getLength(); i++) {
                Element  date     = (Element )rootlist.item( i);
                NodeList datelist = date.getChildNodes();

                Element  dt      = (Element )datelist.item( 0);
                NodeList dtlist  = dt.getChildNodes();
                Text     dttext  = (Text )dtlist.item( 0);
                String   olddata = dttext.getData();

                if(olddata.equals(data)) {
                    dateFound  = true;
                    break;
                }
                else
                    dateFound = false;
            }
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dateFound = false;
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dateFound = false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dateFound = false;
        }
    }
    public static void changeContent( String filePath, String newdt) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder        db;
        Document               doc = null;
        try {
            db = dbf.newDocumentBuilder();

            File file = new File ( filePath);

            doc = db.parse(file);
            Element  root     = doc.getDocumentElement();
            NodeList rootlist = root.getChildNodes();
            for(int i=0; i<rootlist.getLength(); i++) {
                Element  date     = (Element )rootlist.item( i);
                NodeList datelist = date.getChildNodes();

                Element  dt     = (Element )datelist.item( 0);
                NodeList dtlist = dt.getChildNodes();
                Text     dttext = (Text )dtlist.item( 0);
                String   olddt  = dttext.getData();


                if(olddt.equals(newdt)) {
                    Element  flag     = (Element )datelist.item( 1);
                    NodeList flaglist = flag.getChildNodes();
                    Text     flagtext = (Text )flaglist.item( 0);

                    Element  counter     = (Element )datelist.item( 2);
                    NodeList counterlist = counter.getChildNodes();
                    Text     countertext = (Text )counterlist.item( 0);

                    if(flagtext.getData().equals("0"))
                    {
                        flagtext.setNodeValue("1");

                        countertext.setNodeValue("1");
                        break;
                    }
                    else if(flagtext.getData().equals("1")&&( Integer.parseInt( countertext.getData()) >= 1))
                    {
                        if( countertext.getNodeValue().equals("100") || Integer.parseInt( countertext.getNodeValue()) > 100)
                        {
                            counterComplete  = true;
                            break;
                        }
                        else
                        {
                            countertext.setNodeValue(""+( Integer.parseInt( countertext.getData()) + 1));
                            break;
                        }
                    }
                }
            }
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

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer        transformer        = transformerFactory.newTransformer();
            DOMSource          source             = new DOMSource ( doc);
            StreamResult       result             = new StreamResult ( new File ( filePath));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    static         DateFormat formatter ;
    static         Date       updatedSvDate ;
    private static Date       svActDate;
    private static Date       currentDt;
    private static Date       prevDt;
    private static String     prevDtString;

    public static void disableDates( String newdt, String frstdt) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder        db;
        Document               doc = null;
        try {
            db = dbf.newDocumentBuilder();

            File file = new File ( "/assets/Content/calendarD.xml");

            doc = db.parse(file);
            Element  root     = doc.getDocumentElement();
            NodeList rootlist = root.getChildNodes();
            for(int i=0; i<rootlist.getLength(); i++) {
                Element  date     = (Element )rootlist.item( i);
                NodeList datelist = date.getChildNodes();

                Element  dt     = (Element )datelist.item( 0);
                NodeList dtlist = dt.getChildNodes();
                Text     dttext = (Text )dtlist.item( 0);
                String   olddt  = dttext.getData();

                formatter = new SimpleDateFormat ( "yyyyMMdd");
                try {
                    updatedSvDate = (Date )formatter.parse( newdt);
                    svActDate = (Date )formatter.parse( frstdt);

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                for( Date dateFor = svActDate ; dateFor.compareTo( updatedSvDate) < 0; new Date ( dateFor.getTime() + ( 1000 * 60 * 60 * 24)))
                {
                    if(olddt.equals(dateFor)) {
                        Element  flag     = (Element )datelist.item( 1);
                        NodeList flaglist = flag.getChildNodes();
                        Text     flagtext = (Text )flaglist.item( 0);

                        if(flagtext.getData().equals("1"))
                        {
                            Element  counter     = (Element )datelist.item( 2);
                            NodeList counterlist = counter.getChildNodes();
                            Text     countertext = (Text )counterlist.item( 0);
                            countertext.setNodeValue("100");
                            break;
                        }
                    }
                }


            }
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

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer        transformer        = transformerFactory.newTransformer();
            DOMSource          source             = new DOMSource ( doc);
            StreamResult       result             = new StreamResult ( new File ( "/assets/Content/calendarD.xml"));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void serverXML(String fileName) {
        try {
            DocumentBuilderFactory dbf  = DocumentBuilderFactory.newInstance();
            DocumentBuilder        db   = dbf.newDocumentBuilder();
            File                   file = new File ( fileName);
            if (file.exists()) {
                Document doc    = db.parse( file);
                Element  docEle = doc.getDocumentElement();

                NodeList updateCount = docEle.getElementsByTagName( "File");

                pathArr = new String[updateCount.getLength()];
                verArr = new String[updateCount.getLength()];
                SizeArr = new String[updateCount.getLength()];

                if (updateCount != null && updateCount.getLength() > 0) {
                    for (int i = 0; i < updateCount.getLength(); i++) {

                        Node node = updateCount.item( i);

                        if ( node.getNodeType() == Node.ELEMENT_NODE) {

                            Element  e        = (Element ) node;
                            NodeList nodeList = e.getElementsByTagName( "Path");

                            String path =nodeList.item( 0).getChildNodes().item( 0)
                                    .getNodeValue();

                            pathArr[i]=path;

                            nodeList = e.getElementsByTagName("Version");

                            String version = nodeList.item( 0).getChildNodes().item( 0)
                                    .getNodeValue();

                            verArr[i]=version;

                            nodeList = e.getElementsByTagName("Size");

                            String Size = nodeList.item( 0).getChildNodes().item( 0)
                                    .getNodeValue();

                            SizeArr[i]=Size;
                        }
                    }
                } else {
                    // System.exit(1);
                }
            }
        } catch (Exception e) {
            System.out.println( e);
        }
    }

    public static void editByDOM( String response, String node1, String child1,
                                  String value) throws ParserConfigurationException, SAXException,
            IOException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder        db  = dbf.newDocumentBuilder();
        Document               doc = db.parse( new File ( response));

        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName( node1);
        Node     node     = nodeList.item( 0);

        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node temp = node.getChildNodes().item( i);
            if (temp.getNodeName().equalsIgnoreCase(child1)) {
                {
                    temp.setTextContent(value);
                }
            }
        }
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer        transformer        = transformerFactory.newTransformer();
            DOMSource          source             = new DOMSource ( doc);
            StreamResult       result             = new StreamResult ( new File ( response));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void editByDOMForUI( String response, String node1, String child1,
                                       String value, String path) throws ParserConfigurationException, SAXException,
            IOException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder        db  = dbf.newDocumentBuilder();
        Document               doc = db.parse( new File ( response));

        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName( node1);
        for(int j=0;j<nodeList.getLength();j++){
            Node         node         = nodeList.item( j);
            NamedNodeMap attributes   = node.getAttributes();
            Node         theAttribute = attributes.item( 0);
            if(theAttribute.getNodeValue().equals(path)){
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    Node temp = node.getChildNodes().item( i);
                    if (temp.getNodeName().equalsIgnoreCase(child1)) {
                        {
                            temp.setTextContent(value);
                        }
                    }
                }
            }
        }
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer        transformer        = transformerFactory.newTransformer();
            DOMSource          source             = new DOMSource ( doc);
            StreamResult       result             = new StreamResult ( new File ( response));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void disablePrevDates( String filePath, String currentDtString) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder        db;
        Document               doc = null;
        try {
            db = dbf.newDocumentBuilder();

            File file = new File ( filePath);

            doc = db.parse(file);
            Element  root     = doc.getDocumentElement();
            NodeList rootlist = root.getChildNodes();
            for(int i=0; i<rootlist.getLength(); i++) {
                Element  date     = (Element )rootlist.item( i);
                NodeList datelist = date.getChildNodes();

                Element  dt     = (Element )datelist.item( 0);
                NodeList dtlist = dt.getChildNodes();
                Text     dttext = (Text )dtlist.item( 0);
                String   olddt  = dttext.getData();


                formatter = new SimpleDateFormat ( "yyyyMMdd");
                try {
                    currentDt = (Date )formatter.parse( currentDtString);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentDt);
                    calendar.add( Calendar.DAY_OF_YEAR, -1);
                    prevDt = calendar.getTime();

                    SimpleDateFormat sdf = new SimpleDateFormat ( "yyyyMMdd");

                    prevDtString = sdf.format(prevDt);
                    prevDtString=prevDtString.substring(2, 4)+"-"+prevDtString.substring(4, 6)+"-"+prevDtString.substring(6, prevDtString.length());

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if(olddt.equals(prevDtString)) {
                    Element  flag     = (Element )datelist.item( 1);
                    NodeList flaglist = flag.getChildNodes();
                    Text     flagtext = (Text )flaglist.item( 0);

                    if(flagtext.getData().equals("1"))
                    {
                        Element  counter     = (Element )datelist.item( 2);
                        NodeList counterlist = counter.getChildNodes();
                        Text     countertext = (Text )counterlist.item( 0);
                        countertext.setNodeValue("100");
                        break;
                    }
                }


            }
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

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer        transformer        = transformerFactory.newTransformer();
            DOMSource          source             = new DOMSource ( doc);
            StreamResult       result             = new StreamResult ( new File ( filePath));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void deleteContent( String filePath, String data)
    {
        dateFound = false;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder        db;
        Document               doc = null;
        try {
            db = dbf.newDocumentBuilder();

            File file = new File ( filePath);

            doc = db.parse(file);
            Element  root     = doc.getDocumentElement();
            NodeList rootlist = root.getChildNodes();
            for(int i=0; i<rootlist.getLength(); i++) {
                Element  date     = (Element )rootlist.item( i);
                NodeList datelist = date.getChildNodes();

                Element  dt      = (Element )datelist.item( 0);
                NodeList dtlist  = dt.getChildNodes();
                Text     dttext  = (Text )dtlist.item( 0);
                String   olddata = dttext.getData();

                if(olddata.equals(data)) {
                    date.removeChild(dt);
                    break;
                }
            }
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
    public static void createRenewCountTagInStd( String filePath, String value, String key, String root) {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setIgnoringComments(true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document        doc     = builder.parse( new File ( filePath));

            doc.getDocumentElement().normalize();


            nodeList = doc.getElementsByTagName(root);
            Node     node  = nodeList.item( 0);
            NodeList nodes = node.getChildNodes();
            Text     a     = doc.createTextNode( value);
            Element  p     = doc.createElement( key);
            p.appendChild(a);
            nodes.item(0).getParentNode().insertBefore(p, nodes.item(0));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty( OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource ( doc);

            File file =new File ( filePath);

            StreamResult result = new StreamResult ( file);

            transformer.transform(source, result);



        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //	public static void parceDataFile(String response) {
    //		try {
    //
    //			SAXParserFactory factory =SAXParserFactory.newInstance();
    //			SAXParser parser =factory.newSAXParser();
    //			parser.parse(new File(response),new DefaultHandler(){
    //				String str;
    //				CLASS class1;
    //				@Override
    //				public void startDocument() throws SAXException {
    //					super.startDocument();
    //					classes=new ArrayList<CLASS>();
    //				}
    //				@Override
    //				public void startElement(String uri, String localName,
    //						String qName, Attributes attributes)
    //				throws SAXException {
    //					super.startElement(uri, localName, qName, attributes);
    //					if(localName.equals("CLASS"))
    //						class1 = new CLASS();
    //				}
    //				@Override
    //				public void characters(char[] ch, int start, int length)
    //				throws SAXException {
    //					super.characters(ch, start, length);
    //					str=new String(ch,start,length);
    //				}
    //
    //				@Override
    //				public void endElement(String uri, String localName,
    //						String qName) throws SAXException {
    //					if(localName.equals("boardName")){
    //						class1.setBoardName(str);
    //					}else if(localName.equals("mediam")){
    //						class1.setMediam(str);
    //					}else if(localName.equals("clas")){
    //						class1.setClas(str);
    //					}else if(localName.equals("orignalSize")){
    //						class1.setOrignalSize(Long.parseLong(str));
    //					}else if(localName.equals("actualsixe")){
    //						class1.setActualsixe(Long.parseLong(str));
    //					}else if(localName.equals("flag")){
    //						class1.setFlag(Boolean.parseBoolean(str));
    //					}else if(localName.equals("CLASS")){
    //						classes.add(class1);
    //					}
    //					super.endElement(uri, localName, qName);
    //
    //				}
    //
    //			});
    //		} catch (ParserConfigurationException e) {
    //			// TODO Auto-generated catch block
    //			e.printStackTrace();
    //		} catch (SAXException e) {
    //			// TODO Auto-generated catch block
    //			e.printStackTrace();
    //		} catch (IOException e) {
    //			// TODO Auto-generated catch block
    //			e.printStackTrace();
    //		}
    //
    //	}
    public static
    String keydecryption()
    {
        String demoKeyafterchangeXml = null;

        new Rijndael();
        String foundationencrypt = Constant.INTERNAL_SDCARD + "foundation.xml";
        String foundationdecrypt = Constant.INTERNAL_SDCARD + "foundationD.xml";

        File filefoundation = new File ( foundationencrypt);
        if (filefoundation.exists()) {

            try {
                FileInputStream foundationFileInputStream = new FileInputStream (
                        foundationencrypt);
                FileOutputStream foundationFileOutputStream = new FileOutputStream (
                        foundationdecrypt);
                Rijndael.EDR(foundationFileInputStream,
                        foundationFileOutputStream, 2);

                new XMLParser(foundationdecrypt, "key", "EcryptionKey", "");
                String key = XMLParser.child1Value;
                demoKeyafterchangeXml = new TripleDesString().decrypt(key); // change
                // key

                File deletefoundation = new File ( foundationdecrypt);
                if (deletefoundation.exists()) {
                    deletefoundation.delete();
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return demoKeyafterchangeXml;


    }
}

