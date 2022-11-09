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

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.MyViewHolder2> {
    private Context mContext;
    static ArrayList<MusicFilesModel> artistsFiles;
    View view;

    ArtistsAdapter(Context mContext, ArrayList<MusicFilesModel> artistsFiles) {
        this.mContext = mContext;
        this.artistsFiles = artistsFiles;
    }
    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.artists_items, parent, false);
        return new MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {
        // Setting artist name
        holder.artistName.setText(artistsFiles.get(position).getArtist());
        // Setting album title
        holder.albumName.setText(artistsFiles.get(position).getAlbum());
        // Getting the image path
        byte[] image = Utilize.getAlbumArt(artistsFiles.get(position).getPath());
        if (image != null) {
            // Binding the image
            Glide.with(mContext).asBitmap().load(image).into(holder.artistArtWork);
        }else {
            Glide.with(mContext).asBitmap().load(R.drawable.ic_headphones).into(holder.artistArtWork);
        }
    }

    @Override
    public int getItemCount() {
        return artistsFiles.size();
    }

    public class MyViewHolder2 extends RecyclerView.ViewHolder {
        ImageView artistArtWork;
        TextView artistName;
        TextView albumName;
        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            artistArtWork = itemView.findViewById(R.id.idArtistsFragArtistArtWork);
            artistName = itemView.findViewById(R.id.idArtistsFragArtistName);
            albumName = itemView.findViewById(R.id.idArtistsFragAlbumTitle);
        }
    }
}
