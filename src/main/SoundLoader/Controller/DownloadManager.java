package SoundLoader.Controller;

import SoundLoader.Model.HttpDownload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Axel Kennedal
 * @version 1.5
 * Created on 2015-12-18.
 */
public class DownloadManager extends Observable implements Observer
{
    static String youtubeConverterURL = "http://www.youtubeinmp3.com/fetch/?format=JSON&video=";
    private String saveDir;

    // keep all of the downloads from this session in a list
    private ArrayList<HttpDownload> downloads;

    public void setSaveDir(String saveDir)
    {
        this.saveDir = saveDir;
    }

    public String getSaveDir()
    {
        return saveDir;
    }

    public ArrayList<HttpDownload> getDownloads()
    {
        return downloads;
    }

    DownloadManager()
    {
        downloads = new ArrayList<>();
    }


    public HttpDownload startNewYoutubeDownload(String youtubeLink)
    {
        try
        {
            youtubeLink = youtubeLink.replace("https", "http"); // must be done for youtubeinmp3 API to work
            String youtubeConverterLink = youtubeConverterURL + youtubeLink;
            String downloadURL = JSONParser.getDownloadLink(youtubeConverterLink);
            HttpDownload download = new HttpDownload();
            download.addObserver(this);
            download.downloadFile(downloadURL, saveDir);
            downloads.add(download);
            return download;
        }
        catch (Exception e)
        {

        }
        return null;
    }

    /**
     * Notify observers that the state changed.
     * @param o send along an object with additional data
     */
    private void stateChanged(Object o)
    {
        setChanged();
        notifyObservers(o);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (arg instanceof HttpDownload)
        {
            HttpDownload notifier = (HttpDownload) arg;
            System.err.println("State change: " + notifier.getCurrentStatus());
            stateChanged(notifier);

            // retry download a couple of times if it failed
            if (notifier.getCurrentStatus() == HttpDownload.STATUS.FAILED && notifier.getAttempt() < 5)
            {
                notifier.setAttempt(notifier.getAttempt() + 1);
                try
                {
                    notifier.downloadFile();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
