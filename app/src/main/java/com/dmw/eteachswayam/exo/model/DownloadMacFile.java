package com.dmw.eteachswayam.exo.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadMacFile extends AsyncTask<String, String, String> {

    String   downloadpath;
    String   downloadUrl;
    Activity context;
    int          bufferSize         = 4096;
    byte[]       retVal             = null;
    InputStream  in                 = null;
    OutputStream fileos             = null;
    long         numBytesDownloaded = 0;
    HttpURLConnection urlConnection;
    private ProgressDialog dialog;
    boolean checkMacDownload=false;
    int mode;
    public DownloadMacFile( String downloadpath, String downloadUrl,
                            Activity context, int mode) {
        super();
        this.downloadpath = downloadpath;
        this.downloadUrl = downloadUrl;
        this.context = context;
        this.mode = mode;
    }

    @Override
    protected
    String doInBackground( String... params) {
        try {
            checkMacDownload=true;
            // opens a new url connection
            urlConnection = (HttpURLConnection ) new URL ( this.downloadUrl).openConnection();
            int fileLength = urlConnection.getContentLength();
            // open the file output stream for file writing


            fileos = new BufferedOutputStream ( new FileOutputStream ( this.downloadpath));

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

        }

        catch(FileNotFoundException e)
        {
            checkMacDownload=false;
            e.printStackTrace();
        }

        catch(IOException e) {
            Log.e( " ", e.getMessage());
            checkMacDownload=false;
            //downloadFinish=false;;
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
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(checkMacDownload)
        {
            String  orignalmacfileName       = Constant.INTERNAL_SDCARD + "mac.xml";
            String  renamefoundationFileName = Constant.INTERNAL_SDCARD + "foundation.xml";
            File    macFile                  =new File ( orignalmacfileName);
            File    foundationFile           =new File ( renamefoundationFileName);
            boolean checkmac                 =macFile.renameTo(foundationFile);
            if(checkmac)
            {
                Toast.makeText( context, "Downloaded settings file successfully", Toast.LENGTH_SHORT).show();
            }
        }
        dialog.dismiss();
        if(mode==Mode.DOWNLOAD_MAC_MODE){
            context.finish();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog=new ProgressDialog ( context);
        dialog.setMessage("Downloading setting file. Please wait...");
        dialog.setCancelable(false);
        dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }


}
