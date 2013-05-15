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
        if (json != null) {
            return new Country(json);
        }
        return null;
    }

    public City City(String ipAddress) throws GeoIP2Exception {
        JSONObject json = responseFor("city", ipAddress);
        if (json != null) {
            return new City(json);
        }
        return null;
    }

    public Omni Omni(String ipAddress) throws GeoIP2Exception {
        JSONObject json = responseFor("omni", ipAddress);
        if (json != null) {
            return new Omni(json);
        }
        return null;
    }

    public CityISPOrg CityISPOrg(String ipAddress) throws GeoIP2Exception {
        JSONObject json = responseFor("city_isp_org", ipAddress);
        if (json != null) {
            return new CityISPOrg(json);
        }
        return null;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private JSONObject responseFor(String path, String ip_address)
            throws GeoIP2Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            // String uri = "https://ct4-test.maxmind.com/geoip/" + path + "/" +
            // ip_address;
            String uri = "https://" + host;
            if (host.startsWith("localhost")) {
                uri = "http://" + host;
            }
            uri = uri + "/geoip/v2.0/" + path + "/" + ip_address;
            HttpGet httpget = new HttpGet(uri);
            httpget.addHeader("Accept", "application/json");
            httpget.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials(userId, licenseKey),
                    "UTF-8", false));
            HttpResponse response = httpclient.execute(httpget);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                return handleSuccess(response, uri);
            } else {
                handleErrorStatus(response, status, uri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return null;
    }

    private String getContent(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return "";
        }
        InputStream instream = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                instream));
        String content = reader.readLine();
        instream.close();
        return content;
    }

    private JSONObject handleSuccess(HttpResponse response, String uri)
            throws IOException, GeoIP2Exception {
        JSONObject json = null;
        try {
            String content = getContent(response);
            if (content == null) {
                content = "";
            }
            System.out.println(content);
            json = new JSONObject(content);
        } catch (JSONException e) {
            throw new GeoIP2Exception("Received a 200 response for " + uri
                    + " but could not decode the response as JSON: \n"
                    + e.getMessage(), e);
        }
        return json;
    }

    private void handleErrorStatus(HttpResponse response, int status,
            String uri) throws IOException, GeoIP2Exception {
        if ((status >= 400) && (status < 500)) {
            handle4xxStatus(response, status, uri);
        } else if ((status >= 500) && (status < 600)) {
            handle5xxStatus(response, status, uri);
        } else {
            handleNon200Status(response, status, uri);
        }
    }

    private void handle4xxStatus(HttpResponse response, int status, String uri)
            throws IOException, HTTPException {
        String JSON_Message = "Received a " + status + " error for " + uri
                + " but it did not include the expected JSON body: ";
        String content = getContent(response);
        if (content == null) {
            content = "";
        }
        System.out.println(content);
        JSONObject body;
        if (content.equals("") == false) {
            String contentType = response.getEntity().getContentType()
                    .getValue();
            if (contentType.indexOf("json") != -1) {
                try {
                    body = new JSONObject(content);
                } catch (JSONException e) {
                    throw new HTTPException(JSON_Message + e.getMessage(), e,
                            status, uri);
                }
            } else {
                throw new HTTPException("Received a " + status + " error for "
                        + uri + "with the following body: " + content, status,
                        uri);
            }
        } else {
            throw new HTTPException("Received a " + status + " error for "
                    + uri + " with no body", status, uri);
        }
        if (body.has("code") & body.has("error")) {
            try {
                String code = body.getString("code");
                String error = body.getString("error");
                throw new WebServiceException(error, code, status, uri);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
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
