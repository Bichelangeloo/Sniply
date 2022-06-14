package com.example.sniply.Activities;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bullhead.equalizer.EqualizerFragment;
import com.example.sniply.R;

public class EqualizerActivity extends AppCompatActivity {


    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equalizer_layout);


            mediaPlayer=PlayerActivity.mediaPlayer;

        int sessionId = mediaPlayer.getAudioSessionId();
        mediaPlayer.setLooping(true);
        EqualizerFragment equalizerFragment = EqualizerFragment.newBuilder()
                .setAccentColor(Color.parseColor("#4caf50"))
                .setAudioSessionId(sessionId)
                .build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.eqFrame, equalizerFragment)
                .commit();

    }
}
