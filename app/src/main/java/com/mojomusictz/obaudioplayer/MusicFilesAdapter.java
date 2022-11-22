package com.mojomusictz.obaudioplayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
        // Setting the Track Title in the arrayList
        holder.trackTitle.setText(mFiles.get(position).getTitle());
        // Setting the Artis Name in the arrayList
        holder.artistName.setText(mFiles.get(position).getArtist());
        // Setting an artWork in the arrayList
        byte[] image = Utilize.getAlbumArt(mFiles.get(position).getPath()); // Getting the path of the artWork
        if (image != null) {
            Glide.with(mContext).asBitmap().load(image).into(holder.trackArtWork); // Bind the artWork
        }else {
            Glide.with(mContext).asBitmap().load(R.drawable.ic_headphones).into(holder.trackArtWork); // Bind the default artWork if none
        }
        // Setting the onClick action for when menu more option is clicked
        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.idInfo:
                                Toast.makeText(mContext, "Info Clicked", Toast.LENGTH_SHORT).show(); // Show info for debugging
                                getTrackInfo();
                                break;
                            case R.id.idShare:
                                Toast.makeText(mContext, "Share Clicked", Toast.LENGTH_SHORT).show(); // Show info for debugging
                                share();
                                break;
                            case R.id.idAddToPlaylist:
                                Toast.makeText(mContext, "Add to playlist Clicked", Toast.LENGTH_SHORT).show(); // Show info for debugging
                                addToPlaylist();
                                break;
                            case R.id.idSetRingtone:
                                Toast.makeText(mContext, "Set as ringtone Clicked", Toast.LENGTH_SHORT).show(); // Show info for debugging
                                setAsRingtone();
                                break;
                            case R.id.idDelete:
                                Toast.makeText(mContext, "Delete Clicked", Toast.LENGTH_SHORT).show(); // Show info for debugging
                                deleteFile();
                                break;
                        }
                        return true;
                    }
                });
            }
        });
        // Setting onClick action for when a song is clicked in the array list
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "A Track is Clicked", Toast.LENGTH_SHORT).show(); // Show info for debugging
                Intent intent = new Intent(mContext, PlayerActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView trackArtWork;
        ImageView menuMore;
        TextView trackTitle;
        TextView artistName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            trackArtWork = itemView.findViewById(R.id.idTracksArtWork);
            trackTitle = itemView.findViewById(R.id.idTrackTitle);
            artistName = itemView.findViewById(R.id.idArtistName);
            menuMore = itemView.findViewById(R.id.idMoreOptions);
        }
    }
    // Function to add tracks to a playlist
    private void addToPlaylist() {
    }
    // Function set a track as ringtone
    private void setAsRingtone() {
    }
    // Function to share a track to email, bluethooth, what'sapp e.t.c
    private void share() {
    }

    private void getTrackInfo() {
    }
    // Function to show track's information/data
    private void deleteFile() {
    }
}
