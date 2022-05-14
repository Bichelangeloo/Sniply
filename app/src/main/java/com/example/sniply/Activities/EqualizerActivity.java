package com.example.sniply.Activities;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EqualizerActivity extends AppCompatActivity {


    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            mediaPlayer=PlayerActivity.mediaPlayer;



    }
}
