package com.dmw.eteachswayam.exo.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.model.Constant;

import java.io.File;

public class TextPaperActivity extends Activity {
	private Button physics2010Button;
	private Button physics2011Button;
	private Button chemistry2010Button;
	private Button chemistry2011Button;
	private Button mahematics2010Button;
	private Button mahematics2011Button;
	String InternalPath;
	private String accountId;
	private String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			//	WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		if(new Scale(this).Cam_height==752){
//			setContentView(R.layout.text10);
//			
//			}
//			else{
				setContentView(R.layout.activity_text_paper);
			//}
		
		SharedPreferences settings = getApplicationContext().getSharedPreferences( "username", 0);
		accountId =settings.getString("ACCOUNTID", "xxx");
		username =settings.getString("user","xxx");
		InternalPath= Constant.INTERNAL_SDCARD + accountId + "/" + username;
		setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		initComponent();
	}

	public void initComponent()
	{
		ganerateId();
		registerEvent();
	}
	
	public void readPdf(String path)
	{
	       File   targetFile = new File ( path);
	       Uri    targetUri  = Uri.fromFile( targetFile);
	       Intent intent;
	       intent = new Intent ( Intent.ACTION_VIEW);
	       intent.setDataAndType(targetUri, "application/pdf");
	       startActivity(intent);
	}

private void ganerateId() {
	physics2010Button=(Button ) findViewById( R.id.physics2010Button);
	physics2011Button=(Button ) findViewById( R.id.physics2011Button);
	chemistry2010Button=(Button ) findViewById( R.id.chemistry2010Button);
	chemistry2011Button=(Button ) findViewById( R.id.chemistry2011Button);
	mahematics2010Button=(Button ) findViewById( R.id.mathematics2010Button);
	mahematics2011Button=(Button ) findViewById( R.id.mathematics2011Button);
}

private void registerEvent() {
	physics2010Button.setOnClickListener(new OnClickListener () {

		@Override
		public void onClick(View v) {
			//readPdf(InternalPath+"/12_Physics_2010.pdf");
		new CallPDFReaderAsyncTaskForLoader(TextPaperActivity.this).execute(InternalPath+"/12_Physics_2010.pdf");
			
		}
	});
	physics2011Button.setOnClickListener(new OnClickListener () {

		@Override
		public void onClick(View v) {
			
			new CallPDFReaderAsyncTaskForLoader(TextPaperActivity.this).execute(InternalPath+"/12_Physics_2011.pdf");
		}
	});
	chemistry2010Button.setOnClickListener(new OnClickListener () {

		@Override
		public void onClick(View v) {
			new CallPDFReaderAsyncTaskForLoader(TextPaperActivity.this).execute(InternalPath+"/12_Chemistry_2010.pdf");
		}
	});
	chemistry2011Button.setOnClickListener(new OnClickListener () {

		@Override
		public void onClick(View v) {
			new CallPDFReaderAsyncTaskForLoader(TextPaperActivity.this).execute(InternalPath+"/12_Chemistry_2011.pdf");
		}
	});
	mahematics2010Button.setOnClickListener(new OnClickListener () {

		@Override
		public void onClick(View v) {
			new CallPDFReaderAsyncTaskForLoader(TextPaperActivity.this).execute(InternalPath+"/12_Math_2010.pdf");
		}
	});
	mahematics2011Button.setOnClickListener(new OnClickListener () {

		@Override
		public void onClick(View v) {
			new CallPDFReaderAsyncTaskForLoader(TextPaperActivity.this).execute(InternalPath+"/12_Math_2011.pdf");
		}
	});

}
@Override
protected void onResume() {
	super.onResume();
	HomeActivity.checkmemory(TextPaperActivity.this);
}
class CallPDFReaderAsyncTaskForLoader extends AsyncTask<String, String , String> {
	private ProgressDialog loader;
	private Context        context;
	CallPDFReaderAsyncTaskForLoader(Context context){
		this.context = context;
	}
	@Override
	protected void onPreExecute() {
		loader =new ProgressDialog ( context);
		loader.setMessage("Loading. Please wait ...");
		loader.setCancelable(false);
		loader.setProgressStyle( ProgressDialog.STYLE_SPINNER);
		loader.show();
		super.onPreExecute();
	}
	@Override
	protected
    String doInBackground( String... params) {
		String string =params[0].toString();
//		String bal=string.substring(0,string.lastIndexOf("."));
//		//String[] string2=string.split(".");
//		String string3=bal+"_D.pdf";
		//String actualPreset=mFiles[position].getAbsolutePath();
		//	String [] actualPreset12=actualPreset.split(mFiles[position].toString());
//		try {
//			new Rijndael(new TripleDesString().decrypt(FilePlay.demoKey));
//			FileInputStream fileInputStream=new FileInputStream(params[0]);
//			FileOutputStream fileOutputStream=new FileOutputStream(string3);
//			Rijndael.EDR(fileInputStream, fileOutputStream, 2);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		return string;
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		/*Uri uri = Uri.parse(result);
		Intent intent = new Intent(context,MuPDFActivity.class);
		intent.putExtra("Pdfstatuscheck", "DecPdf");
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(uri);
		context.startActivity(intent); */
		loader.dismiss();
	}		
}
}
