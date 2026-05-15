/*
 * SPDX-License-Identifier: LGPL-3.0-only
 *
 * This file is part of Galaxy Under Chaos.
 * It contains code, data, model geometry, behavior, or compatibility logic
 * copied, translated, ported, adapted from, or created to support content
 * derived from Advanced Lightsabers 1.2 by FiskFille, credited to FiskFille
 * and Void Adept.
 *
 * Modifications for Galaxy Under Chaos / Minecraft Forge 1.20.1 by
 *  Vitiate and contributors.
 */

package client.model.lightsaber.legacy;

/** Ceremonial silver-and-gold Temple Guard emitter with a broad guarded crown. */
public class ModelEmitterTempleGuard extends ModelTempleGuardPartBase {
    public ModelEmitterTempleGuard() {
        // Silver emitter shroud: separated into its own UV strip so it no longer samples the gold crown band.
        octagonalSleeve(-12.0F, 10, 2.62F, 0, 8);

        // Gold lower collar and top crown flare.
        octagonalRing(-2.0F, 2, 3.05F, 0, 0);
        octagonalRing(-14.0F, 2, 3.30F, 0, 4);

        // Small temple-guard fins over the front/back of the emitter.
        box(44, 0, -0.5F, -12.0F, -3.85F, 1, 8, 1);
        box(44, 0, -0.5F, -12.0F, 2.85F, 1, 8, 1);

        // Side tabs give the emitter a ceremonial guard profile without becoming a crossguard.
        box(50, 0, -3.5F, -14.0F, -0.5F, 2, 2, 1);
        box(50, 0, 1.5F, -14.0F, -0.5F, 2, 2, 1);
    }
}
