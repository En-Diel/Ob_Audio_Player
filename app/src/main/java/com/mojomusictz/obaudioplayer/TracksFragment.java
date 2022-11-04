package com.mojomusictz.obaudioplayer;

import static com.mojomusictz.obaudioplayer.MainActivity.musicFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TracksFragment extends Fragment {
    RecyclerView tracksRecyclerView;
    MusicFilesAdapter musicFilesAdapter;

    public TracksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);
        tracksRecyclerView = view.findViewById(R.id.idTracksRecyclerView);
        tracksRecyclerView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            musicFilesAdapter = new MusicFilesAdapter(getContext(), musicFiles);
            tracksRecyclerView.setAdapter(musicFilesAdapter);
            tracksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }
        return view;
    }
}