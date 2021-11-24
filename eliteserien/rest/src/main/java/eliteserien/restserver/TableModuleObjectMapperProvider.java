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
*/

@Provider 
@Consumes(MediaType.APPLICATION_JSON) // Tells the setup of the server that the methods in the class deserialize the parameter to make an object requested by the client
@Produces(MediaType.APPLICATION_JSON) // Tells the setup of the server that the return values from the methods in this class will be serialized with JSON back to the client
public class TableModuleObjectMapperProvider implements ContextResolver<ObjectMapper> { // Implements from ContextResolver<T> To supply context information to resource classes and other providers.
    private final ObjectMapper objectMapper;  // ObjectMapper provides functionality for reading and writing JSON

    /**
     * Combines REST-api and core logic ???????????
     * Customizes the objectmapper to the object to call createObjectMapper() in TablePErsistenceClass
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
