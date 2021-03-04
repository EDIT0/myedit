package com.localinfo.je;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class setting extends Fragment {

    ViewGroup viewGroup;

    Button change_range;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_setting, container, false);

        change_range = viewGroup.findViewById(R.id.change_range);

        change_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Range_change.class);
                startActivity(intent);
            }
        });

        return viewGroup;
    }
}