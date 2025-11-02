package io.github.tr100000.researcher.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.tr100000.researcher.PlayerResearchTracker;
import io.github.tr100000.researcher.ResearchManager;
import io.github.tr100000.researcher.ResearchSyncMode;
import io.github.tr100000.researcher.api.ServerResearchTrackerGetter;
import io.github.tr100000.researcher.config.ResearcherConfigs;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin implements ServerResearchTrackerGetter {
    @Shadow @Final private MinecraftServer server;

    @Shadow @Final private List<ServerPlayerEntity> players;
    @Unique
    private final Map<UUID, PlayerResearchTracker> researchTrackers = new Object2ObjectOpenHashMap<>();

    @Inject(method = "savePlayerData", at = @At("TAIL"))
    private void savePlayerData(ServerPlayerEntity player, CallbackInfo ci) {
        PlayerResearchTracker researchTracker = researchTrackers.get(player.getUuid());
        if (researchTracker != null) {
            researchTracker.save(server.researcher$getServerManager());
        }
    }

    @Inject(method = "remove", at = @At(value = "INVOKE", target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0))
    private void remove(ServerPlayerEntity player, CallbackInfo ci, @Local UUID uuid) {
        researchTrackers.remove(uuid);
        players.forEach(otherPlayer -> otherPlayer.researcher$getPlayerTracker().stopSyncWith(player.researcher$getPlayerTracker()));
    }

    @Inject(method = "onDataPacksReloaded", at = @At("HEAD"))
    private void onDataPacksReloaded(CallbackInfo ci) {
        researchTrackers.forEach((uuid, playerResearchTracker) -> playerResearchTracker.reload(server.researcher$getServerManager()));
    }

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        playersToSyncWith(player, ResearcherConfigs.server.researchSyncMode.get())
                .map(ServerPlayerEntity::researcher$getPlayerTracker)
                .forEach(player.researcher$getPlayerTracker()::syncWith);
    }

    @Unique
    private Stream<ServerPlayerEntity> playersToSyncWith(ServerPlayerEntity player, ResearchSyncMode syncMode) {
        return switch (ResearcherConfigs.server.researchSyncMode.get()) {
            case GLOBAL -> players.stream();
            case TEAM -> players.stream().filter(player::isTeammate);
            default -> Stream.empty(); // Don't sync with anyone by default
        };
    }

    @Override @Unique
    public PlayerResearchTracker researcher$getPlayerTracker(ServerPlayerEntity player) {
        UUID uuid = player.getUuid();
        PlayerResearchTracker researchTracker = researchTrackers.computeIfAbsent(uuid, uuid2 -> createResearchTracker(player, uuid2));
        researchTracker.setOwner(player);
        return researchTracker;
    }

    @Unique
    private PlayerResearchTracker createResearchTracker(ServerPlayerEntity player, UUID uuid) {
        Path path = server.getSavePath(ResearchManager.WORLD_SAVE_PATH).resolve(uuid + ".json");
        return new PlayerResearchTracker(player, server.researcher$getServerManager(), (PlayerManager)(Object) this, path);
    }
}
