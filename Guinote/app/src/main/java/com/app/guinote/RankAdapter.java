package com.app.guinote;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;

public class RankAdapter extends BaseAdapter {

    Context contex;
    List<rank> lista;
    CardView cartaview;

    public RankAdapter(Context contex, List<rank> lista) {
        this.contex = contex;
        this.lista = lista;
    }


    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView ImageViewRank;
        TextView TextViewName;
        TextView TextViewPuntuacion;
        rank ranking = lista.get(position);

        if( convertView == null){
            convertView = LayoutInflater.from(contex).inflate(R.layout.rank,null);
        }
        cartaview = (CardView) convertView.findViewById(R.id.cardviewid);
        cartaview.setVisibility(View.VISIBLE);
        cartaview.setBackgroundColor(Color.TRANSPARENT);
        ImageViewRank= convertView.findViewById(R.id.imagerank);
        TextViewName = convertView.findViewById(R.id.textView1);
        TextViewPuntuacion= convertView.findViewById(R.id.textView2);

        ImageViewRank.setImageResource(ranking.imagen);
        TextViewName.setText(ranking.name);
        TextViewPuntuacion.setText(ranking.puntuacion);


        return convertView;
    }
}
