logLevel := sbt.Level.Warn

addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.6")

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.8")

addSbtPlugin("io.kevinlee" % "sbt-devoops" % "1.0.3")

// TODO: Remove it once sbt-docusaur is available in sbt bintray.
resolvers +=
  Resolver.url(
    "Kevin's sbt Plugins",
    new URL("https://dl.bintray.com/kevinlee/sbt-plugins")
  )(Resolver.ivyStylePatterns)

addSbtPlugin("io.kevinlee" % "sbt-docusaur" % "0.1.0")
