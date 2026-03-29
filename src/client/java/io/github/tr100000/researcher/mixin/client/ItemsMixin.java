package io.github.tr100000.researcher.mixin.client;

import io.github.tr100000.researcher.api.trigger.util.EntityPredicateHelper;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Items.class)
public abstract class ItemsMixin {
    @Inject(method = "registerSpawnEgg", at = @At("RETURN"))
    private static void registerSpawnEgg(final EntityType<?> type, CallbackInfoReturnable<Item> cir) {
        EntityPredicateHelper.registerItemForEntityType(type, cir.getReturnValue());
    }

    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void clinit(CallbackInfo ci) {
        RegistryEntryAddedCallback.event(BuiltInRegistries.ITEM).register((_, _, object) -> EntityPredicateHelper.onItemRegistered(object));
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinitEnd(CallbackInfo ci) {
        EntityPredicateHelper.registerDefault();
    }
}
