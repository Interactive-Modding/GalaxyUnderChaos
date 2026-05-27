package server.galaxyunderchaos.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Built-in onboarding book for Galaxy Under Chaos.
 *
 * The real client book screen only reads vanilla written-book stacks, so this
 * item repairs/converts itself into a populated written book whenever it is
 * granted, opened, or found in an older save.
 */
public class GalacticGuideBookItem extends Item {
    public static final String GUIDE_TAG_VERSION = "GUCGuideVersion";
    private static final int CURRENT_VERSION = 4;
    private static final String TITLE = "Galaxy Under Chaos Guide";
    private static final String AUTHOR = "Galaxy Under Chaos";

    private static final String START = "Getting Started";
    private static final String PATHS = "Force Paths";
    private static final String FORCE = "Force Training";
    private static final String STRUCTURES = "Worlds + Structures";
    private static final String SABERS = "Lightsabers";
    private static final String SHIPS = "Ships";
    private static final String RANKS = "Ranks + Students";
    private static final String HELP = "Troubleshooting";

    private static final int COVER_PAGE = 1;
    private static final int PROGRESSION_PAGE = 2;
    private static final int CONTENTS_ONE_PAGE = 3;
    private static final int CONTENTS_TWO_PAGE = 4;
    private static final int CONTENTS_THREE_PAGE = 5;
    private static final int FIRST_GUIDE_PAGE = 6;

    public GalacticGuideBookItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);

        ItemStack guide = createGuideStack();
        if (held.hasCustomHoverName()) {
            guide.setHoverName(held.getHoverName());
        }

        player.setItemInHand(hand, guide);
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!level.isClientSide) {
            player.openItemGui(guide, hand);
        }

        return InteractionResultHolder.sidedSuccess(guide, level.isClientSide());
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        ensureGuideTag(stack);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Right-click to read the full Galaxy Under Chaos progression guide.").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Blueprints, planets, mentors, sabers, Force ranks, and ships.").withStyle(ChatFormatting.DARK_GRAY));
    }

    public static ItemStack createGuideStack() {
        ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
        ensureGuideTag(stack);
        return stack;
    }

    public static boolean isGuideBook(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.is(server.galaxyunderchaos.galaxyunderchaos.GALACTIC_GUIDE_BOOK.get())) {
            return true;
        }
        if (!stack.is(Items.WRITTEN_BOOK)) {
            return false;
        }
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return false;
        }
        return tag.getInt(GUIDE_TAG_VERSION) > 0
                || (TITLE.equals(tag.getString("title")) && AUTHOR.equals(tag.getString("author")));
    }

    public static void ensureGuideTag(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.getInt(GUIDE_TAG_VERSION) == CURRENT_VERSION && tag.contains("pages")) {
            return;
        }

        tag.putString("title", TITLE);
        tag.putString("author", AUTHOR);
        tag.putBoolean("resolved", true);
        tag.putInt("generation", 0);
        tag.putInt(GUIDE_TAG_VERSION, CURRENT_VERSION);

        ListTag pages = new ListTag();
        for (Component page : buildPages()) {
            pages.add(StringTag.valueOf(Component.Serializer.toJson(page)));
        }
        tag.put("pages", pages);
    }

    private static List<Component> buildPages() {
        List<GuideEntry> entries = guideEntries();
        GuideIndex index = buildIndex(entries);
        List<Component> pages = new ArrayList<>();

        pages.add(coverPage(index));
        pages.add(progressionPage(index));
        pages.add(contentsOne(index));
        pages.add(contentsTwo(index));
        pages.add(contentsThree(index));

        int pageNumber = FIRST_GUIDE_PAGE;
        for (int i = 0; i < entries.size(); i++) {
            GuideEntry entry = entries.get(i);
            int nextPage = i + 1 < entries.size() ? pageNumber + 1 : CONTENTS_ONE_PAGE;
            pages.add(guidePage(entry, nextPage));
            pageNumber++;
        }
        return pages;
    }

    private static GuideIndex buildIndex(List<GuideEntry> entries) {
        Map<String, Integer> byTitle = new LinkedHashMap<>();
        Map<String, Integer> bySection = new LinkedHashMap<>();
        int page = FIRST_GUIDE_PAGE;
        for (GuideEntry entry : entries) {
            byTitle.put(entry.title(), page);
            bySection.putIfAbsent(entry.section(), page);
            page++;
        }
        return new GuideIndex(byTitle, bySection);
    }

    private static MutableComponent coverPage(GuideIndex index) {
        MutableComponent page = heading("Galaxy Under Chaos");
        page.append(Component.literal(body(
                "Complete Player Guide",
                "",
                "Read this in order. The early game is ship-first, then world travel, then Force identity and mastery.",
                "",
                "Use the map below if you are lost.")));
        page.append(Component.literal("\n\n"));
        page.append(linkLine("Progression Map", PROGRESSION_PAGE));
        page.append(linkLine("Step 1: Blueprints", index.page("Step 1: Ship Blueprint")));
        page.append(linkLine("Step 3: Hyperdrive", index.page("Step 3: Make Hyperdrive")));
        page.append(linkLine("Become a Master", index.page("How Mastery Works")));
        return page;
    }

    private static MutableComponent progressionPage(GuideIndex index) {
        MutableComponent page = heading("Progression Map");
        page.append(Component.literal(body(
                "1. Find a ship blueprint.",
                "2. Make the Ship Crafting Table.",
                "3. Make the Hyperdrive.",
                "4. Travel to Force worlds.",
                "5. Find a master for your side.",
                "6. Lock identity + allegiance.",
                "7. Complete mentor trials.",
                "8. Build sabers, train students, master the Force.")));
        page.append(Component.literal("\n"));
        page.append(linkLine("Open Step 1", index.page("Step 1: Ship Blueprint")));
        page.append(linkLine("Open Hyperdrive", index.page("Step 3: Make Hyperdrive")));
        page.append(linkLine("Open Contents", CONTENTS_ONE_PAGE));
        return page;
    }

    private static MutableComponent contentsOne(GuideIndex index) {
        MutableComponent page = heading("Contents I");
        page.append(line("Sequential start"));
        page.append(linkLine("Progression Map", PROGRESSION_PAGE));
        page.append(linkLine("1 Ship Blueprint", index.page("Step 1: Ship Blueprint")));
        page.append(linkLine("2 Ship Table", index.page("Step 2: Ship Table")));
        page.append(linkLine("3 Hyperdrive", index.page("Step 3: Make Hyperdrive")));
        page.append(linkLine("4 Travel Worlds", index.page("Step 4: Travel Worlds")));
        page.append(linkLine("5 Find Master", index.page("Step 5: Find Master")));
        page.append(linkLine("6 Lock Identity", index.page("Step 6: Lock Identity")));
        page.append(linkLine("Contents II", CONTENTS_TWO_PAGE));
        page.append(linkLine("Contents III", CONTENTS_THREE_PAGE));
        return page;
    }

    private static MutableComponent contentsTwo(GuideIndex index) {
        MutableComponent page = heading("Contents II");
        page.append(line("Force path + relics"));
        page.append(linkLine("Choosing a Side", index.page("Choosing a Side")));
        page.append(linkLine("Mentor Trials", index.page("Mentor Trial System")));
        page.append(linkLine("Quest Types", index.page("Trial Types")));
        page.append(linkLine("Worlds + Structures", index.section(STRUCTURES)));
        page.append(linkLine("Tython", index.page("Tython")));
        page.append(linkLine("Korriban", index.page("Korriban")));
        page.append(linkLine("Holobooks", index.page("Holobooks")));
        page.append(linkLine("Datacrons", index.page("Datacrons")));
        page.append(linkLine("Lightsabers", index.section(SABERS)));
        page.append(linkLine("Contents III", CONTENTS_THREE_PAGE));
        return page;
    }

    private static MutableComponent contentsThree(GuideIndex index) {
        MutableComponent page = heading("Contents III");
        page.append(line("Ships, ranks, help"));
        page.append(linkLine("Ship Details", index.page("Blueprint Locations")));
        page.append(linkLine("Flying Ships", index.page("Flying Ships")));
        page.append(linkLine("Ranks + Students", index.section(RANKS)));
        page.append(linkLine("Jedi Master", index.page("Jedi Master Route")));
        page.append(linkLine("Neutral Master", index.page("Neutral Master Route")));
        page.append(linkLine("Sith Lord", index.page("Sith Lord Route")));
        page.append(linkLine("Troubleshooting", index.section(HELP)));
        page.append(linkLine("Contents I", CONTENTS_ONE_PAGE));
        page.append(linkLine("Progression Map", PROGRESSION_PAGE));
        return page;
    }

    private static MutableComponent guidePage(GuideEntry entry, int nextPage) {
        MutableComponent page = heading(entry.title());
        page.append(Component.literal(body(entry.lines())));
        page.append(Component.literal("\n\n"));
        page.append(link("Contents", CONTENTS_ONE_PAGE));
        page.append(Component.literal("  |  "));
        page.append(link("Map", PROGRESSION_PAGE));
        page.append(Component.literal("  |  "));
        page.append(link("Next", nextPage));
        return page;
    }

    private static List<GuideEntry> guideEntries() {
        return List.of(
                entry(START, "Read This First",
                        "Galaxy Under Chaos is sequential. Do not start by picking random powers.",
                        "",
                        "Correct order: blueprint, ship table, hyperdrive, world travel, master, identity lock, then Force progression."),
                entry(START, "Step 1: Ship Blueprint",
                        "Your first major goal is a Novadive or Flashfire Blueprint.",
                        "",
                        "Best odds: Desert Pyramid chests have a 25% chance for each blueprint. Mineshaft chest minecarts have an 18% chance for each blueprint."),
                entry(START, "Blueprint Locations",
                        "Other 12% blueprint locations: shipwreck map, shipwreck treasure, buried treasure, ruined portal, jungle temple, simple dungeon, stronghold corridor, stronghold library, ancient city, ancient city ice box, and big/small underwater ruins.",
                        "",
                        "Each blueprint rolls separately. One chest can fail, one can roll Novadive, one can roll Flashfire, or rarely both rolls can succeed."),
                entry(START, "Step 2: Ship Table",
                        "After you find a blueprint, craft the Ship Crafting Table.",
                        "",
                        "Recipe: light gray concrete across the top, gray concrete on the left/right sides of the lower rows, and a diamond in the center column."),
                entry(START, "Build Your First Ship",
                        "Place the Ship Crafting Table, insert the Novadive or Flashfire Blueprint, choose colors, then take the output ship.",
                        "",
                        "The blueprint is the key item. The table uses it to generate the finished ship item with your chosen color data."),
                entry(START, "Step 3: Make Hyperdrive",
                        "Next make the Hyperdrive chain, because ships alone do not unlock the wider galaxy.",
                        "",
                        "First craft a Navigation Computer: titanium ingots across the top, redstone on left/right, compass in center, glass at bottom center."),
                entry(START, "Hyperdrive Core Recipe",
                        "Craft the base Hyperdrive with diamonds across the top, titanium-chromium ingots on left/right, Navigation Computer in center, and quartz at bottom center.",
                        "",
                        "Titanium-chromium ingot recipe: titanium ingot above chromium ingot."),
                entry(START, "Planet Hyperdrives",
                        "Planet Hyperdrives wrap the base Hyperdrive with 8 matching world-themed items.",
                        "",
                        "Tython uses mossy cobblestone. Korriban uses red sand. Ilum uses snow. Hoth uses snowballs. Mustafar uses basalt. Malachor uses dead bush. Dantooine uses stripped birch logs. Ossus uses bamboo. Ashla uses calcite. Bogan uses blackstone. Naboo uses shuura."),
                entry(START, "Step 4: Travel Worlds",
                        "Enter your Novadive or Flashfire, climb above Y=140, then use the correct planet Hyperdrive from inside the ship.",
                        "",
                        "If the game says the Hyperdrive must be used from inside a ship or above Y=140, that is the intended gate."),
                entry(START, "Travel Priority",
                        "Early Force routes usually want Tython, Korriban, Ossus, Ilum, Dantooine, Malachor, Mustafar, Ashla, or Bogan depending on your path.",
                        "",
                        "Travel is before mastery because mentors, holocrons, datacrons, kyber, coffins, and trials are spread across worlds."),
                entry(START, "Step 5: Find Master",
                        "After you can travel, find a Force master matching the side you want: Jedi/Light, Sith/Dark, or Neutral.",
                        "",
                        "Right-click once to get the offer text. Right-click again within the offer window to choose that Force user as your mentor."),
                entry(START, "Choosing a Master",
                        "Pick the mentor for the path you want before locking your identity. Same-side masters are the cleanest route because quests, robes, holocrons, and rank titles all follow allegiance.",
                        "",
                        "If a Force user already follows someone else, it cannot become your mentor."),
                entry(START, "Step 6: Lock Identity",
                        "Once you know your route and master, lock your character identity and allegiance.",
                        "",
                        "Species/body identity is meant to be chosen once. Force name and side-appropriate robe choices can remain flexible, but changing species later should be treated as an admin reset."),
                entry(START, "Robes + Appearance",
                        "Robes are allegiance-gated. Jedi use Jedi robe sets, Sith use Sith robe sets, and Neutral characters use Neutral robe equivalents.",
                        "",
                        "Dark-side eyes unlock through Sith progression, not normal eye-color selection."),
                entry(START, "Force Name + Title",
                        "Your Force name is the name shown with your rank title.",
                        "",
                        "Example rank flow: Padawan -> Jedi Knight -> Jedi Master, Neutral Initiate -> Neutral Knight -> Neutral Master, or Acolyte -> Sith Apprentice -> Sith Lord."),

                entry(PATHS, "Choosing a Side",
                        "The three playable branches are Light, Dark, and Neutral. Your committed side affects holocrons, mentors, quests, robes, student types, ranks, NPC respect, and what graduation means.",
                        "",
                        "Do not train students until you are sure of the path."),
                entry(PATHS, "Changing Sides",
                        "If you already committed to a side, an opposing teacher or holocron can require renunciation before it accepts you.",
                        "",
                        "Renouncing a side can clear that side's mentor/student progress, so treat side changes as a real roleplay decision."),
                entry(PATHS, "Light Side Route",
                        "Light is the Jedi route: discipline, restraint, defense, study, and teaching.",
                        "",
                        "Progression focus: Jedi mentor -> 9 trials -> Jedi Knight -> train Padawan -> Jedi Master."),
                entry(PATHS, "Neutral Route",
                        "Neutral is active balance, not doing nothing. It uses ancient study, mixed archives, restraint, and practical action without becoming owned by Jedi or Sith doctrine.",
                        "",
                        "Progression focus: Neutral mentor -> 9 trials -> Neutral Knight -> train Neutral Padawan -> Neutral Master."),
                entry(PATHS, "Dark Side Route",
                        "Dark is the Sith route: ambition, rivalry, control, and power through conflict.",
                        "",
                        "Progression focus: Sith mentor -> 9 trials -> claim apprentice -> train apprentice -> defeat your bonded Sith master -> Sith Lord."),

                entry(FORCE, "Becoming Force-Sensitive",
                        "Force Sensitivity is tied to Force mentors and holocrons. A valid Force mentor can bind to you, then a holocron can unlock the first Force step.",
                        "",
                        "Watch chat messages. They tell you whether you need a mentor, datacrons, alignment points, or earlier knowledge."),
                entry(FORCE, "Mentor Trial System",
                        "Each side has a 9-trial mentor questline. Complete the active task, return to the mentor, and keep requested proof items in your inventory.",
                        "",
                        "Light/Neutral completion grants Knight standing. Sith completion means your Acolyte trials are done, but you are not Sith Lord yet."),
                entry(FORCE, "Trial Types",
                        "Possible trials include: read a side holobook, recover datacrons, gather saber parts, defeat hostile mobs, defeat rival Force users, visit ancient sites, meditate with mentor, defend innocents, gather holobooks, and prepare saber parts."),
                entry(FORCE, "Quest Progress Rules",
                        "Inventory tasks check items you carry. Structure tasks check nearby ancient/temple/relic blocks. Combat tasks check valid kill credit.",
                        "",
                        "If a quest reads 0 progress, stand closer, bring the student, or make sure the correct item type is in your inventory."),
                entry(FORCE, "Kill Credit",
                        "Combat trials can count direct player kills, projectile kills, Force-power kills, and valid bonded student/apprentice kills.",
                        "",
                        "Opposing Force-user quests require enemies outside your allegiance: Jedi, Sith, or neutral adepts depending on your path."),
                entry(FORCE, "Ancient Site Checks",
                        "Structure trials look around you for temple stone, holobook stones, holocrons, datacrons, relic blocks, statues, or carved ruin blocks.",
                        "",
                        "The safest method is to stand inside the actual ruin/temple room before returning to the quest screen."),
                entry(FORCE, "Datacrons",
                        "Datacrons are consumable training charges. Use/right-click one to bank +1 charge into your character instead of merely carrying it.",
                        "",
                        "Jedi datacrons feed Light training. Sith datacrons feed Dark training. Ancient datacrons feed Ancient/Neutral training."),
                entry(FORCE, "Holocrons",
                        "Holocrons are the main power-unlock interface. Jedi holocrons teach Light + Neutral + universal powers. Sith holocrons teach Dark + Neutral + universal powers.",
                        "",
                        "Ancient holocrons can display all branches but use ancient datacron costs."),
                entry(FORCE, "Unlock Costs",
                        "Normal Jedi/Sith holocrons cost 3 matching datacrons for selectable powers. Ancient holocrons cost 6 ancient datacrons.",
                        "",
                        "Force-level upgrades cost triple: 9 on Jedi/Sith holocrons, 18 on Ancient holocrons."),
                entry(FORCE, "Force Level Gates",
                        "Force Level I and II are early progression. Force Level III, IV, and V require completed student/apprentice training.",
                        "",
                        "That means you must become a teacher, finish a student line, and then return to holocron progression."),
                entry(FORCE, "Light Powers",
                        "Light powers include Force Heal, Fortify, and Stun. They focus on survival, protection, control, and stabilizing fights.",
                        "",
                        "Use them with saber blocking and careful positioning rather than trying to out-damage every enemy."),
                entry(FORCE, "Dark Powers",
                        "Dark powers include Force Drain, Lightning, and Wound. They focus on damage, pressure, target control, and self-sustain through aggression.",
                        "",
                        "Lightning is a held/channel ability, not a one-click projectile."),
                entry(FORCE, "Neutral Powers",
                        "Neutral powers include Stealth, Speed, Sight, Meditation, Blade Throw, Resist Energy, Push, and Rebound-style utility.",
                        "",
                        "These are useful on every path because mobility, scouting, recovery, and defense keep you alive."),
                entry(FORCE, "Force Keybinds",
                        "Default keys: G cycles Force power, R uses the selected Force power, H shows Force allegiance, V switches saber form, and X toggles the lightsaber.",
                        "",
                        "Ship zoom also uses X by default, so adjust controls if a conflict bothers you."),

                entry(STRUCTURES, "Why Structures Matter",
                        "Structures are not just decoration. They supply holobooks, datacrons, kyber, coffins, Sith/Jedi loot, relic blocks, mentor quest locations, and roleplay progression.",
                        "",
                        "Explore worlds methodically instead of expecting one chest to finish a path."),
                entry(STRUCTURES, "Known Structure Loot Areas",
                        "Current loot-table areas include Ashla, Bogan, Dantooine, Hoth, Ilum, Ilum-Hoth, Korriban, Malachor, Mustafar, Naboo, Ossus, and Tython.",
                        "",
                        "Each world leans toward different Force traditions and resources."),
                entry(STRUCTURES, "Tython",
                        "Tython is one of the best early Force destinations. Search Jedi temples, ancient ruins, balanced Force sites, holobooks, datacrons, and kyber sources.",
                        "",
                        "Use Tython when you are deciding between Light and Neutral routes."),
                entry(STRUCTURES, "Korriban",
                        "Korriban is the primary Sith destination. Search tombs, coffins, Sith ruins, dark holobooks, dark datacrons, and hostile Force-user encounters.",
                        "",
                        "Dark route players should expect danger, but this is where Sith progression makes the most sense."),
                entry(STRUCTURES, "Ilum + Hoth",
                        "Ilum and Hoth are strong kyber and cold-world exploration targets. Bring food, armor, and blocks for recovery.",
                        "",
                        "Use them when you need crystals and want safer exploration than Sith tomb worlds."),
                entry(STRUCTURES, "Ossus + Dantooine",
                        "Ossus and Dantooine fit archive, academy, and old Jedi/Neutral exploration themes.",
                        "",
                        "They are good worlds for holobooks, ancient study, and mentor trial objectives."),
                entry(STRUCTURES, "Malachor + Mustafar",
                        "Malachor and Mustafar are dangerous dark-side worlds. They are better after you have armor, food, a ship, a saber, and at least early Force powers.",
                        "",
                        "Do not treat them like peaceful mining trips."),
                entry(STRUCTURES, "Coffins + Special Loot",
                        "Jedi/Sith coffins and lord/master coffin variants can contain side loot, holobooks, datacrons, saber parts, and rare modifiers depending on the loot table.",
                        "",
                        "Search every coffin room carefully because some progression items are chance-based."),
                entry(STRUCTURES, "Holobooks",
                        "Holobooks are lore/progression books used by trials and saber/form progression.",
                        "",
                        "Keep at least one Light/Jedi, Dark/Sith, and Ancient/Neutral holobook. Do not throw them away after reading."),

                entry(SABERS, "Lightsabers",
                        "Sabers are not the first step. Build the ship and hyperdrive first so you can reach the worlds that supply kyber, holobooks, datacrons, and better loot.",
                        "",
                        "Once you have resources, use the Lightsaber Crafting Table for hilts, modular parts, kyber, circuitry, modifiers, and double sabers."),
                entry(SABERS, "Saber Crafting Table",
                        "The Lightsaber Crafting Table uses ancient holobook progression. Use it after you have explored Force worlds, not before.",
                        "",
                        "Common blocker: players have a kyber crystal but are missing the correct hilt/parts/circuitry setup."),
                entry(SABERS, "Preset Sabers",
                        "Preset mode uses a finished hilt item plus a kyber crystal. It is simpler than modular mode.",
                        "",
                        "If a recipe produces nothing, verify the hilt item is not just a part."),
                entry(SABERS, "Modular Sabers",
                        "Modular mode uses emitter, switch section, grip, pommel, internal lightsaber circuitry, and kyber.",
                        "",
                        "This mode is for custom combinations and more detailed saber identity."),
                entry(SABERS, "Modifier Crystals",
                        "Place a finished single saber into the saber table with modifier crystals in modifier slots. The table previews the modified result before you take it.",
                        "",
                        "Modifiers are late enough that you should not build your entire path around finding one immediately."),
                entry(SABERS, "Double Sabers",
                        "Double-bladed crafting uses two finished single lightsabers in the saber table. Both are consumed into one double saber output."),
                entry(SABERS, "Saber Forms",
                        "The form sequence is Shii-Cho, Makashi, Soresu, Ataru, Shien/Djem So, Niman, then Juyo/Vaapad.",
                        "",
                        "Forms are meant to change stance, block behavior, attack rhythm, and combat identity."),
                entry(SABERS, "Saber Troubleshooting",
                        "No output usually means one wrong slot or missing required item. Modular mode needs 4 parts + circuitry + kyber. Preset mode needs hilt + kyber. Modifier mode needs finished saber + modifier."),

                entry(SHIPS, "Ship Details",
                        "Ships are the practical start of Galaxy Under Chaos progression because they unlock travel and make the Force worlds reachable.",
                        "",
                        "Blueprint first, table second, hyperdrive third, then planets."),
                entry(SHIPS, "Flying Ships",
                        "Default ship controls: WASD to move, Space to ascend, C to descend, Q/E to roll, Left Alt or sprint to boost, Z/X to zoom third-person ship view.",
                        "",
                        "Engines spool before flight. Landing gear and cockpit animations are part of the ship behavior."),
                entry(SHIPS, "Hyperspace Safety",
                        "Use Hyperdrives while riding a Novadive or Flashfire above Y=140. The jump transfers the ship and bonded Force-user passengers with you.",
                        "",
                        "Do not abandon a ship in unloaded terrain without a return plan."),
                entry(SHIPS, "Ship Customization",
                        "The ship table stores colors in the ship item. Color sections are base, primary, secondary, and interior.",
                        "",
                        "Cockpit glass is intentionally not part of normal recolor logic."),

                entry(RANKS, "How Mastery Works",
                        "Mastery requires more than unlocking powers. You must finish mentor trials, prove your path through a student/apprentice, and then continue holocron progression.",
                        "",
                        "NPC respect only triggers when your committed side matches and you have trained a student."),
                entry(RANKS, "Student Limits",
                        "Students are allegiance-exclusive. You cannot train Jedi, Sith, and Neutral students at the same time.",
                        "",
                        "Limits: 2 Jedi Padawans, 2 Neutral Padawans, or 5 Sith apprentices/acolytes."),
                entry(RANKS, "Jedi Master Route",
                        "1. Find blueprint and make ship.",
                        "2. Make Hyperdrive and travel.",
                        "3. Find a Jedi mentor, then commit Light.",
                        "4. Complete 9 mentor trials to become Jedi Knight.",
                        "5. Train a Jedi Padawan through 5/5 trials to 100%.",
                        "6. The Padawan leaves as a Jedi Knight and you become Jedi Master."),
                entry(RANKS, "Neutral Master Route",
                        "1. Find blueprint and make ship.",
                        "2. Make Hyperdrive and travel.",
                        "3. Find a Neutral mentor, then commit Neutral.",
                        "4. Complete 9 mentor trials to become Neutral Knight.",
                        "5. Train a Neutral Padawan through 5/5 trials to 100%.",
                        "6. They leave as a Neutral Knight and you become Neutral Master."),
                entry(RANKS, "Sith Lord Route",
                        "1. Find blueprint and make ship.",
                        "2. Make Hyperdrive and travel.",
                        "3. Find a Sith mentor, then commit Dark.",
                        "4. Complete 9 Acolyte mentor trials.",
                        "5. Claim/train a Sith Acolyte into a Sith Apprentice through 5/5 trials.",
                        "6. Defeat your bonded Sith master yourself to become Sith Lord."),
                entry(RANKS, "Sith Betrayal Loop",
                        "A trained Sith Apprentice does not simply leave. After your Sith Lord ascension, that apprentice may challenge your rule.",
                        "",
                        "Defeat them to secure your rank. This betrayal is intended Sith progression."),
                entry(RANKS, "Student Graduation",
                        "Jedi and Neutral Padawans graduate at 100% training and leave direct service as Knights.",
                        "",
                        "Sith Acolytes become Sith Apprentices and continue under you until rivalry mechanics push them forward."),
                entry(RANKS, "Orders + Companions",
                        "Bonded Force users can be ordered to Stay, Wander, or Follow & Defend.",
                        "",
                        "Follow & Defend is best for active questing because many student/apprentice objectives require the learner nearby."),
                entry(RANKS, "Respect From Force Users",
                        "Same-side Force users can recognize you as Master once you have fully trained a student/apprentice on that path.",
                        "",
                        "They use your Force name in greetings, so set a name you actually want to see."),

                entry(HELP, "Quest Not Progressing",
                        "Read the exact objective. Carry-item quests need the item in inventory. Structure quests need proximity to valid blocks. Combat quests need valid kill credit. Student quests often require the student nearby."),
                entry(HELP, "No Holocron Unlock",
                        "Check four things: correct holocron side, parent power unlocked, enough datacron charges, and enough alignment points.",
                        "",
                        "Force Level III+ also requires completed student/apprentice training."),
                entry(HELP, "No Saber Output",
                        "Check slots. Emitter, switch section, grip, pommel, circuitry, and kyber are different requirements. A hilt item is not the same as four modular parts."),
                entry(HELP, "No Ship Blueprint",
                        "Blueprints are chance-based. Desert pyramids are best at 25%, mineshafts are 18%, and many other Overworld chests are 12%.",
                        "",
                        "Looting one chest with no blueprint is normal."),
                entry(HELP, "Hyperdrive Fails",
                        "Use the planet Hyperdrive while riding a Novadive or Flashfire and climb above Y=140 first.",
                        "",
                        "If you are standing on the ground, not inside a ship, or too low, the jump is supposed to fail."),
                entry(HELP, "Lost Progress",
                        "Force rank, identity, quest ledger, and trained-student status are stored on the player. Death should not wipe them.",
                        "",
                        "If a mentor dies, completed trials stay saved. Choose another valid mentor to continue."),
                entry(HELP, "Final Advice",
                        "Keep extra holobooks, datacrons, saber parts, kyber, circuitry, blueprints, and modifier crystals.",
                        "",
                        "Most late-game blockers happen because a player threw away a proof item that later becomes a quest requirement.")
        );
    }


    private static GuideEntry entry(String section, String title, String... lines) {
        return new GuideEntry(section, title, lines);
    }

    private static String body(String... lines) {
        return String.join("\n", lines);
    }

    private static MutableComponent heading(String title) {
        return Component.literal(title + "\n\n").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.BOLD);
    }

    private static MutableComponent line(String text) {
        return Component.literal(text + "\n").withStyle(ChatFormatting.GRAY);
    }

    private static MutableComponent linkLine(String text, int page) {
        MutableComponent line = Component.literal("• ").withStyle(ChatFormatting.DARK_GRAY);
        line.append(link(text, page));
        line.append(Component.literal("\n"));
        return line;
    }

    private static MutableComponent link(String text, int page) {
        int safePage = Math.max(COVER_PAGE, page);
        return Component.literal(text).withStyle(style -> style
                .withColor(ChatFormatting.BLUE)
                .withUnderlined(true)
                .withClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, Integer.toString(safePage)))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Open " + text))));
    }

    private record GuideEntry(String section, String title, String[] lines) {}

    private record GuideIndex(Map<String, Integer> byTitle, Map<String, Integer> bySection) {
        int page(String title) {
            return byTitle.getOrDefault(title, CONTENTS_ONE_PAGE);
        }

        int section(String section) {
            return bySection.getOrDefault(section, CONTENTS_ONE_PAGE);
        }
    }
}
