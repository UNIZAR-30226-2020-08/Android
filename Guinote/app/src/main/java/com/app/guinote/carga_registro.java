package com.app.guinote;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
        Bundle b=getIntent().getExtras();
        String name=b.getString("username");
        String email=b.getString("email");
        String password=b.getString("passwd");

        String postUrl = "http://192.168.1.33:8080/api/auth/signup";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("username", name);
            postData.put("email", email);
            postData.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
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