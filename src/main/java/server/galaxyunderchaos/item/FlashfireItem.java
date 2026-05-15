package server.galaxyunderchaos.item;

import client.renderer.ModItemRenderer;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.ship.ShipCustomization;

import java.util.List;
import java.util.function.Consumer;
import server.galaxyunderchaos.entity.FlashfireEntity;

public class FlashfireItem extends Item {
    public FlashfireItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Player player = context.getPlayer();

        if (!level.isClientSide) {
            FlashfireEntity ship = new FlashfireEntity(level, pos.getX() + 0.5D, pos.getY() + 0.08D, pos.getZ() + 0.5D);
            if (player != null) {
                ship.setYRot(player.getYRot());
            }
            ShipCustomization.applyToShip(context.getItemInHand(), ship);
            level.addFreshEntity(ship);
            level.playSound(null, pos, SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.NEUTRAL, 0.6F, 0.65F);
            if (player == null || !player.getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        ModItemRenderer.registerItemRenderer(consumer);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ShipCustomization.addTooltip(stack, tooltip);
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
