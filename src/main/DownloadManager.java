import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Axel Kennedal
 * @version 1.0
 * Created on 2015-12-18.
 */
public class DownloadManager implements Observer
{
    static String youtubeConverterURL = "http://www.youtubeinmp3.com/fetch/?video=";
    ArrayList<HttpDownloadUtility> downloads;

    DownloadManager()
    {
        downloads = new ArrayList<>();
    }

    public void startNewYoutubeDownload(String youtubeLink, String saveDir)
    {
        try
        {
            String downloadURL = youtubeConverterURL + youtubeLink;
            HttpDownloadUtility download = new HttpDownloadUtility();
            download.addObserver(this);
            download.downloadFile(downloadURL, saveDir.toString());
            downloads.add(download);
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        HttpDownloadUtility notifier = (HttpDownloadUtility) arg;
        System.err.println("State change: " + notifier.getCurrentStatus());
        if (arg instanceof HttpDownloadUtility)
        {

        }
    }
}
