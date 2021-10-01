package eliteserien.json.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

import eliteserien.core.SoccerTeam;
import eliteserien.core.Table;

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
    for (SoccerTeam team : table.getSoccerTeams()) {
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
