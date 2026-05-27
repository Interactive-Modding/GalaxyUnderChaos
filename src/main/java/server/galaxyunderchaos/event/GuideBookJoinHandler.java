package server.galaxyunderchaos.event;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.item.GalacticGuideBookItem;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GuideBookJoinHandler {
    private static final String ROOT = "GUCGuideBook";
    private static final String GIVEN = "GivenStarterGuide";

    private GuideBookJoinHandler() {}

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        CompoundTag tag = player.getPersistentData();
        CompoundTag root = tag.getCompound(ROOT);

        // Always scan first so older version-1 guide books are upgraded to the
        // clickable sequential version even for players who already received one.
        boolean hasGuide = hasGuideBook(player);
        if (root.getBoolean(GIVEN)) {
            return;
        }

        if (hasGuide) {
            root.putBoolean(GIVEN, true);
            tag.put(ROOT, root);
            return;
        }

        ItemStack guide = GalacticGuideBookItem.createGuideStack();
        boolean added = player.getInventory().add(guide);
        if (!added) {
            player.drop(guide, false);
        }

        root.putBoolean(GIVEN, true);
        tag.put(ROOT, root);
        player.displayClientMessage(Component.literal("You received the Galaxy Under Chaos Guide Book. Right-click it for sides, structures, ships, Force training, and saber help.").withStyle(ChatFormatting.AQUA), false);
    }

    private static boolean hasGuideBook(ServerPlayer player) {
        for (ItemStack stack : player.getInventory().items) {
            if (GalacticGuideBookItem.isGuideBook(stack)) {
                // Repair old custom/vanilla guide stacks so they remain readable after updates.
                GalacticGuideBookItem.ensureGuideTag(stack);
                return true;
            }
        }
        for (ItemStack stack : player.getInventory().offhand) {
            if (GalacticGuideBookItem.isGuideBook(stack)) {
                GalacticGuideBookItem.ensureGuideTag(stack);
                return true;
            }
        }
        return false;
    }
}
