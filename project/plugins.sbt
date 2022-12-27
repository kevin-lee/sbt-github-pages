logLevel := sbt.Level.Warn

addSbtPlugin("com.github.sbt"  % "sbt-ci-release"  % "1.5.10")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "3.0.6")
addSbtPlugin("io.kevinlee"     % "sbt-docusaur"    % "0.13.0")

val sbtDevOops = "2.24.0"
addSbtPlugin("io.kevinlee" % "sbt-devoops-scala"     % sbtDevOops)
addSbtPlugin("io.kevinlee" % "sbt-devoops-sbt-extra" % sbtDevOops)
addSbtPlugin("io.kevinlee" % "sbt-devoops-github"    % sbtDevOops)
