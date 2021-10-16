---
title: Modelos personalizados
---

:::note Para tener texturas personalizadas, necesitas tener un paquete. Mira [aquí](pack.md#create-a-pack) para ver cómo puedes crear uno. :::

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

:::caution Necesitas exportar el paquete de recursos antes de tener un modelo personalizado. :::

## Ejemplos

Crear un archivo con este contenido en el directorio temporal. Reemplazar el *\<placeholder\>* por sus valores y asignarlo a un modelo

### Bloquear modelo

Modelo de bloque por defecto:

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
