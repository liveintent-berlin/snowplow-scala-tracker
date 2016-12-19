resolvers ++= Seq(
  "Era7 maven releases" at "https://s3-eu-west-1.amazonaws.com/releases.era7.com",
  Resolver.url("bintray-kipsigman-sbt-plugins", url("http://dl.bintray.com/kipsigman/sbt-plugins"))(Resolver.ivyStylePatterns),
  Resolver.url("LiveIntent Releases", url("https://s3.amazonaws.com/li-berlin-releases"))(Resolver.ivyStylePatterns),
  Resolver.url("LiveIntent Snapshots", url("https://s3.amazonaws.com/li-berlin-snapshots"))(Resolver.ivyStylePatterns)
)

addSbtPlugin("com.liveintent" % "li-sbt-plugins" % "1.4.3")
