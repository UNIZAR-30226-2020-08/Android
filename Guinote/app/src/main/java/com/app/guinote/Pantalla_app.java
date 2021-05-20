package com.app.guinote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Pantalla_app extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView mBtmView;
    private int mMenuId;
    public  static String enPartidaIndividual="";
    public static Socket mSocket;
    private Context ctx;
    private SQLiteDatabase db;


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.off("invitacionRecibida",invitaPartida);
        mSocket.disconnect();
    }

    private Emitter.Listener invitaPartida = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        if(data.getString("username").equals()) {
                            Log.d("notidica", "ya");
                            Intent intent = new Intent(ctx, PantallaJuego1vs1.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, "CHANNEL_ID")
                                    .setSmallIcon(R.drawable.amigo_icon)
                                    .setContentTitle("Invitación a partida")
                                    .setContentText(data.getString("username") + " te ha invitado a la partida " + data.getString("nombre"))
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);

                            // notificationId is a unique int for each notification that you must define
                            notificationManager.notify(1, builder.build());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ctx=this;

        MyOpenHelper dbHelper = new MyOpenHelper(this);
        db = dbHelper.getWritableDatabase();

        createNotificationChannel();



        mSocket = IO.socket(URI.create("https://las10ultimas-backend-realtime.herokuapp.com"));
        mSocket.on("invitacionRecibida", invitaPartida);
        mSocket.connect();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pantalla_app);
        mBtmView = (BottomNavigationView) findViewById(R.id.navegacion_app);
        mBtmView.setOnNavigationItemSelectedListener(this);



        mBtmView.getMenu().findItem(R.id.inicio_juego).setChecked(true);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_app, MainActivity.class, null)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mMenuId = item.getItemId();
        for (int i = 0; i < mBtmView.getMenu().size(); i++) {
            MenuItem menuItem = mBtmView.getMenu().getItem(i);
            boolean isChecked = menuItem.getItemId() == item.getItemId();
            menuItem.setChecked(isChecked);
        }

        switch (item.getItemId()) {
            case R.id.ranking_juego: {

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.side_in_left,
                                R.anim.slide_out_left
                        )
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmento_app, ranking.class, null)
                        .commit();
                return true;
            }
            case R.id.inicio_juego: {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.side_in_left,
                                R.anim.slide_out_left
                        )
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmento_app, MainActivity.class, null)
                        .commit();
                return true;
            }
            case R.id.perfil_juego: {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.side_in_left,
                                R.anim.slide_out_left
                        )
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmento_app, Perfil.class, null)
                        .commit();
                return true;
            }
            case R.id.historial_juego_opt: {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.side_in_left,
                                R.anim.slide_out_left
                        )
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmento_app, HistorialPartidas.class, null)
                        .commit();
                return true;
            }
            default:
                return false;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "Canal", importance);
            channel.setDescription("description");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public String getName() {
        String query="SELECT user FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }

}