import wartremover.{Wart, Warts}

object ProjectInfo {

  val ProjectVersion: String = "0.1.0"

  val commonScalacOptions: Seq[String] = Seq(
      "-deprecation"
    , "-unchecked"
    , "-feature"
    , "-Ywarn-value-discard"
    , "-Yno-adapted-args"
    , "-Xlint"
    , "-Xfatal-warnings"
    , "-Ywarn-dead-code"
    , "-Ywarn-inaccessible"
    , "-Ywarn-nullary-unit"
    , "-Ywarn-nullary-override"
    , "-language:higherKinds"
    , "-encoding", "UTF-8"
  )

  val commonWarts: Seq[Wart] =
    Warts.allBut(
        Wart.DefaultArguments
      , Wart.Overloading
      , Wart.Any
      , Wart.Nothing
      , Wart.NonUnitStatements
    )

}
