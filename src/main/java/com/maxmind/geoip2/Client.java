package com.maxmind.geoip2;

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
  String user_id;
  String license_key;
  Client(String user_id, String license_key) {
    this.user_id = user_id;
    this.license_key = license_key;
  }
  Country Country(String ip_address) {
    DefaultHttpClient httpclient = new DefaultHttpClient();
    try {
      String uri = "https://geoip.maxmind.com/geoip/country/" + ip_address;
      HttpGet httpget = new HttpGet(uri);
      httpget.addHeader("Accept","application/json");
      httpget.addHeader(BasicScheme.authenticate(
      new UsernamePasswordCredentials(user_id,license_key),"UTF-8",false));
      HttpResponse response = httpclient.execute(httpget);
      int status = response.getStatusLine().getStatusCode();
      if (status == 200) {
        return new Country(handle_success(response, uri));
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
      String c = get_content(response);
      json = new JSONObject(c);
    } catch (JSONException e) {
      throw new RuntimeException("Received a 200 response for "+uri+" but could not decode the response as JSON ",e);
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
    String content = get_content(response);
    JSONObject body;
    if (content.equals("") == false) {
      try {
        body = new JSONObject(content);
      } catch (JSONException e) {
        throw new HTTPException(
           "Received a " + status + " error for " + uri + " but it did not include the expected JSON body",
           status,
           uri
        );
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
           "Response contains JSON but it does not specify code or error keys",
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
       "Received a server error (" + status + ") for " + uri,
       status,
       uri
    );    
  }
}

