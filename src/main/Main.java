import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

/**
 * Application that takes a youtube URL and downloads the MP3 of the video.
 * @author Axel Kennedal
 * @version 0.7
 */
public class Main extends Application implements Observer
{
    public static void main(String[] args)
    {
        launch();
    }

    static Stage mainWindow;
    private static GridPane grid;
    DownloadManager downloadManager;
    Text status;
    ProgressIndicator progressIndicator;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        mainWindow = primaryStage;
        initLayout();
        setupComponents();
        downloadManager = new DownloadManager();
        downloadManager.addObserver(this);

        Scene scene = new Scene(grid, 500, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SoundLoader");
        primaryStage.show();
    }

    public void initLayout()
    {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); grid.setVgap(20);
    }

    public void setupComponents()
    {
        Text saveLocation = new Text("Saving to: ");
        grid.add(saveLocation, 0, 0, 2, 1);

        Button locationSelect = new Button("Select Download Destination");
        grid.add(locationSelect, 2, 0);
        locationSelect.setOnAction(event -> {
            DirectoryChooser locationSelector = new DirectoryChooser();
            downloadManager.setSaveDir(locationSelector.showDialog(mainWindow).toString());
            saveLocation.setText("Saving to: " + downloadManager.getSaveDir());
        });

        TextField linkField = new TextField();
        grid.add(linkField, 0, 1, 2, 1);

        Button download = new Button("Download");
        grid.add(download, 2, 1);
        download.setOnAction(event -> {
            downloadManager.startNewYoutubeDownload(linkField.getText(), progressIndicator);
        });

        status = new Text("Idle");
        grid.add(status, 0, 2, 1, 1);

        progressIndicator = new ProgressIndicator(0.0);
        progressIndicator
        grid.add(progressIndicator, 1, 2);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (o instanceof DownloadManager && arg instanceof HttpDownload)
        {
            HttpDownload notifier = (HttpDownload) arg;
            HttpDownload.STATUS newStatus = notifier.getCurrentStatus();
            switch (newStatus)
            {
                case CONNECTING:
                    status.setFill(Color.GRAY);
                    break;
                case DOWNLOADING:
                    status.setFill(Color.BLUE);
                    break;
                case COMPLETED:
                    status.setFill(Color.FORESTGREEN);
                    break;
                case FAILED:
                    status.setFill(Color.RED);
            }
            String statusString = newStatus.name();
            status.setText(statusString);
        }
    }
}
