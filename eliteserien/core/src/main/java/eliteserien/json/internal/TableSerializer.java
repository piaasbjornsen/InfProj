package eliteserien.json.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

import eliteserien.core.Team;
import eliteserien.core.Table;


 /**
  * TableSerializer: Serializer for Table objects
  */

class TableSerializer extends JsonSerializer<Table> {

  /**
   * Serialize:
   * Takes a Table object as input
   * Adds the object to a json-file in this format:
   * format: { "Table": [ {"teamname": , "points": }, ... ] }
   * @param Table object
   * @param JsonGenerator
   * @param SerializerProvider
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
