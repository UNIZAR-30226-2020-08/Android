package com.app.guinote.ActivityTorneo;

import android.app.ActionBar;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.guinote.R;
import com.app.guinote.TorneoFragment.BracketsFragment;
import com.app.guinote.TorneoApp.App1;



public class Torneo extends AppCompatActivity {


    private BracketsFragment bracketFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_torneo);

        initialiseBracketsFragment();
    }

    private void initialiseBracketsFragment() {


        bracketFragment = new BracketsFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.containerTorneo, bracketFragment, "brackets_home_fragment");
        transaction.commit();
        Log.d("hola","sdasdsd");
        manager.executePendingTransactions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setScreenSize();

    }

    private void setScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        App1.getInstance().setScreenHeight(height);
    }
}