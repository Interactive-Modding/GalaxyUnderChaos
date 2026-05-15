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

/** Central activation box with a raised red/amber stud and gold separator bands. */
public class ModelSwitchSectionTempleGuard extends ModelTempleGuardPartBase {
    public ModelSwitchSectionTempleGuard() {
        octagonalSleeve(-8.0F, 8, 2.48F, 0, 8);
        octagonalRing(-8.0F, 1, 2.95F, 0, 0);
        octagonalRing(-1.0F, 1, 2.95F, 0, 4);

        // Gold side rails.
        box(44, 0, -2.85F, -7.0F, -0.5F, 1, 6, 1);
        box(44, 0, 1.85F, -7.0F, -0.5F, 1, 6, 1);

        // Red activation jewel on the front face.
        box(50, 20, -1.0F, -5.75F, -3.62F, 2, 2, 1);
        box(56, 20, -0.5F, -5.25F, -3.98F, 1, 1, 1);
    }
}
