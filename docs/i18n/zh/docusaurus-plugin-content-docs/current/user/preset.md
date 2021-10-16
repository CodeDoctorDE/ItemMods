---
title: Preset
---

预设目录是资源包的预设。 您可以在这里配置 `pack.mcmeta` 和所有模型/纹理及其他一切。

## 创建预设

您需要此文件夹结构才能有一个工作资源包。 如果你有一个带有后退纹理钻石的物品，你需要有一个 `钻石.json`。

```markdown
├── pack.mcmeta
└── assets
    └── minecraft 
        └── models
            └── item
                └── diamond.json
```

`pack.mcmeta` 需要有这个内容：

```json title="pack.mcmeta"
主席:
  "pack":
    "description": "资源包的名称",
    "pack_格式": 7
  }
}
```

`diamid.json` 需要有与我的默认纹理相同的内容。

要完成这个操作，您需要像7-zip或 WinRAR 这样的归档阅读器。

然后你需要找到通常位于 `%appdata%/.minecraft/versions/VERSION/VERSION的Minecraft客户端jar。 ar` (您需要将VERSION替换为您用来连接到服务器的版本)。

在 jar 归档中，您将找到一个 `assets` 目录。 您需要复制相同的文件。 如果你有钻石后退纹理 ，你需要将文件复制到 `assets/minecraft/models/item/diamond.json` 中。

模型文件应该类似于：
```json title="assets/minecraft/models/item/diamond.json"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/diamond"
  }
}
```

如果你有块后退纹理像草块，模型文件就在块子目录， 例如 `assets/minecraft/models/block/grass_block。 儿子`
