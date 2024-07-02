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
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition for a blood damage spell.  Does standard damage to the target, and bypasses any armor
 * that it may have.  No secondary effects.
 * 
 * @author Daedalus4096
 */
public class BloodDamageSpellPayload extends AbstractDamageSpellPayload<BloodDamageSpellPayload> {
    public static final BloodDamageSpellPayload INSTANCE = new BloodDamageSpellPayload();
    
    public static final MapCodec<BloodDamageSpellPayload> CODEC = MapCodec.unit(BloodDamageSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, BloodDamageSpellPayload> STREAM_CODEC = StreamCodec.unit(BloodDamageSpellPayload.INSTANCE);
    
    public static final String TYPE = "blood_damage";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_BLOOD));
    
    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    @Override
    public SpellPayloadType<BloodDamageSpellPayload> getType() {
        return SpellPayloadsPM.BLOOD_DAMAGE.get();
    }

    @Override
    public Source getSource() {
        return Sources.BLOOD;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.BLOOD.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }
    
    @Override
    protected float getBaseDamage(SpellPackage spell, ItemStack spellSource) {
        return 3.0F + (2.0F * this.getModdedPropertyValue(SpellPropertiesPM.POWER.get(), spell, spellSource));
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
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return Component.translatable("spells.primalmagick.payload." + this.getPayloadType() + ".detail_tooltip", DECIMAL_FORMATTER.format(this.getBaseDamage(spell, spellSource)));
    }
}
