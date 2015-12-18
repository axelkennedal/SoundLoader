package com.company;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
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
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 200, 200);

        ProgressIndicator progressIndicator = new ProgressIndicator(0.0);
        root.add(progressIndicator, 0, 0);

        Task downloadWorker = createDownloadWorker();
        progressIndicator.progressProperty().bind(downloadWorker.progressProperty());
        new Thread(downloadWorker).start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Task createDownloadWorker()
    {
        return new Task()
        {
            @Override
            protected Object call() throws Exception
            {
                for (int i = 0; i < 10; i++)
                {
                    Thread.sleep(2000);
                    updateProgress(i+1, 10);
                }
                return true;
            }
        };
    }
}
