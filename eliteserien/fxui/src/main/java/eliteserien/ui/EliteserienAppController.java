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

public class EliteserienAppController{

    // FXML attributes:

    @FXML
    String initialFileName; // Contains json-filename

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
    TextField pointsH;     // Points to home team

    @FXML
    TextField pointsA;     // Points to away team

    @FXML
    Button saveButton;     // Save-match button

    @FXML
    TextField message;     // Text field for error messages to user

    @FXML
    TextField fileNameInput;

    @FXML
    Label tableName;

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
    private EditTableController editTableController;



    /**
     * Returns a table object based on data collected from the json-file found in resource folder. 
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
        return getInitialTable();
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
    
    public void setFileName(String inputName) {
        if (inputName.endsWith(".json")) {
            this.fileName = inputName;
        }
        else {
            this.fileName = inputName.concat(".json");
        }
    }

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

    protected void setTableView() {
        tableView.setItems(teams);
    }

    /**
     * Updates team list
     * Sets TableView with updated team list (for now)
    */

    protected void updateView() {
        setTableName(fileName.substring(0, fileName.length() - 5));
        updateTeamsList();
        setChoices();
        setTableView();
    }

    /**
     * Sets choices for the choose-team boxes 
     * This methos is only called on once in initialize 
     * because the app does not support adding teams
    */

    private void setChoices(){            
        home.getItems().clear();
        away.getItems().clear();         
        for (Team team : table.getTeams()){        
           home.getItems().add(team.getName());
           away.getItems().add(team.getName());
        }
    }

    /**
     * Checks if points added from the user is positive 
     * If they are negative, it throws exception
     * @param points
     * @throws IllegalArgumentException
     */
    private void validPoints(int points) throws IllegalArgumentException{   //check if points is positive
        if(points < 0){         
            throw new IllegalArgumentException();
        }
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
        message.clear();                          // Clear old error message if any
        String homeTeam = home.getValue();        // Extract input from choice boxes
        String awayTeam = away.getValue();
        int pointsHome = 0;                       // Set points 0
        int pointsAway = 0;
        try {
            if(!pointsH.getText().isEmpty()) {          // If points field is not empty try parseInt (if empty: points will stay 0)
                pointsHome = Integer.parseInt(pointsH.getText());
            }
            if(!pointsA.getText().isEmpty()){
                pointsAway = Integer.parseInt(pointsA.getText());
            }
            validPoints(pointsHome);             // Check if points are valid (positive numbers only)
            validPoints(pointsAway);
        }
        catch(IllegalArgumentException e){       // Exception thrown if points is not int or points<0
            message.setText("Invalid points");   // Rrint error message to user
            return;
        }
        if(homeTeam == null || awayTeam == null || homeTeam == awayTeam){    // Check if both teams are selected and different
            message.setText("Choose two different teams");
            return;
        }
        for (Team team : table.getTeams()){      // Find teams and add points
            if(team.getName().equals(homeTeam)){
                addTeamPoints(team, pointsHome);
            }
            else if(team.getName().equals(awayTeam)){
                addTeamPoints(team, pointsAway);
            }
        }
        home.setValue(null);                     // Reset choice boxes and text fields
        away.setValue(null);
        pointsH.clear();
        pointsA.clear();
        updateTeamsList();
        updateView();
        saveTable();
    } 

    /**
     * Add points to team object when user adds points
     * @param team
     * @param points
    */

    private void addTeamPoints(Team team, int points) {
        team.addPoints(points);
    }
    
    /** 
     * use setTable method with input getInitialTable.
     * The setTable method also calls the updateView method so 
     * that the tableView attribute contains the initialTable.
     * For testing: adding 1 point to each team and saving result 
     * in json-file in user.home folder.
    */


    @FXML
    public void handleEdit(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditTable.fxml"));            
            Parent root1 = (Parent) fxmlLoader.load();
            editTableController = fxmlLoader.getController();
            editTableController.setFileName(fileName);
            editTableController.setTable(table);
            editTableController.updateView();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            message.setText("Unable to load edit window");
        }
    }

    @FXML
    public void handleLoad(){
        setTable(getSavedTable());
        updateView();
    }

    @FXML
    void handleOpenFile() {
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

    @FXML
    public void initialize() {
        if (fileName == null) {
            setFileName(initialFileName);
        }
        setTable(getSavedTable());
        updateTeamsList();
        try{
        setTableView();
        teamsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("name"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("points"));
        updateView();
        setChoices();
        } catch(Exception e){

        }
    }
}