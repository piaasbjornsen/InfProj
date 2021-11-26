package sportstable.json.internal;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import sportstable.core.Team;
import sportstable.core.Table;

/*
  * Reads json files with Table format. 
  * Returns a Table object containting the Team objects as described in the json file. 
 */

class TableDeserializer extends JsonDeserializer<Table> {

  /**
   * Makes a TreeNode object from JsonParser input and calls for the other
   * deserialize method with this TreeNode as input
   * 
   * @param JsonParser for reading Json content
   * @param DeserializationContext context for deserialization
   * @return Table object
   * @throws IOException
   * @throws JsonProcessingException
   */

  @Override
  public Table deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  /**
   * Makes Table object from json-file. 
   * Content in json-file has format: { "Table": [{"teamname": , "points": }, ... ] }
   * 
   * @param JsonNode treeNode (content in the json-file)
   * @return the Table object
   */

  Table deserialize(JsonNode treeNode) {
    if (treeNode instanceof ObjectNode objectNode) {
      Table table = new Table(); // Makes an empty table object
      JsonNode tableNode = objectNode.get("Table"); // tableNode now contains the data in the "Table" array as described in the json-file (Team name and points)
      if (tableNode instanceof ArrayNode arrayNode) {
        for (JsonNode elementNode : arrayNode) { // Makes a team object for all elements in the Table array
          String teamname = elementNode.get("teamname").toString();
          teamname = teamname.substring(1, teamname.length() - 1); // Removes quotation marks in teamname string.
          int points = Integer.parseInt(elementNode.get("points").toString());
          Team team = new Team(teamname, points);
          if (team != null) {
            table.addTeams(team); // Adds the Team objects to the Table object.
          }
        }
      }
      return table;
    }
    return null;
  }
}
