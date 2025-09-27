# Adding a Criterion Handler

> [!WARNING]
> Client-only!

## Huh?

A `CriterionHandler` is a way for Researcher to show the player how to complete a
specific task.

If your research `toUnlock` looks like this:

```json
{
  "count": 5,
  "criterion": {
    "trigger": "researcher:item_crafted",
    "conditions": {
      "item": "minecraft:iron_ingot"
    }
  }
}
```

Researcher will look for a `CriterionHandler` for `researcher:item_crafted`, and
will use it to display information to the plyer.

## Doesn't Researcher do this for you?

Some vanilla criteria are not supported, and if you've added custom criteria, they
won't work either. If you want something other than a error showing up, you'll have
to do some work.

You can take a look at Researcher's criterion handlers to get an idea of how they
work, and use `CriterionHandlerRegistry#register` to register it once you've figured
it out.
