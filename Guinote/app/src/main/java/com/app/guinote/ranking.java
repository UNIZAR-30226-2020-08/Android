package com.app.guinote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ranking extends Fragment {

    ListView listView;
    List<rank> lista;
    ImageButton global;
    ImageButton amigos;
    View view;

    public ranking(){
        super(R.layout.activity_ranking);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_ranking,
                container, false);



        global = view.findViewById(R.id.GlobalBotton);
        amigos = view.findViewById(R.id.AmigosBotton);

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.side_in_left,
                        R.anim.slide_out_left
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_ranking,ListRanking.class, null)
                .commit();

        amigos.setBackgroundColor(Color.parseColor("#02590A"));
        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
        amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        return view;
    }

    public void openActivity(){
        amigos.setBackgroundColor(Color.parseColor("#00000000"));
        global.setBackgroundColor(Color.parseColor("#02590A"));
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.side_in_left,
                        R.anim.slide_out_left
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_ranking,ranking2.class, null)
                .commit();
    }

    public void openActivity2(){
        global.setBackgroundColor(Color.parseColor("#00000000"));
        amigos.setBackgroundColor(Color.parseColor("#02590A"));
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.side_in_left,
                        R.anim.slide_out_left
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_ranking,ListRanking.class, null)
                .commit();
    }
}