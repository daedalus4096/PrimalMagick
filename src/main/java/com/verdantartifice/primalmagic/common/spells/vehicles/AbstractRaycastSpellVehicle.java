package com.verdantartifice.primalmagic.common.spells.vehicles;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.util.RayTraceUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AbstractRaycastSpellVehicle extends AbstractSpellVehicle {
    protected abstract double getReachDistance(@Nonnull PlayerEntity caster);
    
    protected void drawFx(@Nonnull World world, @Nonnull SpellPackage spell, Vec3d source, Vec3d target) {
        // Do nothing by default
    }
    
    @Override
    public void execute(SpellPackage spell, World world, PlayerEntity caster) {
        if (spell.getPayload() != null) {
            double reachDistance = this.getReachDistance(caster);
            Vec3d eyePos = caster.getEyePosition(1.0F);
            Vec3d lookVector = caster.getLook(1.0F);
            Vec3d reachPos = eyePos.add(lookVector.scale(reachDistance));
            AxisAlignedBB aabb = caster.getBoundingBox().expand(lookVector.scale(reachDistance)).grow(1.0D, 1.0D, 1.0D);
            EntityRayTraceResult entityResult = RayTraceUtils.rayTraceEntities(world, caster, eyePos, reachPos, aabb, (testEntity) -> {
                return !testEntity.isSpectator();
            }, (reachDistance * reachDistance));
            BlockRayTraceResult blockResult = world.rayTraceBlocks(new RayTraceContext(eyePos, reachPos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, caster));
            RayTraceResult result;
            if (entityResult == null) {
                result = blockResult;
            } else {
                result = (eyePos.squareDistanceTo(entityResult.getHitVec()) <= eyePos.squareDistanceTo(blockResult.getHitVec())) ? entityResult : blockResult;
            }
            this.drawFx(world, spell, eyePos.add(lookVector.scale(0.1D)), result.getHitVec());
            SpellManager.executeSpellPayload(spell, result, world, caster, true);
        }
    }
}
