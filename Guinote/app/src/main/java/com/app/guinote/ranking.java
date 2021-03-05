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

public class ranking extends AppCompatActivity {

    ListView listView;
    String mTitle[] = {"Diego", "Fernando", "Andres", "angel", "zzz"};
    String mDescription[] = {"154", "150", "145", "140", "125"};
    int images[] = {R.drawable.asoros, R.drawable.dosoros, R.drawable.tresoros, R.drawable.cuatrooros, R.drawable.cincooros};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView= findViewById(R.id.lv1);

        MyAdapter adapter = new MyAdapter(this, mTitle, mDescription, images);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==  0) {
                    Toast.makeText(ranking.this,mDescription[0], Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(ranking.this, mDescription[1], Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(ranking.this, mDescription[2], Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(ranking.this, mDescription[3], Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(ranking.this, mDescription[4], Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rDescription[];
        int rImgs[];

        MyAdapter (Context c, String title[], String description[], int imgs[]) {
            super(c, R.layout.rank, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.rank, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription = row.findViewById(R.id.textView2);

            // now set our resources on views
            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);




            return row;
        }
    }
}