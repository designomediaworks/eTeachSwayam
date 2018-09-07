package com.dmw.eteachswayam.exo.fragment.tab;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.adaptor.FragmentPagerAdapter;
import com.dmw.eteachswayam.exo.model.Images;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class ImagePagerView extends Fragment implements OnPageChangeListener {

	private int position = 0, totalImage;
	private ViewPager            viewPage;
	private ArrayList<Integer>   itemData;
	private FragmentPagerAdapter adapter;
	private Images               imageId;

	private static int currentPage = 0;
	private static int NUM_PAGES = 0;
	public ImagePagerView() {
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
		View x =  inflater.inflate( R.layout.imageview_page, null);

		viewPage = (ViewPager )x. findViewById( R.id.viewPager);

		imageId = new Images();
		itemData = imageId.getImageItem();
		totalImage = itemData.size();
		NUM_PAGES = itemData.size();
		adapter = new FragmentPagerAdapter(getChildFragmentManager(),
				itemData);
		viewPage.setAdapter(adapter);

		init();
		return x;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		this.position = position;
		//setPage(position);
	}

	private void init() {

		// Auto start of viewpager
		final Handler handler = new Handler ();
		final Runnable Update = new Runnable () {
			public void run() {
				if (currentPage == NUM_PAGES) {
					currentPage = 0;
				}
				viewPage.setCurrentItem(currentPage++, true);
			}
		};
		Timer swipeTimer = new Timer ();
		swipeTimer.schedule(new TimerTask () {
			@Override
			public void run() {
				handler.post(Update);
			}
		}, 1000, 10000);

	}

}
