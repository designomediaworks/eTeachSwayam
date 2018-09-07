package com.dmw.eteachswayam.exo.fragment.drawer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.activity.ActivateBlockAccount;
import com.dmw.eteachswayam.exo.activity.SubscriptionDetail;
import com.dmw.eteachswayam.exo.model.Board;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

public class ActivateFragment extends Fragment {

    private ArrayList<Board>  boardArrayList    = new ArrayList<Board> ();
    private ArrayList<String> languageArrayList = new ArrayList<String> ();
    private ArrayList<String> classArrayList    = new ArrayList<String> ();
    ArrayAdapter<Board>  boardArrayAdapter;
    ArrayAdapter<String> languageArrayAdapter;
    ArrayAdapter<String> classArrayAdapter;
    EditText             couponNo;
    TextView             coupounTextView;
    TextView             boardTextView;
    TextView             langTextView;
    TextView             classsTextView;
    String               boardname;
    String               langvalue;
    String               classvalue;
    private Spinner board;
    //String source,destination=null;
    private Spinner medium;
    private Spinner classs;
    private File    path;
    private String  accountId;
    private String  username;
    Button buttonActivate, buttonOnlinePay;
    private String uptostring;
    private String counter;
    //private EditText verificationEditText;
    //   private Button activateButton;

    private String                 expdate;
    private Button                 exitButton;
    private TextView               coupanText;
    private String                 PACKAGEPATHFORCALENDER;
    private boolean                isRetMac;
    private String                 macEnc;
    private String                 macDec;
    private String                 accountIdsentserver;
    private String                 usernamesentserver;
    private ArrayList<RegBinClass> regcheck;
    View view;

    public ActivateFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public
    View onCreateView( LayoutInflater inflater, ViewGroup container,
                       Bundle savedInstanceState) {

        view = inflater.inflate( R.layout.fragment_activate, container, false);
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_activate, container, false);

        Setting.regBinarray = new ArrayList<RegBinClass> ();

        SharedPreferences settings = getActivity().getSharedPreferences( "username", 0);
        accountIdsentserver = settings.getString("ACCOUNTID", "xxx");
        usernamesentserver = settings.getString("user", "xxx");

        getID();
        initComponent();
        resgiaterEvent();

        return view;
    }

    private void getID() {
        boardTextView = (TextView ) view.findViewById( R.id.txtBoard);
        langTextView = (TextView ) view.findViewById( R.id.txtMedium);
        classsTextView = (TextView ) view.findViewById( R.id.txtClass);
        board = (Spinner ) view.findViewById( R.id.spinBoard);
        medium = (Spinner ) view.findViewById( R.id.spinMedium);
        classs = (Spinner ) view.findViewById( R.id.spinClass);
        couponNo = (EditText ) view.findViewById( R.id.etCouponCode);
        buttonActivate = (Button ) view.findViewById( R.id.btnActivate);
        buttonOnlinePay = (Button ) view.findViewById( R.id.btnOnlinePayment);

        SharedPreferences settings = getActivity().getSharedPreferences( "username", 0);
        accountId = settings.getString("ACCOUNTID", "xxx");
        username = settings.getString("user", "xxx");
        uptostring = Constant.INTERNAL_SDCARD + "/" + accountId + "/" + username;

        coupanText = (TextView ) view.findViewById( R.id.txtCoupon);

        macEnc = Constant.INTERNAL_SDCARD + accountId + "/" + username + "/mac.xml";
        macDec = Constant.INTERNAL_SDCARD + accountId + "/" + username + "/macD.xml";
        checkBlockAccount();
    }

    private void checkBlockAccount() {
        try {
            FileInputStream  fis = new FileInputStream ( Constant.INTERNAL_SDCARD + "/userxml.xml");
            FileOutputStream fos = new FileOutputStream ( Constant.INTERNAL_SDCARD + "/userxmlD.xml");
            new Rijndael();
            Rijndael.EDR( fis, fos, 2);

            new XMLParser(Constant.INTERNAL_SDCARD + "/userxmlD.xml", "Users", "BlockCount", "");
            counter = XMLParser.child1Value;
            new DeleteFile ( Constant.INTERNAL_SDCARD + "/userxmlD.xml");
            if (counter.equals("10")) {
                Intent intent = new Intent ( getActivity(), ActivateBlockAccount.class);
                intent.putExtra("accountId", accountId);
                intent.putExtra("username", username);
                startActivity(intent);
                getActivity().finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponent() {

        PACKAGEPATHFORCALENDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + Constant.algo( view.getContext().getPackageName());

        path = new File ( Constant.INTERNAL_SDCARD + accountId + "/" + username);

        try {
            FileInputStream  fis = new FileInputStream ( path + "/board.xml");
            FileOutputStream fos = new FileOutputStream ( path + "/boardD.xml");

            new Rijndael();
            Rijndael.EDR(fis, fos, 2);
            LayoutXmlParser parser = new LayoutXmlParser ( path + "/boardD.xml");
            boardArrayList = parser.parse(getActivity());

            new DeleteFile(path + "/boardD.xml");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (boardArrayList == null) {
            Toast.makeText( view.getContext(), "Please reset the application.", Toast.LENGTH_LONG).show();
            getActivity().finish();
        } else {
            boardArrayAdapter = new ArrayAdapter<Board> ( view.getContext(), android.R.layout.simple_list_item_1, boardArrayList) {
                @Override
                public
                View getDropDownView( int position, View convertView, ViewGroup parent) {
                    View     view = super.getView( position, convertView, parent);
                    TextView text = (TextView ) view.findViewById( android.R.id.text1);
                    text.setTextColor( Color.BLACK);//choose your color
                    return view;
                }
            };
            board.setAdapter(boardArrayAdapter);
            board.setOnItemSelectedListener(new OnItemSelectedListener () {

                @Override
                public void onItemSelected( AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                    try {
                        boardname = boardArrayList.get(arg2).getActualName();
                        FileInputStream  fis = new FileInputStream ( path + "/cbselanguage.xml");
                        FileOutputStream fos = new FileOutputStream ( path + "/cbselanguageD.xml");

                        new Rijndael();
                        Rijndael.EDR(fis, fos, 2);
                        LayoutXmlParser parser = new LayoutXmlParser(path + "/cbselanguageD.xml");

                        languageArrayList = parser.parse(boardname);
                        new DeleteFile(path + "/cbselanguageD.xml");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    languageArrayAdapter = new ArrayAdapter<String> ( view.getContext(), android.R.layout.simple_list_item_1, languageArrayList) {
                        @Override
                        public
                        View getDropDownView( int position, View convertView, ViewGroup parent) {
                            View     view = super.getView( position, convertView, parent);
                            TextView text = (TextView ) view.findViewById( android.R.id.text1);
                            text.setTextColor( Color.BLACK);//choose your color
                            return view;

                        }
                    };
                    medium.setAdapter(languageArrayAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {


                }
            });
            mediumClickListener();
        }
    }

    private void mediumClickListener() {


        medium.setOnItemSelectedListener(new OnItemSelectedListener () {
            @Override
            public void onItemSelected( AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                try {
                    langvalue = medium.getSelectedItem().toString();
                    FileInputStream  fis = new FileInputStream ( path + "/cbseclass.xml");
                    FileOutputStream fos = new FileOutputStream ( path + "/cbseclassD.xml");

                    new Rijndael();
                    Rijndael.EDR(fis, fos, 2);


                    LayoutXmlParser parser = new LayoutXmlParser(path + "/cbseclassD.xml");

                    classArrayList = parser.parse(boardname + "/" + langvalue);
                    new DeleteFile(path + "/cbseclassD.xml");
                    languageArrayAdapter.notifyDataSetChanged();


                } catch (Exception e) {
                    // TODO: handle exception
                }
                classArrayAdapter = new ArrayAdapter<String> ( view.getContext(), android.R.layout.simple_list_item_1, classArrayList) {
                    @Override
                    public
                    View getDropDownView( int position, View convertView, ViewGroup parent) {
                        View     view = super.getView( position, convertView, parent);
                        TextView text = (TextView ) view.findViewById( android.R.id.text1);
                        text.setTextColor( Color.BLACK);//choose your color
                        return view;


                    }

                };
                classs.setAdapter(classArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void resgiaterEvent() {

        buttonActivate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //	if(validation()){

                if (username.equals("eteach")) {
                    Toast.makeText( getActivity(), "This is a Demo account.\n You cannot activate any class.\n To activate, please signup.", Toast.LENGTH_LONG).show();
                } else {
                    if (isDataEnable()) {

                        String coponno = couponNo.getText().toString();

                        classvalue = classs.getSelectedItem().toString();

                        checkforRenew(boardname + " - " + langvalue + " - " + classvalue);

                        //							new checkCoupanNumber().execute("");
                    } else {
                        Toast.makeText( getActivity(), "Internet connection is not present", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

    }

    public boolean isDataEnable() {
        Context             context = getActivity();
        ConnectivityManager conMgr;
        conMgr = (ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE);
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

    private void checkforRenew(String uptoclass) {
        try {

            if (new File ( uptostring + "/REG.xml").exists()) {
                Setting.regBinarray = SubscriptionDetail.pareseRegFile( uptostring);
                for (int i = 0; i < Setting.regBinarray.size(); i++) {

                    RegBinClass b = Setting.regBinarray.get(i);
                    String      a = b.getclasspath();
                    if (a.equals(uptoclass.replace("_", " "))) {
                        String bd = b.getActDate();
                        expdate = b.getExpDate();
                        //String d=b.getCouponNo();
                    }

                }
                if (expdate != null) {
                    SimpleDateFormat timeStampFormat = new SimpleDateFormat ( "dd/MM/yy");
                    try {
                        String stringdate = getDateTime();
                        Date   myDate1    = timeStampFormat.parse( stringdate);
                        Date   date2      = timeStampFormat.parse( expdate);
                        if (myDate1.after(date2)) {
                            showDialog();

                        } else {
                            //checkforAlreadyActivateClass();
                            //new checkCoupanNumber(0).execute("");
                            String currentActivationClass = boardname + " - " + langvalue + " - " + classvalue.replace( "_", " ");
                            for (int i = 0; i < Setting.regBinarray.size(); i++) {
                                String classNameForActivation = Setting.regBinarray.get( i).getclasspath();
                                if (classNameForActivation.equals(currentActivationClass)) {
                                    String couponreferancenumber = Setting.regBinarray.get( i).getCourefnumber();
                                    String reffileClass          = Setting.regBinarray.get( i).getclasspath();
                                    if (!couponreferancenumber.equals("")) {
                                        if (!couponreferancenumber.equals("null")) {
                                          /*  if(paymentGateway.isChecked())
                                            {
                                                displayDialogeForRetailarId1();
                                            }
                                            else
                                            {*/
                                            new checkCoupanNumber(0).execute("");
                                            //}
                                        } else {
                                            Toast.makeText( getActivity(), "This standard is already activated.", Toast.LENGTH_LONG).show();
                                            getActivity().finish();
                                        }
                                    } else {
                                        Toast.makeText( getActivity(), "This standard is already activated.", Toast.LENGTH_LONG).show();
                                        getActivity().finish();
                                    }
                                }
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    checkforAlreadyActivateClass();
                    //if(checkCouon())
                    //new checkCoupanNumber(0).execute("");
                }
            } else {
                checkforAlreadyActivateClass();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private
    String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat ( "dd/MM/yy");
        Date       date       = new Date ();
        return dateFormat.format(date);
    }

    @SuppressWarnings("deprecation")
    private void showDialog() {

        final AlertDialog ald1 = new AlertDialog.Builder( getActivity()).create();
        ald1.setTitle("eTeach");
        ald1.setIcon(R.drawable.logo);
        ald1.setMessage("Your subscription for " + classvalue + " is expired \n Do you want to reactivate it?");
        ald1.setButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick( DialogInterface dialog, int which) {
                /*if (paymentGateway.isChecked()) {

                    //new checkcharges().execute("");
                    displayDialogeForRetailarId1();

                } else {*/
                    if (checkCouon())
                        new checkCoupanNumber(1).execute("");
               // }
            }
        });
        ald1.setButton2("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick( DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                ald1.dismiss();


            }
        });
        ald1.show();
    }

    class checkCoupanNumber extends AsyncTask<String, String, String> {
        int mode = 0;

        public checkCoupanNumber(int mode) {
            this.mode = mode;
        }

        private ProgressDialog dialog;
        private String         str;
        private String         AccId;
        private String         hddid;
        private String         Coupon;
        private HttpPost       httppost;
        private String         countervalue;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog = new ProgressDialog ( getActivity());
            dialog.setCancelable(false);
            dialog.setMessage("Checking coupon number. Please wait...");
            dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected
        String doInBackground( String... params) {
            try {
                FileInputStream  fis = new FileInputStream ( uptostring + "/mac.xml");
                FileOutputStream fos = new FileOutputStream ( uptostring + "/macD.xml");
                new Rijndael();
                Rijndael.EDR(fis, fos, 2);

                new XMLParser(uptostring + "/macD.xml", "User", "AcountId", "");
                AccId = XMLParser.child1Value;
                new DeleteFile(uptostring + "/macD.xml");

                hddid = getUdid();
       //         Coupon = couponNo.getText().toString();

                if (mode == 1) {
                    httppost = new HttpPost("http://" + Constant.WEBSERVER + "/designo_pro/ws_renewcoupon.php?format=json");
                } else
                    httppost = new HttpPost("http://" + Constant.WEBSERVER + "/designo_pro/ws_checkcoupon.php?format=json");

                String       xml = "<UserInfo><hddid>" + hddid + "</hddid><AccId>" + AccId + "</AccId><brd>" + boardname + "</brd><med>" + langvalue + "</med><std>" + classvalue + "</std><Coupon>" + Coupon + "</Coupon></UserInfo>";
                StringEntity se  = new StringEntity( xml, HTTP.UTF_8);

                se.setContentType("text/xml");
                httppost.setHeader("Content-Type", "application/soap+xml;charset=UTF-8");
                httppost.setEntity(se);

                HttpClient httpclient = new DefaultHttpClient ();
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
        protected void onPostExecute(String res) {
            try {
                if (res != null) {
                    JSONObject jsonObject = new JSONObject ( res);
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
                        new XML ( Constant.INTERNAL_SDCARD + "/userxmlD.xml", "Users", "BlockCount", "");

                        //						new XMLParser(Constant.INTERNAL_SDCARD+"/userxmlD.xml","Users","BlockCount","");
                        //						countervalue = XMLParser.child1Value;
                        //						if(countervalue!=null){

                        new XMLParser(Constant.INTERNAL_SDCARD + "/userxmlD.xml", "Users", "BlockCount", "");
                        String Name = XMLParser.child1Value;


                        if (Name.equals("")) {
                            XML.addNodeToXML(Constant.INTERNAL_SDCARD + "/userxmlD.xml", "BlockCount", Counter);

                        } else {

                            XML.editByDOM(Constant.INTERNAL_SDCARD + "/userxmlD.xml", "Users", "BlockCount", Counter);
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

                        Intent intent = new Intent ( getActivity(), ActivateBlockAccount.class);
                        intent.putExtra("accountId", accountId);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        getActivity().finish();
                        Toast.makeText( getActivity(), "Your account is blocked \n Please check your mail to unblock your account.", Toast.LENGTH_SHORT).show();
                    }
                    if (CouponNumber.equals("This class is not expired or you have selected wrong class for renewal. Please check your system date and time")) {
                        Toast.makeText( getActivity(), "This class is not expired or you have selected wrong class for renewal.\nPlease check your system date and time", Toast.LENGTH_LONG).show();
                    }

                    if (CouponNumber.equals("coupon not valid")) {
                        //Toast.makeText(ActivateCouponHome.this,"Coupon no is Invalid", Toast.LENGTH_SHORT).show();
                        int change = Integer.parseInt( Counter);
                        int total = 10 - change;
                        Toast.makeText( getActivity(), "Coupon no. is invalid, " + total + " more attempts remaining", Toast.LENGTH_LONG).show();
                        //Toast.makeText(ActivateCouponHome.this,"Device Has Been Changed.\nPlease try to activate on Correct Device", Toast.LENGTH_SHORT).show();
                        couponNo.setText("");
                    } else if (CouponNumber.equals("not matched")) {
                        int change = Integer.parseInt( Counter);
                        int total = 10 - change;
                        Toast.makeText( getActivity(), "Coupon no. is invalid, " + total + " more attempts remaining", Toast.LENGTH_LONG).show();
                        //Toast.makeText(ActivateCouponHome.this,"Device Has Been Changed.\nPlease try to activate on Correct Device", Toast.LENGTH_SHORT).show();
                        couponNo.setText("");
                    } else if (CouponNumber.equals("try again")) {
                        Toast.makeText( getActivity(), "Something went wrong\n Please try again.", Toast.LENGTH_SHORT).show();
                        couponNo.setText("");
                    } else if (CouponNumber.equals(classvalue + " is already activated")) {
                        Toast.makeText( getActivity(), "This standard is already activated.", Toast.LENGTH_SHORT).show();
                        couponNo.setText("");
                    } else if (CouponNumber.equals("1")) {
                        if (!CalCounter.equals("0")) {

                            for (int i = 0; i < Setting.regBinarray.size(); i++) {
                                String refNumber    = Setting.regBinarray.get( i).getCourefnumber();
                                String classPathspo = Setting.regBinarray.get( i).getclasspath();
                                if (!refNumber.equals("")) {
                                    if (!refNumber.equals("null")) {
                                        refreshspo(classPathspo);
                                    }
                                }
                            }

                            //new CreateCalendarXML(addDate1(date),boardname,langvalue,classvalue,Environment.getExternalStorageDirectory().getAbsolutePath(),uptostring,ActivateCouponHome.this,Integer.parseInt(CalCounter));
                            if (mode == 1) {
                                if (new File ( PACKAGEPATHFORCALENDER + "/" + algo( accountId + "/" + username + "/" + boardname + "/" + langvalue + "/" + classvalue) + ".xml").exists()) {
                                    new File ( PACKAGEPATHFORCALENDER + "/" + algo( accountId + "/" + username + "/" + boardname + "/" + langvalue + "/" + classvalue) + ".xml").delete();
                                }
                                new CreateCalendarXML( addDate1(date), boardname, langvalue, classvalue, Environment.getExternalStorageDirectory().getAbsolutePath(), PACKAGEPATHFORCALENDER, getActivity(), Integer.parseInt( CalCounter), hddid, usernamesentserver, accountIdsentserver);
                            } else {
                                new CreateCalendarXML ( addDate1( date), boardname, langvalue, classvalue, Environment.getExternalStorageDirectory().getAbsolutePath(), PACKAGEPATHFORCALENDER, getActivity(), Integer.parseInt( CalCounter), hddid, usernamesentserver, accountIdsentserver);
                            }
                            couponNo.setText("");
                            changeChapterFlag(boardname + "/" + langvalue + "/" + classvalue + "/");
                            String hddidfortab = getUdid();
                            createSubscriptionFile( boardname, langvalue, classvalue, Integer.parseInt( CalCounter), addDate( date), Coupon, Crefnumber, username, hddidfortab, mode);
                            new verifyCoupon(Constant.WEBSERVE + "/designo_pro/verify_coupon.php?usrnm=" + username + "&brd=" + boardname + "&cls=" + classvalue + "&med=" + langvalue + "&coupon=" + Coupon + "&format=xml").execute("");
                            if (!Ctopup.equals("0")) {
                                Toast.makeText( getActivity(), "Class Activated.\nCongratulation!\nYour subscription validity has increased to " + Ctopup + " now.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText( getActivity(), "Class Activated", Toast.LENGTH_SHORT).show();
                            }
                            if (Constant.chapterArrayList != null) {
                                for (int i = 0; i < Constant.chapterArrayList.size(); i++) {
                                    if (Constant.chapterArrayList.get(i).getKey().contains(boardname + "/" + langvalue + "/" + classvalue + "/")) {
                                        Constant.chapterArrayList.get(i).setFlag(2);
                                    }
                                }
                            }
                            getActivity().finish();
                            //calDate = calDate.substring(0, 2)+"-"+calDate.substring(2, 4)+"-"+calDate.substring(4,6);
                        }
                    } else {
                        Toast.makeText( getActivity(), CouponNumber, Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.dismiss();
            super.onPostExecute(null);
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

        private void createSubscriptionFile( String brd, String med, String std, int day, String date, String coupon, String CouponRefNumber, String usernameforreg, String hddidTab, int modereg) {

            regcheck = new ArrayList<RegBinClass> ();
            if (modereg == 1) {
                try {
                    FileInputStream  fis = new FileInputStream ( uptostring + "/REG.xml");
                    FileOutputStream fos = new FileOutputStream ( uptostring + "/REGD.xml");
                    new Rijndael();
                    Rijndael.EDR(fis, fos, 2);
                    new DeleteFile(uptostring + "/REG.xml");

                    String classNamefordelete = brd + " - " + med + " - " + std.replace( "_", " ");

                    Setting.deleteTagforClassName(classNamefordelete, uptostring + "/REGD.xml");

                    FileInputStream  fis1 = new FileInputStream ( uptostring + "/REGD.xml");
                    FileOutputStream fos1 = new FileOutputStream ( uptostring + "/REG.xml");
                    new Rijndael();
                    Rijndael.EDR(fis1, fos1, 1);
                    new DeleteFile(uptostring + "/REGD.xml");
                    try {
                        regcheck = SubscriptionDetail.pareseRegFile(uptostring);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (regcheck.isEmpty()) {
                        File fileregdelete = new File ( uptostring + "/REG.xml");
                        if (fileregdelete.exists()) {
                            fileregdelete.delete();
                        }
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            File   Subscription = new File ( uptostring + "/REG.xml");
            String calDateNxt   = EXPDATE( date, day);
            if (Subscription.exists()) {
                try {
                    FileInputStream  fis           = new FileInputStream ( uptostring + "/REG.xml");
                    FileOutputStream fos           = new FileOutputStream ( uptostring + "/REGD.xml");
                    File             Subscription1 = new File ( uptostring + "/REGD.xml");
                    new Rijndael();
                    Rijndael.EDR(fis, fos, 2);
                    new DeleteFile(uptostring + "/REG.xml");

                    RandomAccessFile temp1 = new RandomAccessFile ( Subscription1, "rw");
                    String           s     = "<item><ClassPath>" + brd + " - " + med + " - " + std.replace( "_", " ") + "</ClassPath><ActDate>" + date.substring( 4, 6) + "/" + date.substring( 2, 4) + "/" + date.substring( 0, 2) + "</ActDate><ExpDate>" + calDateNxt.substring( 4, 6) + "/" + calDateNxt.substring( 2, 4) + "/" + calDateNxt.substring( 0, 2) + "</ExpDate><CouponNo>" + coupon + "</CouponNo><CouRefNumber>" + CouponRefNumber + "</CouRefNumber><reguser>" + usernameforreg + "</reguser><reghddid>" + hddidTab + "</reghddid></item></Reg>";
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

                    File temp2 = new File ( uptostring + "/REGD.xml");
                    temp2.createNewFile();
                    RandomAccessFile temp1 = new RandomAccessFile ( temp2, "rw");

                    String s = "<Reg><item><ClassPath>" + brd + " - " + med + " - " + std.replace( "_", " ") + "</ClassPath><ActDate>" + date.substring( 4, 6) + "/" + date.substring( 2, 4) + "/" + date.substring( 0, 2) + "</ActDate><ExpDate>" + calDateNxt.substring( 4, 6) + "/" + calDateNxt.substring( 2, 4) + "/" + calDateNxt.substring( 0, 2) + "</ExpDate><CouponNo>" + coupon + "</CouponNo><CouRefNumber>" + CouponRefNumber + "</CouRefNumber><reguser>" + usernameforreg + "</reguser><reghddid>" + hddidTab + "</reghddid></item></Reg>";
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
                FileInputStream  fis1 = new FileInputStream ( uptostring + "/REGD.xml");
                FileOutputStream fos1 = new FileOutputStream ( uptostring + "/REG.xml");
                new Rijndael();
                Rijndael.EDR(fis1, fos1, 1);
                new DeleteFile(uptostring + "/REGD.xml");
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

        public void writeFile( String i, String path, String fileName) {
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

        public
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

        public
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

        public
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

        public void changeChapterFlag(String flow) {

            String keyname = null;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder        db;
            Document               doc = null;
            try {
                db = dbf.newDocumentBuilder();
                try {
                    FileInputStream  fis = new FileInputStream ( uptostring + "/cbsechapter.xml");
                    FileOutputStream fos = new FileOutputStream ( uptostring + "/cbsechapterD.xml");

                    new Rijndael();
                    Rijndael.EDR(fis, fos, 2);
                    new DeleteFile(uptostring + "/cbsechapter.xml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                File file = new File ( uptostring + "/cbsechapterD.xml");
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
                    FileInputStream  fis = new FileInputStream ( uptostring + "/cbsechapterD.xml");
                    FileOutputStream fos = new FileOutputStream ( uptostring + "/cbsechapter.xml");
                    new Rijndael();
                    Rijndael.EDR(fis, fos, 1);
                    new DeleteFile(uptostring + "/cbsechapterD.xml");
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

    }

    private void checkforAlreadyActivateClass() {

        new AsyncTask<String, String, String> (){
            String hddid =getUdid();
            private ProgressDialog dialog1;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               /* dialog1=new ProgressDialog(getActivity());
                dialog1.setCancelable(false);
                if(paymentGateway.isChecked())
                    dialog1.setMessage("Checking. Please wait...");
                else dialog1.setMessage("Checking coupon number. Please wait...");
                dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog1.show();*/
            }

            @Override
            protected
            String doInBackground( String... params) {
                String doin_res =null;
                try {
                    String url = "http://" + Constant.WEBSERVER + "/designo_pro/ws_checkclass.php?hddid=" + hddid + "&brd=" + boardname + "&std=" + classvalue + "&med=" + langvalue + "&format=json";

                    HttpGet httpget = new HttpGet( url);

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
                    if(result!=null){
                        JSONObject jsonObject =new JSONObject ( result);

                        JSONArray hrArray = jsonObject.getJSONArray( "posts");

                        JSONObject post = hrArray.getJSONObject( 0)
                                .getJSONObject("post");

                        String result1 = post.getString( "result");
                        if(result1.equals("OK")){
                            //Toast.makeText(Login.this, "You have Entered Wrong Username.\n Please Enter Valid Username. ", Toast.LENGTH_LONG).show();

                           /* dialog1.dismiss();
                            if(paymentGateway.isChecked()){

                                //new checkcharges().execute("");
                                displayDialogeForRetailarId1();
                            }else{*/
                                if(checkCouon())
                                    new checkCoupanNumber(0).execute("");
                          //  }
                        }else {
                            dialog1.dismiss();
                            alertDialogCheck();
                            //Toast.makeText(Login.this, "Your Password is Sent To Your e-mail.\n Please Check your email and Reenter Your Username and Password. ", Toast.LENGTH_LONG).show();
                        }
                    }else Toast.makeText( getActivity(), "Sorry Please Try Again.", Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                    Toast.makeText( getActivity(), "Sorry Please Try Again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }

        }.execute("");
    }

    public boolean checkCouon(){
        if(couponNo.getText().toString().length()==0)
        {
            //Toast.makeText(getApplicationContext(),"Please fillup all field", Toast.LENGTH_SHORT).show();
            couponNo.setError("Enter Coupon no");
            return false;
        }
        else
        {
            couponNo.setError(null);
        }
        return true;
    }

    public
    String getUdid() {

        final String macAddr, androidId;
        String       id = null;

        WifiManager wifiMan = (WifiManager )getActivity().getApplicationContext().getSystemService( Context.WIFI_SERVICE);
        WifiInfo    wifiInf = wifiMan.getConnectionInfo();

        if(!wifiMan.isWifiEnabled())
        {
            wifiMan.setWifiEnabled(true);
            macAddr = wifiInf.getMacAddress();
            Log.e( "macAddr", "" + macAddr);
            androidId = "" + android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
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
            androidId = "" + android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            Log.e( "androidId", "" + androidId);
            UUID deviceUuid = new UUID ( androidId.hashCode(), macAddr.hashCode());
            id=deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1);
            // text.setText(deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1));
            Log.e( "UUID", "" + id);
        }
        return id;
    }

    public class verifyCoupon extends AsyncTask<String, String, String> {

        String urlString;

        public  verifyCoupon(String urlString) {
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

    public void refreshspo(String regclassPath){

        ArrayList<RegBinClass> deleteReg           =new ArrayList<RegBinClass> ();
        String[]               forchapterflag      =regclassPath.split( "-");
        String                 parseforchapterflag = forchapterflag[0].trim() + "/" + forchapterflag[1].trim() + "/" + forchapterflag[2].trim().replace( " ", "_");

        String calPathforspo = PACKAGEPATHFORCALENDER + "/" + algo( accountId + "/" + username + "/" + parseforchapterflag) + ".xml";

        File fileforspo =new File ( calPathforspo);
        if (fileforspo.exists())
        {
            fileforspo.delete();
        }
        // delete the reg tag
        try{
            FileInputStream  fis =new FileInputStream ( uptostring + "/REG.xml");
            FileOutputStream fos =new FileOutputStream ( uptostring + "/REGD.xml");
            new Rijndael();
            Rijndael.EDR(fis, fos, 2);
            new DeleteFile(uptostring+"/REG.xml");

            Setting.deleteTagforClassName(regclassPath, uptostring+"/REGD.xml");

            FileInputStream  fis1 =new FileInputStream ( uptostring + "/REGD.xml");
            FileOutputStream fos1 =new FileOutputStream ( uptostring + "/REG.xml");
            new Rijndael();
            Rijndael.EDR(fis1, fos1, 1);
            new DeleteFile(uptostring+"/REGD.xml");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //change chapter flag to zero


        Setting.changeflagforspouser(parseforchapterflag+"/", uptostring);

        try {
            deleteReg=SubscriptionDetail.pareseRegFile(uptostring);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(deleteReg.isEmpty())
        {
            File fileregdelete =new File ( uptostring + "/REG.xml");
            if(fileregdelete.exists())
            {
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
                }

                else if (value.charAt(i) == 'A') {
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

    @SuppressWarnings("deprecation")
    public void alertDialogCheck(){

        final AlertDialog alterDiloge = new AlertDialog.Builder( getActivity()).create();
        alterDiloge.setIcon(R.drawable.logo);
        alterDiloge.setTitle("eTeach");
        alterDiloge.setMessage("This class is already activated on this Device by another user. \n  Do you want to continue?");
        alterDiloge.setButton2("No", new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int which) {
                alterDiloge.dismiss();
                getActivity().finish();
            }
        });
        alterDiloge.setButton("Yes",new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int which) {
                //finish();
             /*   if(!paymentGateway.isChecked()){
                    if(checkCouon())
                        new checkCoupanNumber(0).execute("");
                }else{

                    displayDialogeForRetailarId1();

                }*/
            }
        });
        alterDiloge.show();
    }

}
