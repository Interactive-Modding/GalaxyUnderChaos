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

/** Stepped ceremonial pommel with a gold foot ring and dark end cap. */
public class ModelPommelTempleGuard extends ModelTempleGuardPartBase {
    public ModelPommelTempleGuard() {
        octagonalSleeve(0.0F, 5, 2.35F, 0, 8);
        octagonalRing(0.0F, 1, 2.85F, 0, 0);
        octagonalRing(4.0F, 2, 3.05F, 0, 4);

        // Dark terminal cap so the bottom reads as a finished hilt end.
        octagonalRing(5.5F, 1, 2.45F, 0, 20);
    }
}
