package server.galaxyunderchaos.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.lightsaber.LightsaberFormCapability;
import server.galaxyunderchaos.lightsaber.LightsaberFormProvider;
import server.galaxyunderchaos.galaxyunderchaos;
import net.minecraft.world.entity.Entity;


@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID)
public class LightsaberFormEventHandler {
    private static final ResourceLocation LIGHTSABER_FORM_CAP = new ResourceLocation(galaxyunderchaos.MODID, "lightsaber_form");

@SubscribeEvent
public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            // Register the actual capability class, **not** the provider
                    event.register(LightsaberFormCapability.class);
        }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            // LazyOptional has isPresent(); negate it instead of isEmpty()
            if (!player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY)
                    .isPresent()) {

                event.addCapability(LIGHTSABER_FORM_CAP, new LightsaberFormProvider());
            }
        }
    }

}
