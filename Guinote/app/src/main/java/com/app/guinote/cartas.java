package com.app.guinote;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cartas} factory method to
 * create an instance of this fragment.
 */
public class cartas extends Fragment {


    public cartas() {
        super(R.layout.activity_login);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cartas, container, false);
        return view;
    }
}