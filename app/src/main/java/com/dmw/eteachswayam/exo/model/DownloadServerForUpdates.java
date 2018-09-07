package com.dmw.eteachswayam.exo.model;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.activity.CancelUpdatesDownloading;
import com.dmw.eteachswayam.exo.activity.SettingUi;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class DownloadServerForUpdates extends Service {

	private Looper mServiceLooper;
	NotificationCompat.Builder builder;
	private ServiceHandler      mServiceHandler;
	private NotificationManager mNM;
	String downloadUrl;
	static int bufferSize = 4096;
	private HttpURLConnection    c;
	private BufferedOutputStream f;
	private BufferedInputStream  in;
	Notification notification;
	private PendingIntent contentIntent;
	private int           fileSize;
	private String        downloadPath;
	ArrayList<Items1> fileArrayList;
	private       Items1 name;
	public static String mntPath12;
	public static double MB_Available;
	private       int    i;
	public static       boolean serviceState =true;
	public static final String  TAG          = "MyService";
	private             boolean flag         = false;
	private             boolean checksdcard  =false;
	private String            sdcardpath;
	private String            sdcardpathforupdates;
	private String            username;
	private String            accountId;
	private String            ListPreference;
	private SharedPreferences prefs;
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {

			downloadFile();

			stopSelf(msg.arg1);
		}
	}


	@Override
	public
    IBinder onBind( Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mNM = (NotificationManager )getSystemService( NOTIFICATION_SERVICE);
		HandlerThread thread = new HandlerThread ( "ServiceStartArguments", 1);
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);

	}
	public void downloadFile(){	
		i=1;
		String result =ISP();
		if(result.equals("true")){
			for ( Iterator<Items1> iterator = fileArrayList.iterator() ; iterator.hasNext() && serviceState; i++) {
				name = (Items1) iterator.next();
				downloadFile(downloadUrl,name.toString(),getResources().getString(R.string.notification_catalog_downloaded),"Downloading ",i,fileArrayList.size());
				String name1 =name.getFileName();
				name1 = name1.replace("\\", "/");
				String   lastName      =name1.substring( 0, name1.lastIndexOf( "/"));
				String[] lastNameSplit =name1.split( lastName);
				if(flag){	
					delete(new File ( downloadPath + name1));
					if(downloadPath.contains( Environment.getExternalStorageDirectory().getAbsolutePath()))
					{
						checkMedia();
						if(checksdcard){
							//copy chapter to external
							for(int j = 0; j< SettingUi.mMounts.size(); j++){
								if(!SettingUi.mMounts.get(j).equals( Environment.getExternalStorageDirectory().getAbsolutePath())){
									sdcardpathforupdates = SettingUi.mMounts.get( j);
									String   copypath      = downloadPath + name1;
									String[] parsecopypath =copypath.split( "eteach");
									Log.e( parsecopypath[0], parsecopypath[1]);
									String chaptername =parsecopypath[1].substring( parsecopypath[1].lastIndexOf( "/"));

									String   orignalpath     = sdcardpathforupdates + "/eteach" + parsecopypath[1] + chaptername + ".zip";
									String[] deleteextsdfile =orignalpath.split( username);

									firstCopy(downloadPath+name1+lastNameSplit[1].toString()+".zip",orignalpath,sdcardpathforupdates+"/eteach"+parsecopypath[1]);
									File extsdfile =new File ( deleteextsdfile[0] + username + "/" + name1 + "/" + "index1_E.html");
									if(extsdfile.exists()){
										delete(new File ( deleteextsdfile[0] + username + "/" + name1));
									}

									extractZipFiles(downloadPath+name1+lastNameSplit[1].toString()+".zip");

									XML.createAppXml(downloadPath+name1,name1+lastNameSplit[1].toString());

									extractZipFiles(orignalpath);

									XML.createAppXml(deleteextsdfile[0]+username+"/"+name1,name1+lastNameSplit[1].toString());

									callReceiver("OK");
								}
							}
						}else{
							extractZipFiles(downloadPath+name1+lastNameSplit[1].toString()+".zip");
							XML.createAppXml(downloadPath+name1,name1+lastNameSplit[1].toString());
							callReceiver("OK");
						}
					}else{
						//copy chapter to internal and extract
						String copyinternalPath = Constant.INTERNAL_SDCARD + accountId + "/" + username + "/" + name1 + lastNameSplit[1].toString() + ".zip";
						String createFolderPath = Constant.INTERNAL_SDCARD + accountId + "/" + username + "/" + name1;

						firstCopy(downloadPath+name1+lastNameSplit[1].toString()+".zip",copyinternalPath,createFolderPath);
						
						//delete chapter from internal
						
						File filedelete =new File ( createFolderPath + "/index1_E.html");
						if(filedelete.exists()){
							delete(new File ( createFolderPath));
						}
						
						//extract chapter from internal
						
						extractZipFiles(copyinternalPath);
						XML.createAppXml(createFolderPath,name1+lastNameSplit[1].toString());

						//extarct chapter from external

					
						extractZipFiles(downloadPath+name1+"/"+lastNameSplit[1].toString()+".zip");
						XML.createAppXml(downloadPath+name1,name1+lastNameSplit[1].toString());


					}
				}else{
					//callReceiver("Not");
					break;
				}
			}
		}
		mNM.cancel( R.string.app_name);

	}
	private void firstCopy( String orignalpath, String distinationFilePath, String createfolder) {
		try {
			File file =new File ( createfolder);
			if(!file.exists())
			{
				file.mkdirs();
			}
			InputStream  in  = new FileInputStream ( orignalpath);
			OutputStream out = new FileOutputStream ( distinationFilePath);
			byte[]       buf = new byte[1024*16];
			int          len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	private boolean checkMedia() {
		SettingUi.mMounts.clear();
		SettingUi.determineStorageOptions();
		if(SettingUi.mMounts.size()>=2){
			checksdcard=true;
		}
		return checksdcard;
	}




	@Override
	public int onStartCommand( Intent intent, int flags, int startId) {
		Log.d( "SERVICE-ONCOMMAND", "onStartCommand");
		SharedPreferences settings = getApplicationContext().getSharedPreferences( "username", 0);
		accountId =settings.getString("ACCOUNTID", "xxx");
		username =settings.getString("user","xxx");

		prefs = PreferenceManager.getDefaultSharedPreferences( getBaseContext());
		ListPreference = prefs.getString("listPref", "digiGreen");
		if (ListPreference.equals("2")){
			SharedPreferences shared = getSharedPreferences( "path", 0);
			Constant.ContentPath= (shared.getString("pathdemo", ""+Constant.INTERNAL_SDCARD));
		}else {
			Constant.ContentPath= Constant.INTERNAL_SDCARD;
		}
		Bundle extra = intent.getExtras();


		String downloadUrl = "http://" + Constant.WEBSERVER + "/android/demoproject/eteach/assets/Content/Actual/";
		//Log.d("URL",downloadUrl);
		//			this.downloadPath = extra.getString("downloadPath");
		this.downloadPath = Constant.ContentPath+accountId+"/"+username+"/";
		this.fileArrayList=extra.getParcelableArrayList("fileArrayList");
		this.downloadUrl=downloadUrl;


		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		mServiceHandler.sendMessage(msg);

		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	public
    String ISP()
	{
		HttpResponse      response   = null;
		DefaultHttpClient httpClient = new DefaultHttpClient ();
		String            result     ="";
		SharedPreferences settings   = getApplicationContext().getSharedPreferences( "username", 0);
		String            username   =settings.getString( "user", "xxx");
		HttpGet           httpget    = new HttpGet( "http://" + Constant.WEBSERVER + "/designo_pro/ws_isp.php?format=json&usrnm=" + username + "&hddid=" + getUdid());
		try {
			response = httpClient.execute(httpget);
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		HttpEntity entity = response.getEntity();
		try {
			if (entity != null) {

				InputStream instream = entity.getContent();
				result = convertStreamToString(instream);
				if(result!=null){
					JSONObject jsonObject =new JSONObject ( result);
					JSONArray  hrArray    = jsonObject.getJSONArray( "posts");

					JSONObject post = hrArray.getJSONObject( 0)
							.getJSONObject("post");

					result = post.getString("result");
				}
				instream.close();
			}
		} catch (IllegalStateException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
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
	private
    String convertStreamToString( InputStream is) {

		BufferedReader reader = new BufferedReader ( new InputStreamReader ( is));
		StringBuilder  sb     = new StringBuilder ();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	public void downloadFile( String fileURL, String filename, String message, String title, int no, int all) {
		long numBytesDownloaded = 0;
		flag=true;

		//		StatFs stat_fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
		//		double avail_sd_space = (double)stat_fs.getAvailableBlocks() *(double)stat_fs.getBlockSize();
		//		double MB_Available = (avail_sd_space / 10485783);
		//		Log.d("MB",""+MB_Available);
		try {

			if(Constant.ContentPath.contains( Environment.getExternalStorageDirectory().getAbsolutePath()))
			{
				MB_Available=0;
				StatFs stat_fs = new StatFs ( Environment.getExternalStorageDirectory().getAbsolutePath());

				double avail_sd_space = (double)stat_fs.getAvailableBlocks() *(double)stat_fs.getBlockSize();
				//double a=1024;

				MB_Available = (long)(avail_sd_space);

				MB_Available=MB_Available/1024;
				MB_Available=MB_Available/1024;
				Log.d( "AvailableMem", "" + MB_Available);


				// MB_Available=MB_Available-100;
				Log.d( "AvailableMemafter minus", "" + MB_Available);
			}
			else if(!getAvailableExternalMemorySize()){
				return;
			}



			filename = filename.replace("\\", "/");
			String   chapterName        =filename.substring( 0, filename.lastIndexOf( "/"));
			String[] chapterNameStrings =filename.split( chapterName);
			File     root               =new File ( downloadPath + filename);
			URL      u                  = new URL ( fileURL + filename + chapterNameStrings[1].toString() + ".zip");
			c = (HttpURLConnection ) u.openConnection();
			if(root.getParentFile().exists() && root.getParentFile().isDirectory()) {
			}
			if(new File ( this.downloadPath , filename + chapterNameStrings[1].toString() + ".zip").exists()){
				c.setRequestProperty("Range", "bytes=" + new File ( this.downloadPath , filename + chapterNameStrings[1].toString() + ".zip").length() + "-");
				f = new BufferedOutputStream ( new FileOutputStream ( new File ( this.downloadPath , filename + chapterNameStrings[1].toString() + ".zip"), true));
				numBytesDownloaded= new File ( this.downloadPath , filename + chapterNameStrings[1].toString() + ".zip").length();
			}else{				
				root.getParentFile().mkdirs();
				c.setRequestProperty("Range", "bytes=" + 0 + "-");
				f = new BufferedOutputStream ( new FileOutputStream ( new File ( this.downloadPath , filename + chapterNameStrings[1].toString() + ".zip"), false));

			}

			Log.d( "CURRENT PATH", root.getPath());


			fileSize  = c.getContentLength();
			c.connect();
			if(((fileSize/1048576)+100)<MB_Available)
			{	
				CharSequence text = message;
				 builder =	new NotificationCompat.Builder(DownloadServerForUpdates.this);
				//notification = new Notification(R.drawable.logo, "Updates Downloading ",System.currentTimeMillis());
				notification = builder
						.setContentTitle("eTeach")
						.setContentText("Updates Downloading")
						.setSmallIcon(R.drawable.logo)
						.setVibrate(new long[]{1000,1000})
						.build();
				RemoteViews contentView = new RemoteViews ( this.getBaseContext().getPackageName(), R.layout.custom_notification_layout);
				contentView.setProgressBar(R.id.progressBar, fileSize, 0, false);        
				contentView.setTextViewText(R.id.text, text);
				notification.contentView = contentView;
				Intent intent = new Intent ( this, CancelUpdatesDownloading.class);
				intent.setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP);
				contentIntent = PendingIntent.getActivity( this.getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			//	notification.setLatestEventInfo(this, title+chapterNameStrings[1].toString().substring(1),text, contentIntent);
				notification = builder.setContentIntent(contentIntent)
						.setContentText(title+chapterNameStrings[1].toString().substring(1))
						.setContentText(text)
						.setSmallIcon(R.drawable.logo)
						.build();
					mNM.notify(R.string.app_name, notification);


				in = new BufferedInputStream ( c.getInputStream(), bufferSize);
				byte[] buffer = new byte[bufferSize];
				int len1 = 0;
				while ((len1 = in.read(buffer)) > 0 && serviceState ) {
					if((numBytesDownloaded * 100 / fileSize)%10==0)
						progressUpdate((int)numBytesDownloaded * 100 / fileSize,title+chapterNameStrings[1].toString().substring(1),no,all);
					f.write(buffer, 0, len1);
					numBytesDownloaded += len1;
				}
				f.close();

				if(numBytesDownloaded >= fileSize){
					progressUpdate(100,"Download Compleate",no,all);
				}else{
					flag=false;
					progressUpdate(0,"Download Compleate",all,all);
				}

			}else{
				Toast.makeText( getApplicationContext(), MB_Available + " MB is available and \n space required to download this chapter is " + ( fileSize / 1048576 + 100) + "MB", Toast.LENGTH_LONG).show();
				serviceState=false;
				flag=false;

			}
		}catch(FileNotFoundException e){
			callReceiver("FNF");
			flag=false;
		}catch(SocketException e){
			callReceiver("ERROR");
			flag=false;
		}catch (Exception e) {
			Log.d( "Downloader", e.getMessage());
			callReceiver("ERROR");
			flag=false; 
		}
		finally {
			try {      
				if(f != null) {
					f.flush();
					f.close();
				}
				if(in != null) {
					in.close();
				}
				c.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void progressUpdate( int percentageComplete, String string, int count, int total) {
		if(percentageComplete>0&&percentageComplete<100){
			CharSequence contentText = percentageComplete + "% complete  " + count + "/" + total;
			notification.contentView.setProgressBar(R.id.progressBar, fileSize, percentageComplete, false);
		//	notification.setLatestEventInfo(this, string,contentText, contentIntent);

			notification = builder.setContentIntent(contentIntent)
					.setContentText(string)
					.setContentText(contentText)
					.setSmallIcon(R.drawable.logo)
					.build();

			mNM.notify(R.string.app_name, notification);
		}
	}
	public boolean getAvailableExternalMemorySize() {
		boolean  val     =true;
		String[] path11  =Constant.ContentPath.split( "eteach");
		String[] path12  =Constant.ContentPath.split( "Content");
		String   mntPath =path11[0].toString();
		mntPath12=path12[0].toString();
		// mntPath=mntPath.substring(0,mntPath.lastIndexOf("/"));
		File path1 =new File ( mntPath12);

		Log.d( "mntPath", mntPath);


		File path = new File ( mntPath);
		if (path1.exists()) {
			Log.d( "externalMemoryAvailable", "externalMemoryAvailable");
			MB_Available=0;


			StatFs stat_fs1        = new StatFs ( path.getAbsolutePath());
			double avail_sd_space1 = (double)stat_fs1.getAvailableBlocks() *(double)stat_fs1.getBlockSize();

			MB_Available = (long)(avail_sd_space1);

			MB_Available=MB_Available/1024;
			MB_Available=MB_Available/1024;
			Log.d( "AvailableMem", "" + MB_Available);

			//MB_Available=MB_Available;
			Log.d( "AvailableMemafter minus", "" + MB_Available);
		}
		else
		{
			Toast.makeText( this, "" +
                                  "SD Card is not mounted", Toast.LENGTH_LONG).show();
			//Log.d("externalMemoryNotAvailable", "externalMemoryNotAvailable");
			val=false;
		}
		return val;
	}
	public  void callReceiver(String str){
		Intent intent = new Intent ();
		intent.setAction("com.manish.android.mybroadcast");
		intent.putExtra("Messsage",str);
		sendBroadcast(intent);
	}
	public  void delete(File file)
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
	private void extractZipFiles(String filename) {
		try {

			String destinationname =filename.substring( 0, filename.lastIndexOf( "/") - 1);
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
					while ((n = zipinputstream.read(buf, 0, 1024)) > -1){
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
		}finally{

		}
	}

	private static void extractFile( ZipInputStream in, File outdir, String name) throws IOException
	{
		byte[]               buffer = new byte[bufferSize];
		BufferedOutputStream out    = new BufferedOutputStream ( new FileOutputStream ( new File ( outdir, name)));
		int                  count  = -1;
		while ((count = in.read(buffer)) != -1)
			out.write(buffer, 0, count);
		out.close();
	}

	private static void mkdirs( File outdir, String path)
	{
		File d = new File ( outdir, path);
		if( !d.exists() )
			d.mkdirs();
	}

	private static
    String dirpart( String name)
	{
		int s = name.lastIndexOf( File.separatorChar );
		return s == -1 ? null : name.substring( 0, s );
	}

/*	*//***
	 * Extract zipfile to outdir with complete directory structure
	 * @param zipfile Input .zip file
	 */
	public static void extract(File zipfile)
	{
		File outdir = zipfile.getParentFile().getParentFile();
		try
		{
			ZipInputStream zin = new ZipInputStream ( new FileInputStream ( zipfile));
			ZipEntry       entry;
			String         name, dir;
			while ((entry = zin.getNextEntry()) != null)
			{
				name = entry.getName();
				if( entry.isDirectory() )
				{
					mkdirs(outdir,name);
					continue;
				}

				dir = dirpart(name);
				if( dir != null )
					mkdirs(outdir,dir);

				extractFile(zin, outdir, name);

			}
			zin.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}finally{
			if(zipfile.exists())zipfile.delete();

		}
	}
}
