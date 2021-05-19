package com.app.guinote;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link anadir_partida#newInstance} factory method to
 * create an instance of this fragment.
 */
public class anadir_partida extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Integer cual;

    public anadir_partida() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment anadir_partida.
     */
    // TODO: Rename and change types and number of parameters
    public static anadir_partida newInstance(String param1, String param2) {
        anadir_partida fragment = new anadir_partida();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view =inflater.inflate(R.layout.fragment_anadir_partida, container, false);
        Button creacion= view.findViewById(R.id.boton_anadir);


        cual = getArguments().getInt("tipoPartida");
        Log.d("cual",cual.toString());
        creacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String postUrl = "https://las10ultimas-backend.herokuapp.com/api/partida/";
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                TextInputLayout cajaNombre= (TextInputLayout) view.findViewById(R.id.cajaPartidaAnadir);
                String nombreUsername=cajaNombre.getEditText().getText().toString();
                JSONObject postData = new JSONObject();
                try {
                    postData.put("nombre", nombreUsername);
                    postData.put("triunfo", "");
                    postData.put("estado", 0);
                    postData.put("tipo", cual);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(cual == 0){
                                String ranking=response.getString("nombre");
                                Intent intent = new Intent(getActivity(),PantallaJuego1vs1.class);

                                Bundle b = new Bundle();
                                b.putString("key", ranking); //Your id
                                b.putInt("torneo", 0);
                                intent.putExtras(b); //Put your id to your next Intent
                                startActivity(intent);
                            }else{

                                String ranking=response.getString("nombre");
                                Intent intent = new Intent(getActivity(),PantallaJuego.class);

                                Bundle b = new Bundle();
                                b.putString("key", ranking);
                                b.putInt("torneo", 0);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                            getView().setVisibility(View.GONE);
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                requestQueue.add(jsonObjectRequest);
            }
        });
        return view;
    }
}