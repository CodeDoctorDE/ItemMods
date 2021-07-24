---
title: API
---

The latest build can you find [here](https://ci.codemc.io/job/CodeDoctorDE/job/ItemMods/lastStableBuild/)

* You can use maven to get the dependency or can use the jars in the github actions.
   * For maven please use this: 
   ```xml
  <project>
    <repositories>
        <repository>
            <id>codemc-repo</id>
            <url>https://repo.codemc.org/repository/maven-public/</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>com.github.codedoctorde</groupId>
            <artifactId>ItemMods</artifactId>
            <version>2.0.0-alpha.0</version>
        </dependency>
    </dependencies>
  </project>
   ```
* The documentation about the api can you find [here](https://codedoctor.tk/ItemMods/apidocs). Please use the classes in the api package. The ItemModsApi instance can you get with `ItemMods.getPlugin().getApi()`
* Custom events:
   * CustomBlockPlaceEvent
   * CustomBlockBreakEvent