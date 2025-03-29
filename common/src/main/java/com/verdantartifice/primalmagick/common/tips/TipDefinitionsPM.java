package com.verdantartifice.primalmagick.common.tips;

import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class TipDefinitionsPM {
    public static final ResourceKey<TipDefinition> THANKS = create("thanks");
    public static final ResourceKey<TipDefinition> DISCORD = create("discord");
    public static final ResourceKey<TipDefinition> MORE_TIPS = create("more_tips");
    public static final ResourceKey<TipDefinition> EARTH_SHRINE_LOC = create("earth_shrine_loc");
    public static final ResourceKey<TipDefinition> SEA_SHRINE_LOC = create("sea_shrine_loc");
    public static final ResourceKey<TipDefinition> SKY_SHRINE_LOC = create("sky_shrine_loc");
    public static final ResourceKey<TipDefinition> SUN_SHRINE_LOC = create("sun_shrine_loc");
    public static final ResourceKey<TipDefinition> MOON_SHRINE_LOC = create("moon_shrine_loc");
    public static final ResourceKey<TipDefinition> NO_BLOOD_SHRINES = create("no_blood_shrines");
    public static final ResourceKey<TipDefinition> NO_INFERNAL_SHRINES = create("no_infernal_shrines");
    public static final ResourceKey<TipDefinition> NO_VOID_SHRINES = create("no_void_shrines");
    public static final ResourceKey<TipDefinition> NO_HALLOWED_SHRINES = create("no_hallowed_shrines");
    public static final ResourceKey<TipDefinition> GO_EXPLORE = create("go_explore");
    public static final ResourceKey<TipDefinition> NEW_DISCIPLINES = create("new_disciplines");
    public static final ResourceKey<TipDefinition> SALT = create("salt");
    public static final ResourceKey<TipDefinition> TREEFOLK = create("treefolk");
    public static final ResourceKey<TipDefinition> VIEW_AFFINITIES = create("view_affinities");
    public static final ResourceKey<TipDefinition> ANALYSIS = create("analysis");
    public static final ResourceKey<TipDefinition> NO_AFFINITIES = create("no_affinities");
    public static final ResourceKey<TipDefinition> RESEARCH_TABLE = create("research_table");
    public static final ResourceKey<TipDefinition> PROJECT_SUCCESS = create("project_success");
    public static final ResourceKey<TipDefinition> RESEARCH_BOLDNESS = create("research_boldness");
    public static final ResourceKey<TipDefinition> RESEARCH_AIDS = create("research_aids");
    public static final ResourceKey<TipDefinition> PERMANENT_ATTUNEMENT = create("permanent_attunement");
    public static final ResourceKey<TipDefinition> TEMPORARY_ATTUNEMENT = create("temporary_attunement");
    public static final ResourceKey<TipDefinition> BETTER_ANALYSIS = create("better_analysis");
    public static final ResourceKey<TipDefinition> SPENDING_MANA = create("spending_mana");
    public static final ResourceKey<TipDefinition> SOTU = create("sotu");
    public static final ResourceKey<TipDefinition> MANA_CHARGER = create("mana_charger");
    public static final ResourceKey<TipDefinition> STAVES = create("staves");
    public static final ResourceKey<TipDefinition> ROBES = create("robes");
    public static final ResourceKey<TipDefinition> RITUAL_SYMMETRY = create("ritual_symmetry");
    public static final ResourceKey<TipDefinition> INDUCED_ATTUNEMENT = create("induced_attunement");
    public static final ResourceKey<TipDefinition> AMBROSIA_CAP = create("ambrosia_cap");
    public static final ResourceKey<TipDefinition> POWER_RUNES = create("power_runes");
    public static final ResourceKey<TipDefinition> RUNE_HINTS = create("rune_hints");

    public static ResourceKey<TipDefinition> create(String name) {
        return ResourceKey.create(RegistryKeysPM.TIPS, ResourceUtils.loc(name));
    }
    
    protected static void register(BootstrapContext<TipDefinition> ctx, ResourceKey<TipDefinition> key, Function<ResourceKey<TipDefinition>, TipDefinition> func) {
        ctx.register(key, func.apply(key));
    }

    protected static void register(BootstrapContext<TipDefinition> ctx, ResourceKey<TipDefinition> key) {
        register(ctx, key, k -> TipDefinition.builder(k).build());
    }
    
    protected static void register(BootstrapContext<TipDefinition> ctx, ResourceKey<TipDefinition> key, ResourceKey<ResearchEntry> req) {
        register(ctx, key, k -> TipDefinition.builder(k).requiredResearch(req).build());
    }

    public static void bootstrap(BootstrapContext<TipDefinition> context) {
        register(context, THANKS);
        register(context, DISCORD);
        register(context, MORE_TIPS);
        register(context, EARTH_SHRINE_LOC);
        register(context, SEA_SHRINE_LOC);
        register(context, SKY_SHRINE_LOC);
        register(context, SUN_SHRINE_LOC);
        register(context, MOON_SHRINE_LOC);
        register(context, NO_BLOOD_SHRINES, ResearchEntries.DISCOVER_BLOOD);
        register(context, NO_INFERNAL_SHRINES, ResearchEntries.DISCOVER_INFERNAL);
        register(context, NO_VOID_SHRINES, ResearchEntries.DISCOVER_VOID);
        register(context, NO_HALLOWED_SHRINES, ResearchEntries.DISCOVER_HALLOWED);
        register(context, GO_EXPLORE);
        register(context, NEW_DISCIPLINES);
        register(context, SALT);
        register(context, TREEFOLK);
        register(context, VIEW_AFFINITIES, ResearchEntries.FIRST_STEPS);
        register(context, ANALYSIS, ResearchEntries.FIRST_STEPS);
        register(context, NO_AFFINITIES, ResearchEntries.FIRST_STEPS);
        register(context, RESEARCH_TABLE, ResearchEntries.THEORYCRAFTING);
        register(context, PROJECT_SUCCESS, ResearchEntries.THEORYCRAFTING);
        register(context, RESEARCH_BOLDNESS, ResearchEntries.THEORYCRAFTING);
        register(context, RESEARCH_AIDS, ResearchEntries.THEORYCRAFTING);
        register(context, PERMANENT_ATTUNEMENT, ResearchEntries.ATTUNEMENTS);
        register(context, TEMPORARY_ATTUNEMENT, ResearchEntries.ATTUNEMENTS);
        register(context, BETTER_ANALYSIS, ResearchEntries.UNLOCK_MAGITECH);
        register(context, SPENDING_MANA, ResearchEntries.TERRESTRIAL_MAGICK);
        register(context, SOTU, k -> TipDefinition.builder(k).requiredResearch(ResearchEntries.SECRETS_OF_THE_UNIVERSE, 0).build());
        register(context, MANA_CHARGER, ResearchEntries.WAND_CHARGER);
        register(context, STAVES, ResearchEntries.STAVES);
        register(context, ROBES, ResearchEntries.IMBUED_WOOL);
        register(context, RITUAL_SYMMETRY, ResearchEntries.BASIC_RITUAL);
        register(context, INDUCED_ATTUNEMENT, ResearchEntries.AMBROSIA);
        register(context, AMBROSIA_CAP, ResearchEntries.AMBROSIA);
        register(context, POWER_RUNES, ResearchEntries.BASIC_RUNEWORKING);
        register(context, RUNE_HINTS, ResearchEntries.BASIC_RUNEWORKING);
    }

    public static Stream<TipDefinition> stream(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.TIPS).stream();
    }

    @NotNull
    public static Optional<TipDefinition> getRandom(Player player, RandomSource randomSource) {
        List<TipDefinition> filteredTips = stream(player.registryAccess()).filter(tip -> tip.shouldShow(player)).toList();
        if (filteredTips.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(filteredTips.get(randomSource.nextInt(filteredTips.size())));
        }
    }
}
