package sportstable.json.internal;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import sportstable.core.Table;

/**
 * A Jackson module for reading and writing to json-files.
 */

@SuppressWarnings("serial")
public class TableModule extends SimpleModule {

    private static final String NAME = "TableModule";

    /**
     * Adds the Serializer and Deserializer for Table objects.
     */

    public TableModule() {
        super(NAME, Version.unknownVersion());
        addSerializer(Table.class, new TableSerializer());
        addDeserializer(Table.class, new TableDeserializer());
    }
}