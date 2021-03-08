package com.app.guinote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ranking extends AppCompatActivity {

    ListView listView;
    List<rank> lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        listView = findViewById(R.id.lv1);

        RankAdapter adapter = new RankAdapter(this,GetData());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rank  ranking = lista.get(position);
                Toast.makeText(getBaseContext(),ranking.name,Toast.LENGTH_SHORT).show();

            }
        });
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