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

    UIManager uiManager;
    DownloadManager downloadManager;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        downloadManager = new DownloadManager();
        uiManager = new UIManager(this, primaryStage);
    }
}
