package com.example.sniply.Activities;

import static com.example.sniply.Activities.PlayerActivity.mediaPlayer;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bullhead.equalizer.EqualizerFragment;
import com.example.sniply.R;

public class EqualizerActivity extends AppCompatActivity {





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equalizer_layout);

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

    @Override
    protected void onPause() {
        super.onPause();



        System.out.println("EQUALIZER PAUSED");





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        System.out.println("EQUALIZER DESTROYED");

    }


}
