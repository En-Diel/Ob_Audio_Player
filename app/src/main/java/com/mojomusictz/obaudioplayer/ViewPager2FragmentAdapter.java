package com.mojomusictz.obaudioplayer;

import static com.mojomusictz.obaudioplayer.MainActivity.tabLabels;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// Adapter to attach fragments to ViewPager2 using FragmentsStateAdapter
public class ViewPager2FragmentAdapter extends FragmentStateAdapter {

    public ViewPager2FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    // Returns fragments at every position
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new ArtistsFragment(); // Artists fragment
            case 1:
                return new TracksFragment(); // Tracks fragment
            case 2:
                return new AlbumsFragment(); // Albums fragment
            case 3:
                return new GenresFragment(); // Genres fragment
        }
        return new TracksFragment(); // Tracks fragment
    }
    // Return total number of tabs
    @Override
    public int getItemCount() {
        return tabLabels.length;
    }
}