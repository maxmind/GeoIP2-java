package com.maxmind.geoip2.webservice;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.simpleframework.transport.connect.Connection;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.maxmind.geoip2.exception.GeoIP2Exception;
import com.maxmind.geoip2.exception.HttpException;
import com.maxmind.geoip2.exception.WebServiceException;
import com.maxmind.geoip2.matchers.CodeMatcher;
import com.maxmind.geoip2.matchers.HttpStatusMatcher;
import com.maxmind.geoip2.model.CountryResponse;

/**
 * Unit test for simple App.
 */
public class ClientTest {
    Connection connection;
    int port;

    final static private String countryResponse = "{\"continent\":{"
            + "\"continent_code\":\"NA\"," + "\"geoname_id\":42,"
            + "\"names\":{\"en\":\"North America\"}" + "}," + "\"country\":{"
            + "\"geoname_id\":1," + "\"iso_code\":\"US\","
            + "\"confidence\":56," + "\"names\":{\"en\":\"United States\"}"
            + "}," + "\"registered_country\":{" + "\"geoname_id\":2,"
            + "\"iso_code\":\"CA\"," + "\"names\":{\"en\":\"Canada\"}" + "},"
            + "\"traits\":{" + "\"ip_address\":\"1.2.3.4\"" + "}}";

    HttpTransport transport = new MockHttpTransport() {
        @Override
        public LowLevelHttpRequest buildRequest(String method, final String url)
                throws IOException {
            return new MockLowLevelHttpRequest() {
                @Override
                public LowLevelHttpResponse execute() throws IOException {
                    String ipAddress = url.substring(url.lastIndexOf('/') + 1);
                    switch (ipAddress) {
                        case "1.2.3.4":
                            return this.getResponse("country", 200,
                                    "application/json", countryResponse);
                        case "1.2.3.5":
                            return this.getResponse("country", 200);
                        case "1.2.3.6":
                            String body = "{\"code\":\"IP_ADDRESS_INVALID\","
                                    + "\"error\":\"The value 1.2.3 is not a valid ip address\"}";
                            return this.getResponse("error", 400,
                                    "application/json", body);
                        case "1.2.3.7":
                            return this.getResponse("error", 400);
                        case "1.2.3.8":
                            return this.getResponse("error", 400,
                                    "application/json", "{\"weird\":42}");
                        case "1.2.3.9":
                            return this.getResponse("error", 400,
                                    "application/json", "{ invalid: }");
                        case "1.2.3.10":
                            return this.getResponse("", 500);
                        case "1.2.3.11":
                            return this.getResponse("", 300);
                        case "1.2.3.12":
                            return this
                                    .getResponse("error", 406, "text/plain",
                                            "Cannot satisfy your Accept-Charset requirements");
                        default:
                            return this.getResponse("", 404);
                    }
                }

                private LowLevelHttpResponse getResponse(String endpoint,
                        int status) {
                    return this.getResponse(endpoint, status, null, "");
                }

                private LowLevelHttpResponse getResponse(String endpoint,
                        int status, String content_type, String body) {
                    MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                    response.addHeader("Content-Length",
                            String.valueOf(body.length()));
                    response.setStatusCode(status);

                    if (content_type != null) {
                        response.setContentType(content_type);
                    } else {
                        response.setContentType("application/vnd.maxmind.com-"
                                + endpoint
                                + "+json; charset=UTF-8; version=1.0");
                    }

                    response.setContent(body);
                    return response;
                }
            };
        }
    };

    Client client = new Client(42, "abcdef123456", this.transport);

    @Test
    public void testCountry() throws GeoIP2Exception {
        CountryResponse country = this.client.country("1.2.3.4");
        assertEquals("country.getContinent().getCode() does not return NA",
                "NA", country.getContinent().getCode());
        assertEquals(
                "country.getContinent().getGeoNameId() does not return 42", 42,
                country.getContinent().getGeoNameId().intValue());
        assertEquals(
                "country.getContinent().getName(\"en\") does not return North America",
                "North America", country.getContinent().getName("en"));
        assertEquals("country.getCountry().getCode() does not return US", "US",
                country.getCountry().getIsoCode());
        assertEquals("country.getCountry().getGeoNameId() does not return 1",
                1, country.getCountry().getGeoNameId().intValue());
        assertEquals(
                "country.getCountry().getName(\"en\") does not return United States",
                "United States", country.getCountry().getName("en"));

    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void noBody() throws GeoIP2Exception {
        this.exception.expect(GeoIP2Exception.class);
        this.exception.expectMessage(containsString("message body"));

        this.client.country("1.2.3.5");
    }

    @Test
    public void webServiceError() throws GeoIP2Exception {
        this.exception.expect(WebServiceException.class);
        this.exception.expect(CodeMatcher.hasCode("IP_ADDRESS_INVALID"));
        this.exception.expect(HttpStatusMatcher.hasStatus(400));
        this.exception
                .expectMessage(containsString("The value 1.2.3 is not a valid ip address"));

        this.client.country("1.2.3.6");
    }

    @Test
    public void noErrorBody() throws GeoIP2Exception {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Received a 400 error for https://geoip.maxmind.com/geoip/v2.0/country/1.2.3.7 with no body"));

        this.client.country("1.2.3.7");
    }

    @Test
    public void weirdErrorBody() throws GeoIP2Exception {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Response contains JSON but it does not specify code or error keys"));

        this.client.country("1.2.3.8");
    }

    @Test
    public void unexpectedErrorBody() throws GeoIP2Exception {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("it did not include the expected JSON body:"));

        this.client.country("1.2.3.9");
    }

    @Test
    public void internalServerError() throws GeoIP2Exception {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Received a server error (500) for"));
        this.client.country("1.2.3.10");
    }

    @Test
    public void surprisingStatus() throws GeoIP2Exception {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Received a very surprising HTTP status (300) for"));

        this.client.country("1.2.3.11");
    }

    @Test
    public void cannotAccept() throws GeoIP2Exception {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Cannot satisfy your Accept-Charset requirements"));
        this.client.country("1.2.3.12");
    }
}
