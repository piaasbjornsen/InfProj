package eliteserien.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.net.URISyntaxException;
import java.net.URL;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import eliteserien.core.Table;
import eliteserien.json.internal.TableModule;


public class TablePersistence {

  private ObjectMapper mapper;

  public TablePersistence() {
    TableModule tableModule = new TableModule(true);
    mapper = new ObjectMapper()
    .registerModule(tableModule);
  }

  public Table readTable(Reader reader) throws IOException {
    return mapper.readValue(reader, Table.class);
  }

  /**
   * Loads a Table from the saved file (saveFilePath) in the user.home folder.
   *
   * @return the loaded Table
   */
  public Table loadTable() throws IOException, IllegalStateException {
    InputStream inputStream = this.getClass().getResourceAsStream("Table.json");
    InputStreamReader reader = new InputStreamReader(inputStream);

    return readTable(reader);
  }
}
