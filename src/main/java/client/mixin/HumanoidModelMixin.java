package client.mixin;

import client.renderer.ForceHandAnimationHandler;
import client.renderer.lightsaber.LightsaberStanceController;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void guc$applyLightsaberStance(LivingEntity entity,
                                           float limbSwing,
                                           float limbSwingAmount,
                                           float ageInTicks,
                                           float netHeadYaw,
                                           float headPitch,
                                           CallbackInfo ci) {
        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
        LightsaberStanceController.applyHumanoidPose(model, entity, limbSwing, limbSwingAmount, ageInTicks);
        ForceHandAnimationHandler.applyHumanoidPose(model, entity, ageInTicks, netHeadYaw, headPitch);
    }
}
