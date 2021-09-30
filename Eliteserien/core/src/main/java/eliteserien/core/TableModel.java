package eliteserien.core;

/**
 * The root container of Table-related data.
 */

public class TableModel {
    private AbstractTable table = new AbstractTable("Tippeligaen");

    /**
     * Gets the table with the provided name.
     */
    public AbstractTable getTable(String name) {
        return table;
    }
}