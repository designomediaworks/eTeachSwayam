package com.dmw.eteachswayam.exo.fragment.tab;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.activity.CoupanActivation;
import com.dmw.eteachswayam.exo.activity.HomeActivity;
import com.dmw.eteachswayam.exo.activity.LoginActivity;
import com.dmw.eteachswayam.exo.activity.PlayActualVedioFile;
import com.dmw.eteachswayam.exo.activity.PlayDemoVedioFile;
import com.dmw.eteachswayam.exo.activity.Renual;
import com.dmw.eteachswayam.exo.activity.SettingUi;
import com.dmw.eteachswayam.exo.model.Board;
import com.dmw.eteachswayam.exo.model.ChapterName;
import com.dmw.eteachswayam.exo.model.Constant;
import com.dmw.eteachswayam.exo.model.DeleteFile;
import com.dmw.eteachswayam.exo.model.DownloadChapter;
import com.dmw.eteachswayam.exo.model.LayoutXmlParser;
import com.dmw.eteachswayam.exo.model.Reload;
import com.dmw.eteachswayam.exo.model.Rijndael;
import com.dmw.eteachswayam.exo.model.Scale;
import com.dmw.eteachswayam.exo.model.XML;
import com.dmw.eteachswayam.exo.model.XMLParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;


/**
 * Created by Chetan on 04-06-2015.
 */
public class AllClassesFragment extends Fragment {

    Button btnShow;
    View   view;
    public static String board,lang,clas,sub;
    private String accountId,username,path;
    private String path2;

    public static String path1;
    private boolean calendarUpdated = false;
    public static String zipfolderPath;
    public static double MB_Available;

    private        ArrayList<String> downCharray   =new ArrayList<String> ();
    private        ArrayList<String> demodownarray =new ArrayList<String> ();
    public static  ArrayList<String> mMounts       = new ArrayList<String> ();
    private static ArrayList<String> mVold         = new ArrayList<String> ();
    private       ArrayList<ChapterName> listarray;
    public static BaseAdapter            handler;
    private       String                 deviceDateString;
    private       String                 disableDateString;
    private       String                 AccId;
    private       String                 DeviceHddId;
    private       String                 usernamepdf;
    private       String                 calAccountId;
    private       String                 classNamepdf;
    private       String                 pdfHddidinchapter;
    private       String                 pdfusernameinchapter;

    private ImageView    imageView1;
    private ImageView    imageView2;
    private TextView     textView1;
    private TextView     textView2;
    private LinearLayout starimage;
    public  Button       button;
    public  Button       button2;
    private String       chaptername;
    private String       PACKAGEPATHFORCALENDER;
    private boolean  checkcalpathforpdf=true;

    public static boolean           ischeckValidUser;
    private       String            uptoSubject;
    private       SharedPreferences prefs;
    private       String            ListPreference;
    private       String            regParhtocheck;
    private       ListView          hListView;
    private       String            uptoString;
    private       LayoutInflater    inflater;
    private       LinearLayout      DownloadColor;

    private ProgressDialog dialog;
    Spinner spBoard,spMedium,spClass,spSubject;
    private ArrayList<Board>  boardArrayList;
    private ArrayList<String> languageArrayList,classArrayList,subjectArrayList;
    private ArrayAdapter<String> languageArrayAdapter,classArrayAdapter,subjectArrayAdapter;
    private ArrayAdapter<Board> boardArrayAdapter;

    /*public AllClassesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }*/

    @Override
    public
    View onCreateView( LayoutInflater inflater, ViewGroup container,
                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate( R.layout.fragment_all_classes, container, false);

        SharedPreferences settings = this.getActivity().getSharedPreferences( "username", 0);
        accountId =settings.getString("ACCOUNTID", "xxx");
        username =settings.getString("user","xxx");
        path = Constant.INTERNAL_SDCARD + accountId + "/" + username;
        uptoString = path;
        getID();
        setSpinnerData();
        show();
      //  uptoSubject = path1;
        PACKAGEPATHFORCALENDER = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                                 + "/Android/data/"
                                 + Constant.algo(getActivity().getApplicationContext().getPackageName());

        prefs = PreferenceManager.getDefaultSharedPreferences( view.getContext());
        ListPreference = prefs.getString("listPref", "digiGreen");
        if (ListPreference.equals("2")){
            SharedPreferences shared = getActivity().getSharedPreferences( "path", 0);
            Constant.ContentPath= (shared.getString("pathdemo", ""+Constant.INTERNAL_SDCARD));
        }else {
            Constant.ContentPath= Constant.INTERNAL_SDCARD;
        }
        //setloader();
        regParhtocheck=Constant.INTERNAL_SDCARD+"/"+accountId+"/"+username;

        if( HomeActivity.ischeckValidUser) {
         //   initComponent();
          /*  SharedPreferences settings5 = getActivity().getSharedPreferences("helpOverlay", 0);
            if (!settings5.getBoolean("eteachList", false)) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        showDemoAgain();
                    }
                }, 1000);


            }
         //   HomeActivity.dialog.dismiss();
        }else{
            Toast.makeText(getActivity(), "Invalid user login!", Toast.LENGTH_LONG).show();
         //   EDUSlidefullActivity.dialog.dismiss();
            finish();
        }*/
        }

        return view;
    }

    @Override
    public void onResume() {
        initComponent();
       // screenShot();
        if(handler!=null)
            handler.notifyDataSetChanged();
        super.onResume();
        checkmemory();
    }

    public  void checkmemory() {
        StatFs stat_fs        = new StatFs ( Environment.getExternalStorageDirectory().getPath());
        double avail_sd_space = (double) stat_fs.getAvailableBlocks() * (double) stat_fs.getBlockSize();
        //double MB_Available = (avail_sd_space / 10485783);
        MB_Available = (avail_sd_space / 1048576);
        // MB_Available=MB_Available-20;
        Log.d( "AvailableMB", "" + MB_Available);
        if (MB_Available <= 100) {
            SharedPreferences myPrefs = getActivity().getSharedPreferences( "Login", Context.MODE_PRIVATE);
            myPrefs.edit().clear().commit();
            Toast.makeText( getActivity().getApplicationContext(), MB_Available + "MB is available and \n minimum required memory for this application is 100MB", Toast.LENGTH_LONG).show();
            Intent i = new Intent ( getActivity(), LoginActivity.class);
            getActivity().startActivity(i);
            getActivity().finish();
        }
    }

    public void getID(){
        btnShow = (Button ) view.findViewById( R.id.btShow);

        spBoard = (Spinner ) view.findViewById( R.id.spinBoard);
        spMedium = (Spinner ) view.findViewById( R.id.spinMedium);
        spClass = (Spinner ) view.findViewById( R.id.spinClass);
        spSubject = (Spinner ) view.findViewById( R.id.spinSubject);
    }

    private void initComponent() {
        ganerateId();
        geristerListView();
        registerListViewEvent();
    }

    private void ganerateId() {
        hListView = (ListView )view.findViewById( R.id.hlistview);

       /* SharedPreferences settings = getApplicationContext().getSharedPreferences("username", 0);

        userNameTextView=(TextView)findViewById(R.id.textView1);
        userNameTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                ListPreference = prefs.getString("listPref", "digiGreen");
                if(!ListPreference.equals("digiGreen")){
                    if(isMyServiceRunningfordownloadAll() && isMyServiceRunning()){
                        alertDialogUpdate();
                    }else{
                        Toast.makeText(Chapter.this, "Please wait, another chapters are downloading...", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Intent i = new Intent(Chapter.this,SettingUi.class);
                    startActivity(i);
                }

            }
        });
*/
        //
    //    uptoSubject = uptoSubject.trim();

    }

    private void geristerListView() {
        if(Constant.chapterArrayList==null || Constant.chapterArrayList.size()==0)
        {
            try {
                FileInputStream  fis =new FileInputStream ( uptoString + "/cbsechapter.xml");
                FileOutputStream fos =new FileOutputStream ( uptoString + "/cbsechapterD.xml");

                new Rijndael();
                Rijndael.EDR( fis, fos, 2);

                LayoutXmlParser parser =new LayoutXmlParser( uptoString + "/cbsechapterD.xml");
                //			getDownloadChapter(uptoString+"/"+uptoSubject);

                Constant.chapterArrayList=parser.parseChapter(uptoSubject,1);
                new DeleteFile ( uptoString + "/cbsechapterD.xml");



            }catch (Exception e) {

                e.printStackTrace();
            }

        }
    }

    private void registerListViewEvent() {

        inflater = LayoutInflater.from( getActivity());
        SharedPreferences settings  = getActivity().getApplicationContext().getSharedPreferences( "username", 0);
        final String      accountId =settings.getString( "ACCOUNTID", "xxx");
        final String      username  =settings.getString( "user", "xxx");

        downCharray.clear();
        demodownarray.clear();
        determineStorageOptions();
        for (int i = 0; i < mMounts.size(); i++) {

            if(username.equals("eteach")){
                getDownloadChapter(mMounts.get(i)+"/eteach/assets/Content/Demo/"+uptoSubject,2);
            }else{
                getDownloadChapter(mMounts.get(i)+"/eteach/assets/Content/"+accountId+"/"+username+"/"+uptoSubject,1);
            }
        }

        registerForContextMenu(hListView);
        listarray=new ArrayList<ChapterName> ();
        for (int i = 0; i < Constant.chapterArrayList.size(); i++) {
            if(Constant.chapterArrayList.get(i).getKey().equals(uptoSubject)){
                listarray.add(Constant.chapterArrayList.get(i));
            }
        }
        handler=new BaseAdapter () {
            private LinearLayout linearlayoutAddButton;
            private LinearLayout layout;
            private TextView post;
            private TextView post1;
            private LinearLayout linearLayout;
            private LinearLayout linearLayout1;


            @Override
            public void notifyDataSetChanged() {
                downCharray.clear();
                demodownarray.clear();
                determineStorageOptions();
                for (int i = 0; i < mMounts.size(); i++) {

                    if(username.equals("eteach")){
                        getDownloadChapter(mMounts.get(i)+"/eteach/assets/Content/Demo/"+uptoSubject,2);
                    }else{
                        getDownloadChapter(mMounts.get(i)+"/eteach/assets/Content/"+accountId+"/"+username+"/"+uptoSubject,1);
                        getDownloadChapter(mMounts.get(i)+"/eteach/assets/Content/Demo/"+uptoSubject,2);
                    }
                }
                super.notifyDataSetChanged();

            }
            @Override
            public
            View getView( int position, View convertView, ViewGroup parent) {
                boolean loopbreak=true;
                //				ChapterName c=null;
                if(convertView==null){

                    if(Constant.ScreenSize<=5.6f)
                    {
                        convertView=inflater.inflate(R.layout.chapter_list_row, null);
                    }
                    else
                    {
                        convertView=inflater.inflate(R.layout.chapter_list_row, null);
                    }

                }
                linearlayoutAddButton=(LinearLayout )convertView.findViewById( R.id.linearlayoutAddButton);
                //				 c =(ChapterName)listarray.get(position);
                imageView1= (ImageView )convertView.findViewById( R.id.imageView1);
                imageView2= (ImageView )convertView.findViewById( R.id.imageView2);
                starimage=(LinearLayout )convertView.findViewById( R.id.starimage);
                textView1= (TextView )convertView.findViewById( R.id.textView1);
                DownloadColor=(LinearLayout )convertView.findViewById( R.id.DownloadColor);
                textView2= (TextView )convertView.findViewById( R.id.textView2);

                textView1.setTextColor( Color.BLACK);
                //textView2.setText(" "+DateFormat.getDateTimeInstance().format(new Date()));
                post= (TextView )convertView.findViewById( R.id.position);
                post.setText(""+position);

                post1= (TextView )convertView.findViewById( R.id.position1);
                post1.setText(""+position);
                DownloadColor.setBackgroundDrawable(null);
                imageView1.setBackgroundResource(R.drawable.shoppingcart);
                String n =listarray.get( position).toString().replaceAll( "_", " ");
                if(n.length()>45)
                    textView1.setText(n.substring(0, 30).substring(0, n.substring(0, 30).lastIndexOf(" "))+"...");

                else
                    textView1.setText(n);
                textView1.setTextColor( Color.BLACK);
                textView1.setLongClickable(true);

                layout=(LinearLayout ) convertView.findViewById( R.id.layouteq);
                String name =listarray.get( position).getKey();
                if(!username.equals("eteach")){
                    if(name.contains("XI"))
                    {
                        if(!uptoSubject.contains("EMATHS")){

                            if(layout.getChildAt(2)==null)
                            {
                                button=new Button ( getActivity());
                                button2=new Button ( getActivity());
                                linearLayout =new LinearLayout ( getActivity());
                                linearLayout.setLayoutParams(new ActionBar.LayoutParams( 2, 37));
                                linearLayout1 =new LinearLayout ( getActivity());
                                linearLayout1.setLayoutParams(new ViewGroup.LayoutParams( 2, 37));
                                linearLayout.setOrientation( LinearLayout.VERTICAL);
                                linearLayout1.setOrientation( LinearLayout.VERTICAL);
                                Space space1 =new Space ( getActivity());
                                space1.setLayoutParams(new ViewGroup.LayoutParams( 20, 0));

                                if( new Scale ( getActivity()).Cam_height == 1440)
                                {
                                    button.setLayoutParams(new ViewGroup.LayoutParams( 50, 50));
                                    button2.setLayoutParams(new ViewGroup.LayoutParams( 50, 50));
                                }
                                else{
                                    button.setLayoutParams(new ViewGroup.LayoutParams( 30, 30));
                                    button2.setLayoutParams(new ViewGroup.LayoutParams( 30, 30));

                                }
                                Space space =new Space ( getActivity());
                                space.setLayoutParams(new ViewGroup.LayoutParams( 30, 0));
                                Space space11 =new Space ( getActivity());
                                space11.setLayoutParams(new ViewGroup.LayoutParams( 30, 0));

                                Space space111 =new Space ( getActivity());
                                space111.setLayoutParams(new ViewGroup.LayoutParams( 30, 0));

                                linearLayout.setBackgroundResource(R.drawable.red);
                                linearLayout1.setBackgroundResource(R.drawable.red);
                                layout.addView(space1);
                                layout.addView(button);
                                layout.addView(space);
                                layout.addView(linearLayout);
                                layout.addView(space11);
                                layout.addView(button2);
                                layout.addView(space111);
                                layout.addView(linearLayout1);


                                //						if(c.getFlag()==1)
                                //						{
                                //							button.setBackgroundResource(R.drawable.ic_launcher);
                                //							button2.setBackgroundResource(R.drawable.ic_launcher);
                                //							button2.setEnabled(true);
                                //							button.setEnabled(true);
                                //						}
                                if(listarray.get(position).getFlag()==2)
                                {
                                    button.setBackgroundResource(R.drawable.ebook);
                                    button2.setBackgroundResource(R.drawable.questionbank);
                                    button2.setEnabled(true);
                                    button.setEnabled(true);
                                }
                                else{
                                    button.setBackgroundResource(R.drawable.gb2);
                                    button2.setBackgroundResource(R.drawable.gb1);

                                    button2.setEnabled(false);
                                    button.setEnabled(false);

                                }
                                //						if(c.getFlag()==1)
                                //						{
                                //							button2.setEnabled(true);
                                //							button.setEnabled(true);
                                //						}
                            }
                        }
                    }
                }
                if(button!=null){

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LinearLayout layout =(LinearLayout )v.getParent();
                            TextView     vz     =(TextView ) layout.findViewById( R.id.position);
                            int          pos    = Integer.parseInt( vz.getText().toString());

                            chaptername=listarray.get(pos).toString();

                            //							start(accountId+"/"+username+"/"+uptoSubject+chaptername,2);
                            String ebook                = Constant.INTERNAL_SDCARD + accountId + "/" + username + "/" + uptoSubject + chaptername + "/" + "Q_" + chaptername + "_E.pdf";
                            String uptoclassName        =uptoSubject.substring( 0, uptoSubject.lastIndexOf( "/"));
                            File   ebookPdf             =new File ( ebook);
                            File   indexFileForPdfCheck =new File ( Constant.INTERNAL_SDCARD + accountId + "/" + username + "/" + uptoSubject + chaptername + "/topics_E.xml");
                            if(indexFileForPdfCheck.exists()){
                                if(ebookPdf.exists())
                                {
                                    String stringPdf =uptoclassName.substring( 0, uptoclassName.lastIndexOf( "/"));
                                    checkCalender(stringPdf);
                                    if(checkcalpathforpdf){
                                        new LoadPdfFile().execute(ebook);
                                    }
                                }
                                else{
                                    Toast.makeText( getActivity(), "eBook is not available", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                start(accountId+"/"+username+"/"+uptoSubject+chaptername,2);
                            }

                        }

                    });
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            LinearLayout layout =(LinearLayout )v.getParent();
                            TextView     vz     =(TextView ) layout.findViewById( R.id.position);
                            int          pos    = Integer.parseInt( vz.getText().toString());

                            chaptername=listarray.get(pos).toString();
                            String Qbank                = Constant.INTERNAL_SDCARD + accountId + "/" + username + "/" + uptoSubject + chaptername + "/" + chaptername + "_E.pdf";
                            String uptoclassName        =uptoSubject.substring( 0, uptoSubject.lastIndexOf( "/"));
                            File   QbankPdf             =new File ( Qbank);
                            File   indexFileForPdfCheck =new File ( Constant.INTERNAL_SDCARD + accountId + "/" + username + "/" + uptoSubject + chaptername + "/topics_E.xml");
                            if(indexFileForPdfCheck.exists()){
                                if(QbankPdf.exists())
                                {
                                    String stringPdf =uptoclassName.substring( 0, uptoclassName.lastIndexOf( "/"));
                                    checkCalender(stringPdf);
                                    if(checkcalpathforpdf){
                                        new LoadPdfFile().execute(Qbank);
                                    }
                                }
                                else{
                                    Toast.makeText( getActivity(), "Qbank is not available", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                start(accountId+"/"+username+"/"+uptoSubject+chaptername,2);
                            }

                        }
                    });
                }

                for (int i = 0; i < mMounts.size(); i++) {
                    if(new File ( mMounts.get( i) + "/eteach/assets/Content/" + accountId + "/" + username + "/" + uptoSubject + listarray.get( position).getValue().toString().replaceAll( " ", "_") + "/star.xml").exists()){
                        imageView2.setBackgroundResource(R.drawable.listing_grid03);
                        break;

                    }else
                    {
                        imageView2.setBackgroundResource(R.drawable.blackstar);
                    }
                }
                if(listarray.get(position).getFlag()==0){
                    textView1.setTextColor( Color.WHITE);
                    //DownloadColor.setBackgroundColor(Color.BLACK);
                    textView1.setTextColor( Color.BLACK);
                    DownloadColor.setBackgroundDrawable(null);
                    imageView1.setBackgroundResource(R.drawable.shoppingcart);
                    imageView1.setVisibility( View.VISIBLE);
                }else if(listarray.get(position).getFlag()==1){
                    textView1.setTextColor( Color.WHITE);
                    textView1.setTextColor( Color.BLACK);
                    //DownloadColor.setBackgroundColor(Color.BLACK);

                    if(demodownarray!=null&&demodownarray.size()!=0){
                        //						for (Iterator<String> iterator = demodownarray.iterator(); iterator.hasNext();) {
                        //							String type = (String) iterator.next();
                        String zxc =listarray.get( position).getValue();
                        if(demodownarray.contains(zxc)){
                            //							if(type.replace("_", " ").equals(zxc)){
                            loopbreak=false;
                            DownloadColor.setBackgroundResource(R.drawable.untitled03);
                            imageView1.setVisibility( View.INVISIBLE);
                            //								break;
                        }else{
                            imageView1.setBackgroundResource(R.drawable.listing_grid04);
                            imageView1.setVisibility( View.VISIBLE);
                            DownloadColor.setBackgroundDrawable(null);
                            //DownloadColor.setBackgroundColor(Color.BLACK);
                        }
                        //						}
                    }else{
                        DownloadColor.setBackgroundDrawable(null);
                        //DownloadColor.setBackgroundColor(Color.BLACK);
                        imageView1.setVisibility( View.VISIBLE);
                        imageView1.setBackgroundResource(R.drawable.listing_grid04);
                    }

                }else if(listarray.get(position).getFlag()==2){
                    if(downCharray!=null&&downCharray.size()!=0){

                        //							for (Iterator<String> iterator = downCharray.iterator(); iterator.hasNext();) {
                        //								String type = (String) iterator.next();
                        String zxc =listarray.get( position).getValue();
                        if(downCharray.contains(zxc)){
                            //								if(type.replace("_", " ").equals(zxc)){
                            loopbreak=false;
                            DownloadColor.setBackgroundResource(R.drawable.untitled03);
                            imageView1.setVisibility( View.INVISIBLE);
                            textView1.setTextColor( Color.BLACK);
                            //									break;
                        }else{
                            imageView1.setBackgroundResource(R.drawable.listing_grid04);
                            imageView1.setVisibility( View.VISIBLE);
                            DownloadColor.setBackgroundDrawable(null);
                            //DownloadColor.setBackgroundColor(Color.BLACK);
                            textView1.setTextColor( Color.BLACK);
                        }
                        //							}
                    }else{
                        DownloadColor.setBackgroundDrawable(null);
                        //DownloadColor.setBackgroundColor(Color.BLACK);
                        imageView1.setBackgroundResource(R.drawable.listing_grid04);
                        textView1.setTextColor( Color.BLACK);
                    }

                }

                if(listarray.get(position).getFlag()!=0)
                {
                    boolean exist=true;
                    for (int i = 0; i < mMounts.size(); i++) {
                        File lastf = null;
                        if(listarray.get(position).getFlag()==1){
                            lastf=new File ( mMounts.get( i) + "/eteach/assets/Content/Demo/" + uptoSubject + listarray.get( position).getValue().toString().replaceAll( " ", "_"));
                        }else if(listarray.get(position).getFlag()==2){
                            lastf=new File ( mMounts.get( i) + "/eteach/assets/Content/" + accountId + "/" + username + "/" + uptoSubject + listarray.get( position).getValue().toString().replaceAll( " ", "_"));
                        }
                        if(lastf.exists())
                        {
                            File f[] =lastf.listFiles();
                            for (File file : f) {
                                if(file.getAbsolutePath().contains(".zip") && file.length()!=0)
                                {
                                    //DownloadColor.setBackgroundResource(R.drawable.downun);
                                    if(loopbreak){
                                        imageView1.setVisibility( View.VISIBLE);
                                        imageView1.setBackgroundResource(R.drawable.downun);
                                        break;
                                    }
                                }
                                else if(file.getAbsolutePath().contains("lastAccess"))
                                {
                                    exist=false;
                                    String vale    =file.getAbsolutePath().toString().substring( 0, file.toString().lastIndexOf( "/"));
                                    String newd    =file.getAbsolutePath().toString().replace( vale, "");
                                    String newname =newd.replace( "/lastAccess", "");
                                    String newna   =newname.replace( ".app", "");
                                    newna=newna.replace("_","/");
                                    newna=newna.replace("-",":");

                                    String[] strings  =newna.split( "/");
                                    String   month    =strings[1].toString();
                                    String   year     =strings[2].toString();
                                    String[] time     =year.split( " ");
                                    String   yearmain =time[0].toString();
                                    String   timemain =time[1].toString();

                                    String maontacual =getMonthForInt( Integer.parseInt( month) - 1);
                                    maontacual=maontacual.substring(0,3);
                                    textView2.setText("Last Access: "+strings[0].toString()+" "+maontacual+" "+yearmain+","+" "+getTime(timemain));
                                    break;

                                }

                            }
                            if(exist)
                                textView2.setText("Last Access");
                            else{
                                break;
                            }
                        }
                    }

                    if(exist)
                        textView2.setText("Last Access");
                } else{
                    textView2.setText("Last Access");
                }

                starimage.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //	v.getParent();
                        LinearLayout layout =(LinearLayout )v.getParent();
                        TextView     vz     =(TextView ) layout.findViewById( R.id.position);
                        int          pos    = Integer.parseInt( vz.getText().toString());
                        String       va     =listarray.get( pos).toString().replaceAll( " ", "_");

                        try {
                            File f =new File ( Constant.ContentPath + accountId + "/" + username + "/" + uptoSubject + va + "/star.xml");
                            //							Log.e("valueeee", "****************"+f.getAbsolutePath()+"****************");
                            if(!f.exists()){
                                f.getParentFile().mkdirs();
                                f.createNewFile();

                            }else{
                                f.delete();
                            }

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        handler.notifyDataSetChanged();
                    }
                });
                textView1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        LinearLayout layout =(LinearLayout )v.getParent();
                        TextView     vz     =(TextView ) layout.findViewById( R.id.position1);
                        int          pos    = Integer.parseInt( vz.getText().toString());
                        ChapterName  c      =listarray.get(pos);

                        if(c.getFlag()==1){
                            //download demo
                            chaptername = c.getValue();
                            chaptername=chaptername.replaceAll(" ", "_");
                            start("Demo/"+uptoSubject+chaptername,c.getFlag());
                        }else if(c.getFlag()==0){
                            if( !username.equals("eteach")){
                                Intent couponIntent =new Intent ( getActivity(), CoupanActivation.class);
                                couponIntent.putExtra("UptoSubject", uptoSubject);
                                couponIntent.putExtra("chaptername",c.getValue());
                                couponIntent.putExtra("uptoString",uptoString);
                                startActivity(couponIntent);
                            }else{
                                Toast.makeText( getActivity(), "This is a Demo account.\n You cannot activate any class.\n To activate, please signup.", Toast.LENGTH_LONG).show();
                            }
                        }else if(c.getFlag()==2){
                            //Download original chapter
                            chaptername = c.getValue();
                            chaptername=chaptername.replaceAll(" ", "_");

                            start(accountId+"/"+username+"/"+uptoSubject+chaptername,c.getFlag());
                        }
                    }
                });
                return convertView;

            }

            @Override
            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return position;
            }

            @Override
            public
            Object getItem( int position) {
                // TODO Auto-generated method stub
                return position;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return listarray.size();
            }
        };

        hListView.setAdapter(handler);

    }

    public
    String getTime( String dateStr){
        String formattedDate = null;

        DateFormat readFormat  = new SimpleDateFormat ( "hh:mm:ss aa");
        DateFormat writeFormat = new SimpleDateFormat ( "HH:mm:ss");
        Date       date        = null;
        try {
            date = writeFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (date != null) {
            formattedDate = readFormat.format(date);
        }
        return formattedDate;
    }

    String getMonthForInt( int num) {
        String            month  = "wrong";
        DateFormatSymbols dfs    = new DateFormatSymbols ();
        String[]          months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
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
                        if(!mMounts.contains(element))
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
                        element.replaceAll("\\*", "/") ;
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

    private
    String algo( String value) {

        // CharSequence value[];
        String SentToSer = "";
        String GetFrmSer = "";
        int    count     = 0;
        value = value.toUpperCase();
        try {
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == '0') {
                    SentToSer += '2';
                    GetFrmSer += '7';
                } else if (value.charAt(i) == '1') {
                    SentToSer += '8';
                    GetFrmSer += '4';
                } else if (value.charAt(i) == '2') {
                    SentToSer += '5';
                    GetFrmSer += '8';
                } else if (value.charAt(i) == '3') {
                    SentToSer += '9';
                    GetFrmSer += '1';
                } else if (value.charAt(i) == '4') {
                    SentToSer += '0';
                    GetFrmSer += '9';
                } else if (value.charAt(i) == '5') {
                    SentToSer += '3';
                    GetFrmSer += '2';
                } else if (value.charAt(i) == '6') {
                    SentToSer += '7';
                    GetFrmSer += '0';
                } else if (value.charAt(i) == '7') {
                    SentToSer += '1';
                    GetFrmSer += '3';
                } else if (value.charAt(i) == '8') {
                    SentToSer += '6';
                    GetFrmSer += '5';
                } else if (value.charAt(i) == '9') {
                    SentToSer += '4';
                    GetFrmSer += '6';
                }

                else if (value.charAt(i) == 'A') {
                    SentToSer += 'S';
                    GetFrmSer += 'K';
                } else if (value.charAt(i) == 'B') {
                    SentToSer += 'G';
                    GetFrmSer += 'E';
                } else if (value.charAt(i) == 'C') {
                    SentToSer += 'I';
                    GetFrmSer += 'T';
                } else if (value.charAt(i) == 'D') {
                    SentToSer += 'M';
                    GetFrmSer += 'F';
                } else if (value.charAt(i) == 'E') {
                    SentToSer += 'L';
                    GetFrmSer += 'B';
                } else if (value.charAt(i) == 'F') {
                    SentToSer += 'R';
                    GetFrmSer += 'Q';
                } else if (value.charAt(i) == 'G') {
                    SentToSer += 'U';
                    GetFrmSer += 'L';
                } else if (value.charAt(i) == 'H') {
                    SentToSer += 'A';
                    GetFrmSer += 'C';
                } else if (value.charAt(i) == 'I') {
                    SentToSer += 'P';
                    GetFrmSer += 'N';
                } else if (value.charAt(i) == 'J') {
                    SentToSer += 'N';
                    GetFrmSer += 'Y';
                } else if (value.charAt(i) == 'K') {
                    SentToSer += 'W';
                    GetFrmSer += 'I';
                } else if (value.charAt(i) == 'L') {
                    SentToSer += 'Y';
                    GetFrmSer += 'R';
                } else if (value.charAt(i) == 'M') {
                    SentToSer += 'B';
                    GetFrmSer += 'H';
                } else if (value.charAt(i) == 'N') {
                    SentToSer += 'K';
                    GetFrmSer += 'J';
                } else if (value.charAt(i) == 'O') {
                    SentToSer += 'Z';
                    GetFrmSer += 'X';
                } else if (value.charAt(i) == 'P') {
                    SentToSer += 'C';
                    GetFrmSer += 'Z';
                } else if (value.charAt(i) == 'Q') {
                    SentToSer += 'F';
                    GetFrmSer += 'A';
                } else if (value.charAt(i) == 'R') {
                    SentToSer += 'D';
                    GetFrmSer += 'V';
                } else if (value.charAt(i) == 'S') {
                    SentToSer += 'J';
                    GetFrmSer += 'O';
                } else if (value.charAt(i) == 'T') {
                    SentToSer += 'Q';
                    GetFrmSer += 'M';
                } else if (value.charAt(i) == 'U') {
                    SentToSer += 'V';
                    GetFrmSer += 'S';
                } else if (value.charAt(i) == 'V') {
                    SentToSer += 'H';
                    GetFrmSer += 'P';
                } else if (value.charAt(i) == 'W') {
                    SentToSer += 'E';
                    GetFrmSer += 'D';
                } else if (value.charAt(i) == 'X') {
                    SentToSer += 'O';
                    GetFrmSer += 'U';
                } else if (value.charAt(i) == 'Y') {
                    SentToSer += 'X';
                    GetFrmSer += 'G';
                } else if (value.charAt(i) == 'Z') {
                    SentToSer += 'T';
                    GetFrmSer += 'W';
                } else if (value.charAt(i) == '_') {
                    SentToSer += '_';
                    GetFrmSer += '_';
                } else {
                    SentToSer += '-';
                    GetFrmSer += '-';
                }

                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e( "Conversion Error", "Error encountered while converting.....");
        }
        int SentLen = SentToSer.length();
        int GetLen = GetFrmSer.length();
        // String fullMsg = Cls_Global_Var.AccountId + " " + SentToSer;
        // label4.Text = "Send eteach ACT " + fullMsg +
        // " to 57333 \nAnd press 'Activate' button if you have recieved activation key.";
        return SentToSer;
    }

    public void checkCalender(String className){
        new Rijndael();
        String classNamePath    = accountId + "/" + username + "/" + className;
        String encXmlCal        = PACKAGEPATHFORCALENDER + "/" + algo( accountId + "/" + username + "/" + className) + ".xml";
        String decXMLCal        = PACKAGEPATHFORCALENDER + "/" + algo( accountId + "/" + username + "/" + className) + "D.xml";
        String uptostring       = Constant.INTERNAL_SDCARD + accountId + "/" + username;
        String uniquehddidintab =getUdid();
        if (!new File ( encXmlCal).exists())
        {
            Toast.makeText( getActivity(), "Please reset the application", Toast.LENGTH_LONG).show();
            checkcalpathforpdf=false;
            getActivity().finish();
        }
        if(new File ( encXmlCal).exists())
        {
            try {
                FileInputStream  fileInputStream  =new FileInputStream ( encXmlCal);
                FileOutputStream fileOutputStream =new FileOutputStream ( decXMLCal);
                Rijndael.EDR(fileInputStream, fileOutputStream, 2);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyyMMdd");
            deviceDateString = sdf.format(new Date ());
            disableDateString = deviceDateString;
            deviceDateString = deviceDateString.substring(2, 4) + "-"
                    + deviceDateString.substring(4, 6) + "-"
                    + deviceDateString.substring(6, deviceDateString.length());

            // deviceDateString
            XML.searchContent(decXMLCal, deviceDateString);

            new DeleteFile(decXMLCal);

            if (! XML.dateFound) {
                checkcalpathforpdf=false;
                Toast.makeText( getActivity(), "Product Expired!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent ( getActivity(), Renual.class);
                Log.e( "FilluptoSubject", uptoSubject);

                i.putExtra("uptoSubject", uptoSubject);
                i.putExtra("uptoString", uptostring);
                startActivity(i);
                getActivity().getFragmentManager().popBackStack();
            }
        }
        if(!new File ( uptostring + "/mac.xml").exists())
        {
            Toast.makeText( getActivity(), "Please reset the application", Toast.LENGTH_LONG).show();
            checkcalpathforpdf=false;
            getActivity().getFragmentManager().popBackStack();
        }else{
            try {
                FileInputStream  fis = new FileInputStream ( uptostring + "/mac.xml");
                FileOutputStream fos = new FileOutputStream ( uptostring + "/macD.xml");

                Rijndael.EDR(fis, fos, 2);

                new XMLParser(uptostring + "/macD.xml", "User", "AcountId", "HddId");
                AccId = XMLParser.child1Value;
                DeviceHddId = XMLParser.child2Value;
                new XMLParser(uptostring + "/macD.xml", "User", "UserName", "HddId");
                usernamepdf = XMLParser.child1Value;
                new DeleteFile(uptostring + "/macD.xml");

                FileInputStream  fileInputStreamcal  =new FileInputStream ( encXmlCal);
                FileOutputStream fileOutputStreamcal =new FileOutputStream ( decXMLCal);
                Rijndael.EDR(fileInputStreamcal, fileOutputStreamcal, 2);

                new DeleteFile(encXmlCal);

                new XMLParser ( decXMLCal, "Date", "CardNo", "ClassName");
                calAccountId = XMLParser.child1Value;
                classNamepdf = XMLParser.child2Value;

                new XMLParser(decXMLCal,"Date", "HDDID", "UName");
                pdfHddidinchapter = XMLParser.child1Value;
                pdfusernameinchapter = XMLParser.child2Value;


                FileInputStream  fileInputStreamcalpdf =new FileInputStream ( decXMLCal);
                FileOutputStream fileOutputStreampdf   =new FileOutputStream ( encXmlCal);
                Rijndael.EDR(fileInputStreamcalpdf, fileOutputStreampdf, 1);

                new DeleteFile(decXMLCal);

                String classPath = uptoSubject;
                classPath = classPath.substring(0, classPath.lastIndexOf("/"));
                String classPath12 = classPath.substring( 0, classPath.lastIndexOf( "/"));
                if (!className.equals(classPath12)) {
                    Toast.makeText( getActivity(), "Sorry, please reset the application", Toast.LENGTH_SHORT).show();
                    checkcalpathforpdf=false;
                    getActivity().getFragmentManager().popBackStack();
                }
                if(!pdfHddidinchapter.equals(uniquehddidintab))
                {
                    Toast.makeText( getActivity(), "Invalid User Login!", Toast.LENGTH_SHORT).show();
                    checkcalpathforpdf=false;
                    getActivity().getFragmentManager().popBackStack();
                }
                if(!pdfusernameinchapter.equals(username))
                {
                    Toast.makeText( getActivity(), "Invalid User Login!", Toast.LENGTH_SHORT).show();
                    checkcalpathforpdf=false;
                    getActivity().getFragmentManager().popBackStack();
                }
                if (!AccId.equals(calAccountId)) {
                    Toast.makeText( getActivity(), "Invalid User Login!", Toast.LENGTH_SHORT).show();
                    checkcalpathforpdf=false;
                    getActivity().getFragmentManager().popBackStack();
                } else if (!DeviceHddId.equals(getUdid())) {
                    Toast.makeText( getActivity(), "Device Changed!", Toast.LENGTH_SHORT).show();
                    checkcalpathforpdf=false;
                    getActivity().getFragmentManager().popBackStack();
                } else if (DeviceHddId.equals(getUdid())) {
                    if(pdfHddidinchapter.equals(uniquehddidintab)){
                        if(pdfusernameinchapter.equals(username)){
                            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyyMMdd");
                            deviceDateString = sdf.format(new Date ());
                            disableDateString = deviceDateString;
                            deviceDateString = deviceDateString.substring(2, 4)
                                    + "-"
                                    + deviceDateString.substring(4, 6)
                                    + "-"
                                    + deviceDateString.substring(6,
                                    deviceDateString.length());
                            // Log.e("vallll",MainActivity.serverdate.substring(2,MainActivity.serverdate.length()));
                            if (isDataEnable() && HomeActivity.serverdate != null) {
                                updateCalendarOnline(HomeActivity.serverdate.substring(2,HomeActivity.serverdate.length()),encXmlCal,decXMLCal, 1);
                            } else
                                updateCalendarOnline(deviceDateString,encXmlCal,decXMLCal, 2);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void updateCalendarOnline( String deviceDateString2, String encCalPathPdf, String decCalPathPdf, int mode) {
        // TODO Auto-generated method stub

        try {
            FileInputStream  fis1 = new FileInputStream ( encCalPathPdf);
            FileOutputStream fos1 = new FileOutputStream ( decCalPathPdf);
            new Rijndael();
            Rijndael.EDR(fis1, fos1, 2);
            new DeleteFile(encCalPathPdf);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!calendarUpdated) {
            XML.changeContent(decCalPathPdf,deviceDateString);
            if (mode == 1)
                XML.disablePrevDates(decCalPathPdf, disableDateString);
        }

        if (XML.counterComplete) {
            Toast.makeText( getActivity(), "You have exceeded the maximum usage for today.", Toast.LENGTH_SHORT).show();
            checkcalpathforpdf=false;
            getActivity().getFragmentManager().popBackStack();
        }
        calendarUpdated = true;

        try {
            FileInputStream  fis1 = new FileInputStream ( decCalPathPdf);
            FileOutputStream fos1 = new FileOutputStream ( encCalPathPdf);
            new Rijndael();
            Rijndael.EDR(fis1, fos1, 1);
            new DeleteFile(decCalPathPdf);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            getActivity().getFragmentManager().popBackStack();
        }
    }

    private
    String getUdid() {

        final String macAddr, androidId;
        String       id = null;

        WifiManager wifiMan = (WifiManager )getActivity().getApplicationContext().getSystemService( Context.WIFI_SERVICE);
        WifiInfo    wifiInf = wifiMan.getConnectionInfo();

        if (!wifiMan.isWifiEnabled()) {
            wifiMan.setWifiEnabled(true);
            macAddr = wifiInf.getMacAddress();
            Log.e( "macAddr", "" + macAddr);
            androidId = ""
                    + android.provider.Settings.Secure.getString(
                    getActivity().getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            Log.e( "androidId", "" + androidId);
            UUID deviceUuid = new UUID ( androidId.hashCode(), macAddr.hashCode());
            id = deviceUuid.toString().substring(
                    deviceUuid.toString().lastIndexOf("-") + 1);
            // text.setText(deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1));
            Log.e( "UUID", "" + id);
            wifiMan.setWifiEnabled(false);

        } else {
            macAddr = wifiInf.getMacAddress();
            Log.e( "macAddr", "" + macAddr);
            androidId = ""
                    + android.provider.Settings.Secure.getString(
                    getActivity().getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            Log.e( "androidId", "" + androidId);
            UUID deviceUuid = new UUID ( androidId.hashCode(), macAddr.hashCode());
            id = deviceUuid.toString().substring(
                    deviceUuid.toString().lastIndexOf("-") + 1);
            // text.setText(deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1));
            Log.e( "UUID", "" + id);
        }
        return id;
    }

    public boolean isDataEnable() {
        ConnectivityManager conMgr;
        conMgr = (ConnectivityManager ) getActivity().getSystemService( Context.CONNECTIVITY_SERVICE);
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

    public class LoadPdfFile extends AsyncTask<String, String, String> {

        ProgressDialog dialogpdf;

        @Override
        protected
        String doInBackground( String... params) {
            String s   =params[0].toString();
            String bal = s.substring( 0, s.lastIndexOf( "/"));
            System.out.println( bal);

            String bal1 = s.substring( s.lastIndexOf( "/") + 1);
            System.out.println( bal1);

            String dot = bal1.substring( 0, bal1.lastIndexOf( ".") - 1);
            System.out.println( dot);

            String dotE = bal1.substring( bal1.lastIndexOf( ".") - 1);
            System.out.println( dotE.replace( "E", "D"));

            String total = bal + "/" + dot + dotE.replace( "E", "D");

            try {
                String keyfound = XML.keydecryption();
                new Rijndael(keyfound);
                FileInputStream  fileInputStreamebook  =new FileInputStream ( s);
                FileOutputStream fileOutputStreamebook =new FileOutputStream ( total);
                Rijndael.EDR(fileInputStreamebook, fileOutputStreamebook, 2);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return total;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Commented by me
          /*  Uri uri = Uri.parse(result);
            Intent intent = new Intent(getActivity(),MuPDFActivity.class);
            intent.putExtra("Pdfstatuscheck", "EncPdf");
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);*/
            dialogpdf.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogpdf=new ProgressDialog ( getActivity());
            dialogpdf.setMessage("Loading. Please wait...");
            dialogpdf.setCancelable(false);
            dialogpdf.setProgressStyle( ProgressDialog.STYLE_SPINNER);
            dialogpdf.show();
        }

    }

    public void start( String bordToChapter, int flag){
        ListPreference = prefs.getString("listPref", "digiGreen");
        if(ListPreference.equals("digiGreen")){
            Intent intent = new Intent ( getActivity(), SettingUi.class);
            intent.putExtra("myBoardToChapter", bordToChapter);
            intent.putExtra("Flag", flag);
            intent.putExtra("requestCode", "61");
            startActivityForResult(intent, 61);
        }else if(ListPreference.equals("1")){
            Constant.ContentPath= Constant.INTERNAL_SDCARD;
            doTheTask(Constant.INTERNAL_SDCARD,bordToChapter,flag);

        }else if(ListPreference.equals("2")){
            if(Constant.EXTERNAL_SDCARD.equals("")){
                SharedPreferences shared = getActivity().getSharedPreferences( "path", 0);
                Constant.EXTERNAL_SDCARD = (shared.getString("pathdemo", ""));
                Constant.ContentPath= (shared.getString("pathdemo", ""));
                if(Constant.EXTERNAL_SDCARD.equals("max")){
                    Intent intent = new Intent ( getActivity(), SettingUi.class);
                    intent.putExtra("myBoardToChapter", bordToChapter);
                    intent.putExtra("Flag", flag);
                    startActivityForResult(intent, 61);
                }
            }
            Constant.ContentPath= Constant.EXTERNAL_SDCARD;
            doTheTask(Constant.EXTERNAL_SDCARD,bordToChapter,flag);
        }
    }

    private void doTheTask( String path4, String bordToChapter, int flag) {
        play(path4,bordToChapter,flag);
    }

    private void play( String path4, String bordToChapter, int flag) {
        //String path2=path4+bordToChapter+"/index1_E.html";
        boolean check=true;
        for (int i = 0; i < mMounts.size(); i++) {
            String val   = mMounts.get( i) + "/eteach/assets/Content/";
            String path2 = val + bordToChapter + "/topics_E.xml";
            Log.e( "", "" + path2);
            zipfolderPath=path4;
            File file1 = new File ( path2);
            if(file1.exists()){
                check=false;
                StatFs stat_fs = new StatFs ( mMounts.get( i).toString());

                double avail_sd_space = (double)stat_fs.getAvailableBlocks() *(double)stat_fs.getBlockSize();
                //double MB_Available = (avail_sd_space / 10485783);
                MB_Available = (avail_sd_space /1048576);
                // MB_Available=MB_Available-20;
                Log.d( "AvailableMB", "" + MB_Available);

                if(MB_Available>=100)	{
                    if(flag==1){
                        Intent goToBoard =new Intent ( getActivity().getApplicationContext(), PlayDemoVedioFile.class);
                        goToBoard.putExtra("path",path2);
                        goToBoard.putExtra("uptoString", uptoString);
                        goToBoard.putExtra("uptoSubject", uptoSubject);

                        goToBoard.putExtra("path4", path4);
                        goToBoard.putExtra("bordToChapter", bordToChapter);
                        goToBoard.putExtra("flag", flag);
                        goToBoard.putExtra("chaptername",chaptername );

                        goToBoard.putExtra("encryptionKey",Constant.MAC_XML_DEMO_KEY);
                        goToBoard.putExtra("salt",Constant.MAC_XML_DEMO_SALT);
                        goToBoard.putExtra("InitVector",Constant.MAC_XML_DEMO_IV);

                        startActivity(goToBoard);
                    }else if(flag==2){

                        if(path2.contains("ebook") ||path2.contains("eQuestion")){
                            //							Intent goToBoard=new Intent(getApplicationContext(),Playingqbankebook.class);
                            //							goToBoard.putExtra("path",path2);
                            //							goToBoard.putExtra("uptoString", uptoString);
                            //							goToBoard.putExtra("uptoSubject", uptoSubject);
                            //
                            //							goToBoard.putExtra("path4", path4);
                            //							goToBoard.putExtra("bordToChapter", bordToChapter);
                            //							goToBoard.putExtra("flag", flag);
                            //							goToBoard.putExtra("chaptername",chaptername );
                            //
                            //							startActivity(goToBoard);
                        }
                        else{
                            Intent goToBoard =new Intent ( getActivity().getApplicationContext(), PlayActualVedioFile.class);
                            goToBoard.putExtra("path",path2);
                            goToBoard.putExtra("uptoString", uptoString);
                            goToBoard.putExtra("uptoSubject", uptoSubject);

                            goToBoard.putExtra("path4", path4);
                            goToBoard.putExtra("bordToChapter", bordToChapter);
                            goToBoard.putExtra("flag", flag);
                            goToBoard.putExtra("chaptername",chaptername );
                            goToBoard.putExtra("encryptionKey",Constant.MAC_XML_FILE_ACTUAL_KEY);
                            goToBoard.putExtra("salt",Constant.MAC_XML_FILE_ACTUAL_SALT);
                            goToBoard.putExtra("InitVector",Constant.MAC_XML_FILE_ACTUAL_IV);

                            startActivity(goToBoard);
                        }
                    }}
                else{
                    Toast.makeText( getActivity(), MB_Available + "MB is available, minimum 100 MB is required to run this file", Toast.LENGTH_LONG).show();
                }
            }if(!check)
                break;
        }
        if(check)
        {
            checkInternetConnection(path4+bordToChapter,flag);
        }
    }

    private void getDownloadChapter( String path, int mode){

        File file =new File ( path);
        if(file.exists())
        {
            File[] files =file.listFiles();
            for(File f:files)
            {
                if(f.isDirectory()){
                    getDownloadChapter(f.getAbsolutePath(),mode);
                }
                else if(f.isFile())
                {
                    if(f.getAbsolutePath().contains("topics_E.xml")){
                        String fileName    =f.getAbsoluteFile().toString();
                        String chapterName =fileName.substring( 0, fileName.lastIndexOf( "/"));
                        String s           = chapterName;
                        s=fileName.substring(0, s.lastIndexOf("/"));
                        String nchapterName =path.replace( s + "/", "");
                        if(mode==1){
                            downCharray.add(nchapterName.replace("_", " "));
                        }else if(mode==2){
                            demodownarray.add(nchapterName.replace("_", " "));
                        }
                    }
                }
            }
        }
    }

    private static void compareMountsWithVold(){

        for (int i = 0; i < mMounts.size(); i++) {
            String mount = mMounts.get( i);
            if (!mVold.contains(mount))
                mMounts.remove(i--);
        }
        mVold.clear();
    }

    // Handle the Spinner functionality
    public void setSpinnerData(){
        getBoardData();
        setSpBoard();
    }

    // Adding data in boardArraylist
    public void getBoardData(){

            if( new File ( path + "/board.xml").exists() && new File ( path + "/cbselanguage.xml").exists() && new File ( path + "/cbseclass.xml").exists() && new File ( path + "/cbsesubject.xml").exists() && new File ( path + "/cbsechapter.xml").exists())
            {
                try {
                    FileInputStream  fis =new FileInputStream ( path + "/board.xml");
                    FileOutputStream fos =new FileOutputStream ( path + "/boardD.xml");

                    new Rijndael();
                    Rijndael.EDR(fis, fos, 2);

                    LayoutXmlParser parser =new LayoutXmlParser(path+"/boardD.xml");
                    boardArrayList = parser.parse(getActivity());
                    if(boardArrayList==null)
                        showReloadDialog();

                    new DeleteFile(path+"/boardD.xml");
                    languageArrayList=new ArrayList<String> ();
                    classArrayList=new ArrayList<String> ();
                    subjectArrayList=new ArrayList<String> ();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                showReloadDialog();
            }

    }

    @SuppressWarnings("deprecation")
    private void showReloadDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(), R.style.MyDialogTheme);
        builder.setTitle("eTeach");
        builder.setMessage("Your all important files are missing.\n do you want to restore this application?");

        String positiveText = getString( android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which) {

                        AlertDialog.Builder builder = new AlertDialog.Builder( new ContextThemeWrapper ( getActivity(), R.style.AppCompatAlertDialogStyle));
                        LayoutInflater      li      = LayoutInflater.from( getActivity());
                        final View          v1      =li.inflate( R.layout.reload_alert_dialog, null);
                        builder.setIcon(android.R.drawable.ic_input_get);
                        builder.setView(v1);
                        builder.setTitle("Please enter your username and password.");
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick( DialogInterface dialog, int which) {
                                EditText u =(EditText ) v1.findViewById( R.id.username);
                                EditText p =(EditText ) v1.findViewById( R.id.password);
                                if(u.getText().toString().length()!=0&&p.getText().toString().length()!=0){
                                    Intent intent =new Intent ( getActivity(), HomeActivity.class);
                                    new Reload ( getActivity(), intent ).execute( u.getText().toString(), p.getText().toString());

                                }else{
                                    Toast.makeText( getActivity(), "Please enter valid details", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();

                                }
                            }
                        });
                        builder.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick( DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

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
        dialog.setIcon(R.drawable.logo);
        // display dialog
        dialog.show();

    }

    // Set the data in board spinner
    public void setSpBoard(){

        boardArrayAdapter = new ArrayAdapter<Board> ( view.getContext(), android.R.layout.simple_spinner_item, boardArrayList);
        boardArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if(boardArrayList!=null) {
            spBoard.setAdapter(boardArrayAdapter);
        }

        spBoard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id) {
                try {
                    subjectArrayList.clear();
                    classArrayList.clear();
                    languageArrayList.clear();
                    lang="";
                    clas="";
                    sub="";
                    FileInputStream  fis =new FileInputStream ( path + "/cbselanguage.xml");
                    FileOutputStream fos =new FileOutputStream ( path + "/cbselanguageD.xml");

                    new Rijndael();
                    Rijndael.EDR(fis, fos, 2);
                    LayoutXmlParser parser =new LayoutXmlParser(path+"/cbselanguageD.xml");
                    board =boardArrayList.get(position).getActualName();
                    languageArrayList = parser.parse(board);
                    new DeleteFile(path+"/cbselanguageD.xml");
                    boardArrayAdapter.notifyDataSetChanged();
                    setSpMedium();
                    if(classArrayAdapter!=null)
                        classArrayAdapter.clear();
                    setSpClass();
                    setSpSubject();

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void setSpMedium(){

        languageArrayAdapter = new ArrayAdapter<String> ( view.getContext(), android.R.layout.simple_spinner_item, languageArrayList);
        languageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMedium.setAdapter(languageArrayAdapter);

        spMedium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id) {
                //	langListView.setSelector(R.drawable.focused);
                subjectArrayList.clear();
                classArrayList.clear();
                clas="";
                sub="";

                try {
                    FileInputStream  fis =new FileInputStream ( path + "/cbseclass.xml");
                    FileOutputStream fos =new FileOutputStream ( path + "/cbseclassD.xml");

                    new Rijndael();
                    Rijndael.EDR(fis, fos, 2);



                    LayoutXmlParser parser =new LayoutXmlParser(path+"/cbseclassD.xml");

                    lang=languageArrayList.get(position);
                    classArrayList=parser.parse(board+"/"+lang);
                    new DeleteFile(path+"/cbseclassD.xml");
                    languageArrayAdapter.notifyDataSetChanged();
                    setSpClass();
                    setSpSubject();

                }
                catch (Exception e) {
                    // TODO: handle exception
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setSpClass(){

        classArrayAdapter = new ArrayAdapter<String> ( view.getContext(), android.R.layout.simple_spinner_item, classArrayList);
        classArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClass.setAdapter(classArrayAdapter);

        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id) {

                //classListView.setSelector(R.drawable.listviewbuttionclick);
                sub="";
                try {
                    FileInputStream  fis =new FileInputStream ( path + "/cbsesubject.xml");
                    FileOutputStream fos =new FileOutputStream ( path + "/cbsesubjectD.xml");

                    new Rijndael();
                    Rijndael.EDR(fis, fos, 2);

                    LayoutXmlParser parser =new LayoutXmlParser(path+"/cbsesubjectD.xml");
                    clas=classArrayList.get(position);
                    subjectArrayList = parser.parse(board+"/"+lang+"/"+clas.replace(" ", "_"));
                    new DeleteFile(path+"/cbsesubjectD.xml");
                    classArrayAdapter.notifyDataSetChanged();
                    setSpSubject();
                }catch (Exception e) {
                    // TODO: handle exception
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void setSpSubject(){

        subjectArrayAdapter = new ArrayAdapter<String> ( this.getContext(), android.R.layout.simple_spinner_item, subjectArrayList);
        subjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubject.setAdapter(subjectArrayAdapter);

        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id) {

                //subjectListView.setSelector(R.drawable.focused);
                //LayoutXmlParser parser =new LayoutXmlParser("cbsechapter.xml", EDUSlidefullActivity.this);
                sub=subjectArrayList.get(position);

                path1=board+"/"+lang+"/"+clas+"/"+sub+"/";
                uptoSubject =path1;


                //chapterArrayList=parser.parseChapter(board+"/"+lang+"/"+clas+"/"+sub.replaceAll(" ", "_")+"/");
                //registerChapterListView();
                subjectArrayAdapter.notifyDataSetChanged();

               /* Intent chapterview=new Intent(getApplicationContext(), Chapter.class);

                chapterview.putExtra("path", path1.replace(" ", "_"));
                chapterview.putExtra("path1", path.replace(" ", "_"));
                startLoader("Loading. Please wait...");
                startActivity(chapterview);*/



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void startLoader(){
        dialog=new ProgressDialog ( getActivity());
        dialog.setCancelable(true);
        dialog.setMessage("Loading. Please wait...");
        dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    public void show(){
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             initComponent();

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void  checkInternetConnection( String path6, int flag) {
        ConnectivityManager conMgr = (ConnectivityManager )getActivity().getSystemService ( Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected())
        {
            if(ListPreference.equals("2") && new File ( Constant.EXTERNAL_SDCARD ).exists()){
                download(path6,flag);
            }else if(ListPreference.equals("1")&& new File ( Constant.INTERNAL_SDCARD).exists()){
                download(path6,flag);
            }else {
                Toast.makeText( getActivity(), "No SD card present.", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText( getActivity(), "Your internet connection is not available.", Toast.LENGTH_LONG).show();
        }
    }

    private void download( String path6, int flag) {

        if(isMyServiceRunningfordownloadAll() && isMyServiceRunning()){
            Intent intent = new Intent ( getActivity(), DownloadChapter.class);
            intent.addCategory( DownloadChapter.TAG);
            path2=path6;
            if(flag ==1){

                intent.putExtra("downloadUrl","/eteach/assets/Content/"+"demo/"+uptoSubject+chaptername+"/"+chaptername+".zip");
                intent.putExtra("downloadPath", path6+"/"+chaptername+".zip");
                intent.putExtra("chaptername", chaptername);
                intent.putExtra("uptoSubject",uptoSubject );

                //				new Download(this, path6+"/"+chaptername+".zip").execute("/eteach/assets/Content/"+"Demo/"+uptoSubject+chaptername+"/"+chaptername+".zip");
            }else if(flag==2){

                if(chaptername.contains("ebook") || chaptername.contains("eQuestion")){
                    //String[] eqString=chaptername.split(buttonText);
                    //Log.e("", ""+eqString);

                    String[] eqString = null;
                    if(chaptername.contains("ebook"))
                    {
                        eqString=chaptername.split("ebook");
                    }
                    if(chaptername.contains("eQuestion"))
                    {
                        eqString=chaptername.split("eQuestion");
                    }

                    intent.putExtra("downloadUrl", "/eteach/assets/Content/"+"Actual/"+uptoSubject+eqString[0].toString().replaceAll(" ", "_")+"/"+chaptername+".zip");
                    //String paString=path6.substring(0, path6.lastIndexOf("/"));
                    intent.putExtra("downloadPath",path6+"/"+chaptername+".zip");
                    intent.putExtra("chaptername",chaptername );
                    intent.putExtra("uptoSubject",uptoSubject );
                }
                else{
                    //if(buttonText!=null){
                    intent.putExtra("downloadUrl", "/eteach/assets/Content/"+uptoSubject+chaptername+"/"+chaptername+".zip");
                    intent.putExtra("downloadPath",path6+"/"+chaptername+".zip");
                    intent.putExtra("uptoSubject",uptoSubject );
                    intent.putExtra("chaptername", chaptername);
                    //	}
                    //				new Download(this, path6+"/"+chaptername+".zip").execute("/eteach/assets/Content/"+"Actual/"+uptoSubject+chaptername+"/"+chaptername+".zip");
                }
            }
            getActivity().startService(intent);
        }else{
            Toast.makeText( getActivity(), "Please wait, another chapter is downloading...", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager ) getActivity().getSystemService( getActivity().getApplicationContext().ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices( Integer.MAX_VALUE)) {
            if ("com.eteach.sol.DownloadChapter".equals(service.service.getClassName())) {
                return false;
            }
        }
        return true;
    }

    private boolean isMyServiceRunningfordownloadAll() {
        ActivityManager manager = (ActivityManager ) getActivity().getSystemService( getActivity().getApplicationContext().ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices( Integer.MAX_VALUE)) {
            if ("com.eteach.sol.DownloadService".equals(service.service.getClassName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            String boardToChapter =data.getStringExtra( "boardToChapter");
            int    flag           =data.getIntExtra("Flag", 3);
            if(requestCode == 61){
                ListPreference = prefs.getString("listPref", "nr1");
                if(ListPreference.equals("1")){
                    doTheTask(Constant.INTERNAL_SDCARD,boardToChapter,flag);
                }else if(ListPreference.equals("2")){
                    if(Constant.EXTERNAL_SDCARD.equals("")){
                        SharedPreferences shared = getActivity().getSharedPreferences( "path", 0);
                        Constant.EXTERNAL_SDCARD = (shared.getString("pathdemo", ""));
                        if(Constant.EXTERNAL_SDCARD.equals("")){

                        }else{
                            doTheTask(Constant.EXTERNAL_SDCARD,boardToChapter,flag);
                        }
                    }

                }else{
                    return;
                }
            }
        }
    }

}
