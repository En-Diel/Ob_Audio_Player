package com.mojomusictz.obaudioplayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyViewHolder4> {

    private Context mContext;
    static ArrayList<MusicFilesModel> albumSongs;

    public AlbumDetailsAdapter(Context mContext, ArrayList<MusicFilesModel> albumSongs) {
        this.mContext = mContext;
        this.albumSongs = albumSongs;
    }


    @NonNull
    @Override
    public AlbumDetailsAdapter.MyViewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tracks_items, parent,false);
        return new MyViewHolder4(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailsAdapter.MyViewHolder4 holder, int position) {
        holder.albumTitle.setText(albumSongs.get(position).getAlbum());
        holder.artistName.setText(albumSongs.get(position).getArtist());
        byte[] image = Utilize.getAlbumArt(albumSongs.get(position).getPath());
        if (image != null) {
            Glide.with(mContext).asBitmap().load(image).into(holder.albumCoverArt);
        }else {
            Glide.with(mContext).asBitmap().load(R.drawable.ic_headphones).into(holder.albumCoverArt);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("sender", "albumDetails"); // This extras identifies where the playerActivity has been started
                intent.putExtra("position", position);
                mContext.startActivity(intent); // We start activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumSongs.size();
    }

    public class MyViewHolder4 extends RecyclerView.ViewHolder {
        TextView albumTitle;
        TextView artistName;
        ImageView albumCoverArt;
        public MyViewHolder4(@NonNull View itemView) {
            super(itemView);
            albumTitle = itemView.findViewById(R.id.idTrackTitle);
            artistName = itemView.findViewById(R.id.idArtistName);
            albumCoverArt = itemView.findViewById(R.id.idTracksArtWork);
        }
    }
}
