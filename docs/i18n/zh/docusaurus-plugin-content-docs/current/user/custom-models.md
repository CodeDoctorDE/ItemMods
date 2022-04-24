---
title: 自定义模型
---

:::note 要有自定义模型，你需要有一个包。

Click [here](pack#create-a-pack) to see how you can create one.

:::

## 创建自定义模型

* 转到应用界面中的模型列表
* 点击知识书
* 给它一个名字
* 转到数据界面
* 通过点击知识书来更改默认变量或将您自己的变量添加到纹理中
* 您可以选择模型文件的所在位置。 文件或互联网：
    * 如果您选择了文件，您需要在插件/临时模式/临时中添加纹理文件
        * 现在您需要输入文件名，例如 `ruby.json`
    * 如果您选择互联网，您需要直接链接到json
        * 如果您选择它，请将 `.json` 添加到网址上，例如 `https://example.com/YOURFILE.json`

::::在有自定义模型之前你需要导出资源包。
:::

## 示例：

在临时目录中创建带有此内容的文件。 用您的值替换 *<placeholder\>* 并将其分配到 模型

### 块模型

默认块型号：

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

### 阻止项目模型

```json title="block_item.json"
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "<your block texture>"
  }
}
```
