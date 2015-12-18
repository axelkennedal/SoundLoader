package com.company;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application
{
    Person model = new Person("", 0);
    GridPane grid;

    public static void main(String[] args)
    {
	    launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        setupGUIComponents();

        Scene scene = new Scene(grid, 200, 100);
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread()
        {
            @Override
            public void run()
            {
                changeModel();
            }
        }.start();
    }

    private void setupGUIComponents()
    {
        Label firstnameLabel = new Label("");
        firstnameLabel.textProperty().bind(model.firstNameProperty());
        grid.add(firstnameLabel, 0, 0);

        Label ageLabel = new Label("");
        ageLabel.textProperty().bind(model.ageProperty());
        grid.add(ageLabel, 1, 0);
    }

    private void changeModel()
    {
        for (int i = 0; i < 10; i++)
        {
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            model.setAge(i);
            model.setFirstName("Dude " + 1);
        }
    }
}


class Person
{
    SimpleStringProperty firstName;
    SimpleIntegerProperty age;

    Person(String firstname, int age)
    {
        this.firstName = new SimpleStringProperty(firstname);
        this.age = new SimpleIntegerProperty(age);
    }

    public int getAge()
    {
        return age.get();
    }

    public SimpleStringProperty ageProperty()
    {
        return new SimpleStringProperty(age.get() + "");
    }

    public void setAge(int age)
    {
        this.age.set(age);
    }

    public String getFirstName()
    {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName.set(firstName);
    }
}