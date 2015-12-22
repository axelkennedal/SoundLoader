package SoundLoader.Controller;

import SoundLoader.View.UIManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Application that takes a youtube URL and downloads the MP3 of the video.
 * @author Axel Kennedal
 * @version 1.0
 */
public class Main extends Application
{
    public static void main(String[] args)
    {
        launch();
    }

    private UIManager uiManager;
    private DownloadManager downloadManager;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        downloadManager = new DownloadManager(this);
        uiManager = new UIManager(this, primaryStage);
    }

    public UIManager getUiManager()
    {
        return uiManager;
    }

    public DownloadManager getDownloadManager()
    {
        return downloadManager;
    }
}
