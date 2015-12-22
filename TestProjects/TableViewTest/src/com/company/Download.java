package com.company;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

/**
 * Model class for testing.
 * @author Axel Kennedal
 * @version 1.0
 * Created on 2015-12-21.
 */
public class Download extends Task
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

    /*
    Simulate performing a download
     */
    @Override
    protected Object call() throws Exception
    {
        updateMessage("Connecting...");
        Thread.sleep(500);

        for (int i = 1; i <= 100; i++)
        {
            updateProgress(0.01 * i, 1.0);
            updateMessage("Working...");
            Thread.sleep(20);
        }
        updateMessage("Done");

        return true;
    }
}
