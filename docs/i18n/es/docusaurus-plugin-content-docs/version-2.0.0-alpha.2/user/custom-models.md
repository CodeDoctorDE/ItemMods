---
title: Modelos personalizados
---

:::note To have custom models, you need to have a pack.

Click [here](pack.md#create-a-pack) to see how you can create one.

:::

## Crear un modelo personalizado

* Ir a la lista de modelos en el gui del pack
* Haga clic en el libro de conocimiento
* Dale un nombre
* Ir al gui de datos
* Cambia la variación predeterminada o añade tu propia variación a la textura haciendo clic en el libro de conocimiento
* Puede elegir dónde se encuentra el archivo del modelo. Archivo o internet:
    * Si elige el archivo, necesita añadir el archivo de textura en plugins/ItemMods/temp
        * Ahora necesita introducir el nombre del archivo, por ejemplo `ruby.json`
    * Si eliges internet, necesitas el enlace directo a json
        * Si lo eliges, por favor añade `.json` a la url, por ejemplo `https://example.com/YOURFILE.json`

:::caution You need to export the resource pack before having a custom model.
:::

## Ejemplos

Create a file with this content in the temp directory. Replace the *\<placeholder\>* with your values and assign it to a model

### Bloquear modelo

The default block model:

```json title="block.json"
{
    "textures": {
        "0": "<your block texture>",
        "particle": "<your block texture>"
    },
    "elements": [
        {
            "from": [0, 0, 0],
            "a": [16, 16, 16],
            "caras": {
                "norte": {"uv": [0, 0, 16, 16], "textura": "#0"},
                "este": {"uv": [0, 0, 16, 16], "texture": "#0"},
                "sur": {"uv": [0, 0, 16, 16], "texture": "#0"},
                "oeste": {"uv": [0, 0, 16, 16], "textura": "#0"},
                "arriba": {"uv": [0, 0, 16, 16], "texture": "#0"},
                "abajo": {"uv": [0, 0, 16, 16], "texture": "#0"}
            }
        }
    ],
    "display": {
        "thirdperson_righthand": {
            "translation": [-7, 5, 3],
            "rotación": [45. , 5, -8.5],
            "escala": [3.9, 3.9, 3. ]
        },
        "ground": {
            "scale": [0. , 0.3, 0. ]
        },
        "gui": {
            "rotation": [45, 45, 0],
            "escala": [0. 5, 0.65, 0. 5]
        },
        "head": {
            "translation": [0, -30. 5, 0],
            "escala": [4, 4, 4, 4, 4]
        }
    }
}

```

### Bloquear modelo de artículo

```json title="block_item.json"
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "<your block texture>"
  }
}
```
