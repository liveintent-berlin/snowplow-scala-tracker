resolvers ++= Seq(
  Resolver.url("LiveIntent Releases", url("https://build.idtargeting.com/nexus/content/repositories/releases"))(Resolver.ivyStylePatterns),
  Resolver.url("LiveIntent Snapshots", url("https://build.idtargeting.com/nexus/content/repositories/snapshots"))(Resolver.ivyStylePatterns)
)

addSbtPlugin("com.liveintent" % "li-sbt-plugins" % "1.10.0")
