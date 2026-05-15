package server.galaxyunderchaos.force;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import server.galaxyunderchaos.menu.ForceHolocronMenu;
import server.galaxyunderchaos.sound.ModSounds;

import java.util.function.Supplier;

public class HolocronPowerActionPacket {
    private final String powerId;
    private final boolean selectOnly;

    public HolocronPowerActionPacket(String powerId, boolean selectOnly) {
        this.powerId = powerId;
        this.selectOnly = selectOnly;
    }

    public static void encode(HolocronPowerActionPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.powerId);
        buf.writeBoolean(packet.selectOnly);
    }

    public static HolocronPowerActionPacket decode(FriendlyByteBuf buf) {
        return new HolocronPowerActionPacket(buf.readUtf(), buf.readBoolean());
    }

    public static void handle(HolocronPowerActionPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || !(player.containerMenu instanceof ForceHolocronMenu menu)) {
                return;
            }

            ForcePower power = ForcePower.byId(packet.powerId);
            if (power == null || !ForceHolocronLogic.isAllowed(menu.getSide(), power)) {
                return;
            }

            player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
                if (packet.selectOnly) {
                    if (cap.hasPower(power) && power.isSelectable()) {
                        cap.selectPower(power);
                        player.level().playSound(null, player.blockPosition(), ModSounds.HOLOCRON_OPEN.get(), SoundSource.BLOCKS, 0.7F, 1.15F);
                    }
                    ForceCapabilityManager.sync(player);
                    return;
                }

                if (!ForceHolocronLogic.hasPrerequisites(menu.getSide(), cap, power)) {
                    player.displayClientMessage(Component.literal(ForceHolocronLogic.requiresCompletedStudentTraining(power) ? "Train a Padawan/Apprentice into a full Jedi, Sith, or Neutral Force user before unlocking this level." : "This technique requires earlier knowledge first."), true);
                    ForceCapabilityManager.sync(player);
                    return;
                }

                int cost = ForceHolocronLogic.getDatacronCost(menu.getSide(), power);
                ForceSide bank = ForceHolocronLogic.getDatacronBank(menu.getSide());
                if (cost > 0 && !cap.consumeDatacrons(bank, cost)) {
                    player.displayClientMessage(Component.literal("Requires " + cost + " " + bankLabel(bank) + " datacron charges."), true);
                    ForceCapabilityManager.sync(player);
                    return;
                }

                boolean unlocked = cap.unlockPower(power);
                if (unlocked) {
                    if (!ForceHolocronLogic.isAncientHolocron(menu.getSide())) {
                        if (power == ForcePower.DARK_SIDE) {
                            cap.setCommittedSide(ForceSide.DARK);
                            cap.beginAlignmentFlash(ForceSide.DARK, 120);
                            player.displayClientMessage(Component.literal("Embracing dark side..."), false);
                        } else if (power == ForcePower.LIGHT_SIDE) {
                            cap.setCommittedSide(ForceSide.LIGHT);
                            player.displayClientMessage(Component.literal("You turn toward the light side."), false);
                        }
                    }

                    if (power.isSelectable()) {
                        cap.selectPower(power);
                    }

                    player.level().playSound(null, player.blockPosition(), power.tier() > 1 ? ModSounds.HOLOCRON_INVEST.get() : ModSounds.HOLOCRON_UNLOCK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (cost > 0) {
                        player.displayClientMessage(Component.literal(power.displayName() + " unlocked (" + cost + " datacrons consumed)."), false);
                    }
                }

                if (ForceHolocronLogic.isCompleteForSide(menu.getSide(), cap)) {
                    player.displayClientMessage(Component.literal("You have exhausted this holocron's knowledge."), false);
                }
                ForceCapabilityManager.sync(player);
            });
        });
        context.setPacketHandled(true);
    }

    private static String bankLabel(ForceSide bank) {
        return switch (bank) {
            case LIGHT -> "Jedi";
            case DARK -> "Sith";
            case NEUTRAL -> "Ancient";
            default -> "Force";
        };
    }
}
