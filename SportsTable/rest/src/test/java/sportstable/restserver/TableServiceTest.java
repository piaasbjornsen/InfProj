package sportstable.restserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Iterator;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sportstable.core.Table;
import sportstable.core.Team;
import sportstable.restapi.TableService;

/**
 * TableService test class
 * Mainly copied from todolist-project, but with som edits to match differences in this project.
 * Tests if the rest service can access the table made by TableConfig.
 */


public class TableServiceTest extends JerseyTest {

  /**
   * Sets TableConfig objects and enables LOG TRAFFIC
   * 
   * @return the config object
   */

  @Override
  protected ResourceConfig configure() {
    final TableConfig config = new TableConfig();
    enable(TestProperties.LOG_TRAFFIC);
    enable(TestProperties.DUMP_ENTITY);
    config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "WARNING");
    return config;
  }

  private ObjectMapper objectMapper; // For reading and writing json-table object

  /**
   * Calls for parent class setUp() method
   * Sets the objectmapper, so that the Jackson serialization and deserialization is customized for our app
   */

  @BeforeEach
  @Override
  public void setUp() throws Exception {
    super.setUp();
    objectMapper = new TableModuleObjectMapperProvider().getContext(getClass());
  }

  /**
   * Calls for parent class tearDown method
   * Cleans up after each test
   */

  @AfterEach
  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }

  /**
   * Tests that we get a table with expected teams and team points when requesting GET
   */

  @Test
  public void testGet_table() {
    Response getResponse = target(TableService.SPORTSTABLE_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .get();
    assertEquals(200, getResponse.getStatus());
    try {
      Table table = objectMapper.readValue(getResponse.readEntity(String.class), Table.class);
      Iterator<Team> it = table.iterator();
      assertTrue(it.hasNext());
      Team team1 = it.next();
      assertTrue(it.hasNext());
      Team team2 = it.next();
      assertEquals("Manual2", team1.getName());
      assertEquals("Manual", team2.getName());
    } catch (JsonProcessingException e) {
      fail(e.getMessage());
    }
  }
}
