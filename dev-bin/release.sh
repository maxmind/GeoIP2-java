#!/bin/bash

set -e

VERSION=$(perl -MFile::Slurp::Tiny=read_file -MDateTime <<EOF
use v5.16;
my \$log = read_file(q{CHANGELOG.md});
\$log =~ /\n(\d+\.\d+\.\d+(?:-[\d\w\.]+)) \((\d{4}-\d{2}-\d{2})\)\n/;
die 'Release time is not today!' unless DateTime->now->ymd eq \$2;
say \$1;
EOF
)

TAG="v$VERSION"

if [ -n "$(git status --porcelain)" ]; then
    echo ". is not clean." >&2
    exit 1
fi


if [ ! -d .gh-pages ]; then
    echo "Checking out gh-pages in .gh-pages"
    git clone -b gh-pages git@github.com:maxmind/GeoIP2-java .gh-pages
    pushd .gh-pages
else
    echo "Updating .gh-pages"
    pushd .gh-pages
    git pull
fi

if [ -n "$(git status --porcelain)" ]; then
    echo ".gh-pages is not clean" >&2
    exit 1
fi

popd

mvn versions:display-dependency-updates

read -e -p "Continue given above dependencies? (y/n) " SHOULD_CONTINUE

if [ "$SHOULD_CONTINUE" != "y" ]; then
    echo "Aborting"
    exit 1
fi

PAGE=.gh-pages/index.md
cat <<EOF > $PAGE
---
layout: default
title: MaxMind GeoIP2 Java API
language: java
version: $TAG
---

EOF

export VERSION
# alter the documentation to point to this version
perl -pi -e 's/(?<=<version>)[^<]*/$ENV{VERSION}/' README.md
perl -pi -e 's/(?<=com\.maxmind\.geoip2\:geoip2\:)\d+\.\d+\.\d+([\w\-]+)?/$ENV{VERSION}/' README.md
cat README.md >> $PAGE

if [ -n "$(git status --porcelain)" ]; then
    git diff

    read -e -p "Commit README.md changes? " SHOULD_COMMIT
    if [ "$SHOULD_COMMIT" != "y" ]; then
        echo "Aborting"
        exit 1
    fi
    git add README.md
    git commit -m 'update version number in README.md'
fi

# could be combined with the primary build
mvn release:clean
mvn release:prepare -DreleaseVersion="$VERSION" -Dtag="$TAG"
mvn release:perform
rm -fr ".gh-pages/doc/$TAG"
cp -r target/apidocs ".gh-pages/doc/$TAG"

pushd .gh-pages

git add doc/
git commit -m "Updated for $TAG" -a

read -e -p "Push to origin? " SHOULD_PUSH

if [ "$SHOULD_PUSH" != "y" ]; then
    echo "Aborting"
    exit 1
fi

git push

popd

git push
git push --tags

echo "Remember to do the release on https://oss.sonatype.org/!"
