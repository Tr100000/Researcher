# Criteria

Any criteria can be used for research, including [all vanilla ones](https://minecraft.wiki/w/Advancement_definition#List_of_triggers),
and any modded ones that you might have added. However, just because they _can_
be used doesn't mean they'll show up properly in the UI.

## Supported Criteria

- [`minecraft:impossible`](https://minecraft.wiki/w/Advancement_definition#minecraft:impossible)
- [`minecraft:consume_item`](https://minecraft.wiki/w/Advancement_definition#minecraft:consume_item)

## More Criteria

Researcher adds some miscellaneous criteria as well, which can be used in research.

- `researcher:block_broken`
  - (`String`) `block` The block that was broken

- `researcher:item_crafted`
  - (`String`) `item` The item that was crafted

- `researcher:research_items`
  > [!NOTE]
  > This can't be triggered without a custom block (or commands).
  - (`int`) `time` The amount of time needed to consume one batch of items
  - (`List<String>`) `items` The items required
