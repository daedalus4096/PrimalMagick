package com.verdantartifice.primalmagic.common.spells.mods;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BurstSpellMod extends AbstractSpellMod {
    public static final String TYPE = "burst";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_MOD_BURST"));

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
        propMap.put("radius", new SpellProperty("radius", "primalmagic.spell.property.radius", 1, 5));
        propMap.put("power", new SpellProperty("power", "primalmagic.spell.property.power", 0, 5));
        return propMap;
    }
    
    @Override
    public int getBaseManaCostModifier() {
        // TODO Auto-generated method stub
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
    public Set<RayTraceResult> getBurstTargets(RayTraceResult origin, SpellPackage spell, World world) {
        Set<RayTraceResult> retVal = new HashSet<>();
        Set<BlockPos> affectedBlocks = new HashSet<>();
        Vec3d hitVec = origin.getHitVec();
        BlockPos hitPos = new BlockPos(hitVec);
        int radius = this.getPropertyValue("radius");
        int power = this.getModdedPropertyValue("power", spell);
        double sqRadius = (double)(radius * radius);
        int searchRadius = radius + 1;
        Explosion explosion = new Explosion(world, null, hitVec.x, hitVec.y, hitVec.z, (float)power, false, Explosion.Mode.NONE);
        
        // Calculate blasted blocks
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 16; k++) {
                    if (i == 0 || i == 15 || j == 0 || j == 15 || k == 0 || k == 15) {
                        // Calculate a direction vector for the burst
                        Vec3d dirVec = new Vec3d((double)i / 15.0D * 2.0D - 1.0D, (double)j / 15.0D * 2.0D - 1.0D, (double)k / 15.0D * 2.0D - 1.0D).normalize();
                        Vec3d curVec = new Vec3d(hitVec.x, hitVec.y, hitVec.z);
                        float remainingPower = (float)power;
                        
                        while (remainingPower >= 0.0F && curVec.squareDistanceTo(hitVec) < sqRadius) {
                            // Add the current block to the result set if it hasn't already been hit
                            BlockPos curPos = new BlockPos(curVec);
                            if (affectedBlocks.add(curPos)) {
                                Vec3d relVec = hitVec.subtract(curVec);
                                Direction dir = Direction.getFacingFromVector(relVec.x, relVec.y, relVec.z);
                                retVal.add(new BlockRayTraceResult(curVec, dir, curPos, false));
                            }
                            
                            // Decrement the remaining power based on the block's explosion resistance
                            BlockState blockState = world.getBlockState(curPos);
                            IFluidState fluidState = world.getFluidState(curPos);
                            if (!blockState.isAir(world, curPos) || !fluidState.isEmpty()) {
                                float resistance = Math.max(blockState.getExplosionResistance(world, curPos, null, explosion), fluidState.getExplosionResistance(world, curPos, null, explosion));
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
        AxisAlignedBB aabb = new AxisAlignedBB(hitPos).grow(searchRadius);
        List<Entity> entities = world.getEntitiesInAABBexcluding(null, aabb, e -> !e.isSpectator());
        for (Entity entity : entities) {
            if (origin.getHitVec().squareDistanceTo(entity.getPositionVec()) <= sqRadius) {
                retVal.add(new EntityRayTraceResult(entity));
            }
        }
        
        return retVal;
    }
}
