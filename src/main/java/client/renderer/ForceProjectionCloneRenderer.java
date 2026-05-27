package client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import server.galaxyunderchaos.entity.ForceProjectionCloneEntity;

public class ForceProjectionCloneRenderer extends HumanoidMobRenderer<ForceProjectionCloneEntity, HumanoidModel<ForceProjectionCloneEntity>> {
    public ForceProjectionCloneRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.45F);
    }

    @Override
    public void render(ForceProjectionCloneEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        AbstractClientPlayer owner = findOwnerPlayer(entity);
        if (owner != null) {
            // Render the real player renderer at the projection's position instead of a generic humanoid model.
            // This preserves slim/wide arms, armor layers, held items, capes, and GUC's player species/robe override layers.
            poseStack.pushPose();
            this.entityRenderDispatcher.render(owner, 0.0D, 0.0D, 0.0D, entityYaw, partialTick, poseStack, buffer, packedLight);
            poseStack.popPose();

            // Vanilla normally hides your own nameplate from yourself. Projections should still display the owner's gamertag.
            if (owner == Minecraft.getInstance().player && entity.hasCustomName()) {
                this.renderNameTag(entity, entity.getDisplayName(), poseStack, buffer, packedLight);
            }
            return;
        }
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    private static AbstractClientPlayer findOwnerPlayer(ForceProjectionCloneEntity entity) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || entity.getOwnerUuid() == null) {
            return null;
        }
        for (Player player : minecraft.level.players()) {
            if (player instanceof AbstractClientPlayer clientPlayer && entity.getOwnerUuid().equals(clientPlayer.getUUID())) {
                return clientPlayer;
            }
        }
        return null;
    }

    @Override
    public ResourceLocation getTextureLocation(ForceProjectionCloneEntity entity) {
        if (Minecraft.getInstance().getConnection() != null) {
            PlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(entity.getOwnerUuid());
            if (info != null) {
                return info.getSkinLocation();
            }
        }
        return DefaultPlayerSkin.getDefaultSkin(entity.getOwnerUuid());
    }
}
