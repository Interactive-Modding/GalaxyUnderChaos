package server.galaxyunderchaos.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import server.galaxyunderchaos.lightsaber.LightsaberFormCapabilityManager;

public class SaberFormHolobookItem extends Item {
    private final String formToUnlock;

    public SaberFormHolobookItem(String form, Properties properties) {
        super(properties);
        this.formToUnlock = form;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            LightsaberFormCapabilityManager.unlockForm(serverPlayer, formToUnlock);
            player.displayClientMessage(Component.nullToEmpty("You have unlocked " + formToUnlock + "!"), true);
            player.getItemInHand(hand).shrink(1); // Consume the holocron
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
    }
}
