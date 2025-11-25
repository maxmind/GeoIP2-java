#!/bin/bash

set -eu -o pipefail

# Check that we're not on the main branch
current_branch=$(git branch --show-current)
if [ "$current_branch" = "main" ]; then
    echo "Error: Releases should not be done directly on the main branch."
    echo "Please create a release branch and run this script from there."
    exit 1
fi

# Fetch latest changes and check that we're not behind origin/main
echo "Fetching from origin..."
git fetch origin

if ! git merge-base --is-ancestor origin/main HEAD; then
    echo "Error: Current branch is behind origin/main."
    echo "Please merge or rebase with origin/main before releasing."
    exit 1
fi

changelog=$(cat CHANGELOG.md)

regex='
([0-9]+\.[0-9]+\.[0-9]+[a-zA-Z0-9\-]*) \(([0-9]{4}-[0-9]{2}-[0-9]{2})\)
-*

((.|
)*)
'

if [[ ! $changelog =~ $regex ]]; then
      echo "Could not find date line in change log!"
      exit 1
fi

version="${BASH_REMATCH[1]}"
date="${BASH_REMATCH[2]}"
notes="$(echo "${BASH_REMATCH[3]}" | sed -n -e '/^[0-9]\+\.[0-9]\+\.[0-9]\+/,$!p')"

if [[ "$date" !=  $(date +"%Y-%m-%d") ]]; then
    echo "$date is not today!"
    exit 1
fi

tag="v$version"

if [ -n "$(git status --porcelain)" ]; then
    echo ". is not clean." >&2
    exit 1
fi

if [ ! -d .gh-pages ]; then
    echo "Checking out gh-pages in .gh-pages"
    git clone -b gh-pages git@github.com:maxmind/GeoIP2-java.git .gh-pages
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

mvn versions:display-plugin-updates
mvn versions:display-dependency-updates

read -r -n 1 -p "Continue given above dependencies? (y/n) " should_continue

if [ "$should_continue" != "y" ]; then
    echo "Aborting"
    exit 1
fi

mvn test

read -r -n 1 -p "Continue given above tests? (y/n) " should_continue

if [ "$should_continue" != "y" ]; then
    echo "Aborting"
    exit 1
fi

page=.gh-pages/index.md
cat <<EOF > $page
---
layout: default
title: MaxMind GeoIP2 Java API
language: java
version: $tag
---

EOF

mvn versions:set -DnewVersion="$version"

perl -pi -e "s/(?<=<version>)[^<]*/$version/" README.md
perl -pi -e "s/(?<=com\.maxmind\.geoip2\:geoip2\:)\d+\.\d+\.\d+([\w\-]+)?/$version/" README.md

cat README.md >> $page

git diff

read -r -n 1 -p "Commit changes? " should_commit
if [ "$should_commit" != "y" ]; then
    echo "Aborting"
    exit 1
fi
git add README.md pom.xml
git commit -m "Preparing for $version"

mvn clean deploy

rm -fr ".gh-pages/doc/$tag"
cp -r target/reports/apidocs ".gh-pages/doc/$tag"
rm .gh-pages/doc/latest
ln -fs "$tag" .gh-pages/doc/latest

pushd .gh-pages

git add doc/
git commit -m "Updated for $tag" -a

echo "Release notes for $version:

$notes

"
read -r -n 1 -p "Push to origin? " should_push

if [ "$should_push" != "y" ]; then
    echo "Aborting"
    exit 1
fi

git push

popd

git push

gh release create --target "$(git branch --show-current)" -t "$version" -n "$notes" "$tag" \
    "target/geoip2-$version-with-dependencies.zip" \
    "target/geoip2-$version-with-dependencies.zip.asc"
