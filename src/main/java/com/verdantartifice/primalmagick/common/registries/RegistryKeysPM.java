package com.verdantartifice.primalmagick.common.registries;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.Culture;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.ResearchKeyType;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementType;
import com.verdantartifice.primalmagick.common.runes.RuneEnchantmentDefinition;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModType;
import com.verdantartifice.primalmagick.common.theorycrafting.ProjectTemplate;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ProjectMaterialType;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.RewardType;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.WeightFunctionType;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * Home for resource keys for custom mod registries.
 * 
 * @author Daedalus4096
 */
public class RegistryKeysPM {
    public static final ResourceKey<Registry<BookDefinition>> BOOKS = key("books");
    public static final ResourceKey<Registry<BookLanguage>> BOOK_LANGUAGES = key("book_languages");
    public static final ResourceKey<Registry<Culture>> CULTURES = key("cultures");
    public static final ResourceKey<Registry<RequirementType<?>>> REQUIREMENT_TYPES = key("requirement_types");
    public static final ResourceKey<Registry<ResearchKeyType<?>>> RESEARCH_KEY_TYPES = key("research_key_types");
    public static final ResourceKey<Registry<ResearchDiscipline>> RESEARCH_DISCIPLINES = key("research_disciplines");
    public static final ResourceKey<Registry<ResearchEntry>> RESEARCH_ENTRIES = key("research_entries");
    public static final ResourceKey<Registry<ProjectTemplate>> PROJECT_TEMPLATES = key("project_templates");
    public static final ResourceKey<Registry<ProjectMaterialType<?>>> PROJECT_MATERIAL_TYPES = key("project_material_types");
    public static final ResourceKey<Registry<RewardType<?>>> PROJECT_REWARD_TYPES = key("project_reward_types");
    public static final ResourceKey<Registry<WeightFunctionType<?>>> PROJECT_WEIGHT_FUNCTION_TYPES = key("project_weight_function_types");
    public static final ResourceKey<Registry<RuneEnchantmentDefinition>> RUNE_ENCHANTMENT_DEFINITIONS = key("rune_enchantment_definitions");
    public static final ResourceKey<Registry<SpellProperty>> SPELL_PROPERTIES = key("spell_properties");
    public static final ResourceKey<Registry<SpellModType<?>>> SPELL_MOD_TYPES = key("spell_mods");
    
    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(PrimalMagick.resource(name));
    }
}
