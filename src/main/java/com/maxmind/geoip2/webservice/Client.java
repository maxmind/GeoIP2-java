package com.maxmind.geoip2.webservice;

import com.maxmind.geoip2.exception.*;
import com.maxmind.geoip2.model.*;
import java.io.*;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.auth.*;
import org.apache.http.auth.*;
import org.json.*;

public class Client {
    private String userId;
    private String licenseKey;
    private String host;

    public Client(String user_id, String licenseKey) {
        this.userId = user_id;
        this.licenseKey = licenseKey;
        this.host = "geoip.maxmind.com";
    }

    public Country Country(String ipAddress) throws GeoIP2Exception {
        JSONObject json = responseFor("country", ipAddress);
        return new Country(json);
    }

    public City City(String ipAddress) throws GeoIP2Exception {
        JSONObject json = responseFor("city", ipAddress);
        return new City(json);
    }

    public Omni Omni(String ipAddress) throws GeoIP2Exception {
        JSONObject json = responseFor("omni", ipAddress);
        return new Omni(json);
    }

    public CityISPOrg CityISPOrg(String ipAddress) throws GeoIP2Exception {
        JSONObject json = responseFor("city_isp_org", ipAddress);
        return new CityISPOrg(json);
    }

    public void setHost(String host) {
        this.host = host;
    }

    private JSONObject responseFor(String path, String ip_address)
            throws GeoIP2Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String uri = "https://" + host;
        if (host.startsWith("localhost")) {
            uri = "http://" + host;
        }
        uri = uri + "/geoip/v2.0/" + path + "/" + ip_address;
        HttpGet httpget = new HttpGet(uri);
        httpget.addHeader("Accept", "application/json");
        httpget.addHeader(BasicScheme.authenticate(
                new UsernamePasswordCredentials(userId, licenseKey), "UTF-8",
                false));
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
            return handleSuccess(response, uri);
        } else {
            handleErrorStatus(response, status, uri);
        }
        return null;
    }

    private String getContent(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        InputStream instream = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                instream));
        String content = reader.readLine();
        instream.close();
        return content;
    }

    private JSONObject handleSuccess(HttpResponse response, String uri)
            throws GeoIP2Exception {
        JSONObject json = null;
        String content;
        try {
            content = getContent(response);
        } catch (IOException e) {
            throw new GeoIP2Exception("Received a 200 response for " + uri
                    + " but could not parse the message content: \n"
                    + e.getMessage(), e);
        }

        if (content == null) {
            throw new GeoIP2Exception("Received a 200 response for " + uri
                    + " but there was no message body.");
        }

        try {
            json = new JSONObject(content);
        } catch (JSONException e) {
            throw new GeoIP2Exception("Received a 200 response for " + uri
                    + " but could not decode the response as JSON: \n"
                    + e.getMessage(), e);
        }
        return json;
    }

    private void handleErrorStatus(HttpResponse response, int status, String uri)
            throws GeoIP2Exception {
        if ((status >= 400) && (status < 500)) {
            handle4xxStatus(response, status, uri);
        } else if ((status >= 500) && (status < 600)) {
            handle5xxStatus(response, status, uri);
        } else {
            handleNon200Status(response, status, uri);
        }
    }

    private void handle4xxStatus(HttpResponse response, int status, String uri)
            throws GeoIP2Exception {
        String JSON_Message = "Received a " + status + " error for " + uri
                + " but it did not include the expected JSON body: ";
        String content = "";
        try {
            content = getContent(response);
        } catch (IOException e) {
            throw new GeoIP2Exception("Received a " + status + " response for "
                    + uri + " but could not parse the message content: \n"
                    + e.getMessage(), e);
        }

        if (content == null) {
            throw new HTTPException("Received a " + status + " error for "
                    + uri + " with no body", status, uri);
        }

        JSONObject body;
        String contentType = response.getEntity().getContentType().getValue();
        if (contentType.indexOf("json") != -1) {
            try {
                body = new JSONObject(content);
            } catch (JSONException e) {
                throw new HTTPException(JSON_Message + e.getMessage(), e,
                        status, uri);
            }
        } else {
            throw new HTTPException("Received a " + status + " error for "
                    + uri + "with the following body: " + content, status, uri);
        }

        try{
            String code = body.getString("code");
            String error = body.getString("error");
            throw new WebServiceException(error, code, status, uri);
        } catch (JSONException e) {
            throw new HTTPException(
                    JSON_Message
                            + "Response contains JSON but it does not specify code or error keys",
                    status, uri);
        }
    }

    private void handle5xxStatus(HttpResponse response, int status, String uri)
            throws HTTPException {
        throw new HTTPException("Received a server error (" + status + ") for "
                + uri, status, uri);

    }

    private void handleNon200Status(HttpResponse response, int status,
            String uri) throws HTTPException {
        throw new HTTPException("Received a very surprising HTTP status ("
                + status + ") for " + uri, status, uri);
    }
}
