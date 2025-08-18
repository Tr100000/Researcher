package io.github.tr100000.researcher.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.screen.ResearchScreen;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public final class ResearcherClientCommand {
    private ResearcherClientCommand() {}

    public static final SuggestionProvider<FabricClientCommandSource> SUGGESTION_PROVIDER = (context, builder) ->
            CommandSource.suggestIdentifiers(context.getSource().getClient().getNetworkHandler().researcher$getClientTracker().listAllIds(), builder);

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(literal("researcher_client")
                .then(literal("show")
                        .then(argument("research", IdentifierArgumentType.identifier())
                                .suggests(SUGGESTION_PROVIDER)
                                .executes(ResearcherClientCommand::showResearch)
                        )
                )
        );
    }

    private static int showResearch(CommandContext<FabricClientCommandSource> context) {
        MinecraftClient client = context.getSource().getClient();
        Identifier id = context.getArgument("research", Identifier.class);
        Research research = client.getNetworkHandler().researcher$getClientTracker().get(id);
        if (research != null) {
            ResearchScreen.setSelected(research);
            client.setScreen(new ResearchScreen(client.currentScreen));
            return 1;
        }
        else {
            context.getSource().sendError(Text.literal("Unknown research id"));
            return 0;
        }
    }
}
