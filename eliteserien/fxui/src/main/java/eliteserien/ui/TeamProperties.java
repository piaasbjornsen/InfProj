package eliteserien.ui;

import javafx.beans.property.SimpleStringProperty;

/**
 * Class for properties to football teams
 * Instead of these properties being a string (name) and integer (points), 
 * They are now listenable objects, so the TableView is automatically updated when they change
*/

public class TeamProperties {

    // Attributes: 
    private SimpleStringProperty name;
    private SimpleStringProperty points;
    
    /**
     * Constructor:
     * Makes name and points into listenable objects
     * @param name
     * @param points
    */

    TeamProperties (String name, String points) {
        this.name = new SimpleStringProperty(name);
        this.points = new SimpleStringProperty(points);
    }

    /**
     * getName is for TableView, so that the property Name gets its own coloumn
     * @return name
    */

    public String getName() {
        return name.get();
    }

    /**
     * getPoints is for TableView, so that the property Points gets its own coloumn
     * @return points
    */

    public String getPoints() {
        return points.get();
    }
}
