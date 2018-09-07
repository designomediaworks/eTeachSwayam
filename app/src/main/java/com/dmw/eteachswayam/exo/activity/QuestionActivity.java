//package com.eteach.sol;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class QuestionActivity extends Activity {
//	private Spinner boardSpinner;
//	private Spinner classSpinner;
//	private Spinner subjectSpinner;
//	private Spinner markSpinner;
//	private RadioButton eassy;
//	private RadioButton midium;
//	private RadioButton hard;
//	private EditText timereEditText;
//	private Button nextButton;
//	private RadioGroup rg;
//	private RadioButton rb1;
//	private RadioButton rb2;
//	private RadioButton rb3;
//	private String board;
//	private String clas;
//	private String subject;
//	private String mark;
//	private String timer;
//	private String eassyValue;
//	private String mediumValue;
//	private String hardValue;
//	public static String path=null;
//
//	/** Called when the activity is first created. */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setContentView(R.layout.quemain);
//	     
//		initComponent();
//	}
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(keyCode==KeyEvent.KEYCODE_MENU)
//		{
//			Intent intent =new Intent(getApplication(), MainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent); 
//			moveTaskToBack(true);
//			
//			startActivity(intent);
//		}
//		if(keyCode==KeyEvent.KEYCODE_BACK){
//
//			Intent i= new Intent(getApplicationContext(),MainActivity.class);
//			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			
//			startActivity(i);
//			
//			finish();
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//	private void initComponent() {
//		ganerateId();
//		registerEvent();
//		
//	}
//
//	private void registerEvent() {
//		nextButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(validation()){
//					next();
//				}
//			}
//		});
//	}
//	private void ganerateId() {
//		boardSpinner=(Spinner) findViewById(R.id.boardSpinner);
//		classSpinner=(Spinner) findViewById(R.id.classSpinner);
//		subjectSpinner=(Spinner) findViewById(R.id.subjectSpinner);
//		markSpinner=(Spinner) findViewById(R.id.markSpinner);
//		eassy=(RadioButton) findViewById(R.id.r1);
//		midium=(RadioButton) findViewById(R.id.r2);
//		hard=(RadioButton) findViewById(R.id.r3);
//		timereEditText=(EditText) findViewById(R.id.timerEditText);
//		nextButton=(Button) findViewById(R.id.nextButton);
//		rg=(RadioGroup) findViewById(R.id.radioGroup1);
//		rb1=(RadioButton) findViewById(R.id.r1);
//		rb2=(RadioButton) findViewById(R.id.r2);
//		rb3=(RadioButton) findViewById(R.id.r3);
//
//		registerSpinner();
//	}
//
//	private void registerSpinner() {
//		ArrayAdapter<String> boardAdapter;
//		ArrayAdapter<String> classAdapter;
//		ArrayAdapter<String> subjectAdapter;
//		ArrayAdapter<Integer> markAdapter;
//
//		String[] boardStrings=new String[]{"CBSE"};
//		String[] classStrings=new String[]{"Class 1","Class 2","Class 3","Class 4","Class 5","Class 6","Class 7","Class 8","Class 9","Class 10","Class 11","Class 12"};
//		String[] subjectStrings=new String[]{"ALL"};
//		Integer[] markStrings=new Integer[]{2,5,10};
//
//		boardAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,boardStrings);
//
//		classAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,classStrings);
//		subjectAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,subjectStrings);
//		markAdapter=new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1,markStrings);
//
//		boardSpinner.setAdapter(boardAdapter);
//		classSpinner.setAdapter(classAdapter);
//		subjectSpinner.setAdapter(subjectAdapter);
//		markSpinner.setAdapter(markAdapter);
//
//	}
//	protected void next() {
//
//		board=boardSpinner.getSelectedItem().toString();
//		clas=classSpinner.getSelectedItem().toString();
//		subject=subjectSpinner.getSelectedItem().toString();
//		mark=markSpinner.getSelectedItem().toString();
//		timer=timereEditText.getText().toString();
//         path=board+"/"+clas+"/"+subject ;
//		Intent go=new Intent(this,Quiz.class);
////		go.putExtra("board", board);
////		go.putExtra("clas", clas);
////		go.putExtra("subject", subject);
//		go.putExtra("path", path);
//		Log.d("path", path);
//		go.putExtra("mark", mark);
//		go.putExtra("timer", timer);
//		
//		if(rb1.isChecked()==true)
//		{
//			eassyValue=rb1.getText().toString();
//			go.putExtra("Value", eassyValue);
//			Log.e(eassyValue, eassyValue);
//		}
//		else if(rb2.isChecked()==true)
//		{
//			mediumValue=rb2.getText().toString();
//			Log.e(mediumValue, mediumValue);
//			go.putExtra("Value", mediumValue);
//		}
//		else	if(rb3.isChecked()==true)
//		{
//			hardValue=rb3.getText().toString();
//			go.putExtra("Value", hardValue);
//			Log.e(hardValue, hardValue);
//		}
//		startActivity(go);
//		finish();
//		
//
//	}
//	public boolean validation()
//	{
//		String setTimer=timereEditText.getText().toString();
//		if (setTimer.trim().matches("")) {
//			Toast.makeText(this, "Please Enter Time ", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//		return true;
//	}
//}
package com.dmw.eteachswayam.exo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.model.Constant;
import com.dmw.eteachswayam.exo.model.DeleteFile;
import com.dmw.eteachswayam.exo.model.LayoutXmlParser;
import com.dmw.eteachswayam.exo.model.Rijndael;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class QuestionActivity extends Activity {
	private Spinner     boardSpinner;
	private Spinner     classSpinner;
	private Spinner     subjectSpinner;
	private Spinner     markSpinner;
	private Spinner     timeSpinner;
	private RadioButton eassy;
	private RadioButton midium;
	private RadioButton hard;
	ArrayAdapter<String> boardAdapter;
	ArrayAdapter<String> classAdapter;

	ArrayAdapter<Integer> markAdapter;
	ArrayAdapter<Integer> timeAdapter;
	//private EditText timereEditText;
	private Button nextButton;
	 RadioGroup rg;
	private RadioButton rb1;
	private RadioButton rb2;
	private RadioButton rb3;
	private String      board;
	private String      clas;
	private String      subject;
	private String      mark;
	private String      timer;
	private String      eassyValue;
	private String      mediumValue;
	private String      hardValue;
	public static String path =null;
	ArrayAdapter<String> subjectArrayAdapter;
	ArrayList<String> subjectArrayList =new ArrayList<String> ();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE);
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                              WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		if(new Scale(this).Cam_height==720 || new Scale(this).Cam_height==1440 ){ 
//		setContentView(R.layout.qmain9);
//		}
		//else{
			setContentView(R.layout.activity_question);
		//}
		setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		initComponent();
	}

	private void initComponent() {
		ganerateId();
		registerEvent();
		spinnerClick();
		

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if( keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent =new Intent ( getApplication(), HomeActivity.class);
			startActivity(intent);
			this.finish();
			
		}
		return super.onKeyDown(keyCode, event);
	}
	private void registerEvent() {
		nextButton.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//if(validation()){
			//	next();
				//}
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		HomeActivity.checkmemory(QuestionActivity.this);
	}
	private void ganerateId() {
		boardSpinner=(Spinner ) findViewById( R.id.boardSpinner);
		classSpinner=(Spinner ) findViewById( R.id.classSpinner);
		subjectSpinner=(Spinner ) findViewById( R.id.subjectSpinner);
		markSpinner=(Spinner ) findViewById( R.id.markSpinner);
		timeSpinner=(Spinner ) findViewById( R.id.timeSpinner);
		eassy=(RadioButton ) findViewById( R.id.r1);
		midium=(RadioButton ) findViewById( R.id.r2);
		hard=(RadioButton ) findViewById( R.id.r3);
		nextButton=(Button ) findViewById( R.id.nextButton);
		rg=(RadioGroup ) findViewById( R.id.radioGroup1);
		rb1=(RadioButton ) findViewById( R.id.r1);
		rb2=(RadioButton ) findViewById( R.id.r2);
		rb3=(RadioButton ) findViewById( R.id.r3);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener () {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent ( QuestionActivity.this, TextPaperActivity.class);
				QuestionActivity.this.startActivity(intent);
				
			}
		});
		//		rb1.setEnabled(false);
		//rb2.setEnabled(true);
		//		rb2.setEnabled(false);
		//		rb3.setEnabled(false);
		
		radioButtonListener();
		registerSpinner();
		spinnerOnclickListener();
	}
	
void radioButtonListener() {

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener () {
			
			@Override
			public void onCheckedChanged( RadioGroup group, int checkedId) {

				if(rb1.getId()==checkedId)
				{
					Toast.makeText( QuestionActivity.this, "This is demo version only, to access full online test feature call at : 0712-6694444 ", Toast.LENGTH_LONG).show();
					rb1.setChecked(false);
					//rb2.setChecked(true);
		
					rb2.setBackgroundResource(R.drawable.c);
					
				//	rb2.setEnabled(true);
					//rb1.
				}
				else if(rb3.getId()==checkedId)
				{
					Toast.makeText( QuestionActivity.this, "This is demo version only, to access full online test feature call at :  0712-6694444", Toast.LENGTH_LONG).show();
					rb3.setChecked(false);
					//rb2.setChecked(true);
					rb2.setBackgroundResource(R.drawable.c);
					//rb1.
				}
				 if(rb2.getId()==checkedId)
				 {
					 rb3.setChecked(false);
				 }
			}
		});

	}

	private void spinnerOnclickListener() {

		classSpinner.setOnItemSelectedListener(new OnItemSelectedListener () {

			private String accountId;
			private String username;

			@Override
			public void onItemSelected( AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
				//	registerSubjectSpinner();
				String            path1    = boardSpinner.getSelectedItem().toString() + "/ENGLISH/" + arg0.getSelectedItem().toString();
				SharedPreferences settings = getApplicationContext().getSharedPreferences( "username", 0);
				accountId =settings.getString("ACCOUNTID", "xxx");
				username =settings.getString("user","xxx");
				try {
					FileInputStream  fis =new FileInputStream ( Constant.INTERNAL_SDCARD + accountId + "/" + username + "/" + "cbsesubject.xml");
					FileOutputStream fos =new FileOutputStream ( Constant.INTERNAL_SDCARD + accountId + "/" + username + "/" + "cbsesubjectD.xml");

					new Rijndael ();
					Rijndael.EDR(fis, fos, 2);
					LayoutXmlParser parser =new LayoutXmlParser ( Constant.INTERNAL_SDCARD + accountId + "/" + username + "/" + "cbsesubjectD.xml");
					subjectArrayList = parser.parse(path1);
					new DeleteFile ( Constant.INTERNAL_SDCARD + accountId + "/" + username + "/" + "cbsesubjectD.xml");
				}catch (Exception e) {
					e.printStackTrace();
				}

			
				
				subjectArrayList.add(0,"ALL");
				subjectArrayAdapter=new ArrayAdapter<String> ( QuestionActivity.this, android.R.layout.simple_list_item_1, subjectArrayList)
				{
					 @Override
						public
                     View getDropDownView( int position, View convertView, ViewGroup parent)
						{
							View view = super.getView( position, convertView, parent);

							//we know that simple_spinner_item has android.R.id.text1 TextView:         

							/* if(isDroidX) {*/
							TextView text = (TextView )view.findViewById( android.R.id.text1);
							//text.setTextColor(Color.BLACK);//choose your color           
							/*}*/

							return view;

						}
					}; 

				
				subjectSpinner.setAdapter(subjectArrayAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});


	}


	private void registerSpinner() {


		String[] boardStrings =new String[]{"CBSE"};
		String[] classStrings =new String[]{"Class_I","Class_II","Class_III","Class_IV","Class_V","Class_VI","Class_VII","Class_VIII","Class_IX","Class_X","Class_XI","Class_XII"};
		//	String[] subjectStrings=new String[]{"ALL"};
		ArrayList<Integer> time =new ArrayList<Integer> ();
		time.add(15);
		time.add(20);
		time.add(30);
		//Integer[] timeString=new Integer[]{15,30,60};
		Integer[] markStrings =new Integer[]{1,3,5};

		boardAdapter=new ArrayAdapter<String> ( this, android.R.layout.simple_list_item_1, boardStrings){
			 @Override
				public
             View getDropDownView( int position, View convertView, ViewGroup parent)
				{
					View view = super.getView( position, convertView, parent);

					//we know that simple_spinner_item has android.R.id.text1 TextView:         

					/* if(isDroidX) {*/
					TextView text = (TextView )view.findViewById( android.R.id.text1);
				//	text.setTextColor(Color.BLACK);//choose your color           
					/*}*/

					return view;

				}
			}; 
		classAdapter=new ArrayAdapter<String> ( this, android.R.layout.simple_list_item_1, classStrings){
			 @Override
				public
             View getDropDownView( int position, View convertView, ViewGroup parent)
				{
					View view = super.getView( position, convertView, parent);

					//we know that simple_spinner_item has android.R.id.text1 TextView:         

					/* if(isDroidX) {*/
					TextView text = (TextView )view.findViewById( android.R.id.text1);
					//text.setTextColor(Color.BLACK);//choose your color           
					/*}*/

					return view;

				}
			}; 
		//	subjectAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,subjectStrings);
		markAdapter=new ArrayAdapter<Integer> ( this, android.R.layout.simple_list_item_1, markStrings){
			 @Override
				public
             View getDropDownView( int position, View convertView, ViewGroup parent)
				{
					View view = super.getView( position, convertView, parent);

					//we know that simple_spinner_item has android.R.id.text1 TextView:         

					/* if(isDroidX) {*/
					TextView text = (TextView )view.findViewById( android.R.id.text1);
				//	text.setTextColor(Color.BLACK);//choose your color           
					/*}*/

					return view;

				}
			}; 
		timeAdapter=new ArrayAdapter<Integer> ( this, android.R.layout.simple_list_item_1, time){
			 @Override
				public
             View getDropDownView( int position, View convertView, ViewGroup parent)
				{
					View view = super.getView( position, convertView, parent);

					//we know that simple_spinner_item has android.R.id.text1 TextView:         

					/* if(isDroidX) {*/
					TextView text = (TextView )view.findViewById( android.R.id.text1);
				//	text.setTextColor(Color.BLACK);//choose your color           
					/*}*/

					return view;

				}
			}; 
		timeSpinner.setAdapter(timeAdapter);
		boardSpinner.setAdapter(boardAdapter);
		classSpinner.setAdapter(classAdapter);
		//subjectSpinner.setAdapter(subjectAdapter);
		markSpinner.setAdapter(markAdapter);


	}
	/*protected void next() {

		board=boardSpinner.getSelectedItem().toString();
		clas=classSpinner.getSelectedItem().toString();
		subject=subjectSpinner.getSelectedItem().toString();
		mark=markSpinner.getSelectedItem().toString();
		//timer=timereEditText.getText().toString();
		path=board+"/"+clas+"/"+subject ;
		Intent go=new Intent(this,Quiz.class);
		//		go.putExtra("board", board);
		//		go.putExtra("clas", clas);
		//		go.putExtra("subject", subject);
		go.putExtra("path", path);
		Log.d("path", path);
		go.putExtra("mark", mark);
		go.putExtra("timer", "15");

//		if(rb1.isChecked()==true)
//		{
//			eassyValue=rb1.getText().toString();
//			go.putExtra("Value", eassyValue);
//			Log.e(eassyValue, eassyValue);
//		}
//		else if(rb2.isChecked()==true)
//		{
//			mediumValue=rb2.getText().toString();
//			Log.e(mediumValue, mediumValue);
//			go.putExtra("Value", mediumValue);
//		}
//		else	if(rb3.isChecked()==true)
//		{
//			hardValue=rb3.getText().toString();
//			go.putExtra("Value", hardValue);
//			Log.e(hardValue, hardValue);
//		}
		startActivity(go);
		finish();


	}*/
	//	public boolean validation()
	//	{
	//		//String setTimer=timerSpinner.getText().toString();
	//		if (setTimer.trim().matches("")) {
	//			Toast.makeText(this, "Please Enter Time ", Toast.LENGTH_SHORT).show();
	//			return false;
	//		}
	//		return true;
	//	}

	public void spinnerClick()
	{
		timeSpinner.setOnItemSelectedListener(new OnItemSelectedListener () {

			@Override
			public void onItemSelected( AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
				// TODO Auto-generated method stub
				//timeSpinner.setSelection(0);
				if(arg2==0)
				{

				}else
				{
					Toast.makeText( QuestionActivity.this, "This is demo version only, to access full online test feature call at :  0712-6694444 ", Toast.LENGTH_LONG).show();
					timeSpinner.setAdapter(timeAdapter);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		markSpinner.setOnItemSelectedListener(new OnItemSelectedListener () {

			@Override
			public void onItemSelected( AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
				// TODO Auto-generated method stub
				//timeSpinner.setSelection(0);
				if(arg2==0)
				{

				}else
				{
					Toast.makeText( QuestionActivity.this, "This is demo version only, to access full online test feature call at : 0712-6493999 ", Toast.LENGTH_LONG).show();
					markSpinner.setAdapter(markAdapter);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		subjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener () {

			@Override
			public void onItemSelected( AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
				// TODO Auto-generated method stub
				//timeSpinner.setSelection(0);
				
				if(arg0.getSelectedItem().toString().equals("ALL"))
				{
               
				}else
				{
					Toast.makeText( QuestionActivity.this, "This is demo version only, to access full online test feature call at : 0712-6493999 ", Toast.LENGTH_LONG).show();
					subjectSpinner.setAdapter(subjectArrayAdapter);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
}