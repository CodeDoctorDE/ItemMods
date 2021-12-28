---
title: "API"
slug: "/api"
sidebar_label: "Home"
sidebar_position: 0
---

Den nyeste versjonen kan du finne [her](https://ci.codemc.io/job/CodeDoctorDE/job/ItemMods/lastStableBuild/)

* Du kan bruke maven for å få avhengigheten eller bruke jarene i github handlingene.
    * For maven, vennligst bruk dette:
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
* Dokumentasjonen om api finner du [her](https://itemmods.linwood.dev/apidocs). Vennligst bruk timene i api pakken.
* En element-modsApi forekomst kan du få med `ItemMods.getPlugin()`
* Egendefinerte hendelser:
    * CustomBlockPlaceEvent
    * CustomBlockBreakEvent
