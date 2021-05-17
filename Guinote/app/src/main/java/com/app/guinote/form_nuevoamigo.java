package com.app.guinote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link form_nuevoamigo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class form_nuevoamigo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SQLiteDatabase db;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    public form_nuevoamigo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment form_nuevoamigo.
     */
    // TODO: Rename and change types and number of parameters
    public static form_nuevoamigo newInstance(String param1, String param2) {
        form_nuevoamigo fragment = new form_nuevoamigo();
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
        view=inflater.inflate(R.layout.fragment_form_nuevoamigo, container, false);


        MyOpenHelper dbHelper = new MyOpenHelper(getContext());
        db = dbHelper.getWritableDatabase();

        Button anadir= view.findViewById(R.id.boton_anadir_amigo);

        anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validar(view)==true) {
                    botonAmigo();
                }
            }
        });
        return view;
    }

    private void botonAmigo(){


        String postUrl = "https://las10ultimas-backend.herokuapp.com/api/amigo/";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        TextInputLayout cajaNombre= (TextInputLayout) view.findViewById(R.id.cajaAmigoAnadir);
        String nombreUsername=cajaNombre.getEditText().getText().toString();
        JSONObject postData = new JSONObject();
        try {
            postData.put("usuario", getName());
            postData.put("amigo", nombreUsername);
            postData.put("aceptado", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .hide(getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmento_form_anadir))
                        .commit();
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

    public String getName() {
        String query="SELECT user FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }


    public boolean validar(View view){
        boolean validar=true;
        TextInputLayout cajaNombre= (TextInputLayout) view.findViewById(R.id.cajaAmigoAnadir);
        String nombreUsername=cajaNombre.getEditText().getText().toString();


        if(nombreUsername.isEmpty()){
            cajaNombre.setError("Escribe un nombre de usuario");
            validar=false;
        }
        else{
            cajaNombre.setError(null);
        }
        return  validar;
    }

}