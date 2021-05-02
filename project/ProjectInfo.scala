import wartremover.{Wart, Warts}

object ProjectInfo {

  val ProjectVersion: String = "0.4.0"

  val commonWarts: Seq[Wart] =
    Warts.allBut(
        Wart.DefaultArguments
      , Wart.Overloading
      , Wart.Any
      , Wart.Nothing
      , Wart.NonUnitStatements
    )

}
