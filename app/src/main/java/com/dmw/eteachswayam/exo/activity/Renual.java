package com.dmw.eteachswayam.exo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.fragment.tab.AllClassesFragment;

public class Renual extends Activity {

	private String uptoSubject;
	private String uptoString;
	Button renew,cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView( R.layout.renualnew);
		getWindow().getDecorView().setBackgroundColor( Color.WHITE);
		setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		renew=(Button ) findViewById( R.id.buttonrenew);
		cancel=(Button ) findViewById( R.id.buttoncancel);
		Intent m =getIntent();
		uptoSubject=m.getStringExtra("uptoSubject");
		uptoString=m.getStringExtra("uptoString");
		Log.e( "renua Uptosubject", uptoSubject);
		Renual.this.setTitle("Renew Product");
		Renual.this.setTitleColor( Color.parseColor( "#4fa5d5"));
	//showDialog();
		renew.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent couponIntent =new Intent ( Renual.this, CoupanActivation.class);
				couponIntent.putExtra("UptoSubject", uptoSubject+"/");
				couponIntent.putExtra("chaptername","");
				couponIntent.putExtra("mode",1);
				couponIntent.putExtra("uptoString",uptoString);
				startActivity(couponIntent);
				finish();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent i =new Intent(Renual.this,Chapter.class);
//				i.putExtra("path", uptoSubject);
//				startActivity(i);
				finish();       
			}
		});
	}
	@SuppressWarnings("deprecation")
	private void showDialog() {

		final AlertDialog ald1 =new AlertDialog.Builder( Renual.this).create();
		ald1.setTitle("eTeach");
		ald1.setIcon(R.drawable.logo);
		ald1.setMessage("Your Class play validation is expired /n Do you want to reactivate the class ?");
		ald1.setButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick( DialogInterface dialog, int which) {

				Intent couponIntent =new Intent ( Renual.this, CoupanActivation.class);
				couponIntent.putExtra("UptoSubject", uptoSubject);
				couponIntent.putExtra("chaptername","");
				couponIntent.putExtra("mode",1);
				couponIntent.putExtra("uptoString",uptoSubject);
				startActivity(couponIntent);
				finish();
			}
		});
		ald1.setButton2("No",new DialogInterface.OnClickListener() {

			@Override
			public void onClick( DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				ald1.dismiss();
				Intent i =new Intent ( Renual.this, AllClassesFragment.class);
				i.putExtra("path", uptoSubject);
				startActivity(i);
				finish();       

			}
		});
		ald1.show();
	}
}



