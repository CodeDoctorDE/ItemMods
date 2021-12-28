---
title: Preset
---

El directorio predefinido es el preset para el paquete de recursos. Aquí puedes configurar el `pack.mcmeta` y todos los modelos/texturas y todo lo demás.

## Crear el preset

Necesita esta estructura de carpetas para tener un paquete de recursos de trabajo. Si tienes un elemento con el diamante de la textura de recurso, necesitas tener un `diamond.json`.

```markdown
★ pack.mcmeta, assets → → → minecraft, modelos, y el elemento → tv diamond.json
```

El `pack.mcmeta` necesita tener este contenido:

```json title="pack.mcmeta"
{
  "pack": {
    "description": "The name of the resource pack",
    "pack_format": 7
  }
}
```

El `diamond.json` necesita tener el mismo contenido que la textura predeterminada de minecraft.

Para hacerlo, necesita un lector de archivos como 7-zip o WinRAR.

Luego tienes que localizar en tu jarra de cliente de minecraft que normalmente se encuentra en `%appdata%/.minecraft/versions/VERSION/VERSION. ar` (Necesitas reemplazar la VERSION por la versión que usaste para conectar al servidor).

En el archivo jar encontrarás un directorio de `activos`. Ahí necesita copiar el mismo archivo. Si tienes la textura de reserva de diamante necesitas copiar el archivo en `assets/minecraft/models/item/diamond.json`.

El archivo del modelo debe ser similar a esto:

```json title="assets/minecraft/models/item/diamond.json"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/diamond"
  }
}
```

If you have a block fallback texture like a grass block, the model file is in the block subdirectory, for example `assets/minecraft/models/block/grass_block.json`.
