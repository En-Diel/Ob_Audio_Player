package com.mojomusictz.obaudioplayer;

import static com.mojomusictz.obaudioplayer.ArtistsAdapter.artistsFiles;
import static com.mojomusictz.obaudioplayer.MainActivity.musicFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Random;

public class ArtistsFragment extends Fragment {
    RecyclerView artistsRecyclerView;
    ArtistsAdapter artistsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    public ArtistsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        artistsRecyclerView = view.findViewById(R.id.idArtistsRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.idArtistsSwipeRefresh);
        artistsRecyclerView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            artistsAdapter = new ArtistsAdapter(getContext(), musicFiles);
            artistsRecyclerView.setAdapter(artistsAdapter);
            artistsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
            // Setting the color of the refreshing icon
            swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipeRefreshColor));
            // Implementing swipe refresh
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(false);
                    RearrangeItems();
                }
            });
        }
        return view;
    }
    // This method for rearranging the items using swipe refresh
    private void RearrangeItems() {
        Collections.shuffle(musicFiles, new Random(System.currentTimeMillis()));
        artistsAdapter = new ArtistsAdapter(getContext(), artistsFiles);
        artistsRecyclerView.setAdapter(artistsAdapter);
    }
}