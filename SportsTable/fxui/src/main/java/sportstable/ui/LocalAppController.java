package sportstable.ui;

import java.io.IOException;

import sportstable.core.Table;
import sportstable.core.Team;
import sportstable.json.TablePersistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller class for when running the application locally
 */

public class LocalAppController {

    // FXML attributes:

    @FXML
    TableView<TeamProperties> tableView; // TableView for SportsTeam Table

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
    TextField fileNameInput; // File name input from user

    @FXML
    String initialFileName; // File name to json-file in resource folder

    @FXML
    String editTable; // Text on edit-table button

    private TablePersistence tablePersistence = new TablePersistence(); // Tablepersistence object for reading and
                                                                        // writing to json-file.
    private Table table; // Table object made by collecting data from the json-file.
    private ObservableList<TeamProperties> teams = FXCollections.observableArrayList(); // ObservableList object for
                                                                                        // tableView
    private String fileName; // Name of json-file to Table object
    private EditTableController editTableController; // Controller for edit-table window

    /**
     * @return Table from json-file found in resource folder
     */

    private Table getInitialTable() {
        Table initialTable = null;
        try {
            initialTable = tablePersistence.loadInitialTable(fileName);
            return initialTable;
        } catch (IOException e) {
            message.setText("Could not read initial table");
        }
        return new Table();
    }

    /**
     * Returns a table object based on data collected from the json-file in
     * user.home folder. If json-file is not found, the method will return the table
     * form initialTable-method.
     * 
     * @return Table object
     */

    Table getSavedTable() {
        Table savedTable = null;
        try {
            savedTable = tablePersistence.loadSavedTable(fileName);
            return savedTable;
        } catch (IOException e) {
            message.setText("Loads initial table");
        }
        return getInitialTable();
    }

    /**
     * Saves the Table Object as json-file in user.home folder.
     */

    void saveTable() {
        try {
            tablePersistence.saveTable(table, fileName);
        } catch (IOException e) {
            message.setText("Could not save Table");
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
     * @param Table to set
     */

    void setTable(Table table) {
        this.table = table;
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
     * Sets choices for the choose-team boxes.
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
     * Checks if points added from the user is valid.
     * 
     * @param points
     * @return True if pointstext is valid, false if pointText is empty, negative or
     *         is not integer.
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
     * @param Team   to add point to
     * @param Points to add
     */

    private void addTeamPoints(String teamName, int points) {
        for (Team team : table.getTeams()) { // Find teams and add points
            if (team.getName().equals(teamName)) {
                team.addPoints(points);
            }
        }
    }

    /**
     * Checks if two teams are equal or if either of them are null.
     * 
     * @param homeTeam name as string
     * @param awayTeam name as string
     * @return True if teams are different and not null.
     */

    private boolean checkTeams(String homeTeam, String awayTeam) {
        return !(homeTeam == null || awayTeam == null || homeTeam.equals(awayTeam));
    }

    /**
     * Sets home and away choiceboxes to null and clears points textFields
     */

    private void resetInputFields() {
        home.setValue(null);
        away.setValue(null);
        pointsH.clear();
        pointsA.clear();
    }

    /**
     * When user pushes save button, this method clears old messages and input
     * boxesand, checks for input-errors (is points valid (positive)) and if the
     * chosen teams are valid (two different teams). Updates table Resets choice
     * boxes and text fields
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
     * Opens edit window. Sets the file name of EditTableController to
     * this.fileName. Sets table of EditTableController to this.table. Sets
     * this.controller as AppController, in EditTableController
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
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            message.setText("Unable to load edit window");
        }
    }

    /**
     * @return the editTableController
     */

    EditTableController getEditTableController() {
        return this.editTableController;
    }

    /**
     * Sets field "filename" to input value and adds ".json"
     * 
     * @param file name from input
     */

    void setFileName(String inputName) {
        this.fileName = inputName.concat(".json");
    }

    /**
     * Tries to open file with input filename. If file is not found, a new file with
     * input name is made with an empty table.
     */

    @FXML
    void handleOpenFile() {
        message.clear();
        if (fileNameInput.getText().isEmpty()) {
            message.setText("No file name entered");
            return;
        }
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
     * Updates teams list, sets team choice-boxes and sets tableView.
     */

    void updateView() {
        updateTeamsList();
        setChoices();
        setTableView();
    }

    /**
     * Opens the intialFileName table. If user have already used the applicaiton
     * before, the saved table with intialFileName will be opened
     */

    @FXML
    public void initialize() {
        setFileName(initialFileName);
        fileNameInput.setText(fileName.substring(0, fileName.length() - 5));
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