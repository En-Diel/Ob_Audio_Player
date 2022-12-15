package com.mojomusictz.obaudioplayer;

import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class Utilize {
    // Private constructor to prevent instantiation
    private Utilize() {}

    public static byte[] getAlbumArt(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

}
