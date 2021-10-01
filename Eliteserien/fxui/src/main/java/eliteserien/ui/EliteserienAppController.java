package eliteserien.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import eliteserien.core.TableListener;
import eliteserien.core.SoccerTeam;
import eliteserien.core.Table;
import eliteserien.json.TablePersistence;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class EliteserienAppController {

    @FXML
    String userTablePath;

    @FXML
    ListView<SoccerTeam> tableView;

    @FXML
    Text tableText;

    private TablePersistence tablePersistence;
    private Table table;

    private Table getInitialTable() {
        Table table = null;
        try {
            table = tablePersistence.loadTable();
        } catch (Exception ioex) {
            System.err.println("Could not read saved table");
            ioex.printStackTrace();
        }

        if (table == null) {
            table = new Table();
            SoccerTeam NNK = new SoccerTeam();
            NNK.addPoints(2);
            NNK.setName("NNK");
            table.addSoccerTeams(NNK);
        }
        return table;
    }

    Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        if (this.table != null) {
            this.table.removeTableListener(listener);
        }
        this.table = table;
        updateView();
        if (this.table != null) {
            this.table.addTableListener(listener);
        }
    }
    
    private Callback<Table, Void> onTableChanged = null;

    public void setOnTableChanged(Callback<Table, Void> onTableChanged) {
        this.onTableChanged = onTableChanged;
    }

    private TableListener listener = table -> {
        if (onTableChanged != null) {
            onTableChanged.call(getTable());
        }
        updateView();
    };

    protected void setTableText() {
        List<SoccerTeam> teams = new ArrayList<>();
        if (table != null) {
            teams.addAll(table.getSoccerTeams());
        }
        String tableString = "";
        for (SoccerTeam team : teams) {
            String namestring = ("teamname: " + team.getName()+ ", ");
            String pointstring = ("points: " + team.getPoints() + "; ");
            tableString = (tableString + namestring + pointstring);
        }
        tableText.setText(tableString);
    }

    protected void updateView() {
        List<SoccerTeam> teams = new ArrayList<>();
        if (table != null) {
            teams.addAll(table.getSoccerTeams());
        }
        tableView.getItems().setAll(teams);
        setTableText();
    }

    @FXML
    void initialize() {
        this.tablePersistence = new TablePersistence();
        this.table = getInitialTable();
        System.out.println(tableText);
        updateView();
    }
}

