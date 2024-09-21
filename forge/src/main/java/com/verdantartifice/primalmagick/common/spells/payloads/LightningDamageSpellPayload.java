package com.verdantartifice.primalmagick.common.spells.payloads;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition of a lightning damage spell.  Deals lower than standard damage at low power property
 * values, and higher than standard damage at high power property values.  No secondary effects.
 * 
 * @author Daedalus4096
 */
public class LightningDamageSpellPayload extends AbstractDamageSpellPayload<LightningDamageSpellPayload> {
    public static final LightningDamageSpellPayload INSTANCE = new LightningDamageSpellPayload();
    
    public static final MapCodec<LightningDamageSpellPayload> CODEC = MapCodec.unit(LightningDamageSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, LightningDamageSpellPayload> STREAM_CODEC = StreamCodec.unit(LightningDamageSpellPayload.INSTANCE);
    
    public static final String TYPE = "lightning_damage";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_LIGHTNING));
    
    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    public static LightningDamageSpellPayload getInstance() {
        return INSTANCE;
    }
    
    @Override
    public SpellPayloadType<LightningDamageSpellPayload> getType() {
        return SpellPayloadsPM.LIGHTNING_DAMAGE.get();
    }

    @Override
    public Source getSource() {
        return Sources.SKY;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.ELECTRIC.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected float getBaseDamage(SpellPackage spell, ItemStack spellSource, HolderLookup.Provider registries) {
        return 5.0F * this.getModdedPropertyValue(SpellPropertiesPM.POWER.get(), spell, spellSource, registries);
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
    
    @Override
    public int getBaseManaCost(SpellPropertyConfiguration properties) {
        int power = properties.get(SpellPropertiesPM.POWER.get());
        return (1 << Math.max(0, power - 1)) + ((1 << Math.max(0, power - 1)) >> 1);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource, HolderLookup.Provider registries) {
        return Component.translatable("spells.primalmagick.payload." + this.getPayloadType() + ".detail_tooltip", DECIMAL_FORMATTER.format(this.getBaseDamage(spell, spellSource, registries)));
    }
}
