package com.example.sniply.Activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sniply.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ListOpenedActivity extends AppCompatActivity {


    TextView textView;
    ArrayList<File> songList;
    ListView listView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_opened);

        textView=findViewById(R.id.textView2);
        listView= findViewById(R.id.listViewSongList);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        songList = (ArrayList) bundle.getIntegerArrayList("songlist");

        runTimePermission();





    }


    //Method To Ask access for the external storeage
    public void runTimePermission() {
        Dexter.withContext(getApplicationContext())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        //Method To Display songs
                        displaySong();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        //Keeps asking for external storage permission
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    public void displaySong() {

        ArrayList<File> mySongs = songList;


        //Calling the adapter and setting it to ListView
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        //Implementing onClickListener for ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String songName = (String) listView.getItemAtPosition(i);

                //Calling the next intent and sending the Required Details to play the songs
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra("songs", mySongs);
                intent.putExtra("songname", songName);
                intent.putExtra("pos", i);
                startActivity(intent);
            }
        });
    }


    class CustomAdapter extends BaseAdapter {

        ArrayList<File> items = songList;


        @Override
        public int getCount() {
            //Returning the count of total songs in an ArrayList
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Inflating all the single music files in a Layout File
            View view = getLayoutInflater().inflate(R.layout.song_name_layout, null);
            TextView txtSong = view.findViewById(R.id.SongName);
            txtSong.setSelected(true);
            txtSong.setText(items.get(position).getName().toString().replace(".mp3", "").replace(".wav", ""));
            return view;
        }
    }

}
