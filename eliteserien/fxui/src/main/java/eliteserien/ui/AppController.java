package eliteserien.ui;

import java.io.IOException;

import eliteserien.core.Table;
import eliteserien.core.Team;
import eliteserien.json.TablePersistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller class
 */

public class AppController {

    // FXML attributes:

    @FXML
    TableView<TeamProperties> tableView; // TableView for Elitserien table

    @FXML
    TableColumn<TeamProperties, String> teamsColumn; // TableColumn for team names

    @FXML
    TableColumn<TeamProperties, String> pointsColumn; // TableColumn for team points

    @FXML
    ChoiceBox<String> home; // Home team

    @FXML
    ChoiceBox<String> away; // Away team

    @FXML
    TextField pointsH; // Points to home team

    @FXML
    TextField pointsA; // Points to away team

    @FXML
    Button saveButton; // Save-match button

    @FXML
    TextField message; // Text field for error messages to user

    @FXML
    TextField fileNameInput; // filename input from user

    @FXML
    Label tableName; // name of the currently viewed table

    @FXML
    String initialFileName;

    @FXML
    String editTable;

    @FXML
    String endpointUri;

    /**
     * Attributes: Tablepersistence object for reading and writing to json-file.
     * Table object made by collecting data from the json-file. ObservableList
     * object for tableView
     */

    private TablePersistence tablePersistence = new TablePersistence();
    private Table table;
    private ObservableList<TeamProperties> teams = FXCollections.observableArrayList();
    private String fileName;
    private EditTableController editTableController;

    /**
     * Returns a table object based on data collected from the json-file found in
     * resource folder.
     * 
     * @return initialTable
     * @throws "Could not read initial table" to terminal if IOException
     */

    private Table getInitialTable() {
        Table initialTable = null;
        try {
            initialTable = tablePersistence.loadInitialTable(initialFileName);
        } catch (IOException e) {
            System.err.println("Could not read initial table");
        }
        return initialTable;
    }

    /**
     * Returns a table object based on data collected from the json-file in
     * user.home folder. If json-file is not found, the method will return the table
     * form initialTable-method.
     * 
     * @return getInitialTable() which returns initialTable
     */

    Table getSavedTable() {
        Table savedTable = null;
        try {
            savedTable = tablePersistence.loadSavedTable(fileName);
            return savedTable;
        } catch (IOException e) {
            System.err.println("Could not read saved table");
        }
        return getInitialTable();
    }

    /**
     * Saves the Table Object as json-file in user.home folder.
     * 
     * @throws "Could not save Table" if IOException
     */

    void saveTable() {
        try {
            tablePersistence.saveTable(table, fileName);
        } catch (IOException e) {
            System.err.println("Could not save Table");
        }
    }

    /**
     * Adds all teams into a table object and returns this object
     * 
     * @return Table object
     */

    Table getTable() {
        Table getTable = new Table();
        for (Team team : table.getTeams()) {
            getTable.addTeams(team);
        }
        return getTable;
    }

    /**
     * Sets table object
     * 
     * @param table
     */

    void setTable(Table table) {
        this.table = table;
    }

    /**
     * Sets tableText name
     * 
     * @param name name of table
     */

    private void setTableName(String name) {
        tableName.setText(name);
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
     * Puts all teams in to the TableView area.
     */

    private void setTableView() {
        tableView.setItems(teams);
    }

    /**
     * Sets choices for the choose-team boxes This methos is only called on once in
     * initialize because the app does not support adding teams
     */

    private void setChoices() {
        home.getItems().clear();
        away.getItems().clear();
        for (Team team : table.getTeams()) {
            home.getItems().add(team.getName());
            away.getItems().add(team.getName());
        }
    }

    /**
     * Checks if points added from the user is valid Return false if pointText is
     * empty, negative or is not integer.
     * 
     * @param points
     * @throws NumberFormatException
     * @return boolean value, true if pointstext is valid
     */

    boolean checkPoints(String points) {
        int pointsAsInt = 0;
        try {
            pointsAsInt = Integer.parseInt(points);
        } catch (NumberFormatException e) {
            return false;
        }
        return pointsAsInt >= 0;
    }

    /**
     * Add points to team object when user adds points
     * 
     * @param team
     * @param points
     */

    private void addTeamPoints(String teamName, int points) {
        for (Team team : table.getTeams()) { // Find teams and add points
            if (team.getName().equals(teamName)) {
                team.addPoints(points);
            }
        }
    }

    /**
     * Checking if two teams are equal or fi either of them are null.
     * 
     * @param homeTeam name as string
     * @param awayTeam name as string
     * @return boolean value, true if teams are different and not null.
     */

    private boolean checkTeams(String homeTeam, String awayTeam) {
        return !(homeTeam == null || awayTeam == null || homeTeam.equals(awayTeam));
    }

    /**
     * sets home and away choiceboxes to null and clears points textFields
     */

    private void resetInputFields() {
        home.setValue(null);
        away.setValue(null);
        pointsH.clear();
        pointsA.clear();
    }

    /**
     * When user pushes save button, this method: Clears old messages and input
     * boxesand Checks for input-errors: Is points valid (positive) Are the chosen
     * teams two different teams? Updates table Resets choice boxes and text fields
     */

    @FXML
    void handleSave() {
        message.clear(); // Clear old error message if any

        if (!checkPoints(pointsH.getText()) || !checkPoints(pointsA.getText())) {
            message.setText("Invalid points");
            return;
        }

        if (!checkTeams(home.getValue(), away.getValue())) { // Check if both teams are selected and different
            message.setText("Invalid teams");
            return;
        }
        addTeamPoints(home.getValue().toString(), Integer.parseInt(pointsH.getText()));
        addTeamPoints(away.getValue().toString(), Integer.parseInt(pointsA.getText()));
        updateTeamsList();
        resetInputFields();
        saveTable();
        setTable(getSavedTable());
        updateView();
    }

    /**
     * use setTable method with input getInitialTable. The setTable method also
     * calls the updateView method so that the tableView attribute contains the
     * initialTable. For testing: adding 1 point to each team and saving result in
     * json-file in user.home folder.
     */

    @FXML
    void handleEdit() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(editTable));
            Parent root1 = (Parent) fxmlLoader.load();
            editTableController = fxmlLoader.getController();
            editTableController.setFileName(fileName);
            editTableController.setTable(table);
            editTableController.updateView();
            editTableController.setAppController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            message.setText("Unable to load edit window");
        }
    }

    EditTableController getEditTableController() {
        return this.editTableController;
    }

    /**
     * setting field "filename" to input value
     * 
     * @param inputName
     */

    void setFileName(String inputName) {
        this.fileName = inputName.concat(".json");
    }

    /**
     * try opening file with input filename. if file is not found, a new file with
     * input name is made with an empty table.
     */

    @FXML
    void handleOpenFile() {
        message.clear();
        Table saveTable = new Table();
        setFileName(fileNameInput.getText());
        try {
            saveTable = tablePersistence.loadSavedTable(fileName);
            this.table = saveTable;
        } catch (IOException e) {
            message.setText("Could not find file, made a new one.");
            try {
                tablePersistence.saveTable(saveTable, fileName);
                this.table = saveTable;
            } catch (IOException io) {
                message.setText("Could not find or make file, try a new filename");
            }
        }
        updateView();
    }

    /**
     * Loads the saved table and updates view. User needs to press the load button
     * to see changes made in edit window.
     */

    @FXML
    void handleLoad() {
        setTable(getSavedTable());
        updateView();
    }

    /**
     * Sets TableName as filename. Updates teams list. Sets team choice boxes. Sets
     * tableView.
     */

    void updateView() {
        setTableName(fileName.substring(0, fileName.length() - 5));
        updateTeamsList();
        setChoices();
        setTableView();
    }

    @FXML
    public void initialize() {
        setFileName(initialFileName);
        setTable(getSavedTable());
        updateTeamsList();
        try {
            setTableView();
            teamsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("name"));
            pointsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("points"));
            updateView();
            setChoices();
        } catch (Exception e) {
            System.err.println("Something went wrong.");
        }
    }
}