package server.galaxyunderchaos.entity.forceuser;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import server.galaxyunderchaos.force.ForceCapability;
import server.galaxyunderchaos.force.ForceCapabilityManager;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForceSide;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Persistent player-side quest ledger for Force mentors.
 *
 * This is intentionally NBT/data driven instead of a hard-coded GUI menu. The
 * player gets nine quest slots that survive logout/death. Mentors fill one empty
 * or completed slot at a time; the same storage can later be wired into a full
 * screen/container without changing quest progression logic.
 */
@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForceQuestInventory {
    private static final String ROOT = "GUCForceQuestInventory";
    private static final String SLOTS = "Slots";
    private static final String PENDING_DIALOGUE = "PendingDialogue";
    private static final int SLOT_COUNT = 9;

    private static final String TYPE_READ_HOLOBOOK = "read_holobook";
    private static final String TYPE_RECOVER_DATACRONS = "recover_datacrons";
    private static final String TYPE_GATHER_SABER_PARTS = "gather_saber_parts";
    private static final String TYPE_DEFEAT_HOSTILES = "defeat_hostiles";
    private static final String TYPE_DEFEAT_OPPOSING_FORCE_USERS = "defeat_opposing_force_users";
    private static final String TYPE_VISIT_ANCIENT_SITE = "visit_ancient_site";
    private static final String TYPE_MEDITATE_WITH_MENTOR = "meditate_with_mentor";
    private static final String TYPE_DEFEND_INNOCENTS = "defend_innocents";
    private static final String TYPE_GATHER_HOLOBOOKS = "gather_holobooks";
    private static final String TYPE_BUILD_SABER_READY = "build_saber_ready";
    private static final String TYPE_APPRENTICE_FIND_STRUCTURE = "apprentice_find_structure";
    private static final String TYPE_APPRENTICE_DEFEAT_JEDI = "apprentice_defeat_jedi";

    private static final String STAGE_MENTOR = "mentor";
    private static final String STAGE_STUDENT = "student";
    private static final String STAGE_GENERAL = "general";

    private static final String[] LIGHT_QUEST_TYPES = {
            TYPE_READ_HOLOBOOK,
            TYPE_RECOVER_DATACRONS,
            TYPE_GATHER_SABER_PARTS,
            TYPE_DEFEAT_HOSTILES,
            TYPE_DEFEAT_OPPOSING_FORCE_USERS,
            TYPE_VISIT_ANCIENT_SITE,
            TYPE_MEDITATE_WITH_MENTOR,
            TYPE_DEFEND_INNOCENTS,
            TYPE_GATHER_HOLOBOOKS,
            TYPE_BUILD_SABER_READY
    };

    private static final String[] NEUTRAL_QUEST_TYPES = {
            TYPE_READ_HOLOBOOK,
            TYPE_RECOVER_DATACRONS,
            TYPE_GATHER_SABER_PARTS,
            TYPE_DEFEAT_HOSTILES,
            TYPE_DEFEAT_OPPOSING_FORCE_USERS,
            TYPE_VISIT_ANCIENT_SITE,
            TYPE_MEDITATE_WITH_MENTOR,
            TYPE_DEFEND_INNOCENTS,
            TYPE_GATHER_HOLOBOOKS,
            TYPE_BUILD_SABER_READY
    };

    private static final String[] DARK_QUEST_TYPES = {
            TYPE_READ_HOLOBOOK,
            TYPE_RECOVER_DATACRONS,
            TYPE_GATHER_SABER_PARTS,
            TYPE_DEFEAT_HOSTILES,
            TYPE_DEFEAT_OPPOSING_FORCE_USERS,
            TYPE_VISIT_ANCIENT_SITE,
            TYPE_MEDITATE_WITH_MENTOR,
            TYPE_GATHER_HOLOBOOKS,
            TYPE_BUILD_SABER_READY
    };

    private static final String[] APPRENTICE_QUEST_TYPES = {
            TYPE_APPRENTICE_FIND_STRUCTURE,
            TYPE_APPRENTICE_DEFEAT_JEDI
    };

    private static final String[] LIGHT_STUDENT_QUEST_TYPES = {
            TYPE_VISIT_ANCIENT_SITE,
            TYPE_DEFEAT_HOSTILES,
            TYPE_READ_HOLOBOOK,
            TYPE_DEFEND_INNOCENTS,
            TYPE_MEDITATE_WITH_MENTOR
    };

    private static final String[] NEUTRAL_STUDENT_QUEST_TYPES = {
            TYPE_VISIT_ANCIENT_SITE,
            TYPE_GATHER_HOLOBOOKS,
            TYPE_DEFEAT_HOSTILES,
            TYPE_RECOVER_DATACRONS,
            TYPE_MEDITATE_WITH_MENTOR
    };

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        ForceUserEntity companionKiller = killingCompanion(event.getSource());
        ServerPlayer player = killingPlayer(event.getSource(), companionKiller);
        if (player == null) {
            return;
        }

        ForceQuestInventory.recordKill(player, event.getEntity(), companionKiller);
    }

    private static ServerPlayer killingPlayer(DamageSource source, ForceUserEntity companionKiller) {
        Entity attacker = source.getEntity();
        if (attacker instanceof ServerPlayer player) {
            return player;
        }
        if (attacker instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer player) {
            return player;
        }
        Entity direct = source.getDirectEntity();
        if (direct instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer player) {
            return player;
        }
        if (companionKiller != null && companionKiller.getBoundMasterUuid() != null && companionKiller.level() instanceof ServerLevel serverLevel) {
            return serverLevel.getServer().getPlayerList().getPlayer(companionKiller.getBoundMasterUuid());
        }
        return null;
    }

    private static ForceUserEntity killingCompanion(DamageSource source) {
        Entity attacker = source.getEntity();
        if (attacker instanceof ForceUserEntity forceUser && forceUser.getBoundMasterUuid() != null) {
            return forceUser;
        }
        Entity direct = source.getDirectEntity();
        if (direct instanceof Projectile projectile && projectile.getOwner() instanceof ForceUserEntity forceUser && forceUser.getBoundMasterUuid() != null) {
            return forceUser;
        }
        return null;
    }

    private static final String[] QUEST_VERBS = {
            "Recover", "Study", "Secure", "Track", "Protect", "Survey", "Restore", "Test", "Verify", "Chart",
            "Guard", "Retrieve", "Cleanse", "Observe", "Map", "Stabilize", "Examine", "Practice", "Deliver", "Decode"
    };

    private static final String[] QUEST_PLACES = {
            "an abandoned archive", "a cracked training dais", "a half-buried meditation circle", "a weathered temple approach",
            "a ruined stair of old stone", "a forgotten chamber", "a pilgrim marker", "an ancient landing ground",
            "a sealed lesson hall", "a collapsed sanctuary", "a frontier shrine", "a broken council alcove",
            "a moonlit causeway", "a canyon reliquary", "a jungle vault", "a stone bridge watched by old carvings"
    };

    private static final String[] QUEST_REASONS = {
            "so your training is built on evidence instead of impulse",
            "so your first student inherits discipline rather than confusion",
            "because old teachings are strongest when tested outside the safety of a temple",
            "to prove you can act without becoming owned by fear",
            "to learn whether your saber will serve judgment or appetite",
            "because every path in the Force begins with what you choose to preserve",
            "to show that strength can stay precise under pressure",
            "before I trust you with another life at your side",
            "so your allegiance is measured by action, not speeches",
            "to make your next lesson difficult enough to matter",
            "because a teacher must know whether you finish difficult work without being watched",
            "so the next rank is earned through habits instead of one lucky victory",
            "because relics are dangerous when impatience reads them first",
            "so you learn to separate useful fear from hunger for control",
            "before I allow another learner to mirror your instincts",
            "because balance, mercy, and ambition all become hollow without practice"
    };

    private static final String[] QUEST_METHODS = {
            "Return when the task is complete and explain what changed in your judgment",
            "Keep the proof in your inventory until I examine it",
            "Do not abandon the trial simply because the first site is empty",
            "Let the lesson be measured by completion, not by how dramatic it felt",
            "Bring back knowledge, resources, or proof of action rather than excuses",
            "Complete this while avoiding needless harm to allies, villagers, students, or mounts",
            "If the path turns dangerous, withdraw, recover, and finish with a clearer mind",
            "Use the task to test your patience before you test your strength",
            "Record what you find; holobooks and datacrons mean more when compared",
            "Treat this as preparation for leading someone younger than you"
    };

    private static final String[] DARK_QUEST_METHODS = {
            "Return when the task is complete and explain how power changed the outcome",
            "Keep the proof in your inventory until I examine it",
            "Do not abandon the trial simply because the first site is empty",
            "Let the lesson be measured by completion, not by theatrics",
            "Bring back knowledge, resources, or proof of action rather than excuses",
            "Complete this without wasting strength on targets that do not matter",
            "If the path turns dangerous, survive, adapt, and finish stronger",
            "Use the task to test your control before you test your rage",
            "Record what you find; relics are leverage when understood",
            "Treat this as preparation for commanding an apprentice"
    };

    private static final String[] QUEST_TONES = {
            "This is an achievable trial, not a punishment",
            "I chose this because your next lesson needs a practical foundation",
            "The task is small enough to finish, but serious enough to reveal habits",
            "A careless learner will rush it; a ready learner will finish cleanly",
            "Your progress will advance when the ledger confirms the objective",
            "The Force is not asking for spectacle here; it is asking for consistency",
            "Do this well and the next stage of training will open",
            "A future student should never inherit shortcuts from you"
    };

    private ForceQuestInventory() {}

    public static void tickMentorConversation(ServerPlayer player, ForceUserEntity teacher) {
        openQuestColumn(player, teacher);
    }

    public static void openQuestColumn(ServerPlayer player, ForceUserEntity teacher) {
        for (String line : buildQuestColumnLines(player, teacher)) {
            player.displayClientMessage(Component.literal(line), false);
        }
    }

    public static List<String> buildQuestColumnLines(ServerPlayer player, ForceUserEntity teacher) {
        ForceSide teacherSide = teacher.getForceUserSide().toCapabilitySide();
        if (studentQuestlineFinished(teacher)) {
            return completedStudentQuestlineLines(player, teacher);
        }
        if (teacher.isForceMentorBond() && mentorQuestlineFinished(player, teacherSide)) {
            retireIncompleteMentorQuestsAfterCap(player, teacherSide);
            return completedMentorQuestlineLines(player, teacher, teacherSide);
        }

        CompoundTag active = getOrCreateActiveQuest(player, teacher);
        if (TYPE_MEDITATE_WITH_MENTOR.equals(active.getString("Type"))) {
            active.putInt("Progress", Math.min(active.getInt("Required"), active.getInt("Progress") + 1));
        }
        boolean completedNow = tryCompleteReadyQuest(player, teacher);
        List<String> lines = new ArrayList<>();
        lines.add("Force Quest Ledger");
        if (!completedNow) {
            lines.add("Quest: " + questTitle(active));
            lines.add("What to do: " + questObjective(active));
            lines.add("Where to go: " + questLocation(active));
            lines.add("Status: " + questStatus(player, active, teacher));
            lines.add("Training: " + trainingStatus(player, teacher));
            lines.add("ProgressValue: " + questProgressValue(player, active));
            lines.add("ProgressMax: " + Math.max(1, active.getInt("Required")));
            lines.add("ProgressLabel: " + progressLabel(player, active, teacher));
        } else {
            boolean mentorLineFinished = teacher.isForceMentorBond() && mentorQuestlineFinished(player, teacherSide);
            boolean studentLineFinished = studentQuestlineFinished(teacher);
            lines.add("Quest: Completed");
            lines.add("What to do: " + (mentorLineFinished ? completedMentorObjective(player, teacherSide) : studentLineFinished ? completedStudentObjective(teacher) : "Reopen this ledger to receive the next training trial."));
            lines.add("Where to go: " + (mentorLineFinished ? completedMentorLocation(player, teacherSide) : studentLineFinished ? completedStudentLocation(teacher) : "Return to your mentor when you are ready."));
            lines.add("Status: Complete");
            lines.add("Training: " + trainingStatus(player, teacher));
            lines.add("ProgressValue: 1");
            lines.add("ProgressMax: 1");
            lines.add("ProgressLabel: " + (mentorLineFinished ? "Mentor trials complete — no more quest rewards can be claimed from this stage." : studentLineFinished ? "Student trials complete — no more student quest rewards are available from this student." : "Complete"));
        }
        return lines;
    }

    private static List<String> completedMentorQuestlineLines(ServerPlayer player, ForceUserEntity teacher, ForceSide side) {
        List<String> lines = new ArrayList<>();
        lines.add("Force Quest Ledger");
        lines.add("Quest: Mentor Questline Complete");
        lines.add("What to do: " + completedMentorObjective(player, side));
        lines.add("Where to go: " + completedMentorLocation(player, side));
        lines.add("Status: Complete");
        lines.add("Training: " + trainingStatus(player, teacher));
        lines.add("ProgressValue: 1");
        lines.add("ProgressMax: 1");
        lines.add("ProgressLabel: Mentor trials complete — no more quest rewards can be claimed from this stage.");
        return lines;
    }

    private static List<String> completedStudentQuestlineLines(ServerPlayer player, ForceUserEntity student) {
        List<String> lines = new ArrayList<>();
        boolean sith = student.getForceUserRole().isApprentice();
        lines.add("Force Quest Ledger");
        lines.add("Quest: " + (sith ? "Sith Apprentice Trials Complete" : student.getForceUserSide().isNeutral() ? "Neutral Padawan Trials Complete" : "Jedi Padawan Trials Complete"));
        lines.add("What to do: " + (sith ? "Your acolyte completed 5 trials and now continues under you as a Sith Apprentice." : "This padawan reached 100% training and is ready to leave as a Knight."));
        lines.add("Where to go: " + (sith ? "When you claim the Dark Lord mantle, expect your apprentice to challenge you." : "They will leave your direct service after graduation."));
        lines.add("Status: Complete");
        lines.add("Training: " + trainingStatus(player, student));
        lines.add("ProgressValue: 1");
        lines.add("ProgressMax: 1");
        lines.add("ProgressLabel: Student trials complete — no more student quest rewards are available from this student.");
        return lines;
    }

    private static String completedMentorObjective(ServerPlayer player, ForceSide side) {
        if (side == ForceSide.DARK) {
            if (ForceTrainingManager.hasTrainedStudent(player)) {
                return "Your Sith ascension is complete. No additional mentor trial rewards are available.";
            }
            if (ForceTrainingManager.isSithLordAscensionReady(player)) {
                return "Defeat your bonded Sith master yourself to complete the Sith Lord ascension.";
            }
            if (ForceTrainingManager.hasSithApprenticeStanding(player)) {
                return "Train your Sith apprentice to readiness. Mentor trials are already finished.";
            }
            return "Claim a Sith apprentice. Your nine mentor trials are already finished.";
        }
        return "Your nine mentor trials are already finished. Train a student or continue your path.";
    }

    private static String completedMentorLocation(ServerPlayer player, ForceSide side) {
        if (side == ForceSide.DARK) {
            if (ForceTrainingManager.isSithLordAscensionReady(player)) {
                return "return to your bonded Sith master when you are ready to challenge them.";
            }
            if (!ForceTrainingManager.hasSithApprenticeStanding(player)) {
                return "find a Sith apprentice candidate; refreshing this ledger will not create more trials.";
            }
            return "stay with your apprentice and finish their training; refreshing this ledger will not create more trials.";
        }
        return "your mentor questline is complete; refreshing this ledger will not create more trials.";
    }

    private static List<String> slotStatusLines(ServerPlayer player) {
        ListTag slots = slots(player);
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < SLOT_COUNT; i++) {
            CompoundTag slot = slots.getCompound(i);
            if (!slot.getBoolean("Active")) {
                lines.add("Slot " + (i + 1) + ": Empty");
            } else if (slot.getBoolean("Complete")) {
                lines.add("Slot " + (i + 1) + ": Complete - " + questTitle(slot));
            } else {
                lines.add("Slot " + (i + 1) + ": Incomplete - " + questTitle(slot));
            }
        }
        return lines;
    }

    private static String questTitle(CompoundTag quest) {
        String title = quest.getString("Title");
        if (!quest.contains("Type") || !quest.contains("Side")) {
            return title;
        }
        try {
            String type = quest.getString("Type");
            ForceSide side = ForceSide.valueOf(quest.getString("Side"));
            if (side == ForceSide.DARK && TYPE_DEFEND_INNOCENTS.equals(type)) {
                return title.replace("Protector's Watch", "Sith Control Sweep");
            }
            if (side == ForceSide.DARK && TYPE_DEFEAT_HOSTILES.equals(type)) {
                return title.replace("Field Defense", "Field Culling");
            }
            if (TYPE_APPRENTICE_FIND_STRUCTURE.equals(type)) {
                return title.isBlank() ? "Sith Apprentice Relic Hunt" : title;
            }
            if (TYPE_APPRENTICE_DEFEAT_JEDI.equals(type)) {
                return title.isBlank() ? "Sith Apprentice Jedi Clash" : title;
            }
        } catch (IllegalArgumentException ignored) {
            return title;
        }
        return title;
    }

    private static String questObjective(CompoundTag quest) {
        String fixed = fixedObjectiveForQuest(quest);
        if (!fixed.isEmpty()) {
            return fixed;
        }
        if (quest.contains("Objective")) {
            return quest.getString("Objective");
        }
        String text = quest.getString("Text");
        int near = text.indexOf(" near ");
        return near > 0 ? text.substring(0, near) : text;
    }

    private static String questLocation(CompoundTag quest) {
        String fixed = fixedLocationForQuest(quest);
        if (!fixed.isEmpty()) {
            return fixed;
        }
        if (quest.contains("Location")) {
            return quest.getString("Location");
        }
        String text = quest.getString("Text");
        int near = text.indexOf(" near ");
        if (near >= 0) {
            int end = text.indexOf(" so ", near + 6);
            if (end < 0) {
                end = text.indexOf(" because ", near + 6);
            }
            if (end < 0) {
                end = text.indexOf('.', near + 6);
            }
            if (end > near) {
                return text.substring(near + 6, end).trim();
            }
        }
        return "follow the objective marker text from your mentor's trial.";
    }

    private static String fixedObjectiveForQuest(CompoundTag quest) {
        if (!quest.contains("Type") || !quest.contains("Side")) {
            return "";
        }
        try {
            String type = quest.getString("Type");
            ForceSide side = ForceSide.valueOf(quest.getString("Side"));
            int required = Math.max(1, quest.getInt("Required"));
            return objectiveFor(type, side, required);
        } catch (IllegalArgumentException ignored) {
            return "";
        }
    }

    private static String fixedLocationForQuest(CompoundTag quest) {
        if (!quest.contains("Type")) {
            return "";
        }
        return locationFor(quest.getString("Type"));
    }

    private static String questStatus(ServerPlayer player, CompoundTag quest, ForceUserEntity teacher) {
        return isComplete(player, quest, teacher) || quest.getBoolean("Complete") ? "Complete" : "Incomplete";
    }

    private static String trainingStatus(ServerPlayer player, ForceUserEntity teacher) {
        ForceSide side = teacher.getForceUserSide().toCapabilitySide();
        if (teacher.isForceMentorBond()) {
            int done = ForceTrainingManager.getMentorQuestCount(player, side);
            if (side == ForceSide.DARK) {
                if (ForceTrainingManager.hasTrainedStudent(player)) {
                    return "Sith Lord ascension complete";
                }
                if (ForceTrainingManager.isSithLordAscensionReady(player)) {
                    return "Apprentice ready — defeat your bonded Sith master to become Sith Lord";
                }
                if (ForceTrainingManager.hasSithApprenticeStanding(player)) {
                    return "Sith Apprentice — train your apprentice to readiness";
                }
                return "Sith Acolyte trials " + done + "/" + ForceTrainingManager.QUESTS_TO_KNIGHT;
            }
            return ForceTrainingManager.mentorProgressLine(player, side);
        }
        if (teacher.getForceUserRole().isStudent()) {
            int done = teacher.getStudentQuestCompletions();
            int required = ForceTrainingManager.STUDENT_QUESTS_TO_GRADUATE;
            if (teacher.getForceUserRole().isApprentice()) {
                return (teacher.isSithApprenticeReady() ? "Sith apprentice ready" : "Sith acolyte trials " + done + "/" + required);
            }
            String label = teacher.getForceUserSide().isNeutral() ? "Neutral padawan trials " : "Jedi padawan trials ";
            return label + done + "/" + required + " (" + teacher.getStudentTrainingProgressPercent() + "%)";
        }
        return ForceTrainingManager.mentorProgressLine(player, side);
    }

    public static CompoundTag getOrCreateActiveQuest(ServerPlayer player, ForceUserEntity teacher) {
        ListTag slots = slots(player);
        ForceSide side = teacher.getForceUserSide().toCapabilitySide();
        if (studentQuestlineFinished(teacher)) {
            return completedStudentQuestlineTag(side, teacher);
        }
        if (teacher.isForceMentorBond() && mentorQuestlineFinished(player, side)) {
            retireIncompleteMentorQuestsAfterCap(player, side);
            return completedMentorQuestlineTag(side);
        }
        String stage = stageFor(teacher);
        for (int i = 0; i < slots.size(); i++) {
            CompoundTag slot = slots.getCompound(i);
            if (slot.getBoolean("Active") && !slot.getBoolean("Complete") && side.name().equals(slot.getString("Side")) && questBelongsToTeacher(slot, teacher, stage)) {
                return slot;
            }
        }
        int targetSlot = firstReusableSlot(slots);
        CompoundTag quest = generateQuest(player, teacher, targetSlot);
        slots.set(targetSlot, quest);
        return quest;
    }

    private static boolean mentorQuestlineFinished(ServerPlayer player, ForceSide side) {
        return ForceTrainingManager.hasMentorQuestlineComplete(player, side)
                || ForceTrainingManager.getMentorQuestCount(player, side) >= ForceTrainingManager.QUESTS_TO_KNIGHT;
    }

    private static CompoundTag completedMentorQuestlineTag(ForceSide side) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Active", false);
        tag.putBoolean("Complete", true);
        tag.putInt("Slot", -1);
        tag.putString("Type", "mentor_questline_complete");
        tag.putString("Side", side.name());
        tag.putString("Title", "Mentor Questline Complete");
        tag.putString("Objective", "The nine mentor trials are already complete.");
        tag.putString("Location", "No new mentor trial is available from this stage.");
        tag.putString("Text", "The nine mentor trials are already complete.");
        tag.putInt("Required", 1);
        tag.putInt("Progress", 1);
        tag.putInt("Reward", 0);
        return tag;
    }

    private static CompoundTag completedStudentQuestlineTag(ForceSide side, ForceUserEntity student) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Active", false);
        tag.putBoolean("Complete", true);
        tag.putInt("Slot", -1);
        tag.putString("Type", "student_questline_complete");
        tag.putString("Side", side.name());
        tag.putString("Title", student != null && student.getForceUserRole().isApprentice() ? "Sith Apprentice Trials Complete" : (side == ForceSide.NEUTRAL ? "Neutral Padawan Trials Complete" : "Jedi Padawan Trials Complete"));
        tag.putString("Objective", completedStudentObjective(student));
        tag.putString("Location", completedStudentLocation(student));
        tag.putString("Text", "The five student trials are already complete.");
        tag.putInt("Required", 1);
        tag.putInt("Progress", 1);
        tag.putInt("Reward", 0);
        return tag;
    }

    private static String completedStudentObjective(ForceUserEntity student) {
        if (student != null && student.getForceUserRole().isApprentice()) {
            return "Your acolyte completed 5 trials and now continues under you as a Sith Apprentice.";
        }
        return "This padawan reached 100% training and is ready to leave as a Knight.";
    }

    private static String completedStudentLocation(ForceUserEntity student) {
        if (student != null && student.getForceUserRole().isApprentice()) {
            return "When you claim the Dark Lord mantle, expect your apprentice to challenge you.";
        }
        return "They will leave your direct service after graduation.";
    }

    private static boolean studentQuestlineFinished(ForceUserEntity teacher) {
        return teacher != null
                && teacher.getForceUserRole().isStudent()
                && !teacher.isForceMentorBond()
                && teacher.getStudentQuestCompletions() >= ForceTrainingManager.STUDENT_QUESTS_TO_GRADUATE;
    }

    private static String stageFor(ForceUserEntity teacher) {
        if (teacher.isForceMentorBond()) {
            return STAGE_MENTOR;
        }
        if (teacher.getForceUserRole().isStudent()) {
            return STAGE_STUDENT;
        }
        return STAGE_GENERAL;
    }

    private static boolean questBelongsToTeacher(CompoundTag quest, ForceUserEntity teacher, String stage) {
        String storedStage = quest.contains("Stage") ? quest.getString("Stage") : STAGE_MENTOR;
        if (!stage.equals(storedStage)) {
            return false;
        }
        if (STAGE_STUDENT.equals(stage)) {
            return quest.hasUUID("TeacherUUID") && quest.getUUID("TeacherUUID").equals(teacher.getUUID());
        }
        return true;
    }

    private static void retireIncompleteMentorQuestsAfterCap(ServerPlayer player, ForceSide side) {
        ListTag slots = slots(player);
        for (int i = 0; i < slots.size(); i++) {
            CompoundTag quest = slots.getCompound(i);
            if (!quest.getBoolean("Active") || quest.getBoolean("Complete") || !side.name().equals(quest.getString("Side")) || STAGE_STUDENT.equals(quest.getString("Stage"))) {
                continue;
            }
            quest.putBoolean("Complete", true);
            quest.putInt("Progress", Math.max(1, quest.getInt("Required")));
        }
    }

    public static boolean tryCompleteReadyQuest(ServerPlayer player, ForceUserEntity teacher) {
        ListTag slots = slots(player);
        ForceSide teacherSide = teacher.getForceUserSide().toCapabilitySide();
        if (studentQuestlineFinished(teacher)) {
            return false;
        }
        if (teacher.isForceMentorBond() && mentorQuestlineFinished(player, teacherSide)) {
            retireIncompleteMentorQuestsAfterCap(player, teacherSide);
            return false;
        }
        for (int i = 0; i < slots.size(); i++) {
            CompoundTag quest = slots.getCompound(i);
            if (!quest.getBoolean("Active") || quest.getBoolean("Complete") || !teacherSide.name().equals(quest.getString("Side")) || !questBelongsToTeacher(quest, teacher, stageFor(teacher))) {
                continue;
            }
            if (!isComplete(player, quest, teacher)) {
                continue;
            }
            quest.putBoolean("Complete", true);
            quest.putInt("Progress", quest.getInt("Required"));
            int reward = Math.max(6, quest.getInt("Reward"));
            player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
                cap.addAlignmentPoints(teacherSide, reward);
                ForceCapabilityManager.sync(player);
            });
            if (teacher.isForceMentorBond()) {
                int completed = ForceTrainingManager.recordMentorQuestComplete(player, teacherSide, teacher.getDisplayName().getString());
                player.displayClientMessage(Component.literal("Quest complete: " + questTitle(quest) + " (+" + reward + " " + sideLabel(teacherSide) + " points, mentor trial " + completed + "/" + ForceTrainingManager.QUESTS_TO_KNIGHT + ")."), false);
            } else {
                teacher.advanceTrainingFromQuest(player, reward);
                player.displayClientMessage(Component.literal("Quest complete: " + questTitle(quest) + " (+" + reward + " " + sideLabel(teacherSide) + " points, student training advanced)."), false);
            }
            return true;
        }
        return false;
    }

    public static void recordKill(ServerPlayer player, LivingEntity killed) {
        recordKill(player, killed, null);
    }

    public static void recordKill(ServerPlayer player, LivingEntity killed, ForceUserEntity companionKiller) {
        ListTag slots = slots(player);
        for (int i = 0; i < slots.size(); i++) {
            CompoundTag quest = slots.getCompound(i);
            if (!quest.getBoolean("Active") || quest.getBoolean("Complete")) {
                continue;
            }
            String type = quest.getString("Type");
            boolean match = false;
            if ((TYPE_DEFEAT_HOSTILES.equals(type) || TYPE_DEFEND_INNOCENTS.equals(type)) && isHostileQuestTarget(killed)) {
                match = !STAGE_STUDENT.equals(quest.getString("Stage")) || studentIsParticipating(player, quest, companionKiller);
                if (!match) {
                    player.displayClientMessage(Component.literal("Your student must be with you for this combat trial to count."), true);
                }
            } else if (TYPE_DEFEAT_OPPOSING_FORCE_USERS.equals(type) && killed instanceof ForceUserEntity forceUser) {
                ForceSide questSide = ForceSide.valueOf(quest.getString("Side"));
                match = forceUser.getForceUserSide().toCapabilitySide() != questSide;
                if (match && STAGE_STUDENT.equals(quest.getString("Stage"))) {
                    match = studentIsParticipating(player, quest, companionKiller);
                    if (!match) {
                        player.displayClientMessage(Component.literal("Your student must be with you for this rival trial to count."), true);
                    }
                }
            } else if (TYPE_APPRENTICE_DEFEAT_JEDI.equals(type) && isJediQuestTarget(killed)) {
                match = studentIsParticipating(player, quest, companionKiller);
                if (!match) {
                    player.displayClientMessage(Component.literal("Your Sith apprentice must be with you for this Jedi trial to count."), true);
                }
            }
            if (match) {
                quest.putInt("Progress", Math.min(quest.getInt("Required"), quest.getInt("Progress") + 1));
                player.displayClientMessage(Component.literal("Quest progress: " + questTitle(quest) + " " + quest.getInt("Progress") + "/" + quest.getInt("Required")), true);
            }
        }
    }

    private static boolean isHostileQuestTarget(LivingEntity killed) {
        return killed instanceof Monster || killed.getType().getCategory() == MobCategory.MONSTER;
    }

    private static boolean isJediQuestTarget(LivingEntity killed) {
        return killed instanceof ForceUserEntity forceUser && forceUser.getForceUserSide().isLight();
    }

    private static boolean studentIsParticipating(ServerPlayer player, CompoundTag quest, ForceUserEntity companionKiller) {
        if (!quest.hasUUID("TeacherUUID")) {
            return false;
        }
        java.util.UUID apprenticeId = quest.getUUID("TeacherUUID");
        if (companionKiller != null && companionKiller.getUUID().equals(apprenticeId) && companionKiller.isBoundTo(player)) {
            return true;
        }
        Entity entity = findEntity(player, apprenticeId);
        return entity instanceof ForceUserEntity apprentice
                && apprentice.isAlive()
                && apprentice.isBoundTo(player)
                && apprentice.distanceToSqr(player) <= 32.0D * 32.0D;
    }

    private static boolean studentIsWithPlayer(ServerPlayer player, ForceUserEntity apprentice) {
        return apprentice != null
                && apprentice.isAlive()
                && apprentice.isBoundTo(player)
                && apprentice.distanceToSqr(player) <= 32.0D * 32.0D;
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

    public static String summary(ServerPlayer player) {
        ListTag slots = slots(player);
        List<String> parts = new ArrayList<>();
        for (int i = 0; i < slots.size(); i++) {
            CompoundTag slot = slots.getCompound(i);
            if (!slot.getBoolean("Active")) {
                parts.add("[" + (i + 1) + "] Empty");
            } else if (slot.getBoolean("Complete")) {
                parts.add("[" + (i + 1) + "] Complete");
            } else {
                parts.add("[" + (i + 1) + "] Incomplete");
            }
        }
        return "Quest slots: " + String.join(" | ", parts);
    }

    public static void setPendingDialogue(ServerPlayer player, ForceUserEntity speaker, DialogueChoice[] choices) {
        CompoundTag pending = new CompoundTag();
        pending.putLong("ExpiresAt", player.level().getGameTime() + 20L * 30L);
        pending.putString("Speaker", speaker.getDisplayName().getString());
        pending.putString("SpeakerSide", speaker.getForceUserSide().toCapabilitySide().name());
        pending.putInt("Count", Math.min(3, choices.length));
        for (int i = 0; i < choices.length && i < 3; i++) {
            CompoundTag option = new CompoundTag();
            option.putString("Text", choices[i].text());
            option.putString("Side", choices[i].side().name());
            option.putInt("Points", choices[i].points());
            pending.put("Option" + i, option);
        }
        root(player).put(PENDING_DIALOGUE, pending);
    }

    public static int chooseDialogueOption(ServerPlayer player, int choiceIndex) {
        CompoundTag root = root(player);
        if (!root.contains(PENDING_DIALOGUE, Tag.TAG_COMPOUND)) {
            player.displayClientMessage(Component.literal("No active Force-user conversation choice."), true);
            return 0;
        }
        CompoundTag pending = root.getCompound(PENDING_DIALOGUE);
        if (player.level().getGameTime() > pending.getLong("ExpiresAt")) {
            root.remove(PENDING_DIALOGUE);
            player.displayClientMessage(Component.literal("That conversation choice has faded. Speak to them again."), true);
            return 0;
        }
        int idx = Math.max(0, Math.min(choiceIndex - 1, pending.getInt("Count") - 1));
        CompoundTag option = pending.getCompound("Option" + idx);
        ForceSide side = ForceSide.valueOf(option.getString("Side"));
        int points = Math.max(0, option.getInt("Points"));
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.addAlignmentPoints(side, points);
            if (cap.getCommittedSide() == ForceSide.NEUTRAL && side != ForceSide.NEUTRAL && Math.abs(cap.getAlignmentBalance()) > 35) {
                player.displayClientMessage(Component.literal("Your neutral path is drifting. Balance requires answering the next crisis without feeding the same extreme."), false);
            }
            ForceCapabilityManager.sync(player);
        });
        player.displayClientMessage(Component.literal("You answered: " + option.getString("Text")), false);
        root.remove(PENDING_DIALOGUE);
        return 1;
    }

    private static CompoundTag generateQuest(ServerPlayer player, ForceUserEntity teacher, int slot) {
        long seed = teacher.getPersonalitySeed() ^ player.getUUID().getLeastSignificantBits() ^ player.level().getGameTime() ^ (slot * 31L);
        Random random = new Random(seed);
        ForceSide side = teacher.getForceUserSide().toCapabilitySide();
        String stage = stageFor(teacher);
        String[] availableTypes = questTypesFor(side, teacher);
        String type;
        if (isStudentTraining(teacher)) {
            type = availableTypes[Math.floorMod(teacher.getStudentQuestCompletions(), availableTypes.length)];
        } else {
            type = availableTypes[Math.floorMod(random.nextInt(), availableTypes.length)];
        }
        int required = requiredFor(type, random);
        String title = titleFor(type, side, random);
        String objective = objectiveFor(type, side, required);
        String location = locationFor(type);
        String text = objective
                + " near " + location
                + " " + QUEST_REASONS[Math.floorMod(random.nextInt(), QUEST_REASONS.length)] + ". "
                + methodFor(side, random) + ". "
                + QUEST_TONES[Math.floorMod(random.nextInt(), QUEST_TONES.length)] + ".";
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Active", true);
        tag.putBoolean("Complete", false);
        tag.putInt("Slot", slot);
        tag.putString("Type", type);
        tag.putString("Side", side.name());
        tag.putString("Stage", stage);
        if (STAGE_STUDENT.equals(stage)) {
            tag.putUUID("TeacherUUID", teacher.getUUID());
            tag.putString("TeacherName", teacher.getDisplayName().getString());
        }
        tag.putString("Title", title);
        tag.putString("Objective", objective);
        tag.putString("Location", location);
        tag.putString("Text", text);
        tag.putInt("Required", required);
        tag.putInt("Progress", 0);
        tag.putInt("Reward", rewardFor(type, required));
        return tag;
    }

    private static String methodFor(ForceSide side, Random random) {
        String[] methods = side == ForceSide.DARK ? DARK_QUEST_METHODS : QUEST_METHODS;
        return methods[Math.floorMod(random.nextInt(), methods.length)];
    }

    private static boolean isStudentTraining(ForceUserEntity teacher) {
        return teacher != null
                && teacher.getForceUserRole().isStudent()
                && !teacher.isForceMentorBond();
    }

    private static String[] questTypesFor(ForceSide side, ForceUserEntity teacher) {
        if (isStudentTraining(teacher)) {
            if (teacher.getForceUserRole().isApprentice()) {
                return APPRENTICE_QUEST_TYPES;
            }
            return switch (side) {
                case LIGHT -> LIGHT_STUDENT_QUEST_TYPES;
                case NEUTRAL -> NEUTRAL_STUDENT_QUEST_TYPES;
                default -> APPRENTICE_QUEST_TYPES;
            };
        }
        return switch (side) {
            case DARK -> DARK_QUEST_TYPES;
            case NEUTRAL -> NEUTRAL_QUEST_TYPES;
            case LIGHT -> LIGHT_QUEST_TYPES;
            default -> NEUTRAL_QUEST_TYPES;
        };
    }

    private static String titleFor(String type, ForceSide side, Random random) {
        String path = switch (side) {
            case DARK -> "Sith";
            case NEUTRAL -> "Balanced";
            case LIGHT -> "Jedi";
            default -> "Force";
        };
        String suffix = switch (type) {
            case TYPE_READ_HOLOBOOK -> "Archive Lesson";
            case TYPE_RECOVER_DATACRONS -> "Datacron Trial";
            case TYPE_GATHER_SABER_PARTS -> "Hilt Discipline";
            case TYPE_DEFEAT_HOSTILES -> side == ForceSide.DARK ? "Field Culling" : "Field Defense";
            case TYPE_DEFEAT_OPPOSING_FORCE_USERS -> "Rival Force Trial";
            case TYPE_VISIT_ANCIENT_SITE -> "Ancient Site Survey";
            case TYPE_MEDITATE_WITH_MENTOR -> "Meditation Cycle";
            case TYPE_DEFEND_INNOCENTS -> side == ForceSide.DARK ? "Sith Control Sweep" : "Protector's Watch";
            case TYPE_GATHER_HOLOBOOKS -> "Holobook Index";
            case TYPE_BUILD_SABER_READY -> "First Saber Readiness";
            case TYPE_APPRENTICE_FIND_STRUCTURE -> "Apprentice Relic Hunt";
            case TYPE_APPRENTICE_DEFEAT_JEDI -> "Apprentice Jedi Clash";
            default -> "Mentor Trial";
        };
        return path + " " + suffix + " " + (1 + Math.floorMod(random.nextInt(), 99));
    }

    private static String objectiveFor(String type, ForceSide side, int required) {
        return switch (type) {
            case TYPE_READ_HOLOBOOK -> "Carry one " + sideLabel(side).toLowerCase(Locale.ROOT) + " holobook and return to your mentor";
            case TYPE_RECOVER_DATACRONS -> "Recover " + required + " " + sideLabel(side).toLowerCase(Locale.ROOT) + " datacron charge" + (required == 1 ? "" : "s");
            case TYPE_GATHER_SABER_PARTS -> "Collect " + required + " saber part" + (required == 1 ? "" : "s");
            case TYPE_DEFEAT_HOSTILES -> side == ForceSide.DARK
                    ? "Defeat " + required + " hostile creature" + (required == 1 ? "" : "s") + " to prove your strength"
                    : "Defeat " + required + " hostile creature" + (required == 1 ? "" : "s") + " without striking allies";
            case TYPE_DEFEAT_OPPOSING_FORCE_USERS -> "Defeat " + required + " opposing Jedi, Sith, or neutral adept" + (required == 1 ? "" : "s") + " threatening your path";
            case TYPE_VISIT_ANCIENT_SITE -> "Visit an ancient site and stand close enough for the Force to quiet around you";
            case TYPE_MEDITATE_WITH_MENTOR -> "Speak with your mentor for " + required + " focused conversation" + (required == 1 ? "" : "s");
            case TYPE_DEFEND_INNOCENTS -> side == ForceSide.DARK
                    ? "Defeat " + required + " threat" + (required == 1 ? "" : "s") + " to secure Sith control"
                    : "Defeat " + required + " threat" + (required == 1 ? "" : "s") + " while keeping villagers and students safe";
            case TYPE_GATHER_HOLOBOOKS -> "Carry " + required + " holobook" + (required == 1 ? "" : "s") + " of any tradition for comparison";
            case TYPE_BUILD_SABER_READY -> "Carry one emitter, one switch, one grip, and one pommel before asking for a student";
            case TYPE_APPRENTICE_FIND_STRUCTURE -> "Bring your Sith apprentice to an ancient structure or Force relic site";
            case TYPE_APPRENTICE_DEFEAT_JEDI -> "Defeat " + required + " Jedi or light-side adept" + (required == 1 ? "" : "s") + " with your Sith apprentice at your side";
            default -> "Complete a worthy lesson";
        };
    }

    private static String locationFor(String type) {
        return switch (type) {
            case TYPE_READ_HOLOBOOK, TYPE_GATHER_HOLOBOOKS -> "archives, ruins, or any stash containing holobooks";
            case TYPE_RECOVER_DATACRONS -> "datacron ruins, vaults, or Force relic sites";
            case TYPE_GATHER_SABER_PARTS -> "ruins, caches, or old workshops";
            case TYPE_DEFEAT_HOSTILES, TYPE_DEFEND_INNOCENTS -> "any area where hostile mobs spawn";
            case TYPE_DEFEAT_OPPOSING_FORCE_USERS -> "areas patrolled by opposing Jedi, Sith, or neutral adepts";
            case TYPE_VISIT_ANCIENT_SITE -> "ancient temple blocks, holobook stones, or carved ruins";
            case TYPE_MEDITATE_WITH_MENTOR -> "return to this mentor";
            case TYPE_BUILD_SABER_READY -> "your inventory, then the saber forge";
            case TYPE_APPRENTICE_FIND_STRUCTURE -> "ancient temples, ruins, statues, holocrons, or carved structure blocks";
            case TYPE_APPRENTICE_DEFEAT_JEDI -> "Jedi patrols, Jedi outposts, temple guards, or light-side adept encounters";
            default -> "wherever your mentor's trial sends you";
        };
    }

    private static int requiredFor(String type, Random random) {
        return switch (type) {
            case TYPE_READ_HOLOBOOK, TYPE_VISIT_ANCIENT_SITE, TYPE_BUILD_SABER_READY, TYPE_APPRENTICE_FIND_STRUCTURE, TYPE_APPRENTICE_DEFEAT_JEDI -> 1;
            case TYPE_RECOVER_DATACRONS -> 1 + random.nextInt(3);
            case TYPE_GATHER_SABER_PARTS, TYPE_GATHER_HOLOBOOKS -> 2 + random.nextInt(3);
            case TYPE_DEFEAT_OPPOSING_FORCE_USERS -> 1 + random.nextInt(2);
            case TYPE_MEDITATE_WITH_MENTOR -> 2 + random.nextInt(3);
            default -> 3 + random.nextInt(4);
        };
    }

    private static int rewardFor(String type, int required) {
        int base = switch (type) {
            case TYPE_DEFEAT_OPPOSING_FORCE_USERS, TYPE_BUILD_SABER_READY, TYPE_APPRENTICE_DEFEAT_JEDI -> 12;
            case TYPE_VISIT_ANCIENT_SITE, TYPE_READ_HOLOBOOK, TYPE_APPRENTICE_FIND_STRUCTURE -> 9;
            default -> 7;
        };
        return base + required;
    }

    private static boolean isComplete(ServerPlayer player, CompoundTag quest, ForceUserEntity teacher) {
        String type = quest.getString("Type");
        int required = Math.max(1, quest.getInt("Required"));
        if (quest.getInt("Progress") >= required) {
            return true;
        }
        ForceSide side = ForceSide.valueOf(quest.getString("Side"));
        if (STAGE_STUDENT.equals(quest.getString("Stage")) && teacher != null && !studentIsWithPlayer(player, teacher)) {
            return false;
        }
        return switch (type) {
            case TYPE_READ_HOLOBOOK -> countItem(player, holobookFor(side)) >= 1;
            case TYPE_RECOVER_DATACRONS -> player.getCapability(ForceProvider.FORCE_CAPABILITY).map(cap -> cap.getDatacrons(side) >= required).orElse(false) || countItem(player, datacronFor(side)) >= required;
            case TYPE_GATHER_SABER_PARTS -> countSaberParts(player) >= required;
            case TYPE_VISIT_ANCIENT_SITE -> isNearAncientSite(player);
            case TYPE_GATHER_HOLOBOOKS -> countHolobooks(player) >= required;
            case TYPE_BUILD_SABER_READY -> hasSaberReadyKit(player);
            case TYPE_APPRENTICE_FIND_STRUCTURE -> teacher != null && studentIsWithPlayer(player, teacher) && isNearAncientSite(player);
            default -> false;
        };
    }

    private static int questProgressValue(ServerPlayer player, CompoundTag quest) {
        String type = quest.getString("Type");
        int required = Math.max(1, quest.getInt("Required"));
        int progress = quest.getInt("Progress");
        ForceSide side = ForceSide.valueOf(quest.getString("Side"));
        boolean studentStage = STAGE_STUDENT.equals(quest.getString("Stage"));
        boolean studentPresent = !studentStage || studentIsParticipating(player, quest, null);
        int current = switch (type) {
            case TYPE_READ_HOLOBOOK -> studentPresent ? countItem(player, holobookFor(side)) : 0;
            case TYPE_RECOVER_DATACRONS -> studentPresent ? Math.max(countItem(player, datacronFor(side)), player.getCapability(ForceProvider.FORCE_CAPABILITY).map(cap -> cap.getDatacrons(side)).orElse(0)) : 0;
            case TYPE_GATHER_SABER_PARTS -> studentPresent ? countSaberParts(player) : 0;
            case TYPE_GATHER_HOLOBOOKS -> studentPresent ? countHolobooks(player) : 0;
            case TYPE_BUILD_SABER_READY -> studentPresent && hasSaberReadyKit(player) ? 1 : 0;
            case TYPE_VISIT_ANCIENT_SITE -> studentPresent && isNearAncientSite(player) ? 1 : 0;
            case TYPE_APPRENTICE_FIND_STRUCTURE -> studentPresent && isNearAncientSite(player) ? 1 : 0;
            default -> progress;
        };
        return Math.max(0, Math.min(required, current));
    }

    private static String progressLabel(ServerPlayer player, CompoundTag quest, ForceUserEntity teacher) {
        String type = quest.getString("Type");
        int required = Math.max(1, quest.getInt("Required"));
        ForceSide side = ForceSide.valueOf(quest.getString("Side"));
        int current = questProgressValue(player, quest);
        return current + "/" + required + " — " + completionHint(type, side);
    }

    private static String completionHint(String type, ForceSide side) {
        return switch (type) {
            case TYPE_READ_HOLOBOOK -> "carry a " + sideLabel(side).toLowerCase(Locale.ROOT) + " holobook.";
            case TYPE_RECOVER_DATACRONS -> "use or carry matching datacrons.";
            case TYPE_GATHER_SABER_PARTS -> "collect modular saber parts.";
            case TYPE_DEFEAT_HOSTILES -> "defeat hostile mobs without hitting allies.";
            case TYPE_DEFEAT_OPPOSING_FORCE_USERS -> "defeat opposing Jedi, Sith, or neutral adepts outside your allegiance.";
            case TYPE_VISIT_ANCIENT_SITE -> "stand beside ancient temple blocks, holobook stones, or carved ruins.";
            case TYPE_MEDITATE_WITH_MENTOR -> "speak with your mentor again.";
            case TYPE_DEFEND_INNOCENTS -> side == ForceSide.DARK ? "defeat threats that challenge Sith control." : "defeat threats without harming civilians.";
            case TYPE_GATHER_HOLOBOOKS -> "carry holobooks for comparison.";
            case TYPE_BUILD_SABER_READY -> "carry an emitter, switch, grip, and pommel.";
            case TYPE_APPRENTICE_FIND_STRUCTURE -> "bring the apprentice with you to a structure/relic site.";
            case TYPE_APPRENTICE_DEFEAT_JEDI -> "kill a Jedi or light-side adept while the apprentice is nearby.";
            default -> "continue the trial.";
        };
    }

    private static int firstReusableSlot(ListTag slots) {
        for (int i = 0; i < SLOT_COUNT; i++) {
            CompoundTag slot = slots.getCompound(i);
            if (!slot.getBoolean("Active") || slot.getBoolean("Complete")) {
                return i;
            }
        }
        return 0;
    }

    private static ListTag slots(ServerPlayer player) {
        CompoundTag root = root(player);
        if (!root.contains(SLOTS, Tag.TAG_LIST)) {
            ListTag list = new ListTag();
            for (int i = 0; i < SLOT_COUNT; i++) {
                CompoundTag empty = new CompoundTag();
                empty.putBoolean("Active", false);
                empty.putInt("Slot", i);
                list.add(empty);
            }
            root.put(SLOTS, list);
        }
        ListTag list = root.getList(SLOTS, Tag.TAG_COMPOUND);
        while (list.size() < SLOT_COUNT) {
            CompoundTag empty = new CompoundTag();
            empty.putBoolean("Active", false);
            empty.putInt("Slot", list.size());
            list.add(empty);
        }
        return list;
    }

    private static CompoundTag root(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT, Tag.TAG_COMPOUND)) {
            persistent.put(ROOT, new CompoundTag());
        }
        return persistent.getCompound(ROOT);
    }

    private static int countItem(ServerPlayer player, Item item) {
        if (item == null) {
            return 0;
        }
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    private static int countSaberParts(ServerPlayer player) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (isSaberPart(stack)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    private static int countHolobooks(ServerPlayer player) {
        return countItem(player, galaxyunderchaos.JEDI_HOLOBOOK.get())
                + countItem(player, galaxyunderchaos.SITH_HOLOBOOK.get())
                + countItem(player, galaxyunderchaos.ANCIENT_HOLOBOOK.get())
                + countItem(player, galaxyunderchaos.SHII_CHO_HOLOBOOK.get())
                + countItem(player, galaxyunderchaos.SORESU_HOLOBOOK.get())
                + countItem(player, galaxyunderchaos.ATARU_HOLOBOOK.get())
                + countItem(player, galaxyunderchaos.SHIEN_DJEM_SO_HOLOBOOK.get())
                + countItem(player, galaxyunderchaos.NIMAN_HOLOBOOK.get())
                + countItem(player, galaxyunderchaos.JUYO_VAAPAD_HOLOBOOK.get());
    }

    private static boolean hasSaberReadyKit(ServerPlayer player) {
        boolean emitter = false;
        boolean sw = false;
        boolean grip = false;
        boolean pommel = false;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.isEmpty()) continue;
            String path = stack.getDescriptionId().toLowerCase(Locale.ROOT);
            emitter |= path.contains("emitter");
            sw |= path.contains("switch") || path.contains("activation");
            grip |= path.contains("grip");
            pommel |= path.contains("pommel");
        }
        return emitter && sw && grip && pommel;
    }

    private static boolean isSaberPart(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        for (RegistryObject<Item> part : galaxyunderchaos.LIGHTSABER_PARTS.values()) {
            if (stack.is(part.get())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNearAncientSite(ServerPlayer player) {
        BlockPos center = player.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-10, -4, -10), center.offset(10, 5, 10))) {
            Block block = player.level().getBlockState(pos).getBlock();
            if (isAncientStructureBlock(block)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAncientStructureBlock(Block block) {
        if (block == galaxyunderchaos.ANCIENT_TEMPLE_STONE.get()
                || block == galaxyunderchaos.ANCIENT_TEMPLE_STONE_PILLAR.get()
                || block == galaxyunderchaos.ANCIENT_TEMPLE_STONE_HOLOBOOK.get()
                || block == galaxyunderchaos.TEMPLE_STONE.get()
                || block == galaxyunderchaos.TEMPLE_STONE_PILLAR.get()
                || block == galaxyunderchaos.TEMPLE_STONE_HOLOBOOK.get()
                || block == galaxyunderchaos.ASHLA_TEMPLE_STONE.get()
                || block == galaxyunderchaos.BOGAN_TEMPLE_STONE.get()
                || block == galaxyunderchaos.TYTHON_TEMPLE_STONE.get()
                || block == galaxyunderchaos.KORRIBAN_TEMPLE_STONE.get()
                || block == galaxyunderchaos.MALACHOR_TEMPLE_STONE.get()
                || block == galaxyunderchaos.DARK_TEMPLE_STONE.get()) {
            return true;
        }
        String id = block.getDescriptionId().toLowerCase(Locale.ROOT);
        return id.startsWith("block." + galaxyunderchaos.MODID + ".")
                && (id.contains("temple") || id.contains("statue") || id.contains("holocron") || id.contains("datacron") || id.contains("relic"));
    }

    private static Item holobookFor(ForceSide side) {
        return switch (side) {
            case DARK -> galaxyunderchaos.SITH_HOLOBOOK.get();
            case NEUTRAL -> galaxyunderchaos.ANCIENT_HOLOBOOK.get();
            default -> galaxyunderchaos.JEDI_HOLOBOOK.get();
        };
    }

    private static Item datacronFor(ForceSide side) {
        return switch (side) {
            case DARK -> galaxyunderchaos.SITH_DATACRON.get();
            case NEUTRAL -> galaxyunderchaos.ANCIENT_DATACRON.get();
            default -> galaxyunderchaos.JEDI_DATACRON.get();
        };
    }

    private static String sideLabel(ForceSide side) {
        return switch (side) {
            case DARK -> "Dark side";
            case NEUTRAL -> "Neutral";
            case LIGHT -> "Light side";
            default -> "Force";
        };
    }

    public record DialogueChoice(String text, ForceSide side, int points) {}
}
