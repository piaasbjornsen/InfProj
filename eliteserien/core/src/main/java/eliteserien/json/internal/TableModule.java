package eliteserien.json.internal;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eliteserien.core.Table;

/**
 * A Jackson module for reading and writing to json-files. TableModule
 * constructor adds the Serializer and DeSerializer for Table objects.
 */

@SuppressWarnings("serial")
public class TableModule extends SimpleModule {

    private static final String NAME = "TableModule";

    public TableModule() {
        super(NAME, Version.unknownVersion());
        addSerializer(Table.class, new TableSerializer());
        addDeserializer(Table.class, new TableDeserializer());
    }
}