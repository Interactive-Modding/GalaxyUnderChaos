package client.renderer.lightsaber.legacy;

import com.mojang.math.Axis;
import org.joml.Vector3f;

public final class LegacyGL {
    private LegacyGL() {
    }

    public static void glPushMatrix() {
        LegacyRenderSession.get().poseStack().pushPose();
    }

    public static void glPopMatrix() {
        LegacyRenderSession.get().poseStack().popPose();
    }

    public static void glTranslatef(float x, float y, float z) {
        LegacyRenderSession.get().poseStack().translate(x, y, z);
    }

    public static void glScalef(float x, float y, float z) {
        LegacyRenderSession.get().poseStack().scale(x, y, z);
    }

    public static void glScaled(double x, double y, double z) {
        LegacyRenderSession.get().poseStack().scale((float) x, (float) y, (float) z);
    }

    public static void glRotatef(float angleDeg, float x, float y, float z) {
        Vector3f axis = new Vector3f(x, y, z);
        if (axis.lengthSquared() == 0.0F) {
            return;
        }
        axis.normalize();
        LegacyRenderSession.get().poseStack().mulPose(Axis.of(axis).rotationDegrees(angleDeg));
    }
}
