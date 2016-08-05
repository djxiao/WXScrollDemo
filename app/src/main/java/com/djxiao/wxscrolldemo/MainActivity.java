package com.djxiao.wxscrolldemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.djxiao.wxscrolldemo.views.ColorGradient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener,View.OnClickListener {

    private ViewPager pager;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private String[] titles = {"first","seconde","third","fourth"};
    private FragmentPagerAdapter adapter;
    private List<ColorGradient> mtabs = new ArrayList<ColorGradient>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        initData();
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
    }

    private void initData(){
        for(String title:titles){
            TagFragment fragment = new TagFragment();
            Bundle args = new Bundle();
            args.putString("title",title);
            fragment.setArguments(args);
            fragments.add(fragment);
        }
       adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
           @Override
           public Fragment getItem(int position) {
               return fragments.get(position);
           }

           @Override
           public int getCount() {
               return fragments.size();
           }
       };
        initTabIndicator();
    }

    private void initTabIndicator(){
        ColorGradient one = (ColorGradient) findViewById(R.id.layout_wx);
        ColorGradient two = (ColorGradient) findViewById(R.id.layout_txl);
        ColorGradient three = (ColorGradient) findViewById(R.id.layout_fx);
        ColorGradient four = (ColorGradient) findViewById(R.id.layout_w);
        mtabs.add(one);
        mtabs.add(two);
        mtabs.add(three);
        mtabs.add(four);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        one.setAlpha(1);
    }




    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if(positionOffset > 0){
            ColorGradient left = mtabs.get(position);
            ColorGradient right = mtabs.get(position+1);
            left.setAlpha(1-positionOffset);
            right.setAlpha(positionOffset);
        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void resetAlpha(){
        for(ColorGradient tab:mtabs){
            tab.setAlpha(0);
        }
    }

    @Override
    public void onClick(View view) {
        resetAlpha();
        switch (view.getId()){
            case R.id.layout_wx:
                mtabs.get(0).setAlpha(1);
                pager.setCurrentItem(0,false);
                break;
            case R.id.layout_txl:
                mtabs.get(1).setAlpha(1);
                pager.setCurrentItem(1,false);
                break;
            case R.id.layout_fx:
                mtabs.get(2).setAlpha(1);
                pager.setCurrentItem(2,false);
                break;
            case R.id.layout_w:
                mtabs.get(3).setAlpha(1);
                pager.setCurrentItem(3,false);
                break;
        }
    }
}
