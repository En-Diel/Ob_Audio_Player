package com.mojomusictz.obaudioplayer;

import static com.mojomusictz.obaudioplayer.AlbumDetailsAdapter.albumSongs;
import static com.mojomusictz.obaudioplayer.MainActivity.musicFiles;
import static com.mojomusictz.obaudioplayer.MainActivity.repeatBoolean;
import static com.mojomusictz.obaudioplayer.MainActivity.shuffleBoolean;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.Random;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{
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
    ImageView playPauseBtn;
    SeekBar seekBar;
    int position = -1;
    BlurView blurView;
    static ArrayList<MusicFilesModel> tracksList = new ArrayList<>();
    static Uri uri;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    private Thread playThread;
    private Thread prevThread;
    private Thread nextThread;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Hiding the actionBar in this Activity only
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        /*
          These code below with getWindow helps to achieve the blur effect
         */
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
        // This enables the next audio to be played when the current one ends
        mediaPlayer.setOnCompletionListener(this);
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
        // Setting onclick listener for shuffle button
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffleBoolean) {
                    shuffleBoolean = false;
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle_off);
                }else {
                    shuffleBoolean = true;
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle_on);
                }
            }
        });
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatBoolean) {
                    repeatBoolean = false;
                    repeatBtn.setImageResource(R.drawable.ic_repeat_off);
                }else {
                    repeatBoolean = true;
                    repeatBtn.setImageResource(R.drawable.ic_repeat_on);
                }
            }
        });
    }
    // For Player Activity Blur Effect
    private void blurBackground() {
        float radius = 5f; // Maximum is 25f
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
        String sender = getIntent().getStringExtra("sender"); // We get the stringExtra from AlbumDetailsAdapter
        if (sender != null && sender.equals("albumDetails")) {
            tracksList = albumSongs;
        }else {
            tracksList = musicFiles;
        }
        if (tracksList != null) {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
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
        int durationTotalTime = Integer.parseInt(tracksList.get(position).getDuration()) / 1000;
        durationTotal.setText(formattedTime(durationTotalTime));
        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if (art != null) {
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length); // Create a bitmap and assign it
            imageAnimation(this, blurCoverArt, bitmap);
            imageAnimation(this, coverArt, bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    //assert palette != null; // Add this line to avoid null pointer exception
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch != null) {
                        ImageView player_gradient = findViewById(R.id.idIVGradient);
                        ImageView player_gradient_top = findViewById(R.id.idIVGradientTop);
                        player_gradient.setBackgroundResource(R.drawable.player_gradient);
                        player_gradient_top.setBackgroundResource(R.drawable.player_gradient_top);

                        GradientDrawable gradientDrawableBgBottom = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), 0x00000000});
                        player_gradient.setBackground(gradientDrawableBgBottom);
                        GradientDrawable gradientDrawableBgTop = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, swatch.getRgb()});
                        player_gradient_top.setBackground(gradientDrawableBgTop);
                        trackTitle.setTextColor(Color.WHITE);
                        artistName.setTextColor(Color.WHITE);
                    }
                }
            });
        }else {
            Glide.with(this).asBitmap().load(R.drawable.ic_headphones).centerCrop().into(blurCoverArt);
            Glide.with(this).asBitmap().load(R.drawable.ic_headphones).centerCrop().into(coverArt);
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
        playPauseBtn = findViewById(R.id.idIVPlayPauseBtn);
        blurView = findViewById(R.id.idBlurView);
    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }
    // Thread for previous btn
    private void prevThreadBtn() {
        prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }
    // Method for previous btn to work
    private void prevBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            //Check conditions for applying repeat and shuffle
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(tracksList.size() - 1);
            }else if(!shuffleBoolean && !repeatBoolean) {
                position = ((position -1) < 0 ? (tracksList.size()) : (position -1));
            }
            uri = Uri.parse(tracksList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            trackTitle.setText(tracksList.get(position).getTitle());
            artistName.setText(tracksList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            // This enables the next audio to be played when the current one ends
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else {
            mediaPlayer.stop();
            mediaPlayer.release();
            //Check conditions for applying repeat and shuffle
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(tracksList.size() - 1);
            }else if(!shuffleBoolean && !repeatBoolean) {
                position = ((position -1) < 0 ? (tracksList.size()) : (position -1));
            }
            uri = Uri.parse(tracksList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            trackTitle.setText(tracksList.get(position).getTitle());
            artistName.setText(tracksList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            // This enables the next audio to be played when the current one ends
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setImageResource(R.drawable.ic_play);
        }
    }
    // Thread for the next btn
    private void nextThreadBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }
    // Method for next btn to work
    private void nextBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            //Check conditions for applying repeat and shuffle
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(tracksList.size() - 1);
            }else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % tracksList.size());
            }
            uri = Uri.parse(tracksList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            trackTitle.setText(tracksList.get(position).getTitle());
            artistName.setText(tracksList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            // This enables the next audio to be played when the current one ends
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else {
            mediaPlayer.stop();
            mediaPlayer.release();
            //Check conditions for applying repeat and shuffle
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(tracksList.size() - 1);
            }else if(!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % tracksList.size());
            }
            uri = Uri.parse(tracksList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            trackTitle.setText(tracksList.get(position).getTitle());
            artistName.setText(tracksList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            // This enables the next audio to be played when the current one ends
            mediaPlayer.setOnCompletionListener(this);
            //playPauseBtn.setImageResource(R.drawable.ic_play); // commented this line to avoid the pause btn not showing
            // After onCompleteListener
        }
    }
    // Creates a random number in the range of trackList(bound of trackList)
    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
    }

    // Thread for play/pause btn
    private void playThreadBtn() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }
    // Method for play/pause btn to work
    private void playPauseBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            playPauseBtn.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }else {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }
    // Image animation method when click next or previous
    public void imageAnimation(Context context, ImageView imageView, Bitmap bitmap) {
        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animOut);
    }
    // This is for when audio ends it starts the next one
    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnClicked();
        if (mediaPlayer != null)  {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}