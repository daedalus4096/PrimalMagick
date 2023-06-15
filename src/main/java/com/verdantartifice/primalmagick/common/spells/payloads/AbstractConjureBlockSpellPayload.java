package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.util.RayTraceUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Base class for a block conjuration spell.  Creates a block with the given state at the designated
 * point.  Has no effect when cast by non-players.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractConjureBlockSpellPayload extends AbstractSpellPayload {
    protected final BlockState targetState;
    protected final int count;
    
    public AbstractConjureBlockSpellPayload(BlockState targetState) {
        this(targetState, 1);
    }
    
    public AbstractConjureBlockSpellPayload(BlockState targetState, int count) {
        super();
        this.targetState = targetState;
        this.count = count;
    }

    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        if (!(caster instanceof Player player)) {
            return;
        }
        
        if (target != null) {
            if (target.getType() == HitResult.Type.BLOCK) {
                this.executeInner((BlockHitResult)target, player, world);
            } else if (target.getType() == HitResult.Type.ENTITY) {
                // If the target is an entity, place the new block at the entity's position
                this.executeInner(RayTraceUtils.getBlockResultFromEntityResult((EntityHitResult)target), player, world);
            }
        }
    }
    
    protected void executeInner(BlockHitResult blockTarget, Player player, Level world) {
        BlockPos targetPos = blockTarget.getBlockPos();
        int shift = this.canPlaceBlockState(player, world, targetPos) ? 0 : 1;
        for (int offset = shift; offset < this.count + shift; offset++) {
            this.placeBlockState(world, targetPos.relative(blockTarget.getDirection(), offset));
        }
    }
    
    @SuppressWarnings("deprecation")
    protected boolean canPlaceBlockState(Player player, Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        boolean isSolid = state.isSolid();
        boolean isReplaceable = state.canBeReplaced();
        return world.mayInteract(player, pos) && !ForgeEventFactory.onBlockPlace(player, BlockSnapshot.create(world.dimension(), world, pos), Direction.UP) && 
                world.isEmptyBlock(pos) || !isSolid || isReplaceable;
    }
    
    @SuppressWarnings("deprecation")
    protected void placeBlockState(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        FluidState fluidState = world.getFluidState(pos);
        boolean isSolid = state.isSolid();
        boolean isReplaceable = state.canBeReplaced();
        if (world.isEmptyBlock(pos) || !isSolid || isReplaceable) {
            // Destroy the existing block at the target location if the new block would replace it
            if (!world.isClientSide && (!isSolid || isReplaceable) && !state.liquid()) {
                world.destroyBlock(pos, true);
            }
            
            // Otherwise set the target state, with facing and waterlogging if applicable, into the world
            BlockState newState = this.targetState;
            if (state.hasProperty(BlockStateProperties.FACING)) {
                newState = newState.setValue(BlockStateProperties.FACING, state.getValue(BlockStateProperties.FACING));
            } else if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
            }
            if (fluidState.isSourceOfType(Fluids.WATER) && newState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                newState = newState.setValue(BlockStateProperties.WATERLOGGED, Boolean.TRUE);
            }
            world.setBlock(pos, newState, Block.UPDATE_ALL);
        }
    }
}
