package com.mojomusictz.obaudioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // Declare variables
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    // Array for tab labels
    public static String[] tabLabels = new String[] {"Artists", "Tracks", "Albums", "Genres"};

    ViewPager2FragmentAdapter viewPager2FragmentAdapter;
    public static final int REQUEST_CODE = 1; // Code for checking the permissions
    static ArrayList<MusicFilesModel> musicFiles; // This arraylist of type MusicFilesModel Model contains all the audio
    static boolean shuffleBoolean = false;
    static boolean repeatBoolean = false;
    static ArrayList<MusicFilesModel> albums = new ArrayList<>(); // This ArrayList will contain album songs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Checking for permsion
        permission();
        // Bind and set TabLayout to ViewPager2, and set labels for every tab
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {tab.setText(tabLabels[position]);}).attach();
        // Set default position to 1 instead of 0
        viewPager2.setCurrentItem(1, false);
    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }else {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            musicFiles = getAllTracks(this);
            // Initialize views
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We do some with permission granted
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                musicFiles = getAllTracks(this);
            }else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }

    private void init() {
        // Initialize tabLayout
        tabLayout = findViewById(R.id.idTabLayout);
        // Initialize viewPager2
        viewPager2 = findViewById(R.id.idViewPager2);
        // Create instance of adapter
        viewPager2FragmentAdapter = new ViewPager2FragmentAdapter(this);
        // Set adapter to viewpager2
        viewPager2.setAdapter(viewPager2FragmentAdapter);
        // Remove default elevation of action bar
        //getSupportActionBar().setElevation(0);
    }

    // Method to return an arrayList of music files
    public static ArrayList<MusicFilesModel> getAllTracks(Context context) {
        // Initialize the ArrayList model
        ArrayList<MusicFilesModel> tempTrackList = new ArrayList<>();
        ArrayList<String> duplicate = new ArrayList<>(); /* This ArrayList of type string will keep track of all the songs added to the
        ArrayList of Albums, so as to detect the duplicate audio in that arraylist */

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.DATA, // Equals to path
                                MediaStore.Audio.Media._ID,
                                MediaStore.Audio.Media.TITLE,
                                MediaStore.Audio.Media.ARTIST,
                                MediaStore.Audio.Media.TRACK,
                                MediaStore.Audio.Media.ALBUM,
                                MediaStore.Audio.Media.DURATION};

        Cursor cursor = context.getContentResolver().query(uri, projection,null,null,null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(0);
                String id = cursor.getString(1);
                String title = cursor.getString(2);
                String artist = cursor.getString(3);
                String track = cursor.getString(4);
                String album = cursor.getString(5);
                String duration = cursor.getString(6);

                // We call the model class MusicFilsModel
                MusicFilesModel musicFilesModel = new MusicFilesModel(path, id, title, artist, track, album, duration);
                // Let's Log to test if works
                Log.e("Path" + path,"Album" + album);
                // Then add the data into the empty initialized ArrayList
                tempTrackList.add(musicFilesModel);
                if(!duplicate.contains(album)) { // If duplicate ArrayList doesn't contain the string "album"
                    albums.add(musicFilesModel);
                    duplicate.add(album); // Update the ArrayList
                }
            }
            cursor.close();
        }
        return tempTrackList;
    }
}