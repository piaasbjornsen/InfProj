package eliteserien.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
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
    mapper = createObjectMapper();
  }

  public static SimpleModule createJacksonModule(boolean deep) {
    return new TableModule(deep);
  }

  public static ObjectMapper createObjectMapper() {
    return new ObjectMapper()
      .registerModule(createJacksonModule(true));
  }

  public Table readTable(Reader reader) throws IOException {
    return mapper.readValue(reader, Table.class);
  }

  public void writeTable(Table table, Writer writer) throws IOException {
    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, table);
  }

  private Path saveFilePath = null;

  public void setSaveFile(String saveFile) {
    this.saveFilePath = Paths.get(System.getProperty("user.home"), saveFile);
  }

  /**
   * Loads a Table from the saved file (saveFilePath) in the user.home folder.
   *
   * @return the loaded Table
   */
  public Table loadTable() throws IOException, IllegalStateException {
    if (saveFilePath == null) {
      throw new IllegalStateException("Save file path is not set, yet");
    }
    try (Reader reader = new FileReader(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
      return readTable(reader);
    }
  }

  /**
   * Saves a Table to the saveFilePath in the user.home folder.
   *
   * @param table the Table to save
   */
  public void saveTable(Table table) throws IOException, IllegalStateException {
    if (saveFilePath == null) {
      throw new IllegalStateException("Save file path is not set, yet");
    }
    try (Writer writer = new FileWriter(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
      writeTable(table, writer);
    }
  }
}
