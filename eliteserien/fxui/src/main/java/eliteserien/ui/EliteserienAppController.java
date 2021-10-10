package eliteserien.ui;

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
 * filename: contains json-filename.
 * tableText: contains the Table object as textarea.
 * 
 * Methods:
 * 
 * getInitialTable: return a table object based on data
 * collected from the json-file found in resource folder. 
 * 
 * getSavedTable: return a table object based on data
 * collected from the json-file in user.home folder. 
 * If json-file is not found, the method will return 
 * the table form initialTable-method.
 * 
 * SaveTable: saves the Table Object as json-file in user.home folder. 
 * 
 * Getters and setters for table object.
 * Methods for listeners (not used in this stage).
 * setTableText method sets the table as a text using the toString method in Table class.
 * updateView sets tableText for now.
 * 
 * Initialize method: 
 * use setTable method with input getInitialTable.
 * The setTable method also calls the updateView method so 
 * that the TextArea attribute contains the initialTable.
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
            return table;
        } catch (IOException e) {
            System.err.println("Could not read saved table");
        }
        return getInitialTable();
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