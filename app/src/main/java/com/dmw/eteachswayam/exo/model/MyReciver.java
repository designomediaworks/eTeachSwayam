package com.dmw.eteachswayam.exo.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.dmw.eteachswayam.exo.fragment.tab.AllClassesFragment;


public class MyReciver extends BroadcastReceiver {

	@Override
	public void onReceive( Context context, Intent intent) {
		Log.e( "", "---------------------------------------------------------------------------------------");
		if(intent.getStringExtra("Messsage").equals("OK")){
			if( AllClassesFragment.handler != null)
				AllClassesFragment.handler.notifyDataSetChanged();
		/*	if(ActivateDemoActivity.adapter!=null)
				ActivateDemoActivity.adapter.notifyDataSetChanged();*/
			Log.e( "", "------------------------------------ok----------------------------------");

		}else if(intent.getStringExtra("Messsage").equals("ERROR")){
			Log.e( "", "------------------------------------ERROR----------------------------------");
			Toast.makeText( context, "Please Try Again.", Toast.LENGTH_LONG).show();
		}else if(intent.getStringExtra("Messsage").equals("FNF")){
			Log.e( "", "------------------------------------File Not Found----------------------------------");
			Toast.makeText( context, "File is not Present at Server.\n It will Be Updated shortly.", Toast.LENGTH_LONG).show();
		}else if(intent.getStringExtra("Messsage").equals("Not")){
			Log.e( "", "------------------------------------Interepted----------------------------------");
			Toast.makeText( context, "File downloading interrupted.", Toast.LENGTH_LONG).show();
		}
	}
}