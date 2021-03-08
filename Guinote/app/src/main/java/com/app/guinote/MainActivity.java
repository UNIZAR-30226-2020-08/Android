package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends Fragment {

    public MainActivity(){

        super(R.layout.activity_main);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main,
                container, false);

        Button play2 = view.findViewById(R.id.button_start_2vs2);
        Button createroom = view.findViewById(R.id.button_start_private_room);
        Button joinroom = view.findViewById(R.id.button_join_private_room);


        MaterialToolbar toolbar = (MaterialToolbar) view.findViewById(R.id.topAppBar);
        toolbar.setOverflowIcon(ResourcesCompat.getDrawable(requireActivity().getResources(),R.drawable.opt_icono,null));

        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        createroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });

        return view;

    }


    public void openActivity2(){
        Intent intent = new Intent(getActivity(), PantallaJuego.class);
        startActivity(intent);
    }
    public void openActivity3(){
        Intent intent = new Intent(getActivity(), ranking.class);
        startActivity(intent);
    }



}
