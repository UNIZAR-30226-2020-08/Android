package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play2 = findViewById(R.id.button_start_2vs2);
        Button createroom = findViewById(R.id.button_start_private_room);
        Button joinroom = findViewById(R.id.button_join_private_room);

        ScrollView mScrollView=(ScrollView) findViewById(R.id.deslizar);
        mScrollView.setSmoothScrollingEnabled(true);

        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });


    }



    public void openActivity2(){
        Intent intent = new Intent(this, PantallaJuego.class);
        startActivity(intent);
    }



}