// release
addCommandAlias("fullRelease", ";clean;publishSigned;sonatypeRelease")

// testing
addCommandAlias("rddTest", ";project sparkRdd;clean;compile;it:compile;it:test")
addCommandAlias("dsTest", ";project sparkDs;clean;compile;it:compile;it:test")
addCommandAlias("streamingTest", ";project sparkStreaming;clean;compile;it:compile;it:test")
