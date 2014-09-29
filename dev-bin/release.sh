#!/bin/bash

set -e

TAG=$1

if [ -z $TAG ]; then
    echo "Please specify a tag"
    exit 1
fi

if [ -n "$(git status --porcelain)" ]; then
    echo ". is not clean." >&2
    exit 1
fi

if [ ! -d .gh-pages ]; then
    echo "Checking out gh-pages in .gh-pages"
    git clone -b gh-pages git@git.maxmind.com:GeoIP2-java .gh-pages
    cd .gh-pages
else
    echo "Updating .gh-pages"
    cd .gh-pages
    git pull
fi

if [ -n "$(git status --porcelain)" ]; then
    echo ".gh-pages is not clean" >&2
    exit 1
fi

cd ..

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

cat README.md >> $PAGE

# could be combined with the primary build
mvn release:clean
mvn release:prepare
mvn release:perform
rm -fr ".gh-pages/doc/$TAG"
cp -r target/apidocs .gh-pages/doc/$TAG

cd .gh-pages

git add doc/
git commit -m "Updated for $TAG" -a

read -e -p "Push to origin? " SHOULD_PUSH

if [ "$SHOULD_PUSH" != "y" ]; then
    echo "Aborting"
    exit 1
fi

git push

cd ..

git push
git push --tags

echo "Remember to do the release on https://oss.sonatype.org/!"
