package com.dmw.eteachswayam.exo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.dmw.eteachswayam.R;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;

import java.io.File;
import java.util.ArrayList;

public
class MainActivity extends AppCompatActivity {
//CustomVideoPlayer customVideoPlayer;



    ArrayList<MediaSource> mediaSourceArrayList ;
    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.layout_video_player );
        //CustomVideoPlayer customVideoPlayer = findViewById(R.id.customVideoPlayer);

        try {

            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermission();
            }
            else {
            }
            mediaSourceArrayList = new ArrayList <> (  );
            String urimp4   =  "/Movies/eteach v";
            File   file     = new File ( Environment.getExternalStorageDirectory ().getAbsolutePath() + urimp4 );
            File       file1 [] = file.listFiles ();
            if ( file.exists () )
            {
                int f =  file1.length;
            }


           // Uri mp4VideoUri1 = Uri.parse( String.valueOf ( file1[0] ) );
           // Uri mp4VideoUri2 = Uri.parse( String.valueOf ( file1[1] ) );
            //customVideoPlayer.setMediaUrl ( "https://www.rmp-streaming.com/media/bbb-360p.mp4" );
            //String vurl = "android.resource://"+getPackageName ()+"/"+R.raw.demo;
            //customVideoPlayer.setMediaUrl ( vurl );
            //customVideoPlayer.setMediaUrl ( String.valueOf ( mp4VideoUri1 ) );
           // customVideoPlayer.play ();
            //customVideoPlayer.play ();
            //customVideoPlayer.enableAutoPlay ( true );
        }catch ( Exception e )
        {
            e.printStackTrace ();
        }
    }
    private
    void checkPermission ( ) {
        if ( ContextCompat.checkSelfPermission( this,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
             != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission( this,
                                                                                        Manifest.permission.READ_EXTERNAL_STORAGE)
                                                     != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement

            ActivityCompat.requestPermissions( this,
                                               new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                                               123);

        } else {

        }
    }

    @Override
    public
    void onRequestPermissionsResult ( int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults ) {
        super.onRequestPermissionsResult ( requestCode , permissions , grantResults );
        switch (requestCode) {
            case 123: {


                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)     {
                    //Peform your task here if any
                } else {

                    checkPermission();
                }
                return;
            }
        }
    }
}
