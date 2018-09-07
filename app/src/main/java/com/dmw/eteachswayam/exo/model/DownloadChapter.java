package com.dmw.eteachswayam.exo.model;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
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

import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.activity.CancleChapterDialogeActivity;
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
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

//import android.graphics.AvoidXfermode;

public class DownloadChapter extends Service {

	SharedPreferences preferences;
	private Looper         mServiceLooper;
	private ServiceHandler mServiceHandler;
	NotificationCompat.Builder builder;

	private NotificationManager mNM;
	String downloadUrl;
	static int bufferSize = 1024*16;
	private       HttpURLConnection    c;
	private       BufferedOutputStream f;
	private       BufferedInputStream  in;
	private       Notification         notification;
	private       PendingIntent        contentIntent;
	private       int                  fileSize;
	private       String               downloadPath;
	public static String               mntPath12;
	public static double               MB_Available;
	String chaptername;
	public static final String  TAG  = "MyServiceTag";
	private             boolean flag = false;
	private String uptoSubject;
	public static boolean serviceState=false;


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
	public void onCreate() {
		
		mNM = (NotificationManager )getSystemService( NOTIFICATION_SERVICE);
		HandlerThread thread = new HandlerThread ( "ServiceStartArguments", 1);
		thread.start();
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		mNM.cancelAll();
	}

	@Override
	public int onStartCommand( Intent intent, int flags, int startId) {
		Log.d( "SERVICE-ONCOMMAND", "onStartCommand");
		if(intent!=null){
			if(intent.hasExtra("downloadPath")){
				Bundle extra = intent.getExtras();
				if(extra != null){
					String downloadUrl = "http://" + Constant.SERVER_ADDRESS + extra.getString( "downloadUrl");
					Log.d( "URL", downloadUrl);
					this.downloadPath = extra.getString("downloadPath");
					this.chaptername=extra.getString("chaptername");
					this.uptoSubject=extra.getString("uptoSubject");
					this.downloadUrl=downloadUrl;
				}

				Message msg = mServiceHandler.obtainMessage();
				msg.arg1 = startId;
				mServiceHandler.sendMessage(msg);
			}                 
			// If we get killed, after returning from here, restart
			return START_STICKY;
		}
		return START_STICKY_COMPATIBILITY;

	}

	@Override
	public void onDestroy() {
		mNM.cancelAll();
		Log.d( "SERVICE-DESTROY", "DESTORY");
		serviceState=false;
	}

	@Override
	public
    IBinder onBind( Intent intent) {
		return null;
	}

	public void downloadFile(){

		if(!new File ( new File ( downloadPath).getParentFile().getAbsolutePath() + "/index1_E.html").exists()){
			String result =ISP();
			if(result.equals("true")){
				serviceState=true;
				downloadFile( downloadUrl, chaptername.toString(), getResources().getString( R.string.notification_catalog_downloaded), "Downloading ");
				if(flag){
					extract(new File ( downloadPath));
					String infoPath =downloadPath.substring( 0, downloadPath.lastIndexOf( "/"));
					XML.createAppXml(infoPath,uptoSubject+chaptername.toString()+"/"+chaptername.toString());
				}else if ( new File ( downloadPath).length() == 0){
					new File ( downloadPath).delete();
				}
			}
		}
		mNM.cancel(R.string.app_name);
		callReceiver("OK");

	}
	public
    String ISP()
	{
		String result ="";
		try {
			HttpResponse      response   = null;
			DefaultHttpClient httpClient = new DefaultHttpClient ();

			SharedPreferences settings = getApplicationContext().getSharedPreferences( "username", 0);
			String            username =settings.getString( "user", "xxx");
			HttpGet           httpget  = new HttpGet( "http://" + Constant.WEBSERVER + "/designo_pro/ws_isp.php?format=json&usrnm=" + username + "&hddid=" + getUdid());

			response = httpClient.execute(httpget);

			if(response!=null){
				HttpEntity entity = response.getEntity();

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
	public void callReceiver(String str){

		Intent intent = new Intent ();
		intent.setAction("com.manish.android.mybroadcast");
		intent.putExtra("Messsage",str);
		sendBroadcast(intent);
	}

	public void downloadFile( String fileURL, String filename, String message, String title) {
		long numBytesDownloaded = 0;

		String path =Constant.ContentPath;
		if(path.contains( Environment.getExternalStorageDirectory().getAbsolutePath()))
		{
			StatFs stat_fs = new StatFs ( Environment.getExternalStorageDirectory().getPath());

			double avail_sd_space = (double)stat_fs.getAvailableBlocks() *(double)stat_fs.getBlockSize();
			//double MB_Available = (avail_sd_space / 10485783);
			MB_Available = (avail_sd_space /1048576);
			// MB_Available=MB_Available-20;
			Log.d( "AvailableMB", "" + MB_Available);
		}
		else if(!getAvailableExternalMemorySize()) {
			return;
		}

		try {
			flag=true;
			File root =new File ( downloadPath);
			URL  u    = new URL ( fileURL);
			c = (HttpURLConnection ) u.openConnection();
			HttpURLConnection cv =(HttpURLConnection ) u.openConnection();
			fileSize  = cv.getContentLength();
			
			if(fileSize<=root.length()){
				
							}
 			if(root.getParentFile().exists() && root.getParentFile().isDirectory()) {
				c.setRequestProperty("Range", "bytes=" + root.length() + "-");
				f = new BufferedOutputStream ( new FileOutputStream ( this.downloadPath, true));
				numBytesDownloaded= root.length();
			}else{				
				root.getParentFile().mkdirs();
				c.setRequestProperty("Range", "bytes=" + 0 + "-");
				f = new BufferedOutputStream ( new FileOutputStream ( this.downloadPath, false));
				
			}
			Log.d( "CURRENT PATH", root.getPath());

						
			fileSize  = c.getContentLength();
			
			c.connect();
			CharSequence text = message;
			//fileSize=fileSize/1024/1024;
			if(((fileSize/1048576)+100)<MB_Available)
			{

				Log.d( "AvailableMB+100", "" + MB_Available);
				Log.d( "FileSize", "" + fileSize / 1048576);

			 builder = new NotificationCompat.Builder(this);

				//notification = new Notification(R.drawable.logo, "Downloading Chapter",System.currentTimeMillis());
				notification = builder
						.setContentTitle("eTeach")
						.setContentText("Downloading Chapter")
						.setSmallIcon(R.drawable.logo)
						.setDefaults( Notification.DEFAULT_SOUND)
						.build();
				RemoteViews contentView = new RemoteViews ( this.getBaseContext().getPackageName(), R.layout.custom_notification_layout);
				contentView.setProgressBar(R.id.progressBar, fileSize, 0, false);        
				contentView.setTextViewText(R.id.text, text);       
				notification.contentView = contentView;
				Intent intent = new Intent ( this, CancleChapterDialogeActivity.class);
				intent.setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP);
				contentIntent = PendingIntent.getActivity( this.getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			//	notification.setLatestEventInfo(this, title+filename,text, contentIntent);
				notification = builder
					.setContentTitle("title+filename")
					.setContentText(text)
						.setContentIntent(contentIntent)
						.build();
				mNM.notify(R.string.app_name, notification);

				progressUpdate((int)numBytesDownloaded * 100 / fileSize,title+filename);
				
				in = new BufferedInputStream ( c.getInputStream(), bufferSize);
				byte[] buffer = new byte[bufferSize];
				int len1 = 0;
				while ((len1 = in.read(buffer)) > 0 && serviceState ) {
					if((numBytesDownloaded * 100 / fileSize)%10>=0)
						progressUpdate((int)numBytesDownloaded * 100 / fileSize,title+filename);
					f.write(buffer, 0, len1);
					numBytesDownloaded += len1;
				}

				f.close();

				if(numBytesDownloaded >= fileSize){
					progressUpdate(100,"downloading completed");
				}else{
					flag=false;

					progressUpdate(0,"Download Canceled");
				}

			}
			else
				{
					Toast.makeText( getApplicationContext(), MB_Available + " MB is available and \n space required to download this chapter is " + ( fileSize / 1048576 + 100) + "MB", Toast.LENGTH_LONG).show();
					Log.d( "MEMORY FULL", "MEMORY FULL");
					flag=false;
				}
			
		}catch(FileNotFoundException e){
			callReceiver("FNF");
			flag=false;
			progressUpdate(0,"Download Canceled");
		}catch(SocketException e){
			callReceiver("ERROR");
			flag=false;
			progressUpdate(0,"Download Canceled");
		}catch (Exception e) {
//			Log.d("Downloader", e.getMessage());
			callReceiver("ERROR");
			flag=false;
			progressUpdate(0,"Download Canceled");
		}finally {
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


	public static boolean isAccessibilityEnabled( Context context, String id) {

		AccessibilityManager am = (AccessibilityManager ) context
		.getSystemService( Context.ACCESSIBILITY_SERVICE);

		List<AccessibilityServiceInfo> runningServices = am
		.getEnabledAccessibilityServiceList( AccessibilityEvent.TYPES_ALL_MASK);
		for (AccessibilityServiceInfo service : runningServices) {
			if (id.equals(service.getId())) {
				return true;
			}
		}

		return false;
	}
	public void progressUpdate(int percentageComplete,String string) {
		//		if(isAccessibilityEnabled(this.getApplicationContext(),  Resources.getSystem().getString(R.string.app_name))){
		if(percentageComplete>0&&percentageComplete<100){
			CharSequence contentText = percentageComplete + "% complete  ";
			notification.contentView.setProgressBar(R.id.progressBar, fileSize, percentageComplete, false);
		//	notification.setLatestEventInfo(this, string,contentText, contentIntent);
			notification = builder
					.setContentTitle(string)
					.setContentText(contentText)
					.setSmallIcon(R.drawable.logo)
					.setDefaults( Notification.DEFAULT_SOUND)
					.build();
			mNM.notify(R.string.app_name, notification);
		}
		//		}
	}
	public class InstantMessenger extends AccessibilityService {

		private boolean isInit;

		@Override
		public void onAccessibilityEvent(AccessibilityEvent event) {
			if ( event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
				//Do something, eg getting packagename
				final String packagename = String.valueOf( event.getPackageName());
			}
		}

		@Override
		protected void onServiceConnected() {
			if (isInit) {
				return;
			}
			AccessibilityServiceInfo info = new AccessibilityServiceInfo ();
			info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
			info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
			setServiceInfo(info);
			isInit = true;
		}

		@Override
		public void onInterrupt() {
			isInit = false;
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

	/***
	 * Extract zipfile to outdir with complete directory structure
	 * @param zipfile Input .zip file
	 */
	public  void extract(File zipfile)
	{
		File outdir = zipfile.getParentFile();
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
			callReceiver("ERROR");
			e.printStackTrace();
		}finally{
			if(zipfile.exists())zipfile.delete();
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
			//	        	 File path = Environment.getExternalStorageDirectory();
			//		            String[] path11 =Constant.ContentPath.split("eteach");
			//		            String mntPath=path11[0].toString();
			//		            mntPath=mntPath.substring(0,mntPath.lastIndexOf("/"));
			// mntPath=(mntPath+"/"+path11[1].toString()).trim();

			//		            StatFs stat_fs1 = new StatFs(path.getAbsolutePath());
			// File path = new File(mntPath);
			StatFs stat_fs1        = new StatFs ( path.getAbsolutePath());
			double avail_sd_space1 = (double)stat_fs1.getAvailableBlocks() *(double)stat_fs1.getBlockSize();
			// long availableBlocks = stat.getAvailableBlocks();
			//return formatSize(availableBlocks * blockSize);




			//double a=1024;

			MB_Available = (long)(avail_sd_space1);

			MB_Available=MB_Available/1024;
			MB_Available=MB_Available/1024;
			Log.d( "AvailableMem", "" + MB_Available);
			//MB_Available=MB_Available-fileArrayList.size()*4;

			//MB_Available=MB_Available-20;
			Log.d( "AvailableMemafter minus", "" + MB_Available);
		}
		else{
			Toast.makeText( getApplicationContext(), "SD_CARD IS NOT MOUNTED", Toast.LENGTH_LONG).show();
			val=false;
		}
		return val;
	}


}