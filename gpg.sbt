ThisBuild / credentials += Credentials(
  "GnuPG Key ID",
  "gpg",
  "CEE7CE443552E579208DCFD59385CE83F6DA105C", // key identifier
  "ignored" // this field is ignored; passwords are supplied by pinentry
)