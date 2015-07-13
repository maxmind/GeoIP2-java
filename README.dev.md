There is a release script at `dev-bin/release.sh` that will do the full
release, including updating the GitHub Pages site.

This script reads the VERSION number from CHANGELOG.md, which you should have
updated to contain the new version number and today's date. After uploading with
this script, you will need to perform the release on the [Sonatype OSS
site](https://oss.sonatype.org/index.html).

We release to the Maven Central Repository through Sonatype OSSRH. They
provide [detailed directions](http://central.sonatype.org/pages/apache-maven.html)
on the steps of the release or snapshot release process.

After releasing, update the
[GitHub Release](https://github.com/maxmind/GeoIP2-java/releases), including
attaching `target/geoip2-2.2.0-with-dependencies.zip*` to the release (both
the zip file and the `.asc`.)

All releases should follow [Semantic Versioning](http://semver.org/).
