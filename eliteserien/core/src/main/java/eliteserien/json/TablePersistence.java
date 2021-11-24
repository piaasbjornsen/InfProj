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
 * The Table.json file in user.home folder can then be modified while program is running.
 * ReadInitialTable method reads the json-file in resource folder with initial teams and values.
 */

public class TablePersistence {

  private ObjectMapper mapper;  // Provides functionality for reading and writing JSON
  private String fileName;
  private Path filePath = null;

  /**
   * Constructor
   * Makes a new and empty TableModule ready for json-file reading and writing
  */

  public TablePersistence() {
    TableModule tableModule = new TableModule();
    mapper = new ObjectMapper().registerModule(tableModule);
  }

  /**
   * Sets name for json-file
   * @param fileName
  */

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Sets the file parth to the json-file
  */
  
  public void setFilePath() {
    this.filePath = Paths.get(System.getProperty("user.home"), fileName);
  }

  /**
   * Should load a Table from file (fileName) in the resource folder.
   * @param fileName
   * @return the loaded Table
   * @throws IOException
   */

  public Table loadInitialTable(String fileName) throws IOException {
    setFileName(fileName);
    InputStream inputStream = this.getClass().getResourceAsStream(fileName);
    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    return readTable(reader);
  }

  /**
   * loadSavedTable loads a Table from the saved file (fileName) in the user.home folder
   * @param fileName
   * @return the loaded Table
   * @throws IOException
  */

  public Table loadSavedTable(String fileName) throws IOException {
    setFileName(fileName);
    setFilePath();
    try (Reader reader = new FileReader(filePath.toFile(), StandardCharsets.UTF_8)) {
      return readTable(reader);
    }
  }
  
  /**
   * ReadTable uses Table.json-file from user.home folder to read JSON
   * @param reader
   * @return values in Table.json file from user.home
   * @throws IOException
  */

  public Table readTable(Reader reader) throws IOException {
    return mapper.readValue(reader, Table.class);
  }

  /**
   * writeTable serializes TableObject
   * @param table
   * @param writer
   * @throws IOException
  */

  public void writeTable(Table table, Writer writer) throws IOException {
    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, table);
  }

  /**
   * saveTable saves the input table as a json-file in user.home folder.
   * @param table
   * @param fileName
   * @throws IOException
  */

  public void saveTable(Table table, String fileName) throws IOException {
    setFileName(fileName);
    setFilePath();
    try (Writer writer = new FileWriter(filePath.toFile(), StandardCharsets.UTF_8)) {
      writeTable(table, writer);
    } 
  }

  public void saveTable(Table table) throws IOException {
    try (Writer writer = new FileWriter(filePath.toFile(), StandardCharsets.UTF_8)) {
      writeTable(table, writer);
    }
  }

    /**
   * Used in RemoteEliteserienAccess
   * @return ObjectMapper
   */
  public static ObjectMapper createObjectMapper() {
    return new ObjectMapper()
      .registerModule(new TableModule());
  }


}
