---
title: "API"
slug: "/api"
sidebar_label: "首页"
sidebar_position: 0
---

最新版本可以在这里找到 [](https://ci.codemc.io/job/CodeDoctorDE/job/ItemMods/lastStableBuild/)

* 您可以使用 maven 获得依赖关系或者可以在 github 动作中使用 jars 。
    * 对于maven，请使用：
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
            <version>2。 0-字母。</version>
        </dependency>
    </dependencies>
  </project>
   ```
* 关于api的文档可以在这里找到 [](https://itemmods.linwood.dev/apidocs)。 请在 api 软件包中使用类.
* ItemModsApi实例可以使用 `ItemMods.getPlugin()`
* 自定义事件：
    * 自定义 BlockPlaceEvent
    * 自定义BlockBreak事件
