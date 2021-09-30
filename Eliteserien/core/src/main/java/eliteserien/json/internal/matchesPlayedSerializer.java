package eliteserien.json.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import eliteserien.core.SoccerMatch;

class matchesPlayedSerializer extends JsonSerializer<SoccerMatch> {

  /*
   * format: { "text": "...", "checked": false, "deadline": ... }
   */

  @Override
  public void serialize(SoccerMatch match, JsonGenerator jsonGen, SerializerProvider serializerProvider)
      throws IOException {
    jsonGen.writeStartObject();
    jsonGen.writeStringField("name1", match.getTeam1Name());
    jsonGen.writeNumberField("points1", match.getTeam1Points());
    jsonGen.writeStringField("name2", match.getTeam2Name());
    jsonGen.writeNumberField("points2", match.getTeam2Points());
    jsonGen.writeEndObject();
  }
}