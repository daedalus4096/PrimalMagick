package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class SpellModsPM {
    private static final DeferredRegister<SpellModType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.SPELL_MOD_TYPES, PrimalMagick.MODID);
    public static final Supplier<IForgeRegistry<SpellModType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<SpellModType<AmplifySpellMod>> AMPLIFY = register(AmplifySpellMod.TYPE, SpellPropertiesPM.AMPLIFY_POWER, AmplifySpellMod.CODEC, AmplifySpellMod.STREAM_CODEC);
    
    protected static <T extends AbstractSpellMod<T>> RegistryObject<SpellModType<T>> register(String id, Supplier<SpellProperty> tiebreaker, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DEFERRED_TYPES.register(id, () -> new SpellModType<T>(PrimalMagick.resource(id), tiebreaker, codec, streamCodec));
    }
}
