package server.galaxyunderchaos.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import server.galaxyunderchaos.force.ForceCapabilityManager;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForceSide;
import server.galaxyunderchaos.sound.ModSounds;

public class DatacronItem extends Item {
    private final ForceSide side;

    public DatacronItem(ForceSide side, Properties properties) {
        super(properties);
        this.side = side;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.pass(stack);
        }

        serverPlayer.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.addDatacrons(side, 1);
            if (!serverPlayer.getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.playSound(null, serverPlayer.blockPosition(), ModSounds.HOLOCRON_OPEN.get(), SoundSource.PLAYERS, 0.9F, 1.05F);
            serverPlayer.displayClientMessage(Component.literal(datacronName() + " absorbed: +1 training charge (" + cap.getDatacrons(side) + " stored)."), false);
            ForceCapabilityManager.sync(serverPlayer);
        });

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private String datacronName() {
        return switch (side) {
            case LIGHT -> "Jedi datacron";
            case DARK -> "Sith datacron";
            case NEUTRAL -> "Ancient datacron";
            default -> "Datacron";
        };
    }
}
