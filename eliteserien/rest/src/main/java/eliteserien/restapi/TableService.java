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
 * The top-level rest service for TodoModel.
 */
@Path(TableService.ELITESERIEN_SERVICE_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class TableService {

  public static final String ELITESERIEN_SERVICE_PATH = "eliteserien";

  private static final Logger LOG = LoggerFactory.getLogger(TableService.class);

  @Context
  private Table table;

  @Context
  private TablePersistence tablePersistence;

  /**
   * The root resource, i.e. /eliteserien
   *
   * @return the Table
   */
  @GET
  public Table getTable() {
    System.out.println("getTable method tableService");
    LOG.debug("getTable: " + table);
    return table;
  }



  private void autoSaveTable() {
    if (tablePersistence != null) {
      try {
        tablePersistence.saveTable(table);
      } catch (IllegalStateException | IOException e) {
        System.err.println("Couldn't auto-save Table: " + e);
      }
    }
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
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
   * Emties the Table.
   */
  @DELETE
  public boolean emptyTable() {
    for (Team team : table.getTeams()) {
      table.deleteTeam(team);
    }
    autoSaveTable();
    return true;
  }
}


