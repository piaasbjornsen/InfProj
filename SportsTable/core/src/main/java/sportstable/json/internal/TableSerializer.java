package sportstable.json.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

import sportstable.core.Team;
import sportstable.core.Table;

/**
 * Serializer for Table objects
 */

class TableSerializer extends JsonSerializer<Table> {

  /**
   * Takes a Table object as input and adds the object to a json-file in the
   * format as described. 
   * format: { "Table": [ {"teamname": , "points": }, ... ] }
   * 
   * @param Table object
   * @param JsonGenerator for reading Json content
   * @param SerializerProvider to obtaint serializer
   * @throws IOException
   */

  @Override
  public void serialize(Table table, JsonGenerator jsonGen, SerializerProvider serializerProvider) throws IOException {
    jsonGen.writeStartObject();
    jsonGen.writeArrayFieldStart("Table");
    for (Team team : table.getTeams()) {
      jsonGen.writeStartObject();
      jsonGen.writeStringField("teamname", team.getName());
      jsonGen.writeNumberField("points", team.getPoints());
      jsonGen.writeEndObject();
    }
    jsonGen.writeEndArray();
    jsonGen.writeEndObject();
  }
}
