package eliteserien.json.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import eliteserien.core.Match;

class matchesPlayedDeserializer extends JsonDeserializer<Match> {

  @Override
  public Match deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  Match deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode objectNode) {
      Match match = new Match();
      JsonNode name1Node = objectNode.get("name1");
      if (name1Node instanceof TextNode) {
        match.setTeam1Name(name1Node.asText());
      }
      JsonNode name2Node = objectNode.get("name2");
      if (name2Node instanceof TextNode) {
        match.setTeam2Name(name2Node.asText());
      }
      JsonNode points1Node = objectNode.get("points1");
      if (points1Node instanceof IntNode) {
        match.setTeam1Points(points1Node.asInt());
      }
      JsonNode points2Node = objectNode.get("points2");
      if (points2Node instanceof IntNode) {
        match.setTeam2Points(points2Node.asInt());
      }
      return match;
    }
    return null;
  }   
}
