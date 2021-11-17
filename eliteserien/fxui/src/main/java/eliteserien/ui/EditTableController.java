package eliteserien.ui;

import java.io.IOException;

import eliteserien.core.Table;
import eliteserien.core.Team;
import eliteserien.json.TablePersistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditTableController {

    // FXML attributes:

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

    /**
     * Attributes: 
     * Tablepersistence object for reading and writing to json-file.
     * Table object made by collecting data from the json-file.
     * ObservableList object for tableView
    */ 

    private TablePersistence tablePersistence = new TablePersistence();
    private Table table;
    private ObservableList<TeamProperties> teams = FXCollections.observableArrayList(); 
    private String fileName;
    private EliteserienAppController appController;

    /**
     * Returns a table object based on data
     * collected from the json-file in user.home folder. 
     * If json-file is not found, the method will return 
     * the table form initialTable-method.
     * @return getInitialTable() which returns initialTable
    */

    public Table getSavedTable() {
        Table savedTable = null;
        try {
            savedTable = tablePersistence.loadSavedTable(fileName);
            return savedTable;
        } catch (IOException e) {
            System.err.println("Could not read saved table");
        }
        return new Table();
    }

    /**
     * Saves the Table Object as json-file in user.home folder. 
     * @throws "Could not save Table" if IOException
    */

    public void saveTable() {
        try {
            tablePersistence.saveTable(table, fileName);
        } catch (IOException e) {
            System.err.println("Could not save Table");
        }
    }

    /**
     * Adds all teams into a table object and returns this object
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
     * Sets table object
     * @param table
    */

    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * sets tableView using teams from teams-list.
     */

    protected void setTableView() {
        tableView.setItems(teams);
    }

        /**
     * Sets filename
     * @param fileName
    */
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

        /**
     * Sets AppController
     * makes it possible to update tableView in main window when saving edits.
     * @param controller
    */

    public void setAppController(EliteserienAppController controller) {
        this.appController = controller;
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
     * Adds selected team to table
     * Checks if points are valid
     * Add team to table
     * Updates tableview
     * Resets text fields
    */

    @FXML
    public void handleAddTeam(){
        errorMessageWindow.clear();
        if (!appController.checkPoints(editTeamPoints.getText())) {
            errorMessageWindow.setText("Invalid points");
        }
        Team team = new Team(editTeamName.getText(), Integer.parseInt(editTeamPoints.getText()));
        table.addTeams(team);
        editTeamName.clear();
        editTeamPoints.clear();
        saveTable();
        updateView();
    }

    /**
     * Add selected teams name and points
     * to the edit fields.
     * Clears selectedTeam-field.
     * Updates view.
    */

    @FXML
    public void handleEditTeam(){
        if(!selectedTeam.getText().isEmpty()){
            editTeamName.setText(selectedTeam.getText());
            for (Team team : table.getTeams()){
                if (selectedTeam.getText().equals(team.getName())){
                    editTeamPoints.setText(String.valueOf(team.getPoints()));
                    table.deleteTeam(team);
                }
            }
            updateView();
            selectedTeam.clear();
        }
    }

    /**
     * Deletes selected team from table
     * Updates table
     * Resets choice boxes and text fields
    */

    @FXML
    public void handleDeleteTeam(){
        if(!selectedTeam.getText().isEmpty()){
            for (Team team : table.getTeams()){
                if (selectedTeam.getText().equals(team.getName())){
                    table.deleteTeam(team);
                }
            }
            updateView();
            selectedTeam.clear();
        }
    }

    /**
     * Adds the selected team from table
     * to the selected team field. 
    */

    @FXML
    void handleSelectedTeamChanged(){
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
     * When user pushes save button, this method:
     * Clears old messages and input boxesand
     * Checks for input-errors:
        * Is points valid (positive)
        * Are the chosen teams two different teams?
     * Updates table
     * Resets choice boxes and text fields
    */

    @FXML
    public void handleSave(){
        saveTable();
        appController.handleLoad();
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        setFileName("Eliteserien.json");
        setTable(getSavedTable());
        updateTeamsList();
        try{
        teamsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("name"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("points"));
        updateView();
        } catch(Exception e){
            System.err.println("Something went wrong when opening edit Window.");
        }
    }
}
