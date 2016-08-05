package com.djxiao.wxscrolldemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author djxiao
 * @create 2016/8/3 16:08
 * @DESC
 */
public class TagFragment extends Fragment {

    private String mTitle = "Default";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getArguments() != null){
            mTitle = getArguments().getString("title");
        }
        TextView textView = new TextView(getActivity());
        textView.setText(mTitle);
        textView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        return textView;
    }
}
