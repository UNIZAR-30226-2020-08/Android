package com.app.guinote.TorneoViewHolder;


import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.guinote.ActivityTorneo.Torneo;
import com.app.guinote.MyOpenHelper;
import com.app.guinote.PantallaJuego;
import com.app.guinote.PantallaJuego1vs1;
import com.app.guinote.R;
import com.app.guinote.TorneoAnimation.Animation1;

public class BracketsCellViewHolder extends RecyclerView.ViewHolder {

    private TextView teamOneName;
    private TextView teamTwoName;
    private TextView teamOneScore;
    private TextView teamTwoScore;
    private TextView teamTitle;
    private Animation animation;
    private SQLiteDatabase db;
    private int participantes;

    private RelativeLayout rootLayout;

    public BracketsCellViewHolder(final View itemView) {
        super(itemView);


        teamOneName = (TextView) itemView.findViewById(R.id.team_one_name);
        teamTwoName = (TextView) itemView.findViewById(R.id.team_two_name);
        teamOneScore = (TextView) itemView.findViewById(R.id.team_one_score);
        teamTwoScore = (TextView) itemView.findViewById(R.id.team_two_score);
        teamTitle = (TextView) itemView.findViewById(R.id.team_title);
        rootLayout = (RelativeLayout) itemView.findViewById(R.id.layout_root);

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("minombre",Torneo.getName());
                if(Torneo.getName().equals(getTeamOneName().getText()) || Torneo.getName().equals(getTeamTwoName().getText())){
                    Log.d("partida",getTeamTitle().getText().toString());
                    Activity activity = (Activity)itemView.getContext();
                    if (Torneo.modalidad==1) {
                        Intent intent = new Intent(activity, PantallaJuego.class);
                        Bundle b = new Bundle();
                        b.putString("key", getTeamTitle().getText().toString()); //Your id
                        intent.putExtras(b); //Put your id to your next Intent
                        activity.startActivity(intent);
                    }else{
                        Intent intent = new Intent(activity, PantallaJuego1vs1.class);
                        Bundle b = new Bundle();
                        b.putString("key", getTeamTitle().getText().toString()); //Your id
                        intent.putExtras(b); //Put your id to your next Intent
                        activity.startActivity(intent);
                    }
                }
            }
        });

    }

    public void setAnimation(int height){
        animation = new Animation1(rootLayout, rootLayout.getHeight(),
                height);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(200);
        rootLayout.setAnimation(animation);
        rootLayout.startAnimation(animation);
    }


    public TextView getTeamTitle() {
        return teamTitle;
    }

    public TextView getTeamTwoName() {
        return teamTwoName;
    }

    public TextView getTeamOneScore() {
        return teamOneScore;
    }

    public TextView getTeamTwoScore() {
        return teamTwoScore;
    }

    public TextView getTeamOneName() {
        return teamOneName;
    }
}
