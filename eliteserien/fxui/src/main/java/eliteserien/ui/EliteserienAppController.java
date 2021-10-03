package eliteserien.ui;

import eliteserien.core.TableListener;
import eliteserien.core.SoccerTeam;
import eliteserien.core.Table;
import eliteserien.json.TablePersistence;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.util.Callback;

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
 * getInitialTable: initializes the table object and adding soccerteams based on data
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
    String userTablePath;

    @FXML
    Text tableText;

    private TablePersistence tablePersistence;
    private Table table;

    private Table getInitialTable() {
        Table table = null;
        try {
            table = tablePersistence.loadTable();
        } catch (Exception ioex) {
            System.err.println("Could not read saved table");
            ioex.printStackTrace();
        }
        return table;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        if (this.table != null) {
            this.table.removeTableListener(listener);
        }
        this.table = table;
        updateView();
        if (this.table != null) {
            this.table.addTableListener(listener);
        }
    }
    
    private Callback<Table, Void> onTableChanged = null;

    public void setOnTableChanged(Callback<Table, Void> onTableChanged) {
        this.onTableChanged = onTableChanged;
    }

    private TableListener listener = table -> {
        if (onTableChanged != null) {
            onTableChanged.call(getTable());
        }
        updateView();
    };

    protected void setTableText() {
        String tableString = table.toString();
        tableText.setText(tableString);
    }

    protected void updateView() {
        setTableText();
    }

    @FXML
    void initialize() {
        this.tablePersistence = new TablePersistence();
        tablePersistence.setSaveFilePath(userTablePath.toString());
        this.table = getInitialTable();
        updateView();
    }
}

