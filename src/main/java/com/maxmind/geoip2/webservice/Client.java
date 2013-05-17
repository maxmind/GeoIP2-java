package com.maxmind.geoip2.webservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.maxmind.geoip2.exception.GeoIP2Exception;
import com.maxmind.geoip2.exception.HttpException;
import com.maxmind.geoip2.exception.WebServiceException;
import com.maxmind.geoip2.model.CityIspOrgResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.OmniResponse;

public class Client {
    private HttpTransport transport;
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private int userId;
    private String licenseKey;
    private String host = "geoip.maxmind.com";

    public Client(int userId, String licenseKey) {
        this(userId, licenseKey, null, null);
    }

    protected Client(int userId, String licenseKey, HttpTransport transport) {
        this(userId, licenseKey, null, transport);
    }

    public Client(int userId, String licenseKey, String host) {
        this(userId, licenseKey, host, null);
    }

    protected Client(int userId, String licenseKey, String host,
            HttpTransport transport) {
        this.userId = userId;
        this.licenseKey = licenseKey;
        if (host != null) {
            this.host = host;
        }
        if (transport == null) {
            this.transport = new NetHttpTransport();
        } else {
            this.transport = transport;
        }
    }

    public CountryResponse country(String ipAddress) throws GeoIP2Exception {
        return this.responseFor("country", ipAddress, CountryResponse.class);
    }

    public CityResponse city(String ipAddress) throws GeoIP2Exception {
        return this.responseFor("city", ipAddress, CityResponse.class);
    }

    public OmniResponse omni(String ipAddress) throws GeoIP2Exception {
        return this.responseFor("omni", ipAddress, OmniResponse.class);
    }

    public CityIspOrgResponse cityIspOrg(String ipAddress)
            throws GeoIP2Exception {
        return this.responseFor("city_isp_org", ipAddress,
                CityIspOrgResponse.class);
    }

    private <T extends CountryResponse> T responseFor(String path,
            String ip_address, Class<T> cls) throws GeoIP2Exception {
        HttpRequestFactory requestFactory = this.transport
                .createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });
        String uri = "https://" + this.host;
        if (this.host.startsWith("localhost")) {
            uri = "http://" + this.host;
        }
        uri = uri + "/geoip/v2.0/" + path + "/" + ip_address;
        // XXX - fix up urls
        HttpRequest request;
        try {
            request = requestFactory.buildGetRequest(new GenericUrl(uri));
        } catch (IOException e) {
            throw new GeoIP2Exception("Error building request", e);
        }
        request.getHeaders().setAccept("application/json");
        request.getHeaders().setBasicAuthentication(
                String.valueOf(this.userId), this.licenseKey);

        HttpResponse response;
        try {
            response = request.execute();
        } catch (HttpResponseException e) {
            throw Client.handleErrorStatus(e.getContent(), e.getStatusCode(),
                    uri);
        } catch (IOException e) {
            throw new GeoIP2Exception(
                    "Unknown error when connecting to web service: "
                            + e.getMessage(), e);
        }

        return Client.handleSuccess(response, uri, cls);
    }

    private static <T extends CountryResponse> T handleSuccess(
            HttpResponse response, String uri, Class<T> cls)
            throws GeoIP2Exception {
        Long content_length = response.getHeaders().getContentLength();

        if (content_length == null || content_length.longValue() <= 0) {
            throw new GeoIP2Exception("Received a 200 response for " + uri
                    + " but there was no message body.");
        }

        if (response.getContentType() == null
                || response.getContentType().indexOf("json") == -1) {
            try {
                throw new GeoIP2Exception("Received a 200 response for " + uri
                        + " but it does not appear to be JSON:\n"
                        + response.parseAsString());
            } catch (IOException e) {
                throw new GeoIP2Exception(
                        "Received a 200 response for "
                                + uri
                                + " but it does not appear to be JSON and could not parse body.");
            }
        }

        try {
            return response.parseAs(cls);
        } catch (IOException e) {
            throw new GeoIP2Exception(
                    "Received a 200 response but not decode it as JSON: "
                            + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new GeoIP2Exception(
                    "Received a 200 response but not decode it as JSON: "
                            + e.getMessage());
        }
    }

    private static HttpException handleErrorStatus(String content, int status,
            String uri) {
        if ((status >= 400) && (status < 500)) {
            return Client.handle4xxStatus(content, status, uri);
        } else if ((status >= 500) && (status < 600)) {
            return Client.handle5xxStatus(status, uri);
        } else {
            return Client.handleNon200Status(status, uri);
        }
    }

    private static HttpException handle4xxStatus(String body, int status,
            String uri) {

        if (body == null) {
            return new HttpException("Received a " + status + " error for "
                    + uri + " with no body", status, uri);
        }

        Map<String, String> content;
        try {
            ObjectMapper mapper = new ObjectMapper();
            content = mapper.readValue(body, HashMap.class);
        } catch (IOException e) {
            return new HttpException(
                    "Received a $status error for "
                            + uri
                            + " but it did not include the expected JSON body: "
                            + body, status, uri);
        }

        String error = content.get("error");
        String code = content.get("code");

        if (error == null || code == null) {
            return new HttpException(
                    "Response contains JSON but it does not specify code or error keys: "
                            + body, status, uri);
        }

        return new WebServiceException(content.get("error"),
                content.get("code"), status, uri);
    }

    private static HttpException handle5xxStatus(int status, String uri) {
        return new HttpException("Received a server error (" + status
                + ") for " + uri, status, uri);
    }

    private static HttpException handleNon200Status(int status, String uri) {
        return new HttpException("Received a very surprising HTTP status ("
                + status + ") for " + uri, status, uri);
    }
}
