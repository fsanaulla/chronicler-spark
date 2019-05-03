ghreleaseRepoOrg := "fsanaulla"
ghreleaseRepoName := "chronicler-spark"

ghreleaseAssets := Seq.empty

ghreleaseNotes := { tagName =>
  val version = tagName.stripPrefix("v")
  IO.read(baseDirectory.value / "changelog" / s"$version.md")
}