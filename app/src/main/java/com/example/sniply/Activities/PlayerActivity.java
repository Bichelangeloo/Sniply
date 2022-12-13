package com.example.sniply.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bullhead.equalizer.DialogEqualizerFragment;
import com.example.sniply.R;
import com.ringdroid.RingdroidEditActivity;
import com.ringdroid.RingdroidSelectActivity;

import org.xmlpull.v1.XmlPullParser;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class PlayerActivity extends AppCompatActivity {

    Button btnPlay, btnNext, btnPrevious, moznosti,favourites,equalizer,snip;
    ImageButton btnFastForward,btnFastBackWard;
    TextView txtSongName, txtSongStart, txtSongEnd;
    SeekBar seekMusicBar;
    ImageView imageView;
    ListView listView;
    TextView listName;
    String songName;
    public static final String EXTRA_NAME = "song_name";
    public static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    LinkedHashMap<String,ArrayList<File>> songList;
    Thread updateSeekBar;
    Uri uri;
    int pocetPrehrani=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //Priradenie adresy Android Materials
        btnPlay = (Button) findViewById(R.id.BtnPlay);
        btnNext = (Button) findViewById(R.id.BtnNext);
        btnPrevious = (Button) findViewById(R.id.BtnPrevious);
        btnFastForward = (ImageButton) findViewById(R.id.BtnFastForward);
        btnFastBackWard = (ImageButton) findViewById(R.id.BtnFastRewind);
        moznosti= findViewById(R.id.moznosti);
        favourites=findViewById(R.id.favourite);
        equalizer =findViewById(R.id.equalizerButton);
        snip= findViewById(R.id.snipButton);
        songList = ListActivity.songList;
        listName = findViewById(R.id.listName);
        listView = findViewById(R.id.listViewPopUp);
        txtSongName = (TextView) findViewById(R.id.SongTxt);
        txtSongStart = (TextView) findViewById(R.id.TxtSongStart);
        txtSongEnd = (TextView) findViewById(R.id.TxtSongEnd);
        seekMusicBar = (SeekBar) findViewById(R.id.SeekBar);
        imageView = (ImageView) findViewById(R.id.MusicImage);
        SharedPreferences sh = getSharedPreferences("SniplySharedPreferences",MODE_PRIVATE);
        SharedPreferences.Editor myEdit= sh.edit();





        //kontrola ci sa da pesnicka prehrat
        if (mediaPlayer != null) {

            //ak neni pesnicka v mediaplayeri ta sa zapne
            mediaPlayer.start();
            mediaPlayer.release();
        }

        //ziskanie detailov minuleho intentu
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mySongs = (ArrayList) bundle.getIntegerArrayList("songs");
        String sName = intent.getStringExtra("songname");


        position = bundle.getInt("pos");
        txtSongName.setSelected(true);

        //ziskanie nazvov z arraylistu
        uri = Uri.parse(mySongs.get(position).toString());
        songName = mySongs.get(position).getName();
        txtSongName.setText(songName);

        //posunutie cesty mediaplayeru
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        //metoda na ziskanie endtime
        songEndTime();

        //toto aktualizuje playbar kym hraje songa
        updateSeekBar = new Thread() {
            @Override
            public void run() {

                int TotalDuration = mediaPlayer.getDuration();
                int CurrentPosition = 0;

                while (CurrentPosition < TotalDuration) {
                    try {

                        sleep(500);
                        CurrentPosition = mediaPlayer.getCurrentPosition();
                        seekMusicBar.setProgress(CurrentPosition);

                    } catch (InterruptedException | IllegalStateException e) {

                        e.printStackTrace();
                    }
                }

            }
        };

        //nastavenie dlzky playbaru
        seekMusicBar.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();

        //nastavenie pozicie playbaru
        seekMusicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                //zmenenie pozicia playbaru
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        //handler na aktualizovanie momentalnej dlzky
        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //nastavenie momentalnej dlzky
                String currentTime = createDuration(mediaPlayer.getCurrentPosition());

                //to iste ale do textview
                txtSongStart.setText(currentTime);
                handler.postDelayed(this, delay);

            }
        }, delay);

        //OnClickListener na Play and Pause Buttony
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //kukne ci hra abo ne
                if (mediaPlayer.isPlaying()) {

                    //ikonka
                    btnPlay.setBackgroundResource(R.drawable.play_song_icon);

                    //stopne media
                    mediaPlayer.pause();

                } else {

                    //ikonka
                    btnPlay.setBackgroundResource(R.drawable.pause_song_icon);

                    //zapne media
                    mediaPlayer.start();

                    //animacia
                    TranslateAnimation moveAnim = new TranslateAnimation(-25, 25, -25, 25);
                    moveAnim.setInterpolator(new AccelerateInterpolator());
                    moveAnim.setDuration(600);
                    moveAnim.setFillEnabled(true);
                    moveAnim.setFillAfter(true);
                    moveAnim.setRepeatMode(Animation.REVERSE);
                    moveAnim.setRepeatCount(1);

                    imageView.startAnimation(moveAnim);

                    //zavola BarVisualizer

                }
            }
        });

        //urobi Button Click Operaciu po dokonceni songu
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnNext.performClick();
            }
        });

        //OnclickListener
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();

                position = ((position + 1) % mySongs.size());
                Uri uri1 = Uri.parse(mySongs.get(position).toString());
                uri=uri1;
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);
                songName = mySongs.get(position).getName();
                txtSongName.setText(songName);
                mediaPlayer.start();
                songEndTime();
                startAnimation(imageView, 360f);

            }
        });

        //OnClick Listener
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                mediaPlayer.release();

                position = ((position - 1) % mySongs.size());
                if (position < 0)
                    position = mySongs.size() - 1;
                Uri uri1 = Uri.parse(mySongs.get(position).toString());
                uri=uri1;
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);
                songName = mySongs.get(position).getName();
                txtSongName.setText(songName);
                mediaPlayer.start();
                songEndTime();
                startAnimation(imageView, -360f);
            }
        });

        //fastForward
        btnFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    //Getting the current position and adding 10sec to it
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                }
            }
        });

        //FastBackWard
        btnFastBackWard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    //Getting the curent Position of the song and decrease 10sec from it
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                }
            }
        });

        moznosti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSongList();
            }
        });

        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Pridane do oblubenych",Toast.LENGTH_SHORT).show();
                addToFav();
            }
        });

        equalizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                equalizer();

            }
        });

        snip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                snip();

            }
        });

        pocetPrehrani = sh.getInt(mySongs.get(position).getName(),0);

        myEdit.putInt(mySongs.get(position).getName(),pocetPrehrani+1);


        System.out.println(pocetPrehrani);

        myEdit.apply();


    }

    private void snip(){

            Intent intentSnip = new Intent(this, RingdroidEditActivity.class);
            intentSnip.putExtra("was_get_content_intent",true);
            intentSnip.setData(uri);

            startActivity(intentSnip);

    }


    private void  equalizer(){

        int sessionId = mediaPlayer.getAudioSessionId();


        DialogEqualizerFragment fragment = DialogEqualizerFragment.newBuilder()
                .setAudioSessionId(sessionId)
                .themeColor(ContextCompat.getColor(this, R.color.primaryColor))
                .textColor(ContextCompat.getColor(this, R.color.textColor))
                .accentAlpha(ContextCompat.getColor(this, R.color.playingCardColor))
                .darkColor(ContextCompat.getColor(this, R.color.primaryDarkColor))
                .setAccentColor(ContextCompat.getColor(this, R.color.secondaryColor))
                .build();
        fragment.show(getSupportFragmentManager(), "eq");




    }

private void openSongList(){

        Intent songListIntent = new Intent(this, addToListActivity.class);
        songListIntent.putExtra("song",uri.toString());
        startActivity(songListIntent);
}

    private void addToFav(){
        Intent songListIntent = new Intent(this, ListActivity.class);
        Uri uriFav = Uri.parse(mySongs.get(position).toString());
        songListIntent.putExtra("songFav",uriFav);
        System.out.println("URIIIIIII FAVOUTRITES  "+uriFav.toString());

        startActivity(songListIntent);
    }

    //metoda na vytvorenie animacie pre imageView
    public void startAnimation(View view, Float degree) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, degree);
        objectAnimator.setDuration(1000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();
    }

    //pripravenie casoveho formatu pre TextView
    public String createDuration(int duration) {

        String time = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        time = time + min + ":";

        if (sec < 10) {

            time += "0";

        }
        time += sec;
        return time;
    }

    //metoda na ziskanie momentalneho casu media a nastavenie ho na TextView
    public void songEndTime() {
        String endTime = createDuration(mediaPlayer.getDuration());
        txtSongEnd.setText(endTime);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}