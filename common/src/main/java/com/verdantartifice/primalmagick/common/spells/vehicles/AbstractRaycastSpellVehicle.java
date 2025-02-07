package com.verdantartifice.primalmagick.common.spells.vehicles;

import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModsPM;
import com.verdantartifice.primalmagick.common.util.RayTraceUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableObject;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * Base class for a raycast-based spell vehicle.  Finds the first target within a defined distance
 * along a direction vector.  This is usually, but not always, the caster's line of sight.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRaycastSpellVehicle<T extends AbstractRaycastSpellVehicle<T>> extends AbstractSpellVehicle<T> {
    /**
     * Determine how far out the spell vehicle should look for a target along its direction vector.
     * 
     * @param caster the entity that originally casted the spell
     * @return the distance, in blocks, that the spell vehicle should search
     */
    protected abstract double getReachDistance(@Nonnull LivingEntity caster, @Nonnull SpellPackage spell, @Nonnull ItemStack spellSource);
    
    protected void drawFx(@Nonnull Level world, @Nonnull SpellPackage spell, Vec3 source, Vec3 target) {
        // Do nothing by default
    }
    
    @Override
    public void execute(SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        if (spell.payload() != null) {
            Vec3 baseLookVector = caster.getViewVector(1.0F);
            MutableObject<List<Vec3>> lookVectors = new MutableObject<>();
            spell.getMod(SpellModsPM.FORK.get()).ifPresentOrElse(forkMod -> {
                // If a Fork mod is in the spell package, calculate a direction vector for each fork, based on the caster's line of sight
                lookVectors.setValue(forkMod.getComponent().getDirectionUnitVectors(baseLookVector, world.random, spell, spellSource));
            }, () -> {
                // If no Fork mod is in the spell package, use the caster's line of sight for the direction vector
                lookVectors.setValue(Arrays.asList(baseLookVector.normalize()));
            });
            
            double reachDistance = this.getReachDistance(caster, spell, spellSource);
            Vec3 eyePos = caster.getEyePosition(1.0F);
            for (Vec3 lookVector : lookVectors.getValue()) {
                Vec3 reachPos = eyePos.add(lookVector.scale(reachDistance));
                AABB aabb = caster.getBoundingBox().expandTowards(lookVector.scale(reachDistance)).inflate(1.0D, 1.0D, 1.0D);
                
                // Determine if an entity hit was found by the raycast
                EntityHitResult entityResult = RayTraceUtils.rayTraceEntities(world, caster, eyePos, reachPos, aabb, (testEntity) -> {
                    return !testEntity.isSpectator();
                }, (reachDistance * reachDistance));
                
                // Determine if a block hit was found by the raycast
                BlockHitResult blockResult = world.clip(new ClipContext(eyePos, reachPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, caster));
                
                HitResult result;
                if (entityResult == null) {
                    // If no entity hit was found, use the block result; this might still be a miss
                    result = blockResult;
                } else {
                    // If both an entity hit and a block hit were found, pick the one that's closer to the caster
                    result = (eyePos.distanceToSqr(entityResult.getLocation()) <= eyePos.distanceToSqr(blockResult.getLocation())) ? entityResult : blockResult;
                }
                
                // Draw any particle effects for the vehicle
                this.drawFx(world, spell, eyePos.add(lookVector.scale(0.1D)), result.getLocation());
                
                // Execute the spell payload on the found target
                SpellManager.executeSpellPayload(spell, result, world, caster, spellSource, true, null);
            }
        }
    }
}
