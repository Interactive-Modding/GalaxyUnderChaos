package client.renderer.lightsaber;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import it.unimi.dsi.fastutil.objects.ObjectList;

public final class TabulaModelFixer {
    private TabulaModelFixer() {
    }

    public static void fixModel(AdvancedModelBox root) {
        applyOffsetFix(root);
    }

    private static void applyOffsetFix(BasicModelPart part) {
        if (part instanceof AdvancedModelBox advBox) {
            advBox.rotationPointX += advBox.offsetX;
            advBox.rotationPointY += advBox.offsetY;
            advBox.rotationPointZ += advBox.offsetZ;

            advBox.offsetX = 0;
            advBox.offsetY = 0;
            advBox.offsetZ = 0;
        }

        if (part instanceof FixedModelRenderer fixed) {
            ObjectList<BasicModelPart> children = fixed.childModels;
            if (children != null) {
                for (BasicModelPart child : children) {
                    applyOffsetFix(child);
                }
            }
        }
    }
}
