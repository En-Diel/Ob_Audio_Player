package com.mojomusictz.obaudioplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    // Declare variables
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    // Array for tab labels
    public static String[] tabLabels = new String[] {"Artists", "Tracks", "Albums", "Genres"};

    ViewPager2FragmentAdapter viewPager2FragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        init();
        // Bind and set TabLayout to ViewPager2, and set labels for every tab
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {tab.setText(tabLabels[position]);}).attach();
        // Set default position to 1 instead of 0
        viewPager2.setCurrentItem(1, false);
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
        getSupportActionBar().setElevation(0);
    }
}