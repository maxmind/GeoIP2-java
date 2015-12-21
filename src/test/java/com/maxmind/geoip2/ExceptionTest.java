package com.maxmind.geoip2;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.exception.*;
import com.maxmind.geoip2.matchers.CodeMatcher;
import com.maxmind.geoip2.matchers.HttpStatusMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.InetAddress;

import static org.hamcrest.CoreMatchers.containsString;

public class ExceptionTest {

    private final HttpTransport transport = new TestTransport();

    private final WebServiceClient client = new WebServiceClient.Builder(42,
            "abcdef123456").testTransport(this.transport).build();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void noBody() throws Exception {
        this.exception.expect(HttpException.class);
        this.exception.expectMessage(containsString("message body"));

        this.client.country(InetAddress.getByName("1.2.3.5"));
    }

    @Test
    public void webServiceError() throws Exception {
        this.exception.expect(InvalidRequestException.class);
        this.exception.expect(CodeMatcher.hasCode("IP_ADDRESS_INVALID"));
        this.exception
                .expectMessage(containsString("The value 1.2.3 is not a valid ip address"));

        this.client.country(InetAddress.getByName("1.2.3.6"));
    }

    @Test
    public void noErrorBody() throws Exception {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Received a 400 error for https://geoip.maxmind.com/geoip/v2.1/country/1.2.3.7 with no body"));
        this.exception.expect(HttpStatusMatcher.hasStatus(400));

        this.client.country(InetAddress.getByName("1.2.3.7"));
    }

    @Test
    public void weirdErrorBody() throws Exception {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Response contains JSON but it does not specify code or error keys"));

        this.client.country(InetAddress.getByName("1.2.3.8"));
    }

    @Test
    public void unexpectedErrorBody() throws Exception {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("it did not include the expected JSON body:"));

        this.client.country(InetAddress.getByName("1.2.3.9"));
    }

    @Test
    public void internalServerError() throws Exception {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Received a server error (500) for"));
        this.client.country(InetAddress.getByName("1.2.3.10"));
    }

    @Test
    public void surprisingStatus() throws Exception {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Received a very surprising HTTP status (300) for"));

        this.client.country(InetAddress.getByName("1.2.3.11"));
    }

    @Test
    public void cannotAccept() throws Exception {
        this.exception.expect(HttpException.class);
        this.exception
                .expectMessage(containsString("Cannot satisfy your Accept-Charset requirements"));
        this.client.country(InetAddress.getByName("1.2.3.12"));
    }

    @Test
    public void badContentType() throws Exception {
        this.exception.expect(GeoIp2Exception.class);
        this.exception
                .expectMessage(containsString(" but it does not appear to be JSON"));
        this.client.insights(InetAddress.getByName("1.2.3.14"));
    }

    @Test
    public void badJsonOn200() throws Exception {
        this.exception.expect(GeoIp2Exception.class);
        this.exception
                .expectMessage(containsString("Received a 200 response but not decode it as JSON: "));
        this.client.city(InetAddress.getByName("1.2.3.15"));
    }

    @Test
    public void addressNotFound() throws Exception {
        this.exception.expect(AddressNotFoundException.class);
        this.exception
                .expectMessage(containsString("The value 1.2.3.16 is not in the database."));

        this.client.country(InetAddress.getByName("1.2.3.16"));
    }

    @Test
    public void addressReserved() throws Exception {
        this.exception.expect(AddressNotFoundException.class);
        this.exception
                .expectMessage(containsString("The value 1.2.3.17 belongs to a reserved or private range."));

        this.client.country(InetAddress.getByName("1.2.3.17"));
    }

    @Test
    public void invalidAuth() throws Exception {
        this.exception.expect(AuthenticationException.class);
        this.exception
                .expectMessage(containsString("You have supplied an invalid MaxMind user ID and/or license key in the Authorization header."));

        this.client.country(InetAddress.getByName("1.2.3.18"));
    }

    @Test
    public void missingLicense() throws Exception {
        this.exception.expect(AuthenticationException.class);
        this.exception
                .expectMessage(containsString("You have not supplied a MaxMind license key in the Authorization header."));

        this.client.country(InetAddress.getByName("1.2.3.19"));
    }

    @Test
    public void missingUserID() throws Exception {
        this.exception.expect(AuthenticationException.class);
        this.exception
                .expectMessage(containsString("You have not supplied a MaxMind user ID in the Authorization header."));

        this.client.country(InetAddress.getByName("1.2.3.20"));
    }

    @Test
    public void outOfQueries() throws Exception {
        this.exception.expect(OutOfQueriesException.class);
        this.exception
                .expectMessage(containsString("The license key you have provided is out of queries."));

        this.client.country(InetAddress.getByName("1.2.3.21"));
    }

}
