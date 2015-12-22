package SoundLoader.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

/**
 * A utility that downloads a file from a URL. Performs download on a new Thread.
 * @author www.codejava.net & Axel Kennedal
 * @version 2.0
 */
public class HttpDownload extends Task
{
    private static final int BUFFER_SIZE = 4096;
    private int bytesDownloaded = 0;
    private int fileSize = -1;
    private int attempt = 1;

    private String fileURL; // URL to the file to download
    private String saveDir; // Local directory to save file to
    private String fileName; // Filename of file to download, determined automatically
    private Thread threadForDownload;

    // properties for outside observers
    private SimpleStringProperty trackName;

    /**
     * Downloads a file from a URL.
     * @param fileURL HTTP URL of the file to be downloaded
     * @param saveDir path of the directory to save the file to
     * @throws IOException
     */
    public void downloadFile(String fileURL, String saveDir) throws IOException
    {
        this.fileURL = fileURL;
        this.saveDir = saveDir;
        this.trackName = new SimpleStringProperty("Fetching...");

        downloadFile();
    }

    /**
     * Helper method that starts the download, on a new thread.
     * @throws IOException
     */
    public void downloadFile() throws IOException
    {
        threadForDownload = new Thread(this);
        threadForDownload.start();
    }

    /**
     * Perform the actual download.
     * @return
     * @throws Exception
     */
    @Override
    protected Object call() throws Exception
    {
        // open connection and check if it's ok
        updateMessage("Connecting");
        URL url = new URL(fileURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int responseCode = connection.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            // figure out the filename of the file to be downloaded
            String disposition = connection.getHeaderField("Content-Disposition");

            if (disposition != null)
            {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0)
                {
                    fileName = disposition.substring(index + 10, disposition.length() - 1);
                }
            }
            else
            {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
            }

            this.trackName.set(fileName);

            // get more information about the file to be downloaded
            String contentType = connection.getContentType();
            fileSize = connection.getContentLength();

            if (fileSize < 1)
            {
                updateMessage("Failed");
                return false;
            }

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + fileSize);
            System.out.println("fileName = " + fileName);

            // opens input stream from the HTTP connection
            InputStream inputStream = connection.getInputStream();

            // prepare to save the file
            String saveFilePath = saveDir + File.separator + fileName;
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            // perform actual download
            updateMessage("Downloading");
            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1)
            {
                outputStream.write(buffer, 0, bytesRead);
                bytesDownloaded += bytesRead;
                updateProgress((float) bytesDownloaded / fileSize, 1.0);
            }

            outputStream.close();
            inputStream.close();
            updateMessage("Completed");

            System.out.println("File downloaded");
        }
        else
        {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        connection.disconnect();
        return true;
    }

    public void retry() throws IOException
    {
        threadForDownload.interrupt();
        downloadFile();
        attempt++;
    }

    public int getFileSize()
    {
        return fileSize;
    }

    public int getAttempt()
    {
        return attempt;
    }

    public void setAttempt(int attempt)
    {
        this.attempt = attempt;
    }

    public String getTrackName()
    {
        return trackName.get();
    }

    public SimpleStringProperty trackNameProperty()
    {
        return trackName;
    }
}