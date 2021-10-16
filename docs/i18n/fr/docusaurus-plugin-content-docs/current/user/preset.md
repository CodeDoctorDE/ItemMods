---
title: Preset
---

Le répertoire prédéfini est le préréglage pour le pack de ressources. Ici, vous pouvez configurer les `pack.mcmeta` et tous les modèles/textures et tout le reste.

## Créer le préréglage

Vous avez besoin de cette structure de dossier pour avoir un pack de ressources de travail. Si vous avez un élément avec la texture de repli diamant, vous devez avoir un `diamond.json`.

```markdown
── pack.mcmeta <unk> ─ assets <unk> ─ minecraft <unk> ── models <unk> ── item <unk> ── diamond.json
```

Le `pack.mcmeta` doit avoir ce contenu:

```json title="pack.mcmeta"
{
  "pack": {
    "description": "Le nom du pack de ressources",
    "pack_format": 7
  }
 } }
```

Le `diamond.json` doit avoir le même contenu que la texture par défaut du minecraft.

Pour cela, vous avez besoin d'un lecteur d'archive comme 7-zip ou WinRAR.

Ensuite, vous devez localiser dans votre jar client minecraft qui est normalement situé dans `%appdata%/.minecraft/versions/VERSION/VERSION. ar` (Vous devez remplacer la VERSION par la version que vous avez utilisée pour vous connecter au serveur).

Dans l'archive jar vous trouverez un répertoire `assets`. Là vous devez copier le même fichier. Si vous avez la texture de repli du diamant vous devez copier le fichier dans `assets/minecraft/models/item/diamond.json`.

Le fichier modèle devrait être similaire à ceci :

```json title="assets/minecraft/models/item/diamond.json"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/diamond"
  }
}
```

If you have a block fallback texture like a grass block, the model file is in the block subdirectory, for example `assets/minecraft/models/block/grass_block.json`.
