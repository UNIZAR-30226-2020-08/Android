package com.app.guinote;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.guinote.ActivityTorneo.Torneo;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends Fragment {


    String[] ListElements = new String[] {
            "Android",
            "PHP"
    };
    CardView createroom;
    ListView lista1vs1;
    CardView cardvs1;
    CardView fd;
    CardView jointorneo;

    private SQLiteDatabase db;

    public MainActivity(){

        super(R.layout.activity_main);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main,
                container, false);

        CardView play2 = view.findViewById(R.id.button_start_2vs2);
        createroom = view.findViewById(R.id.button_start_1vs1);
        fd = view.findViewById(R.id.button_start_private_room);
        jointorneo = view.findViewById(R.id.button_join_private_room);
        MaterialToolbar toolbar = (MaterialToolbar) view.findViewById(R.id.topAppBar);


        MyOpenHelper dbHelper = new MyOpenHelper(getContext());
        db = dbHelper.getWritableDatabase();

        //toolbar.setTitle(getName());
        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        createroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });
        jointorneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity5();
            }
        });
        fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity4();
            }
        });


        return view;

    }

    public void openActivity4(){
        Intent intent = new Intent(getContext(), Torneo.class);
        startActivity(intent);
    }


    public void openActivity2(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_app, Lista2vs2.class, null)
                .commit();
    }
    public void openActivity5(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_app, ListaTorneo.class, null)
                .commit();
    }
    public void openActivity3(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_app, Lista1vs1.class, null)
                .commit();
    }


    public String getName() {
        String query="SELECT user FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }
}
