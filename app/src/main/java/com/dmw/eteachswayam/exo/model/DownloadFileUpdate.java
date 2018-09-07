package com.dmw.eteachswayam.exo.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import com.dmw.eteachswayam.exo.activity.CheckingUpdateActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DownloadFileUpdate {

	String downloadPath;
	String downloadUrl;
	int    mode;
	int          bufferSize         = 4096;
	byte[]       retVal             = null;
	InputStream  in                 = null;
	OutputStream fileos             = null;
	long         numBytesDownloaded = 0;
	HttpURLConnection urlConnection;
	public static boolean downloadFinish = false;
	ArrayList<Items1> parseUpdateXmlArrayList;

	private ArrayList<Items1>      clientsdcardfileName;
	private ArrayList<Items1>      itemFileResult;
	private ArrayList<ChapterName> updatesArrayList;
	private ArrayList<Items1>      fulNameArrayList;
	private ArrayList<Items1>      resultUpdatedContentArrayList;
	private ArrayList<Items1>      resultedUpdetedxmlFileArrayList;
	private ArrayList<Items1>      finalArrayList;
	private SharedPreferences      prefs;
	private String                 ListPreference;

	private double            clientFileVersion;
	private ArrayList<Items1> chapterarray;
	ArrayList<Items1> VerSionArrayList;
	private       String            accountId;
	private       String            username;
	private       ArrayList<Items1> classArrayList;
	public static ArrayList<Items1> mainDisplayArrayList;
	public static String variable ="";
	Activity c;
	public DownloadFileUpdate( String accountid, String username, String url, String path, int mode, Activity c)
	{
		this.accountId=accountid;
		this.username=username;
		this.mode=mode;
		this.downloadPath = path;
		this.downloadUrl = url;
		this.c=c;
		//if(mode==1){
		prefs = PreferenceManager.getDefaultSharedPreferences( c.getBaseContext());
		ListPreference = prefs.getString("listPref", "digiGreen");
		if (ListPreference.equals("2")){
			SharedPreferences shared = c.getSharedPreferences( "path", 0);
			Constant.ContentPath= (shared.getString("pathdemo", ""+Constant.INTERNAL_SDCARD));
		}else {
			Constant.ContentPath= Constant.INTERNAL_SDCARD;
		}
		startDownload();
		//	}

	}

	private void startDownload() {
		String url = downloadUrl;
		downloadPath = downloadPath.replace("\\", "/");
		url=url.replace("\\", "/");
		url = url.replace(" ", "%20");
		new DownloadUpdatesXml().execute(url);
	}


	private
    ArrayList<Items1> compare( ArrayList<Items1> arrayList, ArrayList<Items1> arrayList1)
	{
		ArrayList<Items1> result =new ArrayList<Items1> ();
		if(arrayList!=null){
			for ( Iterator<Items1> iterator = arrayList.iterator() ; iterator.hasNext();) {
				Items1 items = (Items1) iterator.next();

				//boolean flag=false;
				if(arrayList1!=null)
					for ( Iterator<Items1> iterator2 = arrayList1.iterator() ; iterator2.hasNext();) {
						Items1 items1 = (Items1) iterator2.next();
						Log.e( "Item", "" + items1);
						if(items.equals(items1))
						{
							//		flag=true;
							if(!items.versionEquals(items1))
							{
								result.add(items);	
							}
						}
					}
				//if(!flag){
				//	result.add(items);
				//}
			}
		}
		return result;
	}
	class DownloadUpdatesXml extends AsyncTask<String, String, String>
	{
		private ProgressDialog p;


		@Override
		protected
        String doInBackground( String... params) {
			try{
				urlConnection = (HttpURLConnection ) new URL ( params[0]).openConnection();
				int fileLength = urlConnection.getContentLength();
				// open the file output stream for file writing

				fileos = new BufferedOutputStream ( new FileOutputStream ( downloadPath));
				// open the url input stream for reading the connection
				in = new BufferedInputStream ( urlConnection.getInputStream(), bufferSize);

				// write the file from the connection inputstream
				retVal = new byte[bufferSize];
				int length = 0;
				while((length = in.read(retVal)) > -1) {
					fileos.write(retVal, 0, length);
					publishProgress(""+(int) (numBytesDownloaded * 100 / fileLength));
					numBytesDownloaded += length;
				}

			} catch(IOException e) {
				Log.e( " ", e.getMessage());
				return "false";
			} finally {
				try {

					if(fileos != null) {
						fileos.flush();
						fileos.close();
					}
					if(in != null) {
						in.close();
					}
					urlConnection.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return "true";
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			parseXmlUpdates();

			//CheckingUpdateActivity.resultAdapter.notifyDataSetChanged();
			//
			//			if(CheckingUpdateActivity.resultArrayList.isEmpty())
			//			{
			//				Toast.makeText(act,"No Chapter For Update!", Toast.LENGTH_SHORT).show();
			//			}
			CheckingUpdateActivity.resultAdapter.notifyDataSetChanged();

			//			if(mainDisplayArrayList.isEmpty())
			//			{
			//				//MainActivity.updateContentTextViewfullProject.setText("No Chapter For Update");
			//				//Toast.makeText(c,"No Chapter For Update!", Toast.LENGTH_SHORT).show();
			//			}
			//			else{
			//				MainActivity.updateContentTextViewfullProject.setText("New Chapter Updates Available \n Please click on updates button");
			//			}
			//Toast.makeText(act, "Updates Not Available!", Toast.LENGTH_SHORT).show();
			//mProgressDialog.dismiss();
			if(mode==1){             
				p.dismiss();
				if(mainDisplayArrayList.isEmpty()){
					Toast.makeText( c, "Updates not available!", Toast.LENGTH_SHORT).show();
					
				}
			}
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(mode==1)
			{ 
				p=new ProgressDialog ( c);
				p.setMessage("Checking updates. Please wait...");
				p.setProgressStyle( ProgressDialog.STYLE_SPINNER);
				p.setCancelable(false);
				p.show();
			}

		}

	}

	private void parseXmlUpdates() {
		DataXMLParser1 parser;
		Items1         updateXmlItems1;
		File           f;
		File           oserver_E;
		parseUpdateXmlArrayList=new ArrayList<Items1> ();
		clientsdcardfileName=new ArrayList<Items1> ();
		itemFileResult=new ArrayList<Items1> ();
		updatesArrayList=new ArrayList<ChapterName> ();
		fulNameArrayList=new ArrayList<Items1> ();
		resultUpdatedContentArrayList=new ArrayList<Items1> ();
		resultedUpdetedxmlFileArrayList=new ArrayList<Items1> ();
		finalArrayList=new ArrayList<Items1> ();
		VerSionArrayList=new ArrayList<Items1> ();
		classArrayList=new ArrayList<Items1> ();
		mainDisplayArrayList=new ArrayList<Items1> ();
		//updatedFileResult=new ArrayList<Items1>();
		if(new File ( Constant.INTERNAL_SDCARD + accountId + "/" + username + "/UpdateFile.xml").exists())
		{
			parser=new DataXMLParser1(Constant.INTERNAL_SDCARD+accountId+"/"+username+"/UpdateFile.xml");
			parseUpdateXmlArrayList=parser.parser();

			//			for (int i = 0; i < parseUpdateXmlArrayList.size(); i++) {
			//				Items1 newItems1=new Items1();
			//				String parseUpdateXml=parseUpdateXmlArrayList.get(i).getFileName();
			//				parseUpdateXml=parseUpdateXml.substring(0,parseUpdateXml.lastIndexOf("\\"));
			//				parseUpdateXml=parseUpdateXml.substring(0,parseUpdateXml.lastIndexOf("\\"));
			//				newItems1.setFileName(parseUpdateXml);
			//				classArrayList.add(newItems1);
			//			}

		}
		try {
			//			FileInputStream fis=new FileInputStream(Constant.INTERNAL_SDCARD+accountId+"/"+username+"/cbsechapter.xml");
			//			FileOutputStream fos=new FileOutputStream(Constant.INTERNAL_SDCARD+accountId+"/"+username+"/cbsechapterD.xml");
			//
			//			new Rijndael();
			//			Rijndael.EDR(fis, fos, 2);
			//
			//			LayoutXmlParser layoutXmlParser =new LayoutXmlParser(Constant.INTERNAL_SDCARD+accountId+"/"+username+"/cbsechapterD.xml");
			//
			//			if(!parseUpdateXmlArrayList.isEmpty()){
			//				for (int i = 0; i < parseUpdateXmlArrayList.size(); i++) {
			//
			//					String nameofupdatesXml=parseUpdateXmlArrayList.get(i).getFileName();
			//					String nameofupdatesXml1=nameofupdatesXml.substring(0, nameofupdatesXml.lastIndexOf("\\")).replace("\\", "/");
			//					//nameofupdatesXml1=nameofupdatesXml1.substring(0, nameofupdatesXml1.lastIndexOf("\\"));
			//					updatesArrayList = layoutXmlParser.parseChapter(nameofupdatesXml1+"/",2);
			//
			//
			//
			//					for (int j = 0; j < updatesArrayList.size(); j++) {
			//
			//						String nameforDisplayUpdateStringUpToSubject=updatesArrayList.get(j).getKey();
			//						String nameforDisplayUpdateStringOnlyChapter=updatesArrayList.get(j).getValue();
			//						String fulName=nameforDisplayUpdateStringUpToSubject+nameforDisplayUpdateStringOnlyChapter;
			//						Items1 fulNameItems1=new Items1();
			//						fulNameItems1.setFileName(fulName.replaceAll(" ", "_"));
			//						if(!fulNameArrayList.contains(fulNameItems1)){
			//							fulNameArrayList.add(fulNameItems1);
			//						}
			//					}
			//
			//				}
			//			}
			if(!parseUpdateXmlArrayList.isEmpty()){
				for (int i = 0; i < parseUpdateXmlArrayList.size(); i++) {

					String nameofupdatesXml  =parseUpdateXmlArrayList.get( i).getFileName();
					String nameofupdatesXml1 =nameofupdatesXml.substring( 0, nameofupdatesXml.lastIndexOf( "\\")).replace( "\\", "/");
					//nameofupdatesXml1=nameofupdatesXml1.substring(0, nameofupdatesXml1.lastIndexOf("\\"));
					if(Constant.chapterArrayList==null){
						FileInputStream  fis =new FileInputStream ( Constant.INTERNAL_SDCARD + accountId + "/" + username + "/cbsechapter.xml");
						FileOutputStream fos =new FileOutputStream ( Constant.INTERNAL_SDCARD + accountId + "/" + username + "/cbsechapterD.xml");

						new Rijndael();
						Rijndael.EDR(fis, fos, 2);

						LayoutXmlParser layoutXmlParser =new LayoutXmlParser(Constant.INTERNAL_SDCARD+accountId+"/"+username+"/cbsechapterD.xml");

						Constant.chapterArrayList= layoutXmlParser.parseChapter(nameofupdatesXml1+"/",2);
					}


					for (int j = 0; j < Constant.chapterArrayList.size(); j++) {

						String nameforDisplayUpdateStringUpToSubject =Constant.chapterArrayList.get( j).getKey();
						if(Constant.chapterArrayList.get(j).getFlag()==2)
						{
							if(nameforDisplayUpdateStringUpToSubject.equals(nameofupdatesXml1+"/"))
							{
								String nameforDisplayUpdateStringOnlyChapter =Constant.chapterArrayList.get( j).getValue();
								String fulName                               = nameforDisplayUpdateStringUpToSubject + nameforDisplayUpdateStringOnlyChapter;
								Items1 fulNameItems1                         =new Items1();
								fulNameItems1.setFileName(fulName.replaceAll(" ", "_"));
								if(!fulNameArrayList.contains(fulNameItems1)){
									fulNameArrayList.add(fulNameItems1);
								}
							}
						}

					}

				}
			} 

			//xmlParserArrayList = parser.parseChapter(path1,2);
			//			for (int i = 0; i < updatesArrayList.size(); i++) {
			//				String nameforDisplayUpdateStringUpToSubject=updatesArrayList.get(i).getKey();
			//				String nameforDisplayUpdateStringOnlyChapter=updatesArrayList.get(i).getValue();
			//				String fulName=nameforDisplayUpdateStringUpToSubject+nameforDisplayUpdateStringOnlyChapter;
			//				Items1 fulNameItems1=new Items1();
			//				fulNameItems1.setFileName(fulName.replaceAll(" ", "_"));
			//
			//				fulNameArrayList.add(fulNameItems1);
			//			}
			if(!parseUpdateXmlArrayList.isEmpty()){
				for (int i = 0; i < parseUpdateXmlArrayList.size(); i++) {
					Items1 namei    =new Items1();
					String namefile =parseUpdateXmlArrayList.get( i).getFileName().replace( "\\", "/");
					namei.setFileName(namefile);
					resultedUpdetedxmlFileArrayList.add(namei);
				}
			}
			if(!fulNameArrayList.isEmpty() && !resultedUpdetedxmlFileArrayList.isEmpty() ){
				for (int j = 0; j < fulNameArrayList.size(); j++) {
					for (int j2 = 0; j2 < resultedUpdetedxmlFileArrayList.size(); j2++) {
						if(fulNameArrayList.get(j).equals(resultedUpdetedxmlFileArrayList.get(j2)))
						{
							resultUpdatedContentArrayList.add(fulNameArrayList.get(j));
						}
					}
				}
			}
			if(!resultUpdatedContentArrayList.isEmpty()){
				for ( Iterator<Items1> iterator = resultUpdatedContentArrayList.iterator() ; iterator.hasNext();) {
					updateXmlItems1 = (Items1) iterator.next();

					f=new File ( Constant.ContentPath + accountId + "/" + username + "/" + updateXmlItems1.getFileName().replace( "\\", "/") + "/OServer.xml");
					oserver_E=new File ( Constant.ContentPath + accountId + "/" + username + "/" + updateXmlItems1.getFileName().replace( "\\", "/") + "/OServer_E.xml");
					if(f.exists())
					{
						parser=new DataXMLParser1(f.getAbsolutePath());
						clientsdcardfileName.addAll(parser.parser());
					}
					if(oserver_E.exists())
					{
						parser=new DataXMLParser1(oserver_E.getAbsolutePath());
						clientsdcardfileName.addAll(parser.parser());
					}
					//					if(!f.exists()&&!oserver_E.exists()){
					//						
					//						clientsdcardfileName.add(new Items1(updateXmlItems1.getFileName(), 1.0,(long) 3.15));
					//					}

					if(!f.exists() && !oserver_E.exists())
					{
						File check =new File ( Constant.ContentPath + accountId + "/" + username + "/" + updateXmlItems1.getFileName().replace( "\\", "/") + "/index1_E.html");
						if(check.exists()){
							Items1 items1  =new Items1();
							String pathzxc = updateXmlItems1.getFileName().replace( "/", "\\") + "\\";
							items1.setFileName(pathzxc);
							clientsdcardfileName.add(items1);
						}
					} 


				}
			}
			if(!clientsdcardfileName.isEmpty()){
				for (int i = 0; i < clientsdcardfileName.size(); i++) {
					Items1 itemname     =clientsdcardfileName.get(i);
					String newItemValue =itemname.getFileName().toString();
					//					String itemnamevalue=itemname.getFileName().toString();
					//
					newItemValue=newItemValue.substring(0, newItemValue.lastIndexOf("\\"));

					Items1 items1 =new Items1();
					items1.setFileName(newItemValue);

					//new XML("/mnt/sdcard/eteach/assets/Content/4EAD5163/ZZC/"+itemnamevalue.replace("\\", "/")+"/OServer.xml","File","version","size");
					//String clientversion=XML.child1Value;
					File newFile =new File ( Constant.ContentPath + accountId + "/" + username + "/" + newItemValue.replace( "\\", "/") + "/OServer.xml");
					if(newFile.exists()){

						items1.setVersion(getVersion(Constant.ContentPath+accountId+"/"+username+"/"+newItemValue.replace("\\", "/")+"/OServer.xml"));
						itemFileResult.add(items1);
					}
					if(new File ( Constant.ContentPath + accountId + "/" + username + "/" + newItemValue.replace( "\\", "/") + "/OServer_E.xml").exists()){

						items1.setVersion(getVersion(Constant.ContentPath+accountId+"/"+username+"/"+newItemValue.replace("\\", "/")+"/OServer_E.xml"));
						itemFileResult.add(items1);
					}

					File newFile1 =new File ( Constant.ContentPath + accountId + "/" + username + "/" + newItemValue.replace( "\\", "/") + "/OServer.xml");
					File zxc      =new File ( Constant.ContentPath + accountId + "/" + username + "/" + newItemValue.replace( "\\", "/") + "/OServer_E.xml");
					if(!zxc.exists() && !newFile1.exists())
					{
						File indexPath =new File ( Constant.ContentPath + accountId + "/" + username + "/" + newItemValue.replace( "\\", "/") + "/index1_E.html");
						if(indexPath.exists())
						{
							items1.setVersion(1.0);
							itemFileResult.add(items1);

						}
					} 



					//Log.e("OserverVersion",""+ getVersion(Constant.INTERNAL_SDCARD+accountId+"/"+username+"/"+itemnamevalue.replace("\\", "/")+"/OServer.xml"));
					//itemFileResult.add(items1);
					//Log.e("Itemname", itemnamevalue);

				}
			}
			if(!resultUpdatedContentArrayList.isEmpty()){
				for (int i = 0; i < resultUpdatedContentArrayList.size(); i++) {
					Items1 actual       =new Items1();
					String actualString =resultUpdatedContentArrayList.get( i).getFileName();
					actual.setFileName(actualString.replace("/", "\\"));

					VerSionArrayList = parseChapter(Constant.INTERNAL_SDCARD+accountId+"/"+username+"/UpdateFile.xml",actualString);
					for (int j = 0; j < VerSionArrayList.size(); j++) {
						Items1 ff            =VerSionArrayList.get(j);
						String newName       =VerSionArrayList.get( j).getFileName();
						String actualString1 =actualString.replace( "/", "\\");
						if(newName.equals(actualString1))
						{
							Log.e( "UpdateVersion", "" + ff.getVersion());
							//new XML("/mnt/sdcard/eteach/assets/Content/UpdateFile.xml","File","version","size");
							//String updateXmlVersion=XML.child1Value;
							actual.setVersion(ff.getVersion());
							finalArrayList.add(actual);
						}
					}



				}
			}
			if(!finalArrayList.isEmpty() && !itemFileResult.isEmpty()){
				mainDisplayArrayList.addAll(compare(finalArrayList,itemFileResult));
				
				CheckingUpdateActivity.resultArrayList.addAll(compare(finalArrayList,itemFileResult));



			}

			//			for (int i = 0; i < CheckingUpdateActivity.resultArrayList.size(); i++) {
			//				Log.e("name", ""+CheckingUpdateActivity.resultArrayList.get(i));
			//
			//			}

			new DeleteFile(Constant.INTERNAL_SDCARD+accountId+"/"+username+"/"+"cbsechapterD.xml");
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public
    ArrayList<Items1> parseChapter( String fileName, String path) {

		try{
			Items1                 name;
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        docBuilder        = docBuilderFactory.newDocumentBuilder();
			Document               doc               = docBuilder.parse ( new File ( fileName));

			// normalize text representation
			doc.getDocumentElement ().normalize ();
			System.out.println ( "Root element of the doc is " +
                                 doc.getDocumentElement().getNodeName());

			chapterarray = new ArrayList<Items1> ();

			NodeList listOfPersons = doc.getElementsByTagName( "File");
			int      totalPersons  = listOfPersons.getLength();
			System.out.println( "Total no of people : " + totalPersons);

			for(int s=0; s<listOfPersons.getLength() ; s++){

				name = new Items1();
				Node firstPersonNode = listOfPersons.item( s);
				if( firstPersonNode.getNodeType() == Node.ELEMENT_NODE){


					Element firstPersonElement = (Element )firstPersonNode;

					//-------
					NodeList firstNameList    = firstPersonElement.getElementsByTagName( "filename");
					Element  firstNameElement = (Element )firstNameList.item( 0);

					NodeList textFNList = firstNameElement.getChildNodes();
					name.setFileName(((Node )textFNList.item( 0)).getNodeValue().trim());

					//										NodeList lastNameList = firstPersonElement.getElementsByTagName("size");
					//										Element lastNameElement = (Element)lastNameList.item(0);
					//					
					//										NodeList textLNList = lastNameElement.getChildNodes();
					//										name.set(((Node)textLNList.item(0)).getNodeValue().trim());


					NodeList ageList    = firstPersonElement.getElementsByTagName( "version");
					Element  ageElement = (Element )ageList.item( 0);

					NodeList textAgeList = ageElement.getChildNodes();
					name.setVersion( Double.parseDouble( ( (Node )textAgeList.item( 0)).getNodeValue().trim()));
					//------

					chapterarray.add(name);
				}//end of if clause


			}




		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return chapterarray;

	}

	public double getVersion(String fileName)
	{
		try {
			File                   fXmlFile  = new File ( fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        dBuilder  = dbFactory.newDocumentBuilder();
			Document               doc       = dBuilder.parse( fXmlFile);
			doc.getDocumentElement().normalize();

			System.out.println( "Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName( "File");

			System.out.println( "----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item( temp);

				System.out.println( "\nCurrent Element :" + nNode.getNodeName());

				if ( nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element ) nNode;

					//	System.out.println("Staff id : " + eElement.getAttribute("id"));
					System.out.println( "VerSion : " + eElement.getElementsByTagName( "version").item( 0).getTextContent());
					clientFileVersion= Double.parseDouble( eElement.getElementsByTagName( "version").item( 0).getTextContent());
					//					System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
					//					System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
					//					System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());

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
		return clientFileVersion;
	}
}
