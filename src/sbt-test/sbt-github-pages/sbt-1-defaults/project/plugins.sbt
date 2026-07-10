sys.props.get("plugin.version") match {
  case Some(pluginVersion) =>
    addSbtPlugin("io.kevinlee" % "sbt-github-pages" % pluginVersion)

  case None =>
    sys.error(
      """|The system property 'plugin.version' is not defined.
         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin
    )
}
