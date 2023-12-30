package com.verdantartifice.primalmagick.common.spells.payloads;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.util.RayTraceUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Base class for a fluid conjuration spell.  Creates a source block of fluid at the designated point.
 * Works similarly to placing fluid from a bucket.  Has no effect when cast by non-players.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractConjureFluidSpellPayload extends AbstractSpellPayload {
    protected final FlowingFluid fluid;
    
    public AbstractConjureFluidSpellPayload(FlowingFluid fluid) {
        super();
        this.fluid = fluid;
    }
    
    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        if (!(caster instanceof Player)) {
            return;
        }
        
        Player player = (Player)caster;
        if (target != null) {
            ItemStack stack = this.getSimulatedItemStack(this.fluid);
            if (target.getType() == HitResult.Type.BLOCK) {
                // If the target is a block, place the fluid at the block position if it's a fluid container,
                // or at the adjacent block position if it's not.
                BlockHitResult blockTarget = (BlockHitResult)target;
                BlockPos targetPos = blockTarget.getBlockPos();
                if (world.mayInteract(player, targetPos) && player.mayUseItemAt(targetPos, blockTarget.getDirection(), stack)) {
                    // Only place if the caster is allowed to modify the target location
                    BlockState state = world.getBlockState(targetPos);
                    BlockPos placePos = (state.getBlock() instanceof LiquidBlockContainer && this.fluid == Fluids.WATER) ? targetPos : targetPos.relative(blockTarget.getDirection());
                    this.placeFluid(player, world, placePos, blockTarget);
                }
            } else if (target.getType() == HitResult.Type.ENTITY) {
                // If the target is an entity, place the fluid at the entity's position
                BlockHitResult blockTarget = RayTraceUtils.getBlockResultFromEntityResult((EntityHitResult)target);
                if (world.mayInteract(player, blockTarget.getBlockPos()) && player.mayUseItemAt(blockTarget.getBlockPos(), blockTarget.getDirection(), stack)) {
                    // Only place if the caster is allowed to modify the target location
                    this.placeFluid(player, world, blockTarget.getBlockPos(), blockTarget);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    protected ItemStack getSimulatedItemStack(@Nonnull Fluid fluid) {
        if (fluid.is(FluidTags.WATER)) {
            return new ItemStack(Items.WATER_BUCKET);
        } else if (fluid.is(FluidTags.LAVA)) {
            return new ItemStack(Items.LAVA_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @SuppressWarnings("deprecation")
    protected void placeFluid(Player player, Level world, BlockPos pos, BlockHitResult blockTarget) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        boolean isSolid = state.isSolid();
        boolean isReplaceable = state.canBeReplaced();
        if (world.isEmptyBlock(pos) || !isSolid || isReplaceable || (block instanceof LiquidBlockContainer lbc && lbc.canPlaceLiquid(player, world, pos, state, this.fluid))) {
            if (world.dimensionType().ultraWarm() && this.fluid.is(FluidTags.WATER)) {
                // Do nothing for water in the Nether or similar dimensions
                return;
            } else if (block instanceof LiquidBlockContainer lbc && this.fluid == Fluids.WATER) {
                // If the block is a liquid container and we're dealing with water, place the water into the container
                lbc.placeLiquid(world, pos, state, this.fluid.getSource(false));
            } else {
                // Destroy the existing block at the target location if fluid would replace it
                if (!world.isClientSide && (!isSolid || isReplaceable) && !state.liquid()) {
                    world.destroyBlock(pos, true);
                }
                
                // Set the fluid block into the world
                world.setBlock(pos, this.fluid.defaultFluidState().createLegacyBlock(), Block.UPDATE_ALL_IMMEDIATE);
            }
        } else if (blockTarget != null) {
            // If we can't place the fluid at the given position, place it instead at the adjacent position defined by the raytrace result
            this.placeFluid(player, world, blockTarget.getBlockPos().relative(blockTarget.getDirection()), null);
        }
    }
}
