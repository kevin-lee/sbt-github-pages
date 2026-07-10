#!/bin/bash -e

set -x

# Build + test for the given Scala version / sbt series.
#
# Scala 2.12 -> sbt 1.x, Scala 3 -> sbt 2.x (see `pluginCrossBuild / sbtVersion` in build.sbt).
# `+` is never used because the two axes need different JDKs: the Scala 3.8.4 compiler is
# compiled for Java 17 and will not run on Java 11.
#
# Usage: sbt-build.sh <scala-version> <sbt-series: sbt1|sbt2>

if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Usage: sbt-build.sh <scala-version> <sbt-series: sbt1|sbt2>"
  exit 1
fi

scala_version="$1"
sbt_series="$2"

echo "============================================"
echo "Build + test: Scala ${scala_version} (${sbt_series})"
echo "--------------------------------------------"
java -version

CURRENT_BRANCH_NAME="${GITHUB_REF#refs/heads/}"
if [[ "$CURRENT_BRANCH_NAME" == "main" || "$CURRENT_BRANCH_NAME" == "release" ]]
then
  sbt \
    -J-XX:MaxMetaspaceSize=1024m \
    -J-Xmx2048m \
    "++${scala_version}" \
    clean \
    Test/compile \
    test \
    packagedArtifacts
else
  sbt \
    -J-XX:MaxMetaspaceSize=1024m \
    -J-Xmx2048m \
    "++${scala_version}" \
    clean \
    Test/compile \
    test \
    package
fi

echo "============================================"
echo "Build + test: Done"
echo "============================================"
