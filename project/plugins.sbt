logLevel := sbt.Level.Warn

addSbtPlugin("com.github.sbt"  % "sbt-ci-release"  % "1.11.2")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "3.4.1")
addSbtPlugin("io.kevinlee"     % "sbt-docusaur"    % "0.18.0")

val sbtDevOops = "3.3.1"
addSbtPlugin("io.kevinlee" % "sbt-devoops-scala"     % sbtDevOops)
addSbtPlugin("io.kevinlee" % "sbt-devoops-sbt-extra" % sbtDevOops)
addSbtPlugin("io.kevinlee" % "sbt-devoops-github"    % sbtDevOops)
addSbtPlugin("io.kevinlee" % "sbt-devoops-starter"   % sbtDevOops)
