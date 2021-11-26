module sportstable.rest {
    requires jakarta.ws.rs;

    requires jersey.common;
    requires jersey.server;
    requires jersey.media.json.jackson;

    requires org.glassfish.hk2.api;
    requires org.slf4j;

    requires sportstable.core;

    opens sportstable.restapi to jersey.server;
}
