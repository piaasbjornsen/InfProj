package eliteserien.restapi;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eliteserien.core.Team;
import eliteserien.core.Table;
import eliteserien.json.TablePersistence;
import java.io.IOException;


/**
 * Rest api for the web app.
 * Framework for how to answer requests from client, and what requests to answer
*/

@Path(TableService.ELITESERIEN_SERVICE_PATH) // Tells the setup of the server where our service can be reached (through a eliteserien-prefix)
@Produces(MediaType.APPLICATION_JSON)        // Tells the setup of the server that the return values from the methods in this class will be serialized with JSON back to the client
public class TableService {

  public static final String ELITESERIEN_SERVICE_PATH = "eliteserien";           // The prefix that have to be used to ask for this spesific service

  private static final Logger LOG = LoggerFactory.getLogger(TableService.class); // Logger to trigger log events for this class, to help trace out errors

  /**
   * The tableService needs a Table object and a TablePersistnece object
   * The @Context annotation signalizes that the content/values to these objects do not come from this class,
   * but needs to be injected/configured from an extern source
   * (from rest server via TableConfig)
   * So the methods in this class can expect these objects to be available for them 
  */
  
  @Context
  private Table table; 

  @Context
  private TablePersistence tablePersistence; 

  /**
   * The root resource, i.e. /eliteserien
   * 
   * @return the injected Table
  */

  @GET // This method gets called when client sends a GET HTTP request
  public Table getTable() {
    System.out.println("getTable method tableService");
    LOG.debug("getTable: " + table);
    return table;
  }

  /**
   * Saves table
   * Used in putTable and renameTable,
   * @throws IOException if it is not able to save Table with saveTable method
  */

  private void autoSaveTable() {
    if (tablePersistence != null) {  // Checks if there is a tablePeristicenc object avaialable to write to file
      try {
        tablePersistence.saveTable(table);
      } catch (IllegalStateException | IOException e) {
        System.err.println("Couldn't auto-save Table: " + e);
      }
    }
  }

  /**
   * Replaces or updates a Table.
   * Synchronizes the server with the new changes
   * @param tableArg the table with the new updates, or the table to replace current table
   * @return true when done
  */

  @PUT // This method gets called when client sends a PUT HTTP request
  @Consumes(MediaType.APPLICATION_JSON) // Tells the setup of the server that this method deserialize the parameter to make an object requested by the client
  public boolean putTable(Table tableArgs) {
    LOG.debug("putTable({})", tableArgs);
    for (Team team : tableArgs.getTeams()) {
        table.addTeams(team);
    }
    System.out.println("puttable method");
    autoSaveTable();
    return true;
  }

  /**
   * Deletes the teams in the table, so the Table is empty
   * @return true when done
  */

  @DELETE // This method gets called when client sends a DELETE HTTP request
  public boolean emptyTable() {
    for (Team team : table.getTeams()) {
      table.deleteTeam(team);
    }
    autoSaveTable();
    return true;
  }
}


