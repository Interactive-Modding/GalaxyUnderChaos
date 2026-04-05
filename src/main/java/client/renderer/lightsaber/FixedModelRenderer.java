package client.renderer.lightsaber;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class FixedModelRenderer extends AdvancedModelBox {
    public FixedModelRenderer(AdvancedEntityModel<?> model, String name) {
        super(model, name);
    }

    @Override
    public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                       float red, float green, float blue, float alpha) {
        super.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
