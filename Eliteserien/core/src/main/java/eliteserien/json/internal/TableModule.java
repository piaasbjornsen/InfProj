package eliteserien.json.internal;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eliteserien.core.AbstractTable;
import eliteserien.core.Table;
import eliteserien.core.TableModel;
import eliteserien.core.SoccerMatch;


@SuppressWarnings("serial")
public class TableModule extends SimpleModule {
    
    private static final String NAME = "TableModule";
    
    public TableModule(boolean deepTableModuleSerializer) {
    super(NAME, Version.unknownVersion());
    addSerializer(SoccerMatch.class, new matchesPlayedSerializer());
    addDeserializer(SoccerMatch.class, new matchesPlayedDeserializer());
    }

    public TableModule() {
        this(true);
    }
}