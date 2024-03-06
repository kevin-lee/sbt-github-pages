logLevel := sbt.Level.Warn

addSbtPlugin("com.github.sbt"  % "sbt-ci-release"  % "1.5.12")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "3.1.5")
addSbtPlugin("io.kevinlee"     % "sbt-docusaur"    % "0.16.0")

val sbtDevOops = "3.1.0"
addSbtPlugin("io.kevinlee" % "sbt-devoops-scala"     % sbtDevOops)
addSbtPlugin("io.kevinlee" % "sbt-devoops-sbt-extra" % sbtDevOops)
addSbtPlugin("io.kevinlee" % "sbt-devoops-github"    % sbtDevOops)
addSbtPlugin("io.kevinlee" % "sbt-devoops-starter"    % sbtDevOops)
