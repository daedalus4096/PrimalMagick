package com.verdantartifice.primalmagick.common.spells.mods;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public class SpellModsPM {
    public static final IRegistryItem<SpellModType<?>, SpellModType<EmptySpellMod>> EMPTY = register(EmptySpellMod.TYPE, 100, EmptySpellMod::getInstance, EmptySpellMod::getRequirement, SpellPropertiesPM.POWER, EmptySpellMod.CODEC, EmptySpellMod.STREAM_CODEC);
    public static final IRegistryItem<SpellModType<?>, SpellModType<AmplifySpellMod>> AMPLIFY = register(AmplifySpellMod.TYPE, 200, AmplifySpellMod::getInstance, AmplifySpellMod::getRequirement, SpellPropertiesPM.AMPLIFY_POWER, AmplifySpellMod.CODEC, AmplifySpellMod.STREAM_CODEC);
    public static final IRegistryItem<SpellModType<?>, SpellModType<BurstSpellMod>> BURST = register(BurstSpellMod.TYPE, 300, BurstSpellMod::getInstance, BurstSpellMod::getRequirement, SpellPropertiesPM.RADIUS, BurstSpellMod.CODEC, BurstSpellMod.STREAM_CODEC);
    public static final IRegistryItem<SpellModType<?>, SpellModType<QuickenSpellMod>> QUICKEN = register(QuickenSpellMod.TYPE, 400, QuickenSpellMod::getInstance, QuickenSpellMod::getRequirement, SpellPropertiesPM.HASTE, QuickenSpellMod.CODEC, QuickenSpellMod.STREAM_CODEC);
    public static final IRegistryItem<SpellModType<?>, SpellModType<MineSpellMod>> MINE = register(MineSpellMod.TYPE, 500, MineSpellMod::getInstance, MineSpellMod::getRequirement, SpellPropertiesPM.NON_ZERO_DURATION, MineSpellMod.CODEC, MineSpellMod.STREAM_CODEC);
    public static final IRegistryItem<SpellModType<?>, SpellModType<ForkSpellMod>> FORK = register(ForkSpellMod.TYPE, 600, ForkSpellMod::getInstance, ForkSpellMod::getRequirement, SpellPropertiesPM.FORKS, ForkSpellMod.CODEC, ForkSpellMod.STREAM_CODEC);
    
    protected static <T extends AbstractSpellMod<T>> IRegistryItem<SpellModType<?>, SpellModType<T>> register(String id,
            int sortOrder, Supplier<T> instanceSupplier, Supplier<AbstractRequirement<?>> requirementSupplier,
            Supplier<SpellProperty> tiebreaker, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return Services.SPELL_MOD_TYPES.register(id, () -> new SpellModType<T>(ResourceUtils.loc(id), sortOrder, instanceSupplier, requirementSupplier, tiebreaker, codec, streamCodec));
    }
}
