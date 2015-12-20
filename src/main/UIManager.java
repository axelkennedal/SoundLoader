import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * Creates and manages everything UI.
 * @author Axel Kennedal
 * @version 1.0
 * Created on 2015-12-20.
 */
public class UIManager implements Observer
{
    // keep a pointer back to the main class so this class can communicate with others
    Main mainApplicationClass;

    final static int WIDTH = 300;
    final static int HEIGHT = 450;

    Stage mainWindow;
    GridPane layoutGrid;

    TextField linkField;
    Button downloadButton;
    Button savingToButton;
    Label savingToLabel;
    Label downloadsLabel;

    Label statusLabel;
    ProgressIndicator progressIndicator;

    /**
     * Main constructor, creates a graphical window and UI and displays it.
     * @param primaryStage
     */
    UIManager(Main mainApplicationClass, Stage primaryStage)
    {
        this.mainApplicationClass = mainApplicationClass;
        mainWindow = primaryStage;
        prepareLayout(false);
        prepareMainWindow();
        createAndPlaceComponents();
        setEventHandlers();

        mainWindow.show();
    }

    private void prepareMainWindow()
    {
        mainWindow.setTitle("SoundLoader");

        Scene mainScene = new Scene(layoutGrid, WIDTH, HEIGHT);
        mainWindow.setMaxWidth(WIDTH); mainWindow.setMinWidth(WIDTH);
        mainWindow.setMaxHeight(HEIGHT); mainWindow.setMinHeight(HEIGHT);

        mainWindow.setScene(mainScene);
    }

    /**
     * Prepares the layout by setting margins etc. Has to be called before prepareMainWindow().
     * @param debug true if debugging, for extra visual information.
     */
    private void prepareLayout(boolean debug)
    {
        layoutGrid = new GridPane();
        layoutGrid.setGridLinesVisible(debug);
        layoutGrid.setAlignment(Pos.CENTER);
        layoutGrid.setHgap(10); layoutGrid.setVgap(10);
    }

    /**
     * Creates and places the GUI-components in a
     * three-column vertical layout.
     */
    private void createAndPlaceComponents()
    {
        linkField = new TextField();
        layoutGrid.add(linkField, 0, 0, 2, 1);

        downloadButton = new Button("Download");
        layoutGrid.add(downloadButton, 2, 0);

        savingToButton = new Button("Saving to:");
        layoutGrid.add(savingToButton, 0, 1);

        savingToLabel = new Label("<- Click to choose location");
        layoutGrid.add(savingToLabel, 1 ,1 ,2, 1);

        downloadsLabel = new Label("Downloads");
        layoutGrid.add(downloadsLabel, 1, 2);

        statusLabel = new Label("IDLE");
        layoutGrid.add(statusLabel, 0, 3);

        progressIndicator = new ProgressIndicator(0);
        layoutGrid.add(progressIndicator, 1, 3);
    }

    /**
     * Defines how to handle user interaction with the GUI-components.
     * Should delegate as much logic as possible to controller classes.
     */
    private void setEventHandlers()
    {
        downloadButton.setOnAction(event ->
                progressIndicator.progressProperty().bind(
                        mainApplicationClass.downloadManager.startNewYoutubeDownload(linkField.getText())
                                .downloadTask.progressProperty()
                ));

        savingToButton.setOnAction(event -> {
            DirectoryChooser savingToDirectoryChooser = new DirectoryChooser();
            File saveDirectory = savingToDirectoryChooser.showDialog(mainWindow);
            if (saveDirectory != null)
            {
                mainApplicationClass.downloadManager.setSaveDir(saveDirectory.toString());
                savingToLabel.setText(mainApplicationClass.downloadManager.getSaveDir());
            }
        });
    }

    /**
     * Handle updates received from Observable objects this is listening to.
     * @param o The notifier
     * @param arg Data about the update
     */
    @Override
    public void update(Observable o, Object arg)
    {
        if (o instanceof DownloadManager && arg instanceof HttpDownload)
        {
            handleDownloadStatusUpdate((HttpDownload) arg);
        }
    }

    private void handleDownloadStatusUpdate(HttpDownload notifier)
    {
        Color newColor = null;
        switch (notifier.getCurrentStatus())
        {
            case CONNECTING:
                newColor = Color.GRAY;
                break;
            case DOWNLOADING:
                newColor = Color.BLUE;
                break;
            case COMPLETED:
                newColor = Color.FORESTGREEN;
                break;
            case FAILED:
                newColor = Color.RED;
                break;
        }
        statusLabel.setTextFill(newColor);
        statusLabel.setText(notifier.getCurrentStatus().name());
    }
}
