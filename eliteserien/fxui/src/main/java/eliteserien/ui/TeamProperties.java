package eliteserien.ui;

import javafx.beans.property.SimpleStringProperty;

public class TeamProperties {
    private SimpleStringProperty name;
    private SimpleStringProperty points;
    

    TeamProperties (String name, String points) {
        this.name = new SimpleStringProperty(name);
        this.points = new SimpleStringProperty(points);
    }

    public String getName() {
        return name.get();
    }

    public String getPoints() {
        return points.get();
    }
}
