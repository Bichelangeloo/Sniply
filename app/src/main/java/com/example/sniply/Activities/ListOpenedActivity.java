package com.example.sniply.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sniply.MainActivity;
import com.example.sniply.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ListOpenedActivity extends AppCompatActivity {


    TextView textView;
    ArrayList<File> songList;
    ListView listView;
    Button odstranit;
    ArrayList<File> songs;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_opened);
        odstranit = findViewById(R.id.odstranit);
        textView=findViewById(R.id.nazovListu);
        listView= findViewById(R.id.listViewSongList);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        SharedPreferences sh = getSharedPreferences("SniplySharedPreferences",MODE_PRIVATE);

        songList = (ArrayList) bundle.getIntegerArrayList("songlist");
        songs= (ArrayList) bundle.getIntegerArrayList("allsongs");
        String listName = bundle.getString("listname");
        textView.setText(listName);


        if (listName.equals("Najprehravanejsie")){
            try {
                songList.clear();
                LinkedHashMap<File,Integer> pocetPrehrani = new LinkedHashMap<>();

                for (File file:songs) {

                    pocetPrehrani.put(file,sh.getInt(file.getName(),0));



                }

                List<Map.Entry<File, Integer>> entries =
                        new ArrayList<Map.Entry<File, Integer>>(pocetPrehrani.entrySet());
                Collections.sort(entries, new Comparator<Map.Entry<File, Integer>>() {
                    public int compare(Map.Entry<File, Integer> a, Map.Entry<File, Integer> b){
                        return a.getValue().compareTo(b.getValue());
                    }
                });

                for (int i = entries.size()-1; i >= entries.size()-10; i--) {


                    System.out.println(entries.get(i).getKey().toString()+"    SOOOOOOONGGGGGGGG NAAAAAAAMEEEEEE NAJPREHRAVANEJSIE"+entries.get(i).getValue().toString());
                    songList.add((entries.get(i).getKey()));


                }




                System.out.println(pocetPrehrani.keySet());



            }catch (Exception e){

                System.out.println("Nastala chyba pocetPrehrani");
            }



        }

        runTimePermission();







        odstranit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),ListActivity.class);
                intent.putExtra("listToDelete",listName);

                startActivity(intent);
                finish();


            }
        });




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
