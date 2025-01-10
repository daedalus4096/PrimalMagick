package com.verdantartifice.primalmagick.common.research.keys;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ResearchKeyTypesPM {
    public static final IRegistryItem<ResearchKeyType<?>, ResearchKeyType<ResearchDisciplineKey>> RESEARCH_DISCIPLINE = register("research_discipline", ResearchDisciplineKey.CODEC, ResearchDisciplineKey.STREAM_CODEC);
    public static final IRegistryItem<ResearchKeyType<?>, ResearchKeyType<ResearchEntryKey>> RESEARCH_ENTRY = register("research_entry", ResearchEntryKey.CODEC, ResearchEntryKey.STREAM_CODEC);
    public static final IRegistryItem<ResearchKeyType<?>, ResearchKeyType<ResearchStageKey>> RESEARCH_STAGE = register("research_stage", ResearchStageKey.CODEC, ResearchStageKey.STREAM_CODEC);
    public static final IRegistryItem<ResearchKeyType<?>, ResearchKeyType<ItemScanKey>> ITEM_SCAN = register("item_scan", ItemScanKey.CODEC, ItemScanKey.STREAM_CODEC);
    public static final IRegistryItem<ResearchKeyType<?>, ResearchKeyType<EntityScanKey>> ENTITY_SCAN = register("entity_scan", EntityScanKey.CODEC, EntityScanKey.STREAM_CODEC);
    public static final IRegistryItem<ResearchKeyType<?>, ResearchKeyType<StackCraftedKey>> STACK_CRAFTED = register("stack_crafted", StackCraftedKey.CODEC, StackCraftedKey.STREAM_CODEC);
    public static final IRegistryItem<ResearchKeyType<?>, ResearchKeyType<TagCraftedKey>> TAG_CRAFTED = register("tag_crafted", TagCraftedKey.CODEC, TagCraftedKey.STREAM_CODEC);
    public static final IRegistryItem<ResearchKeyType<?>, ResearchKeyType<RuneEnchantmentKey>> RUNE_ENCHANTMENT = register("rune_enchantment", RuneEnchantmentKey.CODEC, RuneEnchantmentKey.STREAM_CODEC);
    public static final IRegistryItem<ResearchKeyType<?>, ResearchKeyType<RuneEnchantmentPartialKey>> RUNE_ENCHANTMENT_PARTIAL = register("rune_enchantment_partial", RuneEnchantmentPartialKey.CODEC, RuneEnchantmentPartialKey.STREAM_CODEC);
    
    protected static <T extends AbstractResearchKey<T>> IRegistryItem<ResearchKeyType<?>, ResearchKeyType<T>> register(String id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return Services.RESEARCH_KEY_TYPES_REGISTRY.register(id, () -> new ResearchKeyType<T>(ResourceUtils.loc(id), codec, streamCodec));
    }
}
