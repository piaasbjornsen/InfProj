package eliteserien.ui;

import eliteserien.core.TableListener;
import eliteserien.core.Team;

import java.io.IOException;

import eliteserien.core.Table;
import eliteserien.json.TablePersistence;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


/**
 * Controller class
 * 
 * Attributes: 
 * Tablepersistence object for reading and writing to json-file.
 * Table object made by collecting data from the json-file. 
 * 
 * FXML attributes:
 * userTablePath: contains json-filename.
 * tableText: contains the Table object as text.
 * 
 * Methods:
 * getInitialTable: initializes the table object and adding teams based on data
 * collected from the json-file (using the persistence object).
 * Getters and setters for table object.
 * Methods for listeners (not used in this stage).
 * setTableText method sets the table as a text using the toString method in Table class.
 * updateView sets tableText for now.
 * 
 * Initialize method: 
 * makes a TablePersistence object and uses this to get data from json-file.
 * Makes the Table object using the data collected.
 * Updates view.
 * 
 */


public class EliteserienAppController {

    @FXML
    String fileName;

    @FXML
    TextArea tableText;

    private TablePersistence tablePersistence = new TablePersistence();
    private Table table;

    private Table getInitialTable() {
        Table table = null;
        try {
            table = tablePersistence.loadInitialTable(fileName);
        } catch (IOException e) {
            System.err.println("Could not read initial table");
        }
        return table;
    }

    public Table getSavedTable() {
        Table table = null;
        try {
            table = tablePersistence.loadSavedTable(fileName);
        } catch (IOException e) {
            System.err.println("Could not read saved table");
        }
        return table;
    } 

    public void saveTable() {
        try {
            tablePersistence.saveTable(table, fileName);
        } catch (IOException e) {
            System.err.println("Could not save Table");
        }
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
        updateView();
    }

    protected void setTableText() {
        tableText.setText(table.toString());
    }

    protected void updateView() {
        setTableText();
    }

    @FXML
    void initialize() {
        setTable(getInitialTable());
    }
}