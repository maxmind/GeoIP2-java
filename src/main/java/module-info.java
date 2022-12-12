module com.maxmind.db {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.maxmind.db;
    requires java.net.http;

    exports com.maxmind.geoip2;
    exports com.maxmind.geoip2.exception;
    exports com.maxmind.geoip2.model;
    exports com.maxmind.geoip2.record;
}
