---
title: Eigene Modelle
---

:::note Um benutzerdefinierte Texturen zu haben, musst du ein Paket haben. Siehe [hier](pack.md#create-a-pack) um zu sehen, wie Sie erstellen können. :::

## Ein individuelles Modell erstellen

* Gehe zur Modellliste im Paketgui
* Klicken Sie auf das Wissensbuch
* Gib ihm einen Namen
* Gehe zum Datengui
* Ändern Sie die Standardvariation oder fügen Sie Ihre eigene Variation zur Textur hinzu, indem Sie auf das Wissensbuch klicken
* Sie können wählen, wo sich die Modelldatei befindet. Datei oder Internet:
    * Wenn Sie eine Datei wählen, müssen Sie die Texturdatei in Plugins/ItemMods/temp hinzufügen
        * Jetzt müssen Sie den Dateinamen eingeben, zum Beispiel `ruby.json`
    * Wenn du Internet wählst, benötigst du den direkten Link zum json
        * Wenn Sie es wählen, fügen Sie bitte `.json` der URL hinzu, zum Beispiel `https://example.com/YOURFILE.json`

:::caution Du musst das Ressourcenpaket exportieren, bevor du ein benutzerdefiniertes Modell hast. :::

## Beispiele

Erstellen Sie eine Datei mit diesem Inhalt im temporären Verzeichnis. Ersetzen Sie das *\<placeholder\>* mit Ihren Werten und weisen Sie es einem Modell zu.

### Blockmodell

Das Standard-Blockmodell:

```json title="block.json"
{
    "textures": {
        "0": "<your block texture>",
        "Teilchen": "<your block texture>"
    },
    "Elemente": [
        {
            "von": [0, 0, 0],
            "bis": [16, 16, 16],
            "faces": {
                "Nord": {"uv": [0, 0, 16, 16], "texture": "#0"},
                "Ost": {"uv": [0, 0, 16, 16], "texture": "#0"},
                "Süden": {"uv": [0, 0, 16, 16], "texture": "#0"},
                "West": {"uv": [0, 16], "texture": 0, 16, 16], "texture": "#0"},
                "up": {"uv": [0, 0, 0, 16, 16], "texture": "#0"},
                "down": {"uv": [0, 0, 16, 16], "texture": "#0"}
            }
        }
    ],
    "display": {
        "thirdperson_righthand": {
            "translation": [-7, 5, 3],
            "Rotation": [45. , 5, -8.5],
            "scale": [3.9, 3.9, 3. ]
        },
        "ground": {
            "scale": [0. , 0.3, 0. ]
        },
        "gui": {
            "rotation": [45, 45, 0],
            "scale": [0. 5, 0.65, 0. 5]
        },
        "head": {
            "translation": [0, -30. 5, 0],
            "Skalieren": [4, 4, 4 4]
        }
    }
}

```

### Block-Artikelmodell

```json title="block_item.json"
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "<your block texture>"
  }
}
```
