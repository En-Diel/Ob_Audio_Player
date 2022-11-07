package com.mojomusictz.obaudioplayer;

import static com.mojomusictz.obaudioplayer.ArtistsAdapter.artistsFiles;
import static com.mojomusictz.obaudioplayer.MainActivity.musicFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ArtistsFragment extends Fragment {
    RecyclerView artistsRecyclerView;
    ArtistsAdapter artistsAdapter;

    public ArtistsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        artistsRecyclerView = view.findViewById(R.id.idArtistsRecyclerView);
        artistsRecyclerView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            artistsAdapter = new ArtistsAdapter(getContext(), musicFiles);
            artistsRecyclerView.setAdapter(artistsAdapter);
            artistsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        return view;
    }
}