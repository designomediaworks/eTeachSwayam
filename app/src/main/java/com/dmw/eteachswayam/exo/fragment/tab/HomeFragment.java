package com.dmw.eteachswayam.exo.fragment.tab;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.dmw.eteachswayam.R;

import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {
    public int currentimageindex=0;
    //    Timer timer;
//    TimerTask task;
    ImageView slidingimage;
    View      x;

    private int[] IMAGE_IDS = {
            R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f,
            R.drawable.g, R.drawable.h
    };


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public
    View onCreateView( LayoutInflater inflater, ViewGroup container,
                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         x =  inflater.inflate(R.layout.fragment_home,null);

        final Handler mHandler = new Handler ();

        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable () {
            public void run() {

                slidingimage = (ImageView )x.findViewById( R.id.ImageView3_Left);
                slidingimage.setImageResource(IMAGE_IDS[currentimageindex%IMAGE_IDS.length]);

                currentimageindex++;

                Animation rotateimage = AnimationUtils.loadAnimation( getActivity(), R.anim.fade_in);

                slidingimage.startAnimation(rotateimage);

            }
        };

        int delay = 500; // delay for 1 sec.

        int period = 5000; // repeat every 4 sec.

        Timer timer = new Timer ();

        timer.scheduleAtFixedRate(new TimerTask () {

            public void run() {

                mHandler.post(mUpdateResults);

            }

        }, delay, period);

        return x;
    }

}