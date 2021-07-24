---
title: Preset
---

The preset directory is the preset for the resource pack. 
Here you can configure the `pack.mcmeta` and all models/textures and everything else.

## Create the preset

You need this folder structure to have a working resource pack.
If you have an item with the fallback texture diamond, you need to have a `diamond.json`.

```markdown
* assets
    * minecraft
        * models
            * item
                * diamond.json
* pack.mcmeta
```

The `pack.mcmeta` needs to have this content:

```json title="pack.mcmeta"
{
  "pack": {
    "description": "The name of the resource pack",
    "pack_format": 7
  }
}
```

The `diamond.json` needs to have the same content as the default texture of minecraft.

To get this done, you need an archive reader like 7-zip or WinRAR.

Then you need to locate to your minecraft client jar which is normally located in `%appdata%/.minecraft/versions/VERSION/VERSION.jar` 
(You need to replace the VERSION with the version which you used to connect to the server).

In the jar archive you will find a `assets` directory. There you need to copy the same file. If you have the diamond fallback texture 
you need to copy the file in `assets/minecraft/models/item/diamond.json`.

The model file should be similar to this:
```json title="assets/minecraft/models/item/diamond.json"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/diamond"
  }
}
```

If you have a block fallback texture like a grass block, the model file is in the block subdirectory, 
for example `assets/minecraft/models/blck/grass_block.json`.
