package server.galaxyunderchaos.entity.forceuser;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import server.galaxyunderchaos.force.ForceCapability;
import server.galaxyunderchaos.force.ForceCapabilityManager;
import server.galaxyunderchaos.force.ForcePower;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForceRenunciationManager;
import server.galaxyunderchaos.force.ForceSide;

public final class ForceTrainingManager {
    private static final String ROOT = "GUCForceTraining";
    private static final String SITH_STUDENTS = "SithStudents";
    private static final String JEDI_STUDENTS = "JediStudents";
    private static final String NEUTRAL_STUDENTS = "NeutralStudents";
    private static final String TRAINED_STUDENT = "TrainedStudentToKnight";
    private static final String FORCE_MENTOR = "ForceMentor";
    private static final String FORCE_MENTOR_SIDE = "ForceMentorSide";
    private static final String FORCE_MENTOR_UUID = "ForceMentorUuid";
    private static final String JEDI_MENTOR_TASKS = "JediMentorTasks";
    private static final String SITH_MENTOR_TASKS = "SithMentorTasks";
    private static final String NEUTRAL_MENTOR_TASKS = "NeutralMentorTasks";
    private static final String JEDI_KNIGHT = "JediKnightStanding";
    /**
     * Historical key name kept for save compatibility. For Sith players this now means
     * "mentor questline complete / eligible to claim an apprentice", not Sith Lord.
     */
    private static final String SITH_KNIGHT = "SithKnightStanding";
    private static final String NEUTRAL_KNIGHT = "NeutralKnightStanding";
    private static final String SITH_APPRENTICE_STANDING = "SithApprenticeStanding";
    private static final String SITH_LORD_ASCENSION_READY = "SithLordAscensionReady";
    private static final String SITH_LORD_ASCENSION_STUDENT = "SithLordAscensionStudent";

    public static final int MAX_SITH_APPRENTICES = 5;
    public static final int MAX_JEDI_PADAWANS = 2;
    public static final int MAX_NEUTRAL_PADAWANS = 2;
    public static final int QUESTS_TO_KNIGHT = 9;
    public static final int STUDENT_QUESTS_TO_GRADUATE = 5;
    public static final int APPRENTICE_QUESTS_TO_READY = STUDENT_QUESTS_TO_GRADUATE;

    private ForceTrainingManager() {}

    public static boolean canAccept(ServerPlayer player, ForceUserRole role) {
        CompoundTag tag = data(player);
        if (hasStudentsOfOtherTradition(player, role.side().toCapabilitySide())) {
            return false;
        }
        if (!hasKnightStandingForStudent(player, role)) {
            return false;
        }
        if (role.isApprentice()) {
            return tag.getInt(SITH_STUDENTS) < MAX_SITH_APPRENTICES;
        }
        if (role.isPadawan() && role.side().isNeutral()) {
            return tag.getInt(NEUTRAL_STUDENTS) < MAX_NEUTRAL_PADAWANS;
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
        ForceSide studentSide = role.side().toCapabilitySide();

        if (committed != ForceSide.UNIVERSAL && committed != studentSide) {
            player.displayClientMessage(Component.literal("You cannot train a " + sideLabel(studentSide) + " student while committed to " + sideLabel(committed) + ". Students are exclusive by allegiance."), true);
            return false;
        }
        if (hasStudentsOfOtherTradition(player, studentSide)) {
            player.displayClientMessage(Component.literal("You already train a student from another Force tradition. Jedi, Sith, and neutral students are exclusive."), true);
            return false;
        }
        if (!hasKnightStandingForStudent(player, role)) {
            player.displayClientMessage(Component.literal("You are not ready to train a student. Complete " + QUESTS_TO_KNIGHT + " mentor quests first" + (studentSide == ForceSide.DARK ? " and claim a Sith apprentice." : " and earn " + knightTitle(studentSide) + " standing.") ), true);
            player.displayClientMessage(Component.literal(mentorProgressLine(player, studentSide)), false);
            return false;
        }
        if (!canAccept(player, role)) {
            player.displayClientMessage(Component.literal(limitMessage(role)), true);
            return false;
        }

        CompoundTag tag = data(player);
        if (role.isApprentice()) {
            tag.putInt(SITH_STUDENTS, tag.getInt(SITH_STUDENTS) + 1);
            if (studentSide == ForceSide.DARK && hasMentorQuestlineComplete(player, ForceSide.DARK)) {
                markSithApprenticeStanding(player, student.getDisplayName().getString());
            }
        } else if (role.isPadawan() && role.side().isNeutral()) {
            tag.putInt(NEUTRAL_STUDENTS, tag.getInt(NEUTRAL_STUDENTS) + 1);
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
        } else if (role.isPadawan() && role.side().isNeutral()) {
            tag.putInt(NEUTRAL_STUDENTS, Math.max(0, tag.getInt(NEUTRAL_STUDENTS) - 1));
        } else if (role.isPadawan()) {
            tag.putInt(JEDI_STUDENTS, Math.max(0, tag.getInt(JEDI_STUDENTS) - 1));
        }
    }

    public static void releaseForceMentor(ServerPlayer player) {
        CompoundTag tag = data(player);
        tag.putBoolean(FORCE_MENTOR, false);
        tag.remove(FORCE_MENTOR_SIDE);
        tag.remove(FORCE_MENTOR_UUID);
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.setHasForceMentor(false);
            ForceCapabilityManager.sync(player);
        });
    }

    /**
     * Returns true when the player has a saved mentor bond. A currently unloaded
     * mentor is treated as still valid so chunk/dimension unloading does not wipe
     * rank progression. Actual mentor death releases the bond from ForceUserEntity#die
     * while leaving completed trials and rank flags intact.
     */
    public static boolean hasForceMentor(ServerPlayer player) {
        CompoundTag tag = data(player);
        boolean taggedMentor = tag.getBoolean(FORCE_MENTOR);
        boolean capabilityMentor = player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(ForceCapability::hasForceMentor)
                .orElse(false);

        if (!taggedMentor && !capabilityMentor) {
            return false;
        }

        if (!tag.hasUUID(FORCE_MENTOR_UUID)) {
            releaseForceMentor(player);
            return false;
        }

        Entity entity = findEntity(player, tag.getUUID(FORCE_MENTOR_UUID));
        if (entity == null) {
            // Do not erase a mentor bond just because the mentor's chunk/dimension is not loaded.
            // Actual mentor deaths release the bond from ForceUserEntity#die while preserving rank/trial data.
            return true;
        }
        if (entity instanceof ForceUserEntity mentor && mentor.isAlive() && mentor.isBoundTo(player) && mentor.isForceMentorBond()) {
            return true;
        }

        releaseForceMentor(player);
        return false;
    }

    private static Entity findEntity(ServerPlayer player, java.util.UUID uuid) {
        if (player.server == null || uuid == null) {
            return null;
        }
        for (ServerLevel level : player.server.getAllLevels()) {
            Entity entity = level.getEntity(uuid);
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }

    public static boolean tryChooseForceMentor(ServerPlayer player, ForceUserEntity mentor) {
        if (!mentor.canUnlockForceSensitivityForPlayer()) {
            player.displayClientMessage(Component.literal("This Force user cannot mentor your first steps in the Force."), true);
            return false;
        }
        if (hasForceMentor(player)) {
            player.displayClientMessage(Component.literal("You already have a Force mentor. If that mentor dies or is released, you may choose another."), true);
            return false;
        }

        ForceSide mentorSide = mentor.getForceUserSide().toCapabilitySide();
        ForceSide committed = player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(ForceCapability::getCommittedSide)
                .orElse(ForceSide.UNIVERSAL);
        if (committed != ForceSide.UNIVERSAL && committed != mentorSide) {
            ForceRenunciationManager.request(player, mentorSide, committed, mentor.blockPosition(), mentor.getDisplayName().getString());
            player.displayClientMessage(Component.literal("You must renounce " + sideLabel(committed) + " before this " + sideLabel(mentorSide) + " teacher will take you on."), true);
            return false;
        }
        CompoundTag tag = data(player);
        tag.putBoolean(FORCE_MENTOR, true);
        tag.putString(FORCE_MENTOR_SIDE, mentorSide.name());
        tag.putUUID(FORCE_MENTOR_UUID, mentor.getUUID());
        mentor.bindAsForceMentor(player);
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.setHasForceMentor(true);
            if (cap.getCommittedSide() == ForceSide.UNIVERSAL) {
                cap.setCommittedSide(mentorSide);
            }
            ForceCapabilityManager.sync(player);
        });
        boolean alreadySensitive = player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(cap -> cap.hasPower(ForcePower.FORCE_SENSITIVITY))
                .orElse(false);
        player.displayClientMessage(Component.literal(mentor.getDisplayName().getString() + " is now your Force mentor" + (alreadySensitive ? ". Continue their trials to advance your training." : ". You may unlock Force Sensitivity at a holocron.") ), false);
        player.displayClientMessage(Component.literal("Your mentor questline has " + QUESTS_TO_KNIGHT + " trials. Jedi/neutral paths promote from Padawan to Knight after the questline. Sith acolytes must finish the trials, claim an apprentice, then later face their master."), false);
        ForceQuestInventory.getOrCreateActiveQuest(player, mentor);
        return true;
    }

    public static int recordMentorQuestComplete(ServerPlayer player, ForceSide side, String teacherName) {
        CompoundTag tag = data(player);
        String key = mentorTaskKey(side);
        int completed = Math.min(QUESTS_TO_KNIGHT, tag.getInt(key) + 1);
        tag.putInt(key, completed);
        if (completed >= QUESTS_TO_KNIGHT && !hasMentorQuestlineComplete(player, side)) {
            markMentorQuestlineComplete(player, side, teacherName);
        }
        return completed;
    }

    public static boolean hasKnightStandingForStudent(ServerPlayer player, ForceUserRole role) {
        ForceSide side = role.side().toCapabilitySide();
        return hasMentorQuestlineComplete(player, side);
    }

    /** True once the mentor questline is complete. For Light/Neutral this is also Knight standing. */
    public static boolean hasKnightStanding(ServerPlayer player, ForceSide side) {
        return hasMentorQuestlineComplete(player, side);
    }

    public static boolean hasMentorQuestlineComplete(ServerPlayer player, ForceSide side) {
        CompoundTag tag = data(player);
        return switch (side) {
            case LIGHT -> tag.getBoolean(JEDI_KNIGHT);
            case DARK -> tag.getBoolean(SITH_KNIGHT);
            case NEUTRAL -> tag.getBoolean(NEUTRAL_KNIGHT);
            default -> false;
        };
    }

    public static int getMentorQuestCount(ServerPlayer player, ForceSide side) {
        return Math.min(QUESTS_TO_KNIGHT, data(player).getInt(mentorTaskKey(side)));
    }

    public static boolean hasSithApprenticeStanding(ServerPlayer player) {
        return data(player).getBoolean(SITH_APPRENTICE_STANDING);
    }

    public static boolean isSithLordAscensionReady(ServerPlayer player) {
        return data(player).getBoolean(SITH_LORD_ASCENSION_READY);
    }

    public static String mentorProgressLine(ServerPlayer player, ForceSide side) {
        CompoundTag tag = data(player);
        int completed = Math.min(QUESTS_TO_KNIGHT, tag.getInt(mentorTaskKey(side)));
        String status = hasMentorQuestlineComplete(player, side) ? "complete" : completed + "/" + QUESTS_TO_KNIGHT;
        return mentorLineTitle(side) + " mentor trials: " + status;
    }

    private static void markMentorQuestlineComplete(ServerPlayer player, ForceSide side, String teacherName) {
        CompoundTag tag = data(player);
        switch (side) {
            case LIGHT -> tag.putBoolean(JEDI_KNIGHT, true);
            case DARK -> tag.putBoolean(SITH_KNIGHT, true);
            case NEUTRAL -> tag.putBoolean(NEUTRAL_KNIGHT, true);
            default -> { return; }
        }
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.unlockPower(ForcePower.FORCE_LEVEL2);
            cap.addAlignmentPoints(side, 20);
            cap.setCurrentForce(cap.getMaxForce());
            ForceCapabilityManager.sync(player);
        });
        if (side == ForceSide.DARK) {
            player.displayClientMessage(Component.literal(teacherName + ": Your nine trials are complete. You remain an Acolyte until you claim and train a Sith apprentice."), false);
        } else {
            player.displayClientMessage(Component.literal(teacherName + ": Your nine trials are complete. You now carry " + knightTitle(side) + " standing and may train a student of this path."), false);
        }
        PlayerForceIdentity.applyTitle(player);
    }

    private static void markSithApprenticeStanding(ServerPlayer player, String studentName) {
        CompoundTag tag = data(player);
        if (tag.getBoolean(SITH_APPRENTICE_STANDING)) {
            return;
        }
        tag.putBoolean(SITH_APPRENTICE_STANDING, true);
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.addAlignmentPoints(ForceSide.DARK, 15);
            ForceCapabilityManager.sync(player);
        });
        PlayerForceIdentity.applyTitle(player);
        player.displayClientMessage(Component.literal("You have claimed " + studentName + " and risen from Acolyte to Sith Apprentice. Dark side eyes are now unlocked for your Force identity."), false);
    }

    public static boolean isDarkLord(ServerPlayer player) {
        if (player == null) {
            return false;
        }
        ForceSide side = player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(ForceCapability::getCommittedSide)
                .orElse(ForceSide.UNIVERSAL);
        return side == ForceSide.DARK && hasTrainedStudent(player);
    }

    public static boolean shouldBeRecognizedAsMaster(ServerPlayer player, ForceSide npcSide) {
        if (player == null || npcSide == null || npcSide == ForceSide.UNIVERSAL) {
            return false;
        }
        ForceSide playerSide = player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(ForceCapability::getCommittedSide)
                .orElse(ForceSide.UNIVERSAL);
        return playerSide == npcSide && hasTrainedStudent(player);
    }

    public static void markSithAscensionReady(ServerPlayer player, ForceUserEntity apprentice) {
        CompoundTag tag = data(player);
        tag.putBoolean(SITH_LORD_ASCENSION_READY, true);
        if (apprentice != null) {
            tag.putUUID(SITH_LORD_ASCENSION_STUDENT, apprentice.getUUID());
        }
        player.displayClientMessage(Component.literal("Your apprentice has completed 5 trials and is ready to stand alone. To become Sith Lord, defeat your bonded Sith master yourself."), false);
    }

    public static boolean tryCompleteSithLordAscension(ServerPlayer player, ForceUserEntity mentor, Entity killer) {
        if (!(killer instanceof ServerPlayer serverPlayer) || serverPlayer != player) {
            return false;
        }
        if (!isSithLordAscensionReady(player)) {
            player.displayClientMessage(Component.literal("You defeated your master, but your apprentice is not ready. The Sith line does not recognize your claim yet."), false);
            return false;
        }
        if (mentor == null || !mentor.isForceMentorBond() || !mentor.isBoundTo(player) || !mentor.getForceUserSide().isDark()) {
            return false;
        }
        data(player).putBoolean(TRAINED_STUDENT, true);
        data(player).putBoolean(SITH_LORD_ASCENSION_READY, false);
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.setHasTrainedStudent(true);
            cap.unlockPower(ForcePower.FORCE_LEVEL3);
            cap.unlockPower(ForcePower.FORCE_LEVEL4);
            cap.addAlignmentPoints(ForceSide.DARK, 35);
            cap.setCurrentForce(cap.getMaxForce());
            ForceCapabilityManager.sync(player);
        });
        PlayerForceIdentity.applyTitle(player);
        startSithApprenticeChallenge(player);
        player.displayClientMessage(Component.literal("You have defeated your master after raising an apprentice. You are now Sith Lord."), false);
        return true;
    }

    public static void startSithApprenticeChallenge(ServerPlayer player) {
        if (player == null || player.server == null) {
            return;
        }
        CompoundTag tag = data(player);
        if (!tag.hasUUID(SITH_LORD_ASCENSION_STUDENT)) {
            return;
        }
        Entity entity = findEntity(player, tag.getUUID(SITH_LORD_ASCENSION_STUDENT));
        if (entity instanceof ForceUserEntity apprentice && apprentice.isAlive()) {
            apprentice.beginSithRivalChallenge(player);
        }
    }

    public static void markStudentFullyTrained(ServerPlayer player) {
        data(player).putBoolean(TRAINED_STUDENT, true);
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.setHasTrainedStudent(true);
            cap.unlockPower(ForcePower.FORCE_LEVEL3);
            cap.unlockPower(ForcePower.FORCE_LEVEL4);
            ForceSide side = cap.getCommittedSide() == ForceSide.UNIVERSAL ? ForceSide.NEUTRAL : cap.getCommittedSide();
            cap.addAlignmentPoints(side, side == ForceSide.DARK ? 30 : 20);
            cap.setCurrentForce(cap.getMaxForce());
            ForceCapabilityManager.sync(player);
        });
        PlayerForceIdentity.applyTitle(player);
        player.displayClientMessage(Component.literal("Your completed student training has unlocked Force Level III and IV knowledge."), false);
    }

    public static boolean hasTrainedStudent(ServerPlayer player) {
        return data(player).getBoolean(TRAINED_STUDENT)
                || player.getCapability(ForceProvider.FORCE_CAPABILITY).map(ForceCapability::hasTrainedStudent).orElse(false);
    }

    public static boolean hasStudentsOfOtherTradition(ServerPlayer player, ForceSide side) {
        CompoundTag tag = data(player);
        if (side != ForceSide.DARK && tag.getInt(SITH_STUDENTS) > 0) return true;
        if (side != ForceSide.LIGHT && tag.getInt(JEDI_STUDENTS) > 0) return true;
        if (side != ForceSide.NEUTRAL && tag.getInt(NEUTRAL_STUDENTS) > 0) return true;
        return false;
    }

    public static void clearProgressForSide(ServerPlayer player, ForceSide side) {
        if (player == null || side == null || side == ForceSide.UNIVERSAL) {
            return;
        }
        CompoundTag tag = data(player);
        ForceSide mentorSide = parseSide(tag.getString(FORCE_MENTOR_SIDE), ForceSide.UNIVERSAL);
        if (mentorSide == side) {
            tag.putBoolean(FORCE_MENTOR, false);
            tag.remove(FORCE_MENTOR_SIDE);
            tag.remove(FORCE_MENTOR_UUID);
            player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> cap.setHasForceMentor(false));
        }
        switch (side) {
            case LIGHT -> {
                tag.putInt(JEDI_STUDENTS, 0);
                tag.putInt(JEDI_MENTOR_TASKS, 0);
                tag.putBoolean(JEDI_KNIGHT, false);
            }
            case DARK -> {
                tag.putInt(SITH_STUDENTS, 0);
                tag.putInt(SITH_MENTOR_TASKS, 0);
                tag.putBoolean(SITH_KNIGHT, false);
                tag.putBoolean(SITH_APPRENTICE_STANDING, false);
                tag.putBoolean(SITH_LORD_ASCENSION_READY, false);
                tag.remove(SITH_LORD_ASCENSION_STUDENT);
            }
            case NEUTRAL -> {
                tag.putInt(NEUTRAL_STUDENTS, 0);
                tag.putInt(NEUTRAL_MENTOR_TASKS, 0);
                tag.putBoolean(NEUTRAL_KNIGHT, false);
            }
            default -> { }
        }
        tag.putBoolean(TRAINED_STUDENT, false);
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.setHasTrainedStudent(false);
            ForceCapabilityManager.sync(player);
        });
        PlayerForceIdentity.applyTitle(player);
    }

    public static String studentSummary(ServerPlayer player) {
        CompoundTag tag = data(player);
        int sith = tag.getInt(SITH_STUDENTS);
        int jedi = tag.getInt(JEDI_STUDENTS);
        int neutral = tag.getInt(NEUTRAL_STUDENTS);
        String rank = "Standing — "
                + mentorProgressLine(player, ForceSide.LIGHT) + " | "
                + mentorProgressLine(player, ForceSide.DARK) + " | "
                + mentorProgressLine(player, ForceSide.NEUTRAL);
        if (sith + jedi + neutral <= 0) {
            return rank + " | Apprentice: none";
        }
        return rank + " | Students — Jedi: " + jedi + ", Sith: " + sith + ", Neutral: " + neutral;
    }

    private static String mentorTaskKey(ForceSide side) {
        return switch (side) {
            case DARK -> SITH_MENTOR_TASKS;
            case NEUTRAL -> NEUTRAL_MENTOR_TASKS;
            default -> JEDI_MENTOR_TASKS;
        };
    }

    private static String limitMessage(ForceUserRole role) {
        if (role.isApprentice()) return "You already have five Sith apprentices.";
        if (role.side().isNeutral()) return "You already have two neutral padawans.";
        return "You already have two Jedi padawans.";
    }

    private static String mentorLineTitle(ForceSide side) {
        return switch (side) {
            case DARK -> "Sith acolyte";
            case LIGHT -> "Jedi Knight";
            case NEUTRAL -> "Neutral Knight";
            default -> "Force";
        };
    }

    private static String knightTitle(ForceSide side) {
        return switch (side) {
            case LIGHT -> "Jedi Knight";
            case DARK -> "Sith Apprentice";
            case NEUTRAL -> "Neutral Knight";
            default -> "Force Knight";
        };
    }

    private static String sideLabel(ForceSide side) {
        return switch (side) {
            case LIGHT -> "the Jedi path";
            case DARK -> "the Sith path";
            case NEUTRAL -> "the neutral path";
            default -> "no path";
        };
    }

    private static ForceSide parseSide(String value, ForceSide fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return ForceSide.valueOf(value);
        } catch (IllegalArgumentException ignored) {
            return fallback;
        }
    }

    private static CompoundTag data(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT)) {
            persistent.put(ROOT, new CompoundTag());
        }
        return persistent.getCompound(ROOT);
    }
}
