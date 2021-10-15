package eliteserien.ui;

import java.io.IOException;

import eliteserien.core.Table;
import eliteserien.core.Team;
import eliteserien.json.TablePersistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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


public class EliteserienAppController{

    @FXML
    String fileName;

    @FXML
    private TableView<Team> tableView = new TableView<Team>();

    @FXML
    private TableColumn<Team, String> teamsColumn = new TableColumn<Team, String>();

    @FXML
    private TableColumn<Team, Integer> pointsColumn = new TableColumn<Team, Integer>();

    @FXML
    private ChoiceBox<String> home;

    @FXML
    private ChoiceBox<String> away;

    @FXML
    private TextField pointsH;  //points home team

    @FXML
    private TextField pointsA;  //points away team

    @FXML
    private Button saveButton;  //save match button

    @FXML
    private TextField message;  //text field for error messages to user


    private TablePersistence tablePersistence = new TablePersistence();
    private Table table;
    
    private ObservableList<Team> teams = FXCollections.observableArrayList();   //for tableView


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

    private void updateTeamsList() {               //update the observable list of teams to match the teams in table
        teams.clear();
        for (Team team : table.getTeams()) {
            teams.add(team);
        }
    }

    private void setChoices(){                     //set choices for the choice boxes
        for (Team team : table.getTeams()){        //only called on once in initialize because the app does not support adding teams
           home.getItems().add(team.getName());
           away.getItems().add(team.getName());
        }
    }

    private void validPoints(int points) throws IllegalArgumentException{   //check if points is positive
        if(points < 0){         
            throw new IllegalArgumentException();
        }
    }

    @FXML
    public void handleSave(){
        message.clear();                          //clear old error message if any
        String homeTeam = home.getValue();        //extract chosen from choice boxes
        String awayTeam = away.getValue();
        int pointsHome = 0;                       //set points 0
        int pointsAway = 0;
        try {
            if(pointsH.getText() != ""){          //if points field is not empty try parseInt (if empty: points will stay 0)
                pointsHome = Integer.parseInt(pointsH.getText());
            }
            if(pointsA.getText() != ""){
                pointsAway = Integer.parseInt(pointsA.getText());
            }
            validPoints(pointsHome);             //check if points are valid(positive numbers only)
            validPoints(pointsAway);
        }
        catch(IllegalArgumentException e){       //exception thrown if points is not int or points<0
            message.setText("Invalid points");   //print error message to user
            return;
        }
        if(homeTeam == null || awayTeam == null || homeTeam == awayTeam){    //check if both teams are selected and different
            message.setText("Choose two different teams");
            return;
        }
        for (Team team : table.getTeams()){      //find teams and add points
            if(team.getName().equals(homeTeam)){
                addTeamPoints(team, pointsHome);
            }
            else if(team.getName().equals(awayTeam)){
                addTeamPoints(team, pointsAway);
            }
        }
        home.setValue(null);                     //reset choice boxes and text fields
        away.setValue(null);
        pointsH.clear();
        pointsA.clear();
    } 

    public void addTeamPoints(Team team, int i) {
        team.addPoints(i);
        updateView();
    }

    @FXML
    public void initialize() {
        setTable(getInitialTable());

        teamsColumn.setCellValueFactory(new PropertyValueFactory<Team, String>("name"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<Team, Integer>("points"));
        
        for (Team team : table.getTeams()) {
            addTeamPoints(team, 1);
        }
        saveTable();
        setChoices();
    }
}