package SoundLoader.View;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;


/**
 * @author Axel Kennedal
 * @version 1.1
 * Created on 2015-12-21.
 */
public class FlexibleTableView
{
    private TableView tableView;
    private StackPane container;

    /**
     * Create a TableView that resizes as you'd actually expect in a GridPane.
     * @param padding how much space to have around the tableview
     */
    public FlexibleTableView(Insets padding)
    {
        tableView = new TableView();
        container = new StackPane(tableView);
        container.setPadding(padding);

        // resize tableview and container to fit parent
        GridPane.setHgrow(tableView, Priority.ALWAYS);
        GridPane.setVgrow(tableView, Priority.ALWAYS);

        GridPane.setHgrow(container, Priority.ALWAYS);
        GridPane.setVgrow(container, Priority.ALWAYS);

        // resize column width depending on their content
        //tableView.setColumnResizePolicy((param -> true));
    }

    public FlexibleTableView()
    {
        new FlexibleTableView(new Insets(0,0,0,0));
    }

    /**
     * Set the columns of this table and their widths using percentages.
     * @param tableColumns the columns to use for the table
     * @param percentageWidths the percentages of the total table width each column should use
     */
    public void setColumns(TableColumn[] tableColumns, double[] percentageWidths)
    {
        if (tableColumns.length != percentageWidths.length)
        {
            throw new IllegalArgumentException("The number of columns and percentagewidths must match");
        }

        for (int i = 0; i < tableColumns.length; i++)
        {
            tableView.getColumns().add(tableColumns[i]);
            tableColumns[i].prefWidthProperty().bind(
                    tableView.widthProperty().multiply(percentageWidths[i])
            );
        }
    }

    /**
     * Set the data that is to be displayed by this table.
     * @param data
     */
    public void setData(ObservableList data)
    {
        tableView.setItems(data);
    }

    public ObservableList getData()
    {
        return tableView.getItems();
    }

    public TableView getTableView()
    {
        return tableView;
    }

    public StackPane getContainer()
    {
        return container;
    }
}
