package com.dmw.eteachswayam.exo.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

import com.dmw.eteachswayam.exo.activity.CoupanActivation;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHttpResponse;

public class Reload extends AsyncTask<String,String,String> {
	private       Activity       act;
	public static double         MB_Available;
	private       Intent         i;
	private       ProgressDialog mProgressDialog;

	boolean checkunknowhostException=true;

	static ArrayList<ReloadValueStore> array;
	public Reload( Activity act, Intent i){
		this.act=act;
		this.i=i;

	}

	protected void onPreExecute() {

		StatFs stat_fs        = new StatFs ( Environment.getExternalStorageDirectory().getPath());
		double avail_sd_space = (double)stat_fs.getAvailableBlocks() *(double)stat_fs.getBlockSize();
		//double MB_Available = (avail_sd_space / 10485783);
		MB_Available = (avail_sd_space /1048576);
		// MB_Available=MB_Available-20;
		Log.d( "AvailableMB", "" + MB_Available);
		if(MB_Available>=10){
			mProgressDialog = new ProgressDialog ( act);
			mProgressDialog.setMessage("Sending information to server...");
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
		}else
			Toast.makeText( this.act, MB_Available + "MB is available and \n minimum required memory for this application is 100MB", Toast.LENGTH_LONG).show();

	};

	protected void onPostExecute(String res) {
		try {
			if(MB_Available>=10){
				if(res!=null){
					if(!res.equals("NOT Valid")){
						JSONObject jsonObject =new JSONObject ( res);

						JSONArray hrArray = jsonObject.getJSONArray( "posts");

						new Rijndael();
						array=new ArrayList<ReloadValueStore> ();
						for(int i = 0, k=0; i < hrArray.length(); i++,k++){ 
							JSONObject c = hrArray.getJSONObject( i);
							if(k==0){
								if(!c.isNull("table1")){

									JSONObject mac = c.getJSONObject( "table1");

									String AccountID        = mac.getString( "V_AccountId");
									String HddId            = mac.getString( "V_HddId");
									String V_UserName       = mac.getString( "V_UserName");
									String V_Password       = mac.getString( "V_Password");
									String V_RetailIDid     =mac.getString( "V_RetailID");
									String V_AppNameinreset =mac.getString( "V_AppName");
									try {

										File macFile =new File ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + AccountID + "/" + V_UserName + "/macD.xml");
										if(!macFile.exists()){
											macFile.getParentFile().mkdirs();
											macFile.createNewFile();

											RandomAccessFile rf = new RandomAccessFile ( macFile, "rw");
											String           s  = "<Root><User><AcountId>" + AccountID + "</AcountId><HddId>" + HddId + "</HddId><UserName>" + V_UserName + "</UserName><RetId>" + V_RetailIDid + "</RetId></User></Root>";
											rf.write(s.getBytes());
											rf.close();
										}else{

											RandomAccessFile rf = new RandomAccessFile ( macFile, "rw");
											String           s  = "<User><AcountId>" + AccountID + "</AcountId><HddId>" + HddId + "</HddId></User>";
											rf.seek((rf.length()-("</User>".length())) );
											rf.write(s.getBytes());
											rf.close();                 
										}
										FileInputStream  fis =new FileInputStream ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + AccountID + "/" + V_UserName + "/macD.xml");
										FileOutputStream fos =new FileOutputStream ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + AccountID + "/" + V_UserName + "/mac.xml");

										new Rijndael();
										Rijndael.EDR(fis, fos, 1);

										new DeleteFile( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + AccountID + "/" + V_UserName + "/macD.xml");

										UserXMLParser.addUserTOXML(new User(V_UserName,V_Password,HddId,V_AppNameinreset,""),AccountID);//
										SharedPreferences        settings = act.getApplicationContext().getSharedPreferences( "username", 0);
										SharedPreferences.Editor editor   = settings.edit();
										editor.putString("user", V_UserName);
										editor.putString("ACCOUNTID", AccountID);
										editor.commit();

										File f = new File ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + AccountID + "/" + V_UserName);
										if(!f.exists())
										{
											f.mkdirs();
										}
										new DownloadUserFileFromReload(act,f.getAbsolutePath().toString(),this.i,V_UserName,HddId,AccountID).execute("/XMLFile");


									} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}else{

									Toast.makeText( act, "Invalid username or password!", Toast.LENGTH_LONG).show();

									mProgressDialog.dismiss();
								}
							}else{
								JSONObject Coupan = c.getJSONObject( "table2");

								String V_Board       = Coupan.getString( "V_Board");
								String V_Medium      = Coupan.getString( "V_Medium");
								String V_Class       = Coupan.getString( "V_Class");
								String Dt_ActDate    = Coupan.getString( "Dt_ActDate");
								String N_CalCounter  = Coupan.getString( "N_CalCounter");
								String V_CouponNo    =Coupan.getString( "V_CouponNo");
								String V_CouponRefNo =Coupan.getString( "V_CouponRefNo");

								if(!Dt_ActDate.equals("null"))
									array.add(new ReloadValueStore(V_Board, V_Medium, V_Class, Dt_ActDate, N_CalCounter,V_CouponNo,V_CouponRefNo));
							}
						}
					}
				}
			}

			mProgressDialog.dismiss();
			if(!checkunknowhostException)
			{
				Toast.makeText( act, "Internet Connection Lost. Please Try Again.", Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText( act, "Reset Failed!", Toast.LENGTH_LONG).show();
			mProgressDialog.dismiss();
		}


	}

	@Override
	protected
    String doInBackground( String... params) {
		String doin_res =null;
		checkunknowhostException=true;
		if(MB_Available>=10){
			String url;
			try {
				if(params[0].equals("eteach"))
					url= "http://"+Constant.WEBSERVER+"/designo_pro/ws_reset.php?usrnm="+params[0]+"&pwd="+params[1]+"&hddid=000123456789"+"&app="+Constant.appName+"&format=json";
				else
					url= "http://"+Constant.WEBSERVER+"/designo_pro/ws_reset.php?usrnm="+params[0]+"&pwd="+params[1]+"&hddid="+getUdid()+"&app="+Constant.appName+"&format=json";
				HttpGet httpget = new HttpGet ( url);

				HttpClient httpclient = new DefaultHttpClient ();
				BasicHttpResponse httpResponse = 
						(BasicHttpResponse ) httpclient.execute( httpget);

				HttpEntity     entity = httpResponse.getEntity();
				InputStream    is     = entity.getContent();
				BufferedReader reader = new BufferedReader ( new InputStreamReader ( is));
				StringBuilder  sb     = new StringBuilder ();
				String         line   = null;

				while ((line = reader.readLine()) != null) { 
					sb.append(line + "\n"); 
				} 
				is.close(); 
				doin_res = sb.toString();


			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (UnknownHostException e) {
				e.printStackTrace();
				checkunknowhostException=false;
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return doin_res;
	}

	private
    String getUdid() {

		final String macAddr, androidId;
		String       id = null;

		WifiManager wifiMan = (WifiManager )act.getApplicationContext().getSystemService( Context.WIFI_SERVICE);
		WifiInfo    wifiInf = wifiMan.getConnectionInfo();

		if(!wifiMan.isWifiEnabled())
		{
			wifiMan.setWifiEnabled(true);
			macAddr = wifiInf.getMacAddress();
			Log.e( "macAddr", "" + macAddr);
			androidId = "" + android.provider.Settings.Secure.getString(act.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
			Log.e( "androidId", "" + androidId);
			UUID deviceUuid = new UUID ( androidId.hashCode(), macAddr.hashCode());
			id=deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1);
			// text.setText(deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1));
			Log.e( "UUID", "" + id);
			wifiMan.setWifiEnabled(false);
		}
		else{
			macAddr = wifiInf.getMacAddress();
			Log.e( "macAddr", "" + macAddr);
			androidId = "" + android.provider.Settings.Secure.getString(act.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
			Log.e( "androidId", "" + androidId);
			UUID deviceUuid = new UUID ( androidId.hashCode(), macAddr.hashCode());
			id=deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1);
			// text.setText(deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1));
			Log.e( "UUID", "" + id);
		}
		return id;
	}
}

class DownloadUserFileFromReload extends AsyncTask<String, String, String>
{
	private Activity       act;
	private String         downloadPath;
	private ProgressDialog dialog;
	private String         urlpath;
	int          bufferSize = 4096;
	byte[]       retVal     = null;
	InputStream  in         = null;
	OutputStream fileos     = null;
	Intent i;
	long numBytesDownloaded = 0;
	HttpURLConnection urlConnection;
	private String            uptoString;
	private ArrayList<String> infoappArrayList;
	private ArrayList<String> downloadappArrayList;
	private String            username;
	private String            PACKAGEPATHFORCALENDER;
	private String            UName;
	private String            HId;
	private String            accoutIdcal;
	DownloadUserFileFromReload( Activity act, String filePath, Intent i, String UName, String HId, String accoutIdcal){
		this.act=act;
		this.downloadPath=filePath+"/XMLFile.zip";
		this.urlpath="http://"+Constant.WEBSERVER+"/android/demoproject";
		this.uptoString=filePath;
		this.i=i;
		this.UName=UName;
		this.HId=HId;
		this.accoutIdcal=accoutIdcal;
		infoappArrayList=new ArrayList<String> ();
		downloadappArrayList=new ArrayList<String> ();
		SharedPreferences settings = act.getSharedPreferences( "username", 0);
		username =settings.getString("user","xxx");
		PACKAGEPATHFORCALENDER= Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + Constant.algo( act.getPackageName());
	}
	@Override
	protected
    String doInBackground( String... params) {
		try {
			// opens a new url connection
			urlConnection = (HttpURLConnection ) new URL ( urlpath + params[0] + "/XMLFile.zip").openConnection();
			int fileLength = urlConnection.getContentLength();
			// open the file output stream for file writing
			if(!new File ( this.downloadPath).exists()){
				new File ( this.downloadPath.substring( 0, this.downloadPath.lastIndexOf( "/"))).mkdirs();
				new File ( this.downloadPath).createNewFile();
			}
			fileos = new BufferedOutputStream ( new FileOutputStream ( this.downloadPath));
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

		}catch (SocketException e){
			Log.e( " ", e.getMessage());
			return "SOCKET";
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
			return "SOCKET";
		}
		catch(IOException e) {
			Log.e( " ", e.getMessage());
			return "false";
		}
		catch(Exception e) {
			Log.e( " ", e.getMessage());
			return "false";
		}
		finally {
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
	protected void onProgressUpdate(String... values) {
		dialog.setProgress( Integer.parseInt( values[0]));
	}
	public void getName(String fileName)
	{
		try {
			File   directory = new File ( fileName);
			File[] fList     = directory.listFiles();
			for (File f : fList) {
				if (f.isDirectory()) {
					getName(f.getAbsolutePath());

				} else if (f.isFile()) {
					if (f.getAbsolutePath().contains("index1_E.html")) {
						if(!f.getAbsolutePath().contains("info.app")){
							infoappArrayList.add(f.getParent());
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println( "Exception " + e);
		}
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result.equals("true")){
			if(this.downloadPath.endsWith(".zip"))
				extractZipFiles(this.downloadPath);

			if(dialog!=null)
				dialog.dismiss();
			File Subscription = new File ( uptoString + "/REG.xml");
			if(Subscription.exists()){
				Subscription.delete();
			}
			if(new File ( uptoString + "/REGD.xml").exists())
				new File ( uptoString + "/REGD.xml").delete();
			if(new File ( uptoString + "/REG.xml").exists())
				new File ( uptoString + "/REG.xml").delete();
			for ( Iterator<ReloadValueStore> iterator = Reload.array.iterator() ; iterator.hasNext();) {
				ReloadValueStore rvs = (ReloadValueStore) iterator.next();
				new CreateCalendarXML( CoupanActivation.addDate1(rvs.Dt_ActDate), rvs.V_Board, rvs.V_Medium, rvs.V_Class, Environment.getExternalStorageDirectory().getAbsolutePath(), PACKAGEPATHFORCALENDER, act, Integer.parseInt( rvs.N_CalCounter), HId, UName, accoutIdcal);
				changeChapterFlag(rvs.toString());

				createSubscriptionFile( rvs.V_Board, rvs.V_Medium, rvs.V_Class, Integer.parseInt( rvs.N_CalCounter), CoupanActivation.addDate( rvs.Dt_ActDate), rvs.V_CouponNo, rvs.V_CouponRefNo, UName, HId);
			}
		}else if(result.equals("SOCKET")){
			new File ( this.downloadPath).delete();
			Toast.makeText( act, "Internet Connection Lost. Please Try Again.", Toast.LENGTH_LONG).show();
		}else{
			new File ( this.downloadPath).delete();
		}



		getName(uptoString);
		for (int i = 0; i < infoappArrayList.size(); i++) {
			String[] appname =infoappArrayList.get( i).split( username + "/");
			downloadappArrayList.add( "http://"+Constant.WEBSERVER+"/android/demoproject/eteach/assets/Content/Actual/"+appname[1].toString());
		}
		String names[] = new String[downloadappArrayList.size()];
		names=downloadappArrayList.toArray(names);
		new DownloadAppFile(act,infoappArrayList).execute(names);
		if(i!=null)
			this.act.startActivity(i);
		if(dialog!=null){
			dialog.dismiss();
		}

		if(result.equals("true")){
			File file =new File ( Constant.INTERNAL_SDCARD + "foundation.xml");
			if(file.exists())
			{
				file.delete();
			}
			
			String urldemo = "http://" + Constant.WEBSERVER + "/school_bag/sysfiles/mac.xml";
			//String urldemo="http://"+Constant.WEBSERVER+"/android/demoproject/XMLFile/mac.xml";
			
			new DownloadMacFile(Constant.INTERNAL_SDCARD+"mac.xml", urldemo, act,Mode.DOWNLOAD_MAC_MODE_Reload).execute("");
		}

	}

	private void createSubscriptionFile( String brd, String med, String std, int day, String date, String CouponNo, String courefNumberspo, String usernameforregreset, String hddidforreg) {

		File   Subscription = new File ( uptoString + "/REG.xml");
		String calDateNxt   = EXPDATE( date, day);
		if(Subscription.exists()){
			try {
				FileInputStream  fis           =new FileInputStream ( uptoString + "/REG.xml");
				FileOutputStream fos           =new FileOutputStream ( uptoString + "/REGD.xml");
				File             Subscription1 =new File ( uptoString + "/REGD.xml");
				new Rijndael();
				Rijndael.EDR(fis, fos, 2);
				new DeleteFile(uptoString+"/REG.xml");

				RandomAccessFile temp1 = new RandomAccessFile ( Subscription1, "rw");
				String           s     = "<item><ClassPath>" + brd + " - " + med + " - " + std.replace( "_", " ") + "</ClassPath><ActDate>" + date.substring( 4, 6) + "/" + date.substring( 2, 4) + "/" + date.substring( 0, 2) + "</ActDate><ExpDate>" + calDateNxt.substring( 4, 6) + "/" + calDateNxt.substring( 2, 4) + "/" + calDateNxt.substring( 0, 2) + "</ExpDate><CouponNo>" + CouponNo + "</CouponNo><CouRefNumber>" + courefNumberspo + "</CouRefNumber><reguser>" + usernameforregreset + "</reguser><reghddid>" + hddidforreg + "</reghddid></item></Reg>";
				temp1.seek(Subscription1.length()-"</Reg>".length());
				temp1.write(s.getBytes());
				temp1.close();


			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {

				File temp2 =new File ( uptoString + "/REGD.xml");
				temp2.createNewFile();
				RandomAccessFile temp1 = new RandomAccessFile ( temp2, "rw");

				String s = "<Reg><item><ClassPath>" + brd + " - " + med + " - " + std.replace( "_", " ") + "</ClassPath><ActDate>" + date.substring( 4, 6) + "/" + date.substring( 2, 4) + "/" + date.substring( 0, 2) + "</ActDate><ExpDate>" + calDateNxt.substring( 4, 6) + "/" + calDateNxt.substring( 2, 4) + "/" + calDateNxt.substring( 0, 2) + "</ExpDate><CouponNo>" + CouponNo + "</CouponNo><CouRefNumber>" + courefNumberspo + "</CouRefNumber><reguser>" + usernameforregreset + "</reguser><reghddid>" + hddidforreg + "</reghddid></item></Reg>";
				temp1.write(s.getBytes());
				temp1.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {
			FileInputStream  fis1 = new FileInputStream ( uptoString + "/REGD.xml");
			FileOutputStream fos1 = new FileOutputStream ( uptoString + "/REG.xml");
			new Rijndael();
			Rijndael.EDR(fis1, fos1, 1);
			new DeleteFile(uptoString+"/REGD.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	String EXPDATE( String dt, int j){

		SimpleDateFormat sdf = new SimpleDateFormat ( "yyMMdd");
		Calendar         c   = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(dt));
		} catch (ParseException e) {

			e.printStackTrace();
		}


		c.add( Calendar.DAY_OF_MONTH, j);


		dt = sdf.format(c.getTime());  
		return dt;
	}
	public static void writeFile( String i, String path, String fileName){
		try
		{
			File       gpxfile = new File ( path, fileName);
			FileWriter writer  = new FileWriter ( gpxfile);
			writer.append(i);
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	} 
	public static
    String readFile( String path, String fileName)
	{
		File file = new File ( path, fileName);

		//Read text from file
		StringBuilder text = new StringBuilder ();

		try {
			BufferedReader br = new BufferedReader ( new FileReader ( file));
			String         line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
		}
		catch (IOException e) {
			//You'll need to add proper error handling here
			return null;

		}
		return text.toString();
	}
	public void changeChapterFlag(String flow)
	{

		String                 keyname = null;
		DocumentBuilderFactory dbf     = DocumentBuilderFactory.newInstance();
		DocumentBuilder        db;
		Document               doc     = null;
		try {
			db = dbf.newDocumentBuilder();
			try {
				FileInputStream  fis =new FileInputStream ( uptoString + "/cbsechapter.xml");
				FileOutputStream fos =new FileOutputStream ( uptoString + "/cbsechapterD.xml");

				new Rijndael();
				Rijndael.EDR(fis, fos, 2);
				new DeleteFile(uptoString+"/cbsechapter.xml");
			}catch (Exception e) {
				e.printStackTrace();
			}
			File file = new File ( uptoString + "/cbsechapterD.xml");
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
							if(keyname.contains(flow))
							{
								itemchild.setTextContent("2");
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
				FileInputStream  fis =new FileInputStream ( uptoString + "/cbsechapterD.xml");
				FileOutputStream fos =new FileOutputStream ( uptoString + "/cbsechapter.xml");
				new Rijndael();
				Rijndael.EDR(fis, fos, 1);
				new DeleteFile(uptoString+"/cbsechapterD.xml");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.e( "Error", "" + e);
		}



	} 
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog=new ProgressDialog ( act);
		dialog.setCancelable(false);
		dialog.setMessage("Downloading file. Please wait..." );
		dialog.setProgressStyle( ProgressDialog.STYLE_HORIZONTAL);
		dialog.show();

	}

	private void extractZipFiles(String filename) {
		try {
			String destinationname =filename;
			destinationname =destinationname.substring(0,destinationname.lastIndexOf("/"));
			byte[]         buf            = new byte[1024];
			int            n;
			ZipInputStream zipinputstream = new ZipInputStream ( new FileInputStream ( filename));
			ZipEntry       zipentry       = zipinputstream.getNextEntry();
			while (zipentry != null) {
				String           entryName = zipentry.getName();
				FileOutputStream fileoutputstream;
				File             newFile   = new File ( entryName);
				String           directory = newFile.getParent();
				if (directory == null) {
					if (newFile.isDirectory())
						break;
				} else 
					new File ( destinationname + "/" + newFile).createNewFile();
				if(!zipentry.isDirectory()){
					System.out.println( "File to be extracted.....");
					fileoutputstream = new FileOutputStream ( destinationname + "/" + newFile);
					while ((n = zipinputstream.read(buf, 0, 1024)) > -1 ){
						fileoutputstream.write(buf, 0, n);
					}
					fileoutputstream.close();
				}
				zipinputstream.closeEntry();
				zipentry = zipinputstream.getNextEntry();

			}
			File f =new File ( filename);
			if(f.exists())   f.delete();
			zipinputstream.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e( "Exception", "" + e);
		}
	}
	public class DownloadAppFile extends AsyncTask<String, String, String>
	{

		private ProgressDialog dialog;
		private String         downloadPath1;
		ArrayList<String> Destinationpath;
		private Activity activity;
		DownloadAppFile( Activity activity, ArrayList<String> Destinationpath){
			this.activity=activity;
			this.Destinationpath=Destinationpath;
		}
		@Override
		protected
        String doInBackground( String... params) {
			for(int i=0; i<params.length;i++){
				String downloadpath =params[i];
				try {
					urlConnection = (HttpURLConnection ) new URL ( downloadpath + "/info.app").openConnection();
					int fileLength = urlConnection.getContentLength();
					// open the file output stream for file writing
					in = new BufferedInputStream ( urlConnection.getInputStream(), bufferSize);
					downloadPath1=Destinationpath.get(i)+"/info.app";
					if(!new File ( downloadPath1).exists()){
						new File ( downloadPath1.substring( 0, downloadPath1.lastIndexOf( "/"))).mkdirs();
						new File ( downloadPath1).createNewFile();
					}

					fileos = new BufferedOutputStream ( new FileOutputStream ( downloadPath1));
					// open the url input stream for reading the connection


					// write the file from the connection inputstream
					retVal = new byte[bufferSize];
					int length = 0;
					while((length = in.read(retVal)) > -1) {
						fileos.write(retVal, 0, length);
						publishProgress(""+(int) (numBytesDownloaded * 100 / fileLength));
						numBytesDownloaded += length;
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (ProtocolException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally {
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
			}
			return null;

		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog=new ProgressDialog ( activity);
			dialog.setMessage("Downloding Please Wait...");
			dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(true);
			dialog.show();
		}

	}
} 
