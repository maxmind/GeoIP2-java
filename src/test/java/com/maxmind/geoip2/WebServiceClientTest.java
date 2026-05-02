package com.maxmind.geoip2;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.jcabi.matchers.RegexMatchers.matchesPattern;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
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
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.MaxMind;
import com.maxmind.geoip2.record.RepresentedCountry;
import com.maxmind.geoip2.record.Subdivision;
import com.maxmind.geoip2.record.Traits;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.ServerSocket;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@WireMockTest
public class WebServiceClientTest {
    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicHttpsPort())
        .build();

    @Test
    public void test200WithNoBody() throws Exception {
        WebServiceClient client = createSuccessClient("insights", "me", "");

        Exception ex = assertThrows(GeoIp2Exception.class, client::insights);
        assertEquals("Received a 200 response but could not decode it as JSON", ex.getMessage());
    }

    @Test
    public void test200WithInvalidJson() throws Exception {
        WebServiceClient client = createSuccessClient("insights", "me", "{");

        Exception ex = assertThrows(GeoIp2Exception.class, client::insights);
        assertEquals("Received a 200 response but could not decode it as JSON", ex.getMessage());
    }

    @Test
    public void test200WithDefaultValues() throws Exception {
        WebServiceClient client = createSuccessClient("insights", "1.2.3.13",
            "{\"traits\":{\"ip_address\":\"1.2.3.13\",\"network\":\"1.2.3.0/24\"}}");

        InsightsResponse insights = client.insights(InetAddress
            .getByName("1.2.3.13"));

        assertThat(insights.toString(),
            CoreMatchers.startsWith("InsightsResponse["));

        City city = insights.city();
        assertNotNull(city);
        assertNull(city.confidence());

        Continent continent = insights.continent();
        assertNotNull(continent);
        assertNull(continent.code());

        Country country = insights.country();
        assertNotNull(country);

        Location location = insights.location();
        assertNotNull(location);
        assertNull(location.accuracyRadius());
        assertNull(location.latitude());
        assertNull(location.longitude());
        assertNull(location.timeZone());
        assertThat(location.toString(),
            CoreMatchers.startsWith("Location["));

        MaxMind maxmind = insights.maxmind();
        assertNotNull(maxmind);
        assertNull(maxmind.queriesRemaining());

        assertNotNull(insights.postal());

        Country registeredCountry = insights.registeredCountry();
        assertNotNull(registeredCountry);

        RepresentedCountry representedCountry = insights
            .representedCountry();
        assertNotNull(representedCountry);
        assertNull(representedCountry.type());

        List<Subdivision> subdivisions = insights.subdivisions();
        assertNotNull(subdivisions);
        assertTrue(subdivisions.isEmpty());

        Subdivision subdiv = insights.mostSpecificSubdivision();
        assertNotNull(subdiv);
        assertNull(subdiv.isoCode());
        assertNull(subdiv.confidence());

        Subdivision leastSpecificSubdiv = insights.leastSpecificSubdivision();
        assertNotNull(leastSpecificSubdiv);
        assertNull(leastSpecificSubdiv.isoCode());
        assertNull(leastSpecificSubdiv.confidence());

        Traits traits = insights.traits();
        assertNotNull(traits);
        assertNull(traits.autonomousSystemNumber());
        assertNull(traits.autonomousSystemOrganization());
        assertNull(traits.connectionType());
        assertNull(traits.domain());
        assertEquals("1.2.3.13", traits.ipAddress().getHostAddress());
        assertEquals("1.2.3.0/24", traits.network().toString());
        assertNull(traits.isp());
        assertNull(traits.organization());
        assertNull(traits.userType());
        assertNull(traits.staticIpScore());
        assertNull(traits.userCount());
        assertFalse(traits.isAnycast());

        for (Country c : new Country[] {country, registeredCountry}) {
            assertNull(c.confidence());
            assertNull(c.isoCode());
            assertFalse(c.isInEuropeanUnion());
        }

        // Check RepresentedCountry separately since it's no longer a Country
        assertNull(representedCountry.confidence());
        assertNull(representedCountry.isoCode());
        assertFalse(representedCountry.isInEuropeanUnion());

        for (NamedRecord r : new NamedRecord[] {city,
            continent, subdiv}) {
            assertNull(r.geonameId());
            assertNull(r.name());
            assertTrue(r.names().isEmpty());
            // Records have their own toString format
            assertNotNull(r.toString());
        }

        for (NamedRecord r : new NamedRecord[] {country,
            registeredCountry, representedCountry}) {
            assertNull(r.geonameId());
            assertNull(r.name());
            assertTrue(r.names().isEmpty());
            // Records have their own toString format
            assertNotNull(r.toString());
        }
    }

    @Test
    public void test200OnInsightsAsMe() throws Exception {
        WebServiceClient client = createSuccessClient("insights", "me",
            "{\"traits\":{\"ip_address\":\"24.24.24.24\"}}");
        assertEquals("24.24.24.24",
            client.insights().traits().ipAddress().getHostAddress());
    }

    @Test
    public void test200OnCityAsMe() throws Exception {
        WebServiceClient client = createSuccessClient("city", "me",
            "{\"traits\":{\"ip_address\":\"24.24.24.24\"}}");
        assertEquals("24.24.24.24",
            client.city().traits().ipAddress().getHostAddress());
    }

    @Test
    public void test200OnCountryAsMe() throws Exception {
        WebServiceClient client = createSuccessClient("country", "me",
            "{\"traits\":{\"ip_address\":\"24.24.24.24\"}}");
        assertEquals("24.24.24.24",
            client.country().traits().ipAddress().getHostAddress());
    }

    @Test
    public void testAddressNotFound() throws Exception {
        Exception ex = assertThrows(AddressNotFoundException.class,
            () -> createInsightsError(
                "1.2.3.16",
                404,
                "application/json",
                "{\"code\":\"IP_ADDRESS_NOT_FOUND\",\"error\":\"not found\"}"
            ));
        assertEquals("not found", ex.getMessage());
    }

    @Test
    public void testAddressReserved() throws Exception {
        Exception ex = assertThrows(AddressNotFoundException.class,
            () -> createInsightsError(
                "1.2.3.17",
                400,
                "application/json",
                "{\"code\":\"IP_ADDRESS_RESERVED\",\"error\":\"reserved\"}"
            ));
        assertEquals("reserved", ex.getMessage());
    }

    @Test
    public void testAddressInvalid() throws Exception {
        Exception ex = assertThrows(InvalidRequestException.class,
            () -> createInsightsError(
                "1.2.3.17",
                400,
                "application/json",
                "{\"code\":\"IP_ADDRESS_INVALID\",\"error\":\"invalid\"}"
            ));
        assertEquals("invalid", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"INSUFFICIENT_FUNDS", "OUT_OF_QUERIES"})
    public void testInsufficientCredit(String code) throws Exception {
        Exception ex = assertThrows(OutOfQueriesException.class,
            () -> createInsightsMeError(
                402,
                "application/json",
                "{\"code\":\"" + code + "\",\"error\":\"out of credit\"}"
            ));
        assertEquals("out of credit", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"AUTHORIZATION_INVALID",
        "LICENSE_KEY_REQUIRED",
        "USER_ID_REQUIRED",
        "USER_ID_UNKNOWN",
        "ACCOUNT_ID_REQUIRED",
        "ACCOUNT_ID_UNKNOWN"})
    public void testInvalidAuth(String code) throws Exception {
        Exception ex = assertThrows(AuthenticationException.class,
            () -> createInsightsMeError(
                401,
                "application/json",
                "{\"code\":\"" + code + "\",\"error\":\"Invalid auth\"}"
            ));
        assertEquals("Invalid auth", ex.getMessage());
    }

    @Test
    public void testPermissionRequired() throws Exception {
        Exception ex = assertThrows(PermissionRequiredException.class,
            () -> createInsightsMeError(
                403,
                "application/json",
                "{\"code\":\"PERMISSION_REQUIRED\",\"error\":\"Permission required\"}"
            ));
        assertEquals("Permission required", ex.getMessage());
    }

    @Test
    public void testInvalidRequest() throws Exception {
        Exception ex = assertThrows(InvalidRequestException.class,
            () -> createInsightsMeError(
                400,
                "application/json",
                "{\"code\":\"IP_ADDRESS_INVALID\",\"error\":\"IP invalid\"}"
            ));
        assertEquals("IP invalid", ex.getMessage());
    }

    @Test
    public void test400WithInvalidJson() throws Exception {
        Exception ex = assertThrows(HttpException.class,
            () -> createInsightsMeError(
                400,
                "application/json",
                "{blah}"
            ));
        assertThat(ex.getMessage(), matchesPattern(
            "Received a 400 error for .*/geoip/v2.1/insights/me but it did not include the expected JSON body: \\{blah\\}"));
    }

    @Test
    public void test400WithNoBody() throws Exception {
        Exception ex = assertThrows(HttpException.class,
            () -> createInsightsMeError(
                400,
                "application/json",
                ""
            ));
        assertThat(ex.getMessage(),
            matchesPattern("Received a 400 error for .*/geoip/v2.1/insights/me with no body"));
    }

    @Test
    public void test400WithUnexpectedContentType() throws Exception {
        Exception ex = assertThrows(HttpException.class,
            () -> createInsightsMeError(
                400,
                "text/plain",
                "text"
            ));
        assertThat(ex.getMessage(), matchesPattern(
            "Received a 400 error for .*/geoip/v2.1/insights/me but it did not include the expected JSON body: text"));
    }

    @Test
    public void test400WithUnexpectedJson() throws Exception {
        Exception ex = assertThrows(HttpException.class,
            () -> createInsightsMeError(
                400,
                "application/json",
                "{\"not\":\"expected\"}"
            ));
        assertEquals(
            "Error response contains JSON but it does not specify code or error keys: {\"not\":\"expected\"}",
            ex.getMessage());
    }

    @Test
    public void test300() throws Exception {
        Exception ex = assertThrows(HttpException.class,
            () -> createInsightsMeError(
                300,
                "application/json",
                ""
            ));
        assertThat(ex.getMessage(), startsWith("Received an unexpected HTTP status (300)"));
    }

    @Test
    public void test500() throws Exception {
        Exception ex = assertThrows(HttpException.class,
            () -> createInsightsMeError(
                500,
                "application/json",
                ""
            ));
        assertThat(ex.getMessage(), startsWith("Received a server error (500)"));
    }

    private void createInsightsError(String ip, int status, String contentType,
                                     String responseContent) throws Exception {
        WebServiceClient client = createClient(
            "insights",
            ip,
            status,
            contentType,
            responseContent
        );
        client.insights(InetAddress.getByName(ip));
    }

    private void createInsightsMeError(int status, String contentType, String responseContent)
        throws Exception {
        WebServiceClient client = createClient(
            "insights",
            "me",
            status,
            contentType,
            responseContent
        );
        client.insights();
    }

    private WebServiceClient createSuccessClient(String service, String ip, String responseContent)
        throws UnsupportedEncodingException {
        return createClient(
            service,
            ip,
            200,
            "application/vnd.maxmind.com-" + service + "+json; charset=UTF-8; version=2.1",
            responseContent
        );
    }

    private WebServiceClient createClient(String service, String ip, int status, String contentType,
                                          String responseContent)
        throws UnsupportedEncodingException {
        byte[] body = responseContent.getBytes(StandardCharsets.UTF_8);
        wireMock.stubFor(get(urlEqualTo("/geoip/v2.1/" + service + "/" + ip))
            .withHeader("Accept", equalTo("application/json"))
            .withHeader("Authorization", equalTo("Basic NjowMTIzNDU2Nzg5"))
            .willReturn(aResponse()
                .withStatus(status)
                // This is wrong if we use non-ASCII characters, but we don't currently
                .withHeader("Content-Length", Integer.toString(body.length))
                .withHeader("Content-Type", contentType)
                .withBody(body)));

        return new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .build();
    }

    @Test
    public void testHttpClientWithConnectTimeoutThrowsException() {
        HttpClient customClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

        WebServiceClient.Builder builder = new WebServiceClient.Builder(6, "0123456789")
            .httpClient(customClient)
            .connectTimeout(Duration.ofSeconds(5));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, builder::build);
        assertEquals("Cannot set both httpClient and connectTimeout. Configure timeout on the provided HttpClient instead.",
            ex.getMessage());
    }

    @Test
    public void testHttpClientWithProxyThrowsException() {
        HttpClient customClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

        ProxySelector proxySelector = ProxySelector.of(new InetSocketAddress("proxy.example.com", 8080));
        WebServiceClient.Builder builder = new WebServiceClient.Builder(6, "0123456789")
            .httpClient(customClient)
            .proxy(proxySelector);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, builder::build);
        assertEquals("Cannot set both httpClient and proxy. Configure proxy on the provided HttpClient instead.",
            ex.getMessage());
    }

    @Test
    public void testHttpClientWithDefaultSettingsDoesNotThrow() throws Exception {
        HttpClient customClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

        // Should not throw because we're not setting connectTimeout or proxy
        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(8080)
            .disableHttps()
            .httpClient(customClient)
            .build();

        assertNotNull(client);
    }

    @Test
    public void testRetriesOnConnectionReset_country() throws Exception {
        String url = "/geoip/v2.1/country/1.2.3.4";
        String body = "{\"traits\":{\"ip_address\":\"1.2.3.4\"}}";

        wireMock.stubFor(get(urlEqualTo(url))
            .inScenario("retry-country")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER))
            .willSetStateTo("succeeded"));

        wireMock.stubFor(get(urlEqualTo(url))
            .inScenario("retry-country")
            .whenScenarioStateIs("succeeded")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type",
                    "application/vnd.maxmind.com-country+json; charset=UTF-8; version=2.1")
                .withBody(body)));

        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .build();

        CountryResponse response = client.country(InetAddress.getByName("1.2.3.4"));
        assertNotNull(response);

        wireMock.verify(2, getRequestedFor(urlEqualTo(url)));
    }

    @Test
    public void testRetriesOnConnectionReset_city() throws Exception {
        String url = "/geoip/v2.1/city/1.2.3.4";
        String body = "{\"traits\":{\"ip_address\":\"1.2.3.4\"}}";

        wireMock.stubFor(get(urlEqualTo(url))
            .inScenario("retry-city")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER))
            .willSetStateTo("succeeded"));

        wireMock.stubFor(get(urlEqualTo(url))
            .inScenario("retry-city")
            .whenScenarioStateIs("succeeded")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type",
                    "application/vnd.maxmind.com-city+json; charset=UTF-8; version=2.1")
                .withBody(body)));

        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .build();

        CityResponse response = client.city(InetAddress.getByName("1.2.3.4"));
        assertNotNull(response);

        wireMock.verify(2, getRequestedFor(urlEqualTo(url)));
    }

    @Test
    public void testRetriesOnConnectionReset_insights() throws Exception {
        String url = "/geoip/v2.1/insights/1.2.3.4";
        String body = "{\"traits\":{\"ip_address\":\"1.2.3.4\"}}";

        wireMock.stubFor(get(urlEqualTo(url))
            .inScenario("retry-insights")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER))
            .willSetStateTo("succeeded"));

        wireMock.stubFor(get(urlEqualTo(url))
            .inScenario("retry-insights")
            .whenScenarioStateIs("succeeded")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type",
                    "application/vnd.maxmind.com-insights+json; charset=UTF-8; version=2.1")
                .withBody(body)));

        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .build();

        InsightsResponse response = client.insights(InetAddress.getByName("1.2.3.4"));
        assertNotNull(response);

        wireMock.verify(2, getRequestedFor(urlEqualTo(url)));
    }

    @Test
    public void testNoRetryOnHttpTimeoutException() {
        String url = "/geoip/v2.1/insights/1.2.3.4";
        wireMock.stubFor(get(urlEqualTo(url))
            .willReturn(aResponse()
                .withStatus(200)
                .withFixedDelay(2000)
                .withBody("{}")));

        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .requestTimeout(Duration.ofMillis(100))
            .build();

        // The request-phase timeout surfaces as a checked exception; we just
        // need to confirm it propagates (any throwable is acceptable here).
        assertThrows(Exception.class,
            () -> client.insights(InetAddress.getByName("1.2.3.4")));

        wireMock.verify(1, getRequestedFor(urlEqualTo(url)));
    }

    @Test
    public void testRetriesOnConnectTimeout() throws Exception {
        // Deterministic alternative to an unroutable address: bind to a free
        // local port, then immediately close it. Connection attempts to the
        // closed port fail fast with ConnectException, which exercises the
        // same retry branch as a connect timeout (both are predicate hits).
        int port;
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }

        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("127.0.0.1")
            .port(port)
            .disableHttps()
            .build();

        assertThrows(Exception.class,
            () -> client.insights(InetAddress.getByName("1.2.3.4")));
    }

    @Test
    public void testNoRetryOn5xx() {
        String url = "/geoip/v2.1/insights/1.2.3.4";
        wireMock.stubFor(get(urlEqualTo(url))
            .willReturn(aResponse()
                .withStatus(500)
                .withHeader("Content-Type", "application/json")
                .withBody("")));

        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .build();

        assertThrows(HttpException.class,
            () -> client.insights(InetAddress.getByName("1.2.3.4")));

        wireMock.verify(1, getRequestedFor(urlEqualTo(url)));
    }

    @Test
    public void testNoRetryOn4xx() {
        String url = "/geoip/v2.1/insights/1.2.3.4";
        wireMock.stubFor(get(urlEqualTo(url))
            .willReturn(aResponse()
                .withStatus(402)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"code\":\"OUT_OF_QUERIES\",\"error\":\"out of credit\"}")));

        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .build();

        assertThrows(OutOfQueriesException.class,
            () -> client.insights(InetAddress.getByName("1.2.3.4")));

        wireMock.verify(1, getRequestedFor(urlEqualTo(url)));
    }

    @Test
    public void testMaxRetriesZeroDisablesRetry() {
        String url = "/geoip/v2.1/insights/1.2.3.4";
        wireMock.stubFor(get(urlEqualTo(url))
            .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .maxRetries(0)
            .build();

        assertThrows(Exception.class,
            () -> client.insights(InetAddress.getByName("1.2.3.4")));

        wireMock.verify(1, getRequestedFor(urlEqualTo(url)));
    }

    @Test
    public void testInterruptDuringRetry() {
        // Pre-interrupt the calling thread; the predicate short-circuits when
        // the thread is interrupted, so no retry should occur and the
        // InterruptedException path in the client should restore the flag.
        String url = "/geoip/v2.1/insights/1.2.3.4";
        wireMock.stubFor(get(urlEqualTo(url))
            .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .build();

        Thread.currentThread().interrupt();
        try {
            assertThrows(Exception.class,
                () -> client.insights(InetAddress.getByName("1.2.3.4")));
            assertTrue(Thread.currentThread().isInterrupted(),
                "interrupt flag should remain set after the call");
        } finally {
            // Clear the interrupt flag so it does not leak to other tests.
            Thread.interrupted();
        }
    }

}
