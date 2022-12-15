package com.mojomusictz.obaudioplayer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class AlbumDetails extends AppCompatActivity {
    BlurView blurView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

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
        initViews();
        blurBackground(); // Blur the music player activity background

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
        blurView = findViewById(R.id.idBlurView);
    }
}