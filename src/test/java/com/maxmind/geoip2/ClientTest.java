package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;


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
    private class MockServer implements Container {
      private String json_String;
      public MockServer() {
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
          //if (ipAddress.equals("1.2.3.4")) {}
          response.setValue("Content-Type", 
           "application/vnd.maxmind.com-country+json; charset=UTF-8; version=1.0");

          response.setCode(200);
          body.println(json_String);
          body.close();


        } catch(Exception e) {
          e.printStackTrace();
        }
      }
      //private void getResponse(Response response,String endpoint,String status,String body,String bad,String content_type = null) {
        //if (content_type != null) {
        //  response.setValue("Content-Type", content_type);
        //} else {
        //  response.setValue("Content-Type", 
        //   "application/vnd.maxmind.com-" +
        //   endpoint + "+json; charset=UTF-8; version=1.0");
        //}
      //}

    }
    public ClientTest( String testName )
    {
        super( testName );
    }
    public static Test suite()
    {
        return new TestSuite( ClientTest.class );
    }

    Connection setup_mock_server() throws Exception {
      Container container = new MockServer();
      Server server = new ContainerServer(container);
      Connection connection = new SocketConnection(server);
      SocketAddress address = new InetSocketAddress(8080);
      connection.connect(address);
      return connection;
    }
    public void testClient() {
      try {
        Connection connection = setup_mock_server();
        Client client = new Client("42","abcdef123456");
        client.setHost("localhost:8080");
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
        connection.close();
      } catch (Exception e) {
        fail(e.getMessage());
      }
    }

}

