package com.maxmind.geoip2.json;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class File {
    public static String readJsonFile(String name) throws IOException,
           URISyntaxException {
        URL resource = File.class
                .getResource("/test-data/" + name + ".json");
        return new String(Files.readAllBytes(Paths.get(resource.toURI())),
                StandardCharsets.UTF_8);
    }
}
