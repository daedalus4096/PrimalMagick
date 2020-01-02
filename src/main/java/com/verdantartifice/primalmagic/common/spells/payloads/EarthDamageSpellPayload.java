package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EarthDamageSpellPayload extends AbstractDamageSpellPayload {
    public static final String TYPE = "earth_damage";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("BASIC_SORCERY"));
    
    public EarthDamageSpellPayload() {
        super();
    }
    
    public EarthDamageSpellPayload(int power) {
        super(power);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected String getPayloadType() {
        return TYPE;
    }
    
    @Override
    public Source getSource() {
        return Source.EARTH;
    }
    
    @Override
    protected float getTotalDamage(Entity target, SpellPackage spell) {
        return 3.0F + this.getModdedPropertyValue("power", spell);
    }
    
    @Override
    protected void applySecondaryEffects(RayTraceResult target, Vec3d burstPoint, SpellPackage spell, World world, LivingEntity caster) {
        if (target != null && target.getType() == RayTraceResult.Type.ENTITY) {
            EntityRayTraceResult entityTarget = (EntityRayTraceResult)target;
            if (entityTarget.getEntity() != null && entityTarget.getEntity() instanceof LivingEntity) {
                Vec3d knockbackVec;
                if (entityTarget.getEntity().equals(caster)) {
                    knockbackVec = caster.getLookVec().scale(-1.0D).normalize();
                } else {
                    Vec3d knockbackSource = burstPoint == null || burstPoint.equals(target.getHitVec()) ? caster.getEyePosition(1.0F) : burstPoint;
                    knockbackVec = target.getHitVec().subtract(knockbackSource).scale(-1.0D).normalize();
                }
                ((LivingEntity)entityTarget.getEntity()).knockBack(caster, 0.25F * this.getTotalDamage(entityTarget.getEntity(), spell), knockbackVec.x, knockbackVec.z);
            }
        }
    }
    
    @Override
    public void playSounds(World world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.ROCKSLIDE, SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }
}
