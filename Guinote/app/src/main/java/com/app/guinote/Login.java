package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Login extends Fragment {
    private static long mLastClickTime = 0;
    public Login(){
        super(R.layout.activity_login);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_login,
                container, false);
        final LottieAnimationView animacion = (LottieAnimationView) getActivity().findViewById(R.id.animation_carga_reg2);
        final RelativeLayout cuerpo = (RelativeLayout) getActivity().findViewById(R.id.pantalla_prin);
        final TextInputEditText nombre= (TextInputEditText) view.findViewById(R.id.username);
        final TextInputEditText passwd= (TextInputEditText) view.findViewById(R.id.passwd);
        final TextInputLayout username= (TextInputLayout) view.findViewById(R.id.usernameLogin);
        final TextInputLayout contra= (TextInputLayout) view.findViewById(R.id.passwdLogin);
        Button loggear = (Button) view.findViewById(R.id.login_boton);
        loggear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String nombreUsername=username.getEditText().getText().toString();
                String nombrePasswd=contra.getEditText().getText().toString();
                nombre.setFocusable(false);
                passwd.setFocusable(false);
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                cuerpo.getBackground().setAlpha(20);
                animacion.setVisibility(View.VISIBLE);
                animacion.playAnimation();

                MyOpenHelper dbHelper = new MyOpenHelper(getContext());
                final SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM auth");
                String url = "http://192.168.1.36:8080/api/auth/signin";
                final List<String> jsonResponses = new ArrayList<>();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("username", nombreUsername);
                    postData.put("password", nombrePasswd);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject contenido = response.getJSONObject("data");
                            String jsonArray = response.getString("accessToken");

                            Log.d("msg",jsonArray.toString());

                            if (jsonArray != null){
                                db.execSQL("INSERT INTO auth (user,copas,f_carta,f_tapete,token) VALUES "+
                                        "('"+contenido.getString("username")+"','"+contenido.getString("copas")+"','"+
                                        contenido.getString("f_carta")+"','"+contenido.getString("f_tapete")+"','"+
                                        jsonArray+"')");
                                new CountDownTimer(2000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                        //animacion.cancelAnimation();
                                    }

                                    public void onFinish() {
                                        animacion.pauseAnimation();
                                        animacion.setVisibility(View.INVISIBLE);
                                        cuerpo.getBackground().setAlpha(255);
                                        Intent intent = new Intent(v.getContext(),Pantalla_app.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("EXIT",true);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition( R.anim.side_in_left, R.anim.slide_out_left);
                                    }
                                }.start();
                            }else{
                                animacion.pauseAnimation();
                                animacion.setVisibility(View.INVISIBLE);
                                cuerpo.getBackground().setAlpha(0);
                            }


                        } catch (JSONException e) {
                            animacion.pauseAnimation();
                            animacion.setVisibility(View.INVISIBLE);
                            cuerpo.getBackground().setAlpha(0);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        animacion.pauseAnimation();
                        animacion.setVisibility(View.INVISIBLE);
                        cuerpo.getBackground().setAlpha(255);
                        nombre.setFocusableInTouchMode(true);
                        passwd.setFocusableInTouchMode(true);
                        error.printStackTrace();
                    }
                });

                requestQueue.add(jsonObjectRequest);
            }
        });
        return view;
    }
}