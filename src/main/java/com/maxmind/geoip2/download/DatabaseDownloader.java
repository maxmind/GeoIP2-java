/* HEADER */
package com.maxmind.geoip2.download;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.maxmind.geoip2.exception.HttpException;

/**
 * {@code Downloader} responsible for downloading the currently available database and it's related checksum.
 */
public class DatabaseDownloader implements Closeable {
    static final String DATABASE_FILE_SUFFIX = "tar.gz";
    static final String DATABASE_MD5_SUFFIX = "tar.gz.md5";

    private final String host;
    private final String editionId;
    private final String licenseKey;
    private final boolean useHttps;
    private final int port;
    private final CloseableHttpClient httpClient;

    public DatabaseDownloader(Builder builder) {
        host = builder.host;
        port = builder.port;
        useHttps = builder.useHttps;
        editionId = builder.editionId;
        licenseKey = builder.licenseKey;

        RequestConfig.Builder configBuilder = RequestConfig.custom().setConnectTimeout(builder.connectTimeout)
                        .setSocketTimeout(builder.readTimeout);

        if (builder.proxy != null) {
            InetSocketAddress address = (InetSocketAddress) builder.proxy.address();
            HttpHost proxyHost = new HttpHost(address.getHostName(), address.getPort());
            configBuilder.setProxy(proxyHost);
        }

        RequestConfig config = configBuilder.build();
        httpClient = HttpClientBuilder.create().setUserAgent(userAgent()).setDefaultRequestConfig(config).build();
    }

    private String userAgent() {
        return "GeoIP2/" + getClass().getPackage().getImplementationVersion() + " (Java/" + System.getProperty("java.version")
                        + ")";
    }

    /**
     * Returns the checksum of the currently available database.
     *
     * @return
     * @throws IOException
     */
    public String getDatabaseMd5() throws IOException {
        return responseFor(DATABASE_MD5_SUFFIX, new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        });
    }

    /**
     * Returns the currently available database.
     *
     * @param file
     * @return
     * @throws IOException
     */
    public void downloadDatabaseToFile(final File file) throws IOException {
        responseFor(DATABASE_FILE_SUFFIX, new ResponseHandler<Void>() {
            @Override
            public Void handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                InputStream source = response.getEntity().getContent();
                try (OutputStream output = new BufferedOutputStream(new FileOutputStream(file))) {
                    IOUtils.copy(source, output);
                }
                return null;
            }
        });
    }

    private <T> T responseFor(String suffix, ResponseHandler<T> handler) throws IOException {
        try {
            URL url = new URIBuilder().setScheme(useHttps ? "https" : "http").setHost(host).setPort(port)
                            .setPath("/app/geoip_download").addParameter("edition_id", editionId).addParameter("suffix", suffix)
                            .addParameter("license_key", licenseKey).build().toURL();
            HttpGet request = new HttpGet(url.toURI());
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return handleResponse(response, url, handler);
            }
        } catch (URISyntaxException use) {
            throw new IOException("Error parsing request URL", use);
        }
    }

    private <T> T handleResponse(CloseableHttpResponse response, URL url, ResponseHandler<T> handler) throws IOException {
        int status = response.getStatusLine().getStatusCode();
        if (status >= 400 && status < 600) {
            throw new HttpException("Received a server error (" + status + ") for " + url, status, url);
        } else if (status != 200) {
            throw new HttpException("Received an unexpected HTTP status (" + status + ") for " + url, status, url);
        }

        try {
            return handler.handleResponse(response);
        } catch (IOException ioe) {
            throw new IOException("Received a 200 response but could not decode ", ioe);
        } finally {
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
        }
    }

    /**
     * Close any open connections and return resources to the system.
     */
    @Override
    public void close() throws IOException {
        httpClient.close();
    }

    /**
     * <p>
     * {@code Builder} creates instances of {@code DatabaseDownloader} from values set by the methods.
     * </p>
     * <p>
     * This example shows how to create a {@code DatabaseDownloader} object with the {@code Builder}:
     * </p>
     * <p>
     * DatabaseDownloader client = new DatabaseDownloader.Builder("GeoIP2-City","licensekey").build();
     * </p>
     * <p>
     * Only the values set in the {@code Builder} constructor are required.
     * </p>
     */
    public static final class Builder {
        private final String editionId;
        private final String licenseKey;

        private String host = "download.maxmind.com";
        private int port = 443;
        private boolean useHttps = true;

        private int connectTimeout = 3000;
        private int readTimeout = 20000;
        private Proxy proxy;

        /**
         * @param editionId Your MaxMind edition ID.
         * @param licenseKey Your MaxMind license key.
         */
        public Builder(String editionId, String licenseKey) {
            this.editionId = editionId;
            this.licenseKey = licenseKey;
        }

        /**
         * @param val Timeout in milliseconds to establish a connection to the service. The default is 3000 (3 seconds).
         * @return Builder object
         */
        public Builder connectTimeout(int val) {
            connectTimeout = val;
            return this;
        }

        /**
         * Disables HTTPS to connect to a test server or proxy.
         *
         * @return Builder object
         */
        public Builder disableHttps() {
            useHttps = false;
            return this;
        }

        /**
         * @param val The host to use.
         * @return Builder object
         */
        public Builder host(String val) {
            host = val;
            return this;
        }

        /**
         * @param val The port to use.
         * @return Builder object
         */
        public Builder port(int val) {
            port = val;
            return this;
        }

        /**
         * @param val readTimeout in milliseconds to read data from an established connection to the service. The default is 20000
         *        (20 seconds).
         * @return Builder object
         */
        public Builder readTimeout(int val) {
            readTimeout = val;
            return this;
        }

        /**
         * @param val the proxy to use when making this request.
         * @return Builder object
         */
        public Builder proxy(Proxy val) {
            proxy = val;
            return this;
        }

        /**
         * @return an instance of {@code DatabaseDownloader} created from the fields set on this builder.
         */
        public DatabaseDownloader build() {
            return new DatabaseDownloader(this);
        }
    }
}