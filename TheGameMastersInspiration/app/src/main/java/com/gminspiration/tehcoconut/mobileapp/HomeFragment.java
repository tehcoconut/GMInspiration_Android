package com.gminspiration.tehcoconut.mobileapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by tehcoconut on 5/30/15.
 */
public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tv = (TextView) v.findViewById(R.id.tv_home);

        Spanned spanned = Html.fromHtml("<b>Hello World!</b><p>Goodbye World!</p><br>");

        tv.setText(spanned.toString());


        return v;
    }


}
