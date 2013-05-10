package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import java.net.*;
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
    Connection setup_mock_server(int port) throws Exception {
      Container container = new MockServer();
      Server server = new ContainerServer(container);
      Connection connection = new SocketConnection(server);
      SocketAddress address = new InetSocketAddress(port);
      connection.connect(address);
      return connection;
    }
    public void testClient() {
      try {
        int port = 30000;
        Random rand = new Random();
        for (int i = 0;i < 256;i++) {
          port = 30000 + rand.nextInt(35000);
          if (available(port) == true) {break;}
        }
        Connection connection = setup_mock_server(port);
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
        connection.close();
      } catch (Exception e) {
        fail(e.getMessage());
      }
    }

}

