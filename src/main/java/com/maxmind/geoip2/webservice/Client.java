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
import com.maxmind.geoip2.model.CityIspOrgResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.OmniResponse;

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

    public CountryResponse country() throws GeoIP2Exception {
        return this.country(null);
    }

    public CountryResponse country(InetAddress ipAddress)
            throws GeoIP2Exception {
        return this.responseFor("country", ipAddress, CountryResponse.class);
    }

    public CityResponse city() throws GeoIP2Exception {
        return this.city(null);
    }

    public CityResponse city(InetAddress ipAddress) throws GeoIP2Exception {
        return this.responseFor("city", ipAddress, CityResponse.class);
    }

    public CityIspOrgResponse cityIspOrg() throws GeoIP2Exception {
        return this.cityIspOrg(null);
    }

    public CityIspOrgResponse cityIspOrg(InetAddress ipAddress)
            throws GeoIP2Exception {
        return this.responseFor("city_isp_org", ipAddress,
                CityIspOrgResponse.class);
    }

    public OmniResponse omni() throws GeoIP2Exception {
        return this.omni(null);
    }

    public OmniResponse omni(InetAddress ipAddress) throws GeoIP2Exception {
        return this.responseFor("omni", ipAddress, OmniResponse.class);
    }

    private <T extends CountryResponse> T responseFor(String path,
            InetAddress ipAddress, Class<T> cls) throws GeoIP2Exception {
        HttpRequestFactory requestFactory = this.transport
                .createRequestFactory();
        String uri = "https://" + this.host + "/geoip/v2.0/" + path + "/"
                + (ipAddress == null ? "me" : ipAddress.getHostAddress());
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

        return this.handleSuccess(response, uri, cls);
    }

    private <T extends CountryResponse> T handleSuccess(HttpResponse response,
            String uri, Class<T> cls) throws GeoIP2Exception {
        Long content_length = response.getHeaders().getContentLength();

        if (content_length == null || content_length.intValue() <= 0) {
            throw new GeoIP2Exception("Received a 200 response for " + uri
                    + " but there was no message body.");
        }

        if (response.getContentType() == null
                || !response.getContentType().contains("json")) {
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

        String jsonBody;
        try {
            jsonBody = response.parseAsString();
        } catch (IOException e) {
            throw new GeoIP2Exception(
                    "Received a 200 response but not decode message body: "
                            + e.getMessage());
        }

        InjectableValues inject = new InjectableValues.Std().addValue(
                "languages", this.languages);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);

        try {
            return mapper.reader(cls).with(inject).readValue(jsonBody);
        } catch (IOException e) {
            throw new GeoIP2Exception(
                    "Received a 200 response but not decode it as JSON: "
                            + jsonBody);
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
            content = mapper.readValue(body,
                    new TypeReference<HashMap<String, String>>() {
                    });
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
