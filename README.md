# intellij.agent
Disable plugin compatibility checks and make IDEA support run on OpenJDK 16.

```
IntelliJ IDEA 2021.1 EAP (Community Edition)
Build #IC-211.4961.33, built on January 28, 2021
```

Attach to the idea as a javaagent, using an absolute path.

Please download and unzip from release.

Add the following JVM parameters:
`-javaagent:<dir>/intellij.agent-0.0.1.jar`
