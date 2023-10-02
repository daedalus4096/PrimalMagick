package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of the Burst spell mod.  This mod causes the spell package to explode outward from its
 * impact point, causing the spell to affect all targets in the area of effect.  Its radius property
 * determines how wide the explosion is.  Its power property determines the explosion's ability to
 * penetrate intervening blocks to affect blocks on the other side.
 * 
 * @author Daedalus4096
 */
public class BurstSpellMod extends AbstractSpellMod {
    public static final String TYPE = "burst";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_MOD_BURST.get().compoundKey();

    public BurstSpellMod() {
        super();
    }
    
    public BurstSpellMod(int radius, int power) {
        super();
        this.getProperty("radius").setValue(radius);
        this.getProperty("power").setValue(power);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("radius", new SpellProperty("radius", "spells.primalmagick.property.radius", 1, 5));
        propMap.put("power", new SpellProperty("power", "spells.primalmagick.property.power", 0, 5));
        return propMap;
    }
    
    @Override
    public int getBaseManaCostModifier() {
        return this.getPropertyValue("power");
    }
    
    @Override
    public int getManaCostMultiplier() {
        int radius = this.getPropertyValue("radius");
        return 1 + (radius * radius);
    }

    @Override
    protected String getModType() {
        return TYPE;
    }

    @Nonnull
    public Set<HitResult> getBurstTargets(HitResult origin, SpellPackage spell, @Nullable ItemStack spellSource, Level world) {
        Set<HitResult> retVal = new HashSet<>();
        Set<BlockPos> affectedBlocks = new HashSet<>();
        Vec3 hitVec = origin.getLocation();
        BlockPos hitPos = BlockPos.containing(hitVec);
        int radius = this.getRadiusBlocks();
        int power = this.getBlastPower(spell, spellSource);
        double sqRadius = (double)(radius * radius);
        int searchRadius = radius + 1;
        Explosion explosion = new Explosion(world, null, hitVec.x, hitVec.y, hitVec.z, (float)power, false, Explosion.BlockInteraction.KEEP);
        
        // Calculate blasted blocks
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 16; k++) {
                    if (i == 0 || i == 15 || j == 0 || j == 15 || k == 0 || k == 15) {
                        // Calculate a direction vector for the burst
                        Vec3 dirVec = new Vec3((double)i / 15.0D * 2.0D - 1.0D, (double)j / 15.0D * 2.0D - 1.0D, (double)k / 15.0D * 2.0D - 1.0D).normalize();
                        Vec3 curVec = new Vec3(hitVec.x, hitVec.y, hitVec.z);
                        float remainingPower = (float)power;
                        
                        while (remainingPower >= 0.0F && curVec.distanceToSqr(hitVec) < sqRadius) {
                            // Add the current block to the result set if it hasn't already been hit
                            BlockPos curPos = BlockPos.containing(curVec);
                            if (affectedBlocks.add(curPos)) {
                                Vec3 relVec = hitVec.subtract(curVec);
                                Direction dir = Direction.getNearest(relVec.x, relVec.y, relVec.z);
                                retVal.add(new BlockHitResult(curVec, dir, curPos, false));
                            }
                            
                            // Decrement the remaining power based on the block's explosion resistance
                            BlockState blockState = world.getBlockState(curPos);
                            FluidState fluidState = world.getFluidState(curPos);
                            if (!blockState.isAir() || !fluidState.isEmpty()) {
                                float resistance = Math.max(blockState.getExplosionResistance(world, curPos, explosion), fluidState.getExplosionResistance(world, curPos, explosion));
                                remainingPower -= (resistance + 0.3F) * 0.3F;
                            }
                            
                            // Progress analysis along the current direction vector
                            curVec = curVec.add(dirVec.scale(0.3D));
                        }
                    }
                }
            }
        }
        
        // Calculate blasted entities
        AABB aabb = new AABB(hitPos).inflate(searchRadius);
        List<Entity> entities = world.getEntities((Entity)null, aabb, e -> !e.isSpectator());
        for (Entity entity : entities) {
            if (origin.getLocation().distanceToSqr(entity.position()) <= sqRadius) {
                retVal.add(new EntityHitResult(entity));
            }
        }
        
        return retVal;
    }
    
    protected int getRadiusBlocks() {
        return this.getPropertyValue("radius");
    }
    
    protected int getBlastPower(SpellPackage spell, ItemStack spellSource) {
        return 5 + (3 * this.getModdedPropertyValue("power", spell, spellSource));
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return Component.translatable("spells.primalmagick.mod." + this.getModType() + ".detail_tooltip", this.getRadiusBlocks(), this.getBlastPower(spell, spellSource));
    }
}
