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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition of a holy damage spell.  Deals standard damage to the target, or double standard damage
 * to undead targets.  No secondary effects.
 * 
 * @author Daedalus4096
 */
public class HolyDamageSpellPayload extends AbstractDamageSpellPayload<HolyDamageSpellPayload> {
    public static final HolyDamageSpellPayload INSTANCE = new HolyDamageSpellPayload();
    
    public static final MapCodec<HolyDamageSpellPayload> CODEC = MapCodec.unit(HolyDamageSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, HolyDamageSpellPayload> STREAM_CODEC = StreamCodec.unit(HolyDamageSpellPayload.INSTANCE);
    
    public static final String TYPE = "holy_damage";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_HOLY));
    
    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    public static HolyDamageSpellPayload getInstance() {
        return INSTANCE;
    }
    
    @Override
    public SpellPayloadType<HolyDamageSpellPayload> getType() {
        return SpellPayloadsPM.HOLY_DAMAGE.get();
    }

    @Override
    public Source getSource() {
        return Sources.HALLOWED;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.ANGELS.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected float getTotalDamage(Entity target, SpellPackage spell, ItemStack spellSource) {
        float damage = super.getTotalDamage(target, spell, spellSource);
        if (target instanceof LivingEntity livingTarget && livingTarget.isInvertedHealAndHarm()) {
            // Deal double damage to undead entities
            damage += damage;
        }
        return damage;
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
