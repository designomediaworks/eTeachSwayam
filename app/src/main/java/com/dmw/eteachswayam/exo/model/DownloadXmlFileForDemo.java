package com.dmw.eteachswayam.exo.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownloadXmlFileForDemo extends AsyncTask<String, String, String> {


	String  url;
	String  downloadUrl;
	Context context;
	int          bufferSize         = 4096;
	byte[]       retVal             = null;
	InputStream  in                 = null;
	OutputStream fileos             = null;
	long         numBytesDownloaded = 0;
	HttpURLConnection urlConnection;
	private ProgressDialog dialog;
	boolean checkstatus=false;

	public DownloadXmlFileForDemo( String url, String downloadUrl, Context context) {
		super();
		this.url = url;
		this.downloadUrl = downloadUrl;
		this.context = context;
	}

	@Override
	protected
    String doInBackground( String... params) {
		try {
			//fileStatus=true;
			// opens a new url connection


			checkstatus=true;
			urlConnection = (HttpURLConnection ) new URL ( this.url).openConnection();
			int fileLength = urlConnection.getContentLength();
			// open the file output stream for file writing


			fileos = new BufferedOutputStream ( new FileOutputStream ( this.downloadUrl));

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
		} catch(FileNotFoundException e)
		{
			//fileStatus=false;
			checkstatus=false;
			e.printStackTrace();
		} 
		catch(SocketException e)
		{
			checkstatus=false;
			e.printStackTrace();
		}
		catch(IOException e) {
			Log.e( " ", e.getMessage());
			//downloadFinish=false;;
			checkstatus=false;
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
		return null;
	}
	@Override
	protected void onProgressUpdate(String... values) {
		dialog.setProgress( Integer.parseInt( values[0]));
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(checkstatus){
			String deleteFileName =this.downloadUrl.substring( 0, this.downloadUrl.lastIndexOf( "/"));
			delete(new File ( deleteFileName));
			extractZipFiles(this.downloadUrl);
		}
		if(!checkstatus){
			Toast.makeText( context, "Internet Connection Lost. Please Try Again.", Toast.LENGTH_LONG).show();
			if(new File ( this.downloadUrl).exists()){
				new File ( this.downloadUrl).delete();
			}
		}
		dialog.dismiss();
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		dialog=new ProgressDialog ( context);
		dialog.setMessage("Downloading file please wait...");
		dialog.setProgressStyle( ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		dialog.show();
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
	private void extractZipFiles(String filename) {
		try {

			String destinationname =filename.substring( 0, filename.lastIndexOf( "/"));
			//destinationname =destinationname.substring(0,destinationname.lastIndexOf("/"));
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


			zipinputstream.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e( "Exception", "" + e);
		}finally{
			File f =new File ( filename);
			if(f.exists())  
				f.delete();

		}
	}
}
