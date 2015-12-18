package com.company;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
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
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 200, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
