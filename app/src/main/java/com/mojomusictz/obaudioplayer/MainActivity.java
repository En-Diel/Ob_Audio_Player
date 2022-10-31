package com.mojomusictz.obaudioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    // Declare variables
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    // Array for tab labels
    public static String[] tabLabels = new String[] {"Artists", "Tracks", "Albums", "Genres"};

    ViewPager2FragmentAdapter viewPager2FragmentAdapter;
    public static final int REQUEST_CODE = 1; // Code for checking the permissions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        permission();
        // Initialize views
        init();
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
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We do some with permission granted
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
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
}