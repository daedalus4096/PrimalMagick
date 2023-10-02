package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
public class HolyDamageSpellPayload extends AbstractDamageSpellPayload {
    public static final String TYPE = "holy_damage";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_PAYLOAD_HOLY.get().compoundKey();
    
    public HolyDamageSpellPayload() {
        super();
    }
    
    public HolyDamageSpellPayload(int power) {
        super(power);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    public Source getSource() {
        return Source.HALLOWED;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.ANGELS.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected float getTotalDamage(Entity target, SpellPackage spell, ItemStack spellSource) {
        float damage = super.getTotalDamage(target, spell, spellSource);
        if (target instanceof LivingEntity && ((LivingEntity)target).isInvertedHealAndHarm()) {
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
    public int getBaseManaCost() {
        int power = this.getPropertyValue("power");
        return (1 << Math.max(0, power - 1)) + ((1 << Math.max(0, power - 1)) >> 1);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return Component.translatable("spells.primalmagick.payload." + this.getPayloadType() + ".detail_tooltip", DECIMAL_FORMATTER.format(this.getBaseDamage(spell, spellSource)));
    }
}
