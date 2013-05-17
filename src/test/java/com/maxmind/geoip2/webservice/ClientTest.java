package com.maxmind.geoip2.webservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import com.maxmind.geoip2.exception.GeoIP2Exception;
import com.maxmind.geoip2.exception.HttpException;
import com.maxmind.geoip2.exception.WebServiceException;
import com.maxmind.geoip2.model.Country;

/**
 * Unit test for simple App.
 */
public class ClientTest {
    Connection connection;
    int port;

    private class MockServer implements Container {
        private String json_String;

        public MockServer() {
            this.initJsonString();
        }

        void initJsonString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"continent\":{");
            sb.append("\"continent_code\":\"NA\",");
            sb.append("\"geoname_id\":42,");
            sb.append("\"names\":{\"en\":\"North America\"}");
            sb.append("},");
            sb.append("\"country\":{");
            sb.append("\"geoname_id\":1,");
            sb.append("\"iso_code\":\"US\",");
            sb.append("\"confidence\":56,");
            sb.append("\"names\":{\"en\":\"United States\"}");
            sb.append("},");
            sb.append("\"registered_country\":{");
            sb.append("\"geoname_id\":2,");
            sb.append("\"iso_code\":\"CA\",");
            sb.append("\"names\":{\"en\":\"Canada\"}");
            sb.append("},");
            sb.append("\"traits\":{");
            sb.append("\"ip_address\":\"1.2.3.4\"");
            sb.append("}}");
            this.json_String = sb.toString();
        }

        @Override
        public void handle(Request request, Response response) {
            String path = request.getPath().toString();
            String ipAddress = path.substring(path.lastIndexOf('/') + 1);
            if (ipAddress.equals("1.2.3.4")) {
                this.setResponse(response, "country", 200, "application/json",
                        this.json_String);
            }
            if (ipAddress.equals("1.2.3.5")) {
                this.setResponse(response, "country", 200);
            }
            if (ipAddress.equals("1.2.3.6")) {
                String body = "{\"code\":\"IP_ADDRESS_INVALID\","
                        + "\"error\":\"The value 1.2.3 is not a valid ip address\"}";
                this.setResponse(response, "error", 400, "application/json",
                        body);
            }
            if (ipAddress.equals("1.2.3.7")) {
                this.setResponse(response, "error", 400);
            }
            if (ipAddress.equals("1.2.3.8")) {
                this.setResponse(response, "error", 400, "application/json",
                        "{\"weird\":42}");
            }
            if (ipAddress.equals("1.2.3.9")) {
                this.setResponse(response, "error", 400, "application/json",
                        "{ invalid: }");
            }
            if (ipAddress.equals("1.2.3.10")) {
                this.setResponse(response, "", 500);
            }
            if (ipAddress.equals("1.2.3.11")) {
                this.setResponse(response, "", 300);
            }
            if (ipAddress.equals("1.2.3.12")) {
                this.setResponse(response, "error", 406, "text/plain",
                        "Cannot satisfy your Accept-Charset requirements");
            }
        }

        private void setResponse(Response response, String endpoint, int status) {
            this.setResponse(response, endpoint, status, null, "");
        }

        private void setResponse(Response response, String endpoint,
                int status, String content_type, String body) {
            if (content_type != null) {
                response.setValue("Content-Type", content_type);
            } else {
                response.setValue("Content-Type",
                        "application/vnd.maxmind.com-" + endpoint
                                + "+json; charset=UTF-8; version=1.0");
            }
            response.setCode(status);
            response.setContentLength(body.length());
            try {
                response.getPrintStream().print(body);
                response.getPrintStream().close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static boolean available(int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    fail(e.getMessage());
                }
            }
        }

        return false;
    }

    @Before
    public void setUp() {
        Random rand = new Random();
        for (int i = 0; i < 256; i++) {
            this.port = 30000 + rand.nextInt(35000);
            if (available(this.port) == true) {
                break;
            }
        }
        try {
            this.connection = this.setup_mock_server(this.port);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error setting up tests");
        }
    }

    @After
    public void tearDown() {
        try {
            this.connection.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Error tearing down tests");
        }
    }

    Connection setup_mock_server(int port) throws Exception {
        Container container = new MockServer();
        Server server = new ContainerServer(container);
        Connection connection = new SocketConnection(server);
        SocketAddress address = new InetSocketAddress(port);
        connection.connect(address);
        return connection;
    }

    @SuppressWarnings("boxing")
    @Test
    public void testCountry() {
        try {
            Client client = new Client(42, "abcdef123456");
            client.setHost("localhost:" + this.port);
            Country country = client.country("1.2.3.4");
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
        Client client = new Client(42, "abcdef123456");
        client.setHost("localhost:" + this.port);
        try {
            Country country = client.country("1.2.3.5");
            fail("no exception thrown when response status is 200 but body is not valid JSON");
        } catch (GeoIP2Exception e) {
            if (e.getMessage().indexOf("message body") == -1) {
                fail("1.2.3.5 error does not contain expected text:"
                        + e.getMessage());
            }
        }
        try {
            Country country = client.country("1.2.3.6");
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
            Country country = client.country("1.2.3.7");
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
            Country country = client.country("1.2.3.8");
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
            Country country = client.country("1.2.3.9");
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
            Country country = client.country("1.2.3.10");
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
            Country country = client.country("1.2.3.11");
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
            Country country = client.country("1.2.3.12");
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
