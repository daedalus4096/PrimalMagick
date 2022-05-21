package com.verdantartifice.primalmagick.common.tiles.devices;

import java.util.List;

import com.verdantartifice.primalmagick.common.blocks.devices.AbstractWindGeneratorBlock;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TilePM;
import com.verdantartifice.primalmagick.common.util.RayTraceUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a wind generator tile entity.  Works with either a Zephyr Engine (wind pushes) or a
 * Void Turbine (wind pulls).
 * 
 * @author Daedalus4096
 */
public class WindGeneratorTileEntity extends TilePM {
    public WindGeneratorTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.WIND_GENERATOR.get(), pos, state);
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, WindGeneratorTileEntity entity) {
        // Move entities in the direction of this device's generated wind
        if (state.getBlock() instanceof AbstractWindGeneratorBlock block && state.getValue(AbstractWindGeneratorBlock.POWERED)) {
            int power = level.getBestNeighborSignal(pos);
            Direction windDir = block.getWindDirection(state);
            Vec3 windStep = new Vec3(windDir.step()).scale(0.1D * (power / 15D));
            Direction facing = state.getValue(AbstractWindGeneratorBlock.FACING);
            AABB zone = new AABB(pos).expandTowards(new Vec3(facing.step()).scale(power));
            List<Entity> affected = level.getEntitiesOfClass(Entity.class, zone, e -> {
                return !e.isSpectator() && (e instanceof ItemEntity || e instanceof LivingEntity) && RayTraceUtils.hasLineOfSight(e, pos);
            });
            for (Entity affectedEntity : affected) {
                affectedEntity.setDeltaMovement(affectedEntity.getDeltaMovement().add(windStep));
            }
        }
    }
}
