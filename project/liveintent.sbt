resolvers ++= Seq(
  "Era7 maven releases" at "https://s3-eu-west-1.amazonaws.com/releases.era7.com",
  Resolver.url("bintray-kipsigman-sbt-plugins", url("https://dl.bintray.com/kipsigman/sbt-plugins"))(Resolver.ivyStylePatterns),
  Resolver.url("LiveIntent Releases", url("https://build.idtargeting.com/nexus/content/repositories/releases"))(Resolver.ivyStylePatterns),
  Resolver.url("LiveIntent Snapshots", url("https://build.idtargeting.com/nexus/content/repositories/snapshots"))(Resolver.ivyStylePatterns)
)

addSbtPlugin("com.liveintent" % "li-sbt-plugins" % "1.17.1")
