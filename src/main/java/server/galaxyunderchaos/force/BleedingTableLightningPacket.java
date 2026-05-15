package server.galaxyunderchaos.force;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import server.galaxyunderchaos.block.BleedingTable;
import server.galaxyunderchaos.entity.BleedingTableBlockEntity;
import server.galaxyunderchaos.menu.BleedingTableMenu;
import server.galaxyunderchaos.sound.ModSounds;

import java.util.function.Supplier;

public class BleedingTableLightningPacket {
    private static final float FORCE_COST = 35.0F;
    private final BlockPos pos;

    public BleedingTableLightningPacket(BlockPos pos) {
        this.pos = pos == null ? BlockPos.ZERO : pos;
    }

    public static void encode(BleedingTableLightningPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
    }

    public static BleedingTableLightningPacket decode(FriendlyByteBuf buf) {
        return new BleedingTableLightningPacket(buf.readBlockPos());
    }

    public static void handle(BleedingTableLightningPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || !(player.containerMenu instanceof BleedingTableMenu menu)) {
                return;
            }
            if (!menu.getBlockPos().equals(packet.pos)) {
                return;
            }
            if (!(player.level().getBlockEntity(packet.pos) instanceof BleedingTableBlockEntity table)) {
                return;
            }

            player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
                if (!cap.hasPower(ForcePower.LIGHTNING1) && !cap.hasPower(ForcePower.LIGHTNING2) && !cap.hasPower(ForcePower.LIGHTNING3)) {
                    player.displayClientMessage(Component.literal("You must know Force Lightning to bleed a kyber crystal."), true);
                    player.level().playSound(null, player.blockPosition(), ModSounds.FORCE_CAST_FAIL.get(), SoundSource.PLAYERS, 0.8F, 1.0F);
                    return;
                }
                if (!table.hasCrystal()) {
                    player.displayClientMessage(Component.literal("Place a kyber crystal in the bleeding table first."), true);
                    return;
                }
                if (!BleedingTable.isValidKyber(table.getCrystalStack())) {
                    player.displayClientMessage(Component.literal("This crystal has already been bled."), true);
                    return;
                }
                if (!cap.consumeForce(FORCE_COST)) {
                    player.displayClientMessage(Component.literal("Not enough Force. Bleeding requires " + (int)FORCE_COST + " Force."), true);
                    player.level().playSound(null, player.blockPosition(), ModSounds.FORCE_CAST_FAIL.get(), SoundSource.PLAYERS, 0.8F, 1.0F);
                    ForceCapabilityManager.sync(player);
                    return;
                }

                cap.beginVisual(ForcePower.LIGHTNING1, 20, ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_LEFT_ARM | ForceCapability.VISUAL_LIGHTNING);
                boolean changed = table.bleedCrystal();
                if (changed) {
                    BleedingTable.summonLightningEffect(player.level(), packet.pos, player);
                    player.level().playSound(null, packet.pos, ModSounds.FORCE_LIGHTNING_START.get(), SoundSource.BLOCKS, 1.0F, 0.85F);
                    player.displayClientMessage(Component.literal("The kyber crystal bleeds red."), false);
                }
                ForceCapabilityManager.sync(player);
            });
        });
        context.setPacketHandled(true);
    }
}
