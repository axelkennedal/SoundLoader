package com.company;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application
{

    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        GridPane grid = new GridPane();

        createTable(grid);

        Label downloadsLabel = new Label("Downloads");
        grid.add(downloadsLabel, 0, 0);

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.setHeight(400); primaryStage.setWidth(325);
        primaryStage.show();
    }

    private void createTable(GridPane grid)
    {
        // create flexible table
        FlexibleTableView flexibleTableView = new FlexibleTableView(new Insets(20, 20, 20, 20));

        // create columns
        TableColumn trackNameColumn = new TableColumn("Track Name");
        TableColumn artistNameColumn = new TableColumn("Artist");
        TableColumn lengthColumn = new TableColumn("Length");

        // create/get data
        ObservableList<Download> data = FXCollections.observableArrayList(
                new Download("Universe", "Axwell", "5:34"),
                new Download("Summer", "Calvin Harris", "4:12"),
                new Download("Fade Into Darkness", "Avicii", "4:52"),
                new Download("Colors", "Hardwell", "3:26")
        );

        // associate data with columns
        trackNameColumn.setCellValueFactory(new PropertyValueFactory<>("trackName"));
        artistNameColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        // associate data with tableview
        flexibleTableView.setData(data);

        flexibleTableView.setColumns(
                new TableColumn[]{trackNameColumn, artistNameColumn, lengthColumn},
                new double[]{0.4, 0.4, 0.2}
        );

        grid.add(flexibleTableView.getContainer(), 0, 1);
    }
}


