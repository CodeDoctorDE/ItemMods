---
title: Preset
---

Das Preset-Verzeichnis ist die Voreinstellung für das Resource Pack. Hier können Sie die `pack.mcmeta` und alle Modelle/Texturen und alles andere konfigurieren.

## Vorlage erstellen

Sie benötigen diese Ordnerstruktur, um ein funktionierendes Ressourcenpaket zu haben. If you have an item with the fallback texture diamond, you need to have a `diamond.json`.

```markdown
<unk> 本<unk> pack.mcmeta <unk> 本<unk> Assets <unk> ร<unk> minecraft <unk> ร<unk> Modelle <unk> 本<unk> Item <unk> <unk> <unk> diamond.json
```

Die `pack.mcmeta` muss diesen Inhalt haben:

```json title="pack.mcmeta"
{
  "pack": {
    "description": "Der Name des Resource Pack",
    "pack_format": 7
  }
}
```

Die `diamond.json` muss den gleichen Inhalt haben wie die Standard-Textur von Minecraft.

Dazu benötigen Sie einen Archivleser wie 7-zip oder WinRAR.

Dann musst du zu deinem Minecraft Client jar finden, das normalerweise in `%appdata%/.minecraft/versions/VERSION/VERSION. ar` (Sie müssen die VERSION durch die Version ersetzen, mit der Sie die Verbindung zum Server hergestellt haben).

Im jar Archiv finden Sie ein `Assets` Verzeichnis. Dort müssen Sie die gleiche Datei kopieren. Wenn Sie die Diamant- Fallback-Textur haben, müssen Sie die Datei in `assets/minecraft/models/item/diamond.json` kopieren.

Die Modelldatei sollte folgendermaßen sein:

```json title="assets/minecraft/models/item/diamond.json"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/diamond"
  }
}
```

If you have a block fallback texture like a grass block, the model file is in the block subdirectory, for example `assets/minecraft/models/block/grass_block.json`.
