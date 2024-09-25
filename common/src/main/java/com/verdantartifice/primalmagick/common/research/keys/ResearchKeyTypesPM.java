package com.verdantartifice.primalmagick.common.research.keys;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class ResearchKeyTypesPM {
    private static final DeferredRegister<ResearchKeyType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.RESEARCH_KEY_TYPES, Constants.MOD_ID);
    public static final Supplier<IForgeRegistry<ResearchKeyType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
    
    public static final RegistryObject<ResearchKeyType<ResearchDisciplineKey>> RESEARCH_DISCIPLINE = register("research_discipline", ResearchDisciplineKey.CODEC, ResearchDisciplineKey.STREAM_CODEC);
    public static final RegistryObject<ResearchKeyType<ResearchEntryKey>> RESEARCH_ENTRY = register("research_entry", ResearchEntryKey.CODEC, ResearchEntryKey.STREAM_CODEC);
    public static final RegistryObject<ResearchKeyType<ResearchStageKey>> RESEARCH_STAGE = register("research_stage", ResearchStageKey.CODEC, ResearchStageKey.STREAM_CODEC);
    public static final RegistryObject<ResearchKeyType<ItemScanKey>> ITEM_SCAN = register("item_scan", ItemScanKey.CODEC, ItemScanKey.STREAM_CODEC);
    public static final RegistryObject<ResearchKeyType<EntityScanKey>> ENTITY_SCAN = register("entity_scan", EntityScanKey.CODEC, EntityScanKey.STREAM_CODEC);
    public static final RegistryObject<ResearchKeyType<StackCraftedKey>> STACK_CRAFTED = register("stack_crafted", StackCraftedKey.CODEC, StackCraftedKey.STREAM_CODEC);
    public static final RegistryObject<ResearchKeyType<TagCraftedKey>> TAG_CRAFTED = register("tag_crafted", TagCraftedKey.CODEC, TagCraftedKey.STREAM_CODEC);
    public static final RegistryObject<ResearchKeyType<RuneEnchantmentKey>> RUNE_ENCHANTMENT = register("rune_enchantment", RuneEnchantmentKey.CODEC, RuneEnchantmentKey.STREAM_CODEC);
    public static final RegistryObject<ResearchKeyType<RuneEnchantmentPartialKey>> RUNE_ENCHANTMENT_PARTIAL = register("rune_enchantment_partial", RuneEnchantmentPartialKey.CODEC, RuneEnchantmentPartialKey.STREAM_CODEC);
    
    protected static <T extends AbstractResearchKey<T>> RegistryObject<ResearchKeyType<T>> register(String id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DEFERRED_TYPES.register(id, () -> new ResearchKeyType<T>(ResourceUtils.loc(id), codec, streamCodec));
    }
}
