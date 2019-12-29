package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class LightningDamageSpellPayload extends AbstractDamageSpellPayload {
    public static final String TYPE = "lightning_damage";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_LIGHTNING"));
    
    public LightningDamageSpellPayload() {
        super();
    }
    
    public LightningDamageSpellPayload(int power) {
        super(power);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    public Source getSource() {
        return Source.SKY;
    }

    @Override
    public void playSounds(World world, LivingEntity caster) {
        world.playSound(null, caster.getPosition(), SoundsPM.ELECTRIC, SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }

    @Override
    protected float getTotalDamage(Entity target, SpellPackage spell) {
        return 2.0F * this.getModdedPropertyValue("power", spell);
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
