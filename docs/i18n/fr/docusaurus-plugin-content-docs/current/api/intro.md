---
title: "API"
slug: "/api"
sidebar_label: "Home"
sidebar_position: 0
---

La dernière version peut vous permettre de trouver [ici](https://ci.codemc.io/job/CodeDoctorDE/job/ItemMods/lastStableBuild/)

* Vous pouvez utiliser maven pour obtenir la dépendance ou utiliser les jars dans les actions github.
    * Pour maven, veuillez utiliser ceci :
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
            <version>2.0.0-alpha.2</version>
        </dependency>
    </dependencies>
  </project>
   ```
* La documentation sur l'api peut vous trouver [ici](https://itemmods.linwood.dev/apidocs). Veuillez utiliser les classes dans le paquet api.
* L'instance ItemModsApi peut vous obtenir avec `ItemMods.getPlugin()`
* Événements personnalisés :
    * format@@0 CustomBlockPlaceEvent
    * format@@0 CustomBlockBreakEvent
