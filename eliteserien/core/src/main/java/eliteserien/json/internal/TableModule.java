package eliteserien.json.internal;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eliteserien.core.Table;


@SuppressWarnings("serial")
public class TableModule extends SimpleModule {
    
    private static final String NAME = "TableModule";
    
    public TableModule(boolean deepTableModuleSerializer) {
    super(NAME, Version.unknownVersion());
    addSerializer(Table.class, new TableSerializer());
    addDeserializer(Table.class, new TableDeserializer());
    }

    public TableModule() {
        this(true);
    }
}