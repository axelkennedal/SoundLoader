package SoundLoader.Controller;

import SoundLoader.Model.HttpDownload;
import SoundLoader.View.UIManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Axel Kennedal
 * @version 1.5
 * Created on 2015-12-18.
 */
public class DownloadManager
{
    static String youtubeConverterURL = "http://www.youtubeinmp3.com/fetch/?format=JSON&video=";
    private String saveDir;
    Main mainApplicationClass;

    public void setSaveDir(String saveDir)
    {
        this.saveDir = saveDir;
    }

    public String getSaveDir()
    {
        return saveDir;
    }

    public DownloadManager(Main mainApplicationClass) { this.mainApplicationClass = mainApplicationClass; }

    /**
     * Attempts to start a new youtube download and add it to the table.
     * @param youtubeLink
     * @return
     */
    public HttpDownload startNewYoutubeDownload(String youtubeLink)
    {
        try
        {
            youtubeLink = youtubeLink.replace("https", "http"); // must be done for youtubeinmp3 API to work
            String youtubeConverterLink = youtubeConverterURL + youtubeLink;
            String downloadURL = JSONParser.getDownloadLink(youtubeConverterLink);

            int URLAttempts = 1;
            while (downloadURL == null && URLAttempts < 5)
            {
                downloadURL = JSONParser.getDownloadLink(youtubeConverterLink);
                URLAttempts++;
            }
            HttpDownload download = new HttpDownload();
            download.downloadFile(downloadURL, saveDir);

            download.messageProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.equals("Failed") && download.getAttempt() < 5)
                {
                    try
                    {
                        download.retry();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            UIManager uiManager =  mainApplicationClass.getUiManager();
            uiManager.getFlexibleTableView().getData().add(download);

            return download;
        }
        catch (Exception e)
        {

        }
        return null;
    }


}
