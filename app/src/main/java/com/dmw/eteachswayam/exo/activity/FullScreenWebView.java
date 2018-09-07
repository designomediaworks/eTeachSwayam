package com.dmw.eteachswayam.exo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.model.Constant;
import com.dmw.eteachswayam.exo.model.CreateCalendarXML;
import com.dmw.eteachswayam.exo.model.DeleteFile;
import com.dmw.eteachswayam.exo.model.LayoutXmlParser;
import com.dmw.eteachswayam.exo.model.Rijndael;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class FullScreenWebView extends Activity {
	private ProgressDialog dialog;
	WebView webView;
	private String epath;
	private String accountId;
	private String path;
	private String username;
	private String uptoString;
	private String uptoSubject;

	private String PACKAGEPATHFORCALENDER;
	private String hddIdforTab;

	@SuppressLint( "JavascriptInterface" )
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		webView=new WebView ( this);

		hddIdforTab=getUdid();
		Intent intent =getIntent();
		path=intent.getStringExtra("Path");
		if(intent.hasExtra("uptoSubject"))
			uptoSubject=intent.getStringExtra("uptoSubject");
		else uptoSubject=null;
		SharedPreferences settings1 = getApplicationContext().getSharedPreferences( "username", 0);
		accountId =settings1.getString("ACCOUNTID", "xxx");
		if(path!=null&& !path.equals("")){
			epath= "http://" + Constant.WEBSERVER + "/designo_pro/payment gateway/checkout2.php?msg=" + path + "&ida=" + accountId + "&idh=" + getUdid() + "&sys=Tab";
		}else{
			Toast.makeText( FullScreenWebView.this, "Please enter valid credentials" , Toast.LENGTH_LONG).show();
			epath="about:blank";
			finish();
		}


		username =settings1.getString("user","xxx"); 
		uptoString=Constant.ContentPath+accountId+"/"+username;
		clearBrowsercache();
		webView.clearCache(true);
	//	webView.getSettings().setPluginsEnabled(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setScrollbarFadingEnabled(true);
		webView.addJavascriptInterface(new MyJavaScriptInterface(this), "AndroidFunction");


		webView.setWebViewClient(new WebViewClient (){

			@Override
			public void onReceivedSslError ( WebView view, SslErrorHandler handler, SslError error){
				handler.proceed();
			}

			@Override
			public void onLoadResource( WebView view, String url) {

				super.onLoadResource(view, url);
			}
			@Override
			public void onPageFinished( WebView view, String url) {
				super.onPageFinished(view, url);
				view.clearCache(true);
				dialog.dismiss();
			}
		});  
		webView.loadUrl(epath);

		setContentView(webView);
		startLoader();
		setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


		PACKAGEPATHFORCALENDER= Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + Constant.algo( getPackageName());
	}
	private
    String getUdid() {

		final String macAddr, androidId;
		String       id = null;

		WifiManager wifiMan = (WifiManager )getApplicationContext().getSystemService( Context.WIFI_SERVICE);
		WifiInfo    wifiInf = wifiMan.getConnectionInfo();

		if(!wifiMan.isWifiEnabled())
		{
			wifiMan.setWifiEnabled(true);
			macAddr = wifiInf.getMacAddress();
			Log.e( "macAddr", "" + macAddr);
			androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
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
			androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
			Log.e( "androidId", "" + androidId);
			UUID deviceUuid = new UUID ( androidId.hashCode(), macAddr.hashCode());
			id=deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1);
			// text.setText(deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1));
			Log.e( "UUID", "" + id);
		}
		return id;
	}
	public class MyJavaScriptInterface {
		Context mContext;

		MyJavaScriptInterface(Context c) {
			mContext = c;
		}

		public void showToast(String toast){
			String[] arr         =toast.split( ":");
			String[] bordToClass =path.split( "-");
			new CreateCalendarXML ( addDate1( arr[3]), bordToClass[0], bordToClass[1], bordToClass[2], Environment.getExternalStorageDirectory().getAbsolutePath(), PACKAGEPATHFORCALENDER, FullScreenWebView.this, Integer.parseInt( arr[4]), hddIdforTab, username, accountId);
			changeChapterFlag(path.replace("-", "/"));
			createSubscriptionFile( bordToClass[0], bordToClass[1], bordToClass[2], Integer.parseInt( arr[4]), addDate( arr[3]), arr[1], "null", username, hddIdforTab);
			if(uptoSubject!=null){
				try {
					FileInputStream  fis =new FileInputStream ( uptoString + "/cbsechapter.xml");
					FileOutputStream fos =new FileOutputStream ( uptoString + "/cbsechapterD.xml");

					new Rijndael();
					Rijndael.EDR( fis, fos, 2);

					LayoutXmlParser parser =new LayoutXmlParser ( uptoString + "/cbsechapterD.xml");
					//			getDownloadChapter(uptoString+"/"+uptoSubject);
					Constant.chapterArrayList=parser.parseChapter(uptoSubject,1);

					new DeleteFile ( uptoString + "/cbsechapterD.xml");
					callReceiver("OK");
					finish();
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		public void callReceiver(String str){

			Intent intent = new Intent ();
			intent.setAction("com.manish.android.mybroadcast");
			intent.putExtra("Messsage",str);
			sendBroadcast(intent);
		}
		public void openAndroidDialog(String toast){
			AlertDialog.Builder myDialog
			= new AlertDialog.Builder( FullScreenWebView.this);
			myDialog.setTitle("DANGER!");
			myDialog.setMessage("You can do what you want!\n"+toast);
			myDialog.setPositiveButton("ON", null);
			myDialog.show();
		}

	}
	private void createSubscriptionFile( String brd, String med, String std, int day, String date, String coupon, String CouponRefNumber, String usernameforreg, String hddidTab) {

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
				String           s     = "<item><ClassPath>" + brd + " - " + med + " - " + std.replace( "_", " ") + "</ClassPath><ActDate>" + date.substring( 4, 6) + "/" + date.substring( 2, 4) + "/" + date.substring( 0, 2) + "</ActDate><ExpDate>" + calDateNxt.substring( 4, 6) + "/" + calDateNxt.substring( 2, 4) + "/" + calDateNxt.substring( 0, 2) + "</ExpDate><CouponNo>" + coupon + "</CouponNo><CouRefNumber>" + CouponRefNumber + "</CouRefNumber><reguser>" + usernameforreg + "</reguser><reghddid>" + hddidTab + "</reghddid></item></Reg>";
				temp1.seek(Subscription1.length()-"</Reg>".length());
				temp1.write(s.getBytes());
				temp1.close();


			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {

				File temp2 =new File ( uptoString + "/REGD.xml");
				temp2.createNewFile();
				RandomAccessFile temp1 = new RandomAccessFile ( temp2, "rw");

				String s = "<Reg><item><ClassPath>" + brd + " - " + med + " - " + std.replace( "_", " ") + "</ClassPath><ActDate>" + date.substring( 4, 6) + "/" + date.substring( 2, 4) + "/" + date.substring( 0, 2) + "</ActDate><ExpDate>" + calDateNxt.substring( 4, 6) + "/" + calDateNxt.substring( 2, 4) + "/" + calDateNxt.substring( 0, 2) + "</ExpDate><CouponNo>" + coupon + "</CouponNo><CouRefNumber>" + CouponRefNumber + "</CouRefNumber><reguser>" + usernameforreg + "</reguser><reghddid>" + hddidTab + "</reghddid></item></Reg>";
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
		c.add( Calendar.DATE, j);  // number of days to add
		dt = sdf.format(c.getTime());  
		return dt;
	}
	public void changeChapterFlag(String flow)
	{

		String keyname = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder        db;
		Document               doc = null;
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
				fis.close();
				fos.close();
			}catch (Exception e) {
				e.printStackTrace();
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			Log.e( "Error", "" + e);
		}



	} 
	public static
    String addDate1( String dt)
	{
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Calendar         c   = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(dt));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.add( Calendar.DATE, 0);  // number of days to add
		dt = new SimpleDateFormat ( "yy-MM-dd").format( c.getTime());  // dt is now the new date

		return dt;
	}
	public static
    String addDate( String dt)
	{
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Calendar         c   = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(dt));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.add( Calendar.DATE, 0);  // number of days to add
		dt = new SimpleDateFormat ( "yyMMdd").format( c.getTime());  // dt is now the new date

		return dt;
	}
	public void startLoader(){
		dialog=new ProgressDialog ( this);
		dialog.setCancelable(true);
		dialog.setMessage("Loading. Please wait...");
		dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
		dialog.show();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if( keyCode == KeyEvent.KEYCODE_MENU)
		{
			createDialog();
		}
		if( keyCode == KeyEvent.KEYCODE_BACK){

			createDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public  void createDialog()
	{
		final AlertDialog alertDialog = new AlertDialog.Builder( this).create();
		alertDialog.setTitle("eTeach");
		alertDialog.setMessage("Do you want to exit the payment gateway?");

		alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int which) {
				//				Intent intent = new Intent(FullScreenWebView.this, MainActivity.class);
				//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				webView.loadUrl("about:blank");
				//				startActivity(intent);
				finish(); 
			}
		});
		alertDialog.setButton("No", new DialogInterface.OnClickListener() {

			public void onClick( DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.setIcon( R.drawable.logo);
		alertDialog.show();

	} 
	private void clearBrowsercache()
	{
		File dir = getCacheDir();

		if(dir!= null && dir.isDirectory()){
			try{
				File[] children = dir.listFiles();
				if (children.length >0) {
					for (int i = 0; i < children.length; i++) {
						File[] temp = children[i].listFiles();
						for(int x = 0; x<temp.length; x++)
						{
							temp[x].delete();
						}
					}
				}
			}catch(Exception e)
			{
				Log.e( "Cache", "failed cache clean");
			}
		}
	} 
}
