package com.maxmind.geoip2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.maxmind.geoip2.exception.*;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.InsightsResponse;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class provides a client API for all the GeoIP2 Precision web service end
 * points. The end points are Country and Insights. Each end point returns a
 * different set of data about an IP address, with Country returning the least
 * data and Insights the most.
 * </p>
 * <p>
 * Each web service end point is represented by a different model class, and
 * these model classes in turn contain multiple Record classes. The record
 * classes have attributes which contain data about the IP address.
 * </p>
 * <p>
 * If the web service does not return a particular piece of data for an IP
 * address, the associated attribute is not populated.
 * </p>
 * <p>
 * The web service may not return any information for an entire record, in which
 * case all of the attributes for that record class will be empty.
 * </p>
 * <h3>Usage</h3>
 * <p>
 * The basic API for this class is the same for all of the web service end
 * points. First you create a web service object with your MaxMind
 * {@code userId} and {@code licenseKey}, then you call the method corresponding
 * to a specific end point, passing it the IP address you want to look up.
 * </p>
 * <p>
 * If the request succeeds, the method call will return a model class for the
 * end point you called. This model in turn contains multiple record classes,
 * each of which represents part of the data returned by the web service.
 * </p>
 * <p>
 * If the request fails, the client class throws an exception.
 * </p>
 * <h3>Exceptions</h3>
 * <p>
 * For details on the possible errors returned by the web service itself, see <a
 * href="http://dev.maxmind.com/geoip2/geoip/web-services">the GeoIP2 web
 * service documentation</a>.
 * </p>
 * <p>
 * If the web service returns an explicit error document, this is thrown as a
 * {@link InvalidRequestException}. If some other sort of transport error
 * occurs, this is thrown as a {@link HttpException}. The difference is that the
 * web service error includes an error message and error code delivered by the
 * web service. The latter is thrown when some sort of unanticipated error
 * occurs, such as the web service returning a 500 or an invalid error document.
 * </p>
 * <p>
 * If the web service returns any status code besides 200, 4xx, or 5xx, this
 * also becomes a {@link HttpException}.
 * </p>
 * <p>
 * Finally, if the web service returns a 200 but the body is invalid, the client
 * throws a {@link GeoIp2Exception}.
 * </p>
 */
public class WebServiceClient implements GeoIp2Provider {
    private final String host;
    private final List<String> locales;
    private final String licenseKey;
    private final int connectTimeout;
    private final int readTimeout;
    private final HttpTransport testTransport;
    private final int userId;
    private final ObjectMapper mapper;

    private WebServiceClient(Builder builder) {
        this.host = builder.host;
        this.locales = builder.locales;
        this.licenseKey = builder.licenseKey;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.testTransport = builder.testTransport;
        this.userId = builder.userId;

        this.mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        mapper.configure(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, false);

    }

    /**
     * <p>
     * {@code Builder} creates instances of {@code WebServiceClient}
     * from values set by the methods.
     * </p>
     * <p>
     * This example shows how to create a {@code WebServiceClient} object
     * with the {@code Builder}:
     * </p>
     * <p>
     * WebServiceClient client = new
     * WebServiceClient.Builder(12,"licensekey").host
     * ("geoip.maxmind.com").build();
     * </p>
     * <p>
     * Only the values set in the {@code Builder} constructor are required.
     * </p>
     */
    public static final class Builder {
        final int userId;
        final String licenseKey;

        String host = "geoip.maxmind.com";
        List<String> locales = Collections.singletonList("en");
        int connectTimeout = 3000;
        int readTimeout = 20000;
        HttpTransport testTransport;

        /**
         * @param userId     Your MaxMind user ID.
         * @param licenseKey Your MaxMind license key.
         */
        public Builder(int userId, String licenseKey) {
            this.userId = userId;
            this.licenseKey = licenseKey;
        }

        /**
         * @param val Timeout in milliseconds to establish a connection to the
         *            web service. The default is 3000 (3 seconds).
         * @return Builder object
         */
        public Builder connectTimeout(int val) {
            this.connectTimeout = val;
            return this;
        }

        /**
         * @param val The host to use.
         * @return Builder object
         */
        public Builder host(String val) {
            this.host = val;
            return this;
        }

        /**
         * @param val List of locale codes to use in name property from most
         *            preferred to least preferred.
         * @return Builder object
         */
        public Builder locales(List<String> val) {
            this.locales = val;
            return this;
        }

        /**
         * @param val readTimeout in milliseconds to read data from an
         *            established connection to the web service. The default is
         *            20000 (20 seconds).
         * @return Builder object
         */
        public Builder readTimeout(int val) {
            this.readTimeout = val;
            return this;
        }

        /**
         * @param val Transport for unit testing.
         * @return Builder object
         */
        Builder testTransport(HttpTransport val) {
            this.testTransport = val;
            return this;
        }

        /**
         * @return an instance of {@code WebServiceClient} created from the
         * fields set on this builder.
         */
        public WebServiceClient build() {
            return new WebServiceClient(this);
        }
    }

    /**
     * @return A Country model for the requesting IP address
     * @throws GeoIp2Exception if there is an error from the web service
     * @throws IOException     if an IO error happens during the request
     */
    public CountryResponse country() throws IOException, GeoIp2Exception {
        return this.country(null);
    }

    @Override
    public CountryResponse country(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.responseFor("country", ipAddress, CountryResponse.class);
    }

    /**
     * @return A City model for the requesting IP address
     * @throws GeoIp2Exception if there is an error from the web service
     * @throws IOException     if an IO error happens during the request
     */
    public CityResponse city() throws IOException, GeoIp2Exception {
        return this.city(null);
    }

    @Override
    public CityResponse city(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.responseFor("city", ipAddress, CityResponse.class);
    }

    /**
     * @return An Insights model for the requesting IP address
     * @throws GeoIp2Exception if there is an error from the web service
     * @throws IOException     if an IO error happens during the request
     */
    public InsightsResponse insights() throws IOException, GeoIp2Exception {
        return this.insights(null);
    }

    /**
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return A Insight model for the requested IP address.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    public InsightsResponse insights(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.responseFor("insights", ipAddress, InsightsResponse.class);
    }

    private <T> T responseFor(String path, InetAddress ipAddress, Class<T> cls)
            throws GeoIp2Exception, IOException {
        GenericUrl uri = this.createUri(path, ipAddress);
        HttpResponse response = this.getResponse(uri);
        Long contentLength = response.getHeaders().getContentLength();

        if (contentLength == null || contentLength.intValue() <= 0) {
            throw new HttpException("Received a 200 response for " + uri
                    + " but there was no message body.", 200, uri.toURL());
        }

        String body = WebServiceClient.getSuccessBody(response, uri);

        String ip = ipAddress == null ? null : ipAddress.getHostAddress();
        InjectableValues inject = new JsonInjector(locales, ip);

        try {
            return mapper.readerFor(cls).with(inject).readValue(body);
        } catch (IOException e) {
            throw new GeoIp2Exception(
                    "Received a 200 response but not decode it as JSON: "
                            + body, e);
        }
    }

    private HttpResponse getResponse(GenericUrl uri) throws GeoIp2Exception,
            IOException {
        // We only use a instance variable for HttpTransport when unit testing
        // as
        // HttpTransport is not thread safe.
        HttpTransport transport = this.testTransport == null ? new NetHttpTransport()
                : this.testTransport;
        HttpRequestFactory requestFactory = transport.createRequestFactory();
        HttpRequest request;
        try {
            request = requestFactory.buildGetRequest(uri);
        } catch (IOException e) {
            throw new GeoIp2Exception("Error building request", e);
        }
        request.setConnectTimeout(this.connectTimeout);
        request.setReadTimeout(this.readTimeout);

        HttpHeaders headers = request.getHeaders();
        headers.setAccept("application/json");
        headers.setBasicAuthentication(String.valueOf(this.userId),
                this.licenseKey);
        headers.setUserAgent("GeoIP2 Java Client v"
                + this.getClass().getPackage().getImplementationVersion() + ";");

        try {
            return request.execute();
        } catch (HttpResponseException e) {
            int status = e.getStatusCode();
            if ((status >= 400) && (status < 500)) {
                WebServiceClient.handle4xxStatus(e.getContent(), status, uri);
            } else if ((status >= 500) && (status < 600)) {
                throw new HttpException("Received a server error (" + status
                        + ") for " + uri, status, uri.toURL());
            }

            throw new HttpException("Received a very surprising HTTP status ("
                    + status + ") for " + uri, status, uri.toURL());
        }
    }

    private static String getSuccessBody(HttpResponse response, GenericUrl uri)
            throws GeoIp2Exception {
        String body;
        try {
            body = response.parseAsString();
        } catch (IOException e) {
            throw new GeoIp2Exception(
                    "Received a 200 response but not decode message body: "
                            + e.getMessage());
        }

        if (response.getContentType() == null
                || !response.getContentType().contains("json")) {
            throw new GeoIp2Exception("Received a 200 response for " + uri
                    + " but it does not appear to be JSON:\n" + body);
        }
        return body;
    }

    private static void handle4xxStatus(String body, int status, GenericUrl uri)
            throws GeoIp2Exception, HttpException {

        if (body == null) {
            throw new HttpException("Received a " + status + " error for "
                    + uri + " with no body", status, uri.toURL());
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> content = mapper.readValue(body,
                    new TypeReference<HashMap<String, String>>() {
                    });
            handleErrorWithJsonBody(content, body, status, uri);
        } catch (HttpException e) {
            throw e;
        } catch (IOException e) {
            throw new HttpException("Received a " + status + " error for "
                    + uri + " but it did not include the expected JSON body: "
                    + body, status, uri.toURL());
        }
    }

    private static void handleErrorWithJsonBody(Map<String, String> content,
                                                String body, int status, GenericUrl uri) throws GeoIp2Exception,
            HttpException {
        String error = content.get("error");
        String code = content.get("code");

        if (error == null || code == null) {
            throw new HttpException(
                    "Response contains JSON but it does not specify code or error keys: "
                            + body, status, uri.toURL());
        }

        if (code.equals("IP_ADDRESS_NOT_FOUND")
                || code.equals("IP_ADDRESS_RESERVED")) {
            throw new AddressNotFoundException(error);
        } else if (code.equals("AUTHORIZATION_INVALID")
                || code.equals("LICENSE_KEY_REQUIRED")
                || code.equals("USER_ID_REQUIRED")) {
            throw new AuthenticationException(error);
        } else if (code.equals("OUT_OF_QUERIES")) {
            throw new OutOfQueriesException(error);
        }

        // These should be fairly rare
        throw new InvalidRequestException(error, code, uri.toURL());
    }

    private GenericUrl createUri(String path, InetAddress ipAddress) {
        return new GenericUrl("https://" + this.host + "/geoip/v2.1/" + path
                + "/" + (ipAddress == null ? "me" : ipAddress.getHostAddress()));

    }
}
