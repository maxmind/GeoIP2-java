---
layout: default
title: MaxMind GeoIP2 Java API
language: java
version: v4.3.0
---

# GeoIP2 Java API #

## Description ##

This distribution provides an API for the GeoIP2 and GeoLite2 [web
services](https://dev.maxmind.com/geoip/docs/web-services?lang=en) and
[databases](https://dev.maxmind.com/geoip/docs/databases?lang=en).

## Installation ##

### Maven ###

We recommend installing this package with [Maven](https://maven.apache.org/).
To do this, add the dependency to your pom.xml:

```xml
    <dependency>
        <groupId>com.maxmind.geoip2</groupId>
        <artifactId>geoip2</artifactId>
        <version>4.3.0</version>
    </dependency>
```

### Gradle ###

Add the following to your `build.gradle` file:

```
repositories {
    mavenCentral()
}
dependencies {
    compile 'com.maxmind.geoip2:geoip2:4.3.0'
}
```

### JAR Files ###

If you are unable to use Maven or Gradle, you may include the `geoip2.jar`
file and its dependencies in your classpath. Download the JAR files from the
[GitHub Releases page](https://github.com/maxmind/GeoIP2-java/releases).

## IP Geolocation Usage ##

IP geolocation is inherently imprecise. Locations are often near the center of
the population. Any location provided by a GeoIP2 database or web service
should not be used to identify a particular address or household.

## Web Service Usage ##

To use the web service API, you must create a new `WebServiceClient` using the
`WebServiceClient.Builder`. You must provide the `Builder` constructor your
MaxMind `accountId` and `licenseKey`. To use the GeoLite2 web services instead
of GeoIP2, set the `host` method on the builder to `geolite.info`. To use
the Sandbox GeoIP2 web services instead of the production GeoIP2 web
services, set the `host` method on the builder to `sandbox.maxmind.com`.
You may also set a `timeout` or set the `locales` fallback order using the
methods on the `Builder`. After you have created the `WebServiceClient`,
you may then call the method corresponding to a specific web service, passing
it the IP address you want to look up.

If the request succeeds, the method call will return a model class for the end
point you called. This model in turn contains multiple record classes, each of
which represents part of the data returned by the web service.

If the request fails, the client class throws an exception.

The `WebServiceClient` object is safe to share across threads. If you are
making multiple requests, the object should be reused so that new connections
are not created for each request.

See the [API documentation](https://maxmind.github.io/GeoIP2-java/) for
more details.

## Web Service Example ##

### Country Service ###

```java
// This creates a WebServiceClient object that is thread-safe and can be
// reused across requests. Reusing the object will allow it to keep
// connections alive for future requests.
//
// Replace "42" with your account ID and "license_key" with your license key.
// To use the GeoLite2 web service instead of the GeoIP2 web service, call the
// host method on the builder with "geolite.info", e.g.
// new WebServiceClient.Builder(42, "license_key").host("geolite.info").build()
// To use the Sandbox GeoIP2 web service instead of the production GeoIP2
// web service, call the host method on the builder with
// "sandbox.maxmind.com", e.g.
// new WebServiceClient.Builder(42, "license_key").host("sandbox.maxmind.com").build()
WebServiceClient client = new WebServiceClient.Builder(42, "license_key")
    .build();

InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

// Do the lookup
CountryResponse response = client.country(ipAddress);

Country country = response.getCountry();
System.out.println(country.getIsoCode());            // 'US'
System.out.println(country.getName());               // 'United States'
System.out.println(country.getNames().get("zh-CN")); // '美国'
```

### City Plus Service ###

```java
// This creates a WebServiceClient object that is thread-safe and can be
// reused across requests. Reusing the object will allow it to keep
// connections alive for future requests.
//
// Replace "42" with your account ID and "license_key" with your license key.
// To use the GeoLite2 web service instead of the GeoIP2 web service, call the
// host method on the builder with "geolite.info", e.g.
// new WebServiceClient.Builder(42, "license_key").host("geolite.info").build()
// To use the Sandbox GeoIP2 web service instead of the production GeoIP2
// web service, call the host method on the builder with
// "sandbox.maxmind.com", e.g.
// new WebServiceClient.Builder(42, "license_key").host("sandbox.maxmind.com").build()
WebServiceClient client = new WebServiceClient.Builder(42, "license_key")
    .build();

InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

// Do the lookup
CityResponse response = client.city(ipAddress);

Country country = response.getCountry();
System.out.println(country.getIsoCode());            // 'US'
System.out.println(country.getName());               // 'United States'
System.out.println(country.getNames().get("zh-CN")); // '美国'

Subdivision subdivision = response.getMostSpecificSubdivision();
System.out.println(subdivision.getName());       // 'Minnesota'
System.out.println(subdivision.getIsoCode());    // 'MN'

City city = response.getCity();
System.out.println(city.getName());       // 'Minneapolis'

Postal postal = response.getPostal();
System.out.println(postal.getCode());       // '55455'

Location location = response.getLocation();
System.out.println(location.getLatitude());        // 44.9733
System.out.println(location.getLongitude());       // -93.2323
```

### Insights Service ###

```java
// This creates a WebServiceClient object that is thread-safe and can be
// reused across requests. Reusing the object will allow it to keep
// connections alive for future requests.
//
// Replace "42" with your account ID and "license_key" with your license key.
// Please note that the GeoLite2 web service does not support Insights.
// To use the Sandbox GeoIP2 web service instead of the production GeoIP2
// web service, call the host method on the builder with
// "sandbox.maxmind.com", e.g.
// new WebServiceClient.Builder(42, "license_key").host("sandbox.maxmind.com").build()
WebServiceClient client = new WebServiceClient.Builder(42, "license_key")
    .build();

InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

// Do the lookup
InsightsResponse response = client.insights(ipAddress);

Country country = response.getCountry();
System.out.println(country.getIsoCode());            // 'US'
System.out.println(country.getName());               // 'United States'
System.out.println(country.getNames().get("zh-CN")); // '美国'
System.out.println(country.getConfidence());         // 99

Subdivision subdivision = response.getMostSpecificSubdivision();
System.out.println(subdivision.getName());       // 'Minnesota'
System.out.println(subdivision.getIsoCode());    // 'MN'
System.out.println(subdivision.getConfidence()); // 90

City city = response.getCity();
System.out.println(city.getName());       // 'Minneapolis'
System.out.println(city.getConfidence()); // 50

Postal postal = response.getPostal();
System.out.println(postal.getCode());       // '55455'
System.out.println(postal.getConfidence()); // 40

Location location = response.getLocation();
System.out.println(location.getLatitude());        // 44.9733
System.out.println(location.getLongitude());       // -93.2323
System.out.println(location.getAccuracyRadius());  // 3
System.out.println(location.getTimeZone());        // 'America/Chicago'

System.out.println(response.getTraits().getUserType()); // 'college'
```

## Database Usage ##

To use the database API, you must create a new `DatabaseReader` using the
`DatabaseReader.Builder`. You must provide the `Builder` constructor either an
`InputStream` or `File` for your GeoIP2 database. You may also specify the
`fileMode` and the `locales` fallback order using the methods on the `Builder`
object.

After you have created the `DatabaseReader`, you may then call one of the
appropriate methods, e.g., `city` or `tryCity`, for your database. These
methods take the IP address to be looked up. The methods with the `try`
prefix return an `Optional` object, which will be empty if the value is
not present in the database. The method without the prefix will throw an
`AddressNotFoundException` if the address is not in the database. If you
are looking up many IPs that are not contained in the database, the `try`
method will be slightly faster as they do not need to construct and throw
an exception. These methods otherwise behave the same.

If the lookup succeeds, the method call will return a response class for the
GeoIP2 lookup. The class in turn contains multiple record classes, each of
which represents part of the data returned by the database.

We recommend reusing the `DatabaseReader` object rather than creating a new
one for each lookup. The creation of this object is relatively expensive as it
must read in metadata for the file. It is safe to share the object across
threads.

See the [API documentation](https://maxmind.github.io/GeoIP2-java/) for
more details.

### Caching ###

The database API supports pluggable caching (by default, no caching is
performed). A simple implementation is provided by `com.maxmind.db.CHMCache`.
Using this cache, lookup performance is significantly improved at the cost of
a small (~2MB) memory overhead.

Usage:

```java
new DatabaseReader.Builder(file).withCache(new CHMCache()).build();
```

### Packaging Database in a JAR ###

If you are packaging the database file as a resource in a JAR file using
Maven, you must
[disable binary file filtering](https://maven.apache.org/plugins/maven-resources-plugin/examples/binaries-filtering.html).
Failure to do so will result in `InvalidDatabaseException` exceptions being
thrown when querying the database.

## Database Example ##

### City ###

```java
// A File object pointing to your GeoIP2 or GeoLite2 database
File database = new File("/path/to/GeoIP2-City.mmdb");

// This creates the DatabaseReader object. To improve performance, reuse
// the object across lookups. The object is thread-safe.
DatabaseReader reader = new DatabaseReader.Builder(database).build();

InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

// Replace "city" with the appropriate method for your database, e.g.,
// "country".
CityResponse response = reader.city(ipAddress);

Country country = response.getCountry();
System.out.println(country.getIsoCode());            // 'US'
System.out.println(country.getName());               // 'United States'
System.out.println(country.getNames().get("zh-CN")); // '美国'

Subdivision subdivision = response.getMostSpecificSubdivision();
System.out.println(subdivision.getName());    // 'Minnesota'
System.out.println(subdivision.getIsoCode()); // 'MN'

City city = response.getCity();
System.out.println(city.getName()); // 'Minneapolis'

Postal postal = response.getPostal();
System.out.println(postal.getCode()); // '55455'

Location location = response.getLocation();
System.out.println(location.getLatitude());  // 44.9733
System.out.println(location.getLongitude()); // -93.2323
```

### Anonymous IP ###

```java
// A File object pointing to your GeoIP2 Anonymous IP database
File database = new File("/path/to/GeoIP2-Anonymous-IP.mmdb");

// This creates the DatabaseReader object. To improve performance, reuse
// the object across lookups. The object is thread-safe.
try (DatabaseReader reader = new DatabaseReader.Builder(database).build()) {
    InetAddress ipAddress = InetAddress.getByName("85.25.43.84");

    AnonymousIpResponse response = reader.anonymousIp(ipAddress);

    System.out.println(response.isAnonymous()); // true
    System.out.println(response.isAnonymousVpn()); // false
    System.out.println(response.isHostingProvider()); // false
    System.out.println(response.isPublicProxy()); // false
    System.out.println(response.isResidentialProxy()); // false
    System.out.println(response.isTorExitNode()); //true
}
```

### Anonymous Plus ###

```java
// A File object pointing to your GeoIP2 Anonymous Plus database
File database = new File("/path/to/GeoIP-Anonymous-Plus.mmdb");

// This creates the DatabaseReader object. To improve performance, reuse
// the object across lookups. The object is thread-safe.
try (DatabaseReader reader = new DatabaseReader.Builder(database).build()) {
    InetAddress ipAddress = InetAddress.getByName("85.25.43.84");

    AnonymousIpResponse response = reader.anonymousPlus(ipAddress);

    System.out.println(response.getAnonymizerConfidence()); // 30
    System.out.println(response.isAnonymous()); // true
    System.out.println(response.isAnonymousVpn()); // false
    System.out.println(response.isHostingProvider()); // false
    System.out.println(response.isPublicProxy()); // false
    System.out.println(response.isResidentialProxy()); // false
    System.out.println(response.isTorExitNode()); // true
    System.out.println(response.getNetworkLastSeen()); // "2025-04-14"
    System.out.println(response.getProviderName()); // "FooBar VPN"
}
```

### ASN ###

```java
// A File object pointing to your GeoLite2 ASN database
File database = new File("/path/to/GeoLite2-ASN.mmdb");

// This creates the DatabaseReader object. To improve performance, reuse
// the object across lookups. The object is thread-safe.
try (DatabaseReader reader = new DatabaseReader.Builder(database).build()) {

    InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

    AsnResponse response = reader.asn(ipAddress);

    System.out.println(response.getAutonomousSystemNumber());       // 217
    System.out.println(response.getAutonomousSystemOrganization()); // 'University of Minnesota'
}
```

### Connection-Type ###

```java
// A File object pointing to your GeoIP2 Connection-Type database
File database = new File("/path/to/GeoIP2-Connection-Type.mmdb");

// This creates the DatabaseReader object. To improve performance, reuse
// the object across lookups. The object is thread-safe.
DatabaseReader reader = new DatabaseReader.Builder(database).build();

InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

ConnectionTypeResponse response = reader.connectionType(ipAddress);

// getConnectionType() returns a ConnectionType enum
ConnectionType type = response.getConnectionType();

System.out.println(type); // 'Corporate'
```

### Domain ###

```java
// A File object pointing to your GeoIP2 Domain database
File database = new File("/path/to/GeoIP2-Domain.mmdb");

// This creates the DatabaseReader object. To improve performance, reuse
// the object across lookups. The object is thread-safe.
DatabaseReader reader = new DatabaseReader.Builder(database).build();

InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

DomainResponse response = reader.domain(ipAddress);

System.out.println(response.getDomain()); // 'umn.edu'
```

### Enterprise ###

```java
// A File object pointing to your GeoIP2 Enterprise database
File database = new File("/path/to/GeoIP2-Enterprise.mmdb");

// This creates the DatabaseReader object. To improve performance, reuse
// the object across lookups. The object is thread-safe.
try (DatabaseReader reader = new DatabaseReader.Builder(database).build()) {
    InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

    //  Use the enterprise(ip) method to do a lookup in the Enterprise database
    EnterpriseResponse response = reader.enterprise(ipAddress);

    Country country = response.getCountry();
    System.out.println(country.getIsoCode());            // 'US'
    System.out.println(country.getName());               // 'United States'
    System.out.println(country.getNames().get("zh-CN")); // '美国'
    System.out.println(country.getConfidence());         // 99

    Subdivision subdivision = response.getMostSpecificSubdivision();
    System.out.println(subdivision.getName());           // 'Minnesota'
    System.out.println(subdivision.getIsoCode());        // 'MN'
    System.out.println(subdivision.getConfidence());     // 77

    City city = response.getCity();
    System.out.println(city.getName());       // 'Minneapolis'
    System.out.println(city.getConfidence()); // 11

    Postal postal = response.getPostal();
    System.out.println(postal.getCode()); // '55455'
    System.out.println(postal.getConfidence()); // 5

    Location location = response.getLocation();
    System.out.println(location.getLatitude());  // 44.9733
    System.out.println(location.getLongitude()); // -93.2323
    System.out.println(location.getAccuracyRadius()); // 50
}
```

### ISP ###

```java
// A File object pointing to your GeoIP2 ISP database
File database = new File("/path/to/GeoIP2-ISP.mmdb");

// This creates the DatabaseReader object. To improve performance, reuse
// the object across lookups. The object is thread-safe.
DatabaseReader reader = new DatabaseReader.Builder(database).build();

InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

IspResponse response = reader.isp(ipAddress);

System.out.println(response.getAutonomousSystemNumber());       // 217
System.out.println(response.getAutonomousSystemOrganization()); // 'University of Minnesota'
System.out.println(response.getIsp());                          // 'University of Minnesota'
System.out.println(response.getOrganization());                 // 'University of Minnesota'
```

## Exceptions ##

For details on the possible errors returned by the web service itself, [see
the GeoIP2 web service
documentation](https://dev.maxmind.com/geoip/docs/web-services?lang=en).

If the web service returns an explicit error document, this is thrown as an
`AddressNotFoundException`, an `AuthenticationException`, an
`InvalidRequestException`, or an `OutOfQueriesException`.

If some sort of transport error occurs, an `HttpException` is thrown. This
is thrown when some sort of unanticipated error occurs, such as the web
service returning a 500 or an invalid error document. If the web service
request returns any status code besides 200, 4xx, or 5xx, this also becomes
an `HttpException`.

Finally, if the web service returns a 200 but the body is invalid, the client
throws a `GeoIp2Exception`. This exception also is the parent exception to
the above exceptions.

## Values to use for Database or Map Keys ##

**We strongly discourage you from using a value from any `getNames` method as
a key in a database or map.**

These names may change between releases. Instead we recommend using one of the
following:

* `com.maxmind.geoip2.record.City` - `City.getGeoNameId`
* `com.maxmind.geoip2.record.Continent` - `Continent.getCode` or `Continent.getGeoNameId`
* `com.maxmind.geoip2.record.Country` and `com.maxmind.geoip2.record.RepresentedCountry` - `Country.getIsoCode`
  or `Country.getGeoNameId`
* `com.maxmind.geoip2.record.Subdivision` - `Subdivision.getIsoCode` or `Subdivision.getGeoNameId`

## Multi-Threaded Use ##

This API fully supports use in multi-threaded applications. When using the
`DatabaseReader` or the `WebServiceClient` in a multi-threaded application,
we suggest creating one object and sharing that across threads.

## What data is returned? ##

While many of the location databases and web services return the same
basic records, the attributes populated can vary. In addition, MaxMind does
not always have every piece of data for any given IP address.

Because of these factors, it is possible for any web service to return a record
where some or all of the attributes are unpopulated.

[See our web-service developer
documentation](https://dev.maxmind.com/geoip/docs/web-services?lang=en) for
details on what data each web service may return.

The only piece of data which is always returned is the `ip_address`
available at `lookup.getTraits().getIpAddress()`.

## Integration with GeoNames ##

[GeoNames](https://www.geonames.org/) offers web services and downloadable
databases with data on geographical features around the world, including
populated places. They offer both free and paid premium data. Each
feature is uniquely identified by a `geonameId`, which is an integer.

Many of the records returned by the GeoIP2 web services and databases
include a `getGeoNameId()` method. This is the ID of a geographical
feature (city, region, country, etc.) in the GeoNames database.

Some of the data that MaxMind provides is also sourced from GeoNames. We
source things like place names, ISO codes, and other similar data from
the GeoNames premium data set.

## Reporting data problems ##

If the problem you find is that an IP address is incorrectly mapped,
please
[submit your correction to MaxMind](https://www.maxmind.com/en/correction).

If you find some other sort of mistake, like an incorrect spelling,
please check [the GeoNames site](https://www.geonames.org/) first. Once
you've searched for a place and found it on the GeoNames map view, there
are a number of links you can use to correct data ("move", "edit",
"alternate names", etc.). Once the correction is part of the GeoNames
data set, it will be automatically incorporated into future MaxMind
releases.

If you are a paying MaxMind customer and you're not sure where to submit
a correction, please [contact MaxMind support](https://www.maxmind.com/en/support)
for help.

## Other Support ##

Please report all issues with this code using the
[GitHub issue tracker](https://github.com/maxmind/GeoIP2-java/issues).

If you are having an issue with a MaxMind service that is not specific
to the client API, please
[contact MaxMind support](https://www.maxmind.com/en/support).

## Requirements  ##

MaxMind has tested this API with Java 11 and above.

## Contributing ##

Patches and pull requests are encouraged. Please include unit tests
whenever possible.

## Versioning ##

The GeoIP2 Java API uses [Semantic Versioning](https://semver.org/).

## Copyright and License ##

This software is Copyright (c) 2013-2025 by MaxMind, Inc.

This is free software, licensed under the Apache License, Version 2.0.
