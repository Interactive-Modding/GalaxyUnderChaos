package server.galaxyunderchaos.lightsaber;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncLightsaberFormPacket {
    private final String selectedForm;
    private final int guardStamina;
    private final int maxGuardStamina;
    private final int staminaVisibleTicks;

    public SyncLightsaberFormPacket(String selectedForm, int guardStamina, int maxGuardStamina, int staminaVisibleTicks) {
        this.selectedForm = selectedForm == null ? "" : selectedForm;
        this.guardStamina = guardStamina;
        this.maxGuardStamina = maxGuardStamina;
        this.staminaVisibleTicks = staminaVisibleTicks;
    }

    public static void encode(SyncLightsaberFormPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.selectedForm);
        buffer.writeVarInt(packet.guardStamina);
        buffer.writeVarInt(packet.maxGuardStamina);
        buffer.writeVarInt(packet.staminaVisibleTicks);
    }

    public static SyncLightsaberFormPacket decode(FriendlyByteBuf buffer) {
        return new SyncLightsaberFormPacket(buffer.readUtf(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt());
    }

    public static void handle(SyncLightsaberFormPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().player == null) return;
            Minecraft.getInstance().player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY)
                    .ifPresent(cap -> {
                        cap.setSelectedForm(msg.selectedForm);
                        cap.setMaxGuardStamina(msg.maxGuardStamina);
                        cap.setGuardStamina(msg.guardStamina);
                        cap.setStaminaVisibleTicks(msg.staminaVisibleTicks);
                        cap.clearDirty();
                    });
        });
        ctx.get().setPacketHandled(true);
    }
}
