---
title: "API"
slug: "/api"
sidebar_label: "Home"
sidebar_position: 0
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
            <groupId>dev.linwood</groupId>
            <artifactId>ItemMods</artifactId>
            <version>2.0.0-alpha.1</version>
        </dependency>
    </dependencies>
  </project>
   ```
* The documentation about the api can you find [here](https://itemmods.linwood.dev/apidocs). Please use the classes in
  the api package.
* The ItemModsApi instance can you get with `ItemMods.getPlugin()`
* Custom events:
    * CustomBlockPlaceEvent
    * CustomBlockBreakEvent
