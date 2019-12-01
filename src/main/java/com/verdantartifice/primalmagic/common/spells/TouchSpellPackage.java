package com.verdantartifice.primalmagic.common.spells;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TouchSpellPackage extends AbstractSpellPackage {
    public static final String TYPE = "touch";
    
    public TouchSpellPackage() {
        super();
    }

    public TouchSpellPackage(String name) {
        super(name);
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.putString("SpellType", TYPE);
        return tag;
    }

    @Override
    public void cast(World world, PlayerEntity caster) {
        double reachDistance = caster.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
        Vec3d eyePos = caster.getEyePosition(1.0F);
        Vec3d lookVector = caster.getLook(1.0F);
        Vec3d reachPos = eyePos.add(lookVector.scale(reachDistance));
        AxisAlignedBB aabb = caster.getBoundingBox().expand(lookVector.scale(reachDistance)).grow(1.0D, 1.0D, 1.0D);
        EntityRayTraceResult entityResult = ProjectileHelper.func_221269_a(world, caster, eyePos, reachPos, aabb, (testEntity) -> {
            return !testEntity.isSpectator();
        }, (reachDistance * reachDistance));
        
        if (entityResult == null || entityResult.getEntity() == null) {
            PrimalMagic.LOGGER.info("No target found for spell {}", this.getName());
        } else {
            PrimalMagic.LOGGER.info("Casting spell {} on {}", this.getName(), entityResult.getEntity().getName().getFormattedText());
        }
    }
}
