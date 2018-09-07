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
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.model.Constant;
import com.dmw.eteachswayam.exo.model.DownloadFileUpgrade;
import com.dmw.eteachswayam.exo.model.DownloadXmlFileForDemo;
import com.dmw.eteachswayam.exo.model.EncryptionDecryption;
import com.dmw.eteachswayam.exo.model.PlayVedioFileConstant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class PlayDemoVedioFile extends Activity {

	private DrawerLayout          mDrawerLayout;
	private ListView              mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	public  ArrayList <Topic>     TopicNameArrayList;
	private CharSequence          mTitle;
	ArrayAdapter<Topic> TopicArrayAdapter;
	private LinearLayout layout;
	private VideoView    videoView;
	private String       path;
	Random random;
	private       String pathDecrypted;
	private       String keyEncryption;
	private       String salt;
	private       String iv;
	private       String accountId;
	private       String username;
	public static String ListPreference;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView( R.layout.playvedio);
		setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		Intent intent =getIntent();
		path=intent.getStringExtra("path");
		keyEncryption=intent.getStringExtra("encryptionKey");
		salt=intent.getStringExtra("salt");
		iv=intent.getStringExtra("InitVector");

		SharedPreferences settings1 = getApplicationContext().getSharedPreferences( "username", 0);
		accountId =settings1.getString("ACCOUNTID", "xxx");
		username = settings1.getString("user","xxx");
		
		PlayDemoVedioFile.this.setTitle("");
		
		initComponent();

	}

	private void initComponent() 
	{
		checkTime();
		ganerateId();
		registerEvent();
	}
	private void ganerateId() 
	{

		mDrawerLayout = (DrawerLayout ) findViewById( R.id.drawer_layout);
		layout=(LinearLayout ) findViewById( R.id.layout);
		mDrawerList = (ListView ) findViewById( R.id.left_drawer);

		int                       width  = getResources().getDisplayMetrics().widthPixels/6;
		DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mDrawerList.getLayoutParams();
		params.width = width;
		mDrawerList.setLayoutParams(params); 
		getActionBar().setIcon(new ColorDrawable ( getResources().getColor( android.R.color.transparent)));
		random=new Random ();

		decryptTopic(path);

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

				new FilePlay().execute("");




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
		DeleteFile(pathDecrypted);
		deleteAllFile( Constant.libpath);
		super.finish();
	}

	private void decryptTopic(String topicXmlPath)
	{
		String decyrptedpath =topicXmlPath.substring( 0, topicXmlPath.lastIndexOf( "/"));
		decyrptedpath=decyrptedpath+"/topic_D.xml";
		new EncryptionDecryption ( topicXmlPath, decyrptedpath, 1, keyEncryption, salt, iv);
		TopicNameArrayList=new ArrayList<Topic> ();
		TopicNameArrayList= PlayVedioFileConstant.parseTopicXml( decyrptedpath);
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

	public class FilePlay extends AsyncTask<String, String, String>
	{
		private ProgressDialog progressDialog;

		@Override
		protected
        String doInBackground( String... params)
		{
			String uptosubjectPath =path.substring( 0, path.lastIndexOf( "/"));
			String getLink         =TopicNameArrayList.get( 0).getLink();
			getLink=getLink.substring(0, getLink.lastIndexOf("."));
			String encryptedString = getLink + "_E.mp4";
			String decryptedLink   = getLink + "_D.mp4";

			String vedioPath_D =PlayVedioFileConstant.generatefoldername( random);
			pathDecrypted=Constant.libpath+"/"+vedioPath_D;

			String encString = uptosubjectPath + "/" + encryptedString;
			String decString = pathDecrypted + "/" + decryptedLink;
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

			new EncryptionDecryption(encString, decString, 1,keyEncryption,salt,iv);

			return decString;
		}
		@Override
		protected void onPostExecute(String result) {
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
			progressDialog=new ProgressDialog ( PlayDemoVedioFile.this);
			progressDialog.setMessage("Loading. Please wait...");
			progressDialog.setCancelable(false);
			progressDialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
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
				new EncryptionDecryption(encryptedPath, decryptedPath, 1,keyEncryption,salt,iv);
			}
			else
			{
				String uptosubjectPath =path.substring( 0, path.lastIndexOf( "/"));
				String getLink         =TopicNameArrayList.get( 0).getLink();
				getLink=getLink.substring(0, getLink.lastIndexOf("."));
				String encryptedString = getLink + "_E.mp4";
				String decryptedLink   = getLink + "_D.mp4";

				String vedioPath_D =PlayVedioFileConstant.generatefoldername( random);
				pathDecrypted=Constant.libpath+"/"+vedioPath_D;

				String encString = uptosubjectPath + "/" + encryptedString;
				decString=pathDecrypted+"/"+decryptedLink;
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

				new EncryptionDecryption(encString, decString, 1,keyEncryption,salt,iv);
			}

			return null;
		}

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			dialog=new ProgressDialog ( PlayDemoVedioFile.this);
			dialog.setMessage("Loading. Please wait...");
			dialog.setCancelable(false);
			dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
			dialog.show();
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if(TopicNameArrayList.get(position).getLink().contains(".html"))
			{
				videoView.setVisibility( View.GONE);
				webView=new WebView ( PlayDemoVedioFile.this);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layout.removeAllViews();
				webView.setLayoutParams(layoutParams);
				layout.addView(webView);

				playVedio1(decryptedPath, webView);

				new Thread ( new Runnable ()
				{
					@Override
					public void run() 
					{
						try 
						{
							Thread.sleep( 2000);
							DeleteFile(pathDecrypted);
						} 
						catch (Exception e)
						{
							e.printStackTrace();
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
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}).start();
			}
			mDrawerLayout.closeDrawer(mDrawerList);
			dialog.dismiss();
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
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	private void playVedio(String path) {
		videoView=new VideoView ( PlayDemoVedioFile.this);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		videoView.setLayoutParams(layoutParams);
		layout.addView(videoView);
		videoView.setVideoPath(path);

		videoView.setMediaController(new MediaController ( PlayDemoVedioFile.this));
		videoView.requestFocus();
		videoView.start();
	} 

	private void playVedio1( String path, WebView webView)
	{
		WebSettings webSettings = webView.getSettings();
		webSettings.setBuiltInZoomControls(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.setScrollBarStyle( WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setScrollbarFadingEnabled(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setPluginState( PluginState.ON);
		webView.setWebChromeClient(new WebChromeClient ());
		webView.loadUrl("file:///"+path); 
	}
	
/*	@Override
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
	} 


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.aboutmenu).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	} 
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (mDrawerToggle.onOptionsItemSelected(item)) 
		{
			return true;
		}
		
		switch (item.getItemId()) {
		case R.id.abouteduslidenew:
			Intent aboutIntent =new Intent(PlayDemoVedioFile.this,LoginAbout.class);
			//aboutIntent.putExtra("islogin", "true");
			startActivity(aboutIntent);
			break;
		case R.id.productFeature:
			if(Constant.ScreenSize<=5.6f)
			{
				Intent productFeatureIntent=new Intent(PlayDemoVedioFile.this,ProductFeature.class);
				startActivity(productFeatureIntent);
			}
			break;
		case R.id.contenttrasfer:
			if(username.equals("eteach")){
				com.contenttransfer.SettingInfo.checkSizeofbothLocation(com.contenttransfer.SettingInfo.INTERNAL_PATH_FOR_COPY_CONTENT+"Demo",MainActivity.sdcardPath+"/assets/demo");
			}else{
				com.contenttransfer.SettingInfo.checkSizeofbothLocation(com.contenttransfer.SettingInfo.INTERNAL_PATH_FOR_COPY_CONTENT+accountId+"/"+username,MainActivity.sdcardPath+"/assets/Content");
			}
			if(com.contenttransfer.SettingInfo.ischeckExternalMediaAvailable){
				if(com.contenttransfer.SettingInfo.checkSizeforContentTransfer){
					com.contenttransfer.SettingInfo.openCopyTrasferActivity(PlayDemoVedioFile.this,"com.contenttransfer.MoveContent",username, accountId);
				}
				else{
					Toast.makeText(PlayDemoVedioFile.this, "Content already available", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(PlayDemoVedioFile.this, "SDcard not available", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.homehelp:
			//			Intent home =new Intent(MainActivity.this,TransperantClass.class);
			//			startActivity(home);
			if(!(Constant.ScreenSize<=5.6f)) 
			{

				//showDemoAgain();
			}
			break;
		case R.id.freememorymenu:
			if(MainActivity.ischeckValidUser){
				Intent freeMemory =new Intent(PlayDemoVedioFile.this,FreeMemory.class);
				startActivity(freeMemory);
			}else{
				Toast.makeText(PlayDemoVedioFile.this, "Invalid user login!", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.termsandconditionnew:
			Intent termIntent=new Intent(PlayDemoVedioFile.this,Term.class);
			startActivity(termIntent);
			break;
		case R.id.forum:
			Intent forum =new Intent(PlayDemoVedioFile.this,DiscussionForem.class);
			//aboutIntent.putExtra("islogin", "true");
			startActivity(forum);
			break;
		case R.id.supportmenunew:
			Intent intent =new Intent(PlayDemoVedioFile.this, Support.class);
			startActivity(intent);
			break;
		case R.id.feedbackmenunew:
			if(isDataEnable()){
				Intent intent1 =new Intent(PlayDemoVedioFile.this, MainActivityImage.class);
				startActivity(intent1);
			}else{
				Toast.makeText(PlayDemoVedioFile.this, "Internet connection is not present", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.updatenew:
			if(MainActivity.ischeckValidUser){
				if(isDataEnable())
				{
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());	
					ListPreference = prefs.getString("listPref", "nr1");
					if(ListPreference.equals("1")||ListPreference.equals("2"))
					{
						alertDialogUpdate();
					}else
						Toast.makeText(PlayDemoVedioFile.this, "Please set correct path in the settings", Toast.LENGTH_LONG).show();

				}
				else{
					Toast.makeText(PlayDemoVedioFile.this, "Your internet connection is not available.", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(PlayDemoVedioFile.this, "Invalid user login!", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.editprofilenew:

			if(!username.equals("eteach")){
				if(MainActivity.ischeckValidUser){
					if(isDataEnable()){
						Intent intent1 =new Intent(PlayDemoVedioFile.this, EditProfileActivity.class);
						startActivity(intent1);
					}
					else{
						Toast.makeText(PlayDemoVedioFile.this, "Your internet connection is not available.", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(PlayDemoVedioFile.this, "Invalid user login!", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(PlayDemoVedioFile.this, "This is a demo account.\n Kindly signup first to Edit Profile.", Toast.LENGTH_LONG).show();
			}

			break;

		case R.id.setpathnew:
			Intent settingsActivity = new Intent(getBaseContext(),SettingUi.class);
			//myWebView.loadUrl("about:blank");
			settingsActivity.putExtra("requestCode", "60");
			startActivity(settingsActivity);
			break;

		case R.id.subscriptionDetailsmenu:

			if(!username.equals("eteach")){

				if(MainActivity.ischeckValidUser){
				String	uptoString=Constant.INTERNAL_SDCARD+"/"+accountId+"/"+username;
					if(new File(uptoString+"/REG.xml").exists()){
						Intent i = new Intent(PlayDemoVedioFile.this,SubscriptionDetail.class);
						i.putExtra("uptoString", uptoString);
						startActivity(i); 
					}
					else{
						Toast.makeText(getApplicationContext(), "Please activate at least one standard!", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(PlayDemoVedioFile.this, "Invalid user login!", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(PlayDemoVedioFile.this, "This is a demo account.\n Kindly signup first to view subscription details.", Toast.LENGTH_LONG).show();
			}

			break;



		case R.id.resetapplicationnew:
			if(isDataEnable())
			{
				if(username.equals("eteach"))
				{
					createDialogfordemo();
				}
				if(!username.equals("eteach"))
				{
					final AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(PlayDemoVedioFile.this, R.style.popup_theme)).create();
					alertDialog.setTitle("eTeach");
					alertDialog.setMessage("Do you want to restore this application?");

					alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PlayDemoVedioFile.this, R.style.popup_theme));
							LayoutInflater li=LayoutInflater.from(PlayDemoVedioFile.this);
							final View v1=li.inflate(R.layout.alertdialoglayout, null);
							builder.setIcon(android.R.drawable.ic_input_get);
							builder.setView(v1);
							builder.setTitle("Please enter your username and password.");
							builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									EditText u=(EditText) v1.findViewById(R.id.username);
									EditText p=(EditText) v1.findViewById(R.id.password);
									if(u.getText().toString().length()!=0&&p.getText().toString().length()!=0){

										new Reload(PlayDemoVedioFile.this,null).execute(u.getText().toString(),p.getText().toString());

									}else{
										Toast.makeText(PlayDemoVedioFile.this, "Please enter valid credentials", Toast.LENGTH_LONG).show();
										dialog.dismiss();

									}
								} 
							});
							v1.findViewById(R.id.password).setOnFocusChangeListener(new OnFocusChangeListener() {

								@Override
								public void onFocusChange(View v, boolean hasFocus) {
									if(hasFocus)
										((EditText) v1.findViewById(R.id.password)).setHint("");
									//else  mPasswordView.setHint("**********");

								}
							});
							v1.findViewById(R.id.username).setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {

									((EditText) v1.findViewById(R.id.username)).setHint("");
								}
							});
							builder.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
							builder.show();
						}
					});
					alertDialog.setButton("No", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							alertDialog.dismiss();
						}
					});
					alertDialog.setIcon(R.drawable.logo);
					alertDialog.show();

				}
			}
			else
			{
				Toast.makeText(PlayDemoVedioFile.this, "Internet connection is not present", Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.logoutmenunew:

			createSharePreferanceforLoginVisibleInvisible();

			Intent logoutIntent=new Intent(PlayDemoVedioFile.this,Login.class);
			//myWebView.loadUrl("about:blank");
			startActivity(logoutIntent);
			SharedPreferences myPrefs = PlayDemoVedioFile.this.getSharedPreferences("Login",Context.MODE_PRIVATE);
			myPrefs.edit().clear().commit();  
			if(Constant.chapterArrayList!=null){
				Constant.chapterArrayList.clear();
			}
			startActivity(logoutIntent);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	*/
	
	private void createSharePreferanceforLoginVisibleInvisible() {

		SharedPreferences        preferences           = PreferenceManager.getDefaultSharedPreferences( PlayDemoVedioFile.this);
		SharedPreferences.Editor editor                = preferences.edit();
		String                   sharedPreferencesdate = preferences.getString( "StatusTrue", "");
		if(sharedPreferencesdate.equals("isLoginTrue"))
		{
			editor = preferences.edit();
			editor.putString("StatusTrue", "isLoginFalse");
			editor.commit();
		}

	}
	
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
							DeleteFile(f.getAbsolutePath());
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
	@SuppressWarnings("deprecation")
	public  void createDialogfordemo()
	{
		final AlertDialog alertDialog = new AlertDialog.Builder( new ContextThemeWrapper ( PlayDemoVedioFile.this, R.style.MyDialogTheme)).create();
		alertDialog.setTitle("eTeach");
		alertDialog.setMessage("Do you want to restore this application for demo?");

		alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int which) {
				String urldemo = "http://" + Constant.WEBSERVER + "/android/demoproject/XMLFile/XMLFile.zip";
				new DownloadXmlFileForDemo ( urldemo, Constant.INTERNAL_SDCARD + "1234ABCD/eteach/XMLFile.zip", PlayDemoVedioFile.this).execute( "");
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
	@SuppressWarnings("deprecation")
	public void alertDialogUpdate(){

		AlertDialog.Builder builder = new AlertDialog.Builder( PlayDemoVedioFile.this, R.style.MyDialogTheme);
		builder.setTitle("eTeach");
		builder.setMessage("Do you want to update?");

		String positiveText = getString( android.R.string.ok);
		builder.setPositiveButton(positiveText,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick( DialogInterface dialog, int which) {
						new DownloadFileUpgrade ( PlayDemoVedioFile.this, "http://" + Constant.WEBSERVER + "/android/demoproject/upgrade/upgradeApp.xml", Constant.INTERNAL_SDCARD + "/upgradeApp.xml", 1);
					}
				});

		String negativeText = getString( android.R.string.cancel);
		builder.setNegativeButton(negativeText,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick( DialogInterface dialog, int which) {
						// negative button logic
					}
				});

		AlertDialog dialog = builder.create();
		// display dialog
		dialog.show();

		/*final AlertDialog ald1= new AlertDialog.Builder(new ContextThemeWrapper(PlayDemoVedioFile.this, R.style.popup_theme)).create();
		ald1.setTitle("eTeach");
		ald1.setIcon(R.drawable.logo);
		ald1.setMessage("Do you want to update?");

		ald1.setButton2("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				ald1.dismiss();
			}
		});
		ald1.setButton("Yes",new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				
				new DownloadFileUpgrade(PlayDemoVedioFile.this, "http://"+Constant.WEBSERVER+"/android/demoproject/upgrade/upgradeApp.xml", Constant.INTERNAL_SDCARD+"/upgradeApp.xml",1);

				//	new UpdatesAsyntask().execute("");

				//				Intent goToSubject=new Intent(getApplicationContext(),CheckingUpdateActivity.class);
				//				myWebView.loadUrl("about:blank");
				//				startActivity(goToSubject);
			}
		});
		ald1.show();*/
	} 

}
