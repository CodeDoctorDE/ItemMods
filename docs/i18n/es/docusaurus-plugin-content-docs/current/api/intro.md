---
title: "API"
slug: "/api"
sidebar_label: "Inicio"
sidebar_position: 0
---

La última versión puede encontrar [aquí](https://ci.codemc.io/job/CodeDoctorDE/job/ItemMods/lastStableBuild/)

* Puede usar el laberinto para obtener la dependencia o puede usar los tarros en las acciones de github.
    * Para maven por favor use esto:
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
            <artifactId>ItemMods</artifactId>
            <version>2. .0-alfa.</version>
        </dependency>
    </dependencies>
  </project>
   ```
* La documentación sobre la api puede encontrar [aquí](https://itemmods.linwood.dev/apidocs). Por favor, utilice las clases en el paquete api.
* La instancia de ItemModsApi puede obtener con `ItemMods.getPlugin()`
* Eventos personalizados:
    * Evento de Lugar personalizado
    * Evento de interrupción de bloques
