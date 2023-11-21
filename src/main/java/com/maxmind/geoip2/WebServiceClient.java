package com.maxmind.geoip2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.AuthenticationException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.exception.HttpException;
import com.maxmind.geoip2.exception.InvalidRequestException;
import com.maxmind.geoip2.exception.OutOfQueriesException;
import com.maxmind.geoip2.exception.PermissionRequiredException;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.InsightsResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * The {@code WebServiceClient} class provides a client API for all the GeoIP2
 * web services. The services are Country, City Plus, and Insights. Each
 * service returns a different set of data about an IP address, with Country
 * returning the least data and Insights the most.
 * </p>
 * <p>
 * Each service is represented by a different model class, and these model
 * classes in turn contain multiple Record classes. The record classes have
 * attributes which contain data about the IP address.
 * </p>
 * <p>
 * If the service does not return a particular piece of data for an IP
 * address, the associated attribute is not populated.
 * </p>
 * <p>
 * The service may not return any information for an entire record, in which
 * case all of the attributes for that record class will be empty.
 * </p>
 * <h2>Usage</h2>
 * <p>
 * To use the web service API, you must create a new {@code WebServiceClient}
 * using the {@code WebServiceClient.Builder}. You must provide the
 * {@code Builder} constructor your MaxMind {@code accountId} and
 * {@code licenseKey}. To use the GeoLite2 web services instead of GeoIP2, set
 * the {@code host} method on the builder to {@code geolite.info}. To use the
 * Sandbox GeoIP2 web services instead of the production GeoIP2 web services,
 * set the {@code host} method on the builder to {@code sandbox.maxmind.com}.
 * You may also set a {@code timeout} or set the {@code locales} fallback order
 * using the methods on the {@code Builder}. After you have created the {@code
 * WebServiceClient}, you may then call the method corresponding to a specific
 * service, passing it the IP address you want to look up.
 * </p>
 * <p>
 * If the request succeeds, the method call will return a model class for the
 * service you called. This model in turn contains multiple record classes,
 * each of which represents part of the data returned by the web service.
 * </p>
 * <p>
 * If the request fails, the client class throws an exception.
 * </p>
 * <p>
 * The {@code WebServiceClient} object is safe to share across threads. If you
 * are making multiple requests, the object should be reused so that new
 * connections are not created for each request.
 * </p>
 * <h2>Exceptions</h2>
 * <p>
 * For details on the possible errors returned by the web service itself, see <a
 * href="https://dev.maxmind.com/geoip/docs/web-services?lang=en">the GeoIP2 web
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
public class WebServiceClient implements WebServiceProvider, Closeable {
    private final String host;
    private final List<String> locales;
    private final String authHeader;
    private final boolean useHttps;
    private final int port;
    private final Duration requestTimeout;
    private final String userAgent = "GeoIP2/"
        + getClass().getPackage().getImplementationVersion()
        + " (Java/" + System.getProperty("java.version") + ")";

    private final ObjectMapper mapper;
    private final HttpClient httpClient;


    private WebServiceClient(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.useHttps = builder.useHttps;
        this.locales = builder.locales;

        // HttpClient supports basic auth, but it will only send it after the
        // server responds with an unauthorized. As such, we just make the
        // Authorization header ourselves.
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString(
            (builder.accountId + ":" + builder.licenseKey).getBytes(StandardCharsets.UTF_8));

        mapper = JsonMapper.builder()
            .disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

        requestTimeout = builder.requestTimeout;
        httpClient = HttpClient.newBuilder()
            .connectTimeout(builder.connectTimeout)
            .proxy(builder.proxy)
            .build();
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
     * {@code WebServiceClient client = new WebServiceClient.Builder(12,"licensekey")
     *      .host("geoip.maxmind.com").build();}
     * </p>
     * <p>
     * Only the values set in the {@code Builder} constructor are required.
     * </p>
     */
    public static final class Builder {
        final int accountId;
        final String licenseKey;

        String host = "geoip.maxmind.com";
        int port = 443;
        boolean useHttps = true;

        Duration connectTimeout = Duration.ofSeconds(3);
        Duration requestTimeout = Duration.ofSeconds(20);

        List<String> locales = Collections.singletonList("en");
        private ProxySelector proxy = ProxySelector.getDefault();

        /**
         * @param accountId  Your MaxMind account ID.
         * @param licenseKey Your MaxMind license key.
         */
        public Builder(int accountId, String licenseKey) {
            this.accountId = accountId;
            this.licenseKey = licenseKey;
        }

        /**
         * @param val Timeout in milliseconds to establish a connection to the
         *            web service. The default is 3000 (3 seconds).
         * @return Builder object
         * @deprecated Use connectTimeout(Duration) instead
         */
        @Deprecated
        public Builder connectTimeout(int val) {
            this.connectTimeout = Duration.ofMillis(val);
            return this;
        }

        /**
         * @param val Timeout duration to establish a connection to the
         *            web service. The default is 3 seconds.
         * @return Builder object
         */
        public Builder connectTimeout(Duration val) {
            this.connectTimeout = val;
            return this;
        }

        /**
         * Disables HTTPS to connect to a test server or proxy. The minFraud ScoreResponse and
         * InsightsResponse web services require HTTPS.
         *
         * @return Builder object
         */
        public WebServiceClient.Builder disableHttps() {
            useHttps = false;
            return this;
        }

        /**
         * @param val The host to use. Set this to {@code geolite.info} to use the
         *            GeoLite2 web services instead of the GeoIP2 web services.
         *            Set this to {@code sandbox.maxmind.com} to use the Sandbox
         *            GeoIP2 web services instead of the production GeoIP2 web
         *            services. The sandbox allows you to experiment with the
         *            API without affecting your production data.
         * @return Builder object
         */
        public Builder host(String val) {
            this.host = val;
            return this;
        }

        /**
         * @param val The port to use.
         * @return Builder object
         */
        public WebServiceClient.Builder port(int val) {
            port = val;
            return this;
        }

        /**
         * @param val List of locale codes to use in name property from most
         *            preferred to least preferred.
         * @return Builder object
         */
        public Builder locales(List<String> val) {
            this.locales = new ArrayList<>(val);
            return this;
        }

        /**
         * @param val readTimeout in milliseconds to read data from an
         *            established connection to the web service. The default is
         *            20000 (20 seconds).
         * @return Builder object
         * @deprecated Use requestTimeout(Duration) instead
         */
        @Deprecated
        public Builder readTimeout(int val) {
            this.requestTimeout = Duration.ofMillis(val);
            return this;
        }

        /**
         * @param val Request timeout duration. The default is 20 seconds.
         * @return Builder object
         */
        public Builder requestTimeout(Duration val) {
            this.requestTimeout = val;
            return this;
        }

        /**
         * @param val the proxy to use when making this request.
         * @return Builder object
         * @deprecated Use proxy(ProxySelector)
         */
        @Deprecated
        public Builder proxy(Proxy val) {
            if (val != null && val != Proxy.NO_PROXY) {
                this.proxy = ProxySelector.of((InetSocketAddress) val.address());
            }
            return this;
        }

        /**
         * @param val the proxy to use when making this request.
         * @return Builder object
         */
        public Builder proxy(ProxySelector val) {
            this.proxy = val;
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
     * @return A City Plus model for the requesting IP address
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
     * @return An Insight model for the requested IP address.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    public InsightsResponse insights(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        return this.responseFor("insights", ipAddress, InsightsResponse.class);
    }

    private <T> T responseFor(String path, InetAddress ipAddress, Class<T> cls)
        throws IOException, GeoIp2Exception {
        URI uri = createUri(path, ipAddress);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .timeout(this.requestTimeout)
            .header("Accept", "application/json")
            .header("Authorization", authHeader)
            .header("User-Agent", this.userAgent)
            .GET()
            .build();
        HttpResponse<InputStream> response = null;
        try {
            response = this.httpClient
                .send(request, HttpResponse.BodyHandlers.ofInputStream());
            return handleResponse(response, cls);
        } catch (InterruptedException e) {
            throw new GeoIp2Exception("Interrupted sending request", e);
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
    }

    private <T> T handleResponse(HttpResponse<InputStream> response, Class<T> cls)
        throws GeoIp2Exception, IOException {
        int status = response.statusCode();
        URI uri = response.uri();

        if (status >= 400 && status < 500) {
            this.handle4xxStatus(response);
        } else if (status >= 500 && status < 600) {
            exhaustBody(response);
            throw new HttpException("Received a server error (" + status
                + ") for " + uri, status, uri);
        } else if (status != 200) {
            exhaustBody(response);
            throw new HttpException("Received an unexpected HTTP status ("
                + status + ") for " + uri, status, uri);
        }

        InjectableValues inject = new JsonInjector(locales, null, null);

        try {
            return mapper.readerFor(cls).with(inject).readValue(response.body());
        } catch (IOException e) {
            throw new GeoIp2Exception(
                "Received a 200 response but could not decode it as JSON", e);
        }
    }

    private void handle4xxStatus(HttpResponse<InputStream> response)
        throws GeoIp2Exception, IOException {
        int status = response.statusCode();
        URI uri = response.uri();

        String body = new String(response.body().readAllBytes(), StandardCharsets.UTF_8);
        if (body.equals("")) {
            throw new HttpException("Received a " + status + " error for "
                + uri + " with no body", status, uri);
        }

        try {
            Map<String, String> content = mapper.readValue(body,
                new TypeReference<HashMap<String, String>>() {
                });
            handleErrorWithJsonBody(content, body, status, uri);
        } catch (HttpException e) {
            throw e;
        } catch (IOException e) {
            throw new HttpException("Received a " + status + " error for "
                + uri + " but it did not include the expected JSON body: "
                + body, status, uri);
        }
    }

    private static void handleErrorWithJsonBody(Map<String, String> content,
                                                String body, int status, URI uri)
        throws GeoIp2Exception, HttpException {
        String error = content.get("error");
        String code = content.get("code");

        if (error == null || code == null) {
            throw new HttpException(
                "Error response contains JSON but it does not specify code or error keys: "
                    + body, status, uri);
        }

        switch (code) {
            case "IP_ADDRESS_NOT_FOUND":
            case "IP_ADDRESS_RESERVED":
                throw new AddressNotFoundException(error);
            case "ACCOUNT_ID_REQUIRED":
            case "ACCOUNT_ID_UNKNOWN":
            case "AUTHORIZATION_INVALID":
            case "LICENSE_KEY_REQUIRED":
            case "USER_ID_REQUIRED":
            case "USER_ID_UNKNOWN":
                throw new AuthenticationException(error);
            case "INSUFFICIENT_FUNDS":
            case "OUT_OF_QUERIES":
                throw new OutOfQueriesException(error);
            case "PERMISSION_REQUIRED":
                throw new PermissionRequiredException(error);
            default:
                // These should be fairly rare
                throw new InvalidRequestException(error, code, uri);
        }
    }

    private URI createUri(String service, InetAddress ipAddress) throws GeoIp2Exception {
        String path = "/geoip/v2.1/" + service + "/"
            + (ipAddress == null ? "me" : ipAddress.getHostAddress());
        try {
            return new URI(
                useHttps ? "https" : "http",
                null,
                host,
                port,
                path,
                null,
                null
            );
        } catch (URISyntaxException e) {
            throw new GeoIp2Exception("Syntax error creating service URL", e);
        }
    }

    private void exhaustBody(HttpResponse<InputStream> response) throws HttpException {
        InputStream body = response.body();

        try {
            // Make sure we read the stream until the end so that
            // the connection can be reused.
            while (body.read() != -1) {
            }
        } catch (IOException e) {
            throw new HttpException("Error reading response body", response.statusCode(),
                response.uri(), e);
        }
    }

    /**
     * @deprecated Closing is no longer necessary with java.net.http.HttpClient.
     */
    @Deprecated
    @Override
    public void close() throws IOException {
    }

    @Override
    public String toString() {
        return "WebServiceClient{"
            + "host='" + host + '\''
            + ", locales=" + locales
            + ", useHttps=" + useHttps
            + ", port=" + port
            + ", requestTimeout=" + requestTimeout
            + ", userAgent='" + userAgent + '\''
            + ", mapper=" + mapper
            + ", httpClient=" + httpClient
            + '}';
    }
}
