package com.dmw.eteachswayam.exo.fragment.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.activity.HomeActivity;


public class SupportFragment extends Fragment {

    View v;

    private TextView textView;
    String support = "For any kind of support on eTeach application or content, " +
                     "you may:<BR/><BR/>Call us on <B> 0712-6493999</B><BR/>Visit us at eteach.co.in/help.html<BR/>" +
                     "Mail us at support@eteach.co.in<BR/><BR/>For hardware support, please contact your device manufacturer/ vendor.<BR/><BR/>We would be happy to serve!";

    private Button okButton;


    public SupportFragment() {
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
        v = inflater.inflate(R.layout.fragment_support, container, false);

        initComponent();

        return v;
    }

    private void initComponent() {
        ganerateId();
        registerEvent();
    }

    private void ganerateId() {
        textView=(TextView )v. findViewById( R.id.supportTextView);
        okButton=(Button ) v.findViewById( R.id.sokButtonafternewchanges);
        textView.setText( Html.fromHtml( support));

    }
    private void registerEvent() {
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent ( getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

    }

}
