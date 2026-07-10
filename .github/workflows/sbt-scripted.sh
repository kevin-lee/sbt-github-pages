#!/bin/bash -e

set -x

# Run the scripted test for the given Scala version / sbt series.
#
# `scriptedSbt` is `(pluginCrossBuild / sbtVersion).value`, so the Scala 2.12 axis launches
# sbt 1.x and the Scala 3 axis launches sbt 2.x. Each axis names its own scripted test
# explicitly rather than running bare `scripted`, so an axis never tries to boot the other
# series' sbt.
#
# Usage: sbt-scripted.sh <scala-version> <sbt-series: sbt1|sbt2>

if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Usage: sbt-scripted.sh <scala-version> <sbt-series: sbt1|sbt2>"
  exit 1
fi

scala_version="$1"
sbt_series="$2"

if [ "$sbt_series" == "sbt2" ]; then
  scripted_test="sbt-github-pages/sbt-2-defaults"
elif [ "$sbt_series" == "sbt1" ]; then
  scripted_test="sbt-github-pages/sbt-1-defaults"
else
  echo "Unknown sbt series: ${sbt_series}. It must be either sbt1 or sbt2."
  exit 1
fi

echo "============================================"
echo "Scripted test: Scala ${scala_version} (${sbt_series}) - ${scripted_test}"
echo "--------------------------------------------"
java -version

sbt \
  -J-XX:MaxMetaspaceSize=1024m \
  -J-Xmx2048m \
  "++${scala_version}" \
  clean \
  "scripted ${scripted_test}"

echo "============================================"
echo "Scripted test: Done"
echo "============================================"
