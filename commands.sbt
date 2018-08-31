// release
addCommandAlias("fullRelease", ";clean;publishSigned;sonatypeRelease")

// testing
addCommandAlias("rddTest", ";project sparkRdd;clean;compile;test:compile;test")
addCommandAlias("dsTest", ";project sparkDs;clean;compile;test:compile;test")
addCommandAlias("streamingTest", ";project sparkStreaming;clean;compile;test:compile;test")
