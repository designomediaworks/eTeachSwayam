package com.dmw.eteachswayam.exo.fragment.drawer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.activity.HomeActivity;


public class TermsConditionFragment extends Fragment {

    View   view;
    Button okButton;
    public TermsConditionFragment() {

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
        view = inflater.inflate(R.layout.fragment_terms_condition, container, false);
        okButton = (Button ) view.findViewById( R.id.oktermButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }
}
