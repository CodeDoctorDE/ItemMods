---
title: "API"
slug: "/api"
sidebar_position: 0
sidebar_label: "Zuhause"
---

Die neueste Version findest du [hier](https://ci.codemc.io/job/CodeDoctorDE/job/ItemMods/lastStableBuild/)

* Du kannst maven benutzen, um die Abhängigkeit zu erhalten, oder die Gläser in den github Aktionen verwenden.
    * Für Mappen, benutze es bitte:
   ```xml
  <project>
    <repositories>
        <repository>
            <id>codemc-repo</id>
            <url>https://repo. odemc. rg/repository/maven-public/</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>dev. inwood</groupId>
            <artifactId>Artikel Mods</artifactId>
            <version>2. .0-Alpha.</version>
        </dependency>
    </dependencies>
  </project>
   ```
* Die Dokumentation über die api findest du [hier](https://itemmods.linwood.dev/apidocs). Bitte verwenden Sie die Klassen im api Paket.
* Die ItemModsApi Instanz kann mit `ItemMods.getPlugin()` geladen werden
* Eigene Ereignisse:
    * CustomBlockPlaceEvent
    * CustomblockBreakEvent
