package eliteserien.ui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller class
 */

public class RemoteAppController {

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
    TextField tableNameInput; // filename input from user

    @FXML
    Label tableName; // name of the currently viewed table

    @FXML
    String editTable;

    @FXML
    String endpointUri;

    /**
     * Attributes: Tablepersistence object for reading and writing to json-file.
     * Table object made by collecting data from the json-file. ObservableList
     * object for tableView
     */

    private ObjectMapper objectMapper;
    private Table table;
    private ObservableList<TeamProperties> teams = FXCollections.observableArrayList();
    private EditTableController editTableController;
    private URI uri;

    /**
     * Emties the table in server and then add the current table.
     * 
     * @throws "Could not save Table" if IOException
     */

    void saveTable() {
        try {
            emptyTable();
            putTable(table);
        } catch (RuntimeException e) {
            System.err.println("Could not save table to server");
        }
    }

    void saveTable(Table table) {
        try {
            emptyTable();
            putTable(table);
        } catch (RuntimeException e) {
            System.err.println("savetable with table input");
        }
    }

    /**
     * Adds all teams into a table object and returns this object
     * 
     * @return Table object
     */

    Table getTable() {
        return this.table;
    }

    void setTable(Table table) {
        this.table = table;
    }

    /**
     * Sets tableText name
     * 
     * @param name name of table
     */

    private void setTableName(String name) {
        table.setName(name);
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
            editTableController.setFileName("eliteserien-server"); // fix
            editTableController.setTable(table);
            editTableController.updateView();
            editTableController.setRemoteAppController(this);
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
     * @param uri
     */

    void setUri(URI uri) {
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    /**
     * try opening file with input filename. if file is not found, a new file with
     * input name is made with an empty table.
     */

     // handleTableNameChange - fix

    @FXML
    void handleOpenFile() {
        message.clear();
        setTableName(tableNameInput.toString());
        updateView();
    }

    /**
     * Loads the saved table and updates view. User needs to press the load button
     * to see changes made in edit window.
     */

    @FXML
    void handleLoad() {
        try {
            setTable(getUriTable(uri));
        } catch (RuntimeException e) {
            System.out.println("could not load table from uri");
        }
        updateView();
    }

    /**
     * Sets TableName as filename. Updates teams list. Sets team choice boxes. Sets
     * tableView.
     */

    void updateView() {
        setTableName(table.getName());
        updateTeamsList();
        setChoices();
        setTableView();
    }

    public Table getUriTable(URI uri) {
        HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json").GET().build();
        System.out.println(request.toString());
        try {
            final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
            Table newTable = objectMapper.readValue(response.body(), Table.class);
            return newTable;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void putTable(Table table) {
        try {
            String json = objectMapper.writeValueAsString(table);
            System.out.println(json);
            HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json")
                    .header("Content-Type", "application/json").PUT(BodyPublishers.ofString(json)).build();
            System.out.println(request.toString());
            final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
            String responseString = response.body();
            Boolean added = objectMapper.readValue(responseString, Boolean.class);
            if (added) {
                System.out.println("table added");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void emptyTable() {
        try {
            HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Accept", "application/json")
                .DELETE()
                .build();
            final HttpResponse<String> response =
                HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
            String responseString = response.body();
            Boolean removed = objectMapper.readValue(responseString, Boolean.class);
            if (removed != null) {
              System.out.println("Table removed from server.");
            }
          } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
          }
    }



    @FXML
    public void initialize() {
        objectMapper = TablePersistence.createObjectMapper();
        if (endpointUri != null) {
            try {
                setUri(new URI(endpointUri));
                System.out.println("Using remote endpoint @ " + endpointUri);
                this.table = getUriTable(uri);
                updateTeamsList();
                setTableView();
                teamsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("name"));
                pointsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("points"));
                updateView();
                setChoices();
            } catch (URISyntaxException e) {
                System.err.println("Something went wrong when trying to use remote uri");
            }
        }
    }
}