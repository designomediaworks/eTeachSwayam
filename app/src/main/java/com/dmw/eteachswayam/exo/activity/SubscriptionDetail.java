package com.dmw.eteachswayam.exo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.model.RegBinClass;
import com.dmw.eteachswayam.exo.model.Rijndael;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SubscriptionDetail extends AppCompatActivity {

    ArrayAdapter<RegBinClass> adapter;
    private ListView       first;
    //ArrayList<RegBinClass> regBinarray;
    private RegBinClass    name;
    private String         uptoString;
    private LayoutInflater inflater;
    private String         accountIdreg;
    private String         usernamereg;
    boolean checkuser;

    private       ArrayList<RegBinClass> arrayListsub;
    public static BaseAdapter            handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_subscription_detail);
        getWindow().getDecorView().setBackgroundColor( Color.WHITE);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent i = getIntent();
        uptoString = i.getStringExtra("uptoString");
        SubscriptionDetail.this.setTitle("Subscription Detail");
        SubscriptionDetail.this.setTitleColor( Color.parseColor( "#4fa5d5"));
        //this.setFinishOnTouchOutside(false);
        //		this.setFinishOnTouchOutside(false);
        SharedPreferences settings = getApplicationContext().getSharedPreferences( "username", 0);
        accountIdreg = settings.getString("ACCOUNTID", "xxx");
        usernamereg = settings.getString("user", "xxx");
        checkuser = true;

        registerView();
        String hddiduser = getUdid();
        arrayListsub = new ArrayList<RegBinClass> ();
        try {
            arrayListsub = pareseRegFile(uptoString);
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
        if (!arrayListsub.isEmpty()) {
            for (int j = 0; j < arrayListsub.size(); j++) {
                String usernameforregtab = arrayListsub.get( j).getuName();
                String hddidfortabinreg  = arrayListsub.get( j).getHhid();
                if (usernameforregtab != null) {
                    if (!usernameforregtab.equals(usernamereg)) {
                        checkuser = false;
                        break;
                    }
                    if (!hddidfortabinreg.equals(hddiduser)) {
                        checkuser = false;
                        break;
                    }
                } else {
                    Toast.makeText( SubscriptionDetail.this, "Please reset the application", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        } else {
            checkuser = false;
            Toast.makeText( SubscriptionDetail.this, "Please activate at least one standard!", Toast.LENGTH_LONG).show();
            finish();
        }

        if (checkuser) {
            initComponent();
        } else {
            Toast.makeText( SubscriptionDetail.this, "Invalid user login!", Toast.LENGTH_LONG).show();
            finish();
        }
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

    private void registerView() {
        //		try {
        //			if(new File(uptoString+"/REG.xml").exists()){
        //				regBinarray=pareseRegFile(uptoString);
        //
        //				new DeleteFile(uptoString+"/REGD.xml");
        //			}
        //			else Toast.makeText(getApplicationContext(), "Please activate at least one standard!", Toast.LENGTH_SHORT).show();
        //		}
        //		catch (ParserConfigurationException e) {
        //			e.printStackTrace();
        //		} catch (SAXException e) {
        //			e.printStackTrace();
        //		} catch (IOException e) {
        //			e.printStackTrace();
        //		}
    }

    private void initComponent() {

        ganerateId();
    }

    private void ganerateId() {
        first = (ListView ) findViewById( R.id.listActivationListview);

        inflater = LayoutInflater.from( this);
        //		adapter=new ArrayAdapter<RegBinClass>(this, android.R.layout.simple_list_item_1,regBinarray)
        //		{
        //			@Override
        //			public View getView(int position, View convertView,ViewGroup parent) {
        //				LinearLayout layout =new LinearLayout(SubscriptionDetail.this);
        //				layout.setOrientation(LinearLayout.HORIZONTAL);
        //				TextView textView1 =new TextView (SubscriptionDetail.this);
        //				TextView textView2 =new TextView (SubscriptionDetail.this);
        //				TextView textView3 =new TextView (SubscriptionDetail.this);
        //				TextView textView4 =new TextView (SubscriptionDetail.this);
        //				if(new Scale(SubscriptionDetail.this).Cam_height<720){
        //					textView1.setLayoutParams(new LayoutParams(290,40));
        //					textView2.setLayoutParams(new LayoutParams(160,40));
        //					textView3.setLayoutParams(new LayoutParams(150,40));
        //					textView4.setLayoutParams(new LayoutParams(180,40));
        //				}else if(new Scale(SubscriptionDetail.this).Cam_height==1440){
        //
        //					textView1.setLayoutParams(new LayoutParams(510,40));
        //					textView2.setLayoutParams(new LayoutParams(350,40));
        //
        //					textView3.setLayoutParams(new LayoutParams(350,40));
        //					textView4.setLayoutParams(new LayoutParams(300,40));
        //
        //
        //				}else{
        //				textView1.setLayoutParams(new LayoutParams(240,40));
        //				textView2.setLayoutParams(new LayoutParams(190,40));
        //				textView3.setLayoutParams(new LayoutParams(160,40));
        //				textView4.setLayoutParams(new LayoutParams(220,40));
        //				}
        //				textView1.setText(regBinarray.get(position).getclasspath());
        //				textView2.setText(regBinarray.get(position).getActDate());
        //				textView3.setText(regBinarray.get(position).getExpDate());
        //				textView4.setText(regBinarray.get(position).getCouponNo());
        //				textView1.setTextColor(Color.BLACK);
        //				textView2.setTextColor(Color.BLACK);
        //				textView3.setTextColor(Color.BLACK);
        //				textView4.setTextColor(Color.BLACK);
        //				layout.addView(textView1);
        //				layout.addView(textView2);
        //				layout.addView(textView3);
        //				layout.addView(textView4);
        //				return layout;
        //			}
        //		};
        //		first.setAdapter(adapter);
        handler = new BaseAdapter () {

            @Override
            public
            View getView( int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.set_activation_details, null);
                }
                TextView activatedClass = (TextView ) convertView.findViewById( R.id.activatedClassTextView);
                TextView activateddate  = (TextView ) convertView.findViewById( R.id.activationDateTextView);
                TextView expiryDate     = (TextView ) convertView.findViewById( R.id.expiryDateTextView);
                TextView couponNumber   = (TextView ) convertView.findViewById( R.id.couponNumberTextview);
                activatedClass.setTextColor( Color.BLACK);
                activateddate.setTextColor( Color.BLACK);
                expiryDate.setTextColor( Color.BLACK);
                couponNumber.setTextColor( Color.BLACK);
                activatedClass.setTextSize(15);
                activateddate.setTextSize(15);
                couponNumber.setTextSize(15);
                expiryDate.setTextSize(15);
                activatedClass.setGravity( Gravity.CENTER);
                activateddate.setGravity( Gravity.CENTER);
                expiryDate.setGravity( Gravity.CENTER);
                couponNumber.setGravity( Gravity.CENTER);


                activatedClass.setText(arrayListsub.get(position).getclasspath());
                activateddate.setText(arrayListsub.get(position).getActDate());
                expiryDate.setText(arrayListsub.get(position).getExpDate());
                couponNumber.setText(arrayListsub.get(position).getCouponNo());


                return convertView;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public
            Object getItem( int position) {
                return position;
            }

            @Override
            public int getCount() {
                return arrayListsub.size();
            }
        };
        first.setAdapter(handler);

    }

    public static
    ArrayList <RegBinClass> pareseRegFile( String uptostring) throws ParserConfigurationException, SAXException, IOException {

        try {
            FileInputStream  fis = new FileInputStream ( uptostring + "/REG.xml");
            FileOutputStream fos = new FileOutputStream ( uptostring + "/REGD.xml");

            new Rijndael();
            Rijndael.EDR( fis, fos, 2);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        ArrayList<RegBinClass> regarray = new ArrayList<RegBinClass> ();
        RegBinClass            name     = null;//=new RegBinClass();
        DocumentBuilderFactory dbf      = DocumentBuilderFactory.newInstance();
        DocumentBuilder        db       = dbf.newDocumentBuilder();
        Document               doc      = db.parse( new File ( uptostring + "/REGD.xml"));
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName( "item");
        //	int classelement =nodeList.getLength();
        for (int s = 0; s < nodeList.getLength(); s++) {
            Node firsttag = nodeList.item( s);
            if ( firsttag.getNodeType() == Node.ELEMENT_NODE) {
                name = new RegBinClass();
                Element  firstPersonElement = (Element ) firsttag;
                NodeList firstNameList      = firstPersonElement.getElementsByTagName( "ClassPath");
                Element  firstNameElement   = (Element ) firstNameList.item( 0);

                NodeList textFNList = firstNameElement.getChildNodes();
                name.setclasspath(((Node ) textFNList.item( 0)).getNodeValue().trim());
                //-------
                String val = ( (Node ) textFNList.item( 0)).getNodeValue().trim();
                //activateClassNamearrayList.add(val);
                Log.e( "ClassPath", "" + ( (Node ) textFNList.item( 0)).getNodeValue().trim());
                NodeList lastNameList    = firstPersonElement.getElementsByTagName( "ActDate");
                Element  lastNameElement = (Element ) lastNameList.item( 0);

                NodeList textLNList = lastNameElement.getChildNodes();
                name.setActDate(((Node ) textLNList.item( 0)).getNodeValue().trim());
                String val1 = ( (Node ) textLNList.item( 0)).getNodeValue().trim();
                //activationDatearrayList.add(val1);
                //----
                NodeList ageList    = firstPersonElement.getElementsByTagName( "ExpDate");
                Element  ageElement = (Element ) ageList.item( 0);

                NodeList textAgeList = ageElement.getChildNodes();
                name.setExpDate(((Node ) textAgeList.item( 0)).getNodeValue().trim());

                String val12 = ( (Node ) textAgeList.item( 0)).getNodeValue().trim();
                //expiryDatearrayList.add(val12);
                //------

                NodeList couponlist    = firstPersonElement.getElementsByTagName( "CouponNo");
                Element  couponelement = (Element ) couponlist.item( 0);

                NodeList coupontext = couponelement.getChildNodes();
                name.setCouponNo(((Node ) coupontext.item( 0)).getNodeValue().trim());


                Log.e( "CouponNo", "" + ( (Node ) coupontext.item( 0)).getNodeValue().trim());

                String as = ( (Node ) coupontext.item( 0)).getNodeValue().trim();

                NodeList couponlist1 = firstPersonElement.getElementsByTagName( "CouRefNumber");

                Element couponelement1 = (Element ) couponlist1.item( 0);
                if (couponlist1.getLength() != 0) {
                    NodeList coupontext1 = couponelement1.getChildNodes();
                    name.setCourefnumber(((Node ) coupontext1.item( 0)).getNodeValue().trim());
                } else {
                    name.setCourefnumber("");
                }

                NodeList couponlist12 = firstPersonElement.getElementsByTagName( "reguser");

                Element couponelement2 = (Element ) couponlist12.item( 0);
                if (couponlist12.getLength() != 0) {
                    NodeList coupontext2 = couponelement2.getChildNodes();
                    name.setuName(((Node ) coupontext2.item( 0)).getNodeValue().trim());
                } else {
                    name.setuName("");
                }

                NodeList couponlist3 = firstPersonElement.getElementsByTagName( "reghddid");

                Element couponelement3 = (Element ) couponlist3.item( 0);
                if (couponlist3.getLength() != 0) {
                    NodeList coupontext3 = couponelement3.getChildNodes();
                    name.setHhid(((Node ) coupontext3.item( 0)).getNodeValue().trim());
                } else {
                    name.setHhid("");
                }


                //coupanNumberarrayList.add(as);
            }//end of if clause
            regarray.add(name);
        }
        return regarray;
    }

}

