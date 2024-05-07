package com.verdantartifice.primalmagick.common.research.keys;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class ResearchKeyTypesPM {
    private static final DeferredRegister<ResearchKeyType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.RESEARCH_KEY_TYPES, PrimalMagick.MODID);
    public static final Supplier<IForgeRegistry<ResearchKeyType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<ResearchKeyType<ResearchDisciplineKey>> RESEARCH_DISCIPLINE = register("research_discipline", ResearchDisciplineKey.CODEC, ResearchDisciplineKey::fromNetwork);
    public static final RegistryObject<ResearchKeyType<ResearchEntryKey>> RESEARCH_ENTRY = register("research_entry", ResearchEntryKey.CODEC, ResearchEntryKey::fromNetwork);
    public static final RegistryObject<ResearchKeyType<ResearchStageKey>> RESEARCH_STAGE = register("research_stage", ResearchStageKey.CODEC, ResearchStageKey::fromNetwork);
    public static final RegistryObject<ResearchKeyType<ItemScanKey>> ITEM_SCAN = register("item_scan", ItemScanKey.CODEC, ItemScanKey::fromNetwork);
    public static final RegistryObject<ResearchKeyType<EntityScanKey>> ENTITY_SCAN = register("entity_scan", EntityScanKey.CODEC, EntityScanKey::fromNetwork);
    public static final RegistryObject<ResearchKeyType<StackCraftedKey>> STACK_CRAFTED = register("stack_crafted", StackCraftedKey.CODEC, StackCraftedKey::fromNetwork);
    public static final RegistryObject<ResearchKeyType<TagCraftedKey>> TAG_CRAFTED = register("tag_crafted", TagCraftedKey.CODEC, TagCraftedKey::fromNetwork);
    public static final RegistryObject<ResearchKeyType<RuneEnchantmentKey>> RUNE_ENCHANTMENT = register("rune_enchantment", RuneEnchantmentKey.CODEC, RuneEnchantmentKey::fromNetwork);
    public static final RegistryObject<ResearchKeyType<RuneEnchantmentPartialKey>> RUNE_ENCHANTMENT_PARTIAL = register("rune_enchantment_partial", RuneEnchantmentPartialKey.CODEC, RuneEnchantmentPartialKey::fromNetwork);
    
    protected static <T extends AbstractResearchKey<T>> RegistryObject<ResearchKeyType<T>> register(String id, Codec<T> codec, FriendlyByteBuf.Reader<T> networkReader) {
        return DEFERRED_TYPES.register(id, () -> new ResearchKeyType<T>(PrimalMagick.resource(id), codec, networkReader));
    }
}
