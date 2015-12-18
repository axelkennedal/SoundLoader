import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Application that takes a youtube URL and downloads the MP3 of the video.
 * @author Axel Kennedal
 * @version 0.5
 */
public class Main extends Application
{
    static String converterURL = "http://www.youtubeinmp3.com/fetch/?video=";
    static File destinationFolder;
    static Stage mainWindow;

    public static void main(String[] args)
    {
        launch();
    }

    public static void downloadTrack(String link)
    {
        try
        {
            String downloadURL = converterURL + link;
            HttpDownloadUtility.downloadFile(downloadURL, destinationFolder.toString());
        }
        catch (Exception e)
        {

        }
    }

    private static GridPane grid;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        mainWindow = primaryStage;
        initLayout();
        setupGUI();

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

    public void setupGUI()
    {
        Text saveLocation = new Text("Saving to: ");
        grid.add(saveLocation, 0, 0, 2, 1);

        Button locationSelect = new Button("Select Download Location");
        grid.add(locationSelect, 2, 0);
        locationSelect.setOnAction(event -> {
            DirectoryChooser locationSelector = new DirectoryChooser();
            destinationFolder = locationSelector.showDialog(mainWindow);
            saveLocation.setText("Saving to: " + destinationFolder.toString());
        });

        TextField linkField = new TextField();
        grid.add(linkField, 0, 1, 2, 1);

        Button download = new Button("Download");
        grid.add(download, 2, 1);
        download.setOnAction(event -> downloadTrack(linkField.getText()));
    }
}
