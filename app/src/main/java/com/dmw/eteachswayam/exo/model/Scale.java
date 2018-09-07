package com.dmw.eteachswayam.exo.model;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class Scale {

	private static Activity c;

	public Scale(Activity c)
	{
		Scale.c = c;
		setDensity();
	}

	public static int Cam_width;
	public static int Cam_height;
	public static float screenSize; 


	public static int SetScale(int x) {
		
		float d = c.getResources().getDisplayMetrics().density;
		int margin = (int) (x * d);
		return margin;
	}

	public static void setDensity()
	{
		DisplayMetrics dm = new DisplayMetrics ();
		c.getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		Display display = ( (WindowManager ) c.getSystemService( Context.WINDOW_SERVICE)).getDefaultDisplay();
		Cam_width = display.getWidth();
		Cam_height = display.getHeight();
		screenSize=(float) Math.sqrt( Math.pow( dm.widthPixels / dm.xdpi, 2) + Math.pow( dm.heightPixels / dm.ydpi, 2));

	}
	
	
}
