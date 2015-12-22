package SoundLoader.View;

import SoundLoader.Controller.Main;
import SoundLoader.Model.HttpDownload;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Creates and manages everything UI.
 * @author Axel Kennedal
 * @version 2.2
 * Created on 2015-12-20.
 */
public class UIManager
{
    // keep a pointer back to the main class so this class can communicate with others
    Main mainApplicationClass;

    final static int MIN_WIDTH = 400; final static int MAX_WIDTH = 500;
    final static int MIN_HEIGHT = 450; final static int MAX_HEIGHT = 600;

    Stage mainWindow;
    GridPane layoutGrid;

    Button downloadButton;
    Button savingToButton;
    Label savingToLabel;
    Label downloadsLabel;

    private FlexibleTableView flexibleTableView;

    /**
     * Main constructor, creates a graphical window and UI and displays it.
     * @param primaryStage
     */
    public UIManager(Main mainApplicationClass, Stage primaryStage)
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

        Scene mainScene = new Scene(layoutGrid, MIN_WIDTH, MIN_HEIGHT);
        mainWindow.setMaxWidth(MAX_WIDTH); mainWindow.setMinWidth(MIN_WIDTH);
        mainWindow.setMaxHeight(MAX_HEIGHT); mainWindow.setMinHeight(MIN_HEIGHT);

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
        layoutGrid.setAlignment(Pos.TOP_CENTER);
        layoutGrid.setHgap(10); layoutGrid.setVgap(10);
        layoutGrid.setPadding(new Insets(20, 10, 10, 10));
    }

    /**
     * Creates and places the GUI-components in a
     * three-column vertical layout.
     */
    private void createAndPlaceComponents()
    {
        Label clipboardLabel = new Label(
                "Copy a Youtube link (cmd-c or ctrl-c) " +
                "and then click Download");
        clipboardLabel.setWrapText(true);
        clipboardLabel.setTextAlignment(TextAlignment.CENTER);
        clipboardLabel.setMinHeight(40);
        layoutGrid.add(clipboardLabel, 0, 0, 3, 1);

        downloadButton = new Button("Download");
        layoutGrid.add(downloadButton, 1, 1);

        savingToButton = new Button("Saving to:");
        layoutGrid.add(savingToButton, 0, 2);

        savingToLabel = new Label("<- Click to choose location");
        layoutGrid.add(savingToLabel, 1 ,2 ,2, 1);

        downloadsLabel = new Label("Downloads");
        layoutGrid.add(downloadsLabel, 1, 3);

        createTable();
        layoutGrid.add(flexibleTableView.getContainer(), 0, 4, 3, 1);

    }

    private void createTable()
    {
        flexibleTableView = new FlexibleTableView(new Insets(0,0,0,0));

        TableColumn<HttpDownload, String> trackNameColumn = new TableColumn("Track Name");
        TableColumn<HttpDownload, String> statusColumn = new TableColumn("Status");
        TableColumn<HttpDownload, Double> progressColumn = new TableColumn("Progress");

        // connect columns with properties of the Task of instances of HttpDownload
        progressColumn.setCellValueFactory(new PropertyValueFactory<HttpDownload, Double>("progress"));
        progressColumn.setCellFactory(ProgressBarTableCell.<HttpDownload> forTableColumn());

        statusColumn.setCellValueFactory(new PropertyValueFactory<HttpDownload, String>("message"));

        ObservableList<HttpDownload> data = FXCollections.observableArrayList();

        trackNameColumn.setCellValueFactory(new PropertyValueFactory<HttpDownload, String>("trackName"));

        flexibleTableView.setColumns(
                new TableColumn[]{trackNameColumn, statusColumn, progressColumn},
                new double[]{0.6, 0.2, 0.2}
        );
    }

    /**
     * Defines how to handle user interaction with the GUI-components.
     * Should delegate as much logic as possible to controller classes.
     */
    private void setEventHandlers()
    {
        downloadButton.setOnAction(event -> startNewDownloadWithClipboardURL());

        savingToButton.setOnAction(event -> {
            DirectoryChooser savingToDirectoryChooser = new DirectoryChooser();
            File saveDirectory = savingToDirectoryChooser.showDialog(mainWindow);
            if (saveDirectory != null)
            {
                mainApplicationClass.getDownloadManager().setSaveDir(saveDirectory.toString());
                savingToLabel.setText(mainApplicationClass.getDownloadManager().getSaveDir());
            }
        });
    }

    /**
     * Attempts to get the URL currently stored in the system clipboard and
     * uses it to initiate a download.
     */
    private void startNewDownloadWithClipboardURL()
    {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString())
        {
            String clipBoardString = clipboard.getString().trim();
            mainApplicationClass.getDownloadManager().startNewYoutubeDownload(clipBoardString);
        }
    }

    public FlexibleTableView getFlexibleTableView()
    {
        return flexibleTableView;
    }
}
