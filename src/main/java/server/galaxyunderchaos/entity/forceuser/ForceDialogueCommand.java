package server.galaxyunderchaos.entity.forceuser;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public final class ForceDialogueCommand {
    private ForceDialogueCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("gucdialogue")
                .then(Commands.argument("choice", IntegerArgumentType.integer(1, 3))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            int choice = IntegerArgumentType.getInteger(context, "choice");
                            return ForceQuestInventory.chooseDialogueOption(player, choice);
                        })));

        dispatcher.register(Commands.literal("gucforceuser")
                .then(Commands.literal("main")
                        .executes(context -> ForceUserInteractionMenu.openMain(context.getSource().getPlayerOrException())))
                .then(Commands.literal("interact")
                        .executes(context -> ForceUserInteractionMenu.openInteract(context.getSource().getPlayerOrException())))
                .then(Commands.literal("order")
                        .executes(context -> ForceUserInteractionMenu.openOrder(context.getSource().getPlayerOrException())))
                .then(Commands.literal("converse")
                        .executes(context -> ForceUserInteractionMenu.converse(context.getSource().getPlayerOrException())))
                .then(Commands.literal("quests")
                        .executes(context -> ForceUserInteractionMenu.openQuestColumn(context.getSource().getPlayerOrException())))
                .then(Commands.literal("setorder")
                        .then(Commands.argument("mode", StringArgumentType.word())
                                .executes(context -> ForceUserInteractionMenu.setOrder(
                                        context.getSource().getPlayerOrException(),
                                        StringArgumentType.getString(context, "mode"))))));
    }
}
