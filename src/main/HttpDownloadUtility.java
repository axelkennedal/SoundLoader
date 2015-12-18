import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A utility that downloads a file from a URL. Performs download on a new Thread.
 * @author www.codejava.net & Axel Kennedal
 * @version 1.2
 */
class HttpDownloadUtility implements Runnable
{
    private static final int BUFFER_SIZE = 4096;
    private String fileURL;
    private String saveDir;

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

        Thread downloadThread = new Thread(this);
        downloadThread.run();
    }

    /**
     * Helper method that performs actual download.
     * @throws IOException
     */
    private void downloadFile() throws IOException {

        // open connection and check if it's ok
        URL url = new URL(fileURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int responseCode = connection.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            // figure out the filename of the file to be downloaded
            String fileName = "";
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
            int contentLength = connection.getContentLength();

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);

            // opens input stream from the HTTP connection
            InputStream inputStream = connection.getInputStream();

            // prepare to save the file
            String saveFilePath = saveDir + File.separator + fileName;
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            // perform actual download
            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1)
            {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("File downloaded");
        }
        else
        {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        connection.disconnect();
    }

    /**
     * Perform download in a new Thread.
     */
    @Override
    public void run()
    {
        try
        {
            downloadFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}