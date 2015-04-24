There is a release script at `dev-bin/release.sh` that will do the full
release, including updating the GitHub Pages site. This script takes a Git tag
as a parameter. The tag should be of the form "vX.Y.Z". After uploading with
this script, you will need to perform the release on the [Sonatype OSS
site](https://oss.sonatype.org/index.html).

We release to the Maven Central Repository through Sonatype OSSRH. They
provide [detailed directions](http://central.sonatype.org/pages/apache-maven.html)
on the steps of the release or snapshot release process.

All releases should follow [Semantic Versioning](http://semver.org/).
