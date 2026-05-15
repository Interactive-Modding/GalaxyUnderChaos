package server.galaxyunderchaos.entity.forceuser;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import server.galaxyunderchaos.force.ForceCapability;
import server.galaxyunderchaos.force.ForceCapabilityManager;
import server.galaxyunderchaos.force.ForcePower;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForceSide;

public final class ForceTrainingManager {
    private static final String ROOT = "GUCForceTraining";
    private static final String SITH_STUDENTS = "SithStudents";
    private static final String JEDI_STUDENTS = "JediStudents";
    private static final String TRAINED_STUDENT = "TrainedStudentToKnight";

    public static final int MAX_SITH_APPRENTICES = 5;
    public static final int MAX_JEDI_PADAWANS = 2;

    private ForceTrainingManager() {}

    public static boolean canAccept(ServerPlayer player, ForceUserRole role) {
        CompoundTag tag = data(player);
        if (role.isApprentice()) {
            return tag.getInt(SITH_STUDENTS) < MAX_SITH_APPRENTICES;
        }
        if (role.isPadawan()) {
            return tag.getInt(JEDI_STUDENTS) < MAX_JEDI_PADAWANS;
        }
        return false;
    }

    public static boolean tryAccept(ServerPlayer player, ForceUserEntity student) {
        ForceUserRole role = student.getForceUserRole();
        ForceSide committed = player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(ForceCapability::getCommittedSide)
                .orElse(ForceSide.UNIVERSAL);

        if (role.isApprentice() && committed != ForceSide.DARK && committed != ForceSide.NEUTRAL) {
            player.displayClientMessage(Component.literal("Only dark-side or neutral players can take a Sith apprentice."), true);
            return false;
        }
        if (role.isPadawan() && committed != ForceSide.LIGHT && committed != ForceSide.NEUTRAL) {
            player.displayClientMessage(Component.literal("Only light-side or neutral players can take a Jedi padawan."), true);
            return false;
        }
        if (!canAccept(player, role)) {
            player.displayClientMessage(Component.literal(role.isApprentice()
                    ? "You already have five Sith apprentices."
                    : "You already have two Jedi padawans."), true);
            return false;
        }

        CompoundTag tag = data(player);
        if (role.isApprentice()) {
            tag.putInt(SITH_STUDENTS, tag.getInt(SITH_STUDENTS) + 1);
        } else if (role.isPadawan()) {
            tag.putInt(JEDI_STUDENTS, tag.getInt(JEDI_STUDENTS) + 1);
        }
        student.bindToMaster(player);
        player.displayClientMessage(Component.literal(student.getDisplayName().getString() + " now follows your teachings."), false);
        return true;
    }

    public static void release(ServerPlayer player, ForceUserRole role) {
        CompoundTag tag = data(player);
        if (role.isApprentice()) {
            tag.putInt(SITH_STUDENTS, Math.max(0, tag.getInt(SITH_STUDENTS) - 1));
        } else if (role.isPadawan()) {
            tag.putInt(JEDI_STUDENTS, Math.max(0, tag.getInt(JEDI_STUDENTS) - 1));
        }
    }

    public static void markStudentFullyTrained(ServerPlayer player) {
        data(player).putBoolean(TRAINED_STUDENT, true);
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.setHasTrainedStudent(true);
            cap.unlockPower(ForcePower.FORCE_LEVEL3);
            cap.unlockPower(ForcePower.FORCE_LEVEL4);
            cap.setCurrentForce(cap.getMaxForce());
            ForceCapabilityManager.sync(player);
        });
        player.displayClientMessage(Component.literal("Your completed student training has unlocked Force Level III and IV knowledge."), false);
    }

    public static boolean hasTrainedStudent(ServerPlayer player) {
        return data(player).getBoolean(TRAINED_STUDENT)
                || player.getCapability(ForceProvider.FORCE_CAPABILITY).map(ForceCapability::hasTrainedStudent).orElse(false);
    }

    private static CompoundTag data(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT)) {
            persistent.put(ROOT, new CompoundTag());
        }
        return persistent.getCompound(ROOT);
    }
}
