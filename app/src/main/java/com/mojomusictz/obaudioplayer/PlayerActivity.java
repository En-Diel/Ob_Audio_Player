package com.mojomusictz.obaudioplayer;

import static com.mojomusictz.obaudioplayer.MainActivity.musicFiles;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import io.alterac.blurkit.BlurLayout;

public class PlayerActivity extends AppCompatActivity {
    ActionBar actionBar;
    //RangeBar rangeBar;
    ImageView backBtn;
    ImageView menuMoreBtn;
    ImageView shuffleBtn;
    ImageView previousBtn;
    ImageView nextBtn;
    ImageView playPauseBtn;
    ImageView repeatBtn;
    SeekBar seekBar;
    TextView artistName;
    TextView trackTitle;
    TextView durationTotal;
    TextView durationPlayed;
    private BlurLayout blurLayout;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    int position = -1;
    ArrayList<MusicFilesModel> trackList = new ArrayList<>(); // Initialize empty arrayList model
    Uri uri;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        // Hiding the actionBar in this Activity only
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Function to initialize the views
        initViews();
        // SeekBar Listener
    /*    rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {

            }
        });*/
        blurLayout = findViewById(R.id.idBlurLayout);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); // Ignores the top/bottom app system bars
        // and fill it with design
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_700));
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        getIntentMethod();
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        trackList = musicFiles;
        if (trackList != null) {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(trackList.get(position).getPath()); // Get path of the song
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
    }

    private void initViews() {
        backBtn = findViewById(R.id.idIVPlayerBackBtn);
        menuMoreBtn = findViewById(R.id.idMoreOptions);
        shuffleBtn = findViewById(R.id.idIVShuffle);
        previousBtn = findViewById(R.id.idIVSkipPrevious);
        nextBtn = findViewById(R.id.idIVSkipNext);
        playPauseBtn = findViewById(R.id.idIVPlayPauseBtn);
        repeatBtn = findViewById(R.id.idIVRepeat);
        //seekBar = findViewById(R.id.idSeekBar);
        artistName = findViewById(R.id.idArtistName);
        trackTitle = findViewById(R.id.idTrackTitle);
        durationPlayed = findViewById(R.id.idTVAudioDurationPlayed);
        durationTotal = findViewById(R.id.idTVAudioDurationTotal);
    }

    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
    }

    @Override
    protected void onStop() {
        blurLayout.pauseBlur();
        super.onStop();
    }
}