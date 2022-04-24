---
title: Egendefinerte modeller
---

:::note To have custom models, you need to have a pack.

Click [here](pack.md#create-a-pack) to see how you can create one.

:::

## Opprett en tilpasset modell

* Gå til modell-listen i pakken gui
* Klikk på kunnskapsboken
* Gi det et navn
* Gå til datalinje
* Endre standardvariasjonen eller legge til din egen variasjon i tekstur ved å klikke på kunnskapsboken
* Du kan velge hvor modellfilen ligger. Fil eller Internett:
    * Hvis du velger fil må du legge til tekstur i plugins/ItemMods/temp
        * Nå må du angi filnavnet, for eksempel `ruby.json`
    * Dersom du velger internett, trenger du direkte link til json
        * Hvis du velger det, legg til `.json` til nettadressen, for eksempel `https://example.com/YOURFILE.json`

:::caution You need to export the resource pack before having a custom model.
:::

## Eksempler

Create a file with this content in the temp directory. Replace the *\<placeholder\>* with your values and assign it to a model

### Blokk modell

The default block model:

```json title="block.json"
{
    "teksturer": {
        "0": "<your block texture>
        "partikkel": "<your block texture>"
    },
    "elementer": [
        {
            "fra": [0, 0, 0],
            "til": [16, 16, 16]
            "flate": {
                "north": {"uv": [0, 0, 16, 16], "tekst": "#0"},
                "øst": {"uv": [0, 16, 16], "tekst": "#0"},
                "sør": {"uv": [0, 0, 16, 16], "tekst": "#0"},
                "vest": {"uv": [0, 0, 16, 16], "tekst": "#0"},
                "opp": {"uv": [0, 16, 16], "texture": "#0"},
                "down": {"uv": [0, 16, 16], "tekst": "#0"}
            }
        }
    ],
    "vise": {
        "thirdperson_righthand": {
            "translation": [-7, 5, 3],
            "rotasjon": [45. , 5, -8,5],
            "skala": [3.9, 3.9, 3. ]
        },
        "bakke": {
            "skala": [0. , 0,3, 0. ]
        },
        "gui": {
            "rotasjon": [45, 45, 0],
            "skala": [0. 5, 0,65, 0. 5]
        },
        "hode": {
            "translation": [0, -30. 5, 0],
            "skala": [4, 4]
        }
    }
}

```

### Blokker element modell

```json title="block_item.json"
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "<your block texture>"
  }

```
