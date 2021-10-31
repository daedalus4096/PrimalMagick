package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.util.RayTraceUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Base class for a block conjuration spell.  Creates a block with the given state at the designated
 * point.  Has no effect when cast by non-players.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractConjureBlockSpellPayload extends AbstractSpellPayload {
    protected final BlockState targetState;
    
    public AbstractConjureBlockSpellPayload(BlockState targetState) {
        super();
        this.targetState = targetState;
    }

    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        if (!(caster instanceof Player player)) {
            return;
        }
        
        if (target != null) {
            if (target.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockTarget = (BlockHitResult)target;
                BlockPos targetPos = blockTarget.getBlockPos();
                if (world.mayInteract(player, targetPos) && !ForgeEventFactory.onBlockPlace(player, BlockSnapshot.create(world.dimension(), world, targetPos), Direction.UP)) {
                    this.placeBlockState(player, world, targetPos, blockTarget);
                }
            } else if (target.getType() == HitResult.Type.ENTITY) {
                // If the target is an entity, place the new block at the entity's position
                BlockHitResult blockTarget = RayTraceUtils.getBlockResultFromEntityResult((EntityHitResult)target);
                if (world.mayInteract(player, blockTarget.getBlockPos()) && !ForgeEventFactory.onBlockPlace(player, BlockSnapshot.create(world.dimension(), world, blockTarget.getBlockPos()), Direction.UP)) {
                    // Only place if the caster is allowed to modify the target location
                    this.placeBlockState(player, world, blockTarget.getBlockPos(), blockTarget);
                }
            }
        }
    }
    
    protected void placeBlockState(Player player, Level world, BlockPos pos, BlockHitResult blockTarget) {
        BlockState state = world.getBlockState(pos);
        FluidState fluidState = world.getFluidState(pos);
        Material material = state.getMaterial();
        boolean isSolid = material.isSolid();
        boolean isReplaceable = material.isReplaceable();
        if (world.isEmptyBlock(pos) || !isSolid || isReplaceable) {
            // Destroy the existing block at the target location if the new block would replace it
            if (!world.isClientSide && (!isSolid || isReplaceable) && !material.isLiquid()) {
                world.destroyBlock(pos, true);
            }
            
            // Otherwise set the target state, with facing and waterlogging if applicable, into the world
            BlockState newState = this.targetState;
            if (state.hasProperty(BlockStateProperties.FACING)) {
                newState = newState.setValue(BlockStateProperties.FACING, state.getValue(BlockStateProperties.FACING));
            } else if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
            }
            if (fluidState.isSourceOfType(Fluids.WATER)) {
                newState = newState.setValue(BlockStateProperties.WATERLOGGED, Boolean.TRUE);
            }
            world.setBlock(pos, newState, Constants.BlockFlags.DEFAULT);
        } else if (blockTarget != null) {
            // If we can't place the new block at the given position, place it instead at the adjacent position defined by the raytrace result
            this.placeBlockState(player, world, blockTarget.getBlockPos().relative(blockTarget.getDirection()), null);
        }
    }
}
