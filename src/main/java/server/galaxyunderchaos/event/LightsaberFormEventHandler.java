package server.galaxyunderchaos.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.lightsaber.LightsaberFormCapability;
import server.galaxyunderchaos.lightsaber.LightsaberFormCapabilityManager;
import server.galaxyunderchaos.lightsaber.LightsaberFormProvider;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID)
public class LightsaberFormEventHandler {
    private static final ResourceLocation LIGHTSABER_FORM_CAP = new ResourceLocation(galaxyunderchaos.MODID, "lightsaber_form");

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(LightsaberFormCapability.class);
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player
                && !player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY).isPresent()) {
            event.addCapability(LIGHTSABER_FORM_CAP, new LightsaberFormProvider());
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        /*
         * Player capabilities are attached to the old Player instance. Minecraft creates a new
         * Player instance after death, so the saber-form data must be copied during Clone.
         *
         * The old behavior intentionally cleared forms when event.isWasDeath() was true. That
         * made every unlocked holobook/form disappear on death and caused the selected form to
         * reset after respawn. We now copy the saved NBT for both death-respawn and End-return
         * clone events so unlocked forms and the selected form survive.
         */
        event.getOriginal().reviveCaps();
        CompoundTag oldTag = event.getOriginal().getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY)
                .map(LightsaberFormCapability::serializeNBT)
                .orElse(new CompoundTag());
        event.getEntity().getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY)
                .ifPresent(cap -> cap.deserializeNBT(oldTag));
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            LightsaberFormCapabilityManager.syncCapability(player);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            LightsaberFormCapabilityManager.syncCapability(player);
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            LightsaberFormCapabilityManager.syncCapability(player);
        }
    }
}
