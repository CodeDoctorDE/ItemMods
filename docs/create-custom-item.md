# Create custom items

* You must create a resource pack to add custom textures for items. You need an assets folder and a `pack.mcmeta`. Here is a tutorial: https://minecraft.gamepedia.com/Tutorials/Creating_a_resource_pack
* Create the folder `assets/minecraft/models/item` and then the item which you want to create a custom texture, for example `iron_sword.json`
* Then copy this:

  ``` json
  {
  "parent": "item/handheld",
    "textures": {
        "layer0": "item/iron_sword"
    },
    "overrides": [
        { "predicate": { "custom_model_data": 1}, "model": "item/iron_sword/1" }
    ]
  }
  ```

  To add multiple custom items to this item, duplicate the section after the overrides and add a comma. Set the `cutom_model_data` and the model a value higher, for example:

  ``` json
  {
    "parent": "item/handheld",
    "textures": {
        "layer0": "item/iron_sword"
    },
    "overrides": [
        { "predicate": { "custom_model_data": 1}, "model": "item/iron_sword/1" },
        { "predicate": { "custom_model_data": 2}, "model": "item/iron_sword/2" },
        { "predicate": { "custom_model_data": 3}, "model": "item/iron_sword/3" },
        { "predicate": { "custom_model_data": 4}, "model": "item/iron_sword/4" },
        { "predicate": { "custom_model_data": 5}, "model": "item/iron_sword/5" }
    ]
  }
  ```

* Create the model folders. Here we have say minecraft, the item is in `assets/item/iron_sword/1.json`. When you have more than one custom item, you must create the files too.
  You must create a folder `iron_sword` and a file `1.json`
* Copy this to the `1.json`:

  ``` json
  {
    "parent": "item/handheld",
    "textures": {
        "layer0": "item/emerald_sword"
    }
  }
  ```

  Replace `emerald_sword` to the name for your sword.
* Move your image of the custom item in the folder, you set in the `1.json`. For this example it is in `assets/item/emerald_sword.png`
* Load this resource pack in your game.

Now you have a custom item. Congratulations!

Click [here](./Create-items) to see how you can create items with the plugin!
