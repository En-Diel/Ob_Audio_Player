package com.mojomusictz.obaudioplayer;

// This is the model to interact with our files database
public class MusicFilesModel {
    // Declare variables
    private String path;
    private String id;
    private String title;
    private String artist;
    private String track;
    private String album;
    private String duration;
    // Constructor
    public MusicFilesModel(String path, String id, String title, String artist, String track, String album, String duration) {
        this.path = path;
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.track = track;
        this.album = album;
        this.duration = duration;
    }
    // Getters & setters

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
