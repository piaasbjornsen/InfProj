package eliteserien.json.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

import eliteserien.core.Team;
import eliteserien.core.Table;


 /*
   * TableSerializer: takes a Table object as input and adds the object to a json-file in the
   * format as described.
   */

   /*
   * format: { "Table": [ {"teamname": , "points": }, ... ] }
   */

class TableSerializer extends JsonSerializer<Table> {

  private final boolean deep;

  public TableSerializer(boolean deep) {
    this.deep = deep;
  }

  public TableSerializer() {
    this(true);
  }

  @Override
  public void serialize(Table table, JsonGenerator jsonGen, SerializerProvider serializerProvider) throws IOException {
    jsonGen.writeStartObject();
    jsonGen.writeArrayFieldStart("Table");
    for (Team team : table.getTeams()) {
      if (deep) {
        jsonGen.writeObject(team);
      } 
      else {
        jsonGen.writeStartObject();
        jsonGen.writeStringField("teamname", team.getName());
        jsonGen.writeNumberField("points", team.getPoints());
        jsonGen.writeEndObject();
      }
    }
    jsonGen.writeEndArray();
    jsonGen.writeEndObject();
  }
}
