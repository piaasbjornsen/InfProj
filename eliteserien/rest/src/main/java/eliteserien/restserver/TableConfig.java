package eliteserien.restserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import eliteserien.core.Table;
import eliteserien.core.Team;
import eliteserien.json.TablePersistence;
import eliteserien.restapi.TableService;

/**
 * Configures the rest service (JSON support with Jackson)
 * Child of ResourceConfig, as this is what we are doing, but for our app specifically
 * So that we can use  TablePersistence ?????
 * 
*/

public class TableConfig extends ResourceConfig {
   
  private Table table; // isetedenfor todomodel????????????
  private TablePersistence tablePersistence;

    /**
     * Initialize this EliteserienConfig
     * 
     * binding: 
     * to bind is to make an association 
     * between two or more programming objects
     * 
     * @param table instance to serve
    */

    public TableConfig(Table table) {
        setTable(table);
        tablePersistence = new TablePersistence();
        tablePersistence.setFileName("server-eliteserien.json");
        tablePersistence.setFilePath();
        register(TableService.class);
        register(TableModuleObjectMapperProvider.class);
        register(JacksonFeature.class);
        
        // AbstractBinder(): Skeleton implementation of injection binder with convenience methods for binding definitions.
        register(new AbstractBinder() {
            // Implement configure to provide binding definitions using the exposed binding methods.
            @Override
            protected void configure() {
                // bind: Start building a new class-based service binding. Does NOT bind the service type itself as a contract type.
                bind(TableConfig.this.table);
                bind(TableConfig.this.tablePersistence);
            }
        });
    }

    /**
     * Initialize this ElitserienConfig with default Table
    */

    public TableConfig() {
        this(createDefaultTable());
    }

    public Table getTable() {
        return this.table;
      }
    
    public void setTable(Table table) {
        this.table = table;
    }
    
    private static Table createDefaultTable() {
        TablePersistence tablePersistence = new TablePersistence();
        URL url = TablePersistence.class.getResource("default-table.json");
        if (url != null) {
          try (Reader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {
            return tablePersistence.readTable(reader);
          } catch (IOException e) {
            System.out.println("Couldn't read default-eliteserien.json, so rigging Table manually ("
                + e + ")");
          }
        }
        Table manualTable = new Table(new Team("Manual", 10), new Team("Manual2", 12));
        
        /// Legg til nye tabller, men trenger å merge for å få denne funksjonaliteten
        // table.addTable(new Table("Første divisjon"));
        // table.addTable(new Table("Andre divisjon"));
        
        return manualTable;
      }
    
}
