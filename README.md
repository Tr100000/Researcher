# Researcher

A (mostly) data-driven research system for Minecraft inspired by Factorio.

> [!NOTE]
> This mod does nothing on its own, and is meant to be used by other mods or datapacks.

## Features

- Lock recipes behind researches!
- Data-driven! (Mostly)
- Research syncing! (CAUTION: Highly experimental feature)
  - Sync research progress between teams or across all players!
- It's like advancements, but with a count

## Legal Stuff

The mod icon and the default research texture are both a modified version of [this](<https://commons.wikimedia.org/wiki/File:Le_Penseur_by_Rodin_(Kunsthalle_Bielefeld)_2014-04-10.JPG>), and are therefore licensed under [CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/deed.en). Everything else is licensed under the MIT license.

## Why not use advancements?

Sure, you could use the `doLimitedCrafting` gamerule with advancements to lock recipes, but it's disabled by default. Force-enabling it could easily mess with other mods. Furthermore, advancements are somewhat limiting when it comes to unlock conditions.

## How does it work internally?

This mod takes advantage of Minecraft's criteria system, which is used by advancements. Unlike advancements, to complete a research, the criterion must be triggered a set number of times.
Research progress is saved per-world, alongside stuff like advancements.
