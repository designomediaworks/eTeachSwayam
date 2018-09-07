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
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.model.Constant;
import com.dmw.eteachswayam.exo.model.DeleteFile;
import com.dmw.eteachswayam.exo.model.DownloadFileUpgrade;
import com.dmw.eteachswayam.exo.model.DownloadXmlFileForDemo;
import com.dmw.eteachswayam.exo.model.EncryptionDecryption;
import com.dmw.eteachswayam.exo.model.NanoHTTPD;
import com.dmw.eteachswayam.exo.model.PlayVedioFileConstant;
import com.dmw.eteachswayam.exo.model.RegBinClass;
import com.dmw.eteachswayam.exo.model.Rijndael;
import com.dmw.eteachswayam.exo.model.XML;
import com.dmw.eteachswayam.exo.model.XMLParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class PlayActualVedioFile extends Activity {

	private DrawerLayout          mDrawerLayout;
	private ListView              mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	public  ArrayList<Topic>      TopicNameArrayList;
	private CharSequence          mTitle;
	ArrayAdapter<Topic> TopicArrayAdapter;
	private LinearLayout layout;
	private VideoView    videoView;
	private String       path;
	Random random;
	private String    vedioPath_D;
	private String    pathDecrypted;
	private NanoHTTPD x;
	private int       port;
	ArrayList<RegBinClass> listBinClasses;
	private String uptostring;
	private String accountId;
	private String username;
	boolean checkuserforfileplay;
	private String PACKAGEPATHFORCALENDER;
	boolean rnee = true;
	private String uptoSubject;
	private String deviceDateString;
	private String disableDateString;
	private String uptoClass;
	private boolean calendarUpdated = false;
	private       String keyactualEncryption;
	private       String salt;
	private       String iv;
	public static String ListPreference;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playvedio);
		Intent intent =getIntent();
		path=intent.getStringExtra("path");
		uptostring=intent.getStringExtra("uptoString");
		uptoSubject=intent.getStringExtra("uptoSubject");
		keyactualEncryption=intent.getStringExtra("encryptionKey");
		salt=intent.getStringExtra("salt");
		iv=intent.getStringExtra("InitVector");
		setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		PlayActualVedioFile.this.setTitle("");

		SharedPreferences settings1 = getApplicationContext().getSharedPreferences( "username", 0);
		accountId =settings1.getString("ACCOUNTID", "xxx");
		username = settings1.getString("user","xxx");
		checkuserforfileplay=true;
		listBinClasses=new ArrayList<RegBinClass> ();

		String className = uptoSubject.substring( 0, uptoSubject.lastIndexOf( "/"));
		uptoClass = className.substring(0, className.lastIndexOf("/"));

		PACKAGEPATHFORCALENDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + Constant.algo( getPackageName());

		new Rijndael ();
		checkTime();
		try
		{
			//listBinClasses=SubscriptionDetail.pareseRegFile(uptostring);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}

		String hddiduser =getUdid();

		if(!listBinClasses.isEmpty())
		{
			for (int j = 0; j < listBinClasses.size(); j++) 
			{
				String usernameforregtab =listBinClasses.get( j).getuName();
				String hddidfortabinreg  =listBinClasses.get( j).getHhid();
				if(!usernameforregtab.equals("")){
					if(!usernameforregtab.equals(username))
					{
						checkuserforfileplay=false;
						break;
					}
					if(!hddidfortabinreg.equals(hddiduser))
					{
						checkuserforfileplay=false;
						break;
					}
				}
				else
				{
					Toast.makeText( PlayActualVedioFile.this, "Please reset the application", Toast.LENGTH_LONG).show();
					checkuserforfileplay=false;
					PlayActualVedioFile.this.finish();
					break;
				}
			}
		}
		else
		{
			checkuserforfileplay=false;
		}

		if(checkuserforfileplay)
		{
			chkDateExpired();
			checkStdActd();
			
			mDrawerLayout = (DrawerLayout ) findViewById( R.id.drawer_layout);
			mDrawerList = (ListView ) findViewById( R.id.left_drawer);
			
			if(rnee)
			{

				initComponent();

				TopicArrayAdapter=new ArrayAdapter<Topic> ( getApplicationContext(), android.R.layout.simple_list_item_1, TopicNameArrayList)
						{
					@Override
					public
                    View getView( int position, View convertView, ViewGroup parent)
					{
						View     view     =super.getView( position, convertView, parent);
						TextView textView =(TextView ) view.findViewById( android.R.id.text1);
						textView.setTextColor( Color.WHITE);
						textView.setText(TopicNameArrayList.get(position).getName());
						return textView;
					}
						};
						mDrawerList.setAdapter(TopicArrayAdapter);
						mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
						getActionBar().setDisplayHomeAsUpEnabled(true);
						getActionBar().setHomeButtonEnabled(true);
						mDrawerToggle = new ActionBarDrawerToggle ( this, mDrawerLayout, R.drawable.icon, R.string.app_name, R.string.app_name )
						{
							public void onDrawerClosed(View view)
							{
								invalidateOptionsMenu();
								layout.setGravity( Gravity.CENTER);
							}
							public void onDrawerOpened(View drawerView) {
								layout.setGravity( Gravity.RIGHT);
								invalidateOptionsMenu();
							}
						};
						mDrawerLayout.setDrawerListener(mDrawerToggle);

						if (savedInstanceState == null) 
						{
							new playVedioFile().execute("");
						}
			}
		}
		else
		{
			Toast.makeText( this, "Invalid user login!", Toast.LENGTH_LONG).show();
			rnee=false;
			return;
		}

	}
	public class playVedioFile extends AsyncTask<String, String, String>
	{

		private ProgressDialog progressDialog;

		@Override
		protected
        String doInBackground( String... params) {


			String path1  =path.substring( 0, path.lastIndexOf( "/"));
			String string =TopicNameArrayList.get( 0).getLink();
			string=string.substring(0, string.lastIndexOf("."));
			String encryptstring = string + "_E.mp4";
			String decyptString  = string + "_D.mp4";
			String vedioPath_E   = path1 + "/" + encryptstring;
			vedioPath_D= PlayVedioFileConstant.generatefoldername( random);
			pathDecrypted= Constant.libpath+"/"+vedioPath_D;
			String decString = pathDecrypted + "/" + decyptString;
			try 
			{
				if(!new File ( pathDecrypted).exists())
				{
					new File ( pathDecrypted).mkdirs();
				}
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
			new EncryptionDecryption ( vedioPath_E, decString , 1, keyactualEncryption, salt, iv);

			return decString;
		}
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);

			playVedio(result);

			new Thread ( new Runnable ()
			{
				@Override
				public void run() 
				{
					try 
					{
						Thread.sleep( 8000);
						DeleteFile(pathDecrypted);
					} 
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}).start();
			progressDialog.dismiss();
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog=new ProgressDialog ( PlayActualVedioFile.this);
			progressDialog.setMessage("Loading. Please wait...");
			progressDialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}


	}
	private void initComponent() 
	{
		ganerateId();
		registerEvent();
	}
	private void ganerateId() 
	{
		
		layout=(LinearLayout ) findViewById( R.id.layout);
		

		int                       width  = getResources().getDisplayMetrics().widthPixels/6;
		DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mDrawerList.getLayoutParams();
		params.width = width;
		mDrawerList.setLayoutParams(params); 
		getActionBar().setIcon(new ColorDrawable ( getResources().getColor( android.R.color.transparent)));
		random=new Random ();

		decryptTopic(path);

	}
	private void registerEvent() 
	{

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if( keyCode == KeyEvent.KEYCODE_BACK)
		{
			DeleteFile(pathDecrypted);
			deleteAllFile(Constant.libpath);
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void finish() 
	{
		if(pathDecrypted!=null)
		{
			DeleteFile(pathDecrypted);
			deleteAllFile(Constant.libpath);
		}
		super.finish();
	}
	@Override
	protected void onResume() 
	{
		super.onResume();
	}
	@Override
	protected void onPause() 
	{
		super.onPause();
	}
	private void decryptTopic(String topicXmlPath)
	{
		String decyrptedpath =topicXmlPath.substring( 0, topicXmlPath.lastIndexOf( "/"));
		decyrptedpath=decyrptedpath+"/topic_D.xml";
		new EncryptionDecryption(topicXmlPath, decyrptedpath, 1,keyactualEncryption,salt,iv);
		TopicNameArrayList=new ArrayList<Topic> ();
		//TopicNameArrayList=PlayVedioFileConstant.parseTopicXml(decyrptedpath);
		if(new File ( decyrptedpath).exists())
		{
			new File ( decyrptedpath).delete();
		}
	}



	private class SlideMenuClickListener implements ListView.OnItemClickListener
	{

		@Override
		public void onItemClick( AdapterView<?> parent, View view, int position, long id)
		{
			DeleteFile(pathDecrypted);

			new PlayVedio(position).execute("");
		}
	}
	private void DeleteFile(String fileName)
	{
		File directory = new File ( fileName);
		//make sure directory exists
		if(!directory.exists())
		{

		}
		else
		{
			try
			{
				delete(directory);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public static void delete(File file)
	{
		if(file.isDirectory())
		{
			if(file.list().length==0)
			{
				file.delete();
			}
			else
			{
				String files[] = file.list();
				for (String temp : files)
				{
					File fileDelete = new File ( file, temp);
					delete(fileDelete);
				}
				if(file.list().length==0)
				{
					file.delete();
				}
			}

		}
		else
		{
			file.delete();
		}
	}


	public class PlayVedio extends AsyncTask<String, String, String>
	{
		private WebView webView;
		int position;

		public PlayVedio(int position) {
			super();
			this.position = position;
		}

		private ProgressDialog dialog;
		private String         decryptedPath;
		private String         decString;

		@Override
		protected
        String doInBackground( String... params)
		{
			if(TopicNameArrayList.get(position).getLink().contains(".html"))
			{
				String path1         =path.substring( 0, path.lastIndexOf( "/"));
				String string        =TopicNameArrayList.get( position).getLink();
				String indexPath     =string.substring( 0, string.lastIndexOf( "/"));
				String encryptedPath = path1 + "/" + indexPath + "/index_E.html";
				decryptedPath=path1+"/"+indexPath+"/index.html";
				new EncryptionDecryption(encryptedPath, decryptedPath, 1,keyactualEncryption,salt,iv);
			}
			else
			{
				String path1  =path.substring( 0, path.lastIndexOf( "/"));
				String string =TopicNameArrayList.get( 0).getLink();
				string=string.substring(0, string.lastIndexOf("."));
				String encryptstring = string + "_E.mp4";
				String decyptString  = string + "_D.mp4";
				String vedioPath_E   = path1 + "/" + encryptstring;

				String vedioPath_D =PlayVedioFileConstant.generatefoldername( random);
				pathDecrypted=Constant.libpath+"/"+vedioPath_D;
				try 
				{
					if(!new File ( pathDecrypted).exists())
					{
						new File ( pathDecrypted).mkdirs();
					}
				} 
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				decString=pathDecrypted+"/"+decyptString;
				new EncryptionDecryption(vedioPath_E,decString , 1,keyactualEncryption,salt,iv);
			}
			return null;
		}


		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);

			if(TopicNameArrayList.get(position).getLink().contains(".html"))
			{
				videoView.setVisibility( View.GONE);
				webView=new WebView ( PlayActualVedioFile.this);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layout.removeAllViews();

				webView.setLayoutParams(layoutParams);
				layout.addView(webView);

				playVedio1(decryptedPath, webView);

				new Thread ( new Runnable ()
				{
					@Override
					public void run() {
						try 
						{
							Thread.sleep( 2000);
						} 
						catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						File indexDecrypted =new File ( decryptedPath);
						if(indexDecrypted.exists())
						{
							indexDecrypted.delete();
						}
					}
				}).start();
			}
			else
			{
				if(webView!=null)
				{
					webView.setVisibility( View.GONE);
					webView.loadUrl("about:blank");
				}
				videoView.setVisibility( View.VISIBLE);
				layout.removeAllViews();

				playVedio(decString);

				new Thread ( new Runnable ()
				{
					@Override
					public void run() 
					{
						try 
						{
							Thread.sleep( 8000);
							DeleteFile(pathDecrypted);
						} 
						catch(InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}).start();

			}
			mDrawerLayout.closeDrawer(mDrawerList);
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			dialog=new ProgressDialog ( PlayActualVedioFile.this);
			dialog.setMessage("Loading. Please wait...");
			dialog.setCancelable(false);
			dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
			dialog.show();
		}

	}


	@SuppressLint("NewApi")
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if(mDrawerToggle!=null)
		{
			mDrawerToggle.syncState();
		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(mDrawerToggle!=null)
		{
			mDrawerToggle.onConfigurationChanged(newConfig);
		}
	}
	private void playVedio(String path) {
		videoView=new VideoView ( PlayActualVedioFile.this);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		videoView.setLayoutParams(layoutParams);
		layout.addView(videoView);
		videoView.setVideoPath(path);

		videoView.setMediaController(new MediaController ( PlayActualVedioFile.this));
		videoView.requestFocus();
		videoView.start();
	} 

	private void playVedio1( String path, WebView webView1)
	{
		webView1.getSettings().setBuiltInZoomControls(false);
		webView1.getSettings().setSupportZoom(false);
		webView1.setWebChromeClient(new WebChromeClient ());
		webView1.getSettings().setJavaScriptEnabled(true);
		webView1.getSettings().setAllowFileAccess(true);
		webView1.setScrollBarStyle( View.SCROLLBARS_INSIDE_OVERLAY);
		webView1.getSettings().setLoadWithOverviewMode(true);
		webView1.getSettings().setUseWideViewPort(true);
		webView1.getSettings().setDefaultZoom( ZoomDensity.FAR);
		webView1.getSettings().setBuiltInZoomControls(true);
		webView1.getSettings().setPluginState( PluginState.ON);
		webView1.loadUrl("file:///"+path);
	} 
	private
    String getUdid() {

		final String macAddr, androidId;
		String       id = null;

		WifiManager wifiMan = (WifiManager ) getApplicationContext().getSystemService( Context.WIFI_SERVICE);
		WifiInfo    wifiInf = wifiMan.getConnectionInfo();

		if (!wifiMan.isWifiEnabled()) {
			wifiMan.setWifiEnabled(true);
			macAddr = wifiInf.getMacAddress();
			Log.e( "macAddr", "" + macAddr);
			androidId = ""
					+ android.provider.Settings.Secure.getString(
							getContentResolver(),
							android.provider.Settings.Secure.ANDROID_ID);
			Log.e( "androidId", "" + androidId);
			UUID deviceUuid = new UUID ( androidId.hashCode(), macAddr.hashCode());
			id = deviceUuid.toString().substring(
					deviceUuid.toString().lastIndexOf("-") + 1);
			// text.setText(deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1));
			Log.e( "UUID", "" + id);
			wifiMan.setWifiEnabled(false);

		} else {
			macAddr = wifiInf.getMacAddress();
			Log.e( "macAddr", "" + macAddr);
			androidId = ""
					+ android.provider.Settings.Secure.getString(
							getContentResolver(),
							android.provider.Settings.Secure.ANDROID_ID);
			Log.e( "androidId", "" + androidId);
			UUID deviceUuid = new UUID ( androidId.hashCode(), macAddr.hashCode());
			id = deviceUuid.toString().substring(
					deviceUuid.toString().lastIndexOf("-") + 1);
			// text.setText(deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1));
			Log.e( "UUID", "" + id);
		}
		return id;
	}

	public static
    String algo( String value){

		//CharSequence value[];
		String SentToSer ="";
		String GetFrmSer ="";
		int    count     = 0;

		value=value.toUpperCase();
		try{	
			for (int i = 0; i < value.length(); i++)
			{
				if (value.charAt(i) == '0') { SentToSer += '2'; GetFrmSer += '7'; } else if (value.charAt(i) == '1') { SentToSer += '8'; GetFrmSer += '4'; }
				else if (value.charAt(i) == '2') { SentToSer += '5'; GetFrmSer += '8'; } else if (value.charAt(i) == '3') { SentToSer += '9'; GetFrmSer += '1'; }
				else if (value.charAt(i) == '4') { SentToSer += '0'; GetFrmSer += '9'; } else if (value.charAt(i) == '5') { SentToSer += '3'; GetFrmSer += '2'; }
				else if (value.charAt(i) == '6') { SentToSer += '7'; GetFrmSer += '0'; } else if (value.charAt(i) == '7') { SentToSer += '1'; GetFrmSer += '3'; }
				else if (value.charAt(i) == '8') { SentToSer += '6'; GetFrmSer += '5'; } else if (value.charAt(i) == '9') { SentToSer += '4'; GetFrmSer += '6'; }

				else if (value.charAt(i) == 'A') { SentToSer += 'S'; GetFrmSer += 'K'; } else if (value.charAt(i) == 'B') { SentToSer += 'G'; GetFrmSer += 'E'; }
				else if (value.charAt(i) == 'C') { SentToSer += 'I'; GetFrmSer += 'T'; } else if (value.charAt(i) == 'D') { SentToSer += 'M'; GetFrmSer += 'F'; }
				else if (value.charAt(i) == 'E') { SentToSer += 'L'; GetFrmSer += 'B'; } else if (value.charAt(i) == 'F') { SentToSer += 'R'; GetFrmSer += 'Q'; }
				else if (value.charAt(i) == 'G') { SentToSer += 'U'; GetFrmSer += 'L'; } else if (value.charAt(i) == 'H') { SentToSer += 'A'; GetFrmSer += 'C'; }
				else if (value.charAt(i) == 'I') { SentToSer += 'P'; GetFrmSer += 'N'; } else if (value.charAt(i) == 'J') { SentToSer += 'N'; GetFrmSer += 'Y'; }
				else if (value.charAt(i) == 'K') { SentToSer += 'W'; GetFrmSer += 'I'; } else if (value.charAt(i) == 'L') { SentToSer += 'Y'; GetFrmSer += 'R'; }
				else if (value.charAt(i) == 'M') { SentToSer += 'B'; GetFrmSer += 'H'; } else if (value.charAt(i) == 'N') { SentToSer += 'K'; GetFrmSer += 'J'; }
				else if (value.charAt(i) == 'O') { SentToSer += 'Z'; GetFrmSer += 'X'; } else if (value.charAt(i) == 'P') { SentToSer += 'C'; GetFrmSer += 'Z'; }
				else if (value.charAt(i) == 'Q') { SentToSer += 'F'; GetFrmSer += 'A'; } else if (value.charAt(i) == 'R') { SentToSer += 'D'; GetFrmSer += 'V'; }
				else if (value.charAt(i) == 'S') { SentToSer += 'J'; GetFrmSer += 'O'; } else if (value.charAt(i) == 'T') { SentToSer += 'Q'; GetFrmSer += 'M'; }
				else if (value.charAt(i) == 'U') { SentToSer += 'V'; GetFrmSer += 'S'; } else if (value.charAt(i) == 'V') { SentToSer += 'H'; GetFrmSer += 'P'; }
				else if (value.charAt(i) == 'W') { SentToSer += 'E'; GetFrmSer += 'D'; } else if (value.charAt(i) == 'X') { SentToSer += 'O'; GetFrmSer += 'U'; }
				else if (value.charAt(i) == 'Y') { SentToSer += 'X'; GetFrmSer += 'G'; } else if (value.charAt(i) == 'Z') { SentToSer += 'T'; GetFrmSer += 'W'; }
				else if (value.charAt(i) == '_') { SentToSer += '_'; GetFrmSer += '_'; } else  { SentToSer += '-'; GetFrmSer += '-'; } 


				count++;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e( "Conversion Error", "Error encountered while converting.....");
		}
		int SentLen = SentToSer.length(); int GetLen = GetFrmSer.length();
		//         String fullMsg = Cls_Global_Var.AccountId + " " + SentToSer;
		//         label4.Text = "Send eteach ACT " + fullMsg + " to 57333 \nAnd press 'Activate' button if you have recieved activation key."; 
		return SentToSer;
	}
	private void chkDateExpired() 
	{

		try {

			if (!new File ( PACKAGEPATHFORCALENDER + "/" + algo( accountId + "/" + username + "/" + uptoClass) + ".xml").exists())
			{
				Toast.makeText( PlayActualVedioFile.this, "Please reset the application", Toast.LENGTH_LONG).show();
				rnee=false;
				finish();
			}	

			FileInputStream  fis = new FileInputStream ( PACKAGEPATHFORCALENDER + "/" + algo( accountId + "/" + username + "/" + uptoClass) + ".xml");
			FileOutputStream fos = new FileOutputStream ( PACKAGEPATHFORCALENDER + "/" + algo( accountId + "/" + username + "/" + uptoClass) + "D.xml");

			new Rijndael();
			Rijndael.EDR(fis, fos, 2);

			SimpleDateFormat sdf = new SimpleDateFormat ( "yyyyMMdd");
			deviceDateString = sdf.format(new Date ());
			disableDateString = deviceDateString;
			deviceDateString = deviceDateString.substring(2, 4) + "-"
					+ deviceDateString.substring(4, 6) + "-"
					+ deviceDateString.substring(6, deviceDateString.length());

			// deviceDateString
			XML.searchContent( PACKAGEPATHFORCALENDER + "/" + algo( accountId + "/" + username + "/" + uptoClass) + "D.xml", deviceDateString);

			new DeleteFile ( PACKAGEPATHFORCALENDER + "/" + algo( accountId + "/" + username + "/" + uptoClass) + "D.xml");

			if (!XML.dateFound) {
				rnee = false;
				Toast.makeText( this, "Product Expired!", Toast.LENGTH_SHORT)
				.show();
				Intent i = new Intent ( PlayActualVedioFile.this, Renual.class);
				Log.e( "FilluptoSubject", uptoSubject);

				i.putExtra("uptoSubject", uptoSubject);
				i.putExtra("uptoString", uptostring);
				startActivity(i);
				this.finish();
			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			finish();
		}
	}

	private String AccId;
	private String calAccountId;
	private String DeviceHddId;
	private String className;
	private String hddidfortabtocheck;
	private String usenamefortabtocheck;
	private void checkStdActd() {
		// TODO Auto-generated method stub
		try {

			String tabidUserforchecking =getUdid();

			FileInputStream  fis = new FileInputStream ( uptostring + "/mac.xml");
			FileOutputStream fos = new FileOutputStream ( uptostring + "/macD.xml");
			new Rijndael();
			Rijndael.EDR(fis, fos, 2);

			new XMLParser ( uptostring + "/macD.xml", "User", "AcountId", "HddId");
			AccId = XMLParser.child1Value;
			DeviceHddId = XMLParser.child2Value;
			new XMLParser(uptostring + "/macD.xml", "User", "UserName", "HddId");
			username = XMLParser.child1Value;
			new DeleteFile(uptostring + "/macD.xml");

			FileInputStream  fis1 = new FileInputStream ( PACKAGEPATHFORCALENDER + "/" + algo( AccId + "/" + username + "/" + uptoClass) + ".xml");
			FileOutputStream fos1 = new FileOutputStream ( PACKAGEPATHFORCALENDER + "/" + algo( AccId + "/" + username + "/" + uptoClass) + "D.xml");
			new Rijndael();
			Rijndael.EDR(fis1, fos1, 2);
			new DeleteFile(PACKAGEPATHFORCALENDER + "/"+ algo(AccId + "/" + username + "/" + uptoClass) + ".xml");

			new XMLParser(PACKAGEPATHFORCALENDER + "/"+ algo(AccId + "/" + username + "/" + uptoClass) + "D.xml","Date", "CardNo", "ClassName");
			calAccountId = XMLParser.child1Value;
			className = XMLParser.child2Value;


			new XMLParser(PACKAGEPATHFORCALENDER + "/"+ algo(AccId + "/" + username + "/" + uptoClass) + "D.xml","Date", "HDDID", "UName");
			hddidfortabtocheck = XMLParser.child1Value;
			usenamefortabtocheck = XMLParser.child2Value;

			FileInputStream  fis11 = new FileInputStream ( PACKAGEPATHFORCALENDER + "/" + algo( AccId + "/" + username + "/" + uptoClass) + "D.xml");
			FileOutputStream fos11 = new FileOutputStream ( PACKAGEPATHFORCALENDER + "/" + algo( AccId + "/" + username + "/" + uptoClass) + ".xml");
			new Rijndael();
			Rijndael.EDR(fis11, fos11, 1);

			new DeleteFile(PACKAGEPATHFORCALENDER + "/"+ algo(AccId + "/" + username + "/" + uptoClass) + "D.xml");

			//String Pathname = uptoSubject;
			String Pathclass = uptoSubject.substring( 0, uptoSubject.lastIndexOf( "/"));
			String classPath = Pathclass.substring( 0, Pathclass.lastIndexOf( "/"));
			if (!className.equals(classPath)) {
				Toast.makeText( this, "Sorry, please reset the application",
                                Toast.LENGTH_SHORT).show();
				rnee=false;
				finish();
			}
			if(usenamefortabtocheck.equals("")){
				Toast.makeText( this, "Please reset the application", Toast.LENGTH_SHORT)
				.show();
				rnee=false;
				finish();
			}
			if(!usenamefortabtocheck.equals(username))
			{
				Toast.makeText( this, "Invalid User Login!", Toast.LENGTH_SHORT)
				.show();
				rnee=false;
				finish();
			}
			if(!hddidfortabtocheck.equals(tabidUserforchecking))
			{
				Toast.makeText( this, "Invalid User Login!", Toast.LENGTH_SHORT)
				.show();
				rnee=false;
				finish();
			}
			if (!AccId.equals(calAccountId)) {
				Toast.makeText( this, "Invalid User Login!", Toast.LENGTH_SHORT)
				.show();
				rnee=false;
				finish();
			} else if (!DeviceHddId.equals(getUdid())) {
				Toast.makeText( this, "Device Changed!", Toast.LENGTH_SHORT)
				.show();
				rnee=false;
				finish();
			}else if(hddidfortabtocheck.equals(getUdid()))
			{
				if(DeviceHddId.equals(getUdid())) {
					if(usenamefortabtocheck.equals(username)){
						SimpleDateFormat sdf = new SimpleDateFormat ( "yyyyMMdd");
						deviceDateString = sdf.format(new Date ());
						disableDateString = deviceDateString;
						deviceDateString = deviceDateString.substring(2, 4)
								+ "-"
								+ deviceDateString.substring(4, 6)
								+ "-"
								+ deviceDateString.substring(6,
										deviceDateString.length());
						// Log.e("vallll",MainActivity.serverdate.substring(2,MainActivity.serverdate.length()));
						if (isDataEnable() && HomeActivity.serverdate != null) {
							updateCalendarOnline(HomeActivity.serverdate.substring(2,
									HomeActivity.serverdate.length()),
									PACKAGEPATHFORCALENDER + "/" + algo(uptoClass), 1);
						} else
							updateCalendarOnline(deviceDateString,
									PACKAGEPATHFORCALENDER
									+ "/"
									+ algo(AccId + "/" + username + "/"
											+ uptoClass), 2);
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.finish();
		}
	}

	private void updateCalendarOnline( String deviceDateString2, String string, int mode) {
		// TODO Auto-generated method stub

		try {
			FileInputStream fis1 = new FileInputStream ( PACKAGEPATHFORCALENDER
                                                         + "/" + algo(AccId + "/" + username + "/" + uptoClass)
                                                         + ".xml");
			FileOutputStream fos1 = new FileOutputStream ( PACKAGEPATHFORCALENDER
                                                           + "/" + algo(AccId + "/" + username + "/" + uptoClass)
                                                           + "D.xml");
			new Rijndael();
			Rijndael.EDR(fis1, fos1, 2);
			new DeleteFile(PACKAGEPATHFORCALENDER + "/"
					+ algo(AccId + "/" + username + "/" + uptoClass) + ".xml");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!calendarUpdated) {
			XML.changeContent(PACKAGEPATHFORCALENDER + "/"
					+ algo(AccId + "/" + username + "/" + uptoClass) + "D.xml",
					deviceDateString);
			if (mode == 1)
				XML.disablePrevDates(PACKAGEPATHFORCALENDER + "/"
						+ algo(AccId + "/" + username + "/" + uptoClass)
						+ "D.xml", disableDateString);
		}

		if (XML.counterComplete) {
			Toast.makeText( this,
                            "You have exceeded the maximum usage for today.",
                            Toast.LENGTH_SHORT).show();
			finish();
		}
		calendarUpdated = true;

		try {
			FileInputStream fis1 = new FileInputStream ( PACKAGEPATHFORCALENDER
                                                         + "/" + algo(AccId + "/" + username + "/" + uptoClass)
                                                         + "D.xml");
			FileOutputStream fos1 = new FileOutputStream ( PACKAGEPATHFORCALENDER
                                                           + "/" + algo(AccId + "/" + username + "/" + uptoClass)
                                                           + ".xml");
			new Rijndael();
			Rijndael.EDR(fis1, fos1, 1);
			new DeleteFile(PACKAGEPATHFORCALENDER + "/"
					+ algo(AccId + "/" + username + "/" + uptoClass) + "D.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		}
	}
	public boolean isDataEnable() {
		ConnectivityManager conMgr;
		conMgr = (ConnectivityManager ) getSystemService( Context.CONNECTIVITY_SERVICE);
		// ARE WE CONNECTED TO THE NET
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			Log.v( "Internet", "Internet Connection Not Present");
			return false;
		}
	}
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater menuInflater = getMenuInflater();
		if(Constant.ScreenSize<=5.6f)
		{
			menuInflater.inflate(R.layout.smallscreenuimenuhome, menu);
		}
		else{
			menuInflater.inflate(R.layout.homemenu, menu);
		} 

		String str= (String) menu.findItem(R.id.logoutmenunew).getTitle();
		menu.findItem(R.id.logoutmenunew).setTitle(str+" "+username);
		return true;
	} */
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (mDrawerToggle.onOptionsItemSelected(item)) {
//			return true;
//		}
//		switch (item.getItemId()) {
//		case R.id.action_settings:
//
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
/*	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.aboutmenu).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	} */
	public void deleteAllFile(String fileName)
	{
		try 
		{
			File   directory = new File ( fileName);
			File[] fList     = directory.listFiles();
			for (File f : fList)
			{
				if (f.isDirectory())
				{
					deleteAllFile(f.getAbsolutePath());

				} 
				else if (f.isFile()) 
				{
					if (f.getAbsolutePath().contains(".mp4")) 
					{
						File file =new File ( f.getAbsolutePath());
						if(file.exists())
						{
							String folderpath =f.getAbsolutePath();
							String stringpath =folderpath.substring( 0, folderpath.lastIndexOf( "/"));
							DeleteFile(stringpath);
						}

					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println( "Exception " + e);
		}
	}
	private void checkTime() {
		new AsyncTask<String, String, String> () {

			private String baa;

			@Override
			protected
            String doInBackground( String... params) {
				try {
					String date = new SimpleDateFormat ( "dd_MM_yyyy HH:mm:ss aa")
					.format(new Date ());
					// baa=date.replace(",", "_");
					baa = date.replace(":", "-");
					String val = path.substring( 0, path.lastIndexOf( "/"));

					File f   = new File ( val + "/lastAccess" + baa + ".app");
					File b[] = f.getParentFile().listFiles();
					for (File file : b) {
						if (file.getAbsolutePath().contains("lastAccess")) {
							file.delete();
							break;
						}

					}
					Log.e( "valueeee", "****************" + f.getAbsolutePath()
                                       + "****************");
					if (!f.exists()) {
						// f.delete();
						f.getParentFile().mkdirs();
						f.createNewFile();
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

		}.execute("");
	}

	private void createSharePreferanceforLoginVisibleInvisible() {

		SharedPreferences        preferences           = PreferenceManager.getDefaultSharedPreferences( PlayActualVedioFile.this);
		SharedPreferences.Editor editor                = preferences.edit();
		String                   sharedPreferencesdate = preferences.getString( "StatusTrue", "");
		if(sharedPreferencesdate.equals("isLoginTrue"))
		{
			editor = preferences.edit();
			editor.putString("StatusTrue", "isLoginFalse");
			editor.commit();
		}

	}
	@SuppressWarnings("deprecation")
	public  void createDialogfordemo()
	{
		final AlertDialog alertDialog = new AlertDialog.Builder( new ContextThemeWrapper ( PlayActualVedioFile.this, R.style.MyDialogTheme)).create();
		alertDialog.setTitle("eTeach");
		alertDialog.setMessage("Do you want to restore this application for demo?");

		alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int which) {
				String urldemo = "http://" + Constant.WEBSERVER + "/android/demoproject/XMLFile/XMLFile.zip";
				new DownloadXmlFileForDemo ( urldemo, Constant.INTERNAL_SDCARD + "1234ABCD/eteach/XMLFile.zip", PlayActualVedioFile.this).execute( "");
			}
		});
		alertDialog.setButton("No", new DialogInterface.OnClickListener() {

			public void onClick( DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.setIcon(R.drawable.logo);
		alertDialog.show();
	} 
	@SuppressWarnings("deprecation")
	public void alertDialogUpdate()
	{
		final AlertDialog ald1 = new AlertDialog.Builder( new ContextThemeWrapper ( PlayActualVedioFile.this, R.style.MyDialogTheme)).create();
		ald1.setTitle("eTeach");
		ald1.setIcon(R.drawable.logo);
		ald1.setMessage("Do you want to update?");

		ald1.setButton2("No", new DialogInterface.OnClickListener() {

			public void onClick( DialogInterface dialog, int which) {

				ald1.dismiss();
			}
		});
		ald1.setButton("Yes",new DialogInterface.OnClickListener() {

			public void onClick( DialogInterface dialog, int which) {

				
				new DownloadFileUpgrade ( PlayActualVedioFile.this, "http://" + Constant.WEBSERVER + "/android/demoproject/upgrade/upgradeApp.xml", Constant.INTERNAL_SDCARD + "/upgradeApp.xml", 1);

			}
		});
		ald1.show();
	} 

}
