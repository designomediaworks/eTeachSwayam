package com.dmw.eteachswayam.exo.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.model.Constant;
import com.dmw.eteachswayam.exo.model.DownloadFileUpdate;
import com.dmw.eteachswayam.exo.model.DownloadServerForUpdates;
import com.dmw.eteachswayam.exo.model.Items1;

import java.util.ArrayList;

//import android.graphics.AvoidXfermode.Mode;

public class CheckingUpdateActivity extends Activity {
	//	private Button checkupdateButton;
	private       Button               downloadupdatescontentButton;
	private       ListView             displayupdatesListView;
	public static ArrayAdapter<Items1> resultAdapter;
	public static ArrayList<Items1>    resultArrayList;
	ArrayList<Items1> downloadArrayList;
	private ArrayList<String> updateArrayList;
	private ToggleButton      selectAllName;
	//public static MyAdapter myAdapter;
	private CheckBox          cb;
	private ArrayList<Items1> downloadArrayListnew;
	private ArrayList<Items1> down;
	private ArrayList<Items1> arrayList23;
	Items1 Itemname;
	private String accountId;
	private String username;
	private Button buttoncancle;
	//	private ArrayList<Items1> list;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//		if(new Scale(this).Cam_height==720 || new Scale(this).Cam_height==1440 ){
		//			setContentView(R.layout.mainupdates9);
		//		}
		//		else
		setContentView( R.layout.newupdatesfileafterchange);
		setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		initComponent();

	}

	private void initComponent() {
		resultArrayList=new ArrayList<Items1> ();
		downloadArrayList=new ArrayList<Items1> ();
		updateArrayList=new ArrayList<String> ();
		//list=new ArrayList<Items1>();
		down=new ArrayList<Items1> ();
		SharedPreferences settings = getApplicationContext().getSharedPreferences( "username", 0);
		accountId =settings.getString("ACCOUNTID", "xxx");
		username =settings.getString("user","xxx");
		Itemname=new Items1();
		if(isDataEnable()){

			SharedPreferences settings1 = getApplicationContext().getSharedPreferences( "username", 0);
			accountId =settings1.getString("ACCOUNTID", "xxx");
			username =settings1.getString("user","xxx");
			new DownloadFileUpdate ( accountId, username, "http://" + Constant.WEBSERVER + "/android/demoproject/Updates/UpdateFile.xml", Constant.INTERNAL_SDCARD + accountId + "/" + username + "/UpdateFile.xml", 1, CheckingUpdateActivity.this);
		}
		ganerateId();



		registerEvent();
		registerListView();
	}


	private void registerListView() {
		resultAdapter=new ArrayAdapter<Items1> ( this, android.R.layout.simple_list_item_multiple_choice, resultArrayList){
			@Override
			public
            View getView( int position, View convertView, ViewGroup parent) {
				CheckedTextView checkedTextView =(CheckedTextView ) super.getView( position, convertView, parent);

				String string =checkedTextView.getText().toString();
				String ss     = string.substring( 0, string.lastIndexOf( "\\"));
				String ss1    = string.substring( string.lastIndexOf( "\\") + 1);
				checkedTextView.setText(ss.replace("\\", " -> ")+" -> "+ss1.replace("_", " "));
				checkedTextView.setTextColor( Color.BLACK);
				return checkedTextView; 
			}
		};


		//		myAdapter=new MyAdapter(this,resultArrayList);
		displayupdatesListView.setAdapter(resultAdapter);
		//		displayupdatesListView.setOnItemClickListener(this);
		displayupdatesListView.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE);
		displayupdatesListView.setOnItemClickListener(new OnItemClickListener () {


			@Override
			public void onItemClick( AdapterView<?> pat, View view, int position,
                                     long arg3) {
				try {
					downloadupdatescontentButton.setEnabled(true);

					//					downloadArrayList.clear();
					//					downloadName = resultArrayList.get(position).toString();
					//					Items1 name=new Items1();
					//					name.setFileName(downloadName);
					//					down.add(name);
				}
				catch (Exception e) {
					e.printStackTrace();
				}

			}
		});		
	}



private void ganerateId() {
	//		checkupdateButton=(Button) findViewById(R.id.checkingUpdateButton);
	downloadupdatescontentButton=(Button ) findViewById( R.id.downloadButton);
	displayupdatesListView=(ListView ) findViewById( R.id.listView);
	selectAllName=(ToggleButton ) findViewById( R.id.selectAllButton);
	buttoncancle=(Button ) findViewById( R.id.cancleButtonForUpdates);
	//		checkupdateButton.setVisibility(View.INVISIBLE);
}

private void registerEvent() {


	downloadupdatescontentButton.setOnClickListener(new OnClickListener () {



		@Override
		public void onClick(View v) {
			down.clear();
			arrayList23 = new ArrayList<Items1> ();
			//				if(isDataEnable())
			//				{
			//					for (int i = 0; i < resultArrayList.size(); i++) {
			//						if(displayupdatesListView.isItemChecked(i)){
			//							Items1 finalName=new Items1();
			//							finalName.setFileName(resultArrayList.get(i).getFileName());
			//							down.add(finalName);
			//							//Toast.makeText(CheckingUpdateActivity.this, resultArrayList.get(i).getFileName(), Toast.LENGTH_SHORT).show();
			//						}
			//					}
			//
			//					//					for (int i = 0; i < down.size(); i++) {
			//					//						String name=down.get(i).getFileName();
			//					//						Toast.makeText(CheckingUpdateActivity.this, "final Name  "+name, Toast.LENGTH_SHORT).show();
			//					//					}
			//
			//					Intent intent = new Intent(CheckingUpdateActivity.this, DownloadServerForUpdates.class);
			//					intent.putParcelableArrayListExtra("fileArrayList", down);
			//
			//					startService(intent);
			//
			//				}
			//				else{
			//					Toast.makeText(CheckingUpdateActivity.this, "Your internet connection is not available.", Toast.LENGTH_SHORT).show();
			//				}

			arrayList23.clear();

			if(isDataEnable()){

				for (int i = 0; i < resultArrayList.size(); i++) {
					if (displayupdatesListView.isItemChecked(i)) {
						arrayList23.add(resultArrayList.get(i));
					}
				}
				if(!arrayList23.isEmpty()){
					Intent intent = new Intent ( CheckingUpdateActivity.this, DownloadServerForUpdates.class);
					intent.putParcelableArrayListExtra("fileArrayList", arrayList23);
					startService(intent);
					finish();
				}else{
					Toast.makeText( CheckingUpdateActivity.this, "Please select chapter for download", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText( CheckingUpdateActivity.this, "Please connect to internet.", Toast.LENGTH_LONG).show();
			}
		}
	});

	selectAllName.setOnClickListener(new OnClickListener () {
		@Override
		public void onClick(View v) {

			if (selectAllName.isChecked()) {
				for (int i = 0; i < displayupdatesListView.getCount(); i++) {
					displayupdatesListView.setItemChecked(i, true);
				}
			} else {
				for (int i = 0; i < displayupdatesListView.getCount(); i++) {
					displayupdatesListView.setItemChecked(i, false);
				}
			}
		}
	});

	buttoncancle.setOnClickListener(new OnClickListener () {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
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


}