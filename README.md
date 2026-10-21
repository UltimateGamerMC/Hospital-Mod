# Hospital Mod

[![Modrinth](https://img.shields.io/badge/Modrinth-hospital-00AF5C)](https://modrinth.com/mod/hospital)
[![GitHub](https://img.shields.io/badge/GitHub-Hospital--Mod-181717?logo=github)](https://github.com/UltimateGamerMC/Hospital-Mod)

A working hospital for Minecraft: patient beds, IV drips, heart monitors, a supply cabinet, a
biohazard sharps bin and a full set of medical tools. Built for **Fabric**, on **Minecraft 26.3**
and **1.21.11**.

Everything is hand-made: three custom block models authored box by box, and 16x16 textures drawn
pixel by pixel in `tools/pixelart.py`.

## The machines

| Block | What it does |
| --- | --- |
| **Hospital Bed** | Right-click to lie down. It heals you to full, clears every debuff, gives Regeneration and becomes your respawn point. While occupied it keeps mending anyone standing beside it, and the model swaps to a patient under a blanket. |
| **IV Drip Stand** | A wheeled pole with an infusion pump. Refill the bag with a Saline Bag and it heals anyone within 4 blocks, one dose at a time. The bag visibly drains: full, half, then empty. |
| **Heart Monitor** | The screen lights up on its own whenever a living patient is within 6 blocks, with a live ECG. Right-click for a full vitals chart of the nearest patient in chat. |
| **Supply Cabinet** | Right-click to restock with bandages, painkillers, saline bags, syringes and the occasional medkit. 30 second cooldown per player. |
| **Sharps Container** | A biohazard disposal bin. Drop used syringes in the slot and the window fills (empty, amber, half, red). When it is full the lid gains hazard tape, a warning lamp rises on top, the bin glows, and it vents soul fumes and shrieks until you seal it with a Biohazard Bag. |
| **Biohazard Sign** | A big glowing wall sign: red field, hazard tape top and bottom, the word BIOHAZARD and a large trefoil. Light level 14, so it lights the room on its own. |
| **Hospital Floor / Wall** | Clean linoleum and panelled walls to build the ward with. |

## The tools

| Item | Use |
| --- | --- |
| **Bandage** | Heals 2 hearts and clears poison, wither and weakness. Cheap and stackable. |
| **Medkit** | Heals 5 hearts, clears all debuffs, then Regeneration and Absorption. |
| **Painkillers** | Clears debuffs and grants Absorption + Resistance for a long time. |
| **Syringe** | Self-inject for Speed II + Regeneration, or inject a patient to stim them. On the undead it works as a sedative instead. |
| **Stethoscope** | Right-click a patient for a full vitals chart, or use it on yourself. |
| **Defibrillator** | Shocks a critical patient back to full health and clears their debuffs. 30 second cooldown. |
| **Saline Bag** | Refills an IV drip stand. |
| **Biohazard Bag** | Seals a full sharps container so it is safe to dispose of. |
| **Scalpel** | A fast, light surgical blade. |

## Commands

- `/hospital give`: the whole kit.
- `/hospital heal`: full heal and clear every debuff.
- `/hospital vitals`: print your own vitals chart.
- `/hospital room`: build a complete ward around you: floor, walls, ceiling, lights, two beds with
  drips, a heart monitor and a supply cabinet.

## Crafting

Everything is craftable from vanilla materials: iron, glass, white concrete, quartz, wool, string,
paper, sugar and redstone. Recipes unlock as you pick up the ingredients.

## Building

```
./gradlew build
```

The jar lands in `build/libs/`. Assets are generated:

```
python3 tools/pixelart.py            # 28 hand-placed 16x16 textures + the icon
python3 tools/models.py --mc 26.3    # block models, blockstates, item models, recipes, lang
```

`tools/bbmodel/` holds Blockbench sources for the three custom models.

## Links

- Modrinth: https://modrinth.com/mod/hospital
- Source and issues: https://github.com/UltimateGamerMC/Hospital-Mod

## Licence

CC0-1.0, do whatever you like with it.
