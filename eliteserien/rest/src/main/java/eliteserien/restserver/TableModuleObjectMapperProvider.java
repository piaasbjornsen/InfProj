package eliteserien.restserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;


import eliteserien.json.TablePersistence;

/**
 * Provides the Jackson module used for JSON serialization
 * ContextResolver<T> Contract for a provider that supplies context information to resource classes and other providers.
*/


@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TableModuleObjectMapperProvider implements ContextResolver<ObjectMapper> {
    private final ObjectMapper objectMapper;  // ObjectMapper provides functionality for reading and writing JSON

    /**
     * Combines REST-api and core logic ???????????
    */

    public TableModuleObjectMapperProvider() {
        this.objectMapper = TablePersistence.createObjectMapper();
    }
    
    /**
     * Provides context information to resource classes and other providers.
    */

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        return this.objectMapper;
    }
}
