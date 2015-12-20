package SoundLoader.Model;

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
 * @version 1.5
 */
public class HttpDownload extends Observable
{
    private static final int BUFFER_SIZE = 4096;
    public enum STATUS {CONNECTING, DOWNLOADING, COMPLETED, FAILED}
    private STATUS currentStatus;
    private int bytesDownloaded = 0;
    private int fileSize = -1;
    private int attempt = 1;

    private String fileURL; // URL to the file to download
    private String saveDir; // Local directory to save file to
    private String fileName; // Filename of file to download, determined automatically

    private Task downloadTask;

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

        downloadFile();
    }

    /**
     * Helper method that performs actual download, on a new thread.
     * @throws IOException
     */
    public void downloadFile() throws IOException
    {
        downloadTask = new Task()
        {
            @Override
            protected Object call() throws Exception
            {
                // open connection and check if it's ok
                setCurrentStatus(STATUS.CONNECTING);
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

                    // get more information about the file to be downloaded
                    String contentType = connection.getContentType();
                    fileSize = connection.getContentLength();

                    if (fileSize < 1)
                    {
                        setCurrentStatus(STATUS.FAILED);
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
                    setCurrentStatus(STATUS.DOWNLOADING);
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
                    setCurrentStatus(STATUS.COMPLETED);

                    System.out.println("File downloaded");
                }
                else
                {
                    System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                }
                connection.disconnect();
                return true;
            }
        };
        new Thread(downloadTask).start();
    }

    public int getFileSize()
    {
        return fileSize;
    }

    public String getFileName()
    {
        return fileName;
    }

    /**
     * Notify observers each time the currentStatus is changed.
     * @param currentStatus new status of the downkoad
     */
    private void setCurrentStatus(STATUS currentStatus)
    {
        this.currentStatus = currentStatus;
        stateChanged();
    }

    public STATUS getCurrentStatus()
    {
        return currentStatus;
    }

    public int getAttempt()
    {
        return attempt;
    }

    public void setAttempt(int attempt)
    {
        this.attempt = attempt;
    }

    public Task getDownloadTask()
    {
        return downloadTask;
    }

    /**
     * Notify observers that this download's status has changed.
     */
    private void stateChanged()
    {
        setChanged();
        notifyObservers(this);
    }
}