# Locking Other Recipe Types

## Add a recipe unlock display

A `RecipeUnlockDisplay` consists of an `IconRenderer` and a `TooltipComponent`. You
can probably figure out the rest. Use `RecipeUnlockDisplayRegistry#register` to register
it.

## Add the recipe type to `unlockable_recipe_types`

Add the recipe type id in `data/researcher/unlockable_recipe_types.json`, like so:

```json
["example:my_recipe_type"]
```

## Make sure locked recipes can't be crafted

This is the most important part! Different recipe types are handled differently,
which means you'll have to write even more code. Look at [this](https://github.com/Tr100000/Researcher/blob/main/src/main/java/io/github/tr100000/researcher/mixin/RecipeUnlockerMixin.java)
for an example.
