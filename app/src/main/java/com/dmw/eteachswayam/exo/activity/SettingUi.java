package com.dmw.eteachswayam.exo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.model.Constant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SettingUi extends Activity implements OnCheckedChangeListener {
	private SharedPreferences prefs;
	int requestCode;
	private static int    flag;
	private static String boardToChapter;

	SharedPreferences        pref;
	SharedPreferences        pathPref;
	SharedPreferences.Editor editor;
	SharedPreferences.Editor pathEditor;
	Switch                   setting1;
	Button                   okBtn;
	LayoutInflater           factory;
	//TextView t1,t2;
	View                     layout;

 public	static ArrayList<String> mMounts =new ArrayList<String> ();
 public	static ArrayList<String> mVold   =new ArrayList<String> ();
	//		@Override
	//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	//		super.onActivityResult(requestCode, resultCode, data);
	//		if(resultCode==RESULT_OK)
	//		{	
	//			Bundle b = new Bundle();
	//			b.putString("Value", pref.getString("listPref", "nr1"));
	//			Intent i = getIntent(); //gets the intent that called this intent
	//			i.putExtras(b);
	//			if(requestCode==61){
	//				i.putExtra("boardToChapter", boardToChapter);
	//				i.putExtra("Flag", flag);
	//				setResult(61, i);
	//			}
	//			else{ 
	//				i.putExtra("boardToChapter", boardToChapter);
	//				i.putExtra("Flag", flag);
	//				setResult(62,i);
	//			}
	//			finish();
	//		}
	//	}
	@SuppressLint( "WrongConstant" )
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView( R.layout.set_path);
		regId();
		Intent intent = getIntent();
		requestCode= Integer.parseInt( intent.getStringExtra( "requestCode"));
		boardToChapter=intent.getStringExtra("myBoardToChapter");
		flag=intent.getIntExtra("Flag", 3);

		pref = getSharedPreferences("com.eteach.sol_preferences", MODE_APPEND);
		editor=pref.edit();


		if (setting1 != null) {
			setting1.setOnCheckedChangeListener(this);   
		}
		pathPref = getSharedPreferences("path", MODE_APPEND);

		pathEditor = pathPref.edit();
		CreateSharedPreference();
		this.setFinishOnTouchOutside(false);
		//addPreferencesFromResource(R.layout.preferences);
		//	Preference button = (Preference)findPreference("button");
		//		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
		//			public boolean onPreferenceClick(Preference arg0) { 
		//				prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		//				Bundle b = new Bundle();
		//				b.putString("Value", prefs.getString("listPref", "nr1"));
		//				String s = prefs.getString("listPref", "nr1");
		//				if(s.equals("2")){
		//					Intent intent = new Intent(Preferences.this,FilePickerActivity.class);
		//					startActivityForResult(intent, 3);
		//				}else if(s.equals("1")){
		//					Constant.ContentPath= Constant.INTERNAL_SDCARD;
		//				}
		//				Intent i = getIntent(); //gets the intent that called this intent
		//				i.putExtras(b);
		//				if(requestCode==61){
		//					i.putExtra("boardToChapter", boardToChapter);
		//					i.putExtra("Flag", flag);
		//					setResult(61, i);
		//					
		//					finish();
		//				}
		//				else if(requestCode==62){
		//					i.putExtra("boardToChapter", boardToChapter);
		//					i.putExtra("Flag", flag);
		//					setResult(62,i);
		//					finish();
		//				}else{
		//					i.putExtra("boardToChapter", boardToChapter);
		//					i.putExtra("Flag", flag);
		//					setResult(62,i);
		//					finish();
		//				}
		//
		//				return true;
		//			}
		//		});

		setting1.setOnCheckedChangeListener(new OnCheckedChangeListener () {

			@Override
			public void onCheckedChanged( CompoundButton buttonView, boolean isChecked) {
				//								if(setting1.isChecked())
				//								{
				//									t1.setVisibility(View.VISIBLE);
				//									t2.setVisibility(View.INVISIBLE);
				//								}
				//								else
				//								{
				//									t2.setVisibility(View.VISIBLE);
				//									t1.setVisibility(View.INVISIBLE);
				//								}
				////					pref = getSharedPreferences("com.eteach.sol_preferences", MODE_APPEND);
				////					editor=pref.edit();
				//					editor.putString("listPref", "2");
				//					editor.commit();
				//					//Constant.ContentPath= (shared.getString("pathdemo", ""+Constant.INTERNAL_SDCARD));
				//					
				////					pathPref = getSharedPreferences("path", MODE_APPEND);
				////					
				//////					if (!f1.exists())
				//////					{
				//////						editor.putString("listPref", "1");
				//////						editor.commit();
				//////					}
				////					pathEditor = pathPref.edit();
				//					mMounts.clear();
				//					mVold.clear();
				//					determineStorageOptions();
				//					if(mMounts.size()>=2){
				//					pathEditor.putString("pathdemo",mMounts.get(1)+"/eteach/assets/Content/");
				//					pathEditor.commit();
				//					}
				//					else{
				//						
				//						Toast.makeText(getApplicationContext(), "Memory-card is not present", Toast.LENGTH_SHORT).show();
				//						
				//						editor.putString("listPref", "1");
				//						editor.commit();
				//					}
				//					
				//					
				//				}
				//				else
				//				{
				//					pref = getSharedPreferences("com.eteach.sol_preferences", MODE_APPEND);
				//					editor=pref.edit();
				//					editor.putString("listPref", "1");
				//					editor.commit();
				//					
				//					
				//					Log.d("uncheck", "uncheck");
				//				}

			}
		});


		okBtn.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				if(setting1.isChecked())
				{
					Log.d( "check", "check");
					//					pref = getSharedPreferences("com.eteach.sol_preferences", MODE_APPEND);
					//					editor=pref.edit();
					editor.putString("listPref", "2");
					editor.commit();
					//Constant.ContentPath= (shared.getString("pathdemo", ""+Constant.INTERNAL_SDCARD));

					//					pathPref = getSharedPreferences("path", MODE_APPEND);
					//					
					////					if (!f1.exists())
					////					{
					////						editor.putString("listPref", "1");
					////						editor.commit();
					////					}
					//					pathEditor = pathPref.edit();
					mMounts.clear();
					mVold.clear();
					determineStorageOptions();

					if(mMounts.size()>=2){
						pathEditor.putString("pathdemo",mMounts.get(1)+"/eteach/assets/Content/");
						if(!new File ( mMounts.get( 1) + "/eteach/assets/Content/").exists()){
							new File ( mMounts.get( 1) + "/eteach/assets/Content/").mkdirs();
						}
						try {
							if(!new File ( mMounts.get( 1) + "/eteach/.nomedia").exists())
								new File ( mMounts.get( 1) + "/eteach/.nomedia").createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Constant.ContentPath= mMounts.get( 1) + "/eteach/assets/Content/";
						pathEditor.commit();

					}
					else{
						Toast.makeText( getApplicationContext(), "Memory-card is not present", Toast.LENGTH_SHORT).show();
						Constant.ContentPath= Constant.INTERNAL_SDCARD;
						editor.putString("listPref", "1");
						editor.commit();
						try {
							if(!new File ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/.nomedia").exists())
								new File ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/.nomedia").createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}


				}
				else
				{
					pref = getSharedPreferences("com.eteach.sol_preferences", MODE_APPEND);
					editor=pref.edit();
					editor.putString("listPref", "1");
					editor.commit();
					try {
						if(!new File ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/.nomedia").exists())
							new File ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/.nomedia").createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}

					Log.d( "uncheck", "uncheck");
				}

				finish();
			}
		});




	}

	//	@Override
	//	protected Dialog onCreateDialog(int id) {
	//
	//		Dialog dialog = null;
	//
	//		if(id == ALERT_ID) {
	//
	//			AlertDialog d = null;
	//			AlertDialog.Builder builder = new Builder(this);
	//			//			   factory = LayoutInflater.from(this);
	//			//			    layout =factory.inflate(R.layout.settingdialog,null);
	//			Button okBtn=(Button) layout.findViewById(R.id.okBtn);
	//			okBtn.setOnClickListener(new View.OnClickListener() {
	//
	//				public void onClick(View v) {
	//					dismissDialog(ALERT_ID);
	//				}
	//			});
	//			// setting1=(Switch) layout.findViewById(R.id.setting1);
	//
	//			setting1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	//
	//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	//					if(setting1.isChecked())
	//					{
	//						Log.d("check", "check");
	////						pref = getSharedPreferences("com.eteach.sol_preferences", MODE_APPEND);
	////						editor=pref.edit();
	//						editor.putString("listPref", "2");
	//						editor.commit();
	//						//Constant.ContentPath= (shared.getString("pathdemo", ""+Constant.INTERNAL_SDCARD));
	//						
	////						pathPref = getSharedPreferences("path", MODE_APPEND);
	////						
	//////						if (!f1.exists())
	//////						{
	//////							editor.putString("listPref", "1");
	//////							editor.commit();
	//////						}
	////						pathEditor = pathPref.edit();
	//						pathEditor.putString("pathdemo", "ExternalPath");
	//						pathEditor.commit();
	//						
	//						
	//					}
	//					else
	//					{
	//						pref = getSharedPreferences("com.eteach.sol_preferences", MODE_APPEND);
	//						editor=pref.edit();
	//						editor.putString("listPref", "1");
	//						editor.commit();
	//						
	//						
	//						Log.d("uncheck", "uncheck");
	//					}
	//
	//
	//				}
	//			});
	//
	//			builder.setView(layout);
	//
	//
	//
	//
	//			//builder.set
	//
	//			d = builder.create();
	//			dialog = d;
	//
	//		}
	//		return dialog;
	//
	//	}
	public void checkSetPath()
	{
		if(pref.getString("listPref","1").equals("2"))
		{
			setting1.setChecked(true);
			//			t1.setVisibility(View.VISIBLE);
			//			t2.setVisibility(View.INVISIBLE);
			Log.d( "2", "2");
		}
		else {
			if(pref.getString("listPref","1").equals("1")||pref.getString("listPref","1").equals("digiGreen") )
			{
				setting1.setChecked(false);
				//				t2.setVisibility(View.VISIBLE);
				//				t1.setVisibility(View.INVISIBLE);
				Log.d( "1", "1");
			}
		}
	}
	public void CreateSharedPreference()
	{
		checkSetPath();
		File f = new File (
				"/data/data/com.eteach.sol/shared_prefs/com.eteach.sol_preferences.xml");
		if (!f.exists())
		{

			editor.putString("listPref","digigreen");
			editor.commit();
		}

		//		factory = LayoutInflater.from(this);
		//		layout =factory.inflate(R.layout.settingdialog,null);
		//		setting1=(Switch) layout.findViewById(R.id.setting1);
	}


	public void regId()
	{
		setting1=(Switch )findViewById( R.id.setting1);
		okBtn=(Button ) findViewById( R.id.okBtn);
		//t1=(TextView) findViewById(R.id.InternalText);
		//	t2=(TextView) findViewById(R.id.ExternalText);

	}

	public static void determineStorageOptions() {
		readMountsFile();

		readVoldFile();

		compareMountsWithVold();

		//  testAndCleanMountsList();

	}
	private static void readMountsFile() {
		mMounts.add( Environment.getExternalStorageDirectory().getAbsolutePath());
		//mMounts.add("/mnt/sdcard");

		try {
			Scanner scanner = new Scanner ( new File ( "/proc/mounts"));
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.startsWith("/dev/block/vold/")) {
					String[] lineElements = line.split( " ");
					String   element      = lineElements[1];
					if (!element.equals( Environment.getExternalStorageDirectory().getAbsolutePath()))
						mMounts.add(element);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void readVoldFile() {
		mVold.add( Environment.getExternalStorageDirectory().getAbsolutePath());
		try {
			Scanner scanner = new Scanner ( new File ( "/system/etc/vold.fstab"));
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.startsWith("dev_mount")) {
					String[] lineElements;
					//	              	String val=line.replace("/t"," ");
					//	              	String val1=line.replaceAll("/t", " ");
					if(line.contains("\t")){
						lineElements = line.split("\\t");
					}else{
						lineElements = line.split(" ");
					}
					String element = lineElements[2];
					if(element.contains("\\"))
					{
						element.replaceAll("\\*", "/");
					}

					if (element.contains(":"))
						element = element.substring(0, element.indexOf(":"));

					// don't add the default vold path
					// it's already in the list.
					if (!element.equals( Environment.getExternalStorageDirectory().getAbsolutePath()))
					{

						mVold.add(element);
					}
				}
			}
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void compareMountsWithVold() {

		for (int i = 0; i < mMounts.size(); i++) {
			String mount = mMounts.get( i);
			if (!mVold.contains(mount)) 
				mMounts.remove(i--);
		}
		mVold.clear();
	}

	private static void testAndCleanMountsList() {

		for (int i = 0; i < mMounts.size(); i++) {
			String mount = mMounts.get( i);
			File   root  = new File ( mount);
			if (!root.exists() || !root.isDirectory() || !root.canWrite())
				mMounts.remove(i--);
		}
	}

	@Override
	public void onCheckedChanged( CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub

	}

}