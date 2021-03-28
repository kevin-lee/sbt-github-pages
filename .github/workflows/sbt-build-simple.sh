#!/bin/bash -e

set -x

if [ "$#" -ne 2 ]
  then
    echo "Scala version is missing. Please enter the Scala version."
    echo "sbt-build-simple.sh 2.12.12 1.3.13"
    exit 1
else
  SCALA_VERSION=$1
  SBT_VERSION=$2
  echo "============================================"
  echo "Build projects (Simple)"
  echo "--------------------------------------------"
  echo ""
  CURRENT_BRANCH_NAME="${GITHUB_REF#refs/heads/}"
  if [[ "$CURRENT_BRANCH_NAME" == "main" || "$CURRENT_BRANCH_NAME" == "release" ]]
  then
    sbt -J-Xmx2048m \
      ++${SCALA_VERSION}! \
      ^^${SBT_VERSION} \
      clean \
      test \
      packagedArtifacts
  else
    sbt -J-Xmx2048m \
      ++${SCALA_VERSION}! \
      ^^${SBT_VERSION} \
      clean \
      test \
      package
  fi

  echo "============================================"
  echo "Building projects (Simple): Done"
  echo "============================================"
fi
