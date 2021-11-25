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
 * 
 * This JAX-RS application use Jackson as the JSON provider, 
 * therefore we need to redefine the default Jackson serialization and deserialization process
 * so that it is custom to this application
*/

@Provider // This way the ContextResolver is automatically discoverd by the JAX-RS runtime during a Provider scanning phase 
@Consumes(MediaType.APPLICATION_JSON) // Tells the setup of the server that the methods in the class deserialize the parameter to make an object requested by the client
@Produces(MediaType.APPLICATION_JSON) // Tells the setup of the server that the return values from the methods in this class will be serialized with JSON back to the client
public class TableModuleObjectMapperProvider implements ContextResolver<ObjectMapper> { // ContextResolver to help customize the Jackson serialization and deserailization
    private final ObjectMapper objectMapper;  // ObjectMapper provides functionality for reading and writing JSON

    /**
     * Registers the Jackson module in ObjectMapper
    */

    public TableModuleObjectMapperProvider() {
        this.objectMapper = TablePersistence.createObjectMapper();
    }
    
    /**
     * Leverage ContextResolver for ObjectMapper,
     * so that Jackson serialisation/dereseilization matches the applications output format in REST responses
     * @return objectMapper
    */

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        return this.objectMapper;
    }
}
