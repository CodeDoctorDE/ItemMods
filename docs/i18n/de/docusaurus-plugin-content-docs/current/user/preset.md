---
title: Preset
---

Das Preset-Verzeichnis ist die Voreinstellung für das Resource Pack. Hier können Sie die `pack.mcmeta` und alle Modelle/Texturen und alles andere konfigurieren.

## Vorlage erstellen

Sie benötigen diese Ordnerstruktur, um ein funktionierendes Ressourcenpaket zu haben. Wenn du einen Gegenstand mit dem Fallback-Textur-Diamanten hast, musst du einen `diamond.json` haben.

```markdown
<unk> 本<unk> pack.mcmeta
<unk> 本<unk> assets
    <unk> 本<unk> minecraft 
        <unk> 本<unk> Modelle
            <unk> <unk> <unk> <unk> <unk> <unk> <unk> item
                <unk> <unk> diamond.json
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

Dann musst du zu deinem Minecraft-Client-Jar finden, das normalerweise in `%appdata%/.minecraft/versions/VERSION/VERSION liegt. ar` (Sie müssen die VERSION durch die Version ersetzen, mit der Sie die Verbindung zum Server hergestellt haben).

Im jar Archiv finden Sie ein `Assets` Verzeichnis. Dort müssen Sie die gleiche Datei kopieren. Wenn Sie die Diamant-Fallback-Textur haben, müssen Sie die Datei in `assets/minecraft/models/item/diamond.json` kopieren.

Die Modelldatei sollte folgendermaßen sein:
```json title="assets/minecraft/models/item/diamond.json"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/diamond"
  }
}
```

Wenn Sie eine Blöcke Fallback-Textur wie einen Grass-Block haben, befindet sich die Modelldatei im Blockunterverzeichnis, zum Beispiel `assets/minecraft/models/block/grass_block. Sohn`.
