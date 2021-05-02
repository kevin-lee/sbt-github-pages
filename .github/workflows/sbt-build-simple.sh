#!/bin/bash -e

set -x

echo "============================================"
echo "Build projects (Simple)"
echo "--------------------------------------------"
echo ""
CURRENT_BRANCH_NAME="${GITHUB_REF#refs/heads/}"
if [[ "$CURRENT_BRANCH_NAME" == "main" || "$CURRENT_BRANCH_NAME" == "release" ]]
then
  sbt \
    -J-XX:MaxMetaspaceSize=1024m \
    -J-Xmx2048m \
    clean \
    test \
    packagedArtifacts
else
  sbt \
    -J-XX:MaxMetaspaceSize=1024m \
    -J-Xmx2048m \
    clean \
    test \
    package
fi

echo "============================================"
echo "Building projects (Simple): Done"
echo "============================================"
