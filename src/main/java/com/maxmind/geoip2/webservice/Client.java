package com.maxmind.geoip2.webservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.maxmind.geoip2.exception.*;
import com.maxmind.geoip2.model.*;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
    private HttpTransport transport;
    static final private JsonFactory JSON_FACTORY = new JacksonFactory();
    private int userId;
    private String licenseKey;
    private String host =  "geoip.maxmind.com";

    public Client(int userId, String licenseKey) {
        this.userId = userId;
        this.licenseKey = licenseKey;
        this.transport = new NetHttpTransport();
    }
    
    public Client(int userId, String licenseKey, HttpTransport transport) {
        this.userId = userId;
        this.licenseKey = licenseKey;
        this.transport = transport;
    }
    

    public Country country(String ipAddress) throws GeoIP2Exception {
        HttpResponse response = responseFor("country", ipAddress);
        try {
            return response.parseAs(Country.class);
        } catch (IOException e) {
            throw new GeoIP2Exception("Received a 200 response but not decode it as JSON: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new GeoIP2Exception("Received a 200 response but not decode it as JSON: " + e.getMessage());
        }
    }

    public City city(String ipAddress) throws GeoIP2Exception {
        HttpResponse response = responseFor("city", ipAddress);
        try {
            return response.parseAs(City.class);
        } catch (IOException e) {
            throw new GeoIP2Exception("Received a 200 response but not decode it as JSON.");
        }
    }

    public Omni omni(String ipAddress) throws GeoIP2Exception {
        HttpResponse response = responseFor("omni", ipAddress);
        try {
            return response.parseAs(Omni.class);
        } catch (IOException e) {
            throw new GeoIP2Exception("Received a 200 response but not decode it as JSON.");
        }
    }

    public CityIspOrg cityIspOrg(String ipAddress) throws GeoIP2Exception {
        HttpResponse response = responseFor("city_isp_org", ipAddress);
        try {
            return response.parseAs(CityIspOrg.class);
        } catch (IOException e) {
            throw new GeoIP2Exception("Received a 200 response but not decode it as JSON.");
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    private HttpResponse responseFor(String path, String ip_address)
            throws GeoIP2Exception {
        HttpRequestFactory requestFactory =
                transport.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                  public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                  }
                });
        String uri = "https://" + host;
        if (host.startsWith("localhost")) {
            uri = "http://" + host;
        }
        uri = uri + "/geoip/v2.0/" + path + "/" + ip_address;
        // XXX - fix up urls
        GenericUrl queryUrl = new GenericUrl(uri);
        HttpRequest request;
        try {
            request = requestFactory.buildGetRequest(queryUrl);
        } catch (IOException e) {
            throw new GeoIP2Exception("Error building request", e);
        }
        request.getHeaders().setAccept("application/json");
        request.getHeaders().setBasicAuthentication(String.valueOf(userId), licenseKey);
        
        HttpResponse response = null;
        try {
            response = request.execute();
        } catch (HttpResponseException e) {
            handleErrorStatus(e.getContent(), e.getStatusCode(), uri);

            throw new GeoIP2Exception("Known error when connecting to web service: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new GeoIP2Exception("Unknown error when connecting to web service: " + e.getMessage(), e);
        }
        
        return handleSuccess(response, uri);
    }

    private HttpResponse handleSuccess(HttpResponse response, String uri)
            throws GeoIP2Exception {
        Long content_length = response.getHeaders().getContentLength();

        if (content_length == null || content_length <= 0 ) {
            throw new GeoIP2Exception("Received a 200 response for " + uri
                    + " but there was no message body.");
        }

        if (response.getContentType() == null || response.getContentType().indexOf("json") == -1) {
            try {
                throw new GeoIP2Exception("Received a 200 response for " + uri
                        + " but it does not appear to be JSON:\n" + response.parseAsString());
            } catch (IOException e) {
                throw new GeoIP2Exception("Received a 200 response for " + uri
                        + " but it does not appear to be JSON and could not parse body.");
            }
        }
            
        return response;
    }

    private void handleErrorStatus(String content, int status, String uri)
            throws GeoIP2Exception {
        if ((status >= 400) && (status < 500)) {
            handle4xxStatus(content, status, uri);
        } else if ((status >= 500) && (status < 600)) {
            handle5xxStatus(status, uri);
        } else {
            handleNon200Status(status, uri);
        }
    }

    private void handle4xxStatus(String body, int status, String uri)
            throws GeoIP2Exception {

        if (body == null) {
            throw new HttpException("Received a " + status + " error for "
                    + uri + " with no body", status, uri);
        }

        Map<String, String> content;
        try {
            ObjectMapper mapper = new ObjectMapper();
            content = mapper.readValue(body, HashMap.class);
        } catch (IOException e) {
            throw new HttpException("Received a $status error for " + uri + " but it did not include the expected JSON body: " +
                    body, status, uri);
        }
        
        String error = content.get("error");
        String code = content.get("code");
        
        if (error == null || code == null){
            throw new HttpException("Response contains JSON but it does not specify code or error keys: " + body, status, uri);
        }
        
        throw new WebServiceException(content.get("error"), content.get("code"),
                status, uri);
    }

    private void handle5xxStatus(int status, String uri)
            throws HttpException {
        throw new HttpException("Received a server error (" + status + ") for "
                + uri, status, uri);

        
    }

    private void handleNon200Status(int status, String uri) throws HttpException {
        throw new HttpException("Received a very surprising HTTP status ("
                + status + ") for " + uri, status, uri);
    }
}
