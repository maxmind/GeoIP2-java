package com.maxmind.geoip2.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {

    private String insightsBody = " {\n"
            + "    \"maxmind\" : {\n"
            + "       \"queries_remaining\" : 11\n"
            + "    },\n"
            + "    \"registered_country\" : {\n"
            + "       \"geoname_id\" : 2,\n"
            + "       \"names\" : {\n"
            + "          \"en\" : \"Canada\"\n"
            + "       },\n"
            + "       \"iso_code\" : \"CA\"\n"
            + "    },\n"
            + "    \"traits\" : {\n"
            + "       \"is_anonymous_proxy\" : true,\n"
            + "       \"autonomous_system_number\" : 1234,\n"
            + "       \"isp\" : \"Comcast\",\n"
            + "       \"ip_address\" : \"1.2.3.4\",\n"
            + "       \"is_satellite_provider\" : true,\n"
            + "       \"autonomous_system_organization\" : \"AS Organization\",\n"
            + "       \"user_type\" : \"college\",\n"
            + "       \"organization\" : \"Blorg\",\n"
            + "       \"domain\" : \"example.com\"\n"
            + "    },\n"
            + "    \"country\" : {\n"
            + "       \"names\" : {\n"
            + "          \"en\" : \"United States of America\"\n"
            + "       },\n"
            + "       \"geoname_id\" : 1,\n"
            + "       \"iso_code\" : \"US\",\n"
            + "       \"confidence\" : 99\n"
            + "    },\n"
            + "    \"continent\" : {\n"
            + "       \"names\" : {\n"
            + "          \"en\" : \"North America\"\n"
            + "       },\n"
            + "       \"code\" : \"NA\",\n"
            + "       \"geoname_id\" : 42\n"
            + "    },\n"
            + "    \"location\" : {\n"
            + "       \"time_zone\" : \"America/Chicago\",\n"
            + "       \"accuracy_radius\" : 1500,\n"
            + "       \"metro_code\" : 765,\n"
            + "       \"latitude\" : 44.98,\n"
            + "       \"longitude\" : 93.2636\n"
            + "    },\n"
            + "    \"subdivisions\" : [\n"
            + "       {\n"
            + "          \"confidence\" : 88,\n"
            + "          \"iso_code\" : \"MN\",\n"
            + "          \"geoname_id\" : 574635,\n"
            + "          \"names\" : {\n"
            + "             \"en\" : \"Minnesota\"\n"
            + "          }\n"
            + "       },\n"
            + "       {\n"
            + "          \"iso_code\" : \"TT\"\n"
            + "       }\n"
            + "    ],\n"
            + "    \"represented_country\" : {\n"
            + "       \"geoname_id\" : 3,\n"
            + "       \"names\" : {\n"
            + "          \"en\" : \"United Kingdom\"\n"
            + "       },\n"
            + "       \"type\" : \"C<military>\",\n"
            + "       \"iso_code\" : \"GB\"\n"
            + "    },\n"
            + "    \"postal\" : {\n"
            + "       \"code\" : \"55401\",\n"
            + "       \"confidence\" : 33\n"
            + "    },\n"
            + "    \"city\" : {\n"
            + "       \"confidence\" : 76,\n"
            + "       \"geoname_id\" : 9876,\n"
            + "       \"names\" : {\n"
            + "          \"en\" : \"Minneapolis\"\n"
            + "       }\n"
            + "    }\n"
            + " }\n";

    @Test
    public void testSerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        InjectableValues inject = new InjectableValues.Std().addValue(
                "locales", new ArrayList<String>());
        InsightsResponse response = mapper.reader(InsightsResponse.class)
                .with(inject).readValue(this.insightsBody);
        JsonNode expectedNode = mapper.readValue(this.insightsBody,
                JsonNode.class);
        JsonNode actualNode = mapper.readValue(response.toJson(),
                JsonNode.class);

        assertEquals(expectedNode, actualNode);
    }

}
