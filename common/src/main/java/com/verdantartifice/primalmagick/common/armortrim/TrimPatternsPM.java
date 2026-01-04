package com.verdantartifice.primalmagick.common.armortrim;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.List;

/**
 * Registry of mod armor trim patterns, backed by datapack JSON.
 * 
 * @author Daedalus4096
 */
public class TrimPatternsPM {
    public static final ResourceKey<TrimPattern> RUNIC = registryKey("runic");

    protected static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;
    protected static final Component RUNIC_ARMOR_TRIM_APPLIES_TO = Component.translatable(Util.makeDescriptionId("tooltip", ResourceUtils.loc("smithing_template.runic_armor_trim.applies_to"))).withStyle(DESCRIPTION_FORMAT);
    protected static final Component RUNIC_ARMOR_TRIM_INGREDIENTS = Component.translatable(Util.makeDescriptionId("tooltip", ResourceUtils.loc("smithing_template.runic_armor_trim.ingredients"))).withStyle(DESCRIPTION_FORMAT);
    protected static final Component RUNIC_ARMOR_TRIM_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("tooltip", ResourceUtils.loc("smithing_template.runic_armor_trim.base_slot_description")));
    protected static final Component RUNIC_ARMOR_TRIM_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("tooltip", ResourceUtils.loc("smithing_template.runic_armor_trim.additions_slot_description")));
    protected static final List<Identifier> RUNIC_TRIMMABLE_ARMOR_ICONS = List.of(Identifier.withDefaultNamespace("item/empty_armor_slot_helmet"), Identifier.withDefaultNamespace("item/empty_armor_slot_chestplate"), Identifier.withDefaultNamespace("item/empty_armor_slot_leggings"), Identifier.withDefaultNamespace("item/empty_armor_slot_boots"));
    protected static final List<Identifier> RUNIC_TRIMMABLE_MATERIAL_ICONS = List.of(ResourceUtils.loc("item/empty_rune_slot"));

    public static SmithingTemplateItem createRunicArmorTrimTemplate(Item.Properties properties) {
        return new SmithingTemplateItem(
                RUNIC_ARMOR_TRIM_APPLIES_TO, 
                RUNIC_ARMOR_TRIM_INGREDIENTS, 
                RUNIC_ARMOR_TRIM_BASE_SLOT_DESCRIPTION,
                RUNIC_ARMOR_TRIM_ADDITIONS_SLOT_DESCRIPTION, 
                RUNIC_TRIMMABLE_ARMOR_ICONS, 
                RUNIC_TRIMMABLE_MATERIAL_ICONS,
                properties);
    }

    private static ResourceKey<TrimPattern> registryKey(String name) {
        return ResourceKey.create(Registries.TRIM_PATTERN, ResourceUtils.loc(name));
    }
    
    private static void register(BootstrapContext<TrimPattern> context, ResourceKey<TrimPattern> patternKey) {
        context.register(patternKey, new TrimPattern(patternKey.identifier(), Component.translatable(Util.makeDescriptionId("trim_pattern", patternKey.identifier())), false));
    }
    
    public static void bootstrap(BootstrapContext<TrimPattern> context) {
        register(context, RUNIC);
    }
}
