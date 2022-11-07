package com.mojomusictz.obaudioplayer;

import static com.mojomusictz.obaudioplayer.AlbumAdapter.albumList;
import static com.mojomusictz.obaudioplayer.MainActivity.musicFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AlbumsFragment extends Fragment {
    RecyclerView albumRecyclerView;
    AlbumAdapter albumAdapter;
    public AlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        albumRecyclerView = view.findViewById(R.id.idAlbumRecyclerView);
        albumRecyclerView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            albumAdapter = new AlbumAdapter(getContext(), musicFiles);
            albumRecyclerView.setAdapter(albumAdapter);
            albumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        return view;
    }
}