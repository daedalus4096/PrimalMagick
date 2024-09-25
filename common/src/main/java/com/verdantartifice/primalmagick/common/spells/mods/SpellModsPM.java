package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class SpellModsPM {
    private static final DeferredRegister<SpellModType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.SPELL_MOD_TYPES, Constants.MOD_ID);
    public static final Supplier<IForgeRegistry<SpellModType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
    
    public static final RegistryObject<SpellModType<EmptySpellMod>> EMPTY = register(EmptySpellMod.TYPE, 100, EmptySpellMod::getInstance, EmptySpellMod::getRequirement, SpellPropertiesPM.POWER, EmptySpellMod.CODEC, EmptySpellMod.STREAM_CODEC);
    public static final RegistryObject<SpellModType<AmplifySpellMod>> AMPLIFY = register(AmplifySpellMod.TYPE, 200, AmplifySpellMod::getInstance, AmplifySpellMod::getRequirement, SpellPropertiesPM.AMPLIFY_POWER, AmplifySpellMod.CODEC, AmplifySpellMod.STREAM_CODEC);
    public static final RegistryObject<SpellModType<BurstSpellMod>> BURST = register(BurstSpellMod.TYPE, 300, BurstSpellMod::getInstance, BurstSpellMod::getRequirement, SpellPropertiesPM.RADIUS, BurstSpellMod.CODEC, BurstSpellMod.STREAM_CODEC);
    public static final RegistryObject<SpellModType<QuickenSpellMod>> QUICKEN = register(QuickenSpellMod.TYPE, 400, QuickenSpellMod::getInstance, QuickenSpellMod::getRequirement, SpellPropertiesPM.HASTE, QuickenSpellMod.CODEC, QuickenSpellMod.STREAM_CODEC);
    public static final RegistryObject<SpellModType<MineSpellMod>> MINE = register(MineSpellMod.TYPE, 500, MineSpellMod::getInstance, MineSpellMod::getRequirement, SpellPropertiesPM.NON_ZERO_DURATION, MineSpellMod.CODEC, MineSpellMod.STREAM_CODEC);
    public static final RegistryObject<SpellModType<ForkSpellMod>> FORK = register(ForkSpellMod.TYPE, 600, ForkSpellMod::getInstance, ForkSpellMod::getRequirement, SpellPropertiesPM.FORKS, ForkSpellMod.CODEC, ForkSpellMod.STREAM_CODEC);
    
    protected static <T extends AbstractSpellMod<T>> RegistryObject<SpellModType<T>> register(String id, int sortOrder, Supplier<T> instanceSupplier, Supplier<AbstractRequirement<?>> requirementSupplier,
            Supplier<SpellProperty> tiebreaker, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DEFERRED_TYPES.register(id, () -> new SpellModType<T>(ResourceUtils.loc(id), sortOrder, instanceSupplier, requirementSupplier, tiebreaker, codec, streamCodec));
    }
}
