@SuppressWarnings("module") // suppress terminal digit warning
module com.maxmind.geoip2 {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires transitive com.maxmind.db;
    requires java.net.http;

    exports com.maxmind.geoip2;
    exports com.maxmind.geoip2.exception;
    exports com.maxmind.geoip2.model;
    exports com.maxmind.geoip2.record;
}
