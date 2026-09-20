logLevel := sbt.Level.Warn

addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.12.1")

addSbtPlugin("com.eed3si9n" % "sbt-salad-days" % "0.2.0")

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "3.6.2")

val sbtDevOops     = "3.9.0"
addSbtPlugin("io.kevinlee" % "sbt-devoops-scala"     % sbtDevOops)
addSbtPlugin("io.kevinlee" % "sbt-devoops-sbt-extra" % sbtDevOops)
addSbtPlugin("io.kevinlee" % "sbt-devoops-github"    % sbtDevOops)
addSbtPlugin("io.kevinlee" % "sbt-devoops-starter"   % sbtDevOops)
