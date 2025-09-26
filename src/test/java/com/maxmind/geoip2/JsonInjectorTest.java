package com.maxmind.geoip2;

import com.maxmind.db.Network;
import com.maxmind.geoip2.record.Traits;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

final class JsonInjectorTest {

    @Test
    void injectsValuesAndReusesTraits() throws Exception {
        var locales = List.of("en", "fr");
        var ip = "1.2.3.4";
        var network = new Network(InetAddress.getByName("1.2.3.0"), 24);

        var injector = new JsonInjector(locales, ip, network);

        assertEquals(locales, injector.findInjectableValue("locales", null, null, null));
        assertEquals(ip, injector.findInjectableValue("ip_address", null, null, null));
        assertEquals(network, injector.findInjectableValue("network", null, null, null));

        var traits1 = injector.findInjectableValue("traits", null, null, null);
        var traits2 = injector.findInjectableValue("traits", null, null, null);
        assertNotNull(traits1);
        assertSame(traits1, traits2);
        assertEquals(ip, ((Traits) traits1).getIpAddress());
        assertEquals(network, ((Traits) traits1).getNetwork());
    }

    @Test
    void unknownOrNonStringKeyReturnsNull() {
        var injector = new JsonInjector(List.of(), null, null);
        assertNull(injector.findInjectableValue("unknown", null, null, null));
        assertNull(injector.findInjectableValue(123, null, null, null));
    }
}
