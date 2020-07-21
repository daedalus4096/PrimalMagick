package com.verdantartifice.primalmagic.common.spells.payloads;

import java.util.Map;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Definition of a healing spell.  Restores health to the target entity based on the power property of
 * the payload.  If the entity is undead, it instead takes damage based on power.  Has no effect on
 * blocks.
 * 
 * @author Daedalus4096
 */
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
    public void execute(RayTraceResult target, Vec3d burstPoint, SpellPackage spell, World world, PlayerEntity caster, ItemStack spellSource) {
        if (target != null && target.getType() == RayTraceResult.Type.ENTITY) {
            EntityRayTraceResult entityTarget = (EntityRayTraceResult)target;
            if (entityTarget.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity)entityTarget.getEntity();
                if (entity.isEntityUndead()) {
                    // Undead entities get dealt damage
                    entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(caster, caster), 1.5F * this.getModdedPropertyValue("power", spell, spellSource));
                } else {
                    // All other entities are healed
                    entity.heal((float)this.getModdedPropertyValue("power", spell, spellSource));
                }
            }
        }
    }

    @Override
    public Source getSource() {
        return Source.SUN;
    }

    @Override
    public int getBaseManaCost() {
        return 2 * this.getPropertyValue("power");
    }

    @Override
    public void playSounds(World world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.HEAL.get(), SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
