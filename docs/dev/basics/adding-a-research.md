# Adding a Research

Open your favorite text editor, it's time for some JSON!

## What's in a research anyway?

> [!TIP]
> Every field is optional, you don't have to define everything.

### `title`

A [text component](https://minecraft.wiki/w/Text_component_format#Java_Edition)
with the name of the research. If missing, defaults to `research.[your-mod-id].[your-research-id]`.

### `description`

A [text component](https://minecraft.wiki/w/Text_component_format#Java_Edition)
with the description of the research. If missing, defaults to `research.[your-mod-id].[your-research-id].desc`.

### `display`

A `IconRenderer` for the icon of the research. If missing, defaults to the mod icon.

### `recipeUnlocks`

A list of recipe ids that will be unlocked once the research is completed.

### `prerequisites`

A list of researches that must be completed to unlock the research.

### `toUnlock`

The criterion to unlock the research.

## An Example

That probably raised more questions than it answered.

Here's what a research file looks like:

```json
{
  "title": "Blast Furnace",
  "display": {
    "type": "item",
    "item": {
      "count": 1,
      "id": "minecraft:blast_furnace"
    }
  },
  "recipeUnlocks": ["minecraft:blast_furnace"],
  "toUnlock": {
    "count": 2,
    "criterion": {
      "trigger": "researcher:item_crafted",
      "conditions": {
        "item": "minecraft:furnace"
      }
    }
  }
}
```

As you can see, this research is completed by crafting two furnaces, and unlocks
the recipe for the blast furnace.

> [!NOTE]
> Research files go in `data/[your-mod-id]/research/`, just in case it wasn't obvious.
>
> <small>I mean, where else would you put them?</small>

<small>TODO make an example datapack and put a download link here</small>
