package com.verdantartifice.primalmagick.common.components;

import java.util.List;
import java.util.function.UnaryOperator;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.concoctions.FuseType;
import com.verdantartifice.primalmagick.common.runes.Rune;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod data component types.
 * 
 * @author Daedalus4096
 */
public class DataComponentsPM {
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, PrimalMagick.MODID);
    
    public static void init() {
        DATA_COMPONENT_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<DataComponentType<Holder<BookDefinition>>> BOOK_DEFINITION = register("book_definition", builder -> builder.persistent(BookDefinition.HOLDER_CODEC).networkSynchronized(BookDefinition.STREAM_CODEC));
    public static final RegistryObject<DataComponentType<Holder<BookLanguage>>> BOOK_LANGUAGE = register("book_language", builder -> builder.persistent(BookLanguage.HOLDER_CODEC).networkSynchronized(BookLanguage.STREAM_CODEC));
    public static final RegistryObject<DataComponentType<String>> AUTHOR_OVERRIDE = register("author_override", builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));
    public static final RegistryObject<DataComponentType<Integer>> BOOK_GENERATION = register("book_generation", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final RegistryObject<DataComponentType<Integer>> TRANSLATED_COMPREHENSION = register("translated_comprehension", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    
    public static final RegistryObject<DataComponentType<ConcoctionType>> CONCOCTION_TYPE = register("concoction_type", builder -> builder.persistent(ConcoctionType.CODEC).networkSynchronized(ConcoctionType.STREAM_CODEC));
    public static final RegistryObject<DataComponentType<Integer>> CONCOCTION_DOSES = register("concoction_doses", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final RegistryObject<DataComponentType<FuseType>> FUSE_TYPE = register("fuse_type", builder -> builder.persistent(FuseType.CODEC).networkSynchronized(FuseType.STREAM_CODEC));
    
    public static final RegistryObject<DataComponentType<Integer>> STORED_EXPERIENCE = register("stored_experience", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final RegistryObject<DataComponentType<Boolean>> ENABLED = register("enabled", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    
    public static final RegistryObject<DataComponentType<Integer>> MANA_DISCOUNT = register("mana_discount", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final RegistryObject<DataComponentType<SourceList>> STORED_CENTIMANA = register("stored_centimana", builder -> builder.persistent(SourceList.CODEC).networkSynchronized(SourceList.STREAM_CODEC));
    
    public static final RegistryObject<DataComponentType<List<Rune>>> INSCRIBED_RUNES = register("inscribed_runes", builder -> builder.persistent(Rune.CODEC.listOf()).networkSynchronized(Rune.STREAM_CODEC.apply(ByteBufCodecs.list())));
    
    public static final RegistryObject<DataComponentType<SpellPackage>> SPELL_PACKAGE = register("spell_package", builder -> builder.persistent(SpellPackage.codec()).networkSynchronized(SpellPackage.streamCodec()));
    public static final RegistryObject<DataComponentType<List<SpellPackage>>> SPELL_PACKAGE_LIST = register("spell_package_list", builder -> builder.persistent(SpellPackage.codec().listOf()).networkSynchronized(SpellPackage.streamCodec().apply(ByteBufCodecs.list())));
    public static final RegistryObject<DataComponentType<Integer>> ACTIVE_SPELL_INDEX = register("active_spell_index", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    
    public static final RegistryObject<DataComponentType<WandCore>> WAND_CORE = register("wand_core", builder -> builder.persistent(WandCore.CODEC).networkSynchronized(WandCore.STREAM_CODEC));
    public static final RegistryObject<DataComponentType<WandCap>> WAND_CAP = register("wand_cap", builder -> builder.persistent(WandCap.CODEC).networkSynchronized(WandCap.STREAM_CODEC));
    public static final RegistryObject<DataComponentType<WandGem>> WAND_GEM = register("wand_gem", builder -> builder.persistent(WandGem.CODEC).networkSynchronized(WandGem.STREAM_CODEC));
    
    public static final RegistryObject<DataComponentType<WandCore>> WAND_CORE_APPEARANCE = register("wand_core_appearance", builder -> builder.persistent(WandCore.CODEC).networkSynchronized(WandCore.STREAM_CODEC));
    public static final RegistryObject<DataComponentType<WandCap>> WAND_CAP_APPEARANCE = register("wand_cap_appearance", builder -> builder.persistent(WandCap.CODEC).networkSynchronized(WandCap.STREAM_CODEC));
    public static final RegistryObject<DataComponentType<WandGem>> WAND_GEM_APPEARANCE = register("wand_gem_appearance", builder -> builder.persistent(WandGem.CODEC).networkSynchronized(WandGem.STREAM_CODEC));
    
    public static final RegistryObject<DataComponentType<BlockPos>> WAND_USE_POSITION = register("wand_use_position", builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC));
    
    public static final RegistryObject<DataComponentType<Integer>> WARD_LEVEL = register("ward_level", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    
    private static <T> RegistryObject<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return DATA_COMPONENT_TYPES.register(name, () -> operator.apply(DataComponentType.builder()).build());
    }
}
