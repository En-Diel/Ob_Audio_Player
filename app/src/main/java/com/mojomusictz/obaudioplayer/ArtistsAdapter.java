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
        holder.artistName.setText(artistsFiles.get(position).getArtist());
    }

    @Override
    public int getItemCount() {
        return artistsFiles.size();
    }

    public class MyViewHolder2 extends RecyclerView.ViewHolder {
        ImageView artistArtWork;
        TextView artistName;
        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            artistArtWork = itemView.findViewById(R.id.idArtistArtWork);
            artistName = itemView.findViewById(R.id.idArtistName);
        }
    }
}
