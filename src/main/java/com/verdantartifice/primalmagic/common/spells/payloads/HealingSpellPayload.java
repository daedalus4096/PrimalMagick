package com.verdantartifice.primalmagic.common.spells.payloads;

import java.util.Map;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HealingSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "healing";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_HEALING"));

    public HealingSpellPayload() {
        super();
    }
    
    public HealingSpellPayload(int power) {
        super();
        this.getProperty("power").setValue(power);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("power", new SpellProperty("power", "primalmagic.spell.property.power", 1, 5));
        return propMap;
    }
    
    @Override
    public void execute(RayTraceResult target, Vec3d blastPoint, SpellPackage spell, World world, LivingEntity caster) {
        if (target != null && target.getType() == RayTraceResult.Type.ENTITY) {
            EntityRayTraceResult entityTarget = (EntityRayTraceResult)target;
            if (entityTarget.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity)entityTarget.getEntity();
                if (entity.isEntityUndead()) {
                    entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(caster, caster), 1.5F * this.getModdedPropertyValue("power", spell));
                } else {
                    entity.heal((float)this.getModdedPropertyValue("power", spell));
                }
            }
        }
    }

    @Override
    public Source getSource() {
        return Source.SUN;
    }

    @Override
    public SourceList getManaCost() {
        return new SourceList().add(this.getSource(), 2 * this.getPropertyValue("power"));
    }

    @Override
    public void playSounds(World world, LivingEntity caster) {
        world.playSound(null, caster.getPosition(), SoundsPM.HEAL, SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
