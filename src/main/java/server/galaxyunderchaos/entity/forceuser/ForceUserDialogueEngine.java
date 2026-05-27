package server.galaxyunderchaos.entity.forceuser;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import server.galaxyunderchaos.force.ForceCapability;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForceSide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class ForceUserDialogueEngine {
    private static final String[] TEMPERAMENTS = {
            "patient", "severe", "quiet", "scholarly", "battle-worn", "ceremonial", "restless", "watchful",
            "gentle", "unyielding", "cryptic", "pragmatic", "compassionate", "austere", "curious", "grim",
            "dry-humored", "soft-spoken", "suspicious", "mentorly", "haunted", "formal", "tactical", "forgiving",
            "demanding", "reflective", "weathered", "plainspoken", "mystic", "protective", "calculating", "measured"
    };

    private static final String[] TEACHING_STYLES = {
            "asks questions before giving answers", "speaks in careful field instructions", "tests your patience before your reflexes",
            "uses old temple drills and practical survival lessons", "teaches through stories of failed students", "watches your hands whenever anger rises",
            "turns every ruin into a classroom", "makes you explain why a choice was necessary", "judges whether you can stop after winning",
            "cares more about your next decision than your last victory", "breaks lessons into breath, stance, choice, and consequence",
            "refuses to separate saber craft from character", "uses silence as often as words", "pushes you into achievable but uncomfortable trials",
            "measures your courage by who survives your strength", "looks for discipline when no one is watching"
    };

    private static final String[] LESSON_FORMS = {
            "Begin with breath, then stance. Power without discipline becomes noise.",
            "The first blade you build should answer a question, not decorate your belt.",
            "Before three thousand years before the last great schisms, teachers measured students by restraint first and victory second.",
            "Datacrons preserve fragments; holobooks preserve context. Read both before chasing stronger techniques.",
            "A saber is not complete when it ignites. It is complete when your choices stop shaking around it.",
            "Old temple records speak of guides who served balance by standing beside the innocent when darkness rose.",
            "Do not let ruins fool you. Stone falls, but a tradition survives when someone practices it carefully.",
            "When you find saber parts, inspect the emitter and focusing chamber first. A careless hilt teaches careless movement.",
            "Power gained from anger is fast, but it makes every later choice narrower.",
            "Mercy is not weakness. Cruelty is not strength. Both are habits, and habits become alignment.",
            "A master gives you tasks before titles because a title without practiced judgment is just costume.",
            "If you cannot carry a lesson through hunger, weather, fear, and frustration, you are not ready to carry a student.",
            "A Force-user who only trains during victory becomes useless during panic.",
            "The old orders kept records because memory alone flatters the survivor.",
            "A white blade means nothing if balance becomes laziness; balance must still defend the living.",
            "The dark side rewards certainty, but false certainty is a chain with a polished name.",
            "The light asks restraint, but restraint without courage abandons people to the violent.",
            "Your apprentice will copy what you do under pressure, not what you say beside a campfire.",
            "If a foe flees and you chase only to enjoy the ending, the Force remembers that appetite.",
            "If you spare danger from cowardice, not wisdom, the Force remembers that too."
    };

    private static final String[] LIGHT_WARNINGS = {
            "I sense the dark side gathering around your habits. I will teach you, but I will not excuse cruelty.",
            "You have spilled too much innocent blood. If you seek the light, prove it through restraint.",
            "The Force around you feels bruised. Stop feeding anger before it starts speaking with your voice.",
            "Your victories are beginning to smell like punishment. Justice is not appetite wearing a robe.",
            "A Jedi Knight cannot be made from technique alone. You must become safe for the weak to stand near."
    };

    private static final String[] DARK_WARNINGS = {
            "You spare enemies when fear would have made a cleaner lesson. Decide whether mercy is tactic or weakness.",
            "Your hesitation costs strength. If you serve the dark, do not pretend every retreat is wisdom.",
            "You lean toward compassion. Useful, sometimes, but dangerous if it commands you.",
            "You want power, but you still ask permission from the guilt of weaker teachers.",
            "A Sith who cannot choose is just a frightened student with sharper tools."
    };

    private static final String[] NEUTRAL_WARNINGS = {
            "Balance is not hiding between two banners. It is choosing clearly without becoming owned by either extreme.",
            "Your path wavers. Read, act, and return when your choices are less noisy.",
            "A neutral guide stands with the innocent against the cruel. Remember that neutrality is not indifference.",
            "You drift toward one flame too often. Balance requires deliberate correction, not denial.",
            "White is not absence. White is discipline held between extremes."
    };

    private static final String[] LIGHT_RESPONSES = {
            "I will defend without cruelty.", "The innocent come before my pride.", "I will win cleanly or learn why I could not.",
            "I will study before I demand power.", "I will stop when the threat is ended.", "A student should inherit restraint from me.",
            "I will not confuse anger for courage.", "I will protect those who cannot repay me.", "I can win and still choose mercy.",
            "My blade should be the last answer, not the first.", "I will make my strength safe to stand beside.", "I will complete the task without punishing the innocent.",
            "A Knight should be measured by what survives their victory.", "I will lead by restraint before I lead by command."
    };

    private static final String[] DARK_RESPONSES = {
            "Power should answer hesitation with command.", "Enemies who return are lessons left unfinished.", "Fear can protect what kindness cannot.",
            "I will take strength where it hides.", "Mercy is useful only when it serves victory.", "My apprentice will learn not to beg the world for permission.",
            "I will turn fear into obedience before it turns into failure.", "I will not leave rivals strong enough to return.", "I will claim power and make it answer me.",
            "A weak lesson deserves to be broken.", "I will teach through consequence, not comfort.", "The galaxy respects strength when kindness cannot hold it.",
            "I will decide who is useful and who is finished.", "Victory is the only apology power needs."
    };

    private static final String[] NEUTRAL_RESPONSES = {
            "I will choose by the moment, not by a banner.", "Balance means correcting myself before the Force has to.", "I will defend life without kneeling to either extreme.",
            "I will read the old records and test them in action.", "A white blade must still have a spine.", "I will hold the center without becoming passive.",
            "I will correct my drift before it rules me.", "I will defend life without belonging to one banner.", "I will keep power useful and judgment clear.",
            "I can balance mercy with consequence.", "I will study both extremes without serving either.", "The center is a discipline, not a hiding place.",
            "I will act when cruelty rises, even if neutrality is misunderstood.", "I will keep my apprentice from mistaking balance for weakness."
    };

    private static final String[] STORY_AGES = {
            "the first temple migrations", "the old hyperspace pilgrimages", "the era of hidden academies", "the silent archive wars",
            "the river trials of the early blade-forgers", "the mountain conclaves before the great codifications",
            "the age when wandering teachers carried lessons between worlds", "the centuries before the great fracture", "the ash-road pilgrimages",
            "the orchard sanctuaries of the outer colonies", "the ice-moon meditations", "the reef observatories", "the buried city vigils",
            "the era of oathless guides", "the first forge-schools", "the age of divided temples", "the lantern archives",
            "the rain-season councils", "the bronze-map expeditions", "the eclipse retreats"
    };

    private static final String[] LIGHT_SUBJECTS = {
            "a healer who refused glory after saving a village from a warlord",
            "a quiet defender who disarmed raiders and then rebuilt their burned granary",
            "a scout who carried refugees through a storm instead of chasing a fleeing rival",
            "a blade-smith who delayed a duel until every child had left the courtyard",
            "a scholar who proved patience could end a siege faster than revenge",
            "a sentinel who stood alone at a bridge and never struck first",
            "a teacher who expelled a gifted student for cruelty, then left food for them on the road",
            "a farmer-adept who hid an archive inside seed vaults to protect future learners",
            "a guardian who spared a defeated captain and gained a witness instead of a corpse",
            "a council messenger who chose truth over promotion"
    };

    private static final String[] DARK_SUBJECTS = {
            "an ambitious war-seer who learned that fear commands quickly but never loyally",
            "a fortress pupil who won every duel and still lost every ally",
            "a relic hunter who fed anger into a broken focus crystal until it answered too loudly",
            "a masked instructor who mistook obedience for loyalty",
            "a conqueror who spared one enemy only because fear made a better messenger",
            "a student who traded comfort for strength and then forgot why strength mattered",
            "a strategist who broke a city without touching its gates",
            "a duelist who discovered that domination leaves no one honest enough to warn you",
            "a commander who could command armies but not their own hunger",
            "a keeper of red archives who wrote every victory twice and every failure never"
    };

    private static final String[] NEUTRAL_SUBJECTS = {
            "a white-bladed guide who defended refugees while refusing imperial titles and temple politics",
            "a mediator who carried two enemy banners into a burning archive and came out with neither",
            "a hermit-teacher who judged students by whether they could change course without shame",
            "a roadwarden who hunted predators but spared the starving",
            "an archivist who wrote dark warnings and light vows on the same page",
            "a healer who treated both sides, then banished the one who returned with cruelty",
            "a wanderer who chose balance by defending villages from raiders while refusing a crown",
            "a white-saber smith who taught that neutrality must still have teeth",
            "a moon-temple guide who measured balance by corrected action, not equal numbers",
            "a lost master who rejected banners but never ignored suffering"
    };

    private static final String[] STORY_CONFLICTS = {
            "Their trial began when a datacron map led to a ruin already claimed by desperate survivors",
            "The lesson was recorded after a student mistook a saber part cache for permission to seek battle",
            "The archive says the decisive moment came during a night march with no food, no shelter, and no praise",
            "Their name survives because they stopped before victory became punishment",
            "The holobook preserves the argument they had with their own master after the mission",
            "The old record notes that the real enemy was not the raiders, but the student's need to be admired",
            "They were tested by a village that begged for protection and a rival who begged for mercy",
            "A broken holocron forced them to choose between faster power and cleaner judgment",
            "They carried a wounded apprentice through enemy ground and learned what their teachings cost",
            "They entered the ruin seeking strength and left with a rule they would never break",
            "A hidden forge accepted their crystal only after their anger cooled",
            "The story survives in fragments because the teacher wanted the task remembered more than their name"
    };

    private static final String[] STORY_ENDINGS = {
            "The record ends with a repaired hilt, an unfinished apology, and a warning to future students.",
            "Later teachers copied the passage because it turns a simple quest into a mirror.",
            "The final page says the Force grew quiet only after the survivor chose what not to do.",
            "No statue was raised; the archive keeper wrote that this was probably the point.",
            "The student's next lesson was not stronger power, but carrying water for those harmed by the conflict.",
            "The account became a common first reading for apprentices who asked for rank before discipline.",
            "A margin note says the lesson should be reread after every major victory.",
            "The holobook closes by asking whether the reader seeks wisdom, permission, or an excuse.",
            "The last line warns that every tradition rots when it teaches technique without consequence.",
            "A later guide added that balance, mercy, and ambition all become dangerous when they stop being examined."
    };

    private static final String[] LIGHT_MORALS = {
            "Compassion becomes stronger when it is disciplined.", "Mercy must be brave enough to protect people after the blade is lowered.",
            "A defender is measured by who can safely stand nearby.", "The light is not softness; it is strength made accountable.",
            "Restraint without courage abandons the innocent."
    };

    private static final String[] DARK_MORALS = {
            "Power grows fastest when desire is focused, but it devours the careless.", "Fear is a tool, not a throne.",
            "Victory without control becomes another master.", "Ambition sharpens the blade, but hunger decides where it falls.",
            "A rival spared for strategy is different from mercy obeyed by weakness."
    };

    private static final String[] NEUTRAL_MORALS = {
            "Balance is active judgment, not passive distance.", "A white blade must defend life without becoming owned by either extreme.",
            "Neutrality is a discipline, not a hiding place.", "The center must correct itself before it becomes cowardice.",
            "To study both extremes is useful; to excuse both is failure."
    };

    private ForceUserDialogueEngine() {}

    public record DialogueScreenPayload(List<String> lines, ForceQuestInventory.DialogueChoice[] choices) {}

    public static DialogueScreenPayload buildScreenPayload(ForceUserEntity teacher, ServerPlayer player, boolean mentorBond) {
        Random random = new Random(teacher.getPersonalitySeed() ^ player.tickCount ^ player.getUUID().getLeastSignificantBits());
        ForceUserSide side = teacher.getForceUserSide();
        String temperament = pick(TEMPERAMENTS, random);
        String style = pick(TEACHING_STYLES, random);
        String lesson = pick(LESSON_FORMS, random);

        List<String> lines = new ArrayList<>();
        lines.add(teacher.getDisplayName().getString() + " studies you with a " + temperament + " expression and " + style + ".");
        lines.add("Teaching: " + lesson);

        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            int light = cap.getLightSidePoints();
            int dark = cap.getDarkSidePoints();
            int balance = cap.getAlignmentBalance();

            if (mentorBond) {
                if (side.isLight() && dark > light + 20) {
                    lines.add("Warning: " + pick(LIGHT_WARNINGS, random));
                } else if (side.isDark() && light > dark + 20) {
                    lines.add("Warning: " + pick(DARK_WARNINGS, random));
                } else if (side.isNeutral() && Math.abs(balance) > 35) {
                    lines.add("Warning: " + pick(NEUTRAL_WARNINGS, random));
                }
            }
        });

        ForceQuestInventory.DialogueChoice[] choices = buildChoices(side, random);
        ForceQuestInventory.setPendingDialogue(player, teacher, choices);
        return new DialogueScreenPayload(lines, choices);
    }

    public static void speakToOwner(ForceUserEntity teacher, ServerPlayer player, boolean mentorBond) {
        DialogueScreenPayload payload = buildScreenPayload(teacher, player, mentorBond);
        for (String line : payload.lines()) {
            player.displayClientMessage(Component.literal(line), false);
        }
        player.displayClientMessage(Component.literal("Choose your response:"), false);
        for (int i = 0; i < payload.choices().length; i++) {
            player.displayClientMessage(clickableChoice(i + 1, payload.choices()[i]), false);
        }
    }

    private static ForceQuestInventory.DialogueChoice[] buildChoices(ForceUserSide speakerSide, Random random) {
        ForceQuestInventory.DialogueChoice light = new ForceQuestInventory.DialogueChoice(pick(LIGHT_RESPONSES, random), ForceSide.LIGHT, speakerSide.isLight() ? 4 : 3);
        ForceQuestInventory.DialogueChoice dark = new ForceQuestInventory.DialogueChoice(pick(DARK_RESPONSES, random), ForceSide.DARK, speakerSide.isDark() ? 4 : 3);
        ForceQuestInventory.DialogueChoice neutral = new ForceQuestInventory.DialogueChoice(pick(NEUTRAL_RESPONSES, random), ForceSide.NEUTRAL, speakerSide.isNeutral() ? 4 : 3);
        if (speakerSide.isDark()) {
            return new ForceQuestInventory.DialogueChoice[]{dark, neutral, light};
        }
        if (speakerSide.isNeutral()) {
            return new ForceQuestInventory.DialogueChoice[]{neutral, light, dark};
        }
        return new ForceQuestInventory.DialogueChoice[]{light, neutral, dark};
    }

    private static MutableComponent clickableChoice(int number, ForceQuestInventory.DialogueChoice choice) {
        return Component.literal("[" + number + "] " + choice.text())
                .withStyle(style -> style
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gucdialogue " + number))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Choose this response."))));
    }

    public static Component holobookStory(ForceSide side, long seed) {
        Random random = new Random(seed);
        String subject = switch (side) {
            case LIGHT -> pick(LIGHT_SUBJECTS, random);
            case DARK -> pick(DARK_SUBJECTS, random);
            case NEUTRAL -> pick(NEUTRAL_SUBJECTS, random);
            default -> "a wanderer who mapped forgotten currents in the Force";
        };
        String moral = switch (side) {
            case LIGHT -> pick(LIGHT_MORALS, random);
            case DARK -> pick(DARK_MORALS, random);
            case NEUTRAL -> pick(NEUTRAL_MORALS, random);
            default -> "Knowledge without practice fades.";
        };
        return Component.literal("Holobook record from " + pick(STORY_AGES, random) + ": " + subject + ". "
                + pick(STORY_CONFLICTS, random) + ". " + pick(STORY_ENDINGS, random) + " Lesson: " + moral);
    }

    private static String pick(String[] values, Random random) {
        return values[Math.floorMod(random.nextInt(), values.length)];
    }
}
