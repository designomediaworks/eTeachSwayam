package com.dmw.eteachswayam.exo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.fragment.drawer.AboutUsFragment;
import com.dmw.eteachswayam.exo.fragment.drawer.ActivateFragment;
import com.dmw.eteachswayam.exo.fragment.drawer.FeedBackFragment;
import com.dmw.eteachswayam.exo.fragment.drawer.MyProfileFragment;
import com.dmw.eteachswayam.exo.fragment.drawer.StorageFragment;
import com.dmw.eteachswayam.exo.fragment.drawer.SupportFragment;
import com.dmw.eteachswayam.exo.fragment.drawer.TabFragment;
import com.dmw.eteachswayam.exo.fragment.drawer.TermsConditionFragment;
import com.dmw.eteachswayam.exo.fragment.drawer.TransferContentFragment;
import com.dmw.eteachswayam.exo.fragment.drawer.UpdatesFragment;
import com.dmw.eteachswayam.exo.model.ChapterName;
import com.dmw.eteachswayam.exo.model.Constant;
import com.dmw.eteachswayam.exo.model.DeleteFile;
import com.dmw.eteachswayam.exo.model.LayoutXmlParser;
import com.dmw.eteachswayam.exo.model.RegBinClass;
import com.dmw.eteachswayam.exo.model.Rijndael;
import com.dmw.eteachswayam.exo.model.Scale;
import com.dmw.eteachswayam.exo.model.Setting;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class HomeActivity extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    DrawerLayout        mDrawerLayout;
    NavigationView      mNavigationView;
    FragmentManager     mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private static double MB_Available;

    public static ArrayList<String> arrayListexpiry;
    public static ArrayList<String> arrayListStrings;

    public static String                  serverdate;
    public static boolean                 ischeckValidUser;
    private       ArrayList <RegBinClass> binClassesreg;
    private       ArrayList<ChapterName>  arrayListNames;
    private       ArrayList<String>       activatedclassArrayList;
    private       String                  uptoString;
    public static ArrayList<Bitmap>       bitmapArray;
    String username;
    private       String      accountId;
    private       PackageInfo pInfo;
    public static String      version;
    public static String      version1;
    public static String      ListPreference;
    private       String      date;
    Handler handler;
    private static SharedPreferences prefs;
    private static SharedPreferences shared;

    private String PACKAGEPATHFORCALENDER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(bitmapArray==null)
            bitmapArray=new ArrayList<Bitmap> ();

        Constant.ScreenSize=new Scale ( this).screenSize;

        setContentView( R.layout.activity_main);

        // Share login Information with class
        SharedPreferences settings = getApplicationContext().getSharedPreferences( "username", 0);
        accountId =settings.getString("ACCOUNTID", "xxx");
        username = settings.getString("user","xxx");
        uptoString=Constant.INTERNAL_SDCARD+"/"+accountId+"/"+username;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        version1 = pInfo.versionName;

        //screenShot();                                    //commennted by me
        //setTitle("Select Play Demo");
        //setTitleColor(Color.BLUE);
        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        new Rijndael ();

        getPrefs();
        date = new SimpleDateFormat ( "yyyy-MM-dd").format( new Date ());

        // Check the registry inforamation of device and valid user
        ischecktab();


        initComponent();
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Initializing Toolbar and setting it as the actionbar
        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout ) findViewById( R.id.drawer);
        mNavigationView = (NavigationView ) findViewById( R.id.navigation_view);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.frame, new TabFragment ()).commit();

        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {

                    case R.id.itHome:
                        FragmentTransaction homeTransaction = mFragmentManager.beginTransaction();
                        homeTransaction.replace(R.id.frame, new TabFragment()).commit();
                        break;
                    case R.id.itMyProfile:
                        FragmentTransaction myProfileTransaction = mFragmentManager.beginTransaction();
                        myProfileTransaction.replace(R.id.frame, new MyProfileFragment ()).commit();
                        break;
                    case R.id.itActivate:
                        FragmentTransaction activateTransaction = mFragmentManager.beginTransaction();
                        activateTransaction.replace(R.id.frame, new ActivateFragment ()).commit();
                        break;
                    case R.id.itStorage:
                        FragmentTransaction storageTransaction = mFragmentManager.beginTransaction();
                        storageTransaction.replace(R.id.frame, new StorageFragment ()).commit();
                        break;
                    case R.id.itTrnsferContent:
                        FragmentTransaction transContentTransaction = mFragmentManager.beginTransaction();
                        transContentTransaction.replace(R.id.frame, new TransferContentFragment ()).commit();
                        break;
                    case R.id.itUpdates:
                        FragmentTransaction updatesTransaction = mFragmentManager.beginTransaction();
                        updatesTransaction.replace(R.id.frame, new UpdatesFragment ()).commit();
                        break;
                    case R.id.itFeedback:
                        FragmentTransaction feedBackTransaction = mFragmentManager.beginTransaction();
                        feedBackTransaction.replace(R.id.frame, new FeedBackFragment ()).commit();
                        break;
                    case R.id.itTandC:
                        FragmentTransaction tConTransaction = mFragmentManager.beginTransaction();
                        tConTransaction.replace(R.id.frame, new TermsConditionFragment ()).commit();
                        break;
                    case R.id.itSuppport:
                        FragmentTransaction supportTransaction = mFragmentManager.beginTransaction();
                        supportTransaction.replace(R.id.frame, new SupportFragment ()).commit();
                        break;
                    case R.id.itAboutUs:
                        FragmentTransaction aboutusTransaction = mFragmentManager.beginTransaction();
                        aboutusTransaction.replace(R.id.frame, new AboutUsFragment ()).commit();
                        break;
                    case R.id.itLogout:
                        SharedPreferences myPrefs = HomeActivity.this.getSharedPreferences( "Login", Context.MODE_PRIVATE);
                        myPrefs.edit().clear().commit();
                        Intent logoutIntent=new Intent ( HomeActivity.this, LoginActivity.class);

                        if(Constant.chapterArrayList!=null){
                            Constant.chapterArrayList.clear();
                        }
                        startActivity(logoutIntent);
                        HomeActivity.this.finish();
                    default:
                        break;
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle ( this, mDrawerLayout, toolbar, R.string.app_name,
                                                                          R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("deprecation")
    private void initComponent() {
        //fetchServerDetail();
        handler = new Handler ();
        prefs = PreferenceManager.getDefaultSharedPreferences( getBaseContext());
        ListPreference = prefs.getString("listPref", "digiGreen");
        shared = getSharedPreferences("path", 0);
        //	ListPreference1 = prefs.getString("listPref", "digiGreen");
        ganerateId();
        // gegisterEvent();

        Setting.determineStorageOptions();
        for(String s : Setting.mMounts){
            if(!username.equals("eteach"))
                if(!s.equals( Environment.getExternalStorageDirectory().getAbsolutePath())){
                    if(new File ( s + "/eteach/assets/Content/" + "xxxx1234/NEWNAME").exists()){
                        final String oldname    = s + "/eteach/assets/Content/" + "xxxx1234";
                        final String newName    = s + "/eteach/assets/Content/" + accountId;
                        final String newOldName = s + "/eteach/assets/Content/" + accountId + "/NEWNAME";
                        final String newNewName = s + "/eteach/assets/Content/" + accountId + "/" + username;

                        AlertDialog.Builder builder = new AlertDialog.Builder( HomeActivity.this, R.style.MyDialogTheme);
                        builder.setTitle("eTeach");
                        builder.setMessage("Do you want to associate the SD card to the application?");

                        String positiveText = getString( android.R.string.ok);
                        builder.setPositiveButton(positiveText,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick( DialogInterface dialog, int which) {
                                        new File ( oldname).renameTo( new File ( newName));
                                        new File ( newOldName).renameTo( new File ( newNewName));
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
                        dialog.setIcon(R.drawable.logo);
                        // display dialog
                        dialog.show();
                    }
                }
        }
    }

    private void ganerateId() {


        PACKAGEPATHFORCALENDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + Constant.algo( getPackageName());
        Constant.libpath=PACKAGEPATHFORCALENDER;

        getPrefs();

        getActivatedClasses();

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        createpath();
        new ServerDate().execute("");

    }

    public  void createpath(){
        File filepath =new File ( Environment.getExternalStorageDirectory().getAbsoluteFile().toString() + "/Android/Data/" + Constant.algo( this.getPackageName()));

        if(!filepath.exists()){
            new File ( Environment.getExternalStorageDirectory().getAbsoluteFile().toString() + "/Android/Data/" + Constant.algo( this.getPackageName())).mkdirs();
            try {
                filepath.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private   void getPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( getBaseContext());
        ListPreference = prefs.getString("listPref", "nr1");
        //	ListPreference1 = prefs.getString("listPref", "nr1");
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

    public static void checkmemory(Activity act) {
        StatFs stat_fs        = new StatFs ( Environment.getExternalStorageDirectory().getPath());
        double avail_sd_space = (double)stat_fs.getAvailableBlocks() *(double)stat_fs.getBlockSize();
        //double MB_Available = (avail_sd_space / 10485783);
        MB_Available = (avail_sd_space /1048576);
        // MB_Available=MB_Available-20;
        Log.d( "AvailableMB", "" + MB_Available);
        if(MB_Available<=100){
            SharedPreferences myPrefs = act.getSharedPreferences( "Login", Context.MODE_PRIVATE);
            myPrefs.edit().clear().commit();
            Toast.makeText( act, MB_Available + "MB is available and \n minimum required memory for this application is 100MB", Toast.LENGTH_LONG).show();
            Intent i =new Intent ( act, LoginActivity.class);
            act.startActivity(i);
            act.finish();
        }

    }


// Check registry inforamation of device
    private void ischecktab() {
        ischeckValidUser=true;
        binClassesreg=new ArrayList<RegBinClass> ();
        arrayListNames=new ArrayList<ChapterName> ();
        activatedclassArrayList=new ArrayList<String> ();
        String idHfortab =getUdid();
        new Rijndael();
        File encregFile =new File ( uptoString + "/REG.xml");
        File decRegFile =new File ( uptoString + "/REGD.xml");

        if(encregFile.exists()){
            try {
                FileInputStream  fileInputStreamreg  =new FileInputStream ( encregFile);
                FileOutputStream fileOutputStreamreg =new FileOutputStream ( decRegFile);
                Rijndael.EDR(fileInputStreamreg, fileOutputStreamreg, 2);
                binClassesreg=SubscriptionDetail.pareseRegFile(uptoString);
                if(decRegFile.exists())
                {
                    decRegFile.delete();
                }
                for (int i = 0; i < binClassesreg.size(); i++) {
                    String Uname =binClassesreg.get( i).getuName();
                    String Hdd   =binClassesreg.get( i).getHhid();
                    if(!Uname.equals(username))
                    {
                        ischeckValidUser=false;
                        break;
                    }
                    if(!Hdd.equals(idHfortab))
                    {
                        ischeckValidUser=false;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(ischeckValidUser){

            try {
                FileInputStream  fis =new FileInputStream ( Constant.INTERNAL_SDCARD + accountId + "/" + username + "/cbsechapter.xml");
                FileOutputStream fos =new FileOutputStream ( Constant.INTERNAL_SDCARD + accountId + "/" + username + "/cbsechapterD.xml");
                new Rijndael();
                Rijndael.EDR(fis, fos, 2);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            LayoutXmlParser layoutXmlParser =new LayoutXmlParser( Constant.INTERNAL_SDCARD + accountId + "/" + username + "/cbsechapterD.xml");
            arrayListNames= layoutXmlParser.parseChapter("",2);
            File deletechapterlistfile =new File ( Constant.INTERNAL_SDCARD + accountId + "/" + username + "/cbsechapterD.xml");
            if(deletechapterlistfile.exists())
            {
                deletechapterlistfile.delete();
            }
            for (int j = 0; j <arrayListNames.size(); j++) {
                if(arrayListNames.get(j).getFlag()==2)
                {
                    String nameclassactivated =arrayListNames.get( j).getKey();
                    if(!activatedclassArrayList.contains(nameclassactivated)){
                        String s1                =nameclassactivated.substring( 0, nameclassactivated.lastIndexOf( "/"));
                        String activatedclassreg =s1.substring( 0, s1.lastIndexOf( "/"));
                        if(!activatedclassArrayList.contains(activatedclassreg)){
                            activatedclassArrayList.add(activatedclassreg);
                        }
                    }
                }
            }

            for (int i = 0; i < binClassesreg.size(); i++) {

                String   regFilePath         =binClassesreg.get( i).getclasspath();
                String[] forchapterflag      =regFilePath.split( "-");
                String   parseforchapterflag = forchapterflag[0].trim() + "/" + forchapterflag[1].trim() + "/" + forchapterflag[2].trim().replace( " ", "_");
                if(!activatedclassArrayList.contains(parseforchapterflag))
                {
                    ischeckValidUser=false;
                    break;
                }
            }

            if(!activatedclassArrayList.isEmpty()){
                if(binClassesreg.isEmpty())
                {
                    ischeckValidUser=false;
                }
            }
            if(activatedclassArrayList.isEmpty()){
                if(!binClassesreg.isEmpty())
                {
                    ischeckValidUser=false;
                }
            }
        }else{
            ischeckValidUser=false;
        }
    }

    private void getActivatedClasses() {
        Setting.arrayListRegBinClasses=new ArrayList<RegBinClass> ();
        arrayListStrings=new ArrayList<String> ();
        arrayListexpiry=new ArrayList<String> ();

        try {
            Setting.arrayListRegBinClasses=SubscriptionDetail.pareseRegFile(uptoString);
            new DeleteFile ( uptoString + "/REGD.xml");
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < Setting.arrayListRegBinClasses.size(); i++) {
            String activatedclassName =Setting.arrayListRegBinClasses.get( i).getclasspath();
            Log.e( activatedclassName, activatedclassName);

            String expiryDate =Setting.arrayListRegBinClasses.get( i).getExpDate();
            Log.e( expiryDate, expiryDate);
            //			String[] parse=activatedclassName.split(" - ");
            //			String classNAme=parse[2];
            //arrayListStrings.add(activatedclassName.trim());
            String sysdatetab =getsystemDate();
            compare(expiryDate, sysdatetab,activatedclassName.trim());
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void compare( String expiry, String sys, String cls)
    {
        try {
            SimpleDateFormat sdf   = new SimpleDateFormat ( "dd/MM/yy");
            Date             date1 = sdf.parse( expiry);
            Date             date2 = sdf.parse( sys);
            if(date2.after(date1)){
                arrayListexpiry.add(cls);
            }
            else{
                arrayListStrings.add(cls);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @SuppressLint("SimpleDateFormat")
    public
    String getsystemDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat ( "dd/MM/yy");
        Date             date       = new Date ();
        String           sysdate    =dateFormat.format( date);
        return sysdate;
    }

    class ServerDate extends AsyncTask<String , String, String> {
        private HttpGet httpget;
        HttpResponse response;
        private String url;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        private String result;

        private JSONObject jObject;

        @Override
        protected
        String doInBackground( String... params) {
            // TODO Auto-generated method stub
            HttpEntity entity = null;
            url="http://"+Constant.WEBSERVER+"/designo_pro/ws_getserverdate.php?format=json";

            httpget = new HttpGet(url);
            try {
                response = httpClient.execute(httpget);
            } catch (ClientProtocolException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

            if (response != null) {
                entity = response.getEntity();
            }
            try {
                if (entity != null) {

                    InputStream instream = entity.getContent();
                    try {
                        result = convertStreamToString(instream);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    instream.close();
                }
            } catch (IllegalStateException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result!=null){
                try {
                    jObject = new JSONObject ( result);

                    JSONArray hrArray = jObject.getJSONArray( "posts");

                    JSONObject post = hrArray.getJSONObject( 0)
                            .getJSONObject("post");
                    serverdate=post.getString("Date");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

    }

    public static
    String convertStreamToString( InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader ( new InputStreamReader ( is));
        StringBuilder  sb     = new StringBuilder ();
        String         line   = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        is.close();

        return sb.toString();
    }

}
