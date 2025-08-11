package io.github.tr100000.researcher.mixin;

import com.mojang.authlib.GameProfile;
import io.github.tr100000.researcher.PlayerResearchTracker;
import io.github.tr100000.researcher.api.PlayerResearchTrackerGetter;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements PlayerResearchTrackerGetter {
    @Unique
    private PlayerResearchTracker researchTracker;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions clientOptions, CallbackInfo ci) {
        researchTracker = server.getPlayerManager().researcher$getPlayerTracker((ServerPlayerEntity)(Object)this);
    }

    @Inject(method = "tick", at = @At(("TAIL")))
    private void tick(CallbackInfo ci) {
        researchTracker.sendUpdate();
    }

    @Override @Unique
    public PlayerResearchTracker researcher$getPlayerTracker() {
        return researchTracker;
    }
}
