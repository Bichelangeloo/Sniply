package com.example.sniply.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sniply.R;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class addToListActivity extends AppCompatActivity {



    ListView listViewPopUp;
    ArrayList<String> listOfNames;
    SharedPreferences sp;
    Set keyset;
    Uri uri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_songlist);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        listViewPopUp = findViewById(R.id.listViewPopUp);



        sp= getSharedPreferences("SniplySharedPreferences",MODE_PRIVATE);
        keyset = sp.getStringSet("keySet",null);
        listOfNames = new ArrayList<>();
        uri = Uri.parse(bundle.getString("song"));
        for (Object key:keyset) {
            System.out.println(key.toString());
            listOfNames.add(key.toString());


        }


        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),listOfNames);
        listViewPopUp.setAdapter(customAdapter);


        listViewPopUp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),ListActivity.class);
                TextView textView = (TextView) view.findViewById(R.id.listName);


                String key = (String) textView.getText();
                System.out.println("ADD TO LIST ACTIVITY   "+ key);
                intent.putExtra("song",uri);
                intent.putExtra("key",key);
                startActivity(intent);
                finish();


            }
        });



    }


    public class CustomAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        ArrayList<String> keyset;

        public CustomAdapter(Context applicationContext, ArrayList<String> songList) {
            this.context = context;
            this.keyset = songList;

            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return keyset.size();
        }

        @Override
        public Object getItem(int i) {

            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.song_list_layout, null);
            TextView nameOfList = (TextView) view.findViewById(R.id.listName);
            nameOfList.setText(keyset.get(i));




            return view;
        }
    }





}
