package server.galaxyunderchaos.ship;

import net.minecraft.world.phys.Vec3;

/**
 * Single tuning file for Novadive engine/exhaust positions.
 *
 * Coordinates are ship-local, in model pixels, then converted to blocks.
 * X: left/right, Y: up/down, Z: back toward the engines.
 * The model nose points toward local -Z, so engine exhausts sit at positive Z.
 */
public final class NovadiveEngineLayout {
    private static final double PX = 1.0D / 16.0D;

    public static final EnginePoint[] ENGINES = new EnginePoint[] {
            engine(-18.75D, 14.0D, 26.25D),
            engine(18.75D, 14.0D, 26.25D),
            engine(-10.77855D, 12.75D, 19.23489D),
            engine(10.77855D, 12.75D, 19.23489D)
    };

    private NovadiveEngineLayout() {
    }

    private static EnginePoint engine(double xPixels, double yPixels, double zPixels) {
        return new EnginePoint(xPixels * PX, yPixels * PX, zPixels * PX);
    }

    public static final class EnginePoint {
        private final double x;
        private final double y;
        private final double z;

        private EnginePoint(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vec3 particleLocal() {
            return new Vec3(this.x, this.y, this.z);
        }

        public float renderX() {
            return (float)this.x;
        }

        public float renderY() {
            return (float)this.y;
        }

        public float renderZ() {
            return (float)-this.z;
        }
    }
}
