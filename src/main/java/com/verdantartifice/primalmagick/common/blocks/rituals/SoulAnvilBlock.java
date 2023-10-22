package com.verdantartifice.primalmagick.common.blocks.rituals;

import java.awt.Color;
import java.util.Map;

import com.google.common.collect.Maps;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.rituals.SoulAnvilTileEntity;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Block definition for a soul anvil.  Soul anvils serve as props in magickal rituals; breaking a soul
 * gem on one at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class SoulAnvilBlock extends BaseEntityBlock implements IRitualPropBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty DIRTY = BooleanProperty.create("dirty");
    
    protected static final VoxelShape BASE_SHAPE = VoxelShapeUtils.fromModel(PrimalMagick.resource("block/soul_anvil"));
    protected static final Map<Direction, VoxelShape> SHAPES = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
        map.put(Direction.NORTH, BASE_SHAPE);
        map.put(Direction.SOUTH, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
        map.put(Direction.WEST, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
        map.put(Direction.EAST, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
    });
    
    public SoulAnvilBlock() {
        super(Block.Properties.of().mapColor(MapColor.COLOR_RED).pushReaction(PushReaction.BLOCK).strength(5.0F, 1200.0F).sound(SoundType.ANVIL));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(DIRTY, Boolean.FALSE));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING, DIRTY);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPES.getOrDefault(state.getValue(FACING), BASE_SHAPE);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise());
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoulAnvilTileEntity(pos, state);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (player != null && player.getItemInHand(handIn).getItem() == ItemsPM.SOUL_GEM.get() && !state.getValue(DIRTY)) {
            // If using a soul gem on a clean anvil, break it
            worldIn.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F + (worldIn.random.nextFloat() * 0.4F));
            if (!worldIn.isClientSide) {
                worldIn.setBlock(pos, state.setValue(DIRTY, Boolean.TRUE), Block.UPDATE_ALL_IMMEDIATE);
                if (!player.getAbilities().instabuild) {
                    player.getItemInHand(handIn).shrink(1);
                    if (player.getItemInHand(handIn).getCount() <= 0) {
                        player.setItemInHand(handIn, ItemStack.EMPTY);
                    }
                }
                
                // If this block is awaiting activation for an altar, notify it
                if (this.isPropOpen(state, worldIn, pos)) {
                    this.onPropActivated(state, worldIn, pos, this.getUsageStabilityBonus());
                }
            }
            return InteractionResult.SUCCESS;
        } else if (player != null && player.getItemInHand(handIn).is(ItemTagsPM.MAGICKAL_CLOTH) && state.getValue(DIRTY)) {
            // If using a magickal cloth on a dirty anvil, clean it
            worldIn.playSound(player, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.BLOCKS, 1.0F, 0.8F + (worldIn.random.nextFloat() * 0.4F));
            if (!worldIn.isClientSide) {
                worldIn.setBlock(pos, state.setValue(DIRTY, Boolean.FALSE), Block.UPDATE_ALL_IMMEDIATE);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        // Close out any pending ritual activity if replaced
        if (!worldIn.isClientSide && state.getBlock() != newState.getBlock()) {
            this.closeProp(state, worldIn, pos);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
    
    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        // Show spell sparkles if receiving salt power
        if (this.isBlockSaltPowered(worldIn, pos)) {
            FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Color.WHITE.getRGB());
        }
    }
    
    @Override
    public float getStabilityBonus(Level world, BlockPos pos) {
        return 0.03F;
    }

    @Override
    public float getSymmetryPenalty(Level world, BlockPos pos) {
        return 0.03F;
    }

    @Override
    public boolean isPropActivated(BlockState state, Level world, BlockPos pos) {
        return state != null && state.hasProperty(DIRTY) && state.getValue(DIRTY);
    }

    @Override
    public String getPropTranslationKey() {
        return "ritual.primalmagick.prop.soul_anvil";
    }

    public float getUsageStabilityBonus() {
        return 15.0F;
    }
}
