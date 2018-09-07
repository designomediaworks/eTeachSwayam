/* This Class is used for Registration for user */
package com.dmw.eteachswayam.exo.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.model.Constant;
import com.dmw.eteachswayam.exo.model.DeleteFile;
import com.dmw.eteachswayam.exo.model.DownloadUserFile;
import com.dmw.eteachswayam.exo.model.Rijndael;
import com.dmw.eteachswayam.exo.model.User;
import com.dmw.eteachswayam.exo.model.UserXMLParser;
import com.dmw.eteachswayam.exo.model.ValidationInRegister;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHttpResponse;
import cz.msebera.android.httpclient.protocol.HTTP;

@SuppressWarnings ( "ALL" )
public class RegisterActivity extends AppCompatActivity {

    EditText etUserName,etPassword,etConformPassword;
    EditText etFirstName,etLastName,etContactNo,etEmailId,etCity,etState,etPinCode,etCountry,etAddress,etSchoolName;
    EditText etRetailerName,etRetailerLocation,etRetailerId;
    String vUserName,vPassword,vConformPassword,vFirstName,vLastName,vContactNo,vEmailId;
    String accId;
    private boolean checkMacPath;

    private boolean     emptyText;
    private boolean     emailIdChecker;
    private boolean     checkInf;
    private boolean     checkRetailerIdNotEmpty;
    private PackageInfo pInfo;
    public  String      apkVersion;

    Button btnSubmit;
    private CheckBox    cbUserName;
    private ProgressBar pbRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_register);

        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Id of all components
        getIdComponent();

        // Check user availability
        clickEvent();
        // Validation
        // emptyText();
        try {

            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        apkVersion = pInfo.versionName;
    }

    // Id of all components
    private void getIdComponent(){

        pbRegister = (ProgressBar )findViewById( R.id.pbRegister);
        cbUserName = (CheckBox )findViewById( R.id.cbUserName);
        cbUserName.setEnabled(false);

        etUserName = (EditText )findViewById( R.id.etUserName);
        etPassword = (EditText )findViewById( R.id.etPassword);
        etConformPassword = (EditText )findViewById( R.id.etConformPassword);

        etFirstName = (EditText )findViewById( R.id.etFirstName);
        etLastName = (EditText )findViewById( R.id.etLastName);
        etContactNo = (EditText )findViewById( R.id.etContactNo);
        etEmailId = (EditText )findViewById( R.id.etEmailId);
        etCity = (EditText )findViewById( R.id.etCity);
        etState = (EditText )findViewById( R.id.etState);
        etPinCode = (EditText )findViewById( R.id.etPinCode);
        etCountry = (EditText )findViewById( R.id.etCountry);
        etAddress = (EditText )findViewById( R.id.etAddress);
        etSchoolName = (EditText )findViewById( R.id.etSchoolName);

        etRetailerName = (EditText )findViewById( R.id.etRetailerName);
        etRetailerLocation = (EditText )findViewById( R.id.etRetailerLocation);
        etRetailerId = (EditText )findViewById( R.id.etRetailerId);

        btnSubmit = (Button ) findViewById( R.id.btnSubmit);
    }

    // Validation of the register form
    public boolean emptyText(){
        emptyText=false;
        if(etUserName.getText().toString().isEmpty())
        {
            etUserName.setError("Please enter Username");
            emptyText=true;
        }else{
            etUserName.setError(null);

        }

        if(etPassword.getText().toString().isEmpty())
        {
            etPassword.setError("Please enter password");
            emptyText=true;
        }else{
            etPassword.setError(null);
        }

        if(etConformPassword.getText().toString().isEmpty())
        {
            etConformPassword.setError("Please enter confirm password");
            emptyText=true;
        }else{
            etConformPassword.setError(null);
        }

        if(etFirstName.getText().toString().isEmpty())
        {
            etFirstName.setError("Please enter first name");
            emptyText=true;
        }else{
            etFirstName.setError(null);
        }

        if(etLastName.getText().toString().isEmpty())
        {
            etLastName.setError("Please enter last name");
            emptyText=true;
        }else{
            etLastName.setError(null);
        }

        if(etContactNo.getText().toString().isEmpty())
        {
            etContactNo.setError("Please enter contact number");
            emptyText=true;
        }else{
            etContactNo.setError(null);
        }

        if(etEmailId.getText().toString().isEmpty())
        {
            etEmailId.setError("Please enter email Id");
            emptyText=true;
        }else{
            etEmailId.setError(null);
        }

        if(etAddress.getText().toString().isEmpty())
        {
            etAddress.setError("Please enter address");
            emptyText=true;
        }else{
            etAddress.setError(null);
        }

        if(etCity.getText().toString().isEmpty())
        {
            etCity.setError("Please enter city");
            emptyText=true;
        }else{
            etCity.setError(null);
        }

        if(etState.getText().toString().isEmpty())
        {
            etState.setError("Please enter state");
            emptyText=true;
        }else{
            etState.setError(null);
        }

        if(etPinCode.getText().toString().isEmpty())
        {
            etPinCode.setError("Please enter pincode");
            emptyText=true;
        }else{
            etPinCode.setError(null);
        }

        if(etCountry.getText().toString().isEmpty())
        {
            etCountry.setError("Please enter country");
            emptyText=true;
        }else{
            etCountry.setError(null);

        }

        if(etSchoolName.getText().toString().isEmpty())
        {
            etSchoolName.setError("Please enter school name");
            emptyText=true;
        }else{
            etSchoolName.setError(null);
        }

        return false;
    }

    // Handle click event in Screen
    private void clickEvent(){

        //Check user availability

        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange( View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etUserName.getText() != null) {
                        if (!etUserName.getText().toString().contains(" ")) {
                            pbRegister.setVisibility( View.VISIBLE);
                            cbUserName.setVisibility( View.INVISIBLE);
                            userNameValidation(etUserName.getText().toString());
                        } else {
                            etUserName.setError("Space is not allowed in username");
                        }
                    }
                } else {
                    pbRegister.setVisibility( View.INVISIBLE);
                    cbUserName.setVisibility( View.VISIBLE);
                    //usrnm.setError(null);
                    cbUserName.setChecked(false);
                }
            }
        });

        // Click event of Submit Button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getValueAtString();

                Constant.arrayListValidation = new ArrayList<EditText> ();

                Constant.arrayListValidation.add(etUserName);
                Constant.arrayListValidation.add(etPassword);
                Constant.arrayListValidation.add(etConformPassword);
                Constant.arrayListValidation.add(etFirstName);
                Constant.arrayListValidation.add(etLastName);
                Constant.arrayListValidation.add(etContactNo);
                Constant.arrayListValidation.add(etEmailId);

                ValidationInRegister validation = new ValidationInRegister ( RegisterActivity.this);
                String[] strings = {"UserName", "Password", "Confirm Password",
                        "First Name", "Last Name", "Contact Number",
                        "Email Id", "Address", "City", "State", "Pin Code",
                        "Country", "School Name",
                };

                String strRetailerId = etRetailerId.getText().toString();

                if (cbUserName.isChecked()) {
                    validation.checkEmptyValidation(Constant.arrayListValidation, strings);
                    if (Constant.checkValidation) {
                        checkEmailPattern(etEmailId.getText().toString());
                        checkInfo(etPassword.getText().toString(), etConformPassword.getText().toString(), etContactNo.getText().toString(), etPinCode.getText().toString());
                        if (checkInf) {
                            {
                                if (strRetailerId.equals("")) {
                                    displayAlertForRetailerId();
                                } else {
                                    new ServerResponseDetails().execute("");
                                }

                            }
                        }
                    }
                } else {
                    Toast.makeText( RegisterActivity.this, "Server is busy for checking username. Please wait", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean userNameValidation(String name) {
        new AsyncTask<String, String, String> () {
            protected void onPreExecute() {
                pbRegister.setVisibility( View.VISIBLE);
                cbUserName.setVisibility( View.INVISIBLE);
            };
            protected void onPostExecute(String res) {
                try {
                    if(res!=null){
                        JSONObject jsonObject =new JSONObject ( res);

                        JSONArray hrArray = jsonObject.getJSONArray( "posts");

                        JSONObject post = hrArray.getJSONObject( 0)
                                .getJSONObject("post");

                        String result = post.getString( "result");
                        if(result.equals("0")){
                            pbRegister.setVisibility( View.INVISIBLE);

                            cbUserName.setVisibility( View.VISIBLE);
                            cbUserName.setChecked(true);
                            btnSubmit.setClickable(true);
                        }
                        else{

                            pbRegister.setVisibility( View.INVISIBLE);
                            cbUserName.setVisibility( View.VISIBLE);
                            cbUserName.setChecked(false);
                            etUserName.setError("Username taken! Please try a different one.");
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            @Override
            protected
            String doInBackground( String... params) {
                String doin_res =null;
                try {

                    HttpPost     httppost = new HttpPost ( "http://" + Constant.WEBSERVER + "/designo_pro/ws_checkusername.php?format=json");
                    String       str      = "<CheckAvailability><V_UserName>" + params[0] + "</V_UserName><V_Password>" + "xyz" + "</V_Password><V_Flag>0</V_Flag></CheckAvailability>";
                    StringEntity se       = new StringEntity ( str, HTTP.UTF_8);

                    se.setContentType("text/xml");
                    httppost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
                    httppost.setEntity(se);

                    HttpClient httpclient = new DefaultHttpClient ();
                    //BasicHttpResponse httpResponse = (BasicHttpResponse ) httpclient.execute( httppost);
                    HttpResponse httpResponse =httpclient.execute ( httppost );

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


                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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
        }.execute(name);

        return false;
    }

    // Get Value of EditText convert to String
    private void getValueAtString() {

        vUserName = etUserName.getText().toString();
        vConformPassword = etConformPassword.getText().toString();
        vContactNo = etContactNo.getText().toString();
        vEmailId = etEmailId.getText().toString();
        vPassword = etPassword.getText().toString();
        vFirstName = etFirstName.getText().toString();
        vLastName = etLastName.getText().toString();
    }

    // Check valid pattern of email Id
    public boolean checkEmailPattern(String email) {
        Pattern pattern = Pattern.compile( ".+@.+\\.[a-z]+");
        Matcher matcher = pattern.matcher( email);
        emailIdChecker = matcher.matches();
        return emailIdChecker;
    }

    public void checkInfo( String strPassword, String strConformPassword, String strContactNo, String strPinCode) {
        checkInf=true;
        if(!strPassword.equals(strConformPassword))
        {
            etConformPassword.setError("password does not match");
            if(checkInf&&Constant.checkValidation)
                etConformPassword.requestFocus();
            checkInf=false;
        }

        if(strPassword.toString().length()<6 && !strPassword.toString().contains(" "))
        {
            etPassword.setError("minimum six characters with no space");
            if(checkInf&&Constant.checkValidation)
                etPassword.requestFocus();
            etPassword.setError("minimum six characters with no space");
            checkInf=false;
        }
        else{
            if(checkInf){
                etConformPassword.setError(null);
                etPassword.setError(null);
            }
        }

        if(strContactNo.length()<9 || strContactNo.length()>15)
        {
            etContactNo.setError("Please enter valid contact number");
            if(checkInf&&Constant.checkValidation)
                etContactNo.requestFocus();
            checkInf=false;
        }
        else{
            etContactNo.setError(null);
        }

        if(!emailIdChecker)
        {
            etEmailId.setError("Please enter valid email ID");
            if(checkInf&&Constant.checkValidation)
                etEmailId.requestFocus();
            checkInf=false;
        }
        else{
            etEmailId.setError(null);
        }
    }

    // Displaying the alert related to Retailer
    @SuppressWarnings("deprecation")
    public void displayAlertForRetailerId(){
        final AlertDialog alertDialog = new AlertDialog.Builder( new ContextThemeWrapper ( RegisterActivity.this, R.style.AppCompatAlertDialogStyle)).create();
        alertDialog.setTitle("eTeach");
        alertDialog.setMessage("Please enter the retailer ID to avail offers on in-app purchases.\nFor support, call +91-712-6694444 or mail at support@eteach.co.in");

        alertDialog.setButton2("I'll enter the retailer ID", new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setButton("I'll enter it later, sign me up", new DialogInterface.OnClickListener() {

            public void onClick( DialogInterface dialog, int which) {
                new ServerResponseDetails().execute("");
            }
        });
        alertDialog.setIcon(R.drawable.logo);
        alertDialog.show();
    }

    // Server Response of Submited data on server.
    class ServerResponseDetails extends AsyncTask<String, String, String> {

        private ProgressDialog dialog;
        @Override
        protected
        String doInBackground( String... params) {

            return generateHddIdToJson();
        }

        @Override
        protected void onPostExecute(String result) {
            String val =null;
            accId=getJsonResponse(result);
            try {
                JSONObject jsonObject =new JSONObject ( result);
                JSONArray  hrArray    = jsonObject.getJSONArray( "posts");

                JSONObject post = hrArray.getJSONObject( 0)
                        .getJSONObject("post");
                val=post.getString("result");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(!val.equals("0")){
                //Log.e("acccid", accid);
                dialog.dismiss();

                Toast.makeText( getApplicationContext(), "Details submited successfully", Toast.LENGTH_SHORT).show();
                clearAllText();

                dialog.dismiss();
            }
            else{
                //Toast.makeText(getApplicationContext(),"Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            super.onPostExecute(result);
        }

        // Clear all the text in EditText
        private void clearAllText() {

            etSchoolName.setText("");
            etAddress.setText("");
            etCity.setText("");
            etCountry.setText("");
            etState.setText("");
            etPinCode.setText("");
            etUserName.setText("");
            etPassword.setText("");
            etConformPassword.setText("");
            etContactNo.setText("");
            etEmailId.setText("");
            etFirstName.setText("");
            etLastName.setText("");
            etRetailerName.setText("");
            etRetailerId.setText("");
            etRetailerLocation.setText("");
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog=new ProgressDialog ( RegisterActivity.this);
            dialog.setCancelable(false);
            dialog.setMessage("Submitting details. Please wait..." );
            dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
            dialog.show();
            super.onPreExecute();
        }

    }

    private
    String generateHddIdToJson() {
        String HDD_ID =getUdId();
        //String copanno=coupanText.getText().toString();
        String doin_res =null;
        try {

            HttpPost     httppost = new HttpPost("http://"+Constant.WEBSERVER+"/designo_pro/ws_user_registration.php?format=json");
            String       str      = "<UserInfo><V_HddId>" + HDD_ID + "</V_HddId><V_Name>" + vFirstName + "</V_Name><V_SurName>" + vLastName + "</V_SurName><V_ContactNo>" + vContactNo + "</V_ContactNo><V_Email>" + vEmailId + "</V_Email><V_Address>" + etAddress.getText().toString() + "</V_Address><V_SchoolName>" + etSchoolName.getText().toString() + "</V_SchoolName><V_City>" + etCity.getText().toString() + "</V_City><V_State>" + etState.getText().toString() + "</V_State><V_Country>" + etCountry.getText().toString() + "</V_Country><V_PinCode>" + etPinCode.getText().toString() + "</V_PinCode><V_UserName>" + vUserName + "</V_UserName><V_Password>" + vPassword + "</V_Password><V_RetailName>" + etRetailerName.getText().toString() + "</V_RetailName><V_RetailLocate>" + etRetailerLocation.getText().toString() + "</V_RetailLocate><V_RetailID>" + etRetailerId.getText().toString() + "</V_RetailID><V_AppName>" + Constant.appName + "</V_AppName><V_Version>" + apkVersion + "</V_Version></UserInfo>";
            StringEntity se       = new StringEntity(str,HTTP.UTF_8);

            se.setContentType("text/xml");
            httppost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
            httppost.setEntity(se);

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse =
                    httpclient.execute(httppost);

            HttpEntity     entity = httpResponse.getEntity();
            InputStream    is     = entity.getContent();
            BufferedReader reader = new BufferedReader ( new InputStreamReader ( is));
            StringBuilder  sb     = new StringBuilder ();
            String         line   = "";

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            doin_res = sb.toString();

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    private
    String getUdId() {

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
    String getJsonResponse( String res) {
        String result = null;
        checkRetailerIdNotEmpty=true;
        try {
            if(res!=null){
                JSONObject jsonObject =new JSONObject ( res);

                JSONArray hrArray = jsonObject.getJSONArray( "posts");

                JSONObject post = hrArray.getJSONObject( 0)
                        .getJSONObject("post");

                result = post.getString("result");
                Log.e( "ServerValue", result );

                String retailer_id =post.getString( "Retailer_Id");

                if(new File ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + "xxxx1234/NEWNAME").exists()){
                    final String oldname    = Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + "xxxx1234";
                    final String newName    = Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + result;
                    final String newOldName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + result + "/NEWNAME";
                    final String newNewName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + result + "/" + vUserName;
                    new File ( oldname).renameTo( new File ( newName));
                    checkMacPath=new File ( newOldName).renameTo( new File ( newNewName));
                }
                String checkRetailerId =this.etRetailerId.getText().toString();
                if(!checkRetailerId.equals("")){
                    if(retailer_id.equals("R"))
                    {
                        Toast.makeText( RegisterActivity.this, "Sorry!\nPlease enter valid Retailer Details!", Toast.LENGTH_LONG).show();
                        checkRetailerIdNotEmpty=false;
                    }
                }
                if(result.equals(null) || result.equals("0")){
                    //Toast.makeText(this, "Something went wrong! Please contact eTeach support.", Toast.LENGTH_SHORT).show();
                    checkRetailerIdNotEmpty=false;
                }
                else{
                    if(checkRetailerIdNotEmpty){
                        try {
                            Log.e( "MacFilePath", "" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + result + "/" + vUserName + "/macD.xml");
                            File macFile =new File ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + result + "/" + vUserName + "/macD.xml");
                            if(!macFile.exists()){
                                if(!checkMacPath){
                                    macFile.getParentFile().mkdirs();
                                }
                                macFile.createNewFile();
                                if(checkRetailerId.equals(""))
                                {
                                    checkRetailerId ="null";
                                }
                                RandomAccessFile rf = new RandomAccessFile ( macFile, "rw");
                                String           s  = "<Root><User><AcountId>" + result + "</AcountId><HddId>" + getUdId() + "</HddId><UserName>" + vUserName + "</UserName><RetId>" + checkRetailerId + "</RetId></User></Root>";
                                rf.write(s.getBytes());
                                rf.close();
                            }
                            FileInputStream  fis =new FileInputStream ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + result + "/" + vUserName + "/macD.xml");
                            FileOutputStream fos =new FileOutputStream ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + result + "/" + vUserName + "/mac.xml");

                            new Rijndael();
                            Rijndael.EDR( fis, fos, 1);

                            new DeleteFile ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + result + "/" + vUserName + "/macD.xml");
                            String hdId = getUdId();
                            UserXMLParser.addUserTOXML( new User ( vUserName, vPassword, hdId), result);//
                            SharedPreferences        settings = getApplicationContext().getSharedPreferences( "username", 0);
                            SharedPreferences.Editor editor   = settings.edit();
                            editor.putString("user", vUserName);
                            editor.putString("ACCOUNTID", result);
                            editor.commit();

                            downloadingReqXml(result);


                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Log.e("Resulttt", result);
        if(result==null)
            return null;
        else return result;

    }

    private void downloadingReqXml(String actId) {
        File f = new File ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/" + actId + "/" + vUserName);
        if(!f.exists())
        {
            f.mkdirs();
        }
        //		if(coupoun.getText().length()!=0)
        //		new DownloadUserFile(FirstTimeRegistration.this,f.getAbsolutePath().toString(),2).execute("/XMLFile");
        //		else
        new DownloadUserFile ( RegisterActivity.this, f.getAbsolutePath().toString(), 1).execute( "/XMLFile");
    }
}
