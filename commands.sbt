// release
addCommandAlias("fullRelease", ";clean;publishSigned;sonatypeRelease")

// testing
addCommandAlias("rddTest", ";project sparkRdd;clean;compile;test:compile;test")
addCommandAlias("dsTest", ";project sparkDs;clean;compile;test:compile;test")
addCommandAlias("structuredTest", ";project sparkStructuredStreaming;clean;compile;test:compile;test")
addCommandAlias("streamingTest", ";project sparkStreaming;clean;compile;test:compile;test")

// publish
//addCommandAlias("rddPublish", ";project sparkRdd; fullRelease")
//addCommandAlias("dsPublish", ";project sparkDs; fullRelease")
//addCommandAlias("structuredPublish", ";project sparkStructuredStreaming; fullRelease")
//addCommandAlias("streamingPublish", ";project sparkStreaming; fullRelease")
