package com.maxmind.geoip2.webservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
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

    @SuppressWarnings("boxing")
    @Test
    public void testCountry() {
        try {
            Client client = new Client(42, "abcdef123456", this.transport);
            CountryResponse country = client.country("1.2.3.4");
            assertEquals("country.getContinent().getCode() does not return NA",
                    "NA", country.getContinent().getCode());
            assertEquals(
                    "country.getContinent().getGeoNameId() does not return 42",
                    42, (int) country.getContinent().getGeoNameId());
            assertEquals(
                    "country.getContinent().getName(\"en\") does not return North America",
                    "North America", country.getContinent().getName("en"));
            assertEquals("country.getCountry().getCode() does not return US",
                    "US", country.getCountry().getIsoCode());
            assertEquals(
                    "country.getCountry().getGeoNameId() does not return 1", 1,
                    (int) country.getCountry().getGeoNameId());
            assertEquals(
                    "country.getCountry().getName(\"en\") does not return United States",
                    "United States", country.getCountry().getName("en"));

        } catch (GeoIP2Exception e) {
            fail(e.toString() + "\n" + e.getMessage());
        }
    }

    @Test
    @SuppressWarnings("unused")
    public void testClientExceptions() {
        Client client = new Client(42, "abcdef123456", this.transport);
        try {
            CountryResponse country = client.country("1.2.3.5");
            fail("no exception thrown when response status is 200 but body is not valid JSON");
        } catch (GeoIP2Exception e) {
            if (e.getMessage().indexOf("message body") == -1) {
                fail("1.2.3.5 error does not contain expected text:"
                        + e.getMessage());
            }
        }
        try {
            CountryResponse country = client.country("1.2.3.6");
            fail("no WebServiceException thrown when webservice returns a 4xx error");
        } catch (WebServiceException e) {
            assertEquals("exception object does not contain expected code",
                    "IP_ADDRESS_INVALID", e.getCode());
            assertEquals(
                    "exception object does not contain expected http_status",
                    400, e.getHttpStatus());
            String msg = "The value 1.2.3 is not a valid ip address";
            if (e.getMessage().indexOf(msg) == -1) {
                fail("1.2.3.6 error does not contain expected text");
            }
        } catch (GeoIP2Exception e) {
            fail("Wrong exception type thrown: " + e + " " + e.getMessage());
        }
        try {
            CountryResponse country = client.country("1.2.3.7");
            fail("no HttpException thrown when webservice returns a 4xx error without a body");
        } catch (HttpException e) {
            if (e.getMessage().indexOf("Received a 400 error for") == -1) {
                fail("1.2.3.7 error does not contain expected text");
            }
            if (e.getMessage().indexOf("with no body") == -1) {
                fail("1.2.3.7 error does not contain expected text");
            }
        } catch (GeoIP2Exception e) {
            fail("Wrong exception type thrown: " + e + " " + e.getMessage());
        }
        try {
            CountryResponse country = client.country("1.2.3.8");
            fail("no HttpException thrown when webservice returns a 4xx error with a JSON body but no code and error keys");
        } catch (HttpException e) {
            String msg = "Response contains JSON but it does not specify code or error keys";
            if (e.getMessage().indexOf(msg) == -1) {
                fail("1.2.3.8 error does not contain expected text");
            }
        } catch (GeoIP2Exception e) {
            fail("Wrong exception type thrown: " + e + " " + e.getMessage());
        }
        try {
            CountryResponse country = client.country("1.2.3.9");
            fail("no HttpException thrown when webservice returns a 4xx error with a non-JSON body");
        } catch (HttpException e) {
            String msg = "it did not include the expected JSON body:";
            if (e.getMessage().indexOf(msg) == -1) {
                fail("1.2.3.9 error does not contain expected text");
            }
        } catch (GeoIP2Exception e) {
            fail("Wrong exception type thrown: " + e + " " + e.getMessage());
        }
        try {
            CountryResponse country = client.country("1.2.3.10");
            fail("no HttpException thrown when webservice returns a 5xx error");
        } catch (HttpException e) {
            String msg = "Received a server error (500) for";
            if (e.getMessage().indexOf(msg) == -1) {
                fail("1.2.3.10 error does not contain expected text");
            }
        } catch (GeoIP2Exception e) {
            fail("Wrong exception type thrown: " + e.getMessage());
        }
        try {
            CountryResponse country = client.country("1.2.3.11");
            fail("no HttpException thrown when webservice returns a 3xx error");
        } catch (HttpException e) {
            String msg = "Received a very surprising HTTP status (300) for";
            if (e.getMessage().indexOf(msg) == -1) {
                fail("1.2.3.11 error does not contain expected text");
            }
        } catch (GeoIP2Exception e) {
            fail("Wrong exception type thrown: " + e + " " + e.getMessage());
        }
        try {
            CountryResponse country = client.country("1.2.3.12");
            fail("no HttpException thrown when webservice returns a 406 error");
        } catch (HttpException e) {
            String msg = "Cannot satisfy your Accept-Charset requirements";
            if (e.getMessage().indexOf(msg) == -1) {
                fail("1.2.3.12 error does not contain expected text");
            }
        } catch (GeoIP2Exception e) {
            fail("Wrong exception type thrown: " + e + " " + e.getMessage());
        }
    }
}
