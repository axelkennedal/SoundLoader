package com.company;

import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Test application for ID3TagManager.
 * @author Axel Kennedal
 * @version 1.0
 */
public class Main extends Application
{
    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.show();

        File fileToParse = new FileChooser().showOpenDialog(primaryStage);
        ID3TagManager.setID3TagsAndNewFilenameFromFilename(fileToParse);

        //ID3TagManager.processAllInDirectory(new DirectoryChooser().showDialog(primaryStage), true);
    }
}