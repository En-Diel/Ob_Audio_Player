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
import android.os.Handler;
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
    TextView trackName;
    TextView artistName;
    TextView durationPlayed;
    TextView durationTotal;
    ImageView coverArt;
    ImageView nextBtn;
    ImageView prevBtn;
    ImageView shuffleBtn;
    ImageView repeatBtn;
    ImageView backBtn;
    ImageView playPause;
    SeekBar seekBar;
    int position = -1;
    private BlurLayout blurLayout;
    static ArrayList<MusicFilesModel> tracksList = new ArrayList<>();
    static Uri uri;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Hiding the actionBar in this Activity only
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
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
        // Initialize the views with this method
        initViews();
        getIntentMethod();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // We use this thread so as to update the UI as the background process is going on
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    durationPlayed.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000); // This is time taken to update the seekbar
            }
        });
    }

    private String formattedTime(int mCurrentPosition) {
        String totalOut;
        String totalNew;
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        }else {
            return totalOut;
        }
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        tracksList = musicFiles;
        if (tracksList != null) {
            playPause.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(tracksList.get(position).getPath());
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
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
    }

    private void initViews() {
        trackName = findViewById(R.id.idTrackTitle);
        artistName = findViewById(R.id.idArtistName);
        durationPlayed = findViewById(R.id.idTVDurationPlayed);
        durationTotal = findViewById(R.id.idDurationTotal);
        coverArt = findViewById(R.id.idIVCoverArt);
        nextBtn = findViewById(R.id.idIVSkipNext);
        prevBtn = findViewById(R.id.idIVSkipPrevious);
        shuffleBtn = findViewById(R.id.idIVShuffle);
        repeatBtn = findViewById(R.id.idIVRepeat);
        backBtn = findViewById(R.id.idIVPlayerBackBtn);
        seekBar = findViewById(R.id.idSeekBar);
        playPause = findViewById(R.id.idIVPlayPauseBtn);
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