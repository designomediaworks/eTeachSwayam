package com.dmw.eteachswayam.exo.model;



import com.dmw.eteachswayam.R;

import java.util.ArrayList;

public class Images {
    private ArrayList<Integer> imageId;
    public Images() {
        imageId = new ArrayList<Integer> ();
        imageId.add( R.drawable.a);
        imageId.add(R.drawable.b);
        imageId.add(R.drawable.c);
        imageId.add(R.drawable.d);
        imageId.add(R.drawable.e);
        imageId.add(R.drawable.f);
        imageId.add(R.drawable.g);
        imageId.add(R.drawable.h);
    }
    public
    ArrayList<Integer> getImageItem() {
        return imageId;
    }
}
