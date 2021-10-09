package eliteserien.json;

import com.fasterxml.jackson.databind.ObjectMapper;

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

/**
 * Persistence class for json-file reading and writing. 
 * ReadInitialTable method reads the json-file in resource folder with initial teams and values.
 * ReadTable and saveTable methods uses Table.json-file from user.home folder.
 * The Table.json file in user.home folder can then be modified while program is running.
 */


public class TablePersistence {

  private ObjectMapper mapper;
  private String fileName;
  private Path filePath = null;

  public TablePersistence() {
    TableModule tableModule = new TableModule();
    mapper = new ObjectMapper()
    .registerModule(tableModule);
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setFilePath() {
    this.filePath = Paths.get(System.getProperty("user.home"), fileName);
  }


  /**
   * Should load a Table from file (fileName) in the resource folder.
   *
   * @return the loaded Table
   */

  public Table loadInitialTable(String fileName) throws IOException {
    setFileName(fileName);
    try {
      InputStream inputStream = this.getClass().getResourceAsStream(fileName);
      InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
      return readTable(reader);
    }
    catch (IOException e) {
      System.out.println("Could not load Table from" + fileName);
    }
    return new Table();
  }

    /**
   * Should load a Table from the saved file (fileName) in the user.home folder
   *
   * @return the loaded Table
   */

  public Table loadSavedTable(String fileName) throws IOException {
    setFileName(fileName);
    setFilePath();
    try (Reader reader = new FileReader(filePath.toFile(), StandardCharsets.UTF_8)) {
        return readTable(reader);
    }
  }
  
  public Table readTable(Reader reader) throws IOException {
    return mapper.readValue(reader, Table.class);
  }

  public void writeTable(Table table, Writer writer) throws IOException {
    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, table);
  }

    /**
   * Should save the input table as a json-file in user.home folder.
   *
   * 
   */

  public void saveTable(Table table, String fileName) throws IOException {
    setFileName(fileName);
    setFilePath();
    try (Writer writer = new FileWriter(filePath.toFile(), StandardCharsets.UTF_8)) {
      writeTable(table, writer);
    } catch (IOException e) {
      System.out.println("Could not save table");
    }
  }
}
