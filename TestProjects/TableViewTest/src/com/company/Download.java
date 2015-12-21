package com.company;

import javafx.beans.property.SimpleStringProperty;

/**
 * Model class for testing.
 * @author Axel Kennedal
 * @version 1.0
 * Created on 2015-12-21.
 */
public class Download
{
    private SimpleStringProperty trackName;
    private SimpleStringProperty artist;
    private SimpleStringProperty length;

    public Download(String trackName, String artist, String length)
    {
        this.trackName = new SimpleStringProperty(trackName);
        this.artist = new SimpleStringProperty(artist);
        this.length = new SimpleStringProperty(length);
    }

    public String getTrackName()
    {
        return trackName.get();
    }

    public SimpleStringProperty trackNameProperty()
    {
        return trackName;
    }

    public void setTrackName(String trackName)
    {
        this.trackName.set(trackName);
    }

    public String getArtist()
    {
        return artist.get();
    }

    public SimpleStringProperty artistProperty()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist.set(artist);
    }

    public String getLength()
    {
        return length.get();
    }

    public SimpleStringProperty lengthProperty()
    {
        return length;
    }

    public void setLength(String length)
    {
        this.length.set(length);
    }
}
