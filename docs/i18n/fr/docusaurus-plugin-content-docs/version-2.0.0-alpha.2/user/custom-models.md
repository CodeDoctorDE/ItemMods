---
title: Modèles personnalisés
---

:::note Pour avoir des modèles personnalisés, vous devez avoir un pack.

Click [here](pack#create-a-pack) to see how you can create one.

:::

## Créer un modèle personnalisé

* Aller à la liste des modèles dans l'interface du pack
* Cliquez sur le livre de connaissances
* Donnez-lui un nom
* Aller à l'interface des données
* Modifiez la variation par défaut ou ajoutez votre propre variation à la texture en cliquant sur le livre de connaissances
* Vous pouvez choisir où se trouve le fichier modèle. Fichier ou internet :
    * Si vous choisissez un fichier, vous devez ajouter le fichier de texture dans plugins/ItemMods/temp
        * Maintenant vous devez entrer le nom du fichier, par exemple `ruby.json`
    * Si vous choisissez Internet, vous avez besoin du lien direct vers le json
        * Si vous le choisissez, veuillez ajouter `.json` à l'url, par exemple `https://example.com/YOURFILE.json`

:::caution Vous devez exporter le pack de ressources avant d'avoir un modèle personnalisé.
:::

## Exemples

Créer un fichier avec ce contenu dans le répertoire temp. Replace the *\<placeholder\>* with your values and assign it to a model

### Modèle de bloc

Le modèle de bloc par défaut :

```json title="block.json"
{
    "textures": {
        "0": "<your block texture>",
        "particle": "<your block texture>"
    },
    "éléments": [
        {
            "from": [0, 0, 0],
            "à": [16, 16, 16],
            "faces": {
                "north": {"uv": [0, 0, 16, 16], "texture": "#0"},
                "est": {"uv": [0, 0, 16, 16], "texture": "#0"},
                "sud": {"uv": [0, 0, 16, 16], "texture": "#0"},
                "ouest": {"uv": [0, 0, 16, 16], "texture": "#0"},
                "up": {"uv": [0, 0, 16, 16], "texture": "#0"},
                "bas": {"uv": [0, 0, 16, 16], "texture": "#0"}
            }
        }
    ],
    "display": {
        "thirdperson_righthand": {
            "translation": [-7, 5, 3],
            "rotation": [45. , 5, -8.5],
            "échelle": [3.9, 3.9, 3.9, 3. ]
        },
        "ground": {
            "scale": [0. , 0.3, 0. ]
        },
        "gui": {
            "rotation": [45, 45, 0],
            "échelle": [0. 5, 0,65, 0. 5]
        },
        "tête": {
            "traduction": [0, -30. 5, 0],
            "échelle": [4, 4, 4, 4]
        }
    }
}

```

### Modèle d'élément de bloc

```json title="block_item.json"
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "<your block texture>"
  }
}
```
