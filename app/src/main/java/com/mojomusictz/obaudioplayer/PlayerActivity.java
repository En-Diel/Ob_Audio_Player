package com.mojomusictz.obaudioplayer;

import static com.mojomusictz.obaudioplayer.MainActivity.musicFiles;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class PlayerActivity extends AppCompatActivity {
    ActionBar actionBar;
    TextView trackTitle;
    TextView artistName;
    TextView durationPlayed;
    TextView durationTotal;
    ImageView coverArt;
    ImageView blurCoverArt;
    ImageView nextBtn;
    ImageView prevBtn;
    ImageView shuffleBtn;
    ImageView repeatBtn;
    ImageView backBtn;
    ImageView playPause;
    SeekBar seekBar;
    int position = -1;
    BlurView blurView;
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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); // Ignores the top/bottom app system bars
        // and fill it with design
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_700));
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        // Initialize the views with this method
        initViews();
        blurBackground(); // Blur the music player activity background
        getIntentMethod();
        artistName.setText(tracksList.get(position).getArtist()); // Set artist name
        trackTitle.setText(tracksList.get(position).getTitle()); // set track name
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

    private void blurBackground() {
        float radius = 21f; // Maximum is 25f
        View decorView = getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();
        blurView.setupWith(rootView).setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(true);

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
        metaData(uri);
    }

    private void metaData(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotalTime = Integer.parseInt(tracksList.get(position).getDuration());
        durationTotal.setText(formattedTime(durationTotalTime));
        byte[] art = retriever.getEmbeddedPicture();
        if (art != null) {
            RequestOptions options = new RequestOptions();
            Glide.with(this).asBitmap().load(art).into(blurCoverArt);
            Glide.with(this).asBitmap().load(art).into(coverArt);
        }else {
            Glide.with(this).asBitmap().load(R.drawable.homestu).into(blurCoverArt);
            Glide.with(this).asBitmap().load(R.drawable.homestu).into(coverArt);
        }
    }

    private void initViews() {
        trackTitle = findViewById(R.id.idTVTrackTitle);
        artistName = findViewById(R.id.idTVArtistName);
        durationPlayed = findViewById(R.id.idTVDurationPlayed);
        durationTotal = findViewById(R.id.idDurationTotal);
        coverArt = findViewById(R.id.idIVCoverArt);
        blurCoverArt = findViewById(R.id.idIVCoverArtBlur);
        nextBtn = findViewById(R.id.idIVSkipNext);
        prevBtn = findViewById(R.id.idIVSkipPrevious);
        shuffleBtn = findViewById(R.id.idIVShuffle);
        repeatBtn = findViewById(R.id.idIVRepeat);
        backBtn = findViewById(R.id.idIVPlayerBackBtn);
        seekBar = findViewById(R.id.idSeekBar);
        playPause = findViewById(R.id.idIVPlayPauseBtn);
        blurView = findViewById(R.id.idBlurView);
    }
}