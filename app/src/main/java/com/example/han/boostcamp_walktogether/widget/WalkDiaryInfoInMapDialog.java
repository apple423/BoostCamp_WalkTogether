package com.example.han.boostcamp_walktogether.widget;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.han.boostcamp_walktogether.R;

/**
 * Created by Han on 2017-08-16.
 */

public class WalkDiaryInfoInMapDialog extends DialogFragment {

    private Button mConfirmButton;

    public static WalkDiaryInfoInMapDialog newInstance() {

        Bundle args = new Bundle();

        WalkDiaryInfoInMapDialog fragment = new WalkDiaryInfoInMapDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_walk_diary_info_map,container,false);
        mConfirmButton = (Button) view.findViewById(R.id.walk_diary_info_confirm_button);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
