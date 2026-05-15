package server.galaxyunderchaos.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.lightsaber.AdvancedLightsaberLegacyHilts;
import server.galaxyunderchaos.lightsaber.LightsaberPartType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Data-generates the Galaxy Under Chaos advancement tree using the same
 * AdvancementSubProvider / Advancement.Builder pattern used by Jurassic Reborn.
 */
public class ModAdvancementProvider implements AdvancementSubProvider {
    private static final String ROOT = "ancient_republic";

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer) {
        Map<String, Advancement> built = new HashMap<>();

        for (AdvancementData data : createAdvancements()) {
            Advancement.Builder builder = Advancement.Builder.advancement()
                    .display(
                            icon(data.icon()),
                            title(data.path()),
                            description(data.path()),
                            data.parent() == null ? new ResourceLocation("minecraft", "textures/gui/advancements/backgrounds/adventure.png") : null,
                            frame(data.frame()),
                            data.toast(),
                            data.announce(),
                            data.hidden()
                    );

            if (data.parent() != null) {
                Advancement parent = built.get(data.parent());
                if (parent == null) {
                    throw new IllegalStateException("Missing parent advancement '" + data.parent() + "' for '" + data.path() + "'");
                }
                builder.parent(parent);
            }

            for (CriterionData criterion : data.criteria()) {
                builder.addCriterion(criterion.name(), criterion.instance());
            }

            // Same OR behavior JR uses for "any of these items" advancements.
            // Without this, Minecraft requires every listed criterion by default.
            if (data.anyRequirement()) {
                builder.requirements(RequirementsStrategy.OR);
            }

            Advancement saved = builder.save(consumer, galaxyunderchaos.MODID + ":" + ROOT + "/" + data.path());
            built.put(data.path(), saved);
        }
    }

    private static List<AdvancementData> createAdvancements() {
        List<AdvancementData> advancements = new ArrayList<>();
        advancements.add(entry("root", null, "galaxyunderchaos:jedi_datacron", "task", false, false, false, false, tick("join")));
        advancements.add(entry("salvage_chromium", "root", "galaxyunderchaos:chromium_ingot", "task", true, false, false, false, hasItem("has_chromium", "galaxyunderchaos:chromium_ingot")));
        advancements.add(entry("salvage_titanium", "salvage_chromium", "galaxyunderchaos:titanium_ingot", "task", true, false, false, false, hasItem("has_titanium", "galaxyunderchaos:titanium_ingot")));
        advancements.add(entry("tempered_alloy", "salvage_titanium", "galaxyunderchaos:titanium_chromium_ingot", "task", true, false, false, false, hasItem("has_alloy", "galaxyunderchaos:titanium_chromium_ingot")));
        advancements.add(entry("navigation_matrix", "tempered_alloy", "galaxyunderchaos:navigation_computer", "task", true, false, false, false, hasItem("has_navigation_computer", "galaxyunderchaos:navigation_computer")));
        advancements.add(entry("hyperdrive_ready", "navigation_matrix", "galaxyunderchaos:portal_item", "task", true, false, false, false, hasItem("has_portal_item", "galaxyunderchaos:portal_item")));
        advancements.add(entry("crystal_ore_survey", "salvage_chromium", "galaxyunderchaos:blue_crystal_ore", "task", true, false, false, false, hasItem("has_blue_ore", "galaxyunderchaos:blue_crystal_ore")));
        advancements.add(orEntry("deep_mining_contract", "crystal_ore_survey", "galaxyunderchaos:titanium_deepslate_ore", "task", true, false, false, hasItem("has_deepslate_chromium", "galaxyunderchaos:chromium_deepslate_ore"), hasItem("has_deepslate_titanium", "galaxyunderchaos:titanium_deepslate_ore")));
        advancements.add(entry("forge_table", "tempered_alloy", "galaxyunderchaos:lightsaber_crafting_table", "task", true, false, false, false, hasItem("has_table", "galaxyunderchaos:lightsaber_crafting_table")));
        advancements.add(entry("internal_circuitry", "forge_table", "galaxyunderchaos:internal_lightsaber_circuitry", "task", true, false, false, false, hasItem("has_circuitry", "galaxyunderchaos:internal_lightsaber_circuitry")));
        advancements.add(entry("legacy_hilt", "internal_circuitry", "galaxyunderchaos:graflex_hilt", "task", true, false, false, false, hasItem("has_hilt", "galaxyunderchaos:graflex_hilt")));
        advancements.add(orEntry("modular_parts", "legacy_hilt", "galaxyunderchaos:graflex_emitter", "task", true, false, false, allLightsaberPartCriteria()));
        advancements.add(entry("first_plasma_blade", "legacy_hilt", "galaxyunderchaos:custom_lightsaber", "goal", true, true, false, false, hasItem("has_custom_saber", "galaxyunderchaos:custom_lightsaber")));
        advancements.add(entry("double_blade", "first_plasma_blade", "galaxyunderchaos:double_lightsaber", "goal", true, true, false, false, hasItem("has_double_saber", "galaxyunderchaos:double_lightsaber")));
        advancements.add(entry("crossguard_pattern", "first_plasma_blade", "galaxyunderchaos:knighted_hilt", "task", true, false, false, false, hasItem("has_knighted_hilt", "galaxyunderchaos:knighted_hilt")));
        advancements.add(entry("displayed_blade", "first_plasma_blade", "galaxyunderchaos:ground_lightsaber_stand", "task", true, false, false, false, hasItem("has_stand", "galaxyunderchaos:ground_lightsaber_stand")));
        advancements.add(entry("archival_display", "displayed_blade", "galaxyunderchaos:white_ground_lightsaber_stand", "task", true, false, false, false, hasItem("has_white_stand", "galaxyunderchaos:white_ground_lightsaber_stand")));
        advancements.add(orEntry("first_kyber", "crystal_ore_survey", "galaxyunderchaos:blue_kyber", "task", true, false, false, hasItem("has_red", "galaxyunderchaos:red_kyber"), hasItem("has_blue", "galaxyunderchaos:blue_kyber"), hasItem("has_orange", "galaxyunderchaos:orange_kyber"), hasItem("has_green", "galaxyunderchaos:green_kyber"), hasItem("has_yellow", "galaxyunderchaos:yellow_kyber"), hasItem("has_cyan", "galaxyunderchaos:cyan_kyber"), hasItem("has_white", "galaxyunderchaos:white_kyber"), hasItem("has_magenta", "galaxyunderchaos:magenta_kyber"), hasItem("has_purple", "galaxyunderchaos:purple_kyber"), hasItem("has_pink", "galaxyunderchaos:pink_kyber"), hasItem("has_lime_green", "galaxyunderchaos:lime_green_kyber"), hasItem("has_turquoise", "galaxyunderchaos:turquoise_kyber"), hasItem("has_blood_orange", "galaxyunderchaos:blood_orange_kyber"), hasItem("has_amber", "galaxyunderchaos:amber_kyber"), hasItem("has_gold", "galaxyunderchaos:gold_kyber"), hasItem("has_light_blue", "galaxyunderchaos:light_blue_kyber"), hasItem("has_dark_blue", "galaxyunderchaos:dark_blue_kyber"), hasItem("has_maroon", "galaxyunderchaos:maroon_kyber"), hasItem("has_deep_violet", "galaxyunderchaos:deep_violet_kyber"), hasItem("has_arctic_blue", "galaxyunderchaos:arctic_blue_kyber"), hasItem("has_rose_pink", "galaxyunderchaos:rose_pink_kyber")));
        advancements.add(entry("red_crystal", "first_kyber", "galaxyunderchaos:red_kyber", "task", true, false, false, false, hasItem("has_red", "galaxyunderchaos:red_kyber")));
        advancements.add(entry("bleeding_table", "red_crystal", "galaxyunderchaos:bleeding_table", "goal", true, false, false, false, hasItem("has_bleeding_table", "galaxyunderchaos:bleeding_table")));
        advancements.add(entry("compressed_focus", "first_kyber", "galaxyunderchaos:focusing_crystal_compressed", "task", true, false, false, false, hasItem("has_compressed", "galaxyunderchaos:focusing_crystal_compressed")));
        advancements.add(entry("cracked_focus", "compressed_focus", "galaxyunderchaos:focusing_crystal_cracked", "task", true, false, false, false, hasItem("has_cracked", "galaxyunderchaos:focusing_crystal_cracked")));
        advancements.add(entry("inverting_focus", "cracked_focus", "galaxyunderchaos:focusing_crystal_inverting", "task", true, false, false, false, hasItem("has_inverting", "galaxyunderchaos:focusing_crystal_inverting")));
        advancements.add(entry("fine_cut_focus", "inverting_focus", "galaxyunderchaos:focusing_crystal_fine_cut", "task", true, false, false, false, hasItem("has_fine_cut", "galaxyunderchaos:focusing_crystal_fine_cut")));
        advancements.add(entry("prismatic_focus", "fine_cut_focus", "galaxyunderchaos:focusing_crystal_prismatic", "goal", true, false, false, false, hasItem("has_prismatic", "galaxyunderchaos:focusing_crystal_prismatic")));
        advancements.add(entry("full_spectrum", "prismatic_focus", "galaxyunderchaos:rose_pink_kyber", "challenge", true, true, false, false, hasItem("has_red", "galaxyunderchaos:red_kyber"), hasItem("has_blue", "galaxyunderchaos:blue_kyber"), hasItem("has_orange", "galaxyunderchaos:orange_kyber"), hasItem("has_green", "galaxyunderchaos:green_kyber"), hasItem("has_yellow", "galaxyunderchaos:yellow_kyber"), hasItem("has_cyan", "galaxyunderchaos:cyan_kyber"), hasItem("has_white", "galaxyunderchaos:white_kyber"), hasItem("has_magenta", "galaxyunderchaos:magenta_kyber"), hasItem("has_purple", "galaxyunderchaos:purple_kyber"), hasItem("has_pink", "galaxyunderchaos:pink_kyber"), hasItem("has_lime_green", "galaxyunderchaos:lime_green_kyber"), hasItem("has_turquoise", "galaxyunderchaos:turquoise_kyber"), hasItem("has_blood_orange", "galaxyunderchaos:blood_orange_kyber"), hasItem("has_amber", "galaxyunderchaos:amber_kyber"), hasItem("has_gold", "galaxyunderchaos:gold_kyber"), hasItem("has_light_blue", "galaxyunderchaos:light_blue_kyber"), hasItem("has_dark_blue", "galaxyunderchaos:dark_blue_kyber"), hasItem("has_maroon", "galaxyunderchaos:maroon_kyber"), hasItem("has_deep_violet", "galaxyunderchaos:deep_violet_kyber"), hasItem("has_arctic_blue", "galaxyunderchaos:arctic_blue_kyber"), hasItem("has_rose_pink", "galaxyunderchaos:rose_pink_kyber")));
        advancements.add(entry("light_datacron", "root", "galaxyunderchaos:jedi_datacron", "task", true, false, false, false, hasItem("has_light_datacron", "galaxyunderchaos:jedi_datacron")));
        advancements.add(entry("dark_datacron", "root", "galaxyunderchaos:sith_datacron", "task", true, false, false, false, hasItem("has_dark_datacron", "galaxyunderchaos:sith_datacron")));
        advancements.add(entry("ancient_datacron", "light_datacron", "galaxyunderchaos:ancient_datacron", "task", true, false, false, false, hasItem("has_ancient_datacron", "galaxyunderchaos:ancient_datacron")));
        advancements.add(entry("light_holocron", "light_datacron", "galaxyunderchaos:jedi_holocron", "task", true, false, false, false, hasItem("has_light_holocron", "galaxyunderchaos:jedi_holocron")));
        advancements.add(entry("dark_holocron", "dark_datacron", "galaxyunderchaos:sith_holocron", "task", true, false, false, false, hasItem("has_dark_holocron", "galaxyunderchaos:sith_holocron")));
        advancements.add(entry("ancient_holocron", "ancient_datacron", "galaxyunderchaos:ancient_holocron", "task", true, false, false, false, hasItem("has_ancient_holocron", "galaxyunderchaos:ancient_holocron")));
        advancements.add(entry("force_archive_trinity", "ancient_holocron", "galaxyunderchaos:ancient_force_holocron", "goal", true, false, false, false, hasItem("has_light_force_holocron", "galaxyunderchaos:jedi_force_holocron"), hasItem("has_dark_force_holocron", "galaxyunderchaos:sith_force_holocron"), hasItem("has_ancient_force_holocron", "galaxyunderchaos:ancient_force_holocron")));
        advancements.add(entry("form_i_foundation", "first_plasma_blade", "galaxyunderchaos:shii_cho_holobook", "task", true, false, false, false, hasItem("has_shii_cho_holobook", "galaxyunderchaos:shii_cho_holobook")));
        advancements.add(entry("form_ii_duelist", "form_i_foundation", "galaxyunderchaos:makashi_holobook", "task", true, false, false, false, hasItem("has_makashi_holobook", "galaxyunderchaos:makashi_holobook")));
        advancements.add(entry("form_iii_shelter", "form_ii_duelist", "galaxyunderchaos:soresu_holobook", "task", true, false, false, false, hasItem("has_soresu_holobook", "galaxyunderchaos:soresu_holobook")));
        advancements.add(entry("form_iv_tempest", "form_iii_shelter", "galaxyunderchaos:ataru_holobook", "task", true, false, false, false, hasItem("has_ataru_holobook", "galaxyunderchaos:ataru_holobook")));
        advancements.add(entry("form_v_answer", "form_iv_tempest", "galaxyunderchaos:shien_djem_so_holobook", "task", true, false, false, false, hasItem("has_shien_djem_so_holobook", "galaxyunderchaos:shien_djem_so_holobook")));
        advancements.add(entry("form_vi_balance", "form_v_answer", "galaxyunderchaos:niman_holobook", "task", true, false, false, false, hasItem("has_niman_holobook", "galaxyunderchaos:niman_holobook")));
        advancements.add(entry("form_vii_ferocity", "form_vi_balance", "galaxyunderchaos:juyo_vaapad_holobook", "task", true, false, false, false, hasItem("has_juyo_vaapad_holobook", "galaxyunderchaos:juyo_vaapad_holobook")));
        advancements.add(entry("all_forms", "form_vii_ferocity", "galaxyunderchaos:juyo_vaapad_holobook", "challenge", true, true, false, false, hasItem("has_shii_cho_holobook", "galaxyunderchaos:shii_cho_holobook"), hasItem("has_makashi_holobook", "galaxyunderchaos:makashi_holobook"), hasItem("has_soresu_holobook", "galaxyunderchaos:soresu_holobook"), hasItem("has_ataru_holobook", "galaxyunderchaos:ataru_holobook"), hasItem("has_shien_djem_so_holobook", "galaxyunderchaos:shien_djem_so_holobook"), hasItem("has_niman_holobook", "galaxyunderchaos:niman_holobook"), hasItem("has_juyo_vaapad_holobook", "galaxyunderchaos:juyo_vaapad_holobook")));
        advancements.add(entry("temple_fabric", "root", "galaxyunderchaos:temple_guard_fabric", "task", true, false, false, false, hasItem("has_temple_fabric", "galaxyunderchaos:temple_guard_fabric")));
        advancements.add(entry("temple_guard_set", "temple_fabric", "galaxyunderchaos:temple_guard_chestplate", "goal", true, false, false, false, hasItem("has_helmet", "galaxyunderchaos:temple_guard_helmet"), hasItem("has_chestplate", "galaxyunderchaos:temple_guard_chestplate"), hasItem("has_leggings", "galaxyunderchaos:temple_guard_leggings"), hasItem("has_boots", "galaxyunderchaos:temple_guard_boots")));
        advancements.add(entry("shadow_guard_set", "temple_guard_set", "galaxyunderchaos:sith_guard_chestplate", "goal", true, false, false, false, hasItem("has_helmet", "galaxyunderchaos:sith_guard_helmet"), hasItem("has_chestplate", "galaxyunderchaos:sith_guard_chestplate"), hasItem("has_leggings", "galaxyunderchaos:sith_guard_leggings"), hasItem("has_boots", "galaxyunderchaos:sith_guard_boots")));
        advancements.add(entry("chitin_fragments", "root", "galaxyunderchaos:chitin_fragments", "task", true, false, false, false, hasItem("has_chitin", "galaxyunderchaos:chitin_fragments")));
        advancements.add(entry("acid_forged_plate", "chitin_fragments", "galaxyunderchaos:acid_forged_plate", "task", true, false, false, false, hasItem("has_plate", "galaxyunderchaos:acid_forged_plate")));
        advancements.add(entry("venom_sample", "acid_forged_plate", "galaxyunderchaos:acidic_venom_sac", "task", true, false, false, false, hasItem("has_venom", "galaxyunderchaos:acidic_venom_sac")));
        advancements.add(orEntry("wingmaw_field_notes", "chitin_fragments", "galaxyunderchaos:wingmaw_feather", "task", true, false, false, hasItem("has_hide", "galaxyunderchaos:wingmaw_hide"), hasItem("has_fang", "galaxyunderchaos:wingmaw_fang"), hasItem("has_feather", "galaxyunderchaos:wingmaw_feather")));
        advancements.add(entry("wingmaw_cookfire", "wingmaw_field_notes", "galaxyunderchaos:cooked_wingmaw_meat", "task", true, false, false, false, hasItem("has_cooked_meat", "galaxyunderchaos:cooked_wingmaw_meat")));
        advancements.add(entry("fang_blade", "wingmaw_field_notes", "galaxyunderchaos:wingmaw_blade", "task", true, false, false, false, hasItem("has_wingmaw_blade", "galaxyunderchaos:wingmaw_blade")));
        advancements.add(entry("frontier_hilt", "fang_blade", "galaxyunderchaos:wingmaw_hilt", "task", true, false, false, false, hasItem("has_wingmaw_hilt", "galaxyunderchaos:wingmaw_hilt")));
        advancements.add(entry("reach_tython", "hyperdrive_ready", "galaxyunderchaos:tython_portal", "task", true, false, false, false, changedDimension("to_tython", "galaxyunderchaos:tython")));
        advancements.add(entry("reach_dantooine", "reach_tython", "galaxyunderchaos:dantooine_portal", "task", true, false, false, false, changedDimension("to_dantooine", "galaxyunderchaos:dantooine")));
        advancements.add(entry("reach_ossus", "reach_dantooine", "galaxyunderchaos:ossus_portal", "task", true, false, false, false, changedDimension("to_ossus", "galaxyunderchaos:ossus")));
        advancements.add(entry("reach_ilum", "reach_ossus", "galaxyunderchaos:ilum_portal", "task", true, false, false, false, changedDimension("to_ilum", "galaxyunderchaos:ilum")));
        advancements.add(entry("reach_hoth", "reach_ilum", "galaxyunderchaos:hoth_portal", "task", true, false, false, false, changedDimension("to_hoth", "galaxyunderchaos:hoth")));
        advancements.add(entry("reach_naboo", "reach_hoth", "galaxyunderchaos:naboo_portal", "task", true, false, false, false, changedDimension("to_naboo", "galaxyunderchaos:naboo")));
        advancements.add(entry("reach_mustafar", "reach_naboo", "galaxyunderchaos:mustafar_portal", "task", true, false, false, false, changedDimension("to_mustafar", "galaxyunderchaos:mustafar")));
        advancements.add(entry("reach_korriban", "reach_mustafar", "galaxyunderchaos:korriban_portal", "task", true, false, false, false, changedDimension("to_korriban", "galaxyunderchaos:korriban")));
        advancements.add(entry("reach_malachor", "reach_korriban", "galaxyunderchaos:malachor_portal", "task", true, false, false, false, changedDimension("to_malachor", "galaxyunderchaos:malachor")));
        advancements.add(entry("reach_ashla", "reach_malachor", "galaxyunderchaos:ashla_portal", "task", true, false, false, false, changedDimension("to_ashla", "galaxyunderchaos:ashla")));
        advancements.add(entry("reach_bogan", "reach_ashla", "galaxyunderchaos:bogan_portal", "task", true, false, false, false, changedDimension("to_bogan", "galaxyunderchaos:bogan")));
        advancements.add(entry("chart_the_routes", "reach_bogan", "galaxyunderchaos:navigation_computer", "challenge", true, true, false, false, changedDimension("to_tython", "galaxyunderchaos:tython"), changedDimension("to_dantooine", "galaxyunderchaos:dantooine"), changedDimension("to_ossus", "galaxyunderchaos:ossus"), changedDimension("to_ilum", "galaxyunderchaos:ilum"), changedDimension("to_hoth", "galaxyunderchaos:hoth"), changedDimension("to_naboo", "galaxyunderchaos:naboo"), changedDimension("to_mustafar", "galaxyunderchaos:mustafar"), changedDimension("to_korriban", "galaxyunderchaos:korriban"), changedDimension("to_malachor", "galaxyunderchaos:malachor"), changedDimension("to_ashla", "galaxyunderchaos:ashla"), changedDimension("to_bogan", "galaxyunderchaos:bogan")));
        advancements.add(entry("tython_stonework", "reach_tython", "galaxyunderchaos:tython_temple_stone", "task", true, false, false, false, hasItem("has_tython_stone", "galaxyunderchaos:tython_temple_stone")));
        advancements.add(entry("ashla_stonework", "reach_ashla", "galaxyunderchaos:ashla_temple_stone", "task", true, false, false, false, hasItem("has_ashla_stone", "galaxyunderchaos:ashla_temple_stone")));
        advancements.add(entry("bogan_stonework", "reach_bogan", "galaxyunderchaos:bogan_temple_stone", "task", true, false, false, false, hasItem("has_bogan_stone", "galaxyunderchaos:bogan_temple_stone")));
        advancements.add(entry("korriban_stonework", "reach_korriban", "galaxyunderchaos:korriban_temple_stone", "task", true, false, false, false, hasItem("has_korriban_stone", "galaxyunderchaos:korriban_temple_stone")));
        advancements.add(entry("ancient_masonry", "tython_stonework", "galaxyunderchaos:ancient_temple_stone", "task", true, false, false, false, hasItem("has_ancient_stone", "galaxyunderchaos:ancient_temple_stone")));
        advancements.add(entry("shadow_masonry", "bogan_stonework", "galaxyunderchaos:dark_temple_stone", "task", true, false, false, false, hasItem("has_dark_stone", "galaxyunderchaos:dark_temple_stone")));
        advancements.add(entry("malachite_obsidian", "reach_malachor", "galaxyunderchaos:malachite_obsidian", "task", true, false, false, false, hasItem("has_malachite", "galaxyunderchaos:malachite_obsidian")));
        advancements.add(entry("council_seating", "ancient_masonry", "galaxyunderchaos:council_chair_1", "task", true, false, false, false, hasItem("has_council_chair", "galaxyunderchaos:council_chair_1")));
        advancements.add(entry("temple_chambers", "council_seating", "galaxyunderchaos:tython_temple_chair_1", "task", true, false, false, false, hasItem("has_temple_chair", "galaxyunderchaos:tython_temple_chair_1")));
        advancements.add(orEntry("statue_recovery", "temple_chambers", "galaxyunderchaos:jedi_guard_statue", "goal", true, false, false, hasItem("has_light_statue", "galaxyunderchaos:jedi_guard_statue"), hasItem("has_dark_statue", "galaxyunderchaos:sith_guard_statue")));
        advancements.add(orEntry("sealed_sarcophagi", "shadow_masonry", "galaxyunderchaos:sith_coffin", "goal", true, false, false, hasItem("has_light_coffin", "galaxyunderchaos:jedi_coffin"), hasItem("has_dark_coffin", "galaxyunderchaos:sith_coffin")));
        advancements.add(entry("heart_berry_grove", "reach_ossus", "galaxyunderchaos:heart_berry_sapling", "task", true, false, false, false, hasItem("has_sapling", "galaxyunderchaos:heart_berry_sapling")));
        advancements.add(entry("ak_timber", "reach_tython", "galaxyunderchaos:ak_log", "task", true, false, false, false, hasItem("has_ak_log", "galaxyunderchaos:ak_log")));
        advancements.add(entry("frontier_shipwright", "ak_timber", "galaxyunderchaos:ak_boat", "task", true, false, false, false, hasItem("has_ak_boat", "galaxyunderchaos:ak_boat")));
        advancements.add(entry("settlement_founder", "heart_berry_grove", "galaxyunderchaos:heart_berry_chest_boat", "challenge", true, true, false, false, hasItem("has_heart_boat", "galaxyunderchaos:heart_berry_chest_boat"), hasItem("has_ak_boat", "galaxyunderchaos:ak_chest_boat"), hasItem("has_temple_chair", "galaxyunderchaos:tython_temple_chair_1"), hasItem("has_ancient_stone", "galaxyunderchaos:ancient_temple_stone")));
        advancements.add(entry("galactic_archive_complete", "chart_the_routes", "galaxyunderchaos:ancient_datacron", "challenge", true, true, false, false, hasItem("has_navigation_computer", "galaxyunderchaos:navigation_computer"), hasItem("has_custom_saber", "galaxyunderchaos:custom_lightsaber"), hasItem("has_ancient_datacron", "galaxyunderchaos:ancient_datacron"), hasItem("has_prismatic", "galaxyunderchaos:focusing_crystal_prismatic"), hasItem("has_form_vii", "galaxyunderchaos:juyo_vaapad_holobook"), hasItem("has_settlement", "galaxyunderchaos:heart_berry_chest_boat")));
        return advancements;
    }

    private static AdvancementData entry(String path, String parent, String icon, String frame, boolean toast, boolean announce, boolean hidden, boolean anyRequirement, CriterionData... criteria) {
        return new AdvancementData(path, parent, icon, frame, toast, announce, hidden, anyRequirement, criteria);
    }

    private static AdvancementData orEntry(String path, String parent, String icon, String frame, boolean toast, boolean announce, boolean hidden, CriterionData... criteria) {
        return entry(path, parent, icon, frame, toast, announce, hidden, true, criteria);
    }

    private static CriterionData[] allLightsaberPartCriteria() {
        List<CriterionData> criteria = new ArrayList<>();

        AdvancedLightsaberLegacyHilts.HILTS.keySet().stream().sorted().forEach(family -> {
            for (LightsaberPartType partType : LightsaberPartType.values()) {
                String partId = family + "_" + partType.getSerializedName();
                criteria.add(hasItem("has_" + partId, galaxyunderchaos.MODID + ":" + partId));
            }
        });

        return criteria.toArray(CriterionData[]::new);
    }

    private static CriterionData tick(String name) {
        return new CriterionData(name, PlayerTrigger.TriggerInstance.tick());
    }

    private static CriterionData hasItem(String name, String itemId) {
        return new CriterionData(name, InventoryChangeTrigger.TriggerInstance.hasItems(item(itemId)));
    }

    private static CriterionData changedDimension(String name, String dimensionId) {
        return new CriterionData(name, ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(dimension(dimensionId)));
    }

    private static ResourceKey<Level> dimension(String dimensionId) {
        ResourceLocation id = ResourceLocation.tryParse(dimensionId);
        if (id == null) {
            throw new IllegalArgumentException("Invalid Galaxy Under Chaos dimension id: " + dimensionId);
        }
        return ResourceKey.create(Registries.DIMENSION, id);
    }

    private static ItemLike icon(String itemId) {
        return item(itemId);
    }

    private static Item item(String itemId) {
        ResourceLocation id = ResourceLocation.tryParse(itemId);
        if (id == null) {
            throw new IllegalArgumentException("Invalid Galaxy Under Chaos item id: " + itemId);
        }

        Item item = ForgeRegistries.ITEMS.getValue(id);
        if (item == null || item == Items.AIR) {
            throw new IllegalStateException("Missing item for Galaxy Under Chaos advancement: " + itemId);
        }
        return item;
    }

    private static Component title(String path) {
        return Component.translatable("advancements." + galaxyunderchaos.MODID + "." + ROOT + "." + path + ".title");
    }

    private static Component description(String path) {
        return Component.translatable("advancements." + galaxyunderchaos.MODID + "." + ROOT + "." + path + ".description");
    }

    private static FrameType frame(String frame) {
        return FrameType.valueOf(frame.toUpperCase(Locale.ROOT));
    }

    private record CriterionData(String name, CriterionTriggerInstance instance) {
    }

    private record AdvancementData(String path, String parent, String icon, String frame, boolean toast, boolean announce, boolean hidden, boolean anyRequirement, CriterionData[] criteria) {
    }
}
