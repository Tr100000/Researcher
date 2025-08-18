package io.github.tr100000.researcher.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import io.github.tr100000.researcher.Researcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public final class ResearcherCommand {
    private ResearcherCommand() {}

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal(Researcher.MODID)
                .executes(ResearcherCommand::printInfo)
                .then(literal("version")
                        .executes(ResearcherCommand::printInfo)
                )
        );
    }

    private static int printInfo(CommandContext<ServerCommandSource> context) {
        Text message = Text.literal("Researcher").append(Text.literal("v" + Researcher.getVersion().getFriendlyString()).formatted(Formatting.GOLD));
        context.getSource().sendMessage(message);
        return 0;
    }
}
