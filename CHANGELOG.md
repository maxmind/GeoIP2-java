CHANGELOG
=========

0.3.0 (2013-06-??)
------------------

* Reorganized the classes. `Client` was renamed `WebServiceClient` and moved
  to `com.maxmind.geoip2`. Record classes now have a suffix of "Record".
  The product classes (e.g., Omni) were renamed to their product name with
  no "Lookup" suffix.
* Additional specific exceptions were added to replace the general
  `WebServiceException`.
* A `DatabaseReader` class was added to the distribution. This reads GeoIP2
  databases and returns similar product object to `WebServiceClient`.
* A `fallbackDatabase` option was added to `WebServiceClient.Builder`.
  This allows you to specify a database to fall back to if the web service
  request fails.

0.2.0 (2013-06-13)
------------------

* Replaced the public constructor on `Client` with a `Builder` class.

0.1.1 (2013-06-10)
------------------

* First official beta release.
* Documentation updates and corrections.
* Changed license to Apache License, Version 2.0.

0.1.0 (2013-05-21)
------------------

* Initial release
