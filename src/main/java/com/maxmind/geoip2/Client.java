package com.maxmind.geoip2;

import com.maxmind.geoip2.model.*;
import java.lang.*;
import java.util.*;
import java.io.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.auth.*;
import org.apache.http.auth.*;
import org.json.*;

public class Client
{
  private String user_id;
  private String license_key;
  private String host;
  Client(String user_id, String license_key) {
    this.user_id = user_id;
    this.license_key = license_key;
    this.host = "geoip.maxmind.com";
  }
  Country Country(String ip_address) {
    JSONObject json = response_for("country",ip_address);
    if (json != null) {
      return new Country(json);
    }
    return null;
  }
  City City(String ip_address) {
    JSONObject json = response_for("city",ip_address);
    if (json != null) {
      return new City(json);
    }
    return null;
  }
  Omni Omni(String ip_address) {
    JSONObject json = response_for("omni",ip_address);
    if (json != null) {
      return new Omni(json);
    }
    return null;
  }
  CityISPOrg CityISPOrg(String ip_address) {
    JSONObject json = response_for("city_isp_org",ip_address);
    if (json != null) {
      return new CityISPOrg(json);
    }
    return null;
  }


  public void setHost(String host) {
    this.host = host;
  }
  private JSONObject response_for(String path,String ip_address) {
    DefaultHttpClient httpclient = new DefaultHttpClient();
    try {
      //String uri = "https://ct4-test.maxmind.com/geoip/" + path + "/" + ip_address;
      String uri = "https://" + host;
      if (host.startsWith("localhost")) {
        uri = "http://" + host;
      }
      uri = uri + "/geoip/v2.0/" + path + "/" + ip_address;
      HttpGet httpget = new HttpGet(uri);
      httpget.addHeader("Accept","application/json");
      httpget.addHeader(BasicScheme.authenticate(
      new UsernamePasswordCredentials(user_id,license_key),"UTF-8",false));
      HttpResponse response = httpclient.execute(httpget);
      int status = response.getStatusLine().getStatusCode();
      if (status == 200) {
        return handle_success(response, uri);
      } else {
        handle_error_status(response, status, uri);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      httpclient.getConnectionManager().shutdown();
    }
    return null;
  }

  private String get_content(HttpResponse response) throws IOException {
    HttpEntity entity = response.getEntity();
    if (entity == null) {return "";}
    InputStream instream = entity.getContent();
    BufferedReader reader = new BufferedReader(
                 new InputStreamReader(instream));
    String content = reader.readLine();
    instream.close();
    return content;
  }
  private JSONObject handle_success(HttpResponse response, String uri) throws IOException {
    JSONObject json = null;
    try {
      String content = get_content(response);
      if (content == null) {content = "";}
      System.out.println(content);
      json = new JSONObject(content);
    } catch (JSONException e) {
      throw new GenericException("Received a 200 response for "+uri+" but could not decode the response as JSON: \n" + e.getMessage(),e);
    }
    return json;
  }
  private void handle_error_status(HttpResponse response, int status, String uri) throws IOException {
    if ((status >= 400) && (status < 500)) {
      handle_4xx_status(response,status,uri);
    } else if ((status >= 500) && (status < 600)) {
      handle_5xx_status(response,status,uri);
    } else {
      handle_non_200_status(response,status,uri);
    }
  }
  private void handle_4xx_status(HttpResponse response, int status, String uri) throws IOException {
    String JSON_Message = "Received a " + status + " error for " + uri + " but it did not include the expected JSON body: ";
    String content = get_content(response);
    if (content == null) {content = "";}
    System.out.println(content);
    JSONObject body;
    if (content.equals("") == false) {
      String contentType = response.getEntity().getContentType().getValue();
      if (contentType.indexOf("json") != -1) {
        try {
          body = new JSONObject(content);
        } catch (JSONException e) {
          throw new HTTPException(
            JSON_Message + e.getMessage(),
             e,
            status,
            uri
          );
        }
      } else {
        throw new HTTPException(
        "Received a " + status + " error for " + uri + "with the following body: " +
        content,status,uri);
      }
    } else {
       throw new HTTPException(
           "Received a " + status + " error for " + uri + " with no body",
           status,
           uri
       );
    }
    if (body.has("code") & body.has("error")) {
      try {
        String code = body.getString("code");
        String error = body.getString("error");
        throw new WebServiceException(error,code,status,uri);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    } else {
        throw new HTTPException(
           JSON_Message + "Response contains JSON but it does not specify code or error keys",
           status,
           uri
        );
    }
  }
  private void handle_5xx_status(HttpResponse response, int status, String uri) {
    throw new HTTPException(
       "Received a server error (" + status + ") for " + uri,
       status,
       uri
    );

  }
  private void handle_non_200_status(HttpResponse response, int status, String uri) {
    throw new HTTPException(
       "Received a very surprising HTTP status (" + status + ") for " + uri,
       status,
       uri
    );
  }
}
