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
    String fileName; // Contains json-filename.

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
    Button testButton;

    @FXML
    Button testButton2;

    @FXML
    TableView<TeamProperties> editTableView;

    @FXML
    TableColumn<TeamProperties, String> editTeamColumn;

    @FXML
    TableColumn<TeamProperties, String> editPointsColumn;

    @FXML
    TextField selectedTeam;

    @FXML
    TextField editTeamName;

    @FXML
    TextField editTeamPoints;
    
    @FXML
    TextField errorMessageEditWindow;

    /**
     * Attributes: 
     * Tablepersistence object for reading and writing to json-file.
     * Table object made by collecting data from the json-file.
     * ObservableList object for tableView
    */ 

    private TablePersistence tablePersistence = new TablePersistence();
    private Table table;
    private ObservableList<TeamProperties> teams = FXCollections.observableArrayList(); 


    /**
     * Returns a table object based on data collected from the json-file found in resource folder. 
     * @return initialTable
     * @throws "Could not read initial table" to terminal if IOException
    */

    private Table getInitialTable() {
        Table initialTable = null;
        try {
            initialTable = tablePersistence.loadInitialTable(fileName);
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
        updateTeamsList();
        setTableView();
    }

    /**
     * Sets choices for the choose-team boxes 
     * This methos is only called on once in initialize 
     * because the app does not support adding teams
    */

    private void setChoices(){                     
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
            if(pointsH.getText() != ""){          // If points field is not empty try parseInt (if empty: points will stay 0)
                pointsHome = Integer.parseInt(pointsH.getText());
            }
            if(pointsA.getText() != ""){
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
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            message.setText("Unable to load edit window");
        }

    }

    @FXML
    public void handleSave2(){
        saveTable();
        Stage stage = (Stage) testButton2.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleAddTeam(){
        errorMessageEditWindow.clear();
        String teamName = editTeamName.getText();        // Extract input from choice boxes
        int points = 0;                       // Set points 0
        try {
            if(editTeamPoints.getText() != ""){          // If points field is not empty try parseInt (if empty: points will stay 0)
                points = Integer.parseInt(editTeamPoints.getText());
            }
            validPoints(points);             // Check if points are valid (positive numbers only)
        }
        catch(IllegalArgumentException e){       // Exception thrown if points is not int or points<0
            errorMessageEditWindow.setText("Invalid points");   // Rrint error message to user
            return;
        }
        Team team = new Team(teamName, points);
        table.addTeams(team);
        saveTable();

        editTeamName.clear();
        editTeamPoints.clear();
        updateEditView();
    }

    @FXML
    public void handleEditTeam(){
        if(selectedTeam.getText() != ""){
            editTeamName.setText(selectedTeam.getText());
            for (Team team : table.getTeams()){
                if (selectedTeam.getText().equals(team.getName())){
                    editTeamPoints.setText(String.valueOf(team.getPoints()));
                    table.deleteTeam(team);
                }
            }
            updateEditView();
            selectedTeam.clear();
        }
    }

    @FXML
    public void handleDeleteTeam(){
        if(selectedTeam.getText() != ""){
            for (Team team : table.getTeams()){
                if (selectedTeam.getText().equals(team.getName())){
                    table.deleteTeam(team);
                }
            }
            updateEditView();
            selectedTeam.clear();
        }
    }

    @FXML
    public void handleLoad(){
        setTable(getSavedTable());
        updateView();
    }

    @FXML
    void handleSelectedTeamChanged(){
        selectedTeam.setText(editTableView.getSelectionModel().getSelectedItem().getName());
    }

    protected void setEditTableView() {
        editTableView.setItems(teams);
    }

    protected void updateEditView() {
        updateTeamsList();
        setEditTableView();
    }

    @FXML
    public void initialize() {
        setTable(getSavedTable());
        updateTeamsList();
        try{
        setTableView();
        teamsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("name"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("points"));
        updateView();
        setChoices();
        } catch(Exception e){}
        try{
        editTeamColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("name"));
        editPointsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("points"));
        updateEditView();
        } catch(Exception e){}
    }
}