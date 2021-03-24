package com.app.guinote;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Timer;
import java.util.TimerTask;

public class carga_registro extends AppCompatActivity {

    public carga_registro() {
        super(R.layout.animacion_carga);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LottieAnimationView animacion = (LottieAnimationView) findViewById(R.id.animation_carga_reg);
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                //animacion.cancelAnimation();
            }

            public void onFinish() {
                animacion.pauseAnimation();
                animacion.setVisibility(View.INVISIBLE);
                finish();
            }
        }.start();
    }

}