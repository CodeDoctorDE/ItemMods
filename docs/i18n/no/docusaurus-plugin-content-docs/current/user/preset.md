---
title: Preset
---

Den forhåndsinnstilte mappen er forhåndsinnstillingen for ressurspakken. Her kan du konfigurere `-pakken. mcmeta` og alle modeller/teksturer og alt annet.

## Opprett forhåndsinnstilling

Du trenger denne mappestrukturen for å ha en fungerende ressurspakke. Hvis du har en gjenstand med en fallback-tekstur diamant, må du ha en `diamond.json`.

```markdown
➜ pack.mcmeta
########″assets
    εminecraftminecraftminecraft 
        ε⁺ models
            Conocominecrafttags item
                εdiamond.json
```

`pakken .mcmeta` må ha dette innholdet:

```json title="pack.mcmeta"
{
  "pakke": {
    "description": "The name of the resource pack",
    "pack_format": 7

}
```

`diamond.json` må ha det samme innholdet som standardteksten for minecraft.

For å gjøre dette må du ha en arkivleser som 7-zip eller WinRAR.

Du må deretter finne din minecraft klientkrukke som normalt ligger i `%appdata%/.minecraft/versions/VERSION/VERSION. ar` (Du må erstatte VERSION med den versjonen du brukte til å koble til serveren).

I jar-arkivet finner du en `element` mappe. Du må kopiere den samme filen. Dersom du har diamant-reserveløsningen må du kopiere filen i `eiendeler/minecraft/models/item/diamond.json`.

Modellfilen bør ligne dette slik:
```json title="assets/minecraft/models/item/diamond.json"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/diamond"
  }

```

Hvis du har en blokk fallback-tekstur som en gressblokk, er modellfilen i blokkens undermappe, for eksempel `eiendeler/minecraft/models/block/grass_block. sønn`.
