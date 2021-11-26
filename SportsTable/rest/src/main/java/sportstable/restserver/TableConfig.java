package sportstable.restserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import sportstable.core.Table;
import sportstable.core.Team;
import sportstable.json.TablePersistence;
import sportstable.restapi.TableService;

/**
 * Configures the rest service (JSON support with Jackson),
 * so that our API can be used in a HTTP server 
 * (Tells Jersey (JAX-RS implementation) how and when our rest api TableService should be called)
 * 
*/

public class TableConfig extends ResourceConfig { // Inherits ResourceConfig, as this is what we are doing, but for our app specifically
  
  // Instance of the objects that we use in rest service 
  private Table table; 
  private TablePersistence tablePersistence;

  /**
   * Initialize this TableConfig, so that it:
   * - Annotes the rest api objects (Table object and TablePersistence object) correctly
   * - Sets up the rest server
   * - Sets up logic related to serializationg with JSON
   * 
   * @param table instance to serve
  */

  public TableConfig(Table table) {
    setTable(table);  // Annotes the rest-api Table object
    
    // Instances the objects that later will på injected to the rest-api objects
    tablePersistence = new TablePersistence();
    tablePersistence.setFileName("server-sportstable.json");
    tablePersistence.setFilePath();

    // Sets up the rest server
    register(TableService.class); // Tells that TableService is a service object, finds the path annotaions to find out when this object is used

    // Sets up logic related to serialization with JSON
    register(TableModuleObjectMapperProvider.class); // Tells which Jackson Module we will use to serialize our objects
    register(JacksonFeature.class); // Tells that we use Jackson

    // Makes sure that the objects that needs to be injected,
    // are injected from the right source: table and tablePersistence in this class
    register(new AbstractBinder() { 
      @Override
      protected void configure() {     // table and tablePersictence is bound to the instances that are available by running TableConfig
        bind(TableConfig.this.table);  // Its the same table object that is instanced every time the the service object (TableService) gets a new instance (for every request)
        bind(TableConfig.this.tablePersistence); // Its the same tablePeristence object that is instanced every time the the service object (TableService) gets a new instance (for every request)
      }
    });
  }

  /**
   * Initialize this TableConfig with default Table,
   * if there is no table as a parameter.
   * This is a quicker way to start the server, 
   * and is used in integrationtesting
  */

  public TableConfig() {
    this(createDefaultTable());
  }
  
  /**
   * Returns table
   * @return table
  */

  public Table getTable() {
    return this.table;
  }
  
  /**
   * Sets table
  */

  public void setTable(Table table) {
    this.table = table;
  }
  
  /**
   * Creates a default table, with premade data from a premade json file
   * @return a default table
   * @throws IOException if it is not able to read default-table.json
  */

  private static Table createDefaultTable() {
    TablePersistence tablePersistence = new TablePersistence();
    URL url = TablePersistence.class.getResource("default-table.json");
    if (url != null) {
      try (Reader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {
        return tablePersistence.readTable(reader);
      } catch (IOException e) {
        System.out.println("Couldn't read default-table.json, so rigging Table manually (" + e + ")");
      }
    }

    // If error when trying to read default-table.json, use this data:
    Table manualTable = new Table(new Team("Manual", 10), new Team("Manual2", 12));
        
    return manualTable;
  }    
}
