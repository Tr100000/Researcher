package io.github.tr100000.researcher.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.tr100000.researcher.Research;
import io.github.tr100000.researcher.ResearchManager;
import io.github.tr100000.researcher.Researcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class ResearchCommand {
    private ResearchCommand() {}

    private static final DynamicCommandExceptionType EXCEPTION = new DynamicCommandExceptionType(Message.class::cast);
    public static final SuggestionProvider<ServerCommandSource> SUGGESTION_PROVIDER = (context, builder)
            -> CommandSource.suggestIdentifiers(context.getSource().getServer().researcher$getServerManager().listAllIds(), builder);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
        dispatcher.register(literal(ResearchManager.PATH)
                .requires(source -> source.hasPermissionLevel(2))
                .then(literal("grant")
                        .then(argument("targets", EntityArgumentType.players())
                                .then(literal("only")
                                        .then(argument("research", IdentifierArgumentType.identifier())
                                                .suggests(SUGGESTION_PROVIDER)
                                                .executes(context -> execute(context, Operation.GRANT, Selection.ONLY))
                                        )
                                )
                                .then(literal("from")
                                        .then(argument("research", IdentifierArgumentType.identifier())
                                                .suggests(SUGGESTION_PROVIDER)
                                                .executes(context -> execute(context, Operation.GRANT, Selection.FROM))
                                        )
                                )
                                .then(literal("until")
                                        .then(argument("research", IdentifierArgumentType.identifier())
                                                .suggests(SUGGESTION_PROVIDER)
                                                .executes(context -> execute(context, Operation.GRANT, Selection.UNTIL))
                                        )
                                )
                                .then(literal("through")
                                        .then(argument("research", IdentifierArgumentType.identifier())
                                                .suggests(SUGGESTION_PROVIDER)
                                                .executes(context -> execute(context, Operation.GRANT, Selection.THROUGH))
                                        )
                                )
                                .then(literal("everything")
                                        .executes(context -> execute(context, Operation.GRANT, Selection.EVERYTHING))
                                )
                        )
                )
                .then(literal("revoke")
                        .then(argument("targets", EntityArgumentType.players())
                                .then(literal("only")
                                        .then(argument("research", IdentifierArgumentType.identifier())
                                                .suggests(SUGGESTION_PROVIDER)
                                                .executes(context -> execute(context, Operation.REVOKE, Selection.ONLY))
                                        )
                                )
                                .then(literal("from")
                                        .then(argument("research", IdentifierArgumentType.identifier())
                                                .suggests(SUGGESTION_PROVIDER)
                                                .executes(context -> execute(context, Operation.REVOKE, Selection.FROM))
                                        )
                                )
                                .then(literal("until")
                                        .then(argument("research", IdentifierArgumentType.identifier())
                                                .suggests(SUGGESTION_PROVIDER)
                                                .executes(context -> execute(context, Operation.REVOKE, Selection.UNTIL))
                                        )
                                )
                                .then(literal("through")
                                        .then(argument("research", IdentifierArgumentType.identifier())
                                                .suggests(SUGGESTION_PROVIDER)
                                                .executes(context -> execute(context, Operation.REVOKE, Selection.THROUGH))
                                        )
                                )
                                .then(literal("everything")
                                        .executes(context -> execute(context, Operation.REVOKE, Selection.EVERYTHING))
                                )
                        )
                )
                .then(literal("increment")
                        .then(argument("targets", EntityArgumentType.players())
                                .then(argument("research", IdentifierArgumentType.identifier())
                                        .suggests(SUGGESTION_PROVIDER)
                                        .executes(ResearchCommand::increment)
                                )
                        )
                )
                .then(literal("list")
                        .requires(ServerCommandSource::isExecutedByPlayer)
                        .executes(context -> execute(context, Operation.LIST, Selection.EVERYTHING))
                        .then(literal("from")
                                .then(argument("research", IdentifierArgumentType.identifier())
                                        .suggests(SUGGESTION_PROVIDER)
                                        .executes(context -> execute(context, Operation.LIST, Selection.FROM))
                                )
                        )
                        .then(literal("until")
                                .then(argument("research", IdentifierArgumentType.identifier())
                                        .suggests(SUGGESTION_PROVIDER)
                                        .executes(context -> execute(context, Operation.LIST, Selection.UNTIL))
                                )
                        )
                        .then(literal("through")
                                .then(argument("research", IdentifierArgumentType.identifier())
                                        .suggests(SUGGESTION_PROVIDER)
                                        .executes(context -> execute(context, Operation.LIST, Selection.THROUGH))
                                )
                        )
                        .then(literal("everything")
                                .executes(context -> execute(context, Operation.LIST, Selection.EVERYTHING))
                        )
                        .then(literal("root")
                                .executes(context -> execute(context, Operation.LIST, Selection.ROOT))
                        )
                )
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, Operation operation, Selection selection) throws CommandSyntaxException {
        if (operation == Operation.LIST) {
            return executeList(context, selection);
        }

        Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "targets");
        Set<Research> selected = select(context, selection);

        int i = 0;
        try {
            for (ServerPlayerEntity player : targets) {
                i += operation.processAll(player, selected);
            }
        }
        catch (Exception e) {
            Researcher.LOGGER.info("Unknown error!", e);
            return -1;
        }

        ResearchManager researchManager = context.getSource().getServer().researcher$getServerManager();
        if (i == 0) {
            if (selected.size() == 1) {
                if (targets.size() == 1) {
                    throw EXCEPTION.create(
                            Text.translatable(
                                    operation.getCommandPrefix() + ".one.to.one.failure",
                                    selected.iterator().next().getTitle(researchManager),
                                    targets.iterator().next().getDisplayName()
                            )
                    );
                } else {
                    throw EXCEPTION.create(
                            Text.translatable(
                                    operation.getCommandPrefix() + ".one.to.many.failure", selected.iterator().next().getTitle(researchManager), targets.size()
                            )
                    );
                }
            } else if (targets.size() == 1) {
                throw EXCEPTION.create(
                        Text.translatable(
                                operation.getCommandPrefix() + ".many.to.one.failure", selected.size(), targets.iterator().next().getDisplayName()
                        )
                );
            } else {
                throw EXCEPTION.create(Text.translatable(operation.getCommandPrefix() + ".many.to.many.failure", selected.size(), targets.size()));
            }
        }
        else {
            if (selected.size() == 1) {
                if (targets.size() == 1) {
                    context.getSource().sendFeedback(
                            () -> Text.translatable(
                                    operation.getCommandPrefix() + ".one.to.one.success",
                                    selected.iterator().next().getTitle(researchManager),
                                    targets.iterator().next().getDisplayName()
                            ),
                            true
                    );
                } else {
                    context.getSource().sendFeedback(
                            () -> Text.translatable(
                                    operation.getCommandPrefix() + ".one.to.many.success", selected.iterator().next().getTitle(researchManager), targets.size()
                            ),
                            true
                    );
                }
            } else if (targets.size() == 1) {
                context.getSource().sendFeedback(
                        () -> Text.translatable(
                                operation.getCommandPrefix() + ".many.to.one.success", selected.size(), targets.iterator().next().getDisplayName()
                        ),
                        true
                );
            } else {
                context.getSource().sendFeedback(() -> Text.translatable(operation.getCommandPrefix() + ".many.to.many.success", selected.size(), targets.size()), true);
            }

            return i;
        }
    }

    private static int executeList(CommandContext<ServerCommandSource> context, Selection selection) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        Set<Research> selected = select(context, selection);
        return Operation.LIST.processAll(player, selected);
    }

    private static int increment(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ResearchManager researchManager = context.getSource().getServer().researcher$getServerManager();
        Research research = researchManager.get(IdentifierArgumentType.getIdentifier(context, "research"));
        if (research != null) {
            Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "targets");
            targets.forEach(player -> player.researcher$getPlayerTracker().incrementCriterion(research));
            return 0;
        }
        return -1;
    }

    public static Set<Research> select(CommandContext<ServerCommandSource> context, Selection selection) {
        ResearchManager researchManager = context.getSource().getServer().researcher$getServerManager();
        switch (selection) {
            case Selection.EVERYTHING -> {
                return researchManager.listAllAsSet();
            }
            case Selection.ROOT -> {
                return researchManager.getRootNodes();
            }
            default -> {
                Identifier id = IdentifierArgumentType.getIdentifier(context, "research");
                Research research = researchManager.get(id);
                Set<Research> nodes = new HashSet<>();
                if (selection.before) {
                    nodes.addAll(researchManager.allPredecessorsOf(research));
                }
                nodes.add(research);
                if (selection.after) {
                    nodes.addAll(researchManager.allSuccessorsOf(research));
                }
                return nodes;
            }
        }
    }

    public enum Operation {
        GRANT("grant") {
            @Override
            protected boolean processEach(ServerPlayerEntity player, Research research) {
                research.prerequisites(player.getServer().researcher$getServerManager()).forEach(research1 -> {
                    processEach(player, research1);
                });
                return player.researcher$getPlayerTracker().grantCriterion(research);
            }
        },
        REVOKE("revoke") {
            @Override
            protected boolean processEach(ServerPlayerEntity player, Research research) {
                return player.researcher$getPlayerTracker().revokeCriterion(research);
            }
        },
        LIST("list") {
            @Override
            protected boolean processEach(ServerPlayerEntity player, Research research) {
                player.sendMessage(Text.literal(player.getServer().researcher$getServerManager().getId(research).toString()));
                return true;
            }
        };

        private final String commandPrefix;

        Operation(final String name) {
            this.commandPrefix = "commands." + Researcher.MODID + "." + name;
        }

        public int processAll(ServerPlayerEntity player, Iterable<Research> researchList) {
            int i = 0;
            for (Research research : researchList) {
                if (this.processEach(player, research)) {
                    i++;
                }
            }
            return i;
        }

        protected abstract boolean processEach(ServerPlayerEntity player, Research research);

        public String getCommandPrefix() {
            return this.commandPrefix;
        }
    }

    public enum Selection {
        ONLY(false, false),
        THROUGH(true, true),
        FROM(false, true),
        UNTIL(true, false),
        EVERYTHING(true, true),
        ROOT(false, false);

        final boolean before;
        final boolean after;

        Selection(final boolean before, final boolean after) {
            this.before = before;
            this.after = after;
        }
    }
}
