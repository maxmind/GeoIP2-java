package com.maxmind.geoip2;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import com.maxmind.db.Network;
import com.maxmind.geoip2.record.Traits;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class JsonInjector extends InjectableValues {
    private final Map<String, Object> injectables;

    JsonInjector(List<String> locales, String ip, Network network) {
        var map = new HashMap<String, Object>(4, 1f);
        if (locales != null) {
            map.put("locales", locales);
        }
        if (ip != null) {
            map.put("ip_address", ip);
        }
        if (network != null) {
            map.put("network", network);
        }
        map.put("traits", new Traits(ip, network));
        this.injectables = Map.copyOf(map);
    }

    @Override
    public Object findInjectableValue(Object valueId, DeserializationContext ctxt,
                                      BeanProperty forProperty, Object beanInstance) {
        return (valueId instanceof String k) ? injectables.get(k) : null;
    }
}
