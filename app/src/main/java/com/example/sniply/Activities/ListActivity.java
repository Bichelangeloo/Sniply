package com.example.sniply.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sniply.R;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ListActivity extends AppCompatActivity {


     ListView listView;
     Button newSongListButton;
     Button createNewSongListButton;
     EditText newSongListName;
     SharedPreferences sharedPreferences;




    ArrayList<File> songs = findSong(Environment.getExternalStorageDirectory());
    ArrayList<File> tmp = new ArrayList<>();
   public static LinkedHashMap<String,ArrayList<File>> songList = new LinkedHashMap<>();
   ArrayList<File> favouritess = new ArrayList<>();




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        listView= findViewById(R.id.simpleListView);
        newSongListButton = findViewById(R.id.newSongList);


        newSongListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemDialog(ListActivity.this);
            }
        });




        SharedPreferences sh = getSharedPreferences("SniplySharedPreferences",MODE_PRIVATE);


        songList.put("favourites",favouritess);



        Set keySet = sh.getStringSet("keySet",null);

        System.out.println("KEYSET:   "+keySet);
try {
    for (Object key: keySet) {

        Set tmp = sh.getStringSet(key.toString(),null);
        ArrayList<File> tmp2 = new ArrayList<>();

        System.out.println(" FIIIIIIIIIILEEEEEEEEEEEEES   "+sh.getStringSet(key.toString(),null));
        for (Object file:tmp) {

            Uri uri = Uri.parse(file.toString());
            tmp2.add(new File(uri.getPath()));

        }

        songList.put(key.toString(),tmp2);


    }
}catch (Exception e){

    System.out.println("0 songov");

}



        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), songList);
        listView.setAdapter(customAdapter);






        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),ListOpenedActivity.class);
                intent.putExtra("songlist",songList.get(listView.getItemAtPosition(i).toString()));
                startActivity(intent);


            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


try {
    Uri uriFav = (Uri) bundle.get("songFav");
    songList.get("favourites").add(new File(uriFav.getPath()));
}catch (Exception e){

    System.out.println("0 favoritov");


}














        try {
            String keyAddToListActiv = bundle.getString("key");
            Uri uriAddToListActiv = (Uri) bundle.get("song");
            System.out.println("kluuuuuuuuuuuc "+ keyAddToListActiv);
            songList.get(keyAddToListActiv).add(new File(uriAddToListActiv.getPath()));
        }catch (Exception e){

            System.out.println(e);

        }




    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sh = getSharedPreferences("SniplySharedPreferences",MODE_PRIVATE);



        Set keySet = sh.getStringSet("keySet",null);

        System.out.println("KEYSET:   "+keySet);
try {
    for (Object key: keySet) {

        System.out.println(" FIIIIIIIIIILEEEEEEEEEEEEES   "+sh.getStringSet(key.toString(),null));

    }
}catch (Exception e){

    System.out.println("0 songov");


}




    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sh = getSharedPreferences("SniplySharedPreferences",MODE_PRIVATE);
        SharedPreferences.Editor myEdit= sh.edit();

        Set keySet = songList.keySet();
        myEdit.putStringSet("keySet",keySet);

        System.out.println(keySet);



        for (String key: songList.keySet()) {
            TreeSet tmp = new TreeSet();


            for (File file:songList.get(key)) {

                System.out.println(file.getName());

                tmp.add(file.toString());

            }
            myEdit.putStringSet(key,tmp);
            tmp.clear();

            
        }

        myEdit.apply();
    }

    public void createSongList(String nazov){


    songList.put(nazov,tmp);

}


    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Pridaj novy zoznam skladieb")
                .setMessage("Ako sa mam zoznam volat?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nazov = String.valueOf(taskEditText.getText());
                        createSongList(nazov);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

//private void popUp(){
//
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        View popupView = inflater.inflate(R.layout.popup_songlist,null);
//
//    // create the popup window
//    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
//    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//    boolean focusable = true; // lets taps outside the popup also dismiss it
//    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//
//    // show the popup window
//    // which view you pass in doesn't matter, it is only used for the window tolken
//    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
//
//
//
//    // dismiss the popup window when touched
//    popupView.setOnTouchListener(new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            popupWindow.dismiss();
//            return true;
//        }
//    });
//
//
//
//}

  //  private void popUp(){
  //      PopupMenu p = new PopupMenu(ListActivity.this, newSongListButton);
  //      p.getMenuInflater().inflate(R.menu.popup_menu_songlist,p.getMenu());
  //      p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
  //          @Override
  //          public boolean onMenuItemClick(MenuItem menuItem) {
  //              Toast.makeText(ListActivity.this,menuItem.getTitle(),Toast.LENGTH_SHORT).show();
  //
  //
  //
  //              return true;
  //          }
  //      });
  //      p.show();
  //
  //
  //
  //  }

    public ArrayList<File> findSong(File file) {

        //ArrayList to store all songs
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
try {
    for (File singleFile : files) {

        //Adding the directory to arrayList if it is not hidden
        if (singleFile.isDirectory() && !singleFile.isHidden()) {

            arrayList.addAll(findSong(singleFile));

        } else {
            //Adding the single music file to ArrayList
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

    public class CustomAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        LinkedHashMap<String,ArrayList<File>> songList = new LinkedHashMap<>();

        public CustomAdapter(Context applicationContext, LinkedHashMap<String,ArrayList<File>> songList) {
            this.context = context;
            this.songList = songList;

            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return songList.size();
        }

        @Override
        public Object getItem(int i) {

            Set<String> keys = songList.keySet();
            Object[] keysArray = keys.toArray();

            return keysArray[i].toString();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.song_list_layout, null);
            TextView nameOfList = (TextView) view.findViewById(R.id.listName);
            Set<String> keys = songList.keySet();
            Object[] keysArray = keys.toArray();

            nameOfList.setText(keysArray[i].toString());



            return view;
        }
    }
}



//class SongList{
//
//    String nameOfList;
//    ArrayList<File> songs;
//
//
//    public SongList(String nameOfList, ArrayList<File> songs) {
//        this.nameOfList = nameOfList;
//        this.songs = songs;
//    }
//
//    public String getNameOfList() {
//        return nameOfList;
//    }
//
//    public void setNameOfList(String nameOfList) {
//        this.nameOfList = nameOfList;
//    }
//
//    public ArrayList<File> getSongs() {
//        return songs;
//    }
//
//    public void setSongs(ArrayList<File> songs) {
//        this.songs = songs;
//    }
//}

//public View getView(int position, View convertView, ViewGroup parent) {
//
//            //Inflating all the single music files in a Layout File
//            View view = getLayoutInflater().inflate(R.layout.song_name_layout, null);
//            TextView txtSong = view.findViewById(R.id.SongName);
//            txtSong.setSelected(true);
//            txtSong.setText(items[position]);
//            return view;
//        }














