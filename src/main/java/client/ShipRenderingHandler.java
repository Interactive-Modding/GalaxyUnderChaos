package client;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;
import java.util.ArrayList;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public class ShipRenderingHandler {
    public static final ShipRenderingHandler INSTANCE = new ShipRenderingHandler();
    private float thirdPersonViewDistance = 4.0f;

    public float getThirdPersonViewDistance() {
        return thirdPersonViewDistance;
    }

    public void setThirdPersonViewDistance(float dist) {
        this.thirdPersonViewDistance = Mth.clamp(dist, 2.0f, 40.0f);
    }

    public void resetThirdPersonViewDistance() {
        this.thirdPersonViewDistance = 4.0f;
    }
}
