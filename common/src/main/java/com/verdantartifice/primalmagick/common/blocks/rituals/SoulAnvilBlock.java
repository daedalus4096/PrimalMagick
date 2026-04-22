package com.verdantartifice.primalmagick.common.blocks.rituals;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.rituals.SoulAnvilTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;
import net.minecraft.util.Util;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Map;

/**
 * Block definition for a soul anvil.  Soul anvils serve as props in magickal rituals; breaking a soul
 * gem on one at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class SoulAnvilBlock extends BaseEntityBlock implements IRitualPropBlock {
    public static final MapCodec<SoulAnvilBlock> CODEC = simpleCodec(SoulAnvilBlock::new);
    
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty DIRTY = BooleanProperty.create("dirty");
    
    protected static final VoxelShape BASE_SHAPE = VoxelShapeUtils.fromModel(ResourceUtils.loc("block/soul_anvil"));
    protected static final Map<Direction, VoxelShape> SHAPES = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
        map.put(Direction.NORTH, BASE_SHAPE);
        map.put(Direction.SOUTH, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
        map.put(Direction.WEST, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
        map.put(Direction.EAST, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
    });
    
    public SoulAnvilBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(DIRTY, Boolean.FALSE));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING, DIRTY);
    }
    
    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPES.getOrDefault(state.getValue(FACING), BASE_SHAPE);
    }
    
    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise());
    }
    
    @Override
    @NotNull
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType type) {
        return false;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SoulAnvilTileEntity(pos, state);
    }
    
    @Override
    @NotNull
    protected InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level worldIn,
                                          @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn,
                                          @NotNull BlockHitResult hit) {
        if (stack.is(ItemsPM.SOUL_GEM.get()) && !state.getValue(DIRTY)) {
            // If using a soul gem on a clean anvil, break it
            worldIn.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F + (worldIn.random.nextFloat() * 0.4F));
            if (!worldIn.isClientSide()) {
                worldIn.setBlock(pos, state.setValue(DIRTY, Boolean.TRUE), Block.UPDATE_ALL_IMMEDIATE);
                if (!player.hasInfiniteMaterials()) {
                    stack.shrink(1);
                    if (stack.getCount() <= 0) {
                        player.setItemInHand(handIn, ItemStack.EMPTY);
                    }
                }
                
                // If this block is awaiting activation for an altar, notify it
                if (this.isPropOpen(state, worldIn, pos)) {
                    this.onPropActivated(state, worldIn, pos, this.getUsageStabilityBonus());
                }
            }
            return InteractionResult.SUCCESS;
        } else if (player.getItemInHand(handIn).is(ItemTagsPM.MAGICKAL_CLOTH) && state.getValue(DIRTY)) {
            // If using a magickal cloth on a dirty anvil, clean it
            worldIn.playSound(player, pos, SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.BLOCKS, 1.0F, 0.8F + (worldIn.random.nextFloat() * 0.4F));
            if (!worldIn.isClientSide()) {
                worldIn.setBlock(pos, state.setValue(DIRTY, Boolean.FALSE), Block.UPDATE_ALL_IMMEDIATE);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }
    
    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
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

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
