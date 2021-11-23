module eliteserien.rest {
    requires jakarta.ws.rs;

    requires jersey.common;
    requires jersey.server;
    requires jersey.media.json.jackson;

    requires org.glassfish.hk2.api;
    requires org.slf4j;

    requires eliteserien.core;

    opens eliteserien.restapi to jersey.server;
}
