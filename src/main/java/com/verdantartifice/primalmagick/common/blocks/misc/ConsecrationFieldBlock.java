package com.verdantartifice.primalmagick.common.blocks.misc;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Block definition for the consecration field.  Consecration fields prevent entry by non-player mobs
 * and grant players health and hunger recovery while standing inside.
 * 
 * @author Daedalus4096
 */
public class ConsecrationFieldBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    
    public ConsecrationFieldBlock() {
        super(Block.Properties.of().replaceable().strength(-1, 3600000).lightLevel(state -> 15).isSuffocating((state, blockReader, pos) -> {
            return false;
        }).isViewBlocking((state, blockReader, pos) -> {
            return false;
        }).noLootTable().noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        // Show glittering particles
        super.animateTick(stateIn, worldIn, pos, rand);
        FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Source.HALLOWED.getColor());
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        // Don't show a selection highlight when mousing over the field
        return Shapes.empty();
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        // The field should only have a collision box for living, non-player entities; everything else can pass through it
        if (context instanceof EntityCollisionContext ecc) {
            if (ecc.getEntity() != null) {
                Entity entity = ecc.getEntity();
                if (entity instanceof Player || !(entity instanceof LivingEntity)) {
                    return Shapes.empty();
                }
            }
        }
        return Shapes.block();
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
    
    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        // Don't work with the creative pick-block feature, as this block has no corresponding item block
        return ItemStack.EMPTY;
    }
    
    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        // While a player is in the field, give them regeneration and hunger recovery for a few seconds
        if (entityIn instanceof Player && entityIn.tickCount % 5 == 0) {
            Player player = (Player)entityIn;
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 110));
            player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 110));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
