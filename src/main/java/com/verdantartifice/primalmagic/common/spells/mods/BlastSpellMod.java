package com.verdantartifice.primalmagic.common.spells.mods;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlastSpellMod extends AbstractSpellMod {
    public static final String TYPE = "blast";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_MOD_BLAST"));

    public BlastSpellMod() {
        super();
    }
    
    public BlastSpellMod(int power) {
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
    public SourceList modifyManaCost(SourceList cost) {
        SourceList newCost = cost.copy();
        int power = this.getPropertyValue("power");
        for (Source source : cost.getSources()) {
            int amount = cost.getAmount(source);
            if (amount > 0) {
                newCost.add(source, amount * power);
            }
        }
        return newCost;
    }

    @Override
    protected String getModType() {
        return TYPE;
    }

    @Nonnull
    public Set<RayTraceResult> getBlastTargets(RayTraceResult origin, SpellPackage spell, World world) {
        Set<RayTraceResult> retVal = new HashSet<>();
        BlockPos hitPos = new BlockPos(origin.getHitVec());
        int power = this.getModdedPropertyValue("power", spell);
        double sqDistance = (double)(power * power);
        int searchRadius = power + 1;
        
        // Calculate blasted blocks
        for (int i = -searchRadius; i <= searchRadius; i++) {
            for (int j = -searchRadius; j <= searchRadius; j++) {
                for (int k = -searchRadius; k <= searchRadius; k++) {
                    BlockPos searchPos = hitPos.add(i, j, k);
                    BlockState state = world.getBlockState(searchPos);
                    if (!state.isAir(world, searchPos) && hitPos.withinDistance(searchPos, power)) {
                        retVal.add(new BlockRayTraceResult(new Vec3d(searchPos.getX(), searchPos.getY(), searchPos.getZ()), Direction.UP, searchPos, false));
                    }
                }
            }
        }
        
        // Calculate blasted entities
        AxisAlignedBB aabb = new AxisAlignedBB(hitPos).grow(power + 1);
        List<Entity> entities = world.getEntitiesInAABBexcluding(null, aabb, e -> !e.isSpectator());
        for (Entity entity : entities) {
            if (origin.getHitVec().squareDistanceTo(entity.getPositionVec()) <= sqDistance) {
                retVal.add(new EntityRayTraceResult(entity));
            }
        }
        
        return retVal;
    }
}
