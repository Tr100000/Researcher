package io.github.tr100000.researcher.mixin.client;

import io.github.tr100000.codec2schema.Codec2Schema;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Items.class)
public abstract class ItemsMixin {
    @Inject(method = "registerSpawnEgg", at = @At("RETURN"))
    private static void registerSpawnEgg(EntityType<?> type, CallbackInfoReturnable<Item> cir) {
        // FIXME
        Item item = cir.getReturnValue();
        Codec2Schema.LOGGER.info("{}: {}", type, item);
//        EntityPredicateHelper.registerItemForEntityType(type, item);
    }
}
