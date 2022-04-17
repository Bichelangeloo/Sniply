package com.example.sniply.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sniply.R;

public class ListActivity extends AppCompatActivity {


    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        listView.findViewById(R.id.listView);


    }









    class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return 0;
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
        public View getView(int i, View convertView, ViewGroup parent) {
            //Inflating all the single music files in a Layout File
            View view = getLayoutInflater().inflate(R.layout.song_list_layout, null);
            TextView txtList = view.findViewById(R.id.ListName);

            return view;
        }
    }


}



