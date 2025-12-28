package io.github.tr100000.researcher.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.screen.ResearchScreen;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public final class ResearcherClientCommand {
    private ResearcherClientCommand() {}

    public static final SuggestionProvider<FabricClientCommandSource> SUGGESTION_PROVIDER = (context, builder) ->
            SharedSuggestionProvider.suggestResource(context.getSource().getClient().getConnection().researcher$getClientTracker().listAllIds(), builder);

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(literal("researcher_client")
                .then(literal("show")
                        .then(argument("research", IdentifierArgument.id())
                                .suggests(SUGGESTION_PROVIDER)
                                .executes(ResearcherClientCommand::showResearch)
                        )
                )
        );
    }

    private static int showResearch(CommandContext<FabricClientCommandSource> context) {
        Minecraft client = context.getSource().getClient();
        Identifier id = context.getArgument("research", Identifier.class);
        Research research = client.getConnection().researcher$getClientTracker().get(id);
        if (research != null) {
            ResearchScreen.setSelected(research);
            client.setScreen(new ResearchScreen(client.screen));
            return 1;
        }
        else {
            context.getSource().sendError(Component.literal("Unknown research id"));
            return 0;
        }
    }
}
