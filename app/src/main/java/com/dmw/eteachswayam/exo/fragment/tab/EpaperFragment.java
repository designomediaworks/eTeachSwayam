package com.dmw.eteachswayam.exo.fragment.tab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.activity.QuestionActivity;
import com.dmw.eteachswayam.exo.model.Constant;

import java.io.File;


public class EpaperFragment extends Fragment {

    WebViewClient client;
    private ProgressDialog dialog;
    private WebView        webView;
    View view;
   /* public EpaperFragment() {

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
      view = inflater.inflate(R.layout.fragment_epaper, container, false);

        webView = (WebView ) view.findViewById( R.id.webview_epaper);
        webView.clearView();
        clearBrowsercache();
        webView.clearCache(true);

        // new Help(Quesions.this,"Question","Question");

        if(isDataEnable()){
            webView.getSettings().setPluginState( WebSettings.PluginState.ON);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle( WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setWebViewClient(new WebViewClient (){
                @Override
                public void onPageFinished( WebView view, String url) {
                    super.onPageFinished(view, url);
                    dialog.dismiss();
                }
            });
            webView.setScrollbarFadingEnabled(true);
            webView.loadUrl( "http://" + Constant.WEBSERVER + "/QPG_PRO/");
           // setContentView(webView);
          //  startLoader();
          //  getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else{
            Intent intent =new Intent ( getActivity(), QuestionActivity.class);
            getActivity().finish();
            startActivity(intent);
        }

        return view;
    }

    public void startLoader(){
        dialog=new ProgressDialog ( getActivity());
        dialog.setCancelable(true);
        dialog.setMessage("Loading. Please wait...");
        dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    public boolean isDataEnable() {
        ConnectivityManager conMgr;
        Context             context = getActivity();
        conMgr = (ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE);
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

    private void clearBrowsercache() {
        File dir = getActivity().getCacheDir();

        if(dir!= null && dir.isDirectory()){
            try{
                File[] children = dir.listFiles();
                if (children.length >0) {
                    for (int i = 0; i < children.length; i++) {
                        File[] temp = children[i].listFiles();
                        for(int x = 0; x<temp.length; x++)
                        {
                            temp[x].delete();
                        }
                    }
                }
            }catch(Exception e)
            {
                Log.e( "Cache", "failed cache clean");
            }
        }
    }

}
