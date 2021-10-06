package eliteserien.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import eliteserien.core.Table;
import eliteserien.json.internal.TableModule;

/**
 * Persistence class for json-file reading and writing. 
 * for now it only loads and reads from file using the TableModule class
 * and deSerializer class for Table objects.
 * TODO: implement write and save methods.
 */


public class TablePersistence {

  private ObjectMapper mapper;
  private String saveFilePath;

  public TablePersistence() {
    TableModule tableModule = new TableModule(true);
    mapper = new ObjectMapper()
    .registerModule(tableModule);
  }

  public void setSaveFilePath(String saveFilePath) {
    this.saveFilePath = saveFilePath;
  }

  public Table readTable(Reader reader) throws IOException {
    return mapper.readValue(reader, Table.class);
  }

  /**
   * Should load a Table from the saved file (saveFilePath) in the eliteserien.ui folder.
   *
   * @return the loaded Table
   */
  public Table loadTable() throws IOException, IllegalStateException {
    if (saveFilePath == null) {
      throw new IllegalStateException("SaveFilePath is null");
    }
    InputStream inputStream = this.getClass().getResourceAsStream(saveFilePath);
    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    return readTable(reader);
  }
}
