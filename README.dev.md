There is a release script at `dev-bin/release.sh` that will do the full
release, including updating the GitHub Pages site.

This script reads the VERSION number from `CHANGELOG.md`, which you should
have updated to contain the new version number and today's date. After
uploading with this script, you will need to perform the release on the
[Sonatype OSS site](https://oss.sonatype.org/index.html).

We release to the Maven Central Repository through Sonatype OSSRH. They
provide [detailed directions](https://central.sonatype.org/pages/apache-maven.html)
on the steps of the release or snapshot release process.

All releases should follow [Semantic Versioning](https://semver.org/).

Steps for releasing:

1. Review open issues and PRs to see if any can easily be fixed, closed, or
   merged.
2. Bump copyright year in `README.md`, if necessary.
   * You do not need to update the version. The release script will do so.
3. Review `CHANGELOG.md` for completeness and correctness. Update its release
   date. Commit it.
4. Install or update `hub` as it used by the release script.
5. Test that `mvn package` can complete successfully. Run `git clean -dxff`
   or something similar to clean up afterwards.
5. Run `./dev-bin/release.sh`.
   * This will package the release, update the gh-pages branch, bump the
     version to the next development release, upload the release to GitHub
     and tag it, and upload to Sonatype.
   * It may prompt you about out of date dependencies. You should consider
     updating them if appropriate. Say no and review the changes and upate
     `pom.xml` and start the release process over again if you do.
6. Complete the release on Sonatype

There is more information in the
[minfraud-api-java](https://github.com/maxmind/minfraud-api-java/blob/master/README.dev.md)
`README.dev.md` about doing a Java release, including setting up your
environment and completing the release on Sonatype.
