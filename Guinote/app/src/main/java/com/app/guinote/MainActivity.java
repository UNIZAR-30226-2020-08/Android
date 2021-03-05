package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play2 = findViewById(R.id.button_start_2vs2);
        Button createroom = findViewById(R.id.button_start_private_room);
        Button joinroom = findViewById(R.id.button_join_private_room);


        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        toolbar.setOverflowIcon(getDrawable(R.drawable.opt_icono));

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


    }

    public void openActivity3(){
        Intent intent = new Intent(this, ranking.class);
        startActivity(intent);
    }

    public void openActivity2(){
        Intent intent = new Intent(this, PantallaJuego.class);
        startActivity(intent);
    }



}