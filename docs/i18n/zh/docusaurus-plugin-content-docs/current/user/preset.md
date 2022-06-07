---
title: Preset
---

预设目录是资源包的预设。 您可以在这里配置 `pack.mcmeta` 和所有 模型/纹理及其他一切。

## 创建预设

您需要此文件夹结构才能有一个工作资源包。 如果你有一个带有后退纹理钻石的物品， 你需要一个 `钻石.json`

```markdown
├── pack.mcmeta └── assets └── minecraft └── models └── item └── diamond.json
```

`pack.mcmeta` 需要有这个内容：

```json title="pack.mcmeta"
{
  "pack": {
    "description": "The name of the resource pack",
    "pack_format": 7
  }
}
```

`diamid.json` 需要有与我的默认纹理相同的内容。

要完成这个操作，您需要像7-zip或 WinRAR 这样的归档阅读器。

Then you need to locate to your minecraft client jar which is normally located in `%appdata%/.minecraft/versions/VERSION/VERSION.jar` (You need to replace the VERSION with the version which you used to connect to the server).

在 jar 归档中，您将找到一个 `assets` 目录。 您需要复制相同的文件。 如果你有钻石 回退纹理，你需要将文件复制到 `assets/minecraft/models/item/diamond.json` 中。

模型文件应该类似于：

```json title="assets/minecraft/models/item/diamond.json"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/diamond"
  }
}
```

如果你有一个块回退纹理像草块，模型文件就在块子目录中，例如 `assets/minecraft/models/block/gras_block。 儿子`
