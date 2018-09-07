/* This Class used for Login the user */
package com.dmw.eteachswayam.exo.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.MainActivity;
import com.dmw.eteachswayam.exo.model.Constant;
import com.dmw.eteachswayam.exo.model.DeleteFile;
import com.dmw.eteachswayam.exo.model.Reload;
import com.dmw.eteachswayam.exo.model.Rijndael;
import com.dmw.eteachswayam.exo.model.UserXMLParser;
import com.dmw.eteachswayam.exo.model.XML;
import com.dmw.eteachswayam.exo.model.XMLParser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHttpResponse;

@SuppressWarnings ( "ALL" )
@SuppressLint ( "Registered" )
public
class LoginActivity extends AppCompatActivity {

    private Button btnRegister;
    private Button btnLogin;
    private TextView txtForgotPassword;
    private EditText etUserName;
    private EditText etPassword;
    private        String             upToString;
    private        View               mLoginFormView;
    private        View               mLoginStatusView;
    private        UserXMLParser      par;
    private        CheckBox           cbRememberMe;
    private        ArrayList <String> headingArrayList;
    private        ArrayList <String> bodyArrayList;
    private        EditText           mEmailView;
    private        EditText           mPasswordView;
    private        TextView           forgotpassword;
    private static double             MB_Available;
    private        TextView           mLoginStatusMessageView;

    // Values for email and password at the time of the login attempt.
    private static String mEmail;
    private        String mPassword;

    @Nullable
    private UserLoginTask mAuthTask = null;

    private SharedPreferences        icon;
    private SharedPreferences.Editor iconEditor;

    private float width;
    private int   height;

    private String accountId;
    private String username;
    private String uptoString;

    public static
    double getMB_Available ( ) {
        return MB_Available;
    }

    public static
    void setMB_Available ( double MB_Available ) {
        LoginActivity.MB_Available = MB_Available;
    }

    public static
    String getmEmail ( ) {
        return mEmail;
    }

    public static
    void setmEmail ( String mEmail ) {
        LoginActivity.mEmail = mEmail;
    }

    // Back Button event
    @Override
    public
    boolean onKeyDown ( int keyCode , KeyEvent event ) {
        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            createDialog ( );
            return true;
        }
        return super.onKeyDown ( keyCode , event );
    }

    // Back Button event AlertDialog
    @SuppressWarnings ( "deprecation" )
    public
    void createDialog ( ) {
        final AlertDialog alertDialog = new AlertDialog.Builder ( this ).create ( );
        alertDialog.setTitle ( "eTeach" );
        alertDialog.setMessage ( "Do you want to exit?" );
        alertDialog.setButton2 ( "Yes" , new DialogInterface.OnClickListener ( ) {
            public
            void onClick ( DialogInterface dialog , int which ) {

                moveTaskToBack ( true );
                java.lang.System.exit ( 0 );
                finish ( );
            }
        } );
        alertDialog.setButton ( "No" , new DialogInterface.OnClickListener ( ) {

            public
            void onClick ( DialogInterface dialog , int which ) {
                alertDialog.dismiss ( );
            }
        } );
        alertDialog.setIcon ( R.drawable.logo );
        alertDialog.show ( );
    }

    @SuppressLint ( { "WrongConstant" , "CutPasteId" , "CommitPrefEdits" } )
    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );
        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );

        SharedPreferences settings = getSharedPreferences ( "PREFERENCE" , 0 );
        setPar ( new UserXMLParser ( this ) );

        //   Constant.ScreenSize=new Scale(this).screenSize;

        //  BugSenseHandler.initAndStartSession(this, Constant.BugsenseKey);
        //  setContentView(R.layout.activity_login);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setXmlForCaptcha ( );
        //  createShortcut();

        setIcon ( getSharedPreferences ( "ICON" , MODE_APPEND ) );
        setIconEditor ( getIcon ( ).edit ( ) );
        getIconEditor ( ).putString ( "icon" , "1" );
        getIconEditor ( ).commit ( );

        setBtnRegister ( ( Button ) findViewById ( R.id.btnRegister ) );
        setBtnLogin ( ( Button ) findViewById ( R.id.btnLogin ) );
        setTxtForgotPassword ( ( TextView ) findViewById ( R.id.tvForgotPassword ) );
        setEtUserName ( ( EditText ) findViewById ( R.id.etEmailIdLogin ) );
        setEtPassword ( ( EditText ) findViewById ( R.id.etPasswordLogin ) );

        setHeadingArrayList ( new ArrayList <String> ( ) );
        setBodyArrayList ( new ArrayList <String> ( ) );
        setCbRememberMe ( ( CheckBox ) findViewById ( R.id.cbLogin ) );
        setmEmailView ( ( EditText ) findViewById ( R.id.etEmailIdLogin ) );
        setForgotpassword ( ( TextView ) findViewById ( R.id.tvForgotPassword ) );

        setmPasswordView ( ( EditText ) findViewById ( R.id.etPasswordLogin ) );
        SharedPreferences settings1 = getApplicationContext ( ).getSharedPreferences ( "username" , 0 );
        setAccountId ( settings1.getString ( "ACCOUNTID" , "xxx" ) );
        setUsername ( settings1.getString ( "user" , "xxx" ) );
        setUptoString ( Constant.INTERNAL_SDCARD + "/" + getAccountId ( ) + "/" + getUsername ( ) );

        setmLoginFormView ( findViewById ( R.id.login_form ) );
        setmLoginStatusView ( findViewById ( R.id.login_status ) );

        getmEmailView ( ).setOnFocusChangeListener ( new View.OnFocusChangeListener ( ) {
            @Override
            public
            void onFocusChange ( View v , boolean hasFocus ) {
                if ( hasFocus )
                    getmEmailView ( ).setHint ( "" );
                else getmEmailView ( ).setHint ( "Username" );

            }
        } );


        getmPasswordView ( ).setOnFocusChangeListener ( new View.OnFocusChangeListener ( ) {

            @Override
            public
            void onFocusChange ( View v , boolean hasFocus ) {
                if ( hasFocus )
                    getmPasswordView ( ).setHint ( "" );
                else {
                    getmPasswordView ( ).setHint ( "**********" );
                }
            }
        } );

        getBtnLogin ( ).setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View v ) {
/*
                new Rijndael ( );
                try {
                    FileInputStream  fileInputStream  = new FileInputStream ( getUptoString ( ) + "/mac.xml" );
                    FileOutputStream fileOutputStream = new FileOutputStream ( getUptoString ( ) + "/macD.xml" );
                    Rijndael.EDR ( fileInputStream , fileOutputStream , 2 );

                }
                catch ( FileNotFoundException e ) {
                    // TODO Auto-generated catch block
                    e.printStackTrace ( );
                }

                new XMLParser ( getUptoString ( ) + "/macD.xml" , "User" , "capchacount" , "" );
                String Name = XMLParser.child1Value;
                if ( Name.equals ( "25" ) ) {

                    new DeleteFile ( getUptoString ( ) + "/macD.xml" );
                    Intent intent = new Intent ( LoginActivity.this , CreateCapchaActivity.class );
                    intent.putExtra ( "uptoString" , getUptoString ( ) );
                    startActivity ( intent );

                } else {
                    checkCapchacount ( );
                }
*/
                Intent intent = new Intent ( LoginActivity.this , RegisterActivity.class );
                startActivityForResult ( intent , 12 );
            }
        } );

        getBtnRegister ( ).setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View v ) {
                if ( isDataEnable ( ) ) {
                    Intent intent = new Intent ( LoginActivity.this , RegisterActivity.class );
                    startActivityForResult ( intent , 12 );
                } else {
                    Toast.makeText ( LoginActivity.this , "Your internet connection is not available." , Toast.LENGTH_LONG ).show ( );
                }
            }
        } );

        getTxtForgotPassword ( ).setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View v ) {
                if ( isDataEnable ( ) ) {
                    if ( ! getEtUserName ( ).getText ( ).toString ( ).equals ( "" ) )
                        setTxtForgotPassword ( getEtUserName ( ).getText ( ).toString ( ) );
                    else {
                        final AlertDialog alertDialog = new AlertDialog.Builder ( new ContextThemeWrapper ( LoginActivity.this , R.style.AppCompatAlertDialogStyle ) ).create ( );
                        alertDialog.setTitle ( "eTeach" );
                        alertDialog.setMessage ( "Please enter a valid Username." );
                        alertDialog.setButton ( "OK" , new DialogInterface.OnClickListener ( ) {

                            public
                            void onClick ( DialogInterface dialog , int which ) {
                                alertDialog.dismiss ( );
                            }
                        } );
                        alertDialog.setIcon ( R.drawable.logo );
                        alertDialog.show ( );
                    }
                } else {
                    Toast.makeText ( LoginActivity.this , "Your internet connection is not available." , Toast.LENGTH_LONG ).show ( );
                }
            }
        } );
        requestStoragePermossion();
    }

    private
    void requestStoragePermossion ( ) {
        Dexter.withActivity ( this ).withPermissions ( Manifest.permission.READ_EXTERNAL_STORAGE,
                                                       Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener ( new MultiplePermissionsListener ( ) {
                    @Override
                    public
                    void onPermissionsChecked ( MultiplePermissionsReport report ) {
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public
                    void onPermissionRationaleShouldBeShown ( List <PermissionRequest> permissions , PermissionToken token ) {

                    }
                } ).withErrorListener ( new PermissionRequestErrorListener ( ) {
            @Override
            public
            void onError ( DexterError error ) {
                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        } ).onSameThread ().check ();
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts( "package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    @Override
    protected
    void onResume ( ) {

        SharedPreferences spLogin = this.getSharedPreferences ( "Login" , 0 );

        String strUser = spLogin.getString ( "username" , null );
        String strPwd  = spLogin.getString ( "password" , null );
        if ( strUser != null && strPwd != null && ! strPwd.equals ( "eteach" ) && ! strUser.equals ( "eteach" ) ) {
            startActivity ( new Intent ( LoginActivity.this , HomeActivity.class ) );
            finish ( );
        }
        super.onResume ( );
    }

    private
    void setXmlForCaptcha ( ) {
        SharedPreferences        preferences           = PreferenceManager.getDefaultSharedPreferences ( LoginActivity.this );
        SharedPreferences.Editor editor                = preferences.edit ( );
        String                   sharedPreferencesDate = preferences.getString ( "chapechdate" , "" );
        if ( sharedPreferencesDate.equals ( "" ) ) {
            editor = preferences.edit ( );
            editor.putString ( "chapechdate" , getDateTime ( ) );
            editor.apply ( );
        }
        if ( ! sharedPreferencesDate.equals ( "" ) ) {
            String datechapecha = getDateTime ( );
            if ( ! sharedPreferencesDate.equals ( datechapecha ) ) {
                try {
                    new Rijndael ( );
                    FileInputStream  fileInputStream  = new FileInputStream ( getUpToString ( ) + "/mac.xml" );
                    FileOutputStream fileOutputStream = new FileOutputStream ( getUpToString ( ) + "/macD.xml" );
                    Rijndael.EDR ( fileInputStream , fileOutputStream , 2 );
                    new DeleteFile ( getUpToString ( ) + "/mac.xml" );
                    XML.editByDOM ( getUpToString ( ) + "/macD.xml" , "User" , "capchacount" , "0" );

                    editor = preferences.edit ( );
                    editor.putString ( "chapechdate" , getDateTime ( ) );
                    //editor.putInt("count", 0);
                    editor.commit ( );

                    FileInputStream  fileInputStream1  = new FileInputStream ( getUpToString ( ) + "/macD.xml" );
                    FileOutputStream fileOutputStream1 = new FileOutputStream ( getUpToString ( ) + "/mac.xml" );
                    Rijndael.EDR ( fileInputStream1 , fileOutputStream1 , 1 );

                    new DeleteFile ( getUpToString ( ) + "/macD.xml" );

                }
                catch ( ParserConfigurationException | SAXException | IOException e ) {
                    // TODO Auto-generated catch block
                    e.printStackTrace ( );
                }
            }

        }

    }

    @SuppressLint ( "SimpleDateFormat" )
    private
    String getDateTime ( ) {
        DateFormat dateFormat = new SimpleDateFormat ( "yyyy/MM/dd" );
        Date       date       = new Date ( );
        return dateFormat.format ( date );
    }

    // Forgot password logic.
    @SuppressLint ( "StaticFieldLeak" )
    public
    void setTxtForgotPassword ( String name ) {

        new StringStringStringAsyncTask ( ).execute ( name );

    }

    // Check Internet Connection
    public
    boolean isDataEnable ( ) {
        ConnectivityManager conMgr;
        conMgr = ( ConnectivityManager ) getSystemService ( Context.CONNECTIVITY_SERVICE );
        // ARE WE CONNECTED TO THE NET
        assert conMgr != null;
        if ( conMgr.getActiveNetworkInfo ( ) != null
             && conMgr.getActiveNetworkInfo ( ).isAvailable ( )
             && conMgr.getActiveNetworkInfo ( ).isConnected ( ) ) {
            return true;
        } else {
            Log.v ( "Internet" , "Internet Connection Not Present" );
            return false;
        }
    }

    // Create Shortcut of app in main screen
    @SuppressLint ( "SdCardPath" )
    public
    void createShortcut ( ) {
        File f = new File (
                "/data/data/com.eteach.sol/shared_prefs/ICON.xml" );
        if ( ! f.exists ( ) ) {
            deleteShortcut ( );
            addShortcut ( );
        }
    }

    // Add Shortcut in app
    private
    void addShortcut ( ) {

        Intent shortcutIntent = new Intent ( getApplicationContext ( ) ,
                                             LoginActivity.class );

        shortcutIntent.setAction ( Intent.ACTION_MAIN );

        Intent addIntent = new Intent ( );

        addIntent.putExtra ( Intent.EXTRA_SHORTCUT_INTENT , shortcutIntent );
        addIntent.putExtra ( Intent.EXTRA_SHORTCUT_NAME , "eTeach Video Content 5.0" );
        addIntent.putExtra ( Intent.EXTRA_SHORTCUT_ICON_RESOURCE , Intent.ShortcutIconResource.fromContext ( getApplicationContext ( ) , R.drawable.logo ) );
        addIntent.setAction ( "com.android.launcher.action.INSTALL_SHORTCUT" );
        getApplicationContext ( ).sendBroadcast ( addIntent );
    }

    // Remove the Shortcut app
    private
    void deleteShortcut ( ) {
        Intent shortcutIntent = new Intent ( getApplicationContext ( ) ,
                                             LoginActivity.class );

        shortcutIntent.setAction ( Intent.ACTION_MAIN );

        Intent addIntent = new Intent ( );
        addIntent.putExtra ( Intent.EXTRA_SHORTCUT_INTENT , shortcutIntent );
        addIntent.putExtra ( Intent.EXTRA_SHORTCUT_NAME , "eTeach Video Content 5.0" );
        addIntent.putExtra ( Intent.EXTRA_SHORTCUT_ICON_RESOURCE , Intent.ShortcutIconResource.fromContext ( getApplicationContext ( ) , R.drawable.logo ) );

        addIntent.setAction ( "com.android.launcher.action.UNINSTALL_SHORTCUT" );
        getApplicationContext ( ).sendBroadcast ( addIntent );
    }

    // Hide the Keyboard method
    public static
    void hideSoftKeyboard ( Activity activity ) {
        InputMethodManager inputMethodManager = ( InputMethodManager ) activity.getSystemService ( Activity.INPUT_METHOD_SERVICE );
        if ( inputMethodManager != null && activity.getCurrentFocus ( ) != null )
            inputMethodManager.hideSoftInputFromWindow ( activity.getCurrentFocus ( ).getWindowToken ( ) , 0 );
    }

    private
    void checkCapchacount ( ) {
        new Rijndael ( );
        try {
            FileInputStream  fileInputStream  = new FileInputStream ( getUptoString ( ) + "/mac.xml" );
            FileOutputStream fileOutputStream = new FileOutputStream ( getUptoString ( ) + "/macD.xml" );
            Rijndael.EDR ( fileInputStream , fileOutputStream , 2 );

        }
        catch ( FileNotFoundException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace ( );
        }
        new DeleteFile ( getUptoString ( ) + "/mac.xml" );
        new XMLParser ( getUptoString ( ) + "/macD.xml" , "User" , "capchacount" , "" );
        String Name = XMLParser.child1Value;

        if ( Name.equals ( "" ) ) {
            XML.addNodeToXMLForCapecha ( getUptoString ( ) + "/macD.xml" , "capchacount" , "0" );
        } else {

            int Counter = Integer.parseInt ( Name );
            Counter++;
            String value = String.valueOf ( Counter );

            try {
                XML.editByDOM ( getUptoString ( ) + "/macD.xml" , "User" , "capchacount" , value );
            }
            catch ( ParserConfigurationException | SAXException | IOException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace ( );
            }
        }

        try {
            FileInputStream  fileInputStream1  = new FileInputStream ( getUptoString ( ) + "/macD.xml" );
            FileOutputStream fileOutputStream1 = new FileOutputStream ( getUptoString ( ) + "/mac.xml" );
            Rijndael.EDR ( fileInputStream1 , fileOutputStream1 , 1 );
        }
        catch ( FileNotFoundException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace ( );
        }
        new DeleteFile ( getUptoString ( ) + "/macD.xml" );
        attemptLogin ( );
    }

    public
    void attemptLogin ( ) {
        if ( mAuthTask != null ) {
            return;
        }

        // Reset errors.
        getmEmailView ( ).setError ( null );
        getmPasswordView ( ).setError ( null );

        // Store values at the time of the login attempt.
        setmEmail ( getmEmailView ( ).getText ( ).toString ( ) );
        setmPassword ( getmPasswordView ( ).getText ( ).toString ( ) );

        boolean cancel    = false;
        View    focusView = null;

        // Check for a valid password.
        if ( TextUtils.isEmpty ( getmPassword ( ) ) ) {
            getmPasswordView ( ).setError ( getString ( R.string.error_field_required ) );
            focusView = getmPasswordView ( );
            cancel = true;
        } else if ( getmPassword ( ).length ( ) < 4 ) {
            getmPasswordView ( ).setError ( getString ( R.string.error_invalid_password ) );
            focusView = getmPasswordView ( );
            cancel = true;
        }

        // Check for a valid email address.
        if ( TextUtils.isEmpty ( getmEmail ( ) ) ) {
            getmEmailView ( ).setError ( getString ( R.string.error_field_required ) );
            focusView = getmEmailView ( );
            cancel = true;
        }

        if ( cancel ) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus ( );
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            StatFs stat_fs        = new StatFs ( Environment.getExternalStorageDirectory ( ).getPath ( ) );
            double avail_sd_space = ( double ) stat_fs.getAvailableBlocks ( ) * ( double ) stat_fs.getBlockSize ( );
            //double MB_Available = (avail_sd_space / 10485783);
            setMB_Available ( ( avail_sd_space / 1048576 ) );
            // MB_Available=MB_Available-20;
            Log.d ( "AvailableMB" , "" + getMB_Available ( ) );
            if ( getMB_Available ( ) >= 100 ) {
                // mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
                showProgress ( true );
                mAuthTask = new UserLoginTask ( );
                mAuthTask.execute ( ( Void ) null );
            } else
                Toast.makeText ( LoginActivity.this , getMB_Available ( ) + "MB is available and \n minimum required memory for this application is 100MB" , Toast.LENGTH_LONG ).show ( );
        }
    }

    public
    Button getBtnRegister ( ) {
        return btnRegister;
    }

    public
    void setBtnRegister ( Button btnRegister ) {
        this.btnRegister = btnRegister;
    }

    public
    Button getBtnLogin ( ) {
        return btnLogin;
    }

    public
    void setBtnLogin ( Button btnLogin ) {
        this.btnLogin = btnLogin;
    }

    public
    TextView getTxtForgotPassword ( ) {
        return txtForgotPassword;
    }

    public
    void setTxtForgotPassword ( TextView txtForgotPassword ) {
        this.txtForgotPassword = txtForgotPassword;
    }

    public
    EditText getEtUserName ( ) {
        return etUserName;
    }

    public
    void setEtUserName ( EditText etUserName ) {
        this.etUserName = etUserName;
    }

    public
    EditText getEtPassword ( ) {
        return etPassword;
    }

    public
    void setEtPassword ( EditText etPassword ) {
        this.etPassword = etPassword;
    }

    public
    String getUpToString ( ) {
        return upToString;
    }

    public
    void setUpToString ( String upToString ) {
        this.upToString = upToString;
    }

    public
    View getmLoginFormView ( ) {
        return mLoginFormView;
    }

    public
    void setmLoginFormView ( View mLoginFormView ) {
        this.mLoginFormView = mLoginFormView;
    }

    public
    View getmLoginStatusView ( ) {
        return mLoginStatusView;
    }

    public
    void setmLoginStatusView ( View mLoginStatusView ) {
        this.mLoginStatusView = mLoginStatusView;
    }

    public
    UserXMLParser getPar ( ) {
        return par;
    }

    public
    void setPar ( UserXMLParser par ) {
        this.par = par;
    }

    public
    CheckBox getCbRememberMe ( ) {
        return cbRememberMe;
    }

    public
    void setCbRememberMe ( CheckBox cbRememberMe ) {
        this.cbRememberMe = cbRememberMe;
    }

    public
    ArrayList <String> getHeadingArrayList ( ) {
        return headingArrayList;
    }

    public
    void setHeadingArrayList ( ArrayList <String> headingArrayList ) {
        this.headingArrayList = headingArrayList;
    }

    public
    ArrayList <String> getBodyArrayList ( ) {
        return bodyArrayList;
    }

    public
    void setBodyArrayList ( ArrayList <String> bodyArrayList ) {
        this.bodyArrayList = bodyArrayList;
    }

    public
    EditText getmEmailView ( ) {
        return mEmailView;
    }

    public
    void setmEmailView ( EditText mEmailView ) {
        this.mEmailView = mEmailView;
    }

    public
    EditText getmPasswordView ( ) {
        return mPasswordView;
    }

    public
    void setmPasswordView ( EditText mPasswordView ) {
        this.mPasswordView = mPasswordView;
    }

    public
    TextView getForgotpassword ( ) {
        return forgotpassword;
    }

    public
    void setForgotpassword ( TextView forgotpassword ) {
        this.forgotpassword = forgotpassword;
    }

    public
    TextView getmLoginStatusMessageView ( ) {
        return mLoginStatusMessageView;
    }

    public
    void setmLoginStatusMessageView ( TextView mLoginStatusMessageView ) {
        this.mLoginStatusMessageView = mLoginStatusMessageView;
    }

    public
    String getmPassword ( ) {
        return mPassword;
    }

    public
    void setmPassword ( String mPassword ) {
        this.mPassword = mPassword;
    }

    public
    SharedPreferences getIcon ( ) {
        return icon;
    }

    public
    void setIcon ( SharedPreferences icon ) {
        this.icon = icon;
    }

    public
    SharedPreferences.Editor getIconEditor ( ) {
        return iconEditor;
    }

    public
    void setIconEditor ( SharedPreferences.Editor iconEditor ) {
        this.iconEditor = iconEditor;
    }

    public
    float getWidth ( ) {
        return width;
    }

    public
    void setWidth ( float width ) {
        this.width = width;
    }

    public
    int getHeight ( ) {
        return height;
    }

    public
    void setHeight ( int height ) {
        this.height = height;
    }

    public
    String getAccountId ( ) {
        return accountId;
    }

    public
    void setAccountId ( String accountId ) {
        this.accountId = accountId;
    }

    public
    String getUsername ( ) {
        return username;
    }

    public
    void setUsername ( String username ) {
        this.username = username;
    }

    public
    String getUptoString ( ) {
        return uptoString;
    }

    public
    void setUptoString ( String uptoString ) {
        this.uptoString = uptoString;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public
    class UserLoginTask extends AsyncTask <Void, Void, Boolean> {
        private String AccountID;

        @Override
        protected
        void onPreExecute ( ) {
            setPar ( new UserXMLParser ( LoginActivity.this ) );
            super.onPreExecute ( );
        }

        @Override
        protected
        Boolean doInBackground ( Void... params ) {
            // TODO: attempt authentication against a network service.

            AccountID = getPar ( ).xmlcompare ( getmEmail ( ) , getmPassword ( ) );
            if ( AccountID.equals ( "false" ) || AccountID.equals ( "eteach" ) ) {
                Log.e ( "Login Failed" , " Try Again !!!!!" );
                return false;
            } else
                Log.e ( "Login successful" , "user" + getmEmail ( ) + "   Pass" + getmPassword ( ) );
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected
        void onPostExecute ( final Boolean success ) {
            mAuthTask = null;
            showProgress ( false );

            if ( success ) {
                Toast.makeText ( LoginActivity.this , "Login successful" , Toast.LENGTH_LONG ).show ( );
                if ( getCbRememberMe ( ).isChecked ( ) ) {
                    SharedPreferences        settings = getApplicationContext ( ).getSharedPreferences ( "Login" , 0 );
                    SharedPreferences.Editor editor   = settings.edit ( );
                    editor.putString ( "username" , getmEmailView ( ).getText ( ).toString ( ) );
                    editor.putString ( "password" , getmPasswordView ( ).getText ( ).toString ( ) );
                    editor.apply ( );
                }
                SharedPreferences        settings = getApplicationContext ( ).getSharedPreferences ( "username" , 0 );
                SharedPreferences.Editor editor   = settings.edit ( );
                editor.putString ( "user" , getmEmailView ( ).getText ( ).toString ( ) );
                editor.putString ( "ACCOUNTID" , AccountID );
                editor.apply ( );
                Intent intent = new Intent ( LoginActivity.this , HomeActivity.class );
                startActivity ( intent );
                finish ( );
            }

            //			else 	if(isDataEnable())
            //				{
            //					Toast.makeText(Login.this, "Please Wait.\n connecting to server........" , Toast.LENGTH_SHORT).show();
            //					UserNameValid(mEmailView.getText().toString(),mPasswordView.getText().toString());
            //
            //				}
            else {
                if ( getmEmail ( ).equals ( "eteach" ) ) {
                    Toast.makeText ( LoginActivity.this , "Login successful." , Toast.LENGTH_LONG ).show ( );
                    boolean flag = true;
                    if ( ! new File ( Constant.INTERNAL_SDCARD + "1234ABCD/eteach/board.xml" ).exists ( ) ) {
                        flag = false;

                        ArrayList <String> array = new ArrayList <String> ( );
                        array.add ( "12_Chemistry_2010.pdf" );
                        array.add ( "12_Chemistry_2011.pdf" );
                        array.add ( "12_Math_2010.pdf" );
                        array.add ( "12_Math_2011.pdf" );
                        array.add ( "12_Physics_2010.pdf" );
                        array.add ( "12_Physics_2011.pdf" );
                        array.add ( "board.xml" );
                        array.add ( "cbsechapter.xml" );
                        array.add ( "cbseclass.xml" );
                        array.add ( "cbselanguage.xml" );
                        array.add ( "cbsesubject.xml" );
                        new myAsyncTask ( LoginActivity.this ).execute ( array );
                        //						new DownloadUserFile(Login.this,Constant.INTERNAL_SDCARD+"1234ABCD/eteach").execute("/XMLFile");
                    }
                    if ( getCbRememberMe ( ).isChecked ( ) ) {
                        SharedPreferences        settings = getApplicationContext ( ).getSharedPreferences ( "Login" , 0 );
                        SharedPreferences.Editor editor   = settings.edit ( );
                        editor.putString ( "username" , getmEmailView ( ).getText ( ).toString ( ) );
                        editor.putString ( "password" , getmPasswordView ( ).getText ( ).toString ( ) );
                        editor.apply ( );
                    }

                    SharedPreferences        settings = getApplicationContext ( ).getSharedPreferences ( "username" , 0 );
                    SharedPreferences.Editor editor   = settings.edit ( );
                    editor.putString ( "user" , getmEmailView ( ).getText ( ).toString ( ) );
                    editor.putString ( "ACCOUNTID" , "1234ABCD" );
                    editor.apply ( );
                    if ( flag ) {
                        if ( new File ( Constant.INTERNAL_SDCARD + "/userxmlD.xml" ).exists ( ) )
                            new File ( Constant.INTERNAL_SDCARD + "/userxmlD.xml" ).delete ( );
                        Intent intent = new Intent ( LoginActivity.this , HomeActivity.class );
                        startActivity ( intent );
                        finish ( );
                    }

                } else if ( isDataEnable ( ) ) {
                    Intent intent = new Intent ( LoginActivity.this , HomeActivity.class );
                    new Reload ( LoginActivity.this , intent ).execute ( getmEmailView ( ).getText ( ).toString ( ) , getmPasswordView ( ).getText ( ).toString ( ) );
                } else {
                    Toast.makeText ( LoginActivity.this , "Login Failed ...\nPlease try agin!" , Toast.LENGTH_LONG ).show ( );
                    getmPasswordView ( )
                            .setError ( getString ( R.string.error_incorrect_password ) );
                    getmPasswordView ( ).requestFocus ( );
                }
            }

        }


        boolean isDataEnable ( ) {
            ConnectivityManager conMgr;
            conMgr = ( ConnectivityManager ) getSystemService ( Context.CONNECTIVITY_SERVICE );
            // ARE WE CONNECTED TO THE NET
            assert conMgr != null;
            if ( conMgr.getActiveNetworkInfo ( ) != null
                 && conMgr.getActiveNetworkInfo ( ).isAvailable ( )
                 && conMgr.getActiveNetworkInfo ( ).isConnected ( ) ) {
                return true;
            } else {
                Log.v ( "Internet" , "Internet Connection Not Present" );
                return false;
            }

        }


        @Override
        protected
        void onCancelled ( ) {
            mAuthTask = null;
            showProgress ( false );
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    //@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private
    void showProgress ( final boolean show ) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources ( ).getInteger (
                android.R.integer.config_shortAnimTime );

        getmLoginStatusView ( ).setVisibility ( View.VISIBLE );
        getmLoginStatusView ( ).animate ( ).setDuration ( shortAnimTime )
                .alpha ( show ? 1 : 0 )
                .setListener ( new AnimatorListenerAdapter ( ) {
                    @Override
                    public
                    void onAnimationEnd ( Animator animation ) {
                        getmLoginStatusView ( ).setVisibility ( show ? View.VISIBLE
                                                                 : View.GONE );
                    }
                } );

        getmLoginFormView ( ).setVisibility ( View.VISIBLE );
        getmLoginFormView ( ).animate ( ).setDuration ( shortAnimTime )
                .alpha ( show ? 0 : 1 )
                .setListener ( new AnimatorListenerAdapter ( ) {
                    @Override
                    public
                    void onAnimationEnd ( Animator animation ) {
                        getmLoginFormView ( ).setVisibility ( show ? View.GONE
                                                               : View.VISIBLE );
                    }
                } );
    }

    @SuppressLint ( "StaticFieldLeak" )
    private
    class myAsyncTask extends AsyncTask <ArrayList <String>, Void, Void> {
        private ArrayList <String> files;
        private ProgressDialog     dialog;
        private Activity           act;

        myAsyncTask ( Activity act ) {
            this.setAct ( act );
        }

        @Override
        protected
        void onPreExecute ( ) {
            setDialog ( ProgressDialog.show ( LoginActivity.this , "Copying File Please wait" , "Loading..." ) );
        }

        @SafeVarargs
        @Override
        protected final
        Void doInBackground ( ArrayList <String>... params ) {
            setFiles ( params[ 0 ] );
            for ( int i = 0 ; i < getFiles ( ).size ( ) ; i++ ) {
                copyFileFromAssetsToSDCard ( getFiles ( ).get ( i ) );
                getFiles ( ).get ( i );
                Log.e ( "Copy Files" , "" + getFiles ( ).get ( i ) );
            }
            return null;
        }

        @Override
        protected
        void onPostExecute ( Void result ) {
            getDialog ( ).dismiss ( );
            Intent intent = new Intent ( getAct ( ) , HomeActivity.class );
            getAct ( ).startActivity ( intent );
            getAct ( ).finish ( );
        }

        List<String> getFiles ( ) {
            return Collections.unmodifiableList ( files );
        }

        void setFiles ( ArrayList <String> files ) {
            this.files = files;
        }

        public
        ProgressDialog getDialog ( ) {
            return dialog;
        }

        public
        void setDialog ( ProgressDialog dialog ) {
            this.dialog = dialog;
        }

        public
        Activity getAct ( ) {
            return act;
        }

        public
        void setAct ( Activity act ) {
            this.act = act;
        }
    }

    // Function to copy file from Assets to the SDCard
    public
    void copyFileFromAssetsToSDCard ( String fileFromAssets ) {
        AssetManager is = this.getAssets ( );
        InputStream  fis;
        try {

            fis = is.open ( fileFromAssets );
            FileOutputStream fos;
            if ( ! new File ( Constant.INTERNAL_SDCARD + "1234ABCD/eteach" ).exists ( ) ) {
                new File ( Constant.INTERNAL_SDCARD + "1234ABCD/eteach" ).mkdirs ( );

            }

            fos = new FileOutputStream ( new File ( Constant.INTERNAL_SDCARD + "1234ABCD/eteach" , fileFromAssets ) );
            byte[] b = new byte[ 1024 ];
            int    i;
            while ( ( i = fis.read ( b ) ) != - 1 ) {
                fos.write ( b , 0 , i );
            }
            fos.flush ( );
            fos.close ( );
            fis.close ( );
        }
        catch ( IOException e1 ) {
            e1.printStackTrace ( );
        }
    }

    private
    class StringStringStringAsyncTask extends AsyncTask<String, String, String> {
        private ProgressDialog mProgressDialog;

        protected
        void onPreExecute ( ) {
            mProgressDialog = new ProgressDialog ( LoginActivity.this );
            mProgressDialog.setMessage ( "Sending information to server. Please wait..." );
            mProgressDialog.setCancelable ( true );
            mProgressDialog.show ( );
        }

        protected
        void onPostExecute ( String res ) {
            try {
                if ( res != null ) {
                    JSONObject jsonObject = new JSONObject ( res );

                    JSONArray hrArray = jsonObject.getJSONArray ( "posts" );

                    JSONObject post = hrArray.getJSONObject ( 0 )
                            .getJSONObject ( "post" );

                    String result = post.getString ( "result" );
                    if ( result.equals ( "0" ) ) {
                        final AlertDialog alertDialog = new AlertDialog.Builder ( new ContextThemeWrapper ( LoginActivity.this , R.style.AppCompatAlertDialogStyle ) ).create ( );
                        alertDialog.setTitle ( "eTeach" );
                        alertDialog.setMessage ( "You have entered a wrong Username.\n Please enter a valid Username. " );
                        alertDialog.setButton ( "OK" , new DialogInterface.OnClickListener ( ) {

                            public
                            void onClick ( DialogInterface dialog , int which ) {
                                alertDialog.dismiss ( );
                            }
                        } );
                        alertDialog.setIcon ( R.drawable.logo );
                        alertDialog.show ( );

                        getEtUserName ( ).setText ( "" );
                    } else if ( result.equals ( "1" ) ) {
                        final AlertDialog alertDialog = new AlertDialog.Builder ( new ContextThemeWrapper ( LoginActivity.this , R.style.AppCompatAlertDialogStyle ) ).create ( );
                        alertDialog.setTitle ( "eTeach" );
                        alertDialog.setMessage ( "Your password has been sent to your registered email. Please check and re-enter your username and password." );
                        alertDialog.setButton ( "OK" , new DialogInterface.OnClickListener ( ) {

                            public
                            void onClick ( DialogInterface dialog , int which ) {
                                alertDialog.dismiss ( );
                            }
                        } );

                        alertDialog.setIcon ( R.drawable.logo );
                        alertDialog.show ( );

                        getEtUserName ( ).setText ( "" );
                    }
                }
            }
            catch ( JSONException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace ( );
            }
            mProgressDialog.dismiss ( );
        }

        @Override
        protected
        String doInBackground ( String... params ) {
            String doin_res = null;
            try {
                String  url     = "http://" + Constant.WEBSERVER + "/designo_pro/ws_forgotpwd.php?usrnm=" + params[ 0 ] + "&format=json";
                HttpGet httpget = new HttpGet ( url );

                HttpClient httpclient = new DefaultHttpClient ( );
                BasicHttpResponse httpResponse =
                        ( BasicHttpResponse ) httpclient.execute ( httpget );

                HttpEntity     entity = httpResponse.getEntity ( );
                InputStream    is     = entity.getContent ( );
                BufferedReader reader = new BufferedReader ( new InputStreamReader ( is ) );
                StringBuilder  sb     = new StringBuilder ( );
                String         line   = null;

                while ( ( line = reader.readLine ( ) ) != null ) {
                    sb.append ( line ).append ( "\n" );
                }
                is.close ( );
                doin_res = sb.toString ( );

            }
            catch ( IllegalStateException | IOException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace ( );
            }
            return doin_res;
        }
    }
}
