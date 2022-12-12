package com.maxmind.geoip2;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import com.maxmind.db.Network;
import com.maxmind.geoip2.record.Traits;
import java.util.List;

class JsonInjector extends InjectableValues {
    private final List<String> locales;
    private final String ip;
    private final Network network;

    public JsonInjector(List<String> locales, String ip, Network network) {
        this.locales = locales;
        this.ip = ip;
        this.network = network;
    }

    @Override
    public Object findInjectableValue(Object valueId, DeserializationContext ctxt,
                                      BeanProperty forProperty, Object beanInstance) {
        if ("locales".equals(valueId)) {
            return locales;
        }
        if ("ip_address".equals(valueId)) {
            return ip;
        }
        if ("network".equals(valueId)) {
            return network;
        }
        if ("traits".equals(valueId)) {
            return new Traits(ip, network);
        }
        return null;
    }
}