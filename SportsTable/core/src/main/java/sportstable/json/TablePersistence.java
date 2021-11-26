package sportstable.json;

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

import sportstable.core.Table;
import sportstable.json.internal.TableModule;

/**
 * Persistence class for json-file reading and writing. The Table.json file in
 * user.home folder can then be modified while program is running.
 */

public class TablePersistence {

  private ObjectMapper mapper; // Provides functionality for reading and writing JSON
  private String fileName; // Name to json-file for table
  private Path filePath = null; // File path to user.jome folder

  /**
   * Makes a new and empty TableModule ready for json-file reading and writing
   */

  public TablePersistence() {
    TableModule tableModule = new TableModule();
    mapper = new ObjectMapper().registerModule(tableModule);
  }

  /**
   * Sets name for json-file
   * 
   * @param fileName to json-file for table
   */

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Sets the file path to the json-file
   */

  public void setFilePath() {
    this.filePath = Paths.get(System.getProperty("user.home"), fileName);
  }

  /**
   * Reads the json-file in resource folder with initial teams and values. Instead
   * of using filePath, this method uses getResource as stream
   * 
   * @param fileName to json-file for initial table
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
   * Loads a Table from the saved file (fileName) in the user.home folder. This
   * methos uses filePath as stream
   *
   * @param fileName to json-file for table
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
   * Reads table from json-file
   * 
   * @param Reader from InputStreamReader or FileReader
   * @return Table object from json-file
   * @throws IOException
   */

  public Table readTable(Reader reader) throws IOException {
    return mapper.readValue(reader, Table.class);
  }

  /**
   * Writes Table object in json format
   * 
   * @param Table  object
   * @param writer FileWriter
   * @throws IOException
   */

  public void writeTable(Table table, Writer writer) throws IOException {
    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, table);
  }

  /**
   * Saves the table as a json-file in user.home folder or the set file path
   * 
   * @param Table    object
   * @param fileName to json-file for table
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
   * Creates a jackson module for reading and writing Table objects. Used in rest
   * folder
   * 
   * @return ObjectMapper with Table serializer and deserializer
   */

  public static ObjectMapper createObjectMapper() {
    return new ObjectMapper().registerModule(new TableModule());
  }
}
