package server.galaxyunderchaos.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.function.Supplier;

public class ModBoatItem extends Item {

    private final boolean chest;
    private final Supplier<? extends EntityType<? extends Boat>> typeSupplier;  // <-- keep this

    /**
     * @param chest         ‑ true for chest‑boat item
     * @param typeSupplier  ‑ supplier that returns the EntityType to spawn (e.g. ModEntityTypes.AK_BOAT::get)
     * @param props         ‑ normal item properties
     */
    public ModBoatItem(boolean chest,
                       Supplier<? extends EntityType<? extends Boat>> typeSupplier,
                       Properties props) {
        super(props);
        this.chest = chest;
        this.typeSupplier = typeSupplier;
    }

    // ───────────────────────────── USE / PLACE ─────────────────────────────
    @Override
    public InteractionResultHolder<ItemStack> use(Level level,
                                                  Player player,
                                                  InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        HitResult hit = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }

        BlockPos clicked = ((BlockHitResult) hit).getBlockPos();
        Direction face   = ((BlockHitResult) hit).getDirection();
        BlockPos spawnAt = clicked.relative(face);

        BlockState spawnState = level.getBlockState(spawnAt);
        if (!level.getFluidState(spawnAt).isEmpty()
                && !spawnState.getCollisionShape(level, spawnAt).isEmpty()) {
            return InteractionResultHolder.fail(stack);
        }

        Boat boat = (Boat) typeSupplier.get().create(level);   // supplier used here
        if (boat == null) return InteractionResultHolder.fail(stack);

        boat.setPos(spawnAt.getX() + 0.5D,
                spawnAt.getY() + 0.5D,
                spawnAt.getZ() + 0.5D);
        boat.setYRot(player.getYRot());

        if (!level.isClientSide) {
            level.addFreshEntity(boat);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, spawnAt);
            level.playSound(null, spawnAt, SoundEvents.BOAT_PADDLE_LAND,
                    SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild) stack.shrink(1);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    /** Convenience getter */
    public EntityType<? extends Boat> getEntityType() {
        return typeSupplier.get();
    }
}
