package client.renderer;

import client.renderer.forceuser.PlayerForceIdentityClientState;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Hides only the vanilla player model while the Force identity layer renders the selected species/robe. */
@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlayerForceBodyVisibilityEvents {
    private static final Map<UUID, VisibilitySnapshot> SNAPSHOTS = new HashMap<>();

    private PlayerForceBodyVisibilityEvents() {}

    @SubscribeEvent
    public static void beforePlayerRender(RenderPlayerEvent.Pre event) {
        Player rendered = event.getEntity();
        if (!(rendered instanceof AbstractClientPlayer player)
                || player.isInvisible()
                || !PlayerForceIdentityClientState.hasVisibleOverride(player.getUUID())) {
            return;
        }

        PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();
        SNAPSHOTS.put(player.getUUID(), VisibilitySnapshot.capture(model));

        boolean alienModelSelected = PlayerForceIdentityClientState.hasAlienSpecies(player.getUUID());

        // Only the vanilla player model is hidden here. Alien model parts are
        // never toggled off; PlayerForceSpeciesLayer renders the full selected
        // alien model. Legacy robe-only saves keep the vanilla head visible.
        model.body.visible = false;
        model.jacket.visible = false;
        model.rightArm.visible = false;
        model.rightSleeve.visible = false;
        model.leftArm.visible = false;
        model.leftSleeve.visible = false;
        model.rightLeg.visible = false;
        model.rightPants.visible = false;
        model.leftLeg.visible = false;
        model.leftPants.visible = false;
        model.head.visible = !alienModelSelected;
        model.hat.visible = !alienModelSelected;
    }

    @SubscribeEvent
    public static void afterPlayerRender(RenderPlayerEvent.Post event) {
        Player rendered = event.getEntity();
        if (!(rendered instanceof AbstractClientPlayer player)) {
            return;
        }
        VisibilitySnapshot snapshot = SNAPSHOTS.remove(player.getUUID());
        if (snapshot != null) {
            snapshot.restore(event.getRenderer().getModel());
        }
    }

    private record VisibilitySnapshot(boolean head, boolean hat, boolean body, boolean jacket,
                                      boolean rightArm, boolean rightSleeve, boolean leftArm, boolean leftSleeve,
                                      boolean rightLeg, boolean rightPants, boolean leftLeg, boolean leftPants) {
        static VisibilitySnapshot capture(PlayerModel<AbstractClientPlayer> model) {
            return new VisibilitySnapshot(
                    model.head.visible, model.hat.visible, model.body.visible, model.jacket.visible,
                    model.rightArm.visible, model.rightSleeve.visible, model.leftArm.visible, model.leftSleeve.visible,
                    model.rightLeg.visible, model.rightPants.visible, model.leftLeg.visible, model.leftPants.visible
            );
        }

        void restore(PlayerModel<AbstractClientPlayer> model) {
            model.head.visible = head;
            model.hat.visible = hat;
            model.body.visible = body;
            model.jacket.visible = jacket;
            model.rightArm.visible = rightArm;
            model.rightSleeve.visible = rightSleeve;
            model.leftArm.visible = leftArm;
            model.leftSleeve.visible = leftSleeve;
            model.rightLeg.visible = rightLeg;
            model.rightPants.visible = rightPants;
            model.leftLeg.visible = leftLeg;
            model.leftPants.visible = leftPants;
        }
    }
}
