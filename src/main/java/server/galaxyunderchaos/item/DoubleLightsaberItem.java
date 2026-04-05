package server.galaxyunderchaos.item;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import server.galaxyunderchaos.lightsaber.DoubleLightsaberData;

import java.util.List;

public class DoubleLightsaberItem extends LightsaberItem {
    public DoubleLightsaberItem(String bladeColor, String hiltId, Properties properties) {
        super(bladeColor, hiltId, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);

        if (!attacker.level().isClientSide && isActive(stack)) {
            float splashDamage = 5.0F;
            double reach = 2.35D;
            Vec3 look = attacker.getLookAngle().normalize();
            Vec3 center = attacker.position().add(look.scale(1.15D));
            AABB area = new AABB(center.x - reach, attacker.getBoundingBox().minY - 0.25D, center.z - reach,
                    center.x + reach, attacker.getBoundingBox().maxY + 0.5D, center.z + reach);

            List<LivingEntity> entities = attacker.level().getEntitiesOfClass(LivingEntity.class, area,
                    entity -> entity != attacker && entity != target && entity.isAlive());

            for (LivingEntity entity : entities) {
                if (entity.distanceTo(attacker) > 3.25F) {
                    continue;
                }

                if (entity.hurt(attacker.damageSources().mobAttack(attacker), splashDamage)) {
                    double dx = entity.getX() - attacker.getX();
                    double dz = entity.getZ() - attacker.getZ();
                    double len = Math.max(0.001D, Math.sqrt(dx * dx + dz * dz));
                    entity.push(dx / len * 0.35D, 0.1D, dz / len * 0.35D);
                }
            }
        }

        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        tooltip.add(Component.literal(isActive(stack) ? "Activated" : "Inactive"));
        tooltip.add(Component.literal("Double-bladed configuration"));
        tooltip.add(Component.literal("Upper: " + DoubleLightsaberData.getPrimaryHiltId(stack, true, "mauler") + " / "
                + DoubleLightsaberData.getBladeColor(stack, true, "red")));
        tooltip.add(Component.literal("Lower: " + DoubleLightsaberData.getPrimaryHiltId(stack, false, "mauler") + " / "
                + DoubleLightsaberData.getBladeColor(stack, false, "red")));
    }
}
