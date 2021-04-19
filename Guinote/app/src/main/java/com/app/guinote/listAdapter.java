package com.app.guinote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class listAdapter extends BaseAdapter {

    Context context;
    List<listItem> lista;

    public listAdapter(Context context, List<listItem> lista) {
        this.context = context;
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
        TextView participantes;
        TextView TextViewPuntuacion;
        listItem item = lista.get(position);

        if( convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem,null);
        }
        ImageViewRank= convertView.findViewById(R.id.image1vs1);
        TextViewName = convertView.findViewById(R.id.namePartida1vs1);
        participantes = convertView.findViewById(R.id.participantes1vs1);

        ImageViewRank.setImageResource(item.imagen);
        TextViewName.setText(item.name);
        participantes.setText(item.participantes);

        return convertView;
    }
}
