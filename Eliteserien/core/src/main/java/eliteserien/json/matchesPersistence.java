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
import todolist.core.TodoModel;
import todolist.json.internal.TodoModule;

/**
 * Wrapper class for JSON serialization,
 * to avoid direct compile dependencies on Jackson for other modules.
 */
public class matchesPersistence {

  private ObjectMapper mapper;

  public matchesPersistence() {
    mapper = createObjectMapper();
  }

  public static SimpleModule createJacksonModule(boolean deep) {
    return new TableModule(deep);
  }

  public static ObjectMapper createObjectMapper() {
    return new ObjectMapper()
      .registerModule(createJacksonModule(true));
  }

  public TableModel readTableModel(Reader reader) throws IOException {
    return mapper.readValue(reader, TableModel.class);
  }

  public void writeTableModel(TableModel tableModel, Writer writer) throws IOException {
    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, tableModel);
  }

  private Path saveFilePath = null;

  public void setSaveFile(String saveFile) {
    this.saveFilePath = Paths.get(System.getProperty("user.home"), saveFile);
  }

  /**
   * Loads a TableModel from the saved file (saveFilePath) in the user.home folder.
   *
   * @return the loaded TableModel
   */
  public TableModel loadTableModel() throws IOException, IllegalStateException {
    if (saveFilePath == null) {
      throw new IllegalStateException("Save file path is not set, yet");
    }
    try (Reader reader = new FileReader(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
      return readTableModel(reader);
    }
  }

  /**
   * Saves a TableModel to the saveFilePath in the user.home folder.
   *
   * @param tableModel the TableModel to save
   */
  public void saveTableModel(TableModel tableModel) throws IOException, IllegalStateException {
    if (saveFilePath == null) {
      throw new IllegalStateException("Save file path is not set, yet");
    }
    try (Writer writer = new FileWriter(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
      writeTableModel(tableModel, writer);
    }
  }
}
