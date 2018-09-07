package com.dmw.eteachswayam.exo.fragment.drawer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.activity.HomeActivity;
import com.dmw.eteachswayam.exo.model.Constant;
import com.dmw.eteachswayam.exo.model.DeleteFile;
import com.dmw.eteachswayam.exo.model.Rijndael;
import com.dmw.eteachswayam.exo.model.XMLParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class MyProfileFragment extends Fragment {

    private ProgressDialog dialog;
    ProgressDialog dialog1;
    String         arryspinner1[], arryspinner2[], arryspinner3[];

    ArrayAdapter<String> adapter1, adapter2, adapter3;
    EditText etFirstName, psw,confPsw,oldpsw, etContactNo, etEmail, etLastName, etAddress, etCity, etState, etPinCode, etSchool,etCountry;
    String vFirstName,vpwd,vconfPwd,vLastName, vContactNo, vEmailId, vAddress, vCity, vState, vPinCode, vSchool, vCountry;
    String strFirstName,strLastName,strEmailId,strContactNo,strAddress,strCity,strState,strPincode,strCountry,strSchool;
    private boolean emailcheck;
    private boolean pstatus;
    Button buttonSubmit;
    Button jump;
    private CheckBox     changepassword;
    private String       actPassword;
    private TextView     retailarinupdatetextview;
    private TextView     retailarIdinupdatetextview;
    private String       accountIdeditProfile;
    private String       usernameeditProfile;
    private String       macEnc;
    private String       macDec;
    private LinearLayout layout;
    private boolean      checkupdateretId;

    public AsyncHttpClient client = new AsyncHttpClient();

    View view;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public
    View onCreateView( LayoutInflater inflater, ViewGroup container,
                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate( R.layout.fragment_my_profile, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences( "username", 0);
        accountIdeditProfile = settings.getString("ACCOUNTID", "xxx");
        usernameeditProfile = settings.getString("user", "xxx");
        macEnc = Constant.INTERNAL_SDCARD + accountIdeditProfile + "/" + usernameeditProfile + "/mac.xml";
        macDec = Constant.INTERNAL_SDCARD + accountIdeditProfile + "/" + usernameeditProfile + "/macD.xml";

        getid();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean flag = false;
                getValueAtTextField();
                if (changepassword.isChecked()) {
                if (oldpsw.getText().toString().equals(actPassword)) {
                    checkPassWordAndConfirmPassword(vpwd, vconfPwd);
                    flag=true;
                }
            }else {
                    flag = true;
                    pstatus = true;
                }

                checkEmail(vEmailId);
                setEditTextData();
                emptyText();
                readValidation();
                if (flag && pstatus == true && emailcheck == true && emptyText() && readValidation()) {
                    //intent.putExtra("user", createUser());
                    //	startActivityForResult(intent, 21);

                  //  new editProfileService().execute();
                    editProfileData();
                //    sendEditData();
                  // eventClick();

                }
            }
        });

        initcomponent();
      //  GetUserInfo();
        getProfile();

        return view;
    }

    private void getid() {

        etFirstName = (EditText ) view.findViewById( R.id.etMPFirstName);
        psw =(EditText )view.findViewById( R.id.passwordEditt);
        changepassword=(CheckBox )view.findViewById( R.id.changepassword);
        oldpsw = (EditText )view.findViewById( R.id.oldpassword);
        confPsw=(EditText )view.findViewById( R.id.confirmPasswordEdit);
        etSchool = (EditText ) view.findViewById( R.id.etMPSchool);
        buttonSubmit = (Button ) view.findViewById( R.id.btnMPSubmit);
        etLastName = (EditText ) view.findViewById( R.id.etMPLastName);
        etAddress = (EditText ) view.findViewById( R.id.etMPPostalAddress);
        etCity = (EditText ) view.findViewById( R.id.etMPCity);
        etCountry = (EditText ) view.findViewById( R.id.etMPCountry);
        etState = (EditText ) view.findViewById( R.id.etMPState);
        etPinCode = (EditText ) view.findViewById( R.id.etMPPinCode);

        etContactNo = (EditText ) view.findViewById( R.id.etMPContactNo);
        etEmail = (EditText ) view.findViewById( R.id.etMPEmailId);
        //   layout=(LinearLayout) findViewById(R.id.retailarinupdatelayout);

        File macFileChecking = new File ( macEnc);
        if (macFileChecking.exists()) {
            try {
                new Rijndael();
                FileInputStream  fileInputStream  = new FileInputStream ( macEnc);
                FileOutputStream fileOutputStream = new FileOutputStream ( macDec);
                  Rijndael.EDR( fileInputStream, fileOutputStream, 2);

                new XMLParser(macDec, "User", "RetId", "");
                String regIDInMac = XMLParser.child1Value;
               /* if (regIDInMac.equals("null")) {
                    retailarIdEditTextforeduslideinupdatetextview.setEnabled(true);

                } else {
                    retailarIdEditTextforeduslideinupdatetextview.setEnabled(false);
                }*/
                File deleteMac = new File ( macDec);
                if (deleteMac.exists()) {
                    deleteMac.delete();
                }

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    /*    buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean flag = false;
                getValueAtTextField();
                if (changepassword.isChecked()) {
                if (oldpsw.getText().toString().equals(actPassword)) {
                    checkPassWordAndConfirmPassword(vpwd, vconfPwd);
                    flag=true;
                }
            }else {
                    flag = true;
                    pstatus = true;
                }

                checkEmail(vEmailId);
                setEditTextData();
                emptyText();
                readValidation();
                if (flag && pstatus == true && emailcheck == true && emptyText() && readValidation()) {
                    //intent.putExtra("user", createUser());
                    //	startActivityForResult(intent, 21);

                    new editProfileService().execute();

                }
            }
        });*/
    }

    private void getValueAtTextField() {
    try {

        vFirstName = etFirstName.getText().toString();
        vLastName = etLastName.getText().toString();
        vconfPwd = confPsw.getText().toString();
        vpwd = psw.getText().toString();
        vContactNo = etContactNo.getText().toString();
        vEmailId = etEmail.getText().toString();
        vAddress = etAddress.getText().toString();
        vCity = etCity.getText().toString();
        vState = etState.getText().toString();
        vSchool = etSchool.getText().toString();
        vCountry = etCountry.getText().toString();
        vPinCode = etPinCode.getText().toString();

    }catch (Exception e){
        e.printStackTrace();
    }

    }

    public boolean checkPassWordAndConfirmPassword( String password, String confirmPassword) {
        pstatus = false;
        // if (confirmPassword != null && password != null)
        if (confirmPassword.length() != 0 && password.length() != 0) {
            if (password.length() > 4) {

                if (password.equals(confirmPassword)) {
                    Log.e( "pstatusssss", "" + pstatus);
                    pstatus = true;

                    //return pstatus;
                }
            } else {

                psw.setError(getString(R.string.error_invalid_password));
            }

        }
        return pstatus;
    }

    public boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile( ".+@.+\\.[a-z]+");
        Matcher matcher = pattern.matcher( email);
        emailcheck = matcher.matches();
        return emailcheck;
    }

    public boolean emptyText() {

        if (etAddress.getText().toString().length()==0) {
            etAddress.setError("Fill up Address");
            return false;
        } else {
            etAddress.setError(null);
        }
        if (etCity.getText().toString().length() == 0) {
            etCity.setError("Fill up City Name");
            return false;
        } else {
            etCity.setError(null);
        }

        if (etSchool.getText().toString().length() == 0) {
            etSchool.setError("Fill up School Name");
            return false;
        } else {
            etSchool.setError(null);
        }

        if (etState.getText().toString().length() == 0) {
            etState.setError("Fill up State Name");
            return false;
        } else {
            etState.setError(null);
        }

        if (etPinCode.getText().toString().length() == 0 || etPinCode.getText().toString().length() > 7) {
            etPinCode.setError("Fill up Pincode");
            return false;
        } else {
            etPinCode.setError(null);
        }
        return true;
    }

    public void setEditTextData(){

        try {
            strAddress = etAddress.getText().toString();
            strCity = etCity.getText().toString();
            strSchool = etSchool.getText().toString();
            strState = etState.getText().toString();
            strPincode = etPinCode.getText().toString();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean readValidation() {
        boolean var=false;
        Log.e( "pstatus", "" + pstatus);

        if(vFirstName.matches(""))
        {
            etFirstName.setError("Enter first name");
        }
        if(!pstatus==true)
        {
            psw.setError("password mismatch");
            confPsw.setError("password mismatch");
        }
        if(vContactNo.matches("")||vContactNo.length()<9)
        {
            etContactNo.setError("Enter contact no.");
        }
        if(vLastName.matches(""))
        {
            etLastName.setError("Enter last Name");
        }
        if(!emailcheck==true)
        {
            etEmail.setError("Invalid email ID");
        }
        else if((pstatus==true)&& (emailcheck==true) && (vContactNo.length()!=0)&&(vFirstName.length()!=0)&&(vLastName.length()!=0))
        {
            var =true;
            psw.setError(null);
            confPsw.setError(null);
            etContactNo.setError(null);
            etEmail.setError(null);
            etFirstName.setError(null);
            etLastName.setError(null);
        }
        return var;
    }

    /*class editProfileService extends AsyncTask<String, String, String> {

        private ProgressDialog dialog;

      *//*  @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog=new ProgressDialog(getActivity());
            dialog.setCancelable(false);
            dialog.setMessage("Submitting details. Please wait..." );
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            super.onPreExecute();
        }*//*


        @Override
        protected String doInBackground(String... params) {;

            String doin_res=null;

            try {

            //    String idRet=retailarIdEditTextforeduslideinupdatetextview.getText().toString();
                HttpPost httppost = new HttpPost("http://"+Constant.WEBSERVER+"/designo_pro/ws_profileupdate.php?format=json");
            //    String str=  "<UserInfo><V_Name>"+vFirstName+"</V_Name><V_SurName>"+vLastName+"</V_SurName><V_ContactNo>"+vContactNo+"</V_ContactNo><V_Email>"+vEmailId+"</V_Email><V_Address>"+vAddress+"</V_Address><V_SchoolName>"+vSchool+"</V_SchoolName><V_City>"+vCity+"</V_City><V_State>"+vState+"</V_State><V_Country>"+vCountry+"</V_Country><V_PinCode>"+vPinCode+"</V_PinCode><V_UserName>"+vusrnm+"</V_UserName><V_RetailID>"+idRet+"</V_RetailID>";
                String str=  "<UserInfo><V_Name>"+vFirstName+"</V_Name><V_SurName>"+vLastName+"</V_SurName><V_ContactNo>"+vContactNo+"</V_ContactNo><V_Email>"+vEmailId+"</V_Email><V_Address>"+vAddress+"</V_Address><V_SchoolName>"+vSchool+"</V_SchoolName><V_City>"+vCity+"</V_City><V_State>"+vState+"</V_State><V_Country>"+vCountry+"</V_Country><V_PinCode>"+vPinCode+"</V_PinCode>";

               *//* if(changepassword.isChecked()){
                    str=str.concat("<V_Password>"+vpwd+"</V_Password></UserInfo>");
                }else{
                    str=str.concat("<V_Password/></UserInfo>");
                }*//*


                StringEntity se = new StringEntity(str, HTTP.UTF_8);

                se.setContentType("text/xml");
                httppost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
                httppost.setEntity(se);

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse =
                        httpclient.execute(httppost);

                HttpEntity entity = httpResponse.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";

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
        @Override
        protected void onPostExecute(String result) {

            checkupdateretId=true;

            try {
                if(result!=null)
                {
                    JSONObject jsonObject =new JSONObject(result);

                    JSONArray hrArray = jsonObject.getJSONArray("posts");

                    JSONObject post = hrArray.getJSONObject(0)
                            .getJSONObject("post");

                    String res = post.getString("result");
                    String RetailerId = post.getString("Retailer_Id");
                    Log.e("ServerValue",res );
                    if(RetailerId.equals("R"))
                    {
                        Toast.makeText(getActivity(), "Sorry!\nPlease enter valid Retailer Details!", Toast.LENGTH_SHORT).show();
                        checkupdateretId=false;
                    }
                    if(res.equals("0")){
                        //Toast.makeText(EditProfileActivity.this, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                        checkupdateretId=false;
                    }
                    else if(res.equals("1")) {
                        if(checkupdateretId){
                            Toast.makeText(getActivity()," Details Submited Successfully", Toast.LENGTH_SHORT).show();

                            File macfileChecking=new File(macEnc);
                            if(macfileChecking.exists())
                            {

                                try {
                                    new Rijndael();
                                    FileInputStream fileInputStream=new FileInputStream(macEnc);
                                    FileOutputStream fileOutputStream=new FileOutputStream(macDec);
                                    Rijndael.EDR(fileInputStream, fileOutputStream, 2);

                                    new XMLParser(macDec,"User","RetId","");
                                    String regIDInMac = XMLParser.child1Value;
                                    new DeleteFile(macEnc);
                                 //   String reId=retailarIdEditTextforeduslideinupdatetextview.getText().toString();
                                  *//*  if(regIDInMac.equals("null"))
                                    {
                                        XML.editByDOM(macDec, "User", "RetId", reId);
                                    }*//*
                                    FileInputStream fileInputStream1=new FileInputStream(macDec);
                                    FileOutputStream fileOutputStream1=new FileOutputStream(macEnc);
                                    Rijndael.EDR(fileInputStream1, fileOutputStream1, 1);
                                    File deleteMac=new File(macDec);
                                    if(deleteMac.exists())
                                    {
                                        deleteMac.delete();
                                    }
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            Intent intent =new Intent(getActivity(),HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        //    dialog.dismiss();
            super.onPostExecute(result);
        }

    }*/

    private void initcomponent() {
        changepassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    view.findViewById(R.id.passwordEditt).setVisibility( View.VISIBLE);
                    view.findViewById(R.id.oldpassword).setVisibility( View.VISIBLE);
                    view.findViewById(R.id.confirmPasswordEdit).setVisibility( View.VISIBLE);
                    view.findViewById(R.id.OldtextView2).setVisibility( View.VISIBLE);
                    view.findViewById(R.id.PasswordtextView3).setVisibility( View.VISIBLE);
                    view.findViewById(R.id.ConfirmtextView4).setVisibility( View.VISIBLE);
                    view.findViewById(R.id.LogintextView1).setVisibility( View.VISIBLE);

                }else{
                    view.findViewById(R.id.passwordEditt).setVisibility( View.INVISIBLE);
                    view.findViewById(R.id.oldpassword).setVisibility( View.INVISIBLE);
                    view.findViewById(R.id.confirmPasswordEdit).setVisibility( View.INVISIBLE);
                    view.findViewById(R.id.OldtextView2).setVisibility( View.INVISIBLE);
                    view.findViewById(R.id.PasswordtextView3).setVisibility( View.INVISIBLE);
                    view.findViewById(R.id.ConfirmtextView4).setVisibility( View.INVISIBLE);
                    view.findViewById(R.id.LogintextView1).setVisibility( View.INVISIBLE);
                }
            }
        });

    }

   /* private void GetUserInfo() {
        SharedPreferences settings = getActivity().getSharedPreferences("username", 0);
        final String username = settings.getString("user","xxx");
        new AsyncTask<String, String, String>() {
            private ProgressDialog mProgressDialog;

            protected void onPreExecute() {
                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setMessage("Fetching  information from server. Please wait...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

            };
            @Override
            protected String doInBackground(String... params) {
                String url= "http://"+Constant.WEBSERVER+"/designo_pro/ws_getuser.php?usrnm="+params[0]+"&format=json";
*//*                String doin_res=null;
                try {
                    String url= "http://"+Constant.WEBSERVER+"/designo_pro/ws_getuser.php?usrnm="+params[0]+"&format=json";
                    HttpGet httpget = new HttpGet(url);

                    HttpClient httpclient = new DefaultHttpClient();
                    BasicHttpResponse httpResponse =
                            (BasicHttpResponse) httpclient.execute(httpget);

                    HttpEntity entity = httpResponse.getEntity();
                    InputStream is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

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
                }*//*

                client.post(url, new JsonHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // called when response HTTP status is "200 OK"
                        try{
                            JSONArray hrArray = response.getJSONArray("posts");

                            JSONObject post = hrArray.getJSONObject(0)
                                    .getJSONObject("post");

                            String V_Name = post.getString("V_Name");
                            String V_SurName = post.getString("V_SurName");
                            String V_CantactNo = post.getString("V_CantactNo");
                            String V_EMailID = post.getString("V_EMailID");
                            String V_Address = post.getString("V_Address");
                            String V_SchoolName = post.getString("V_SchoolName");
                            String V_City = post.getString("V_City");
                            String V_State = post.getString("V_State");
                            String V_Country = post.getString("V_Country");
                            String V_PinCode = post.getString("V_PinCode");

                            actPassword = post.getString("V_Password");

                            etContactNo.setText(V_CantactNo);
                            etEmail.setText(V_EMailID);
                            etFirstName.setText(V_Name);
                            etLastName.setText(V_SurName);
                            etSchool.setText(V_SchoolName);
                            etAddress.setText(V_Address);
                            etCity.setText(V_City);

                            etState.setText(V_State);
                            etCountry.setText(V_Country);
                            etPinCode.setText(V_PinCode);

                            if(username.equals("eteach")){

                                etContactNo.setEnabled(false);
                                etEmail.setEnabled(false);
                                etFirstName.setEnabled(false);
                                etLastName.setEnabled(false);
                                etSchool.setEnabled(false);
                                etAddress.setEnabled(false);
                                etCity.setEnabled(false);
                                etState.setEnabled(false);
                                etCountry.setEnabled(false);
                                etPinCode.setEnabled(false);
                                changepassword.setEnabled(false);
                                Toast.makeText(getActivity(), "This is a Demo account.\n You can not edit this account. ", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }


                });
                return null;
            }

            protected void onPostExecute(String res) {
              *//*  try {
                    if(res!=null){
                        JSONObject jsonObject = new JSONObject(res);

                        JSONArray hrArray = jsonObject.getJSONArray("posts");

                        JSONObject post = hrArray.getJSONObject(0)
                                .getJSONObject("post");

                        String V_Name = post.getString("V_Name");
                        String V_SurName = post.getString("V_SurName");
                        String V_CantactNo = post.getString("V_CantactNo");
                        String V_EMailID = post.getString("V_EMailID");
                        String V_Address = post.getString("V_Address");
                        String V_SchoolName = post.getString("V_SchoolName");
                        String V_City = post.getString("V_City");
                        String V_State = post.getString("V_State");
                        String V_Country = post.getString("V_Country");
                        String V_PinCode = post.getString("V_PinCode");
                        // String V_UserName = post.getString("V_UserName");
                        // String retailarID=post.getString("V_RetailID");
                        actPassword = post.getString("V_Password");
                        // usrnm.setText(V_UserName);
                        etContactNo.setText(V_CantactNo);
                        etEmail.setText(V_EMailID);
                        etFirstName.setText(V_Name);
                        etLastName.setText(V_SurName);
                        etSchool.setText(V_SchoolName);
                        etAddress.setText(V_Address);
                        etCity.setText(V_City);
                       *//**//* if(!retailarID.equals("null")){
                            retailarIdEditTextforeduslideinupdatetextview.setText(retailarID);
                        }*//**//*
                        etState.setText(V_State);
                        etCountry.setText(V_Country);
                        etPinCode.setText(V_PinCode);

                        if(username.equals("eteach")){
                            //    usrnm.setEnabled(false);
                            etContactNo.setEnabled(false);
                            etEmail.setEnabled(false);
                            etFirstName.setEnabled(false);
                            etLastName.setEnabled(false);
                            etSchool.setEnabled(false);
                            etAddress.setEnabled(false);
                            etCity.setEnabled(false);
                            etState.setEnabled(false);
                            etCountry.setEnabled(false);
                            etPinCode.setEnabled(false);
                            changepassword.setEnabled(false);
                            Toast.makeText(getActivity(), "This is a Demo account.\n You can not edit this account. ", Toast.LENGTH_LONG).show();
                        }

                    }
                    //					else{
                    //						Toast.makeText(EditProfileActivity.this, "Something waint wrong", Toast.LENGTH_SHORT).show();
                    //					}
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*//*
                mProgressDialog.dismiss();
            }
        }.execute(username);
    }*/

    public void getProfile(){

        final ProgressDialog progress = new ProgressDialog ( getActivity());
        progress.setMessage("Please Wait...");
        progress.setIndeterminate(false);
        progress.setCancelable(false);

        SharedPreferences settings = getActivity().getSharedPreferences( "username", 0);
        final String      username = settings.getString( "user", "xxx");
        String            url      = "http://" + Constant.WEBSERVER + "/designo_pro/ws_getuser.php?usrnm=" + username + "&format=json";


        progress.show();
        client.post(url, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                try{
                    JSONArray hrArray = response.getJSONArray( "posts");

                    JSONObject post = hrArray.getJSONObject( 0)
                            .getJSONObject("post");

                    String V_Name       = post.getString( "V_Name");
                    String V_SurName    = post.getString( "V_SurName");
                    String V_CantactNo  = post.getString( "V_CantactNo");
                    String V_EMailID    = post.getString( "V_EMailID");
                    String V_Address    = post.getString( "V_Address");
                    String V_SchoolName = post.getString( "V_SchoolName");
                    String V_City       = post.getString( "V_City");
                    String V_State      = post.getString( "V_State");
                    String V_Country    = post.getString( "V_Country");
                    String V_PinCode    = post.getString( "V_PinCode");

                    actPassword = post.getString("V_Password");

                    etContactNo.setText(V_CantactNo);
                    etEmail.setText(V_EMailID);
                    etFirstName.setText(V_Name);
                    etLastName.setText(V_SurName);
                    if (!V_SchoolName.isEmpty()) {
                        etSchool.setText(V_SchoolName);
                    }
                    if (!V_Address.isEmpty()) {
                        etAddress.setText(V_Address);
                    }
                    if (!V_City.isEmpty()) {
                        etCity.setText(V_City);
                    }
                    if (!V_State.isEmpty()) {
                        etState.setText(V_State);
                    }
                    if (!V_Country.isEmpty()) {
                        etCountry.setText(V_Country);
                    }
                    if (!V_PinCode.isEmpty()) {
                        etPinCode.setText(V_PinCode);
                    }

                    if(username.equals("eteach")){

                        etContactNo.setEnabled(false);
                        etEmail.setEnabled(false);
                        etFirstName.setEnabled(false);
                        etLastName.setEnabled(false);
                        etSchool.setEnabled(false);
                        etAddress.setEnabled(false);
                        etCity.setEnabled(false);
                        etState.setEnabled(false);
                        etCountry.setEnabled(false);
                        etPinCode.setEnabled(false);
                        changepassword.setEnabled(false);
                        Toast.makeText( getActivity(), "This is a Demo account.\n You can not edit this account. ", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                progress.dismiss();
            }

            @Override
            public void onFailure( int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progress.dismiss();
            }
        });


    }

    public void editProfileData(){

        Context context = getActivity().getApplicationContext();
        String  str     = "<UserInfo><V_Name>" + vFirstName + "</V_Name><V_SurName>" + vLastName + "</V_SurName><V_ContactNo>" + vContactNo + "</V_ContactNo><V_Email>" + vEmailId + "</V_Email><V_Address>" + vAddress + "</V_Address><V_SchoolName>" + vSchool + "</V_SchoolName><V_City>" + vCity + "</V_City><V_State>" + vState + "</V_State><V_Country>" + vCountry + "</V_Country><V_PinCode>" + vPinCode + "</V_PinCode>";

        AsyncHttpClient client = new AsyncHttpClient();
        String          url    = "http://" + Constant.WEBSERVER + "/designo_pro/ws_profileupdate.php?format=json";
      //  JSONObject params;
        RequestParams params;

        params = new RequestParams();
     //   params = new JSONObject();


            params.put("V_Name", vFirstName);


        params.put("V_SurName", vLastName);
        params.put("V_ContactNo", vContactNo);
        params.put("V_Email", vEmailId);
        params.put("V_Address", vAddress);
        params.put("V_SchoolName", vSchool);
        params.put("V_City", vCity);
        params.put("V_State", vState);
        params.put("V_Country", vCountry);
        params.put("V_PinCode", vPinCode);

        //    StringEntity entity = null;

         //   entity = new StringEntity(params.toString());
         //   entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));


        client.get(url,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.e( "Response", "" + response + "  " + statusCode);
                checkupdateretId=true;

                try {
                    if(response!=null)
                    {
                        // JSONObject jsonObject =new JSONObject(result);

                        JSONArray hrArray = response.getJSONArray( "posts");

                        JSONObject post = hrArray.getJSONObject( 0)
                                .getJSONObject("post");

                        String res        = post.getString( "result");
                        String RetailerId = post.getString( "Retailer_Id");
                        Log.e( "ServerValue", res );
                        if(RetailerId.equals("R"))
                        {
                            Toast.makeText( getActivity(), "Sorry!\nPlease enter valid Retailer Details!", Toast.LENGTH_SHORT).show();
                            checkupdateretId=false;
                        }
                        if(res.equals("0")){
                            //Toast.makeText(EditProfileActivity.this, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                            checkupdateretId=false;
                        }
                        else if(res.equals("1")) {
                            if(checkupdateretId){
                                Toast.makeText( getActivity(), " Details Submited Successfully", Toast.LENGTH_SHORT).show();

                                File macfileChecking =new File ( macEnc);
                                if(macfileChecking.exists())
                                {

                                    try {
                                        new Rijndael();
                                        FileInputStream  fileInputStream  =new FileInputStream ( macEnc);
                                        FileOutputStream fileOutputStream =new FileOutputStream ( macDec);
                                        Rijndael.EDR(fileInputStream, fileOutputStream, 2);

                                        new XMLParser(macDec,"User","RetId","");
                                        String regIDInMac = XMLParser.child1Value;
                                        new DeleteFile ( macEnc);
                                        //   String reId=retailarIdEditTextforeduslideinupdatetextview.getText().toString();
                                  /*  if(regIDInMac.equals("null"))
                                    {
                                        XML.editByDOM(macDec, "User", "RetId", reId);
                                    }*/
                                        FileInputStream  fileInputStream1  =new FileInputStream ( macDec);
                                        FileOutputStream fileOutputStream1 =new FileOutputStream ( macEnc);
                                        Rijndael.EDR(fileInputStream1, fileOutputStream1, 1);
                                        File deleteMac =new File ( macDec);
                                        if(deleteMac.exists())
                                        {
                                            deleteMac.delete();
                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }

                                Intent intent =new Intent ( getActivity(), HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //    dialog.dismiss();
            }

        });

    }

  /*  public void  sendEditData(){

        String doin_res=null;

        try {

            //    String idRet=retailarIdEditTextforeduslideinupdatetextview.getText().toString();
            HttpPost httppost = new HttpPost("http://"+Constant.WEBSERVER+"/designo_pro/ws_profileupdate.php?format=json");
            //    String str=  "<UserInfo><V_Name>"+vFirstName+"</V_Name><V_SurName>"+vLastName+"</V_SurName><V_ContactNo>"+vContactNo+"</V_ContactNo><V_Email>"+vEmailId+"</V_Email><V_Address>"+vAddress+"</V_Address><V_SchoolName>"+vSchool+"</V_SchoolName><V_City>"+vCity+"</V_City><V_State>"+vState+"</V_State><V_Country>"+vCountry+"</V_Country><V_PinCode>"+vPinCode+"</V_PinCode><V_UserName>"+vusrnm+"</V_UserName><V_RetailID>"+idRet+"</V_RetailID>";
            String str=  "<UserInfo><V_Name>"+vFirstName+"</V_Name><V_SurName>"+vLastName+"</V_SurName><V_ContactNo>"+vContactNo+"</V_ContactNo><V_Email>"+vEmailId+"</V_Email><V_Address>"+vAddress+"</V_Address><V_SchoolName>"+vSchool+"</V_SchoolName><V_City>"+vCity+"</V_City><V_State>"+vState+"</V_State><V_Country>"+vCountry+"</V_Country><V_PinCode>"+vPinCode+"</V_PinCode>";

               *//* if(changepassword.isChecked()){
                    str=str.concat("<V_Password>"+vpwd+"</V_Password></UserInfo>");
                }else{
                    str=str.concat("<V_Password/></UserInfo>");
                }*//*


            StringEntity se = new StringEntity(str, HTTP.UTF_8);

            se.setContentType("text/xml");
            httppost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
            httppost.setEntity(se);

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse =
                    httpclient.execute(httppost);

            HttpEntity entity = httpResponse.getEntity();
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = "";

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            doin_res = sb.toString();
            jsonOutput(doin_res);

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

    }*/

    public void jsonOutput(String outPut){


        checkupdateretId=true;

        try {
            if(outPut!=null)
            {
                JSONObject jsonObject =new JSONObject ( outPut);

                JSONArray hrArray = jsonObject.getJSONArray( "posts");

                JSONObject post = hrArray.getJSONObject( 0)
                        .getJSONObject("post");

                String res        = post.getString( "result");
                String RetailerId = post.getString( "Retailer_Id");
                Log.e( "ServerValue", res );
                if(RetailerId.equals("R"))
                {
                    Toast.makeText( getActivity(), "Sorry!\nPlease enter valid Retailer Details!", Toast.LENGTH_SHORT).show();
                    checkupdateretId=false;
                }
                if(res.equals("0")){
                    //Toast.makeText(EditProfileActivity.this, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                    checkupdateretId=false;
                }
                else if(res.equals("1")) {
                    if(checkupdateretId){
                        Toast.makeText( getActivity(), " Details Submited Successfully", Toast.LENGTH_SHORT).show();

                        File macfileChecking =new File ( macEnc);
                        if(macfileChecking.exists())
                        {

                            try {
                                new Rijndael();
                                FileInputStream  fileInputStream  =new FileInputStream ( macEnc);
                                FileOutputStream fileOutputStream =new FileOutputStream ( macDec);
                                Rijndael.EDR(fileInputStream, fileOutputStream, 2);

                                new XMLParser(macDec,"User","RetId","");
                                String regIDInMac = XMLParser.child1Value;
                                new DeleteFile(macEnc);
                                //   String reId=retailarIdEditTextforeduslideinupdatetextview.getText().toString();
                                  /*  if(regIDInMac.equals("null"))
                                    {
                                        XML.editByDOM(macDec, "User", "RetId", reId);
                                    }*/
                                FileInputStream  fileInputStream1  =new FileInputStream ( macDec);
                                FileOutputStream fileOutputStream1 =new FileOutputStream ( macEnc);
                                Rijndael.EDR(fileInputStream1, fileOutputStream1, 1);
                                File deleteMac =new File ( macDec);
                                if(deleteMac.exists())
                                {
                                    deleteMac.delete();
                                }
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        Intent intent =new Intent ( getActivity(), HomeActivity.class);
                        startActivity(intent);
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void startLoader(){
        dialog=new ProgressDialog ( getActivity());
        dialog.setCancelable(true);
        dialog.setMessage("Loading. Please wait...");
        dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    /*private class UpdateAsyncTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            String doin_res=null;

            try {

                //    String idRet=retailarIdEditTextforeduslideinupdatetextview.getText().toString();
                HttpPost httppost = new HttpPost("http://"+Constant.WEBSERVER+"/designo_pro/ws_profileupdate.php?format=json");
                //    String str=  "<UserInfo><V_Name>"+vFirstName+"</V_Name><V_SurName>"+vLastName+"</V_SurName><V_ContactNo>"+vContactNo+"</V_ContactNo><V_Email>"+vEmailId+"</V_Email><V_Address>"+vAddress+"</V_Address><V_SchoolName>"+vSchool+"</V_SchoolName><V_City>"+vCity+"</V_City><V_State>"+vState+"</V_State><V_Country>"+vCountry+"</V_Country><V_PinCode>"+vPinCode+"</V_PinCode><V_UserName>"+vusrnm+"</V_UserName><V_RetailID>"+idRet+"</V_RetailID>";
                String str=  "<UserInfo><V_Name>"+vFirstName+"</V_Name><V_SurName>"+vLastName+"</V_SurName><V_ContactNo>"+vContactNo+"</V_ContactNo><V_Email>"+vEmailId+"</V_Email><V_Address>"+vAddress+"</V_Address><V_SchoolName>"+vSchool+"</V_SchoolName><V_City>"+vCity+"</V_City><V_State>"+vState+"</V_State><V_Country>"+vCountry+"</V_Country><V_PinCode>"+vPinCode+"</V_PinCode>";

               *//* if(changepassword.isChecked()){
                    str=str.concat("<V_Password>"+vpwd+"</V_Password></UserInfo>");
                }else{
                    str=str.concat("<V_Password/></UserInfo>");
                }*//*


                StringEntity se = new StringEntity(str, HTTP.UTF_8);

                se.setContentType("text/xml");
                httppost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
                httppost.setEntity(se);

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse =
                        httpclient.execute(httppost);

                HttpEntity entity = httpResponse.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                doin_res = sb.toString();
                jsonOutput(doin_res);

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

            return null;
        }

    }*/

    @Override
    public void onAttach(Activity activity) {
        Log.d( getClass().getName(), "[onAttach]");
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        Log.d( getClass().getName(), "[onDetach]");
        super.onDetach();
    }

    public void getUserData(){
        SharedPreferences settings = getActivity().getSharedPreferences( "username", 0);
        final String      username = settings.getString( "user", "xxx");
        Constant.ProfGetData = "/designo_pro/ws_getuser.php?usrnm="+username+"&format=json";
    }


}
