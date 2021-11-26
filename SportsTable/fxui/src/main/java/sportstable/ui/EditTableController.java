package sportstable.ui;

import java.io.IOException;
import sportstable.core.Table;
import sportstable.core.Team;
import sportstable.json.TablePersistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for edit table window. The controller is used in both
 * AppController and RemoteAppController.
 */

public class EditTableController {

    @FXML
    TableView<TeamProperties> tableView;

    @FXML
    TableColumn<TeamProperties, String> teamsColumn;

    @FXML
    TableColumn<TeamProperties, String> pointsColumn;

    @FXML
    TextField selectedTeam;

    @FXML
    TextField editTeamName;

    @FXML
    TextField editTeamPoints;

    @FXML
    TextField errorMessageWindow;

    private TablePersistence tablePersistence = new TablePersistence(); // Tablepersistence object for reading and
                                                                        // writing to json-file.
    private Table table; // Table object made by collecting data from the json-file.
    private ObservableList<TeamProperties> teams = FXCollections.observableArrayList(); // ObservableList object for
                                                                                        // tableView
    private String fileName;
    private LocalAppController appController; // AppController for local use
    private RemoteAppController remoteAppController; // RemoteAppController for use with extern server

    /**
     * @return true if remoteAppController exists
     */

    private boolean isRemote() {
        return remoteAppController != null;
    }

    /**
     * Saves the Table Object as json-file in user.home folder.
     */

    public void saveTable() {
        if (!isRemote()) {
            try {
                tablePersistence.saveTable(table, fileName);
            } catch (IOException e) {
                System.err.println("Could not save Table");
            }
        }
        if (isRemote()) {
            remoteAppController.setTable(table);
            remoteAppController.saveTable(table);
        }
    }

    /**
     * Adds all teams into a table object and returns this object
     * 
     * @return Table object
     */

    public Table getTable() {
        Table getTable = new Table();
        for (Team team : table.getTeams()) {
            getTable.addTeams(team);
        }
        return getTable;
    }

    /**
     * @param Table to set
     */

    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * Sets tableView using teams from teams-list.
     */

    protected void setTableView() {
        tableView.setItems(teams);
    }

    /**
     * @param fileName to set
     */

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Sets AppController makes it possible to update tableView in main window when
     * saving edits.
     * 
     * @param LocalAppController to set
     */

    public void setAppController(LocalAppController controller) {
        this.appController = controller;
    }

    /**
     * @param remoteController to set
     */

    public void setRemoteAppController(RemoteAppController remoteController) {
        this.remoteAppController = remoteController;
    }

    /**
     * Updates the observable list of teams to match the teams in table
     */

    private void updateTeamsList() {
        teams.clear();
        for (Team team : table.getTeams()) {
            String name = team.getName();
            String points = Integer.toString(team.getPoints());
            teams.add(new TeamProperties(name, points));
        }
    }

    /**
     * Adds selected team to table. Checks if points are valid. Add team to table.
     * Updates tableview. Resets text fields.
     */

    @FXML
    public void handleAddTeam() {
        errorMessageWindow.clear();
        if (!isRemote()) {
            if (!appController.checkPoints(editTeamPoints.getText())) {
                errorMessageWindow.setText("Invalid points");
                return;
            }
        }
        if (isRemote()) {
            if (!remoteAppController.checkPoints(editTeamPoints.getText())) {
                errorMessageWindow.setText("Invalid points");
                return;
            }
        }
        Team team = new Team(editTeamName.getText(), Integer.parseInt(editTeamPoints.getText()));
        table.addTeams(team);
        editTeamName.clear();
        editTeamPoints.clear();
        saveTable();
        updateView();
    }

    /**
     * Add selected teams name and points to the edit fields. Clears
     * selectedTeam-field. Updates view.
     */

    @FXML
    public void handleEditTeam() {
        if (!selectedTeam.getText().isEmpty()) {
            editTeamName.setText(selectedTeam.getText());
            for (Team team : table.getTeams()) {
                if (selectedTeam.getText().equals(team.getName())) {
                    editTeamPoints.setText(String.valueOf(team.getPoints()));
                    table.deleteTeam(team);
                }
            }
            updateView();
            selectedTeam.clear();
        }
    }

    /**
     * Deletes selected team from table. Updates table. Resets choice boxes and text
     * fields.
     */

    @FXML
    public void handleDeleteTeam() {
        if (!selectedTeam.getText().isEmpty()) {
            for (Team team : table.getTeams()) {
                if (selectedTeam.getText().equals(team.getName())) {
                    table.deleteTeam(team);
                }
            }
            saveTable();
            updateView();
            selectedTeam.clear();
        }
    }

    /**
     * Adds the selected team from table to the selected team field.
     */

    @FXML
    void handleSelectedTeamChanged() {
        selectedTeam.setText(tableView.getSelectionModel().getSelectedItem().getName());
    }

    /**
     * Updates teams list and sets table view.
     */

    protected void updateView() {
        updateTeamsList();
        setTableView();
    }

    /**
     * When user pushes save button, this method clears old messages and input
     * boxes, checks for input-errors (are points valid (positive) and are the
     * chosen teams two different teams?), updates table and resets choice boxes and
     * text fields
     */

    @FXML
    public void handleSave() {
        saveTable();
        if (!isRemote()) {
            appController.setTable(this.table);
            appController.updateView();
        }
        if (isRemote()) {
            remoteAppController.setTable(this.table);
            remoteAppController.updateView();
        }
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets fileName and Table to initial values. Gets updated through the editTable
     * method in main controller (either AppControler or Remotecontroller, depending
     * if we are running locally or externally).
     */

    @FXML
    public void initialize() {
        setFileName("Eliteserien.json");
        setTable(new Table(new Team("Initialize", 1)));
        updateTeamsList();
        try {
            teamsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("name"));
            pointsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("points"));
            updateView();
        } catch (Exception e) {
            System.err.println("Something went wrong when opening edit Window.");
        }
    }
}
