package com.example.sniply;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.sniply.Activities.ListActivity;
import com.example.sniply.Activities.PlayerActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    String[] items;
    Button listButton;
    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        //pridelenie adresy android materialov
        listView = (ListView) findViewById(R.id.ListView);
        listButton = findViewById(R.id.listButton);

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSongList();
            }
        });


        exitButton = findViewById(R.id.exitButton);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });



        //volanie metody
        runTimePermission();


    }

    private void openSongList(){

        Intent songListIntent = new Intent(this, ListActivity.class);
        startActivity(songListIntent);

    }


    //metoda na poziadanie pristupu k externemu ulozisku
    public void runTimePermission() {
        Dexter.withContext(getApplicationContext())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        //metoda na ukazovanie pesniciek
                        displaySong();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        //stale ziada pristup k externemu ulozisku
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }




    public ArrayList<File> findSong(File file) {

        //ArrayList na ulozenie vsetkych pesniciek
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

//gfasgasg
try {
    for (File singleFile : files) {

        //pridanie priecunku do arrayList ak nieje skryty
        if (singleFile.isDirectory() && !singleFile.isHidden()) {

            arrayList.addAll(findSong(singleFile));

        } else {
            //pridanie samostatnej pesnicky do arrayList
            if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                arrayList.add(singleFile);
            }
        }
    }
}catch (Exception e){

    System.out.println("0 songov");


}


        return arrayList;
    }

    public void displaySong() {

        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];

        //pridanie vsetkych pesniciek bez koncoviek do arrayList
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }

        // volanie adaptera a setujeme ho na ListView
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        //implementacia onClickListener pre ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String songName = (String) listView.getItemAtPosition(i);

                //volanie dalsieho intentu a posielanie vyzadovanych detailov na hranie pesniciek
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra("songs", mySongs);
                intent.putExtra("songname", songName);
                intent.putExtra("pos", i);
                startActivity(intent);
            }
        });
    }


    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            //vracia sa pocet pesniciek v ArrayList
            return items.length;
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


            View view = getLayoutInflater().inflate(R.layout.song_name_layout, null);
            TextView txtSong = view.findViewById(R.id.SongName);
            txtSong.setSelected(true);
            txtSong.setText(items[position]);
            return view;
        }
    }

}