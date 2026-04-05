package server.galaxyunderchaos.lightsaber;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import server.galaxyunderchaos.lightsaber.LightsaberFormCapabilityManager;
import server.galaxyunderchaos.lightsaber.LightsaberFormProvider;

import java.util.Set;

public class LightsaberFormCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lightsaberform")
                .then(Commands.literal("list")
                        .executes(ctx -> listForms(ctx.getSource())))
                .then(Commands.literal("unlock")
                        .then(Commands.argument("form", StringArgumentType.string())
                                .executes(ctx -> unlockForm(ctx.getSource(), StringArgumentType.getString(ctx, "form")))))
                .then(Commands.literal("switch")
                        .then(Commands.argument("form", StringArgumentType.string())
                                .executes(ctx -> switchForm(ctx.getSource(), StringArgumentType.getString(ctx, "form"))))));
    }

    private static int listForms(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY).ifPresent(cap -> {
                Set<String> forms = cap.getUnlockedForms();
                source.sendSuccess(() -> Component.literal("Unlocked Forms: " + String.join(", ", forms)), false);
            });
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int unlockForm(CommandSourceStack source, String form) {
        if (source.getEntity() instanceof ServerPlayer player) {
            LightsaberFormCapabilityManager.unlockForm(player, form);
            source.sendSuccess(() -> Component.literal("Unlocked form: " + form), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int switchForm(CommandSourceStack source, String form) {
        if (source.getEntity() instanceof ServerPlayer player) {
            LightsaberFormCapabilityManager.changeForm(player, form);
            source.sendSuccess(() -> Component.literal("Switched to form: " + form), true);
        }
        return Command.SINGLE_SUCCESS;
    }
}