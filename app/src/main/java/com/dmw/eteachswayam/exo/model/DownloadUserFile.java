package com.dmw.eteachswayam.exo.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.dmw.eteachswayam.exo.activity.HomeActivity;

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

public class DownloadUserFile extends AsyncTask<String, String, String>
{
    private Activity       act;
    private String         downloadPath;
    private ProgressDialog dialog;
    private String         urlpath;
    int          bufferSize         = 4096;
    byte[]       retVal             = null;
    InputStream  in                 = null;
    OutputStream fileos             = null;
    long         numBytesDownloaded = 0;
    HttpURLConnection urlConnection;
    private int mode;
    boolean flag=false;
    public DownloadUserFile( Activity act, String filePath, int mode){
        this.act=act;
        this.downloadPath=filePath+"/XMLFile.zip";
        this.urlpath="http://"+Constant.WEBSERVER+"/android/demoproject";
        this.mode=mode;
    }
    @Override
    protected
    String doInBackground( String... params) {
        try {
            // opens a new url connection
            flag=true;
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
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(flag){
            if(this.downloadPath.endsWith(".zip")){
                extractZipFiles(this.downloadPath);
            }
        }else{
            File f =new File ( this.downloadPath);
            if(f.exists())
            {
                f.delete();
            }
        }

        if(dialog!=null){
            dialog.dismiss();
        }
        if(flag){

            String urldemo = "http://" + Constant.WEBSERVER + "/school_bag/sysfiles/mac.xml";

            new DownloadMacFile(Constant.INTERNAL_SDCARD+"mac.xml", urldemo, act,Mode.DOWNLOAD_MAC_MODE).execute("");

        }
        if(mode==1){
            Intent intent =new Intent ( act, HomeActivity.class);
            act.startActivity(intent);
            act.finish();
        }else {
            //			FirstTimeRegistration act1=(FirstTimeRegistration)act;
            //			act1.new checkCoupanNumber().execute("");
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
    public  void callReceiver(String str){
        Intent intent = new Intent ();
        intent.setAction("com.manish.android.mybroadcast");
        intent.putExtra("Messsage",str);
        act.sendBroadcast(intent);
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
}


