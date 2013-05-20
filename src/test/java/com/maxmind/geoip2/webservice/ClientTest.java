package com.maxmind.geoip2.webservice;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.exception.GeoIP2Exception;
import com.maxmind.geoip2.exception.HttpException;
import com.maxmind.geoip2.exception.WebServiceException;
import com.maxmind.geoip2.matchers.CodeMatcher;
import com.maxmind.geoip2.matchers.HttpStatusMatcher;
import com.maxmind.geoip2.model.CountryLookup;

/**
 * Unit test for simple App.
 */
public class ClientTest {

    private final HttpTransport transport = new TestTransport();

    private final Client client = new Client(42, "abcdef123456", this.transport);

    @Test
    public void testCountry() throws GeoIP2Exception, UnknownHostException {
        CountryLookup country = this.client.country(InetAddress
                .getByName("1.2.3.4"));
        assertEquals("country.getContinent().getCode() does not return NA",
                "NA", country.getContinent().getCode());
        assertEquals(
                "country.getContinent().getGeoNameId() does not return 42", 42,
                country.getContinent().getGeoNameId().intValue());
        assertEquals(
                "country.getContinent().getName(\"en\") does not return North America",
                "North America", country.getContinent().getName());
        assertEquals("country.getCountry().getCode() does not return US", "US",
                country.getCountry().getIsoCode());
        assertEquals("country.getCountry().getGeoNameId() does not return 1",
                1, country.getCountry().getGeoNameId().intValue());
        assertEquals(
                "country.getCountry().getName(\"en\") does not return United States",
                "United States", country.getCountry().getName());

    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void noBody() throws GeoIP2Exception, UnknownHostException {
        this.exception.expect(GeoIP2Exception.class);
        this.exception.expectMessage(containsString("message body"));

        this.client.country(InetAddress.getByName("1.2.3.5"));
    }

    @Test
    public void webServiceError() throws GeoIP2Exception, UnknownHostException {
        this.exception.expect(WebServiceException.class);
        this.exception.expect(CodeMatcher.hasCode("IP_ADDRESS_INVALID"));
        this.exception.expect(HttpStatusMatcher.hasStatus(400));
        this.exception
                .expectMessage(containsString("The value 1.2.3 is not a valid ip address"));

        this.client.country(InetAddress.getByName("1.2.3.6"));
    }

    @Test
    public void noErrorBody() throws GeoIP2Exception, UnknownHostException {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Received a 400 error for https://geoip.maxmind.com/geoip/v2.0/country/1.2.3.7 with no body"));

        this.client.country(InetAddress.getByName("1.2.3.7"));
    }

    @Test
    public void weirdErrorBody() throws GeoIP2Exception, UnknownHostException {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Response contains JSON but it does not specify code or error keys"));

        this.client.country(InetAddress.getByName("1.2.3.8"));
    }

    @Test
    public void unexpectedErrorBody() throws GeoIP2Exception,
            UnknownHostException {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("it did not include the expected JSON body:"));

        this.client.country(InetAddress.getByName("1.2.3.9"));
    }

    @Test
    public void internalServerError() throws GeoIP2Exception,
            UnknownHostException {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Received a server error (500) for"));
        this.client.country(InetAddress.getByName("1.2.3.10"));
    }

    @Test
    public void surprisingStatus() throws GeoIP2Exception, UnknownHostException {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Received a very surprising HTTP status (300) for"));

        this.client.country(InetAddress.getByName("1.2.3.11"));
    }

    @Test
    public void cannotAccept() throws GeoIP2Exception, UnknownHostException {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Cannot satisfy your Accept-Charset requirements"));
        this.client.country(InetAddress.getByName("1.2.3.12"));
    }

    @Test
    public void badContentType() throws GeoIP2Exception, UnknownHostException {
        this.exception.expect(GeoIP2Exception.class);
        this.exception
                .expectMessage(containsString(" but it does not appear to be JSON"));
        this.client.omni(InetAddress.getByName("1.2.3.14"));
    }

    @Test
    public void badJsonOn200() throws GeoIP2Exception, UnknownHostException {
        this.exception.expect(GeoIP2Exception.class);
        this.exception
                .expectMessage(containsString("Received a 200 response but not decode it as JSON: "));
        this.client.cityIspOrg(InetAddress.getByName("1.2.3.15"));
    }
}
