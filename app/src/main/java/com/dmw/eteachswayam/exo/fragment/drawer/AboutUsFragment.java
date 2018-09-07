package com.dmw.eteachswayam.exo.fragment.drawer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmw.eteachswayam.R;


public class AboutUsFragment extends Fragment {
TextView tvAboutUs;
    public AboutUsFragment() {
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
       // return inflater.inflate(R.layout.fragment_about_us, container, false);
        View view = inflater.inflate( R.layout.fragment_about_us,
                                      container, false);

        tvAboutUs = (TextView )view.findViewById( R.id.tvAboutUs);
        tvAboutUs.setText( Html.fromHtml( "<html>\n" +
                                          "<body>\n" +
                                          "\n" +
                                          "<h2>This Product is developed by `Designo Media Works (I) Pvt. Ltd. Nagpur`.  </h2> <p>Address - Designo, 390, Opp. Rashtriya Vidyalaya</br>Hanuman Nagar, Krida Square Nagpur-440009</p></br></br>\n" +
                                          "<p>Call us on 0712-6694444/6694450</p></br>\n" +
                                          "<p>Visit us at www.designomediaworks.com</p>\n" +
                                          "\n" +
                                          "</body>\n" +
                                          "</html>\n"));
        return view;
    }

    /*public void setText(String url) {
        TextView view = (TextView) getView().findViewById(R.id.tvAboutUs);
        view.setText(url);
    }*/


}
