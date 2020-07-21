package com.verdantartifice.primalmagic.common.spells.vehicles;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.mods.ForkSpellMod;
import com.verdantartifice.primalmagic.common.util.RayTraceUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Base class for a raycast-based spell vehicle.  Finds the first target within a defined distance
 * along a direction vector.  This is usually, but not always, the caster's line of sight.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRaycastSpellVehicle extends AbstractSpellVehicle {
    /**
     * Determine how far out the spell vehicle should look for a target along its direction vector.
     * 
     * @param caster the player that originally casted the spell
     * @return the distance, in blocks, that the spell vehicle should search
     */
    protected abstract double getReachDistance(@Nonnull PlayerEntity caster);
    
    protected void drawFx(@Nonnull World world, @Nonnull SpellPackage spell, Vec3d source, Vec3d target) {
        // Do nothing by default
    }
    
    @Override
    public void execute(SpellPackage spell, World world, PlayerEntity caster, ItemStack spellSource) {
        if (spell.getPayload() != null) {
            ForkSpellMod forkMod = spell.getMod(ForkSpellMod.class, "forks");
            Vec3d baseLookVector = caster.getLook(1.0F);
            List<Vec3d> lookVectors;
            if (forkMod == null) {
                // If no Fork mod is in the spell package, use the caster's line of sight for the direction vector
                lookVectors = Arrays.asList(baseLookVector.normalize());
            } else {
                // If a Fork mod is in the spell package, calculate a direction vector for each fork, based on the caster's line of sight
                lookVectors = forkMod.getDirectionUnitVectors(baseLookVector, world.rand);
            }
            
            double reachDistance = this.getReachDistance(caster);
            Vec3d eyePos = caster.getEyePosition(1.0F);
            for (Vec3d lookVector : lookVectors) {
                Vec3d reachPos = eyePos.add(lookVector.scale(reachDistance));
                AxisAlignedBB aabb = caster.getBoundingBox().expand(lookVector.scale(reachDistance)).grow(1.0D, 1.0D, 1.0D);
                
                // Determine if an entity hit was found by the raycast
                EntityRayTraceResult entityResult = RayTraceUtils.rayTraceEntities(world, caster, eyePos, reachPos, aabb, (testEntity) -> {
                    return !testEntity.isSpectator();
                }, (reachDistance * reachDistance));
                
                // Determine if a block hit was found by the raycast
                BlockRayTraceResult blockResult = world.rayTraceBlocks(new RayTraceContext(eyePos, reachPos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, caster));
                
                RayTraceResult result;
                if (entityResult == null) {
                    // If no entity hit was found, use the block result; this might still be a miss
                    result = blockResult;
                } else {
                    // If both an entity hit and a block hit were found, pick the one that's closer to the caster
                    result = (eyePos.squareDistanceTo(entityResult.getHitVec()) <= eyePos.squareDistanceTo(blockResult.getHitVec())) ? entityResult : blockResult;
                }
                
                // Draw any particle effects for the vehicle
                this.drawFx(world, spell, eyePos.add(lookVector.scale(0.1D)), result.getHitVec());
                
                // Execute the spell payload on the found target
                SpellManager.executeSpellPayload(spell, result, world, caster, spellSource, true);
            }
        }
    }
}
