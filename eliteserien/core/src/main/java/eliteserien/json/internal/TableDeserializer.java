package eliteserien.json.internal;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import eliteserien.core.Team;
import eliteserien.core.Table;

/*
  * TableDeserializer: reads json files with Table format and returns a Table object containting the
  * Team objects as described in the json file. 
  */

/*
* format: { "Table": [ {"teamname": , "points": }, ... ] }
* 
* deserialize method:
* treeNode input is the content in the json-file
* A empty Table object is made.
* tableNode contains the data in the "Table" array as described in the json-file (Team name and points)
* A Team object is made for all elements in the Table array with a for-loop.
* The Team objects is then added to the Table object.
* The Table object is returned.
*/

class TableDeserializer extends JsonDeserializer<Table> {

  @Override
  public Table deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  Table deserialize(JsonNode treeNode) {
    if (treeNode instanceof ObjectNode objectNode) {
      Table table = new Table();
      JsonNode tableNode = objectNode.get("Table");
      if (tableNode instanceof ArrayNode arrayNode) {
        for (JsonNode elementNode : arrayNode) {
          String teamname = elementNode.get("teamname").toString();
          teamname = teamname.substring(1, teamname.length() - 1); // removing quotation marks in teamname string.
          int points = Integer.parseInt(elementNode.get("points").toString());
          Team team = new Team(teamname, points);
          if (team != null) {
            table.addTeams(team);
          }
        }
      }
      return table;
    }
    return null;
  }
}
