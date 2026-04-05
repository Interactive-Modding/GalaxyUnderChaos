package server.galaxyunderchaos.lightsaber;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
import server.galaxyunderchaos.lightsaber.LightsaberFormCapabilityManager;
import java.util.List;

public class SwitchLightsaberFormPacket {
    public SwitchLightsaberFormPacket() {}

    public static void encode(SwitchLightsaberFormPacket packet, FriendlyByteBuf buffer) {
        // No data to write since we are simply toggling next form
    }

    public static SwitchLightsaberFormPacket decode(FriendlyByteBuf buffer) {
        return new SwitchLightsaberFormPacket();
    }

    public static void handle(SwitchLightsaberFormPacket packet, Supplier<NetworkEvent.Context> ctx) {
        //System.out.println("DEBUG: SwitchLightsaberFormPacket.handle triggered on the server!");
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                //System.out.println("DEBUG: Player is null, returning...");
                return;
            }

            player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY).ifPresent(cap -> {
                //System.out.println("DEBUG: We have a capability, checking unlocked forms...");
                var unlockedForms = List.copyOf(cap.getUnlockedForms());

                if (unlockedForms.isEmpty()) {
                    // If truly empty, print at least a debug
                    //System.out.println("DEBUG: No forms unlocked, sending chat message...");
                    player.sendSystemMessage(Component.literal("You have not unlocked any lightsaber forms yet!"));
                    return;
                }

                int currentIndex = unlockedForms.indexOf(cap.getSelectedForm());
                int nextIndex = (currentIndex + 1) % unlockedForms.size();

                String oldForm = cap.getSelectedForm();
                String newForm = unlockedForms.get(nextIndex);

                cap.setSelectedForm(newForm);
                //System.out.println("DEBUG: cap now says selectedForm = " + cap.getSelectedForm());

                // Sync to client, also debug
                //System.out.println("DEBUG: Switching from " + oldForm + " to " + newForm);
                LightsaberFormCapabilityManager.syncCapability(player);

                // The line you want to see in chat
                player.sendSystemMessage(Component.literal(
                        "Switched lightsaber form from '" + oldForm + "' to '" + newForm + "'."));
            });
        });
        ctx.get().setPacketHandled(true);
    }



}
