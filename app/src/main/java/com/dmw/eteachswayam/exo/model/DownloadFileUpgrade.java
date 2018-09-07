package com.dmw.eteachswayam.exo.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.activity.CheckingUpdateActivity;
import com.dmw.eteachswayam.exo.activity.HomeActivity;
import com.dmw.eteachswayam.exo.activity.WhatnewActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class DownloadFileUpgrade  {

	private        ProgressDialog mProgressDialog;
	private        Activity       act;
	private        String         downloadUrl;
	private        String         downloadPath;
	private        int            mode;
	private        String         newVersion;
	private static String         newVerLink;
	int          bufferSize         = 4096;
	byte[]       retVal             = null;
	InputStream  in                 = null;
	OutputStream fileos             = null;
	long         numBytesDownloaded = 0;
	HttpURLConnection urlConnection;
	private        ArrayList<Upgrade> upgrades;
	private static Upgrade            upgrade;
	public static boolean downloadFinish = false;
	private static ArrayList<Upgrade> arrayListforUpgrade;

	public DownloadFileUpgrade( Activity act, String url, String path, int mode) {
		// TODO Auto-generated constructor stub

		this.mode=mode;
		this.downloadPath = path;
		this.downloadUrl = url;
		this.act=act;
		if(isDataEnable())
			startDownload();
		else
			Toast.makeText( act, "Your internet connection is not available.", Toast.LENGTH_SHORT).show();
	}


	public boolean isDataEnable() {
		ConnectivityManager conMgr;
		conMgr = (ConnectivityManager ) act.getSystemService( Context.CONNECTIVITY_SERVICE);
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
	private void startDownload() {
		String url = downloadUrl;
		downloadPath = downloadPath.replace("\\", "/");
		url=url.replace("\\", "/");
		url = url.replace(" ", "%20");
		new DownloadFileAsync().execute(url);
	}

	private
    ProgressDialog showDialog() {
		// TODO Auto-generated method stub
		mProgressDialog = new ProgressDialog ( act);
		if(mode ==1)
			mProgressDialog.setMessage("Checking updates. Please wait...");
		else
			mProgressDialog.setMessage("Downloading new version. Please wait...");
		//mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		return mProgressDialog;
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {



		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog();
		}



		@Override
		protected
        String doInBackground( String... aurl) {

			try {
				// opens a new url connection
				urlConnection = (HttpURLConnection ) new URL ( aurl[0]).openConnection();
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
				//downloadFinish=false;
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
			downloadFinish=true;
			return "true";
		}



		@Override
		protected void onProgressUpdate(String... progress) {
			Log.d( "ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress( Integer.parseInt( progress[0]));
		}

		@SuppressWarnings("unused")
		@Override
		protected void onPostExecute(String unused) {
			//dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			mProgressDialog.dismiss();
			
			if(downloadFinish){
				if(mode == 2){

					for (int i = 0; i < arrayListforUpgrade.size(); i++) {
						String link     =arrayListforUpgrade.get( i).getInstaler();
						String fileName = Constant.INTERNAL_SDCARD + link;
						Log.e( "", fileName);
						Intent intent = new Intent ( Intent.ACTION_VIEW);
						intent.setDataAndType( Uri.fromFile( new File ( fileName)), "application/vnd.android.package-archive");
						act.startActivity(intent);
					}
				}

				if(mode == 1)
				{
					arrayListforUpgrade=new ArrayList<Upgrade> ();
					arrayListforUpgrade=readXmlforupgreade(Constant.INTERNAL_SDCARD+"/upgradeApp.xml", Constant.appName);

					new DeleteFile(Constant.INTERNAL_SDCARD+"upgradeApp.xml");
					if(!arrayListforUpgrade.isEmpty()){
						for (int i = 0; i < arrayListforUpgrade.size(); i++) {
							String version              =arrayListforUpgrade.get( i).getVersion();
							double dnew                 = Double.parseDouble( version);
							double dmainactivityversion = Double.parseDouble( HomeActivity.version1);

							if(dmainactivityversion<dnew)

							{
								final AlertDialog alertDialog = new AlertDialog.Builder( new ContextThemeWrapper ( act, R.style.MyDialogTheme)).create();
								alertDialog.setTitle("Upgrade");
								alertDialog.setMessage("New version available. \nclick ok to install new version");
								alertDialog.setButton( -1,"OK", new DialogInterface.OnClickListener() {
									public void onClick( DialogInterface dialog, int which) {
										for (int i = 0; i < arrayListforUpgrade.size(); i++) {
											String linknew    =arrayListforUpgrade.get( i).getInstaler();
											Intent gotowhatup =new Intent ( act, WhatnewActivity.class);
											gotowhatup.putExtra("newVerLink",linknew);
											act.startActivity(gotowhatup);
											act.finish();
										}
									} });
								alertDialog.setButton( -2,"Cancel", new DialogInterface.OnClickListener() {
									public void onClick( DialogInterface dialog, int which) {

										alertDialog.dismiss();
									} });
								alertDialog.show();		    			

							}
							else
							{
								Intent goToSubject =new Intent ( act, CheckingUpdateActivity.class);
								//myWebView.loadUrl("about:blank");
								act.startActivity(goToSubject);




								//						final AlertDialog alertDialog = new AlertDialog.Builder(act).create();
								//						alertDialog.setTitle("Update");
								//						alertDialog.setMessage("Check Chapter Updates Available!");
								//						alertDialog.setButton( -1,"Update", new DialogInterface.OnClickListener() {
								//							public void onClick(DialogInterface dialog, int which) {
								//								Intent goToSubject=new Intent(act,CheckingUpdateActivity.class);
								//								//myWebView.loadUrl("about:blank");
								//								act.startActivity(goToSubject);
								// alertDialog.dismiss();
								//} });
								//	alertDialog.setButton( -2,"Cancel", new DialogInterface.OnClickListener() {
								//	public void onClick(DialogInterface dialog, int which) {
								//		alertDialog.dismiss();
								//	} });
								//alertDialog.show();		
							}
						}
					}

				}
			}
		}
	}
	public
    ArrayList<Upgrade> readXmlforupgreade( String filePath, String appnameforuprgade){
		try {
			upgrades=new ArrayList<Upgrade> ();
			upgrade=new Upgrade();

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        docBuilder        = docBuilderFactory.newDocumentBuilder();
			Document               doc               = docBuilder.parse ( new File ( filePath));

			// normalize text representation
			doc.getDocumentElement ().normalize ();
			System.out.println ( "Root element of the doc is " +
                                 doc.getDocumentElement().getNodeName());


			NodeList listOfPersons = doc.getElementsByTagName( "appNameinfo");
			int      totalPersons  = listOfPersons.getLength();
			System.out.println( "Total no of people : " + totalPersons);

			for(int s=0; s<listOfPersons.getLength() ; s++){


				Node firstPersonNode = listOfPersons.item( s);
				if( firstPersonNode.getNodeType() == Node.ELEMENT_NODE){


					Element firstPersonElement = (Element )firstPersonNode;

					//-------
					NodeList firstNameList    = firstPersonElement.getElementsByTagName( "appname");
					Element  firstNameElement = (Element )firstNameList.item( 0);

					NodeList textFNList = firstNameElement.getChildNodes();
					System.out.println( "First Name : " +
                                        ((Node )textFNList.item( 0)).getNodeValue().trim());

					String appname =( (Node )textFNList.item( 0)).getNodeValue().trim();


					NodeList fileSize                = firstPersonElement.getElementsByTagName( "version");
					Element  fileSizefileSizeelement = (Element )fileSize.item( 0);

					NodeList textFNListsize = fileSizefileSizeelement.getChildNodes();
					System.out.println( "First Name : " +
                                        ((Node )textFNListsize.item( 0)).getNodeValue().trim());
					String version =( (Node )textFNListsize.item( 0)).getNodeValue().trim();


					NodeList fileSize1                = firstPersonElement.getElementsByTagName( "installer");
					Element  fileSizefileSizeelement1 = (Element )fileSize1.item( 0);

					NodeList textFNListsize1 = fileSizefileSizeelement1.getChildNodes();
					System.out.println( "First Name : " +
                                        ((Node )textFNListsize1.item( 0)).getNodeValue().trim());
					String instalerupragde =( (Node )textFNListsize1.item( 0)).getNodeValue().trim();

					if(appname.equals(appnameforuprgade))
					{
						upgrade.setAppName(appname);
						upgrade.setVersion(version);
						upgrade.setInstaler(instalerupragde);
						upgrades.add(upgrade);
					}
					

				}//end of if clause


			}//end of for loop with s var
//			File file=new File(filePath);
//			if(file.exists())
//			{
//				file.delete();
//			}

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
		return upgrades;




	}//end of main

}