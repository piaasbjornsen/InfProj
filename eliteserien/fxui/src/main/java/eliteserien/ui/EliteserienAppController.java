package eliteserien.ui;

import java.io.IOException;

import eliteserien.core.Table;
import eliteserien.core.Team;
import eliteserien.json.TablePersistence;
import javafx.fxml.FXML;
import javafx.util.Callback;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * Controller class
 * 
 * Attributes: 
 * Tablepersistence object for reading and writing to json-file.
 * Table object made by collecting data from the json-file. 
 * 
 * FXML attributes:
 * filename: contains json-filename.
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
 * setTableView method puts all teams in to the TableView area.
 * updateView sets tableView for now.
 * 
 * Initialize method: 
 * use setTable method with input getInitialTable.
 * The setTable method also calls the updateView method so 
 * that the tableView attribute contains the initialTable.
 * For testing: adding 1 point to each team and saving result 
 * in json-file in user.home folder.
 * 
 */


public class EliteserienAppController {

    @FXML
    String fileName;

    @FXML
    private TableView<Team> tableView = new TableView<Team>();

    @FXML
    private TableColumn<Team, String> teamsColumn = new TableColumn<Team, String>();

    @FXML
    private TableColumn<Team, Integer> pointsColumn = new TableColumn<Team, Integer>();


    private TablePersistence tablePersistence = new TablePersistence();
    private Table table;
    
    private ObservableList<Team> teams = FXCollections.observableArrayList();

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

    protected void setTableView() {
        tableView.setItems(teams);
    }

    protected void updateView() {
        updateTeamsList();
        setTableView();
    }

    private void updateTeamsList() {
        teams.clear();
        for (Team team : table.getTeams()) {
            teams.add(team);
        }
    }

    public void addTeamPoints(Team team, int i) {
        team.addPoints(i);
        updateView();
    }

    @FXML
    void initialize() {
        teamsColumn.setCellValueFactory(new PropertyValueFactory<Team, String>("name"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<Team, Integer>("points"));
        setTable(getInitialTable());
        for (Team team : table.getTeams()) {
            addTeamPoints(team, 1);
        }
        saveTable();
    }
}