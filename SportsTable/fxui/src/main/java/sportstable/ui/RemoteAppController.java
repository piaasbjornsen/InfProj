package sportstable.ui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Controller class for when running the application with an extern server
 */

public class RemoteAppController {

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
    TextField tableName; // name input from user

    @FXML
    String editTable; // Text on edit-table button

    @FXML
    String UriString; // URI string: "http://localhost:8999/sportstable/"

    private ObjectMapper objectMapper; // For reading and writing to json-file
    private Table table; // Table object made by collecting data from the json-file.
    private ObservableList<TeamProperties> teams = FXCollections.observableArrayList(); // ObservableList object for
                                                                                        // tableView
    private EditTableController editTableController; // Controller for edit-table window
    private URI uri; // For server access

    /**
     * Emties the table in server and then add the current table.
     */

    void saveTable() {
        try {
            emptyTable();
            putTable(table);
        } catch (RuntimeException e) {
            message.setText("Could not save table to server");
        }
    }

    /**
     * Sets table and saves it using saveTable method.
     * 
     * @param table to set and save
     */

    void saveTable(Table table) {
        setTable(table);
        saveTable();
    }

    /**
     * @return Table object
     */

    Table getTable() {
        return this.table;
    }

    /**
     * @param table to set
     */

    void setTable(Table table) {
        this.table = table;
    }

    /**
     * Sets tablename and sets tabletext name
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
     * @param points to check
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
     * @param team   name
     * @param points to add
     */

    private void addTeamPoints(String teamName, int points) {
        for (Team team : table.getTeams()) { // Find teams and add points
            if (team.getName().equals(teamName)) {
                team.addPoints(points);
            }
        }
    }

    /**
     * Checking if two teams are equal or if either of them are null.
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
     * Opens edit window. Sets the file name of EditTableController to
     * "sportstable-server". Sets table of EditTableController to this.table. Sets
     * RemoteAppController as main controller in EditTableController
     */

    @FXML
    void handleEdit() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(editTable));
            Parent root1 = (Parent) fxmlLoader.load();
            editTableController = fxmlLoader.getController();
            editTableController.setFileName("sportstable-server");
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

    /**
     * @return this.editTableController
     */

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

    /**
     * @return this uri
     */

    public URI getUri() {
        return uri;
    }

    /**
     * sets input Table name from tableName textfield. (in LocalApp this method will
     * open a new file, but remoteApp only supports use of one Table)
     */

    @FXML
    void handleSetTableName() {
        message.clear();
        setTableName(tableName.getText());
        updateView();
    }

    /**
     * Updates teams list, sets team choice-boxes and sets tableView.
     */

    void updateView() {
        setTableName(table.getName());
        updateTeamsList();
        setChoices();
        setTableView();
    }

    /**
     * Sends a GET request from httpClient to uri path. objectMapper reads value of
     * response, and makes a Table object.
     * 
     * @param uri
     * @return Table object from httpClient
     * @throws RuntimeException
     */

    public Table getUriTable(URI uri) throws RuntimeException {
        HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json").GET().build(); // request:
                                                                                                              // "http://localhost:8999/sportstable/
                                                                                                              // GET"
        try {
            final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
            Table newTable = objectMapper.readValue(response.body(), Table.class);
            return newTable;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a PUT request from httpClient to uri path with Table input as
     * json-String. If added, boolean true value is returned, and "table added"
     * printet to system.
     * 
     * @param table to put in server
     * @throws RuntimeException
     */

    private void putTable(Table table) throws RuntimeException {
        try {
            String json = objectMapper.writeValueAsString(table);
            HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json")
                    .header("Content-Type", "application/json").PUT(BodyPublishers.ofString(json)).build(); // request:
                                                                                                            // "http://localhost:8999/sportstable/
                                                                                                            // PUT"
            System.out.println(request.toString());
            final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
            String responseString = response.body();
            Boolean added = objectMapper.readValue(responseString, Boolean.class);
            if (added) {
                System.out.println("Table added");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A DELETE request is made by httpClient and sent to uri path.
     */
    private void emptyTable() throws RuntimeException {
        try {
            HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json").DELETE().build(); // request:
                                                                                                                     // "http://localhost:8999/sportstable/
                                                                                                                     // DELETE"
            final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
            String responseString = response.body();
            Boolean removed = objectMapper.readValue(responseString, Boolean.class);
            if (removed != null) {
                System.out.println("Table removed from server.");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * initializes a objectMapper object, try to read Table from uri path and
     * updates Table values and View
     */

    @FXML
    public void initialize() {
        objectMapper = TablePersistence.createObjectMapper();
        try {
            setUri(new URI(UriString));
            System.out.println("Using remote endpoint @ " + UriString);
            this.table = getUriTable(uri);
            updateTeamsList();
            setTableView();
            teamsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("name"));
            pointsColumn.setCellValueFactory(new PropertyValueFactory<TeamProperties, String>("points"));
            updateView();
            setChoices();
        } catch (URISyntaxException | RuntimeException e) {
            System.err.println("Something went wrong when trying to use remote uri");
        }
    }
}