package SoundLoader.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Class to create and handle everything related to the menu (menubar).
 * @author Axel Kennedal
 * @version 1.0
 * Created on 2015-12-27.
 */
public class ApplicationMenu
{
    Stage mainWindow;

    public ApplicationMenu(Stage owner)
    {
        this.mainWindow = owner;
    }

    public MenuBar createApplicationMenu()
    {
        Menu aboutMenu = new Menu("About");
        MenuItem aboutItem = new MenuItem("About this application...");
        aboutItem.setOnAction(event ->
        {
            displayAboutWindow();
        });
        aboutMenu.getItems().addAll(aboutItem);


        MenuBar menuBar = new MenuBar(aboutMenu);
        menuBar.setUseSystemMenuBar(true);
        return menuBar;
    }

    private void displayAboutWindow()
    {
        Stage stage = new Stage();
        int width = 300;
        int height = 300;
        stage.setMinHeight(height); stage.setMaxHeight(height);
        stage.setMinWidth(width); stage.setMaxWidth(width);

        stage.setTitle("About SoundLoader");

        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);
        grid.setAlignment(Pos.CENTER);
        int spacing = 20;
        grid.setVgap(spacing);
        grid.setPadding(new Insets(spacing, spacing, spacing, spacing));

        Text soundLoaderHeader = new Text("SoundLoader");
        soundLoaderHeader.setTextAlignment(TextAlignment.CENTER);
        grid.add(soundLoaderHeader, 0, 0);

        Text authorText = new Text("Created by Axel Kennedal");
        authorText.setTextAlignment(TextAlignment.CENTER);
        grid.add(authorText, 0, 1);

        Text versionText = new Text("Version 1.0");
        versionText.setTextAlignment(TextAlignment.CENTER);
        grid.add(versionText, 0, 2);

        Text gitHubText = new Text("This application was crafted with love in JavaFX. " +
        "Source code is available at https://github.com/axelkennedal/SoundLoader, feel " +
        "free to contribute and add improvements/bug fixes!");
        gitHubText.setTextAlignment(TextAlignment.CENTER);
        gitHubText.setWrappingWidth(width - 2*spacing);
        grid.add(gitHubText, 0, 3);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
}
