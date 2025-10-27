package com.maxmind.geoip2;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.maxmind.db.Network;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.*;

final class NetworkDeserializerTest {


    private static Network parse(String jsonString) throws IOException {
        var deserializer = new NetworkDeserializer();
        JsonFactory jf = new JsonFactory();
        try (JsonParser p = jf.createParser(jsonString)) {
            p.nextToken();
            return deserializer.deserialize(p, null);
        }
    }
    private static void assertNetwork(Network n, String addr, int prefix) throws Exception {
        assertNotNull(n);
        assertEquals(InetAddress.getByName(addr), n.networkAddress());
        assertEquals(prefix, n.prefixLength());
    }

    @Test
    void parsesValidIPv4Cidr() throws Exception {
        Network actual = parse("\"1.2.3.0/24\"");
        assertNetwork(actual, "1.2.3.0", 24);
    }

    @Test
    void parsesValidIPv6Cidr() throws Exception {
        Network actual = parse("\"2001:db8::/32\"");
        assertNetwork(actual, "2001:db8::", 32);
    }

    @Test
    void rejectsWhitespaceInCidr() {
        assertThrows(IOException.class, () -> parse("\"  10.0.0.0/8  \""));
    }




    @Test
    void returnsNullOnJsonNull() throws Exception {
        Network actual = parse("null");
        assertNull(actual);
    }

    @Test
    void returnsNullOnBlankString() throws Exception {
        Network actual = parse("\"   \"");
        assertNull(actual);
    }

    @Test
    void throwsOnMissingSlash() {
        assertThrows(IllegalArgumentException.class, () -> parse("\"1.2.3.0\""));
    }

    @Test
    void throwsOnNonNumericPrefix() {
        assertThrows(IllegalArgumentException.class, () -> parse("\"1.2.3.0/xx\""));
    }

    @Test
    void throwsOnOutOfRangePrefixIpv4() {
        assertThrows(IllegalArgumentException.class, () -> parse("\"1.2.3.0/64\""));
        assertThrows(IllegalArgumentException.class, () -> parse("\"1.2.3.0/-1\""));
    }

    @Test
    void throwsOnOutOfRangePrefixIpv6() {
        assertThrows(IllegalArgumentException.class, () -> parse("\"::/129\""));
        assertThrows(IllegalArgumentException.class, () -> parse("\"::/-1\""));
    }

    @Test
    void wrapsUnknownHostInIOException() {
        IOException ex = assertThrows(IOException.class, () -> parse("\"999.999.999.999/24\""));
        assertNotNull(ex.getCause());
    }
}
