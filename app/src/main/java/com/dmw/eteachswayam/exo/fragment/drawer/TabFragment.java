package com.dmw.eteachswayam.exo.fragment.drawer;

/**
 * Created by Chetan on 12/1/2015.
 */
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.fragment.tab.ActivatedClassFragment;
import com.dmw.eteachswayam.exo.fragment.tab.AllClassesFragment;
import com.dmw.eteachswayam.exo.fragment.tab.Edu_PuzzleFragment;
import com.dmw.eteachswayam.exo.fragment.tab.EpaperFragment;
import com.dmw.eteachswayam.exo.fragment.tab.GlosaryFragment;
import com.dmw.eteachswayam.exo.fragment.tab.ImagePagerView;
import com.dmw.eteachswayam.exo.fragment.tab.SelfFragment;
import com.dmw.eteachswayam.exo.fragment.tab.SubscriptionFragment;


public class TabFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 7 ;


    @Override
    public
    View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate( R.layout.tab_layout, null);
        tabLayout = (TabLayout ) x.findViewById( R.id.tabs);
        viewPager = (ViewPager ) x.findViewById( R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable () {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public
        Fragment getItem( int position)
        {
            switch (position){

                case 0 : return new ImagePagerView ();
                case 1 : return new SubscriptionFragment ();
                case 2 : return new ActivatedClassFragment ();
                case 3 : return new AllClassesFragment ();
                case 4 : return new EpaperFragment ();
                case 5 : return new Edu_PuzzleFragment ();
                case 6 : return new SelfFragment ();
                case 7 : return new GlosaryFragment ();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public
        CharSequence getPageTitle( int position) {

            switch (position){
                case 0:
                    return "Home";
                case 1 :
                    return "Subscription";
                case 2 :
                    return "Activated Class";
                case 3 :
                    return "All Classes";
                case 4 :
                    return "ePaper";
                case 5 :
                    return "Edu-Puzzle";
                case 6 :
                    return  "Shelf";
                case 7 :
                    return  "Glossary";

            }
            return null;
        }
    }

}
