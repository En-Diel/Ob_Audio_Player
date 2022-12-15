package com.mojomusictz.obaudioplayer;

import static com.mojomusictz.obaudioplayer.MainActivity.musicFiles;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class AlbumDetails extends AppCompatActivity {
    BlurView blurView;
    String albumTitle;
    TextView artistName;
    TextView albumYear;
    ImageView albumCoverArt;
    ImageView albumCoverArtBlur;
    Button albumPlayBtn;
    RecyclerView albumDetailsRecyclerView;
    ArrayList<MusicFilesModel> albumSongs = new ArrayList<>(); // This will be the albums songs to be displayed
    AlbumDetailsAdapter albumDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        // Hiding the actionBar in this Activity only
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        /* These code below with getWindow helps to achieve the blur effect */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); // Ignores the top/bottom app system bars
        // and fill it with design
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_700));
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        // Initialize the views
        initViews();
        blurBackground(); // Blur the music player activity background
        // Populating the album songs
        int j = 0;
        for (int i = 0; i < musicFiles.size(); i++) {
            if (albumTitle.equals(musicFiles.get(i).getAlbum())) {
                albumSongs.add(j, musicFiles.get(i)); // loading the album songs
                j++;
            }
        }
        // Loading the album cover art
        byte[] image = Utilize.getAlbumArt(albumSongs.get(0).getPath());
        if (image != null) {
            Glide.with(this).load(image).into(albumCoverArt);
            Glide.with(this).load(image).into(albumCoverArtBlur);
        }else {
            Glide.with(this).load(R.drawable.ic_headphones).into(albumCoverArt);
            Glide.with(this).load(R.drawable.ic_headphones).into(albumCoverArtBlur);
        }

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

    private void initViews() {
        blurView = findViewById(R.id.idAlbumDetailsBlurView);
        albumTitle = getIntent().getStringExtra("albumName");
        albumCoverArt = findViewById(R.id.idIVAlbumDetailsCoverArt);
        albumCoverArtBlur = findViewById(R.id.idIVAlbumDetailsCoverArtBlur);
        albumDetailsRecyclerView = findViewById(R.id.idAlbumDetailsRecyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(albumSongs.size() < 1)) {
            albumDetailsAdapter = new AlbumDetailsAdapter(this, albumSongs);
            albumDetailsRecyclerView.setAdapter(albumDetailsAdapter);
            albumDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }
    }
}