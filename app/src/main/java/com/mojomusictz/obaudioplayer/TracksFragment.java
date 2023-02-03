package com.mojomusictz.obaudioplayer;

import static com.mojomusictz.obaudioplayer.MainActivity.musicFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Random;

public class TracksFragment extends Fragment {
    RecyclerView tracksRecyclerView;
    static MusicFilesAdapter musicFilesAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    public TracksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);
        tracksRecyclerView = view.findViewById(R.id.idTracksRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.idTracksRefreshLayout);
        tracksRecyclerView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            musicFilesAdapter = new MusicFilesAdapter(getContext(), musicFiles);
            tracksRecyclerView.setAdapter(musicFilesAdapter);
            tracksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            // Implementing Swipe refresh
            swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipeRefreshColor));
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
        musicFilesAdapter = new MusicFilesAdapter(getContext(), musicFiles);
        tracksRecyclerView.setAdapter(musicFilesAdapter);
    }
}