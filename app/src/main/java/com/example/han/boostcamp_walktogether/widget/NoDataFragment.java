package com.example.han.boostcamp_walktogether.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-08-17.
 */

public class NoDataFragment extends Fragment {

    public static NoDataFragment newInstance() {

        Bundle args = new Bundle();

        NoDataFragment fragment = new NoDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.no_data_fragmet,container,false);
        return view;
    }
}
