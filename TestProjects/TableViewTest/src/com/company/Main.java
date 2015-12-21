package com.company;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
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
        FlexibleTableView flexibleTableView = new FlexibleTableView(new Insets(20, 20, 20, 20));

        TableColumn trackNameColumn = new TableColumn("Track Name");
        TableColumn artistNameColumn = new TableColumn("Artist");
        TableColumn lengthColumn = new TableColumn("Length");

        flexibleTableView.setColumns(
                new TableColumn[]{trackNameColumn, artistNameColumn, lengthColumn},
                new double[]{0.4, 0.4, 0.2}
        );

        grid.add(flexibleTableView.getContainer(), 0, 1);
    }
}
