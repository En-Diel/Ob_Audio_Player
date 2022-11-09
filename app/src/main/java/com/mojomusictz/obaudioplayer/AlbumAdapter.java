package com.mojomusictz.obaudioplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder3> {
    private Context mContext;
    static ArrayList<MusicFilesModel> albumList;

    public AlbumAdapter(Context mContext, ArrayList<MusicFilesModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public MyViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.albums_items, parent, false);
        return new MyViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder3 holder, int position) {
        holder.albumTitle.setText(albumList.get(position).getAlbum());
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class MyViewHolder3 extends RecyclerView.ViewHolder {
        ImageView albumArtWork;
        TextView albumTitle;
        public MyViewHolder3(@NonNull View itemView) {
            super(itemView);
            albumArtWork = itemView.findViewById(R.id.idAlbumArtWork);
            albumTitle = itemView.findViewById(R.id.idAlbumTitle);
        }
    }
}
