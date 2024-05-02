package com.verdantartifice.primalmagick.common.research.keys;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class ResearchKeyTypesPM {
    private static final DeferredRegister<ResearchKeyType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.RESEARCH_KEY_TYPES, PrimalMagick.MODID);
    public static final Supplier<IForgeRegistry<ResearchKeyType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static final RegistryObject<ResearchKeyType<ResearchEntryKey>> RESEARCH_ENTRY = register("research_entry", ResearchEntryKey.CODEC);
    public static final RegistryObject<ResearchKeyType<ResearchStageKey>> RESEARCH_STAGE = register("research_stage", ResearchStageKey.CODEC);
    public static final RegistryObject<ResearchKeyType<ItemScanKey>> ITEM_SCAN = register("item_scan", ItemScanKey.CODEC);
    public static final RegistryObject<ResearchKeyType<EntityScanKey>> ENTITY_SCAN = register("entity_scan", EntityScanKey.CODEC);
    public static final RegistryObject<ResearchKeyType<StackCraftedKey>> STACK_CRAFTED = register("stack_crafted", StackCraftedKey.CODEC);
    public static final RegistryObject<ResearchKeyType<TagCraftedKey>> TAG_CRAFTED = register("tag_crafted", TagCraftedKey.CODEC);
    public static final RegistryObject<ResearchKeyType<RuneEnchantmentKey>> RUNE_ENCHANTMENT = register("rune_enchantment", RuneEnchantmentKey.CODEC);
    public static final RegistryObject<ResearchKeyType<RuneEnchantmentPartialKey>> RUNE_ENCHANTMENT_PARTIAL = register("rune_enchantment_partial", RuneEnchantmentPartialKey.CODEC);
    
    protected static <T extends AbstractResearchKey> RegistryObject<ResearchKeyType<T>> register(String id, Codec<T> codec) {
        return DEFERRED_TYPES.register(id, () -> new ResearchKeyType<T>(codec));
    }
}
