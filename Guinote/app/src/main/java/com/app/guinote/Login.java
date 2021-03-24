package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;

public class Login extends Fragment {
    private static long mLastClickTime = 0;
    public Login(){
        super(R.layout.activity_login);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login,
                container, false);
        final LottieAnimationView animacion = (LottieAnimationView) getActivity().findViewById(R.id.animation_carga_reg2);
        final RelativeLayout cuerpo = (RelativeLayout) getActivity().findViewById(R.id.pantalla_prin);
        Button loggear = (Button) view.findViewById(R.id.login_boton);
        loggear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                cuerpo.getBackground().setAlpha(20);
                animacion.setVisibility(View.VISIBLE);
                animacion.playAnimation();
                new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        //animacion.cancelAnimation();
                    }

                    public void onFinish() {
                        animacion.pauseAnimation();
                        animacion.setVisibility(View.INVISIBLE);
                        cuerpo.getBackground().setAlpha(255);
                        Intent intent = new Intent(v.getContext(),Pantalla_app.class);
                        startActivity(intent);
                    }
                }.start();
            }
        });
        return view;
    }
}