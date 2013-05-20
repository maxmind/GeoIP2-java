package com.maxmind.geoip2.webservice;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.maxmind.geoip2.exception.GeoIP2Exception;
import com.maxmind.geoip2.exception.HttpException;
import com.maxmind.geoip2.exception.WebServiceException;
import com.maxmind.geoip2.model.CityIspOrgLookup;
import com.maxmind.geoip2.model.CityLookup;
import com.maxmind.geoip2.model.CountryLookup;
import com.maxmind.geoip2.model.OmniLookup;

public class Client {
    private final HttpTransport transport;
    private final int userId;
    private final String licenseKey;
    private final List<String> languages;
    private String host = "geoip.maxmind.com";

    public Client(int userId, String licenseKey) {
        this(userId, licenseKey, null, null, null);
    }

    public Client(int userId, String licenseKey, List<String> languages) {
        this(userId, licenseKey, languages, null, null);
    }

    /* package-private as this is just for unit testing */
    Client(int userId, String licenseKey, HttpTransport transport) {
        this(userId, licenseKey, null, null, transport);
    }

    /* package-private as this is just for unit testing */
    Client(int userId, String licenseKey, List<String> languages,
            HttpTransport transport) {
        this(userId, licenseKey, languages, null, transport);
    }

    public Client(int userId, String licenseKey, String host) {
        this(userId, licenseKey, null, host, null);
    }

    /* package-private as we only need to specify a transport for unit testing */
    Client(int userId, String licenseKey, List<String> languages, String host,
            HttpTransport transport) {
        this.userId = userId;
        this.licenseKey = licenseKey;
        if (host != null) {
            this.host = host;
        }
        if (languages == null) {
            this.languages = Arrays.asList("en");
        } else {
            this.languages = languages;
        }
        if (transport == null) {
            this.transport = new NetHttpTransport();
        } else {
            this.transport = transport;
        }
    }

    public CountryLookup country() throws GeoIP2Exception {
        return this.country(null);
    }

    public CountryLookup country(InetAddress ipAddress) throws GeoIP2Exception {
        return this.responseFor("country", ipAddress, CountryLookup.class);
    }

    public CityLookup city() throws GeoIP2Exception {
        return this.city(null);
    }

    public CityLookup city(InetAddress ipAddress) throws GeoIP2Exception {
        return this.responseFor("city", ipAddress, CityLookup.class);
    }

    public CityIspOrgLookup cityIspOrg() throws GeoIP2Exception {
        return this.cityIspOrg(null);
    }

    public CityIspOrgLookup cityIspOrg(InetAddress ipAddress)
            throws GeoIP2Exception {
        return this.responseFor("city_isp_org", ipAddress,
                CityIspOrgLookup.class);
    }

    public OmniLookup omni() throws GeoIP2Exception {
        return this.omni(null);
    }

    public OmniLookup omni(InetAddress ipAddress) throws GeoIP2Exception {
        return this.responseFor("omni", ipAddress, OmniLookup.class);
    }

    private <T extends CountryLookup> T responseFor(String path,
            InetAddress ipAddress, Class<T> cls) throws GeoIP2Exception {
        GenericUrl uri = this.createUri(path, ipAddress);
        HttpResponse response = this.getResponse(uri);

        Long content_length = response.getHeaders().getContentLength();

        if (content_length == null || content_length.intValue() <= 0) {
            throw new GeoIP2Exception("Received a 200 response for " + uri
                    + " but there was no message body.");
        }

        String body = Client.getSuccessBody(response, uri);

        InjectableValues inject = new InjectableValues.Std().addValue(
                "languages", this.languages);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);

        try {
            return mapper.reader(cls).with(inject).readValue(body);
        } catch (IOException e) {
            throw new GeoIP2Exception(
                    "Received a 200 response but not decode it as JSON: "
                            + body);
        }
    }

    private HttpResponse getResponse(GenericUrl uri) throws GeoIP2Exception {
        HttpRequestFactory requestFactory = this.transport
                .createRequestFactory();
        HttpRequest request;
        try {
            request = requestFactory.buildGetRequest(uri);
        } catch (IOException e) {
            throw new GeoIP2Exception("Error building request", e);
        }
        request.getHeaders().setAccept("application/json");
        request.getHeaders().setBasicAuthentication(
                String.valueOf(this.userId), this.licenseKey);

        try {
            return request.execute();
        } catch (HttpResponseException e) {
            throw Client.handleErrorStatus(e.getContent(), e.getStatusCode(),
                    uri);
        } catch (IOException e) {
            throw new GeoIP2Exception(
                    "Unknown error when connecting to web service: "
                            + e.getMessage(), e);
        }
    }

    private static String getSuccessBody(HttpResponse response, GenericUrl uri)
            throws GeoIP2Exception {
        String body;
        try {
            body = response.parseAsString();
        } catch (IOException e) {
            throw new GeoIP2Exception(
                    "Received a 200 response but not decode message body: "
                            + e.getMessage());
        }

        if (response.getContentType() == null
                || !response.getContentType().contains("json")) {
            throw new GeoIP2Exception("Received a 200 response for " + uri
                    + " but it does not appear to be JSON:\n" + body);
        }
        return body;
    }

    private static HttpException handleErrorStatus(String content, int status,
            GenericUrl uri) {
        if ((status >= 400) && (status < 500)) {
            return Client.handle4xxStatus(content, status, uri);
        } else if ((status >= 500) && (status < 600)) {
            return new HttpException("Received a server error (" + status
                    + ") for " + uri, status, uri.toURL());
        } else {
            return new HttpException("Received a very surprising HTTP status ("
                    + status + ") for " + uri, status, uri.toURL());
        }
    }

    private static HttpException handle4xxStatus(String body, int status,
            GenericUrl uri) {

        if (body == null) {
            return new HttpException("Received a " + status + " error for "
                    + uri + " with no body", status, uri.toURL());
        }

        Map<String, String> content;
        try {
            ObjectMapper mapper = new ObjectMapper();
            content = mapper.readValue(body,
                    new TypeReference<HashMap<String, String>>() {
                    });
        } catch (IOException e) {
            return new HttpException(
                    "Received a $status error for "
                            + uri
                            + " but it did not include the expected JSON body: "
                            + body, status, uri.toURL());
        }

        String error = content.get("error");
        String code = content.get("code");

        if (error == null || code == null) {
            return new HttpException(
                    "Response contains JSON but it does not specify code or error keys: "
                            + body, status, uri.toURL());
        }

        return new WebServiceException(content.get("error"),
                content.get("code"), status, uri.toURL());
    }

    private GenericUrl createUri(String path, InetAddress ipAddress) {
        return new GenericUrl("https://" + this.host + "/geoip/v2.0/" + path
                + "/" + (ipAddress == null ? "me" : ipAddress.getHostAddress()));

    }
}
