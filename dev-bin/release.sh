#!/bin/bash

TAG=$1
SKIP_MAVEN=$2

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
mvn javadoc:javadoc
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
if [ -z $TAG ]; then
    mvn release:clean
    mvn release:prepare
    mvn release:perform
fi
git push
git push --tags
