logLevel := sbt.Level.Warn

addSbtPlugin("com.geirsson"    % "sbt-ci-release"  % "1.5.7")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.10")
addSbtPlugin("io.kevinlee"     % "sbt-docusaur"    % "0.6.0")

val sbtDevOops = "2.10.0"
addSbtPlugin("io.kevinlee" % "sbt-devoops-scala"     % sbtDevOops)
addSbtPlugin("io.kevinlee" % "sbt-devoops-sbt-extra" % sbtDevOops)
addSbtPlugin("io.kevinlee" % "sbt-devoops-github"    % sbtDevOops)
