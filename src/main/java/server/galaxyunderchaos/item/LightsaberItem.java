package server.galaxyunderchaos.item;

import client.renderer.ModItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import server.galaxyunderchaos.sound.ModSounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.lightsaber.BladeModifierCrystal;
import server.galaxyunderchaos.lightsaber.LightsaberIdHelper;
import server.galaxyunderchaos.lightsaber.ModularLightsaberData;

public class LightsaberItem extends SwordItem {
    private static final String ACTIVE_TAG = "LightsaberActive";
    private final String bladeColor;
    @Nullable
    private final String explicitHiltId;

    public LightsaberItem(String bladeColor, Item.Properties properties) {
        this(bladeColor, null, properties);
    }

    public LightsaberItem(String bladeColor, @Nullable String hiltId, Item.Properties properties) {
        super(ModToolTiers.LIGHTSABER, 2, -2.4F, properties);
        this.bladeColor = bladeColor;
        this.explicitHiltId = hiltId;
    }

    public boolean isActive(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(ACTIVE_TAG);
    }

    public void setActive(ItemStack stack, boolean active, Level level, @Nullable Player player) {
        if (isActive(stack) != active) {
            stack.getOrCreateTag().putBoolean(ACTIVE_TAG, active);

            double x = player != null ? player.getX() : 0.0D;
            double y = player != null ? player.getY() : 0.0D;
            double z = player != null ? player.getZ() : 0.0D;

            level.playSound(null, x, y, z,
                    active ? ModSounds.LIGHTSABER_TURN_ON.get() : ModSounds.LIGHTSABER_TURN_OFF.get(),
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!isActive(stack)) {
            return InteractionResultHolder.pass(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return isActive(stack);
    }

    public String getTextureLocation(ItemStack stack) {
        String resolvedBladeColor = getBladeColor(stack);
        return isActive(stack) ?
                "galaxyunderchaos:item/" + resolvedBladeColor + "_blade" :
                "galaxyunderchaos:item/" + getHiltId(stack) + "_hilt";
    }

    public static int getLightLevel(ItemStack stack) {
        return (stack.getItem() instanceof LightsaberItem ls && ls.isActive(stack)) ? 15 : 0;
    }
    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (isActive(stack) && level.isClientSide && entity instanceof Player player) {
            level.playSound(
                    player,
                    player.getX(), player.getY(), player.getZ(),
                    ModSounds.LIGHTSABER_SWING.get(),
                    SoundSource.PLAYERS,
                    0.3F,
                    0.5F
            );
        }
        return super.mineBlock(stack, level, state, pos, entity);
    }
    public String getHilt(ItemStack stack) {
        return getHiltId(stack);
    }

    public String getHiltId(ItemStack stack) {
        String fallback = explicitHiltId != null ? explicitHiltId : LightsaberIdHelper.resolveHiltId(stack.getItem());
        return ModularLightsaberData.getPrimaryHiltId(stack, fallback);
    }

    public static String getBladeColor(ItemStack stack) {
        if (stack.getItem() instanceof LightsaberItem saber) {
            return ModularLightsaberData.getBladeColor(stack, saber.bladeColor);
        }
        return ModularLightsaberData.getBladeColor(stack, "white");
    }


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slot, isSelected);
        if (level.isClientSide && entity instanceof Player player && isSelected && isActive(stack) && entity.tickCount % 40 == 0) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(),
                    ModSounds.LIGHTSABER_IDLE.get(),
                    SoundSource.PLAYERS, 0.3F, 0.5F);
        }
    }
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!isActive(stack)) {
            setActive(stack, true, attacker.level(), attacker instanceof Player player ? player : null);
        }
        if (!attacker.level().isClientSide) {
            attacker.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                    ModSounds.LIGHTSABER_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        if (!attacker.level().isClientSide && target.isDeadOrDying() && attacker instanceof ServerPlayer player) {
            ServerLevel serverLevel = (ServerLevel) target.level();

            ResourceLocation lootTableLocation = target.getType().getDefaultLootTable();
            LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(lootTableLocation);

            LootParams.Builder lootParams = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.ORIGIN, target.position())
                    .withParameter(LootContextParams.THIS_ENTITY, target)
                    .withParameter(LootContextParams.DAMAGE_SOURCE, target.getLastDamageSource());
            List<ItemStack> drops = lootTable.getRandomItems(lootParams.create(LootContextParamSets.ENTITY));

            List<ItemStack> cookedDrops = new ArrayList<>();
            for (ItemStack drop : drops) {
                Item cookedItem = getCookedVersion(drop.getItem());
                if (cookedItem != null) {
                    cookedDrops.add(new ItemStack(cookedItem, drop.getCount()));
                } else {
                    cookedDrops.add(drop);
                }
            }

            target.remove(Entity.RemovalReason.DISCARDED);

            for (ItemStack cookedDrop : cookedDrops) {
                target.spawnAtLocation(cookedDrop);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }
    private Item getCookedVersion(Item rawFood) {
        Map<Item, Item> cookingMap = Map.of(
                Items.BEEF, Items.COOKED_BEEF,
                Items.CHICKEN, Items.COOKED_CHICKEN,
                Items.PORKCHOP, Items.COOKED_PORKCHOP,
                Items.MUTTON, Items.COOKED_MUTTON,
                Items.RABBIT, Items.COOKED_RABBIT,
                Items.SALMON, Items.COOKED_SALMON,
                Items.COD, Items.COOKED_COD
        );
        return cookingMap.getOrDefault(rawFood, null);
    }
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal("Blade color: " + getBladeColor(stack)));
        tooltip.add(Component.literal("Emitter: " + ModularLightsaberData.getPartFamily(stack, server.galaxyunderchaos.lightsaber.LightsaberPartType.EMITTER, getHiltId(stack))));
        tooltip.add(Component.literal("Switch: " + ModularLightsaberData.getPartFamily(stack, server.galaxyunderchaos.lightsaber.LightsaberPartType.SWITCH_SECTION, getHiltId(stack))));
        tooltip.add(Component.literal("Grip: " + ModularLightsaberData.getPartFamily(stack, server.galaxyunderchaos.lightsaber.LightsaberPartType.GRIP, getHiltId(stack))));
        tooltip.add(Component.literal("Pommel: " + ModularLightsaberData.getPartFamily(stack, server.galaxyunderchaos.lightsaber.LightsaberPartType.POMMEL, getHiltId(stack))));
        java.util.EnumSet<BladeModifierCrystal> modifiers = ModularLightsaberData.getBladeModifiers(stack);
        if (!modifiers.isEmpty()) {
            tooltip.add(Component.literal("Modifiers: " + modifiers.stream()
                    .map(BladeModifierCrystal::getSerializedName)
                    .collect(Collectors.joining(", "))));
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer customRenderer = new ModItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return customRenderer;
            }
        });
    }
}