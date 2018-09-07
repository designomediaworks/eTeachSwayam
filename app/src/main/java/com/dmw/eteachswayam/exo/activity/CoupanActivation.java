package com.dmw.eteachswayam.exo.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.model.ChapterName;
import com.dmw.eteachswayam.exo.model.Constant;
import com.dmw.eteachswayam.exo.model.CreateCalendarXML;
import com.dmw.eteachswayam.exo.model.DeleteFile;
import com.dmw.eteachswayam.exo.model.LayoutXmlParser;
import com.dmw.eteachswayam.exo.model.RegBinClass;
import com.dmw.eteachswayam.exo.model.Rijndael;
import com.dmw.eteachswayam.exo.model.Setting;
import com.dmw.eteachswayam.exo.model.XML;
import com.dmw.eteachswayam.exo.model.XMLParser;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHttpResponse;
import cz.msebera.android.httpclient.protocol.HTTP;

public class CoupanActivation extends AppCompatActivity {

    private EditText    coupanNumberEditText;
    private Button      submitCoupanNumberButton;
    private Button      exitFormButton;
    private TextView    classNameTextView;
    private RadioButton paymentGaytwayFormButton;
    private String      Coupon;

    private String      result;
    private String      uptoSubject;
    private String      std;
    private String      brd;
    private String      med;
    private String      chaptername;
    private RadioButton coupanFormButton;
    private String      coupanNumber;
    private String      className;
    private String      board;
    private String      uptoString;
    private ChapterName name = new ChapterName();
    private TextView class1;
    private TextView class2;
    private String   counter;
    //	private Button activateBlackAccountButton;
    //private EditText activateBlockAccountNumberEditText;
    private String   username;
    private String   accountId;
    private int      mode;

    private String PACKAGEPATHFORCALENDER;
    ArrayList<RegBinClass> arrayListBinClasses;
    ArrayList<RegBinClass> regcheckList;
    //	private TextView textView3;

    /**
     * Called when the activity is first created.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_coupan_activation);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = getIntent();
        arrayListBinClasses = new ArrayList<RegBinClass> ();
        mode = intent.getIntExtra("mode", 0);
        uptoSubject = intent.getStringExtra("UptoSubject");
        chaptername = intent.getStringExtra("chaptername");
        uptoString = intent.getStringExtra("uptoString");
        board = uptoSubject.substring(0, uptoSubject.lastIndexOf("/"));
        board = board.substring(0, board.lastIndexOf("/") + 1);
        SharedPreferences settings1 = getApplicationContext().getSharedPreferences( "username", 0);
        accountId = settings1.getString("ACCOUNTID", "xxx");
        username = settings1.getString("user", "xxx");

        String[] classs = board.split( "/");
        std = classs[2].toString();
        med = classs[1].toString();
        brd = classs[0].toString();
        //		String[] stdnumber=std.split("_");
        //		 classNameNumber=stdnumber[0].toString();

        PACKAGEPATHFORCALENDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + Constant.algo( getPackageName());

        this.setFinishOnTouchOutside(false);
        initComponent();
    }

    private void initComponent() {
        // TODO Auto-generated method stub
        ganerateId();
        registerEvent();
    }

    public void parseXml() {

        try {
            FileInputStream  fis = new FileInputStream ( Constant.INTERNAL_SDCARD + "/userxml.xml");
            FileOutputStream fos = new FileOutputStream ( Constant.INTERNAL_SDCARD + "/userxmlD.xml");
            new Rijndael();
            Rijndael.EDR( fis, fos, 2);

            new XMLParser(Constant.INTERNAL_SDCARD + "/userxmlD.xml", "Users", "BlockCount", "");
            counter = XMLParser.child1Value;
            new DeleteFile ( Constant.INTERNAL_SDCARD + "/userxmlD.xml");

            if (counter.equals("10")) {
                Intent intent = new Intent ( CoupanActivation.this, ActivateBlockAccount.class);
                intent.putExtra("accountId", accountId);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void ganerateId() {
        // TODO Auto-generated method stub

        class1 = (TextView ) findViewById( R.id.classNameTextView);
        class2 = (TextView ) findViewById( R.id.coupanNumberTextView);

        coupanNumberEditText = (EditText ) findViewById( R.id.coupanNumberEditText);
        submitCoupanNumberButton = (Button ) findViewById( R.id.submitButton);
        exitFormButton = (Button ) findViewById( R.id.exitButton);
        classNameTextView = (TextView ) findViewById( R.id.classTextView);
        paymentGaytwayFormButton = (RadioButton ) findViewById( R.id.paymentGatwayRadio);
        coupanFormButton = (RadioButton ) findViewById( R.id.coupanRadio);
        //textView3=(TextView) findViewById(R.id.textView3);
        //		paymentGaytwayFormButton.setEnabled(false);
     //   Coupon = coupanNumberEditText.getText().toString();
        String board = brd;
        if (brd.equals("STATE"))
            board = "MH BOARD";
        classNameTextView.setText(board + "->" + med + "->" + std);
        if (mode == 1) {
            submitCoupanNumberButton.setText("Renew Class");
        }
        parseXml();
        paymentGaytwayFormButton.setOnCheckedChangeListener(new OnCheckedChangeListener () {

            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    submitCoupanNumberButton.setEnabled(false);
                    coupanNumberEditText.setEnabled(false);
                    //new checkcharges().execute("");
                    displayDialogeForRetailarId();
                }
            }
        });
        coupanFormButton.setOnCheckedChangeListener(new OnCheckedChangeListener () {

            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    submitCoupanNumberButton.setEnabled(true);
                    coupanNumberEditText.setEnabled(true);
                }
            }
        });
        findViewById(R.id.relativeLayout1).setOnTouchListener(new OnTouchListener () {
            @Override
            public boolean onTouch( View v, MotionEvent event) {
                LoginActivity.hideSoftKeyboard(CoupanActivation.this);
                return false;
            }
        });

        try {
            arrayListBinClasses = SubscriptionDetail.pareseRegFile(uptoString);

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
    }

    private void registerEvent() {
        // TODO Auto-generated method stub

        submitCoupanNumberButton.setOnClickListener(new OnClickListener () {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stu
                if (validation()) {
                    if (isDataEnable()) {
                        Coupon = coupanNumberEditText.getText().toString();
                        getValue();
                        if (mode == 1) {
                        //    new checkCoupanNumber().execute("");
                            new TalkToServer().execute();

                        } else {
                            checkforAlreadyActivateClass();
                        }
                    } else
                        Toast.makeText( CoupanActivation.this, "Internet connection is not present", Toast.LENGTH_SHORT).show();
                }
            }


        });

        exitFormButton.setOnClickListener(new OnClickListener () {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alertDialogUpdate();
            }
        });
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

    private void getValue() {
        coupanNumber = coupanNumberEditText.getText().toString();
        className = classNameTextView.getText().toString();
    }

    private void checkforAlreadyActivateClass() {

        new AsyncTask<String, String, String> () {
            String hddid = getUdid();
            private ProgressDialog dialog1;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog1 = new ProgressDialog ( CoupanActivation.this);
                dialog1.setCancelable(false);
                dialog1.setMessage("Checking coupon number. Please wait...");
                dialog1.setProgressStyle( ProgressDialog.STYLE_SPINNER);
                dialog1.show();
            }

            @Override
            protected
            String doInBackground( String... params) {
                String doin_res = null;
                try {
                    String url = "http://" + Constant.WEBSERVER + "/designo_pro/ws_checkclass.php?hddid=" + hddid + "&brd=" + brd + "&std=" + std + "&med=" + med + "&format=json";

                    HttpGet httpget = new HttpGet( url);

                    HttpClient httpclient = new DefaultHttpClient ();
                    BasicHttpResponse httpResponse =
                            (BasicHttpResponse ) httpclient.execute( httpget);

                    HttpEntity     entity = httpResponse.getEntity();
                    InputStream    is     = entity.getContent();
                    BufferedReader reader = new BufferedReader ( new InputStreamReader ( is));
                    StringBuilder  sb     = new StringBuilder ();
                    String         line   = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    doin_res = sb.toString();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return doin_res;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try {
                    if (result != null) {
                        JSONObject jsonObject = new JSONObject ( result);

                        JSONArray hrArray = jsonObject.getJSONArray( "posts");

                        JSONObject post = hrArray.getJSONObject( 0)
                                .getJSONObject("post");

                        String result1 = post.getString( "result");
                        if (result1.equals("OK")) {
                            //Toast.makeText(Login.this, "You have Entered Wrong Username.\n Please Enter Valid Username. ", Toast.LENGTH_LONG).show();
                            dialog1.dismiss();
                          //  new checkCoupanNumber().execute("");
                            new TalkToServer().execute();
                        } else {
                            dialog1.dismiss();
                            alertDialogCheck();
                            //Toast.makeText(Login.this, "Your Password is Sent To Your e-mail.\n Please Check your email and Reenter Your Username and Password. ", Toast.LENGTH_LONG).show();

                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }

        }.execute("");
    }

    public void alertDialogUpdate() {
        final AlertDialog alterDiloge = new AlertDialog.Builder( this).create();
        alterDiloge.setIcon(R.drawable.logo);
        alterDiloge.setTitle("eTeach");
        alterDiloge.setMessage("Do you want to cancel product activation?");
        alterDiloge.setButton2("No", new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int which) {
                alterDiloge.dismiss();
            }
        });
        alterDiloge.setButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int which) {
                finish();
            }
        });
        alterDiloge.show();
    }

    public void alertDialogCheck() {
        final AlertDialog alterDiloge = new AlertDialog.Builder( this).create();
        alterDiloge.setIcon(R.drawable.logo);
        alterDiloge.setTitle("eTeach");
        alterDiloge.setMessage("This class is already activated on this Device by another user. \n  Do you want to continue?");
        alterDiloge.setButton2("No", new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int which) {
                alterDiloge.dismiss();
                finish();
            }
        });
        alterDiloge.setButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int which) {
                //finish();
             //   new checkCoupanNumber().execute("");
                new TalkToServer().execute();
            }
        });
        alterDiloge.show();
    }

    public boolean validation() {
        if (coupanNumberEditText.getText().toString() == null || coupanNumberEditText.getText().toString().isEmpty()) {
            Toast.makeText( this, "Please enter a coupon number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

  /*  class checkCoupanNumber extends AsyncTask<String, String, String> {

        private ProgressDialog dialog;
        private String str;
        private String AccId;
        private String hddid;
      //  private String Coupon;

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpPost httppost;
                FileInputStream fis = new FileInputStream(uptoString + "/mac.xml");
                FileOutputStream fos = new FileOutputStream(uptoString + "/macD.xml");
                new Rijndael();
                Rijndael.EDR(fis, fos, 2);

                new XMLParser(uptoString + "/macD.xml", "User", "AcountId", "");
                AccId = XMLParser.child1Value;
                new DeleteFile(uptoString + "/macD.xml");

                hddid = getUdid();
               // Coupon = coupanNumberEditText.getText().toString();
               // Coupon = coupanNumberEditText.getText().toString()

                //HttpPost httppost = new HttpPost("http://"+Constant.WEBSERVER+"/designo_pro/ws_checkcoupon_1.1.php?format=json" );
                if (mode == 1) {
                    httppost = new HttpPost("http://" + Constant.WEBSERVER + "/designo_pro/ws_renewcoupon.php?format=json");
                } else
                    httppost = new HttpPost("http://" + Constant.WEBSERVER + "/designo_pro/ws_checkcoupon.php?format=json");

                String xml = "<UserInfo><hddid>" + hddid + "</hddid><AccId>" + AccId + "</AccId><brd>" + brd + "</brd><med>" + med + "</med><std>" + std + "</std><Coupon>" + Coupon + "</Coupon></UserInfo>";
               Log.e("URLC",""+xml);
                StringEntity se = new StringEntity(xml, HTTP.UTF_8);

                se.setContentType("text/xml");
                httppost.setHeader("Content-Type", "application/soap+xml;charset=UTF-8");
                httppost.setEntity(se);

                HttpClient httpclient = new DefaultHttpClient();
                BasicHttpResponse httpResponse =
                        (BasicHttpResponse) httpclient.execute(httppost);

                HttpEntity entity = httpResponse.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                str = sb.toString();


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return str;
        }


        @Override
        protected void onPostExecute(String res) {
            try {
                if (res != null) {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray hrArray = jsonObject.getJSONArray("posts");

                    JSONObject post = hrArray.getJSONObject(0)
                            .getJSONObject("post");

                    String CouponNumber = post.getString("result");
                    String date = post.getString("Date");
                    String CalCounter = post.getString("CalCounter");
                    String Counter = post.getString("HitCounter");
                    String Crefnumber = post.getString("CouponRefNo");
                    String Ctopup = post.getString("topup");

                    try {
                        FileInputStream fis11 = new FileInputStream(Constant.INTERNAL_SDCARD + "/userxml.xml");
                        FileOutputStream fos11 = new FileOutputStream(Constant.INTERNAL_SDCARD + "/userxmlD.xml");
                        new Rijndael();
                        Rijndael.EDR(fis11, fos11, 2);

                        File f = new File(Constant.INTERNAL_SDCARD + "/userxml.xml");
                        f.delete();
                        new XML(Constant.INTERNAL_SDCARD + "/userxmlD.xml", "Users", "BlockCount", "");


                        new XMLParser(Constant.INTERNAL_SDCARD + "/userxmlD.xml", "Users", "BlockCount", "");
                        String Name = XMLParser.child1Value;


                        if (Name.equals("")) {
                            XML.addNodeToXML(Constant.INTERNAL_SDCARD + "/userxmlD.xml", "BlockCount", Counter);

                        } else {

                            XML.editByDOM(Constant.INTERNAL_SDCARD + "/userxmlD.xml", "Users", "BlockCount", Counter);
                        }


                        FileInputStream fis111 = new FileInputStream(Constant.INTERNAL_SDCARD + "/userxmlD.xml");
                        FileOutputStream fos111 = new FileOutputStream(Constant.INTERNAL_SDCARD + "/userxml.xml");
                        new Rijndael();
                        Rijndael.EDR(fis111, fos111, 1);

                        File f1 = new File(Constant.INTERNAL_SDCARD + "/userxmlD.xml");
                        f1.delete();


                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                    if (Counter.equals("10")) {

                        Intent intent = new Intent(CoupanActivation.this, ActivateBlockAccount.class);
                        intent.putExtra("accountId", accountId);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();

                        //						coupanNumberEditText.setEnabled(false);
                        //						submitCoupanNumberButton.setEnabled(false);
                        //						exitFormButton.setEnabled(false);
                        //						classNameTextView.setEnabled(false);
                        //						paymentGaytwayFormButton.setEnabled(false);
                        //						coupanFormButton.setEnabled(false);
                        //						classNameTextView.setEnabled(false);
                        //						paymentGaytwayFormButton.setEnabled(false);
                        //						class1.setEnabled(false);
                        //						class2.setEnabled(false);
                        //activateBlackAccountButton.setEnabled(true);
                        //activateBlackAccountButton.setVisibility(View.VISIBLE);
                        //activateBlockAccountNumberEditText.setVisibility(View.VISIBLE);
                        //textView3.setVisibility(View.VISIBLE);
                        //activateBlockAccountNumberEditText.setEnabled(true);

                        //new XML(Constant.INTERNAL_SDCARD+"/macD.xml","User","AcountId","Counter");
                        Toast.makeText(CoupanActivation.this, "Your account is blocked \n Please check your mail to unblock your account.", Toast.LENGTH_SHORT).show();
                    }

                    if (CouponNumber.equals("This class is not expired or you have selected wrong class for renewal. Please check your system date and time")) {
                        Toast.makeText(CoupanActivation.this, "This class is not expired or you have selected wrong class for renewal.\nPlease check your system date and time", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    if (CouponNumber.equals("coupon not valid")) {
                        //Toast.makeText(CoupanActivation.this,"Coupon no is Invalid", Toast.LENGTH_SHORT).show();
                        int change = Integer.parseInt(Counter);
                        int total = 10 - change;
                        Toast.makeText(CoupanActivation.this, "Coupon no. is invalid, " + total + " more attempts remaining", Toast.LENGTH_LONG).show();
                        //Toast.makeText(ActivateCouponHome.this,"Device Has Been Changed.\nPlease try to activate on Correct Device", Toast.LENGTH_SHORT).show();
                        coupanNumberEditText.setText("");
                    } else if (CouponNumber.equals("not matched")) {
                        int change = Integer.parseInt(Counter);
                        int total = 10 - change;
                        Toast.makeText(CoupanActivation.this, "Coupon no. is invalid, " + total + " more attempts remaining", Toast.LENGTH_LONG).show();
                        ;
                        //Toast.makeText(CoupanActivation.this,"Device Has Been Changed.\nPlease try to activate on Correct Device", Toast.LENGTH_SHORT).show();
                        coupanNumberEditText.setText("");
                    } else if (CouponNumber.equals("try again")) {
                        Toast.makeText(CoupanActivation.this, "Something went wrong\n Please try again.", Toast.LENGTH_SHORT).show();
                        coupanNumberEditText.setText("");
                    } else if (CouponNumber.equals(std + " is already activated")) {
                        Toast.makeText(CoupanActivation.this, "This standard is already activated.", Toast.LENGTH_SHORT).show();
                        coupanNumberEditText.setText("");
                    } else if (CouponNumber.equals("1")) {
                        if (!CalCounter.equals("0")) {

                            for (int i = 0; i < arrayListBinClasses.size(); i++) {
                                String refNumber = arrayListBinClasses.get(i).getCourefnumber();
                                String classPathspo = arrayListBinClasses.get(i).getclasspath();
                                if (!refNumber.equals("")) {
                                    if (!refNumber.equals("null")) {
                                        refreshspo(classPathspo);
                                    }
                                }
                            }

                            if (mode == 1) {
                                if (new File(PACKAGEPATHFORCALENDER + "/" + algo(accountId + "/" + username + "/" + brd + "/" + med + "/" + std) + ".xml").exists()) {
                                    new File(PACKAGEPATHFORCALENDER + "/" + algo(accountId + "/" + username + "/" + brd + "/" + med + "/" + std) + ".xml").delete();
                                }
                                new CreateCalendarXML(addDate1(date), brd, med, std, Environment.getExternalStorageDirectory().getAbsolutePath(), PACKAGEPATHFORCALENDER, CoupanActivation.this, Integer.parseInt(CalCounter), hddid, username, accountId);
                            } else {
                                new CreateCalendarXML(addDate1(date), brd, med, std, Environment.getExternalStorageDirectory().getAbsolutePath(), PACKAGEPATHFORCALENDER, CoupanActivation.this, Integer.parseInt(CalCounter), hddid, username, accountId);
                            }


                            coupanNumberEditText.setText("");
                            changeChapterFlag(board);
                            String hddidfortab = getUdid();
                            createSubscriptionFile(brd, med, std, Integer.parseInt(CalCounter), addDate(date), Coupon, Crefnumber, username, hddidfortab, mode);
                            new verifyCoupan(Constant.WEBSERVE + "/designo_pro/verify_coupon.php?usrnm=" + username + "&brd=" + brd + "&cls=" + std + "&med=" + med + "&coupon=" + Coupon + "&format=xml").execute("");
                            if (!Ctopup.equals("0")) {
                                Toast.makeText(CoupanActivation.this, "Class Activated.\nCongratulation!\nYour subscription validity has increased to " + Ctopup + " now.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CoupanActivation.this, "Class Activated", Toast.LENGTH_SHORT).show();
                            }
                            try {
                                FileInputStream fis = new FileInputStream(uptoString + "/cbsechapter.xml");
                                FileOutputStream fos = new FileOutputStream(uptoString + "/cbsechapterD.xml");

                                new Rijndael();
                                Rijndael.EDR(fis, fos, 2);

                                LayoutXmlParser parser = new LayoutXmlParser(uptoString + "/cbsechapterD.xml");
                                //			getDownloadChapter(uptoString+"/"+uptoSubject);
                                Constant.chapterArrayList = parser.parseChapter(uptoSubject, 1);

                                new DeleteFile(uptoString + "/cbsechapterD.xml");
                                callReceiver("OK");
                                finish();
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            //calDate = calDate.substring(0, 2)+"-"+calDate.substring(2, 4)+"-"+calDate.substring(4,6);
                        }
                    } else {
                        Toast.makeText(CoupanActivation.this, CouponNumber, Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            } catch (JSONException e) {
                callReceiver("ERROR");
                e.printStackTrace();
            }


            dialog.dismiss();
            super.onPostExecute(result);
        }


        private String algo(String value) {

            //CharSequence value[];
            String SentToSer = "";
            String GetFrmSer = "";
            int count = 0;
            value = value.toUpperCase();
            try {
                for (int i = 0; i < value.length(); i++) {
                    if (value.charAt(i) == '0') {
                        SentToSer += '2';
                        GetFrmSer += '7';
                    } else if (value.charAt(i) == '1') {
                        SentToSer += '8';
                        GetFrmSer += '4';
                    } else if (value.charAt(i) == '2') {
                        SentToSer += '5';
                        GetFrmSer += '8';
                    } else if (value.charAt(i) == '3') {
                        SentToSer += '9';
                        GetFrmSer += '1';
                    } else if (value.charAt(i) == '4') {
                        SentToSer += '0';
                        GetFrmSer += '9';
                    } else if (value.charAt(i) == '5') {
                        SentToSer += '3';
                        GetFrmSer += '2';
                    } else if (value.charAt(i) == '6') {
                        SentToSer += '7';
                        GetFrmSer += '0';
                    } else if (value.charAt(i) == '7') {
                        SentToSer += '1';
                        GetFrmSer += '3';
                    } else if (value.charAt(i) == '8') {
                        SentToSer += '6';
                        GetFrmSer += '5';
                    } else if (value.charAt(i) == '9') {
                        SentToSer += '4';
                        GetFrmSer += '6';
                    } else if (value.charAt(i) == 'A') {
                        SentToSer += 'S';
                        GetFrmSer += 'K';
                    } else if (value.charAt(i) == 'B') {
                        SentToSer += 'G';
                        GetFrmSer += 'E';
                    } else if (value.charAt(i) == 'C') {
                        SentToSer += 'I';
                        GetFrmSer += 'T';
                    } else if (value.charAt(i) == 'D') {
                        SentToSer += 'M';
                        GetFrmSer += 'F';
                    } else if (value.charAt(i) == 'E') {
                        SentToSer += 'L';
                        GetFrmSer += 'B';
                    } else if (value.charAt(i) == 'F') {
                        SentToSer += 'R';
                        GetFrmSer += 'Q';
                    } else if (value.charAt(i) == 'G') {
                        SentToSer += 'U';
                        GetFrmSer += 'L';
                    } else if (value.charAt(i) == 'H') {
                        SentToSer += 'A';
                        GetFrmSer += 'C';
                    } else if (value.charAt(i) == 'I') {
                        SentToSer += 'P';
                        GetFrmSer += 'N';
                    } else if (value.charAt(i) == 'J') {
                        SentToSer += 'N';
                        GetFrmSer += 'Y';
                    } else if (value.charAt(i) == 'K') {
                        SentToSer += 'W';
                        GetFrmSer += 'I';
                    } else if (value.charAt(i) == 'L') {
                        SentToSer += 'Y';
                        GetFrmSer += 'R';
                    } else if (value.charAt(i) == 'M') {
                        SentToSer += 'B';
                        GetFrmSer += 'H';
                    } else if (value.charAt(i) == 'N') {
                        SentToSer += 'K';
                        GetFrmSer += 'J';
                    } else if (value.charAt(i) == 'O') {
                        SentToSer += 'Z';
                        GetFrmSer += 'X';
                    } else if (value.charAt(i) == 'P') {
                        SentToSer += 'C';
                        GetFrmSer += 'Z';
                    } else if (value.charAt(i) == 'Q') {
                        SentToSer += 'F';
                        GetFrmSer += 'A';
                    } else if (value.charAt(i) == 'R') {
                        SentToSer += 'D';
                        GetFrmSer += 'V';
                    } else if (value.charAt(i) == 'S') {
                        SentToSer += 'J';
                        GetFrmSer += 'O';
                    } else if (value.charAt(i) == 'T') {
                        SentToSer += 'Q';
                        GetFrmSer += 'M';
                    } else if (value.charAt(i) == 'U') {
                        SentToSer += 'V';
                        GetFrmSer += 'S';
                    } else if (value.charAt(i) == 'V') {
                        SentToSer += 'H';
                        GetFrmSer += 'P';
                    } else if (value.charAt(i) == 'W') {
                        SentToSer += 'E';
                        GetFrmSer += 'D';
                    } else if (value.charAt(i) == 'X') {
                        SentToSer += 'O';
                        GetFrmSer += 'U';
                    } else if (value.charAt(i) == 'Y') {
                        SentToSer += 'X';
                        GetFrmSer += 'G';
                    } else if (value.charAt(i) == 'Z') {
                        SentToSer += 'T';
                        GetFrmSer += 'W';
                    } else if (value.charAt(i) == '_') {
                        SentToSer += '_';
                        GetFrmSer += '_';
                    } else {
                        SentToSer += '-';
                        GetFrmSer += '-';
                    }


                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Conversion Error", "Error encountered while converting.....");
            }
            int SentLen = SentToSer.length();
            int GetLen = GetFrmSer.length();
            //         String fullMsg = Cls_Global_Var.AccountId + " " + SentToSer;
            //         label4.Text = "Send eteach ACT " + fullMsg + " to 57333 \nAnd press 'Activate' button if you have recieved activation key.";
            return SentToSer;
        }

        public void callReceiver(String str) {

            Intent intent = new Intent();
            intent.setAction("com.manish.android.mybroadcast");
            intent.putExtra("Messsage", str);
            sendBroadcast(intent);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog = new ProgressDialog(CoupanActivation.this);
            dialog.setCancelable(false);
            dialog.setMessage("Checking coupon number. Please wait...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
          //  super.onPreExecute();
        }

    }*/


    public class TalkToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog dialog;
        private String         str;
        private String         AccId;
        private String         hddid;
        //  private String Coupon;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog ( CoupanActivation.this);
            dialog.setCancelable(false);
            dialog.setMessage("Checking coupon number. Please wait...");
            dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
            dialog.show();
            super.onPreExecute();
        }


        @Override
        protected
        String doInBackground( Void... params) {

            try {
                HttpPost         httppost;
                FileInputStream  fis = new FileInputStream ( uptoString + "/mac.xml");
                FileOutputStream fos = new FileOutputStream ( uptoString + "/macD.xml");
                new Rijndael();
                Rijndael.EDR(fis, fos, 2);

                new XMLParser(uptoString + "/macD.xml", "User", "AcountId", "");
                AccId = XMLParser.child1Value;
                new DeleteFile(uptoString + "/macD.xml");

                hddid = getUdid();
                // Coupon = coupanNumberEditText.getText().toString();
                // Coupon = coupanNumberEditText.getText().toString()

                //HttpPost httppost = new HttpPost("http://"+Constant.WEBSERVER+"/designo_pro/ws_checkcoupon_1.1.php?format=json" );
                if (mode == 1) {
                    httppost = new HttpPost("http://" + Constant.WEBSERVER + "/designo_pro/ws_renewcoupon.php?format=json");
                } else
                    httppost = new HttpPost("http://" + Constant.WEBSERVER + "/designo_pro/ws_checkcoupon.php?format=json");

                String xml = "<UserInfo><hddid>" + hddid + "</hddid><AccId>" + AccId + "</AccId><brd>" + brd + "</brd><med>" + med + "</med><std>" + std + "</std><Coupon>" + Coupon + "</Coupon></UserInfo>";
                Log.e( "URLC", "" + xml);
                StringEntity se = new StringEntity ( xml, HTTP.UTF_8);

                se.setContentType("text/xml");
                httppost.setHeader("Content-Type", "application/soap+xml;charset=UTF-8");
                httppost.setEntity(se);

                HttpClient httpclient = new DefaultHttpClient();
                BasicHttpResponse httpResponse =
                        (BasicHttpResponse) httpclient.execute(httppost);

                HttpEntity     entity = httpResponse.getEntity();
                InputStream    is     = entity.getContent();
                BufferedReader reader = new BufferedReader ( new InputStreamReader ( is));
                StringBuilder  sb     = new StringBuilder ();
                String         line   = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                str = sb.toString();


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return str;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject ( result);
                    JSONArray  hrArray    = jsonObject.getJSONArray( "posts");

                    JSONObject post = hrArray.getJSONObject( 0)
                            .getJSONObject("post");

                    String CouponNumber = post.getString( "result");
                    String date         = post.getString( "Date");
                    String CalCounter   = post.getString( "CalCounter");
                    String Counter      = post.getString( "HitCounter");
                    String Crefnumber   = post.getString( "CouponRefNo");
                    String Ctopup       = post.getString( "topup");

                    try {
                        FileInputStream  fis11 = new FileInputStream ( Constant.INTERNAL_SDCARD + "/userxml.xml");
                        FileOutputStream fos11 = new FileOutputStream ( Constant.INTERNAL_SDCARD + "/userxmlD.xml");
                        new Rijndael();
                        Rijndael.EDR(fis11, fos11, 2);

                        File f = new File ( Constant.INTERNAL_SDCARD + "/userxml.xml");
                        f.delete();
                        new XML(Constant.INTERNAL_SDCARD + "/userxmlD.xml", "Users", "BlockCount", "");


                        new XMLParser(Constant.INTERNAL_SDCARD + "/userxmlD.xml", "Users", "BlockCount", "");
                        String Name = XMLParser.child1Value;


                        if (Name.equals("")) {
                            XML.addNodeToXML(Constant.INTERNAL_SDCARD + "/userxmlD.xml", "BlockCount", Counter);

                        } else {

                            XML.editByDOM( Constant.INTERNAL_SDCARD + "/userxmlD.xml", "Users", "BlockCount", Counter);
                        }


                        FileInputStream  fis111 = new FileInputStream ( Constant.INTERNAL_SDCARD + "/userxmlD.xml");
                        FileOutputStream fos111 = new FileOutputStream ( Constant.INTERNAL_SDCARD + "/userxml.xml");
                        new Rijndael();
                        Rijndael.EDR(fis111, fos111, 1);

                        File f1 = new File ( Constant.INTERNAL_SDCARD + "/userxmlD.xml");
                        f1.delete();


                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                    if (Counter.equals("10")) {

                        Intent intent = new Intent ( CoupanActivation.this, ActivateBlockAccount.class);
                        intent.putExtra("accountId", accountId);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();

                        Toast.makeText( CoupanActivation.this, "Your account is blocked \n Please check your mail to unblock your account.", Toast.LENGTH_SHORT).show();
                    }

                    if (CouponNumber.equals("This class is not expired or you have selected wrong class for renewal. Please check your system date and time")) {
                        Toast.makeText( CoupanActivation.this, "This class is not expired or you have selected wrong class for renewal.\nPlease check your system date and time", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    if (CouponNumber.equals("coupon not valid")) {
                        //Toast.makeText(CoupanActivation.this,"Coupon no is Invalid", Toast.LENGTH_SHORT).show();
                        int change = Integer.parseInt( Counter);
                        int total = 10 - change;
                        Toast.makeText( CoupanActivation.this, "Coupon no. is invalid, " + total + " more attempts remaining", Toast.LENGTH_LONG).show();
                        //Toast.makeText(ActivateCouponHome.this,"Device Has Been Changed.\nPlease try to activate on Correct Device", Toast.LENGTH_SHORT).show();
                        coupanNumberEditText.setText("");
                    } else if (CouponNumber.equals("not matched")) {
                        int change = Integer.parseInt( Counter);
                        int total = 10 - change;
                        Toast.makeText( CoupanActivation.this, "Coupon no. is invalid, " + total + " more attempts remaining", Toast.LENGTH_LONG).show();
                        ;
                        //Toast.makeText(CoupanActivation.this,"Device Has Been Changed.\nPlease try to activate on Correct Device", Toast.LENGTH_SHORT).show();
                        coupanNumberEditText.setText("");
                    } else if (CouponNumber.equals("try again")) {
                        Toast.makeText( CoupanActivation.this, "Something went wrong\n Please try again.", Toast.LENGTH_SHORT).show();
                        coupanNumberEditText.setText("");
                    } else if (CouponNumber.equals(std + " is already activated")) {
                        Toast.makeText( CoupanActivation.this, "This standard is already activated.", Toast.LENGTH_SHORT).show();
                        coupanNumberEditText.setText("");
                    } else if (CouponNumber.equals("1")) {
                        if (!CalCounter.equals("0")) {

                            for (int i = 0; i < arrayListBinClasses.size(); i++) {
                                String refNumber    = arrayListBinClasses.get( i).getCourefnumber();
                                String classPathspo = arrayListBinClasses.get( i).getclasspath();
                                if (!refNumber.equals("")) {
                                    if (!refNumber.equals("null")) {
                                        refreshspo(classPathspo);
                                    }
                                }
                            }

                            if (mode == 1) {
                                if (new File ( PACKAGEPATHFORCALENDER + "/" + algo( accountId + "/" + username + "/" + brd + "/" + med + "/" + std) + ".xml").exists()) {
                                    new File ( PACKAGEPATHFORCALENDER + "/" + algo( accountId + "/" + username + "/" + brd + "/" + med + "/" + std) + ".xml").delete();
                                }
                                new CreateCalendarXML( addDate1(date), brd, med, std, Environment.getExternalStorageDirectory().getAbsolutePath(), PACKAGEPATHFORCALENDER, CoupanActivation.this, Integer.parseInt( CalCounter), hddid, username, accountId);
                            } else {
                                new CreateCalendarXML ( addDate1( date), brd, med, std, Environment.getExternalStorageDirectory().getAbsolutePath(), PACKAGEPATHFORCALENDER, CoupanActivation.this, Integer.parseInt( CalCounter), hddid, username, accountId);
                            }


                            coupanNumberEditText.setText("");
                            changeChapterFlag(board);
                            String hddidfortab = getUdid();
                            createSubscriptionFile( brd, med, std, Integer.parseInt( CalCounter), addDate( date), Coupon, Crefnumber, username, hddidfortab, mode);
                            new verifyCoupan(Constant.WEBSERVE + "/designo_pro/verify_coupon.php?usrnm=" + username + "&brd=" + brd + "&cls=" + std + "&med=" + med + "&coupon=" + Coupon + "&format=xml").execute("");
                            if (!Ctopup.equals("0")) {
                                Toast.makeText( CoupanActivation.this, "Class Activated.\nCongratulation!\nYour subscription validity has increased to " + Ctopup + " now.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText( CoupanActivation.this, "Class Activated", Toast.LENGTH_SHORT).show();
                            }
                            try {
                                FileInputStream  fis = new FileInputStream ( uptoString + "/cbsechapter.xml");
                                FileOutputStream fos = new FileOutputStream ( uptoString + "/cbsechapterD.xml");

                                new Rijndael();
                                Rijndael.EDR(fis, fos, 2);

                                LayoutXmlParser parser = new LayoutXmlParser ( uptoString + "/cbsechapterD.xml");
                                //			getDownloadChapter(uptoString+"/"+uptoSubject);
                                Constant.chapterArrayList = parser.parseChapter(uptoSubject, 1);

                                new DeleteFile(uptoString + "/cbsechapterD.xml");
                                callReceiver("OK");
                                finish();
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            //calDate = calDate.substring(0, 2)+"-"+calDate.substring(2, 4)+"-"+calDate.substring(4,6);
                        }
                    } else {
                        Toast.makeText( CoupanActivation.this, CouponNumber, Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            } catch (JSONException e) {
                callReceiver("ERROR");
                e.printStackTrace();
            }


            dialog.dismiss();
            super.onPostExecute(result);
        }

        private
        String algo( String value) {

            //CharSequence value[];
            String SentToSer = "";
            String GetFrmSer = "";
            int    count     = 0;
            value = value.toUpperCase();
            try {
                for (int i = 0; i < value.length(); i++) {
                    if (value.charAt(i) == '0') {
                        SentToSer += '2';
                        GetFrmSer += '7';
                    } else if (value.charAt(i) == '1') {
                        SentToSer += '8';
                        GetFrmSer += '4';
                    } else if (value.charAt(i) == '2') {
                        SentToSer += '5';
                        GetFrmSer += '8';
                    } else if (value.charAt(i) == '3') {
                        SentToSer += '9';
                        GetFrmSer += '1';
                    } else if (value.charAt(i) == '4') {
                        SentToSer += '0';
                        GetFrmSer += '9';
                    } else if (value.charAt(i) == '5') {
                        SentToSer += '3';
                        GetFrmSer += '2';
                    } else if (value.charAt(i) == '6') {
                        SentToSer += '7';
                        GetFrmSer += '0';
                    } else if (value.charAt(i) == '7') {
                        SentToSer += '1';
                        GetFrmSer += '3';
                    } else if (value.charAt(i) == '8') {
                        SentToSer += '6';
                        GetFrmSer += '5';
                    } else if (value.charAt(i) == '9') {
                        SentToSer += '4';
                        GetFrmSer += '6';
                    } else if (value.charAt(i) == 'A') {
                        SentToSer += 'S';
                        GetFrmSer += 'K';
                    } else if (value.charAt(i) == 'B') {
                        SentToSer += 'G';
                        GetFrmSer += 'E';
                    } else if (value.charAt(i) == 'C') {
                        SentToSer += 'I';
                        GetFrmSer += 'T';
                    } else if (value.charAt(i) == 'D') {
                        SentToSer += 'M';
                        GetFrmSer += 'F';
                    } else if (value.charAt(i) == 'E') {
                        SentToSer += 'L';
                        GetFrmSer += 'B';
                    } else if (value.charAt(i) == 'F') {
                        SentToSer += 'R';
                        GetFrmSer += 'Q';
                    } else if (value.charAt(i) == 'G') {
                        SentToSer += 'U';
                        GetFrmSer += 'L';
                    } else if (value.charAt(i) == 'H') {
                        SentToSer += 'A';
                        GetFrmSer += 'C';
                    } else if (value.charAt(i) == 'I') {
                        SentToSer += 'P';
                        GetFrmSer += 'N';
                    } else if (value.charAt(i) == 'J') {
                        SentToSer += 'N';
                        GetFrmSer += 'Y';
                    } else if (value.charAt(i) == 'K') {
                        SentToSer += 'W';
                        GetFrmSer += 'I';
                    } else if (value.charAt(i) == 'L') {
                        SentToSer += 'Y';
                        GetFrmSer += 'R';
                    } else if (value.charAt(i) == 'M') {
                        SentToSer += 'B';
                        GetFrmSer += 'H';
                    } else if (value.charAt(i) == 'N') {
                        SentToSer += 'K';
                        GetFrmSer += 'J';
                    } else if (value.charAt(i) == 'O') {
                        SentToSer += 'Z';
                        GetFrmSer += 'X';
                    } else if (value.charAt(i) == 'P') {
                        SentToSer += 'C';
                        GetFrmSer += 'Z';
                    } else if (value.charAt(i) == 'Q') {
                        SentToSer += 'F';
                        GetFrmSer += 'A';
                    } else if (value.charAt(i) == 'R') {
                        SentToSer += 'D';
                        GetFrmSer += 'V';
                    } else if (value.charAt(i) == 'S') {
                        SentToSer += 'J';
                        GetFrmSer += 'O';
                    } else if (value.charAt(i) == 'T') {
                        SentToSer += 'Q';
                        GetFrmSer += 'M';
                    } else if (value.charAt(i) == 'U') {
                        SentToSer += 'V';
                        GetFrmSer += 'S';
                    } else if (value.charAt(i) == 'V') {
                        SentToSer += 'H';
                        GetFrmSer += 'P';
                    } else if (value.charAt(i) == 'W') {
                        SentToSer += 'E';
                        GetFrmSer += 'D';
                    } else if (value.charAt(i) == 'X') {
                        SentToSer += 'O';
                        GetFrmSer += 'U';
                    } else if (value.charAt(i) == 'Y') {
                        SentToSer += 'X';
                        GetFrmSer += 'G';
                    } else if (value.charAt(i) == 'Z') {
                        SentToSer += 'T';
                        GetFrmSer += 'W';
                    } else if (value.charAt(i) == '_') {
                        SentToSer += '_';
                        GetFrmSer += '_';
                    } else {
                        SentToSer += '-';
                        GetFrmSer += '-';
                    }


                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e( "Conversion Error", "Error encountered while converting.....");
            }
            int SentLen = SentToSer.length();
            int GetLen = GetFrmSer.length();
            //         String fullMsg = Cls_Global_Var.AccountId + " " + SentToSer;
            //         label4.Text = "Send eteach ACT " + fullMsg + " to 57333 \nAnd press 'Activate' button if you have recieved activation key.";
            return SentToSer;
        }

        public void callReceiver(String str) {

            Intent intent = new Intent ();
            intent.setAction("com.manish.android.mybroadcast");
            intent.putExtra("Messsage", str);
            sendBroadcast(intent);
        }
    }

    private void createSubscriptionFile( String brd, String med, String std, int day, String date, String coupon, String coupanrefnumber, String usernameforreg, String hddidfortabreg, int modereg) {


        regcheckList = new ArrayList<RegBinClass> ();

        if (modereg == 1) {
            try {
                FileInputStream  fis = new FileInputStream ( uptoString + "/REG.xml");
                FileOutputStream fos = new FileOutputStream ( uptoString + "/REGD.xml");
                new Rijndael();
                Rijndael.EDR(fis, fos, 2);
                new DeleteFile(uptoString + "/REG.xml");

                String classNamefordelete = brd + " - " + med + " - " + std.replace( "_", " ");

                Setting.deleteTagforClassName( classNamefordelete, uptoString + "/REGD.xml");

                FileInputStream  fis1 = new FileInputStream ( uptoString + "/REGD.xml");
                FileOutputStream fos1 = new FileOutputStream ( uptoString + "/REG.xml");
                new Rijndael();
                Rijndael.EDR(fis1, fos1, 1);
                new DeleteFile(uptoString + "/REGD.xml");
                try {
                    regcheckList = SubscriptionDetail.pareseRegFile(uptoString);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (regcheckList.isEmpty()) {
                    File fileregdelete = new File ( uptoString + "/REG.xml");
                    if (fileregdelete.exists()) {
                        fileregdelete.delete();
                    }
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        File   Subscription = new File ( uptoString + "/REG.xml");
        String calDateNxt   = EXPDATE( date, day);
        if (Subscription.exists()) {
            try {
                FileInputStream  fis           = new FileInputStream ( uptoString + "/REG.xml");
                FileOutputStream fos           = new FileOutputStream ( uptoString + "/REGD.xml");
                File             Subscription1 = new File ( uptoString + "/REGD.xml");
                new Rijndael();
                Rijndael.EDR(fis, fos, 2);
                new DeleteFile(uptoString + "/REG.xml");

                RandomAccessFile temp1 = new RandomAccessFile ( Subscription1, "rw");
                String           s     = "<item><ClassPath>" + brd + " - " + med + " - " + std.replace( "_", " ") + "</ClassPath><ActDate>" + date.substring( 4, 6) + "/" + date.substring( 2, 4) + "/" + date.substring( 0, 2) + "</ActDate><ExpDate>" + calDateNxt.substring( 4, 6) + "/" + calDateNxt.substring( 2, 4) + "/" + calDateNxt.substring( 0, 2) + "</ExpDate><CouponNo>" + coupanNumber + "</CouponNo><CouRefNumber>" + coupanrefnumber + "</CouRefNumber><reguser>" + usernameforreg + "</reguser><reghddid>" + hddidfortabreg + "</reghddid></item></Reg>";
                temp1.seek(Subscription1.length() - "</Reg>".length());
                temp1.write(s.getBytes());
                temp1.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {

                File temp2 = new File ( uptoString + "/REGD.xml");
                temp2.createNewFile();
                RandomAccessFile temp1 = new RandomAccessFile ( temp2, "rw");

                String s = "<Reg><item><ClassPath>" + brd + " - " + med + " - " + std.replace( "_", " ") + "</ClassPath><ActDate>" + date.substring( 4, 6) + "/" + date.substring( 2, 4) + "/" + date.substring( 0, 2) + "</ActDate><ExpDate>" + calDateNxt.substring( 4, 6) + "/" + calDateNxt.substring( 2, 4) + "/" + calDateNxt.substring( 0, 2) + "</ExpDate><CouponNo>" + coupanNumber + "</CouponNo><CouRefNumber>" + coupanrefnumber + "</CouRefNumber><reguser>" + usernameforreg + "</reguser><reghddid>" + hddidfortabreg + "</reghddid></item></Reg>";
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
            new DeleteFile(uptoString + "/REGD.xml");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    String EXPDATE( String dt, int j) {

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

    public static void writeFile( String i, String path, String fileName) {
        try {
            File       gpxfile = new File ( path, fileName);
            FileWriter writer  = new FileWriter ( gpxfile);
            writer.append(i);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static
    String readFile( String path, String fileName) {
        File file = new File ( path, fileName);

        //Read text from file
        StringBuilder text = new StringBuilder ();

        try {
            BufferedReader br = new BufferedReader ( new FileReader ( file));
            String         line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            //You'll need to add proper error handling here
            return null;

        }
        return text.toString();
    }

    public void changeChapterFlag(String flow) {

        String keyname = null;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder        db;
        Document               doc = null;
        try {
            db = dbf.newDocumentBuilder();
            try {
                FileInputStream  fis = new FileInputStream ( uptoString + "/cbsechapter.xml");
                FileOutputStream fos = new FileOutputStream ( uptoString + "/cbsechapterD.xml");

                new Rijndael();
                Rijndael.EDR(fis, fos, 2);
                new DeleteFile(uptoString + "/cbsechapter.xml");
            } catch (Exception e) {
                e.printStackTrace();
            }
            File file = new File ( uptoString + "/cbsechapterD.xml");
            doc = db.parse(file);
            Node     root        = doc.getFirstChild();
            NodeList rootelement = root.getChildNodes();
            for (int i = 0; i < rootelement.getLength(); i++) {
                Node bal = rootelement.item( i);
                if (bal.getNodeName().equals("item")) {
                    NodeList itemss = bal.getChildNodes();
                    for (int j = 0; j < itemss.getLength(); j++) {
                        Node itemchild = itemss.item( j);
                        if (itemchild.getNodeName().equals("key")) {
                            keyname = itemchild.getTextContent();
                        }
                        if (itemchild.getNodeName().equals("flag")) {
                            if (keyname.contains(flow)) {
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
                FileInputStream  fis = new FileInputStream ( uptoString + "/cbsechapterD.xml");
                FileOutputStream fos = new FileOutputStream ( uptoString + "/cbsechapter.xml");
                new Rijndael();
                Rijndael.EDR(fis, fos, 1);
                new DeleteFile(uptoString + "/cbsechapterD.xml");
                fis.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e( "Error", "" + e);
        }


    }

    public static
    String addDate1( String dt) {
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
    String addDate( String dt) {
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
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            Log.e( "androidId", "" + androidId);
            UUID deviceUuid = new UUID ( androidId.hashCode(), macAddr.hashCode());
            id = deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-") + 1);
            // text.setText(deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1));
            Log.e( "UUID", "" + id);
            wifiMan.setWifiEnabled(false);
        } else {
            macAddr = wifiInf.getMacAddress();
            Log.e( "macAddr", "" + macAddr);
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            Log.e( "androidId", "" + androidId);
            UUID deviceUuid = new UUID ( androidId.hashCode(), macAddr.hashCode());
            id = deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-") + 1);
            // text.setText(deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1));
            Log.e( "UUID", "" + id);
        }
        return id;
    }

    public void displayDialogeForRetailarId() {
        final AlertDialog alertDialog = new AlertDialog.Builder( new ContextThemeWrapper ( CoupanActivation.this, R.style.AppCompatAlertDialogStyle)).create();
        alertDialog.setTitle("eTeach");
        alertDialog.setMessage("Please enter the retailer ID to avail offers on in-app purchases.\nFor support, call +91-712-6694444 or mail at support@eteach.co.in");

        alertDialog.setButton2("I'll enter the retailer ID", new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int which) {
                displaymsg();
            }
        });
        alertDialog.setButton("I'll enter it later/ I've already entered", new DialogInterface.OnClickListener() {

            public void onClick( DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (isDataEnable()) {
                    Intent intent = new Intent ( CoupanActivation.this, FullScreenWebView.class);
                    intent.putExtra("Path", brd + "-" + med + "-" + std);
                    intent.putExtra("uptoSubject", uptoSubject);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText( CoupanActivation.this, "Internet connection is not present", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.setIcon(R.drawable.logo);
        alertDialog.show();
    }


    @SuppressWarnings("deprecation")
    public void displaymsg() {
        final AlertDialog alertDialog = new AlertDialog.Builder( new ContextThemeWrapper ( CoupanActivation.this, R.style.AppCompatAlertDialogStyle)).create();
        alertDialog.setTitle("eTeach");
        alertDialog.setMessage("To provide Retailer ID, go to Menu > Settings > Edit Profile");

        alertDialog.setButton2("OK", new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setIcon(R.drawable.logo);
        alertDialog.show();
    }

    public void refreshspo(String regclassPath) {
        ArrayList<RegBinClass> deleteRegincoupanactivation = new ArrayList<RegBinClass> ();

        String[] forchapterflag      = regclassPath.split( "-");
        String   parseforchapterflag = forchapterflag[0].trim() + "/" + forchapterflag[1].trim() + "/" + forchapterflag[2].trim().replace( " ", "_");

        String calPathforspo = PACKAGEPATHFORCALENDER + "/" + algo( accountId + "/" + username + "/" + parseforchapterflag) + ".xml";

        File fileforspo = new File ( calPathforspo);
        if (fileforspo.exists()) {
            fileforspo.delete();
        }
        // delete the reg tag
        try {
            FileInputStream  fis = new FileInputStream ( uptoString + "/REG.xml");
            FileOutputStream fos = new FileOutputStream ( uptoString + "/REGD.xml");
            new Rijndael();
            Rijndael.EDR(fis, fos, 2);
            new DeleteFile(uptoString + "/REG.xml");

            Setting.deleteTagforClassName(regclassPath, uptoString + "/REGD.xml");

            FileInputStream  fis1 = new FileInputStream ( uptoString + "/REGD.xml");
            FileOutputStream fos1 = new FileOutputStream ( uptoString + "/REG.xml");
            new Rijndael();
            Rijndael.EDR(fis1, fos1, 1);
            new DeleteFile(uptoString + "/REGD.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //change chapter flag to zero


        Setting.changeflagforspouser(parseforchapterflag + "/", uptoString);

        try {

            deleteRegincoupanactivation = SubscriptionDetail.pareseRegFile(uptoString);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (deleteRegincoupanactivation.isEmpty()) {
            File fileregdelete = new File ( uptoString + "/REG.xml");
            if (fileregdelete.exists()) {
                fileregdelete.delete();
            }
        }


    }

    private
    String algo( String value) {

        // CharSequence value[];
        String SentToSer = "";
        String GetFrmSer = "";
        int    count     = 0;
        value = value.toUpperCase();
        try {
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == '0') {
                    SentToSer += '2';
                    GetFrmSer += '7';
                } else if (value.charAt(i) == '1') {
                    SentToSer += '8';
                    GetFrmSer += '4';
                } else if (value.charAt(i) == '2') {
                    SentToSer += '5';
                    GetFrmSer += '8';
                } else if (value.charAt(i) == '3') {
                    SentToSer += '9';
                    GetFrmSer += '1';
                } else if (value.charAt(i) == '4') {
                    SentToSer += '0';
                    GetFrmSer += '9';
                } else if (value.charAt(i) == '5') {
                    SentToSer += '3';
                    GetFrmSer += '2';
                } else if (value.charAt(i) == '6') {
                    SentToSer += '7';
                    GetFrmSer += '0';
                } else if (value.charAt(i) == '7') {
                    SentToSer += '1';
                    GetFrmSer += '3';
                } else if (value.charAt(i) == '8') {
                    SentToSer += '6';
                    GetFrmSer += '5';
                } else if (value.charAt(i) == '9') {
                    SentToSer += '4';
                    GetFrmSer += '6';
                } else if (value.charAt(i) == 'A') {
                    SentToSer += 'S';
                    GetFrmSer += 'K';
                } else if (value.charAt(i) == 'B') {
                    SentToSer += 'G';
                    GetFrmSer += 'E';
                } else if (value.charAt(i) == 'C') {
                    SentToSer += 'I';
                    GetFrmSer += 'T';
                } else if (value.charAt(i) == 'D') {
                    SentToSer += 'M';
                    GetFrmSer += 'F';
                } else if (value.charAt(i) == 'E') {
                    SentToSer += 'L';
                    GetFrmSer += 'B';
                } else if (value.charAt(i) == 'F') {
                    SentToSer += 'R';
                    GetFrmSer += 'Q';
                } else if (value.charAt(i) == 'G') {
                    SentToSer += 'U';
                    GetFrmSer += 'L';
                } else if (value.charAt(i) == 'H') {
                    SentToSer += 'A';
                    GetFrmSer += 'C';
                } else if (value.charAt(i) == 'I') {
                    SentToSer += 'P';
                    GetFrmSer += 'N';
                } else if (value.charAt(i) == 'J') {
                    SentToSer += 'N';
                    GetFrmSer += 'Y';
                } else if (value.charAt(i) == 'K') {
                    SentToSer += 'W';
                    GetFrmSer += 'I';
                } else if (value.charAt(i) == 'L') {
                    SentToSer += 'Y';
                    GetFrmSer += 'R';
                } else if (value.charAt(i) == 'M') {
                    SentToSer += 'B';
                    GetFrmSer += 'H';
                } else if (value.charAt(i) == 'N') {
                    SentToSer += 'K';
                    GetFrmSer += 'J';
                } else if (value.charAt(i) == 'O') {
                    SentToSer += 'Z';
                    GetFrmSer += 'X';
                } else if (value.charAt(i) == 'P') {
                    SentToSer += 'C';
                    GetFrmSer += 'Z';
                } else if (value.charAt(i) == 'Q') {
                    SentToSer += 'F';
                    GetFrmSer += 'A';
                } else if (value.charAt(i) == 'R') {
                    SentToSer += 'D';
                    GetFrmSer += 'V';
                } else if (value.charAt(i) == 'S') {
                    SentToSer += 'J';
                    GetFrmSer += 'O';
                } else if (value.charAt(i) == 'T') {
                    SentToSer += 'Q';
                    GetFrmSer += 'M';
                } else if (value.charAt(i) == 'U') {
                    SentToSer += 'V';
                    GetFrmSer += 'S';
                } else if (value.charAt(i) == 'V') {
                    SentToSer += 'H';
                    GetFrmSer += 'P';
                } else if (value.charAt(i) == 'W') {
                    SentToSer += 'E';
                    GetFrmSer += 'D';
                } else if (value.charAt(i) == 'X') {
                    SentToSer += 'O';
                    GetFrmSer += 'U';
                } else if (value.charAt(i) == 'Y') {
                    SentToSer += 'X';
                    GetFrmSer += 'G';
                } else if (value.charAt(i) == 'Z') {
                    SentToSer += 'T';
                    GetFrmSer += 'W';
                } else if (value.charAt(i) == '_') {
                    SentToSer += '_';
                    GetFrmSer += '_';
                } else {
                    SentToSer += '-';
                    GetFrmSer += '-';
                }

                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e( "Conversion Error", "Error encountered while converting.....");
        }
        int SentLen = SentToSer.length();
        int GetLen = GetFrmSer.length();
        // String fullMsg = Cls_Global_Var.AccountId + " " + SentToSer;
        // label4.Text = "Send eteach ACT " + fullMsg +
        // " to 57333 \nAnd press 'Activate' button if you have recieved activation key.";
        return SentToSer;
    }

    public class verifyCoupan extends AsyncTask<String, String, String> {

        String urlString;

        public verifyCoupan(String urlString) {
            super();
            this.urlString = urlString;
        }

        @Override
        protected
        String doInBackground( String... params) {
            try {
                HttpGet httpget = new HttpGet(urlString);
                HttpClient httpclient = new DefaultHttpClient();
                BasicHttpResponse httpResponse =
                        (BasicHttpResponse) httpclient.execute(httpget);
                HttpEntity     entity = httpResponse.getEntity();
                InputStream    is     = entity.getContent();
                BufferedReader reader = new BufferedReader ( new InputStreamReader ( is));
                StringBuilder  sb     = new StringBuilder ();
                String         line   = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

    }
}
