package com.maxmind.geoip2.webservice;

import com.maxmind.geoip2.exception.*;
import com.maxmind.geoip2.model.*;
import java.util.*;
import java.io.*;
import java.net.*;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;


/**
 * Unit test for simple App.
 */
public class ClientTest
    extends TestCase
{
    Connection connection;
    int port;

    private class MockServer implements Container {
      private String json_String;
      public MockServer() {
        initJsonString();
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
        sb.append("\"ip_address\":\"1.2.3.4\",");
        sb.append("}}");
        json_String = sb.toString();
      }
      public void handle(Request request, Response response) {
        try {
          String path = request.getPath().toString();
          String ipAddress = path.substring(path.lastIndexOf('/')+1);
          PrintStream body = response.getPrintStream();
          if (ipAddress.equals("1.2.3.4")) {
            setResponse(response,"country",200);
            body.print(json_String);
          }
          if (ipAddress.equals("1.2.3.5")) {
            setResponse(response,"country",200);
          }
          if (ipAddress.equals("1.2.3.6")) {
            setResponse(response,"error",400);
            body.print("{");
            body.print("\"code\":\"IP_ADDRESS_INVALID\",");
            body.print("\"error\":\"The value 1.2.3 is not a valid ip address\"");
            body.print("}");
          }
          if (ipAddress.equals("1.2.3.7")) {
            setResponse(response,"error",400);
          }
          if (ipAddress.equals("1.2.3.8")) {
            setResponse(response,"error",400);
            body.print("{");
            body.print("\"weird\":42");
            body.print("}");
          }
          if (ipAddress.equals("1.2.3.9")) {
            setResponse(response,"error",400);
            body.print("{ invalid: }");
          }
          if (ipAddress.equals("1.2.3.10")) {
            setResponse(response,"",500);
          }
          if (ipAddress.equals("1.2.3.11")) {
            setResponse(response,"",300);
          }
          if (ipAddress.equals("1.2.3.12")) {
            setResponse(response,"error",406,"text/plain");
            body.print("Cannot satisfy your Accept-Charset requirements");
          }
          body.close();
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
      private void setResponse(Response response,String endpoint,int status) {
        setResponse(response,endpoint,status,null);
      }
      private void setResponse(Response response,String endpoint,int status,String content_type) {
        if (content_type != null) {
          response.setValue("Content-Type", content_type);
        } else {
          response.setValue("Content-Type",
           "application/vnd.maxmind.com-" +
           endpoint + "+json; charset=UTF-8; version=1.0");
        }
        response.setCode(status);
      }

    }
    public ClientTest( String testName )
    {
        super( testName );
    }
    public static Test suite()
    {
        return new TestSuite( ClientTest.class );
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
    public void setUp() throws java.lang.Exception {
        Random rand = new Random();
        for (int i = 0;i < 256;i++) {
          port = 30000 + rand.nextInt(35000);
          if (available(port) == true) {break;}
        }
        connection = setup_mock_server(port);

    }
    public void tearDown() throws java.lang.Exception {
      connection.close();
    }
    Connection setup_mock_server(int port) throws Exception {
      Container container = new MockServer();
      Server server = new ContainerServer(container);
      Connection connection = new SocketConnection(server);
      SocketAddress address = new InetSocketAddress(port);
      connection.connect(address);
      return connection;
    }
    public void testCountry() {
      try {
        Client client = new Client("42","abcdef123456");
        client.setHost("localhost:" + port);
        Country country = client.Country("1.2.3.4");

        assertEquals(
          "country.getContinent().getCode() does not return NA",
           "NA",
           country.getContinent().getCode()
        );
        assertEquals(
          "country.getContinent().getGeoNameId() does not return 42",
           42,
           country.getContinent().getGeoNameId()
        );
        assertEquals(
"country.getContinent().getName(\"en\") does not return North America",
         "North America",
         country.getContinent().getName("en")
        );
        assertEquals(
        "country.getCountry().getCode() does not return US",
         "US",
         country.getCountry().getCode()
        );
        assertEquals(
        "country.getCountry().getGeoNameId() does not return 1",
         1,
         country.getCountry().getGeoNameId()
        );
        assertEquals(
"country.getCountry().getName(\"en\") does not return United States",
         "United States",
         country.getCountry().getName("en")
        );

      } catch (Exception e) {
        fail(e.getMessage());
      }
    }
    public void testClientExceptions() {
      Client client = new Client("42","abcdef123456");
      client.setHost("localhost:" + port);
      try {
        Country country = client.Country("1.2.3.5");
        fail("no exception thrown when response status is 200 but body is not valid JSON");
      } catch (GeoIP2Exception e) {
        //System.out.println(e.getMessage());
        //e.printStackTrace();
        String json = "could not decode the response as JSON";
        if (e.getMessage().indexOf("message body") == -1) {
          fail("1.2.3.5 error does not contain expected text");
        }
      }
      try {
        Country country = client.Country("1.2.3.6");
        fail("no WebServiceException thrown when webservice returns a 4xx error");
      } catch (WebServiceException e) {
        assertEquals(
          "exception object does not contain expected code",
          "IP_ADDRESS_INVALID",e.getCode()
        );
        assertEquals(
          "exception object does not contain expected http_status",
           400,e.getHttpStatus()
        );
        String msg = "The value 1.2.3 is not a valid ip address";
        if (e.getMessage().indexOf(msg) == -1) {
          fail("1.2.3.6 error does not contain expected text");
        }
      } catch (GeoIP2Exception e) {
          fail("Wrong exception typ thrown");
      }
      try {
        Country country = client.Country("1.2.3.7");
        fail("no HTTPException thrown when webservice returns a 4xx error without a body");
      } catch (HTTPException e) {
        if (e.getMessage().indexOf("Received a 400 error for") == -1) {
          fail("1.2.3.7 error does not contain expected text");
        }
        if (e.getMessage().indexOf("with no body") == -1) {
          fail("1.2.3.7 error does not contain expected text");
        }
      } catch (GeoIP2Exception e) {
          fail("Wrong exception typ thrown");
      }
      try {
        Country country = client.Country("1.2.3.8");
        fail("no HTTPException thrown when webservice returns a 4xx error with a JSON body but no code and error keys");
      } catch (HTTPException e) {
        String msg = "it did not include the expected JSON body: Response contains JSON but it does not specify code or error keys";
        System.out.println(e.getMessage());
        if (e.getMessage().indexOf(msg) == -1) {
          fail("1.2.3.8 error does not contain expected text");
        }
      } catch (GeoIP2Exception e) {
          fail("Wrong exception typ thrown");
      }
      try {
        Country country = client.Country("1.2.3.9");
        fail("no HTTPException thrown when webservice returns a 4xx error with a non-JSON body");
      } catch (HTTPException e) {
        String msg = "it did not include the expected JSON body:";
        if (e.getMessage().indexOf(msg) == -1) {
          fail("1.2.3.9 error does not contain expected text");
        }
      } catch (GeoIP2Exception e) {
          fail("Wrong exception typ thrown");
      }
      try {
        Country country = client.Country("1.2.3.10");
        fail("no HTTPException thrown when webservice returns a 5xx error");
      } catch (HTTPException e) {
        String msg = "Received a server error (500) for";
        if (e.getMessage().indexOf(msg) == -1) {
          fail("1.2.3.10 error does not contain expected text");
        }
      } catch (GeoIP2Exception e) {
          fail("Wrong exception typ thrown");
      }
      try {
        Country country = client.Country("1.2.3.11");
        fail("no HTTPException thrown when webservice returns a 3xx error");
      } catch (HTTPException e) {
        String msg = "Received a very surprising HTTP status (300) for";
        if (e.getMessage().indexOf(msg) == -1) {
          fail("1.2.3.11 error does not contain expected text");
        }
      } catch (GeoIP2Exception e) {
          fail("Wrong exception typ thrown");
      }
      try {
        Country country = client.Country("1.2.3.12");
        fail("no HTTPException thrown when webservice returns a 406 error");
      } catch (HTTPException e) {
        String msg = "Cannot satisfy your Accept-Charset requirements";
        if (e.getMessage().indexOf(msg) == -1) {
          fail("1.2.3.12 error does not contain expected text");
        }
      } catch (GeoIP2Exception e) {
          fail("Wrong exception typ thrown");
      }
    }
}
