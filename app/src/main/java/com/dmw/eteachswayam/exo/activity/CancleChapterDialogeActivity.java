package com.dmw.eteachswayam.exo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.model.DownloadChapter;


public class CancleChapterDialogeActivity extends Activity {
	private Button   cancleYesButton;
	private Button   cancleNoButton;
	private TextView nameTextView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			//	WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView( R.layout.cancledownloadingnewui);
		getWindow().getDecorView().setBackgroundColor( Color.WHITE);
		setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		CancleChapterDialogeActivity.this.setTitle("Cancel Chapter Downloading");
		CancleChapterDialogeActivity.this.setTitleColor( Color.parseColor( "#4fa5d5"));
		initComponent();
	}

	private void initComponent() {
		ganerateId();
		registerEvent();
	}

	private void ganerateId() {
		cancleYesButton=(Button ) findViewById( R.id.yesButton);
		cancleNoButton=(Button ) findViewById( R.id.noButton);
		 nameTextView=(TextView ) findViewById( R.id.msgtextview);
		 nameTextView.setText("Do you want to cancel Chapter downloading?");
	}
	private void registerEvent() {
		cancleYesButton.setOnClickListener(new OnClickListener () {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i =new Intent ( CancleChapterDialogeActivity.this, DownloadChapter.class);
				i.addCategory(DownloadChapter.TAG);
				DownloadChapter.serviceState=false;
				stopService(i);
				finish();
			}
		});
		
		cancleNoButton.setOnClickListener(new OnClickListener () {
			
			@Override
			public void onClick(View v) {
				DownloadChapter.serviceState=true;
				finish();
			}
		});
	}

}