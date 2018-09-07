package com.dmw.eteachswayam.exo.model;

import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Setting {
	public static  ArrayList<String> mMounts = new ArrayList<String> ();
	private static ArrayList<String> mVold   = new ArrayList<String> ();
	public static  boolean                               checkfordelete;
	public static  boolean                               checkforflagchange;
	public static  ArrayList<RegBinClass>                regBinarray;
	//public static ArrayList<RegBinClass> regBinarrayforfileplay;
	public static  ArrayList<RegBinClass>                arrayListRegBinClasses;
	private static ArrayList<ChapterName>                chapterarrayforcheck;
	private static MacFileBeanClass macFileBeanClass;

	public static void determineStorageOptions() {
		mMounts.clear();
		mVold.clear();
		readMountsFile();

		readVoldFile();

		compareMountsWithVold();

		//  testAndCleanMountsList();

	}
	private static void readMountsFile() {
		mMounts.add( Environment.getExternalStorageDirectory().getAbsolutePath());
		//mMounts.add("/mnt/sdcard");

		try {
			Scanner scanner = new Scanner ( new File ( "/proc/mounts"));
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.startsWith("/dev/block/vold/")) {
					String[] lineElements = line.split( " ");
					String   element      = lineElements[1];
					if (!element.equals( Environment.getExternalStorageDirectory().getAbsolutePath()))
						mMounts.add(element);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void readVoldFile() {
		mVold.add( Environment.getExternalStorageDirectory().getAbsolutePath());
		try {
			Scanner scanner = new Scanner ( new File ( "/system/etc/vold.fstab"));
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.startsWith("dev_mount")) {
					String[] lineElements;
					//	              	String val=line.replace("/t"," ");
					//	              	String val1=line.replaceAll("/t", " ");
					if(line.contains("\t")){
						lineElements = line.split("\\t");
					}else{
						lineElements = line.split(" ");
					}
					String element = lineElements[2];
					if(element.contains("\\"))
					{
						element.replaceAll("\\*", "/");
					}

					if (element.contains(":"))
						element = element.substring(0, element.indexOf(":"));

					// don't add the default vold path
					// it's already in the list.
					if (!element.equals( Environment.getExternalStorageDirectory().getAbsolutePath()))
					{

						mVold.add(element);
					}
				}
			}
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void compareMountsWithVold() {

		for (int i = 0; i < mMounts.size(); i++) {
			String mount = mMounts.get( i);
			if (!mVold.contains(mount)) 
				mMounts.remove(i--);
		}
		mVold.clear();
	}
	public static void deleteTagforClassName( String ClassNameForDelete, String fileName) {
		checkfordelete=true;
		try {
			DocumentBuilderFactory factory    = DocumentBuilderFactory.newInstance();
			DocumentBuilder        docBuilder = factory.newDocumentBuilder();
			Document               doc        = docBuilder.parse( new File ( fileName));
			NodeList               list       = doc.getElementsByTagName( "*");
			for (int i = 0; i <list.getLength(); i++) {
				Node node = (Node ) list.item( i);
				if (node.getNodeName().equalsIgnoreCase("item")) {
					NodeList childList = node.getChildNodes();
					for (int x = 0; x < childList.getLength(); x++) {
						Node child = (Node ) childList.item( x);
						if ( child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equalsIgnoreCase( "ClassPath") && child.getTextContent().equalsIgnoreCase( ClassNameForDelete)) {
							node.getParentNode().removeChild(node);
						}
					}
				}
			}
			try {
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer        transformer        = transformerFactory.newTransformer();
				DOMSource          source             = new DOMSource ( doc);
				StreamResult       result             = new StreamResult ( new File ( fileName));
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

		} catch (ParserConfigurationException pce) {
			checkfordelete=false;
			pce.printStackTrace();
		} catch (IOException ioe) {
			checkfordelete=false;
			ioe.printStackTrace();
		} catch (SAXException saxe) {
			checkfordelete=false;
			saxe.printStackTrace();
		}
		catch (Exception e) {
			checkfordelete=false;
			e.printStackTrace();
		}
	}

	public static void changeflagforspouser( String classNameForremoveFlag, String uptostring)
	{
		checkforflagchange=true;
		String keyname = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder        db;
		Document               doc = null;
		try {
			db = dbf.newDocumentBuilder();
			try {
				FileInputStream  fis =new FileInputStream ( uptostring + "/cbsechapter.xml");
				FileOutputStream fos =new FileOutputStream ( uptostring + "/cbsechapterD.xml");

				new Rijndael();
				Rijndael.EDR(fis, fos, 2);
				new DeleteFile(uptostring+"/cbsechapter.xml");
			}catch (Exception e) {
				e.printStackTrace();
			}
			File file = new File ( uptostring + "/cbsechapterD.xml");
			doc = db.parse(file);
			Node     root        = doc.getFirstChild();
			NodeList rootelement =root.getChildNodes();
			for(int i=0;i<rootelement.getLength();i++)
			{
				Node bal =rootelement.item( i);
				if(bal.getNodeName().equals("item")){
					NodeList itemss = bal.getChildNodes();
					for(int j=0;j<itemss.getLength();j++)
					{
						Node itemchild =itemss.item( j);
						if(itemchild.getNodeName().equals("key"))
						{	
							keyname=itemchild.getTextContent();
						}
						if(itemchild.getNodeName().equals("flag"))
						{
							if(keyname.contains(classNameForremoveFlag))
							{
								itemchild.setTextContent("0");

							}
						}
					}
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer        transformer        = transformerFactory.newTransformer();
			DOMSource          source             = new DOMSource ( doc);
			StreamResult       result             = new StreamResult ( file);
			transformer.transform(source, result);
			Log.e( "DONE", "");
			try {
				FileInputStream  fis =new FileInputStream ( uptostring + "/cbsechapterD.xml");
				FileOutputStream fos =new FileOutputStream ( uptostring + "/cbsechapter.xml");
				new Rijndael();
				Rijndael.EDR(fis, fos, 1);
				new DeleteFile(uptostring+"/cbsechapterD.xml");
				fis.close();
				fos.close();
			}catch (Exception e) {
				e.printStackTrace();
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			Log.e( "Error", "" + e);
			checkforflagchange=false;
		}
	}
	public static
    ArrayList<ChapterName> parseChapterforcheck( int mode, String filename) {

		try{
			ChapterName            name;
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        docBuilder        = docBuilderFactory.newDocumentBuilder();
			Document               doc               = docBuilder.parse ( new File ( filename));

			// normalize text representation
			doc.getDocumentElement ().normalize ();
			System.out.println ( "Root element of the doc is " +
                                 doc.getDocumentElement().getNodeName());

			chapterarrayforcheck = new ArrayList<ChapterName> ();
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
					int modecheck= Integer.parseInt( ( (Node )textAgeList.item( 0)).getNodeValue().trim());
					//------
					if(modecheck==mode)
					{
						chapterarrayforcheck.add(name);
					}

				}//end of if clause



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

		return chapterarrayforcheck;
	}

	public static
    ArrayList<MacFileBeanClass> parseMacXmlFile( String fileName)
	{
		ArrayList<MacFileBeanClass> arrayList =new ArrayList<MacFileBeanClass> ();
		macFileBeanClass=new MacFileBeanClass();

		try 
		{
			File                   fXmlFile  = new File ( fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        dBuilder  = dbFactory.newDocumentBuilder();
			Document               doc       = dBuilder.parse( fXmlFile);
			doc.getDocumentElement().normalize();


			NodeList nList = doc.getElementsByTagName( "key");



			for (int temp = 0; temp < nList.getLength(); temp++) 
			{

				Node nNode = nList.item( temp);


				if ( nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element ) nNode;

					String AencryptionKey  =eElement.getElementsByTagName( "VKeyA").item( 0).getTextContent();
					String Aencryptionsalt =eElement.getElementsByTagName( "VSaltA").item( 0).getTextContent();
					String Aencryptioniv   =eElement.getElementsByTagName( "VivA").item( 0).getTextContent();


					String DencryptionKey  =eElement.getElementsByTagName( "VKeyD").item( 0).getTextContent();
					String Dencryptionsalt =eElement.getElementsByTagName( "VSaltD").item( 0).getTextContent();
					String Dencryptioniv   =eElement.getElementsByTagName( "VivD").item( 0).getTextContent();



					macFileBeanClass.setAkey(AencryptionKey);
					macFileBeanClass.setAsalt(Aencryptionsalt);
					macFileBeanClass.setAiv(Aencryptioniv);

					macFileBeanClass.setDkey(DencryptionKey);
					macFileBeanClass.setDsalt(Dencryptionsalt);
					macFileBeanClass.setDiv(Dencryptioniv);

				}
				arrayList.add(macFileBeanClass);
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return arrayList;
	}

}

