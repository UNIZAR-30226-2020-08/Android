package com.app.guinote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ranking extends Fragment {

    ListView listView;
    List<rank> lista;
    View view;

    public ranking(){
        super(R.layout.activity_ranking);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_ranking,
                container, false);
        listView = view.findViewById(R.id.lv1);

        RankAdapter adapter = new RankAdapter(getActivity(),GetData());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rank  ranking = lista.get(position);
                Toast.makeText(getActivity(),ranking.name,Toast.LENGTH_SHORT).show();

            }
        });

        ImageButton global = view.findViewById(R.id.GlobalBotton);
        ImageButton amigos = view.findViewById(R.id.AmigosBotton);

       global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
       amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        return view;
    }

    public void openActivity(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.side_in_left,
                        R.anim.slide_out_left
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_ranking,ranking2.class, null)
                .commit();
    }

    public void openActivity2(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.side_in_left,
                        R.anim.slide_out_left
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_ranking,ListRanking.class, null)
                .commit();
    }

    private List<rank> GetData() {
        lista = new ArrayList<>();
        lista.add(new rank(1,"FERNANDO07",R.drawable.asoros,"150"));
        lista.add(new rank(2,"DIEGOL10",R.drawable.dosoros,"140"));
        lista.add(new rank(3,"JAMONERO",R.drawable.tresoros,"130"));
        lista.add(new rank(4,"DRESPIN",R.drawable.cuatrooros,"120"));

        return lista;


    }
}