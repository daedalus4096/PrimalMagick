package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.ISpellPackage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EarthDamageSpellPayload extends AbstractDamageSpellPayload {
    public static final String TYPE = "earth_damage";
    
    public EarthDamageSpellPayload() {
        super();
    }
    
    public EarthDamageSpellPayload(int power) {
        super(power);
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
    protected float getTotalDamage() {
        return 3.0F + this.power;
    }
    
    @Override
    protected void applySecondaryEffects(RayTraceResult target, ISpellPackage spell, World world, PlayerEntity caster) {
        if (target != null && target.getType() == RayTraceResult.Type.ENTITY) {
            EntityRayTraceResult entityTarget = (EntityRayTraceResult)target;
            if (entityTarget.getEntity() != null && entityTarget.getEntity() instanceof LivingEntity) {
                Vec3d knockbackVec;
                if (entityTarget.getEntity().equals(caster)) {
                    knockbackVec = caster.getLookVec().scale(-1.0D).normalize();
                } else {
                    knockbackVec = target.getHitVec().subtract(caster.getEyePosition(1.0F)).scale(-1.0D).normalize();
                }
                ((LivingEntity)entityTarget.getEntity()).knockBack(caster, 0.25F * this.getTotalDamage(), knockbackVec.x, knockbackVec.z);
            }
        }
    }
    
    @Override
    public void playSounds(World world, PlayerEntity caster) {
        world.playSound(null, caster.getPosition(), SoundsPM.ROCKSLIDE, SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }
}
