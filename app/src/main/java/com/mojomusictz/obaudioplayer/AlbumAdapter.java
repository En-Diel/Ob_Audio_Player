package com.mojomusictz.obaudioplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
        // Setting the album title
        holder.albumTitle.setText(albumList.get(position).getAlbum());
        // Setting the artist name
        holder.artistName.setText(albumList.get(position).getArtist());
        // Binding the album artwork
        byte[] image = Utilize.getAlbumArt(albumList.get(position).getPath());
        if (image != null) {
            Glide.with(mContext).asBitmap().load(image).into(holder.albumArtWork);
        }else {
            Glide.with(mContext).asBitmap().load(R.drawable.ic_headphones).into(holder.albumArtWork);
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class MyViewHolder3 extends RecyclerView.ViewHolder {
        ImageView albumArtWork;
        TextView albumTitle;
        TextView artistName;
        public MyViewHolder3(@NonNull View itemView) {
            super(itemView);
            albumArtWork = itemView.findViewById(R.id.idAlbumFragAlbumArtWork);
            albumTitle = itemView.findViewById(R.id.idAlbumFragAlbumTitle);
            artistName = itemView.findViewById(R.id.idAlbumFragArtistName);
        }
    }
}
