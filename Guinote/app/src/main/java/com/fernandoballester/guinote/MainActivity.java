package com.fernandoballester.guinote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView myImageLogo = (ImageView) findViewById(R.id.imagen_logo);
        myImageLogo.setImageResource(R.drawable.logo);
    }
}