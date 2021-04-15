package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Fragment {


    String[] ListElements = new String[] {
            "Android",
            "PHP"
    };
    Button createroom;
    ListView lista1vs1;
    CardView cardvs1;

    public MainActivity(){

        super(R.layout.activity_main);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main,
                container, false);

        Button play2 = view.findViewById(R.id.button_start_2vs2);
        createroom = view.findViewById(R.id.button_start_1vs1);
        Button joinroom = view.findViewById(R.id.button_join_private_room);
        lista1vs1 = (ListView) view.findViewById(R.id.lista1vs1);
        cardvs1=view.findViewById(R.id.card1vs1);
        final List< String > ListElementsArrayList = new ArrayList< String >
                (Arrays.asList(ListElements));
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


    }
    public void openActivity3(){
        createroom.setVisibility(View.INVISIBLE);
        lista1vs1.setVisibility(View.VISIBLE);
    }



}
