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

public class MusicFilesAdapter extends RecyclerView.Adapter<MusicFilesAdapter.MyViewHolder> {

    private ArrayList<MusicFilesModel> mFiles;
    private Context mContext;

    MusicFilesAdapter(Context mContext, ArrayList<MusicFilesModel> mFiles) {
        this.mContext = mContext;
        this.mFiles = mFiles;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tracks_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.trackTitle.setText(mFiles.get(position).getTitle());
        holder.artistName.setText(mFiles.get(position).getArtist());
        byte[] image = Utilize.getAlbumArt(mFiles.get(position).getPath());
        if (image != null) {
            Glide.with(mContext).asBitmap().load(image).into(holder.trackArtWork);
        }else {
            Glide.with(mContext).asBitmap().load(R.drawable.ic_headphones).into(holder.trackArtWork);
        }
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView trackArtWork;
        TextView trackTitle;
        TextView artistName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            trackArtWork = itemView.findViewById(R.id.idTracksArtWork);
            trackTitle = itemView.findViewById(R.id.idTrackTitle);
            artistName = itemView.findViewById(R.id.idArtistName);
        }
    }
}
