package com.app.guinote;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListRanking} factory method to
 * create an instance of this fragment.
 *
 */
public class ListRanking extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ListView listView;
    List<rank> lista;


    public ListRanking(){
        super(R.layout.fragment_list_ranking);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_ranking,
                container, false);
        listView = view.findViewById(R.id.lista_amigos);

        RankAdapter adapter = new RankAdapter(getActivity(),GetData());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rank  ranking = lista.get(position);
                Toast.makeText(getActivity(),ranking.name,Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }


    private List<rank> GetData() {
        lista = new ArrayList<>();
        lista.add(new rank(1,"FERNANDO08",R.drawable.asoros,"150"));
        lista.add(new rank(2,"DIEGOL10",R.drawable.dosoros,"140"));
        lista.add(new rank(3,"JAMONERO",R.drawable.tresoros,"130"));
        lista.add(new rank(4,"DRESPIN",R.drawable.cuatrooros,"120"));

        return lista;


    }
}