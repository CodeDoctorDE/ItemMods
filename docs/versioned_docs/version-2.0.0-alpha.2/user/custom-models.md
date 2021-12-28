---
title: Custom models
---

:::note To have custom textures, you need to have a pack. See [here](pack.md#create-a-pack) to see how you can create one.
:::

## Create a custom model

* Go to the model list in the pack gui
* Click on the knowledge book
* Give it a name
* Go to the data gui
* Change the default variation or add your own variation to the texture by clicking on the knowledge book
* You can choose where the model file is located. File or internet:
    * If you choose file, you need to add the texture file in plugins/ItemMods/temp
        * Now you need to enter the file name, for example `ruby.json`
    * If you choose internet, you need the direct link to the json
        * If you choose it, please add `.json` to the url, for example `https://example.com/YOURFILE.json`

:::caution You need to export the resource pack before having a custom model.
:::

## Examples

Create a file with this content in the temp directory. Replace the *\<placeholder\>* with your values and assign it to a model

### Block model

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
			"to": [16, 16, 16],
			"faces": {
				"north": {"uv": [0, 0, 16, 16], "texture": "#0"},
				"east": {"uv": [0, 0, 16, 16], "texture": "#0"},
				"south": {"uv": [0, 0, 16, 16], "texture": "#0"},
				"west": {"uv": [0, 0, 16, 16], "texture": "#0"},
				"up": {"uv": [0, 0, 16, 16], "texture": "#0"},
				"down": {"uv": [0, 0, 16, 16], "texture": "#0"}
			}
		}
	],
	"display": {
		"thirdperson_righthand": {
			"translation": [-7, 5, 3],
			"rotation": [45.5, 5, -8.5],
			"scale": [3.9, 3.9, 3.9]
		},
		"ground": {
			"scale": [0.3, 0.3, 0.3]
		},
		"gui": {
			"rotation": [45, 45, 0],
			"scale": [0.65, 0.65, 0.65]
		},
		"head": {
			"translation": [0, -30.75, 0],
			"scale": [4, 4, 4]
		}
	}
}

```

### Block item model

```json title="block_item.json"
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "<your block texture>"
  }
}
```
