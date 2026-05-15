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

/** Long ribbed grip/body with temple-guard gold end bands. */
public class ModelBodyTempleGuard extends ModelTempleGuardPartBase {
    public ModelBodyTempleGuard() {
        // Dark ribbed main grip. Its UV is isolated from the gold bands so the bands do not bleed onto the grip.
        octagonalSleeve(0.0F, 22, 2.45F, 0, 8);

        // Gold caps at each end of the grip.
        octagonalRing(0.0F, 1, 2.95F, 0, 0);
        octagonalRing(20.5F, 2, 2.95F, 0, 4);

        // Four raised grip ribs for a readable darker handhold silhouette.
        for (int i = 0; i < 4; ++i) {
            raisedRib(2.0F, 17, 2.95F, 44, 0, i);
        }

        // A small gold temple crest on the forward face.
        box(50, 0, -0.5F, 14.0F, -3.72F, 1, 5, 1);
        box(54, 0, -1.5F, 17.0F, -3.72F, 3, 1, 1);
    }
}
