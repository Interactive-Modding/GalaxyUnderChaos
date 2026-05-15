package server.galaxyunderchaos.force;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.stream.Collectors;

public final class ForceCommand {
    private ForceCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("force")
                .then(Commands.literal("list")
                        .executes(ctx -> list(ctx.getSource())))
                .then(Commands.literal("unlock")
                        .then(Commands.argument("power", StringArgumentType.word())
                                .executes(ctx -> unlock(ctx.getSource(), StringArgumentType.getString(ctx, "power")))))
                .then(Commands.literal("branch")
                        .then(Commands.argument("side", StringArgumentType.word())
                                .executes(ctx -> branch(ctx.getSource(), StringArgumentType.getString(ctx, "side")))))
                .then(Commands.literal("select")
                        .then(Commands.argument("power", StringArgumentType.word())
                                .executes(ctx -> select(ctx.getSource(), StringArgumentType.getString(ctx, "power")))))
                .then(Commands.literal("unlockall")
                        .executes(ctx -> unlockAll(ctx.getSource())))
                .then(Commands.literal("refill")
                        .executes(ctx -> refill(ctx.getSource()))));
    }

    private static int list(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
                String unlocked = cap.getUnlockedSelectablePowers().stream()
                        .map(ForcePower::displayName)
                        .collect(Collectors.joining(", "));
                source.sendSuccess(() -> Component.literal("Unlocked Force powers: " + (unlocked.isBlank() ? "none" : unlocked)), false);
            });
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int unlock(CommandSourceStack source, String id) {
        if (source.getEntity() instanceof ServerPlayer player) {
            ForcePower power = ForcePower.byId(id);
            if (power == null) {
                source.sendFailure(Component.literal("Unknown power id: " + id));
                return 0;
            }
            ForceCapabilityManager.unlockPower(player, power);
            source.sendSuccess(() -> Component.literal("Unlocked: " + power.displayName()), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int branch(CommandSourceStack source, String value) {
        if (source.getEntity() instanceof ServerPlayer player) {
            ForceSide side;
            try {
                side = ForceSide.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException ex) {
                source.sendFailure(Component.literal("Unknown side: " + value + " (use light, dark, or neutral)"));
                return 0;
            }
            ForceCapabilityManager.unlockBranch(player, side);
            source.sendSuccess(() -> Component.literal("Unlocked " + side.name().toLowerCase() + " Force branch."), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int select(CommandSourceStack source, String id) {
        if (source.getEntity() instanceof ServerPlayer player) {
            ForcePower power = ForcePower.byId(id);
            if (power == null) {
                source.sendFailure(Component.literal("Unknown power id: " + id));
                return 0;
            }
            player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
                cap.selectPower(power);
                ForceCapabilityManager.sync(player);
            });
            source.sendSuccess(() -> Component.literal("Selected: " + power.displayName()), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int unlockAll(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            ForceCapabilityManager.unlockAll(player);
            source.sendSuccess(() -> Component.literal("Unlocked every Force power."), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int refill(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            ForceCapabilityManager.refill(player);
            source.sendSuccess(() -> Component.literal("Force energy restored."), true);
        }
        return Command.SINGLE_SUCCESS;
    }
}
