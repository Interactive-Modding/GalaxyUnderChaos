package server.galaxyunderchaos.lightsaber;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
import server.galaxyunderchaos.lightsaber.LightsaberFormCapability;
import net.minecraft.world.entity.player.Player;

public class SyncLightsaberFormPacket {
    private final String selectedForm;

    public SyncLightsaberFormPacket(String selectedForm) {
        this.selectedForm = selectedForm == null ? "" : selectedForm;
    }

    public static void encode(SyncLightsaberFormPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.selectedForm);
    }

    public static SyncLightsaberFormPacket decode(FriendlyByteBuf buffer) {
        return new SyncLightsaberFormPacket(buffer.readUtf());
    }

    public static void handle(SyncLightsaberFormPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // On the client main thread
            if (Minecraft.getInstance().player == null) return;

            // Update the local capability
            Minecraft.getInstance().player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY)
                    .ifPresent(cap -> {
                        cap.setSelectedForm(msg.selectedForm);
                    });

            // Show ephemeral action bar text (true = action bar)
            Minecraft.getInstance().player.displayClientMessage(
                    Component.literal("New Lightsaber Form: " + msg.selectedForm),
                    true
            );
        });
        ctx.get().setPacketHandled(true);
    }
}