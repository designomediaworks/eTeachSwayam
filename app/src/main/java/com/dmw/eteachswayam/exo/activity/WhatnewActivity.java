package com.dmw.eteachswayam.exo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.model.Constant;
import com.dmw.eteachswayam.exo.model.DownloadFileUpgrade;


public class WhatnewActivity extends Activity {
	private WebView whatnewView;
	private Button  continuewwhatsbutton;
	private Button  exitwwhatsbutton;
	private String  newVerLink;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE);
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView( R.layout.newwhatsnew);
		initComponent();
	}

	private void initComponent() {
		ganerateId();
		registerWebview();
		registerEvent();
	}
	private void ganerateId() {
		whatnewView=(WebView ) findViewById( R.id.whatnewWebview);
		continuewwhatsbutton=(Button ) findViewById( R.id.whatupcontinueButton);
		exitwwhatsbutton=(Button ) findViewById( R.id.whatexitButton);
	}
	private void registerWebview() {
//		whatnewView.setBackgroundColor(Color.BLACK);
		whatnewView.getSettings().setPluginState( WebSettings.PluginState.ON);
		whatnewView.getSettings().setAllowFileAccess(true);
		whatnewView.getSettings().setLoadWithOverviewMode(true);
		whatnewView.getSettings().setUseWideViewPort(true);
		whatnewView.setScrollBarStyle( WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		whatnewView.setScrollbarFadingEnabled(true);
		whatnewView.loadUrl("http://www.eteach.co.in/and/whatsnew.html");
	}
	private void registerEvent() {
		continuewwhatsbutton.setOnClickListener(new OnClickListener () {
			@Override
			public void onClick(View v) {
				Intent intent =getIntent();
				if(intent.hasExtra("newVerLink")){
					newVerLink=intent.getStringExtra("newVerLink");
					new DownloadFileUpgrade ( WhatnewActivity.this, "http://" + Constant.WEBSERVER + "/android/demoproject/upgrade/" + newVerLink, Constant.INTERNAL_SDCARD + newVerLink, 2);
				}else{
					finish();
				}
				
			}
		});

		exitwwhatsbutton.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				alertDialogUpdate();
			}
		});

	}
	@SuppressWarnings("deprecation")
	public void alertDialogUpdate()
	{

		AlertDialog.Builder builder = new AlertDialog.Builder( WhatnewActivity.this, R.style.MyDialogTheme);
		builder.setTitle("eTeach");
		builder.setMessage("Do you want to cancel?");

		String positiveText = getString( android.R.string.ok);
		builder.setPositiveButton(positiveText,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick( DialogInterface dialog, int which) {
						Intent i =new Intent ( WhatnewActivity.this, HomeActivity.class);
						startActivity(i);
						WhatnewActivity.this.finish();
					}
				});

		String negativeText = getString( android.R.string.cancel);
		builder.setNegativeButton(negativeText,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick( DialogInterface dialog, int which) {

					}
				});

		AlertDialog dialog = builder.create();
		// display dialog
		dialog.show();

		/*final AlertDialog alterDiloge= new AlertDialog.Builder(new ContextThemeWrapper(WhatnewActivity.this, R.style.popup_theme)).create();
		alterDiloge.setIcon(R.drawable.logo);
		alterDiloge.setTitle("eTeach");
		alterDiloge.setMessage("Do you want to cancel?");
		alterDiloge.setButton2("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alterDiloge.dismiss();
			}
		});
		alterDiloge.setButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent i=new Intent(WhatnewActivity.this,HomeActivity.class);
				startActivity(i);
				WhatnewActivity.this.finish();
			}
		});
		alterDiloge.show();*/
	} 
}