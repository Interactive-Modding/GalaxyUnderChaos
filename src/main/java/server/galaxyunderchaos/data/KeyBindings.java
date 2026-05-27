package server.galaxyunderchaos.data;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class KeyBindings {

    public static final KeyMapping SWITCH_FORM_KEY = new KeyMapping(
            "key.galaxyunderchaos.switch_form",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.categories.galaxyunderchaos"
    );

    public static final KeyMapping TOGGLE_LIGHTSABER = new KeyMapping(
            "key.galaxyunderchaos.toggle_lightsaber",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "key.categories.galaxyunderchaos"
    );

    public static final KeyMapping CYCLE_FORCE_POWER = new KeyMapping(
            "key.galaxyunderchaos.cycle_force_power",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.galaxyunderchaos"
    );

    public static final KeyMapping USE_FORCE_POWER = new KeyMapping(
            "key.galaxyunderchaos.use_force_power",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.categories.galaxyunderchaos"
    );

    public static final KeyMapping SHOW_FORCE_ALIGNMENT = new KeyMapping(
            "key.galaxyunderchaos.show_force_alignment",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "key.categories.galaxyunderchaos"
    );

    public static final KeyMapping SHIP_ASCEND = new KeyMapping(
            "key.galaxyunderchaos.ship_ascend",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_SPACE,
            "key.categories.galaxyunderchaos"
    );

    public static final KeyMapping SHIP_DESCEND = new KeyMapping(
            "key.galaxyunderchaos.ship_descend",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "key.categories.galaxyunderchaos"
    );

    public static final KeyMapping SHIP_ROLL_LEFT = new KeyMapping(
            "key.galaxyunderchaos.ship_roll_left",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Q,
            "key.categories.galaxyunderchaos"
    );

    public static final KeyMapping SHIP_ROLL_RIGHT = new KeyMapping(
            "key.galaxyunderchaos.ship_roll_right",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_E,
            "key.categories.galaxyunderchaos"
    );

    public static final KeyMapping SHIP_BOOST = new KeyMapping(
            "key.galaxyunderchaos.ship_boost",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            "key.categories.galaxyunderchaos"
    );
    public static final KeyMapping SHIP_THIRD_PERSON_VIEW_ZOOM_OUT = new KeyMapping(
            "key.galaxyunderchaos.ship_zoom_out",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "key.categories.galaxyunderchaos"
    );

    public static final KeyMapping SHIP_THIRD_PERSON_VIEW_ZOOM_IN = new KeyMapping(
            "key.galaxyunderchaos.ship_zoom_in",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "key.categories.galaxyunderchaos"
    );
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(SWITCH_FORM_KEY);
        event.register(TOGGLE_LIGHTSABER);
        event.register(CYCLE_FORCE_POWER);
        event.register(USE_FORCE_POWER);
        event.register(SHOW_FORCE_ALIGNMENT);
        event.register(SHIP_ASCEND);
        event.register(SHIP_DESCEND);
        event.register(SHIP_ROLL_LEFT);
        event.register(SHIP_ROLL_RIGHT);
        event.register(SHIP_BOOST);
        event.register(SHIP_THIRD_PERSON_VIEW_ZOOM_OUT);
        event.register(SHIP_THIRD_PERSON_VIEW_ZOOM_IN);
    }

    private KeyBindings() {}
}
