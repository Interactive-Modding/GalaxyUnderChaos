package client.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import server.galaxyunderchaos.worldgen.GalaxyJigsawStructureCodecs;

/**
 * Keeps Galaxy Under Chaos' large jigsaw-structure support, but avoids putting
 * RecordCodecBuilder lambdas directly inside the mixin class. Runtime lambda
 * bodies inside mixin classes can make the mixin class load as "invalid" during
 * bootstrap. This injector only swaps the static CODEC after vanilla builds it;
 * the actual codec lives in a normal helper class.
 */
@Mixin(JigsawStructure.class)
public abstract class JigsawStructureLimitMixin {
    @Shadow
    @Final
    @Mutable
    public static Codec<JigsawStructure> CODEC;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void galaxyunderchaos$replaceJigsawCodec(CallbackInfo ci) {
        CODEC = GalaxyJigsawStructureCodecs.create();
    }
}
