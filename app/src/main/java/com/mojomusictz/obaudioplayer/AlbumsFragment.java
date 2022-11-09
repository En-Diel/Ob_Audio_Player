package com.mojomusictz.obaudioplayer;

import static com.mojomusictz.obaudioplayer.AlbumAdapter.albumList;
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

public class AlbumsFragment extends Fragment {

    RecyclerView albumRecyclerView;
    AlbumAdapter albumAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    public AlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        albumRecyclerView = view.findViewById(R.id.idAlbumRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.idAlbumsRefreshLayout);
        albumRecyclerView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            albumAdapter = new AlbumAdapter(getContext(), musicFiles);
            albumRecyclerView.setAdapter(albumAdapter);
            albumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            // Implementing swipe refresh
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
        albumAdapter = new AlbumAdapter(getContext(), albumList);
        albumRecyclerView.setAdapter(albumAdapter);
    }
}