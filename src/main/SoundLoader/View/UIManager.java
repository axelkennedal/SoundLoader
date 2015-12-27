package SoundLoader.View;

import SoundLoader.Controller.Main;
import SoundLoader.Model.HttpDownload;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;

/**
 * Creates and manages everything UI.
 * @author Axel Kennedal
 * @version 3.0
 * Created on 2015-12-20.
 */
public class UIManager
{
    // keep a pointer back to the main class so this class can communicate with others
    Main mainApplicationClass;

    final static int MIN_WIDTH = 525; final static int MAX_WIDTH = 600;
    final static int MIN_HEIGHT = 450; final static int MAX_HEIGHT = 1000;

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

        mainScene.getStylesheets().add(UIManager.class.getResource("style.css").toExternalForm());

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
        layoutGrid.setHgap(20); layoutGrid.setVgap(10);
        layoutGrid.setPadding(new Insets(10, 10, 10, 10));

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(70);
        layoutGrid.getColumnConstraints().addAll(column1, column2);
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
        //clipboardLabel.setId("clipboardLabel");
        clipboardLabel.setWrapText(true);
        clipboardLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(clipboardLabel, HPos.CENTER);
        clipboardLabel.setMinHeight(40);
        layoutGrid.add(clipboardLabel, 0, 0, 2, 1);

        downloadButton = new Button("Download");
        downloadButton.setId("downloadButton");
        downloadButton.setMaxWidth(10000);
        downloadButton.setMinHeight(60);
        layoutGrid.add(downloadButton, 0, 1, 2, 1);

        savingToButton = new Button("Saving to:");
        layoutGrid.add(savingToButton, 0, 2);

        savingToLabel = new Label("<- Click to choose location");
        savingToLabel.setTextAlignment(TextAlignment.RIGHT);
        GridPane.setHalignment(savingToLabel, HPos.RIGHT);
        savingToLabel.setTextOverrun(OverrunStyle.LEADING_WORD_ELLIPSIS);
        layoutGrid.add(savingToLabel, 1 ,2);

        downloadsLabel = new Label("Downloads");
        downloadsLabel.getStyleClass().add("bigLabel");
        downloadsLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(downloadsLabel, HPos.CENTER);
        layoutGrid.add(downloadsLabel, 0, 3, 2, 1);

        createTable();
        layoutGrid.add(flexibleTableView.getContainer(), 0, 4, 2, 1);

    }

    private void createTable()
    {
        flexibleTableView = new FlexibleTableView(new Insets(10,10,10,10));
        flexibleTableView.getContainer().setId("tableContainer");
        flexibleTableView.getTableView().setId("table");
        flexibleTableView.getTableView().setMouseTransparent(true);

        TableColumn<HttpDownload, String> trackNameColumn = new TableColumn("Track Name");
        TableColumn<HttpDownload, String> statusColumn = new TableColumn("Status");
        TableColumn<HttpDownload, Double> progressColumn = new TableColumn("Progress");

        // connect columns with properties of the Task of instances of HttpDownload
        progressColumn.setCellValueFactory(new PropertyValueFactory<HttpDownload, Double>("progress"));
        progressColumn.setCellFactory(new Callback<TableColumn<HttpDownload, Double>, TableCell<HttpDownload, Double>>()
        {
            @Override
            public TableCell<HttpDownload, Double> call(TableColumn<HttpDownload, Double> param)
            {
                return new TableCell<HttpDownload, Double>()
                {
                    ProgressBar indicator;
                    @Override
                    protected void updateItem(Double item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (!isEmpty())
                        {
                            this.setAlignment(Pos.CENTER);
                            if (item == 0) item = 0.001;

                            indicator = new ProgressBar(item);
                            if (item == 1.0)
                            {
                                indicator.setStyle("-fx-accent: limegreen;");
                            }
                            else if (item == -1.0)
                            {
                                indicator.setStyle("-fx-accent: red");
                            }
                            else
                            {
                                indicator.setStyle("-fx-accent: #00B4FF");
                            }

                            setGraphic(indicator);
                        }
                    }
                };
            }
        });

        statusColumn.setCellValueFactory(new PropertyValueFactory<HttpDownload, String>("message"));
        statusColumn.setCellFactory(new Callback<TableColumn<HttpDownload, String>, TableCell<HttpDownload, String>>()
        {
            @Override
            public TableCell<HttpDownload, String> call(TableColumn<HttpDownload, String> param)
            {
                return new TableCell<HttpDownload, String>()
                {
                    Text text;
                    @Override
                    protected void updateItem(String item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (!isEmpty())
                        {
                            this.setAlignment(Pos.CENTER);
                            text = new Text(item);
                            text.getStyleClass().add("text");
                            setGraphic(text);
                        }
                    }
                };
            }
        });

        trackNameColumn.setCellValueFactory(new PropertyValueFactory<HttpDownload, String>("trackName"));
        trackNameColumn.setCellFactory(new Callback<TableColumn<HttpDownload, String>, TableCell<HttpDownload, String>>() {
            @Override
            public TableCell<HttpDownload, String> call(TableColumn<HttpDownload, String> arg0)
            {
                return new TableCell<HttpDownload, String>()
                {
                    Text text;
                    @Override
                    public void updateItem(String item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (!isEmpty())
                        {
                            this.setAlignment(Pos.CENTER_LEFT);
                            text = new Text(item);
                            text.wrappingWidthProperty().bind(trackNameColumn.widthProperty());
                            text.getStyleClass().add("text");
                            this.setWrapText(true);
                            setGraphic(text);
                        }
                    }
                };
            }
        });

        flexibleTableView.setColumns(
                new TableColumn[]{trackNameColumn, statusColumn, progressColumn},
                new double[]{0.5, 0.25, 0.25}
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
                savingToLabel.setText(mainApplicationClass.getDownloadManager().getSaveDir() + "/");
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
            if (mainApplicationClass.getDownloadManager().startNewYoutubeDownload(clipBoardString) == null)
            {
                Alert downloadProblem = new Alert(Alert.AlertType.ERROR);
                downloadProblem.setTitle("Could not start download");
                downloadProblem.setHeaderText(
                        "Make sure you have copied a link of the format\n"+
                                "\"https://www.youtube.com/watch?v=oavMtUWDBTM\"\n" +
                                "to your clipboard before clicking Download,\n" +
                                "and that you've chosen a directory to save to."
                );
                downloadProblem.show();
            }
        }
    }

    public FlexibleTableView getFlexibleTableView()
    {
        return flexibleTableView;
    }
}
