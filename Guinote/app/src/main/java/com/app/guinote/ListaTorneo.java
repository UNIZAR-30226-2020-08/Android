package com.app.guinote;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.app.guinote.ActivityTorneo.Torneo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ListaTorneo extends Fragment {

    View view;

    ListView listView16_2;
    ListView listView8_2;
    ListView listView16_1;
    ListView listView8_1;


    public ListaTorneo(){
        super(R.layout.activity_lista_torneo);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_lista_torneo,
                container, false);



        CardView card81 = view.findViewById(R.id.torneo8_individual);
        CardView card82= view.findViewById(R.id.torneo8_parejas);
        CardView card161 = view.findViewById(R.id.torneo16_individual);
        CardView card162 = view.findViewById(R.id.torneo16_parejas);

        card81.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData8_1();
            }
        });

        card82.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData8_2();
            }
        });

        card161.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData16_1();
            }
        });

        card162.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData16_2();
            }
        });



        return view;
    }

    private void GetData8_2() {




        final ArrayList<TipoLista> lista8_2 = new ArrayList<TipoLista>();
        final Customadapterlista adapter8_2 = new Customadapterlista(getContext(), lista8_2);

        String url = "https://las10ultimas-backend.herokuapp.com/api/torneo/findAllTournament/1/8";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray contenido = response;
                    for (int i=0;i<contenido.length();i++){
                        JSONObject objeto=contenido.getJSONObject(i);
                        String nombre=objeto.getString("nombre");
                        Integer puntuacion=objeto.getInt("jugadores_online");
                        Log.d("msg82",nombre);
                        lista8_2.add(new TipoLista(nombre,puntuacion.toString()+"/16"));
                    }



                    final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
                    builder.setTitle("Partidas");

                    builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setSingleChoiceItems(adapter8_2,1,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("cual",lista8_2.get(which).get_subjectName());
                            Intent intent = new Intent(getContext(), Torneo.class);
                            Bundle b = new Bundle();
                            b.putString("key", lista8_2.get(which).get_subjectName()); //Your id
                            intent.putExtras(b); //Put your id to your next Intent
                            startActivity(intent);
                            dialog.dismiss();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(
                                            R.anim.side_in_left,
                                            R.anim.slide_out_left
                                    )
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragmento_app, MainActivity.class, null)
                                    .commit();
                        }
                    });



                    builder.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void GetData16_2() {
        final ArrayList<TipoLista> lista16_2 = new ArrayList<TipoLista>();
        final Customadapterlista adapter16_2 = new Customadapterlista(getContext(), lista16_2);



        String url = "https://las10ultimas-backend.herokuapp.com/api/torneo/findAllTournament/1/16";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray contenido = response;
                    for (int i=0;i<contenido.length();i++){
                        JSONObject objeto=contenido.getJSONObject(i);
                        String nombre=objeto.getString("nombre");
                        Integer puntuacion=objeto.getInt("jugadores_online");
                        Log.d("msg162",nombre);
                        lista16_2.add(new TipoLista(nombre,puntuacion.toString()+"/32"));
                    }
                    final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
                    builder.setTitle("Partidas");

                    builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setSingleChoiceItems(adapter16_2,1,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("cual",lista16_2.get(which).get_subjectName());
                            Intent intent = new Intent(getContext(), Torneo.class);
                            Bundle b = new Bundle();
                            b.putString("key", lista16_2.get(which).get_subjectName()); //Your id
                            intent.putExtras(b); //Put your id to your next Intent
                            startActivity(intent);
                            dialog.dismiss();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(
                                            R.anim.side_in_left,
                                            R.anim.slide_out_left
                                    )
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragmento_app, MainActivity.class, null)
                                    .commit();
                        }
                    });



                    builder.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void GetData8_1() {
        final ArrayList<TipoLista> lista8_1 = new ArrayList<TipoLista>();
        final Customadapterlista adapter8_1 = new Customadapterlista(getContext(), lista8_1);


        Log.d("ddidi1","didi");
        String url = "https://las10ultimas-backend.herokuapp.com/api/torneo/findAllTournament/0/8";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.d("ddidi",response.toString());
                    JSONArray contenido = response;
                    for (int i=0;i<contenido.length();i++){
                        JSONObject objeto=contenido.getJSONObject(i);
                        String nombre=objeto.getString("nombre");
                        Integer puntuacion=objeto.getInt("jugadores_online");
                        Log.d("msg81",nombre);
                        lista8_1.add(new TipoLista(nombre,puntuacion.toString()+"/8"));
                    }
                    final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
                    builder.setTitle("Partidas");

                    builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setSingleChoiceItems(adapter8_1,1,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("cual",lista8_1.get(which).get_subjectName());
                            Intent intent = new Intent(getContext(), Torneo.class);
                            Bundle b = new Bundle();
                            b.putString("key", lista8_1.get(which).get_subjectName()); //Your id
                            intent.putExtras(b); //Put your id to your next Intent
                            startActivity(intent);
                            dialog.dismiss();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(
                                            R.anim.side_in_left,
                                            R.anim.slide_out_left
                                    )
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragmento_app, MainActivity.class, null)
                                    .commit();
                        }
                    });



                    builder.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void GetData16_1() {
        final ArrayList<TipoLista> lista16_1 = new ArrayList<TipoLista>();
        final Customadapterlista adapter16_1 = new Customadapterlista(getContext(), lista16_1);


        String url = "https://las10ultimas-backend.herokuapp.com/api/torneo/findAllTournament/0/16";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray contenido = response;
                    for (int i=0;i<contenido.length();i++){
                        JSONObject objeto=contenido.getJSONObject(i);
                        String nombre=objeto.getString("nombre");
                        Integer puntuacion=objeto.getInt("jugadores_online");
                        Log.d("msg161",nombre);
                        lista16_1.add(new TipoLista(nombre,puntuacion.toString()+"/16"));
                    }
                    final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
                    builder.setTitle("Partidas");

                    builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setSingleChoiceItems(adapter16_1,1,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("cual",lista16_1.get(which).get_subjectName());
                            Intent intent = new Intent(getContext(), Torneo.class);
                            Bundle b = new Bundle();
                            b.putString("key", lista16_1.get(which).get_subjectName()); //Your id
                            intent.putExtras(b); //Put your id to your next Intent
                            startActivity(intent);
                            dialog.dismiss();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(
                                            R.anim.side_in_left,
                                            R.anim.slide_out_left
                                    )
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragmento_app, MainActivity.class, null)
                                    .commit();
                        }
                    });



                    builder.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}