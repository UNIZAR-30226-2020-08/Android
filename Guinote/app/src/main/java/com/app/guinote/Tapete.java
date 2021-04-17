package com.app.guinote;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tapete} factory method to
 * create an instance of this fragment.
 */
public class Tapete extends Fragment {

    public Tapete() {
        super(R.layout.fragment_tapete);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tapete, container, false);
        return view;
    }
}