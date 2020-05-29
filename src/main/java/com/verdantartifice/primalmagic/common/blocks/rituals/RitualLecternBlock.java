package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.awt.Color;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;
import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualLecternTileEntity;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

/**
 * Block definition for a ritual lectern.  Ritual lecterns serve as props in magical rituals; placing
 * an enchanted book on one at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class RitualLecternBlock extends Block implements IRitualPropBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_BOOK = BlockStateProperties.HAS_BOOK;

    protected static final VoxelShape BASE_AND_STAND_SHAPE = VoxelShapes.or(Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.makeCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D));
    protected static final VoxelShape COLLISION_SHAPE = VoxelShapes.or(BASE_AND_STAND_SHAPE, Block.makeCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 15.0D, 16.0D));
    protected static final VoxelShape SHAPE = VoxelShapes.or(Block.makeCuboidShape(0.0D, 10.0D, 1.0D, 16.0D, 14.0D, 5.333333D), Block.makeCuboidShape(0.0D, 12.0D, 5.333333D, 16.0D, 16.0D, 9.666667D), Block.makeCuboidShape(0.0D, 14.0D, 9.666667D, 16.0D, 18.0D, 14.0D), BASE_AND_STAND_SHAPE);
    protected static final Map<Direction, VoxelShape> SHAPES = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
        map.put(Direction.NORTH, SHAPE);
        map.put(Direction.SOUTH, VoxelShapeUtils.rotate(SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
        map.put(Direction.WEST, VoxelShapeUtils.rotate(SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
        map.put(Direction.EAST, VoxelShapeUtils.rotate(SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
    });
    
    public RitualLecternBlock() {
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(HAS_BOOK, Boolean.FALSE));
    }
    
    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return BASE_AND_STAND_SHAPE;
    }
    
    @Override
    public boolean isTransparent(BlockState state) {
        return true;
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return COLLISION_SHAPE;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.getOrDefault(state.get(FACING), BASE_AND_STAND_SHAPE);
    }
    
    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.with(FACING, direction.rotate(state.get(FACING)));
    }
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_BOOK);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote && handIn == Hand.MAIN_HAND) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof RitualLecternTileEntity) {
                RitualLecternTileEntity lecternTile = (RitualLecternTileEntity)tile;
                if (lecternTile.getStackInSlot(0).isEmpty() && player.getHeldItem(handIn).getItem() == Items.ENCHANTED_BOOK) {
                    // When activating an empty lectern with an enchanted book in hand, place it on the lectern
                    ItemStack stack = player.getHeldItem(handIn).copy();
                    stack.setCount(1);
                    lecternTile.setInventorySlotContents(0, stack);
                    player.getHeldItem(handIn).shrink(1);
                    if (player.getHeldItem(handIn).getCount() <= 0) {
                        player.setHeldItem(handIn, ItemStack.EMPTY);
                    }
                    player.inventory.markDirty();
                    worldIn.playSound(null, pos, SoundEvents.ITEM_BOOK_PUT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    worldIn.setBlockState(pos, state.with(HAS_BOOK, Boolean.TRUE), Constants.BlockFlags.DEFAULT);
                    
                    // If this block is awaiting activation for an altar, notify it
                    if (this.isPropOpen(state, worldIn, pos)) {
                        this.onPropActivated(state, worldIn, pos);
                    }

                    return ActionResultType.SUCCESS;
                } else if (!lecternTile.getStackInSlot(0).isEmpty() && player.getHeldItem(handIn).isEmpty()) {
                    // When activating a full lectern with an empty hand, pick up the book
                    ItemStack stack = lecternTile.getStackInSlot(0).copy();
                    lecternTile.setInventorySlotContents(0, ItemStack.EMPTY);
                    player.setHeldItem(handIn, stack);
                    player.inventory.markDirty();
                    worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.4F, 1.0F);
                    worldIn.setBlockState(pos, state.with(HAS_BOOK, Boolean.FALSE), Constants.BlockFlags.DEFAULT);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }
    
    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        // Show spell sparkles if receiving salt power
        if (this.isBlockSaltPowered(worldIn, pos)) {
            FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Color.WHITE.getRGB());
        }
    }

    @Override
    public float getStabilityBonus(World world, BlockPos pos) {
        return 0.02F;
    }

    @Override
    public float getSymmetryPenalty(World world, BlockPos pos) {
        return 0.02F;
    }

    @Override
    public boolean isPropActivated(BlockState state, World world, BlockPos pos) {
        if (state != null && state.getBlock() instanceof RitualLecternBlock) {
            return state.get(HAS_BOOK);
        } else {
            return false;
        }
    }

    @Override
    public String getPropTranslationKey() {
        return "primalmagic.ritual.prop.ritual_lectern";
    }

    @Override
    public float getUsageStabilityBonus() {
        return 10.0F;
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new RitualLecternTileEntity();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        // Pass any received events on to the tile entity and let it decide what to do with it
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tile = worldIn.getTileEntity(pos);
        return (tile == null) ? false : tile.receiveClientEvent(id, param);
    }
}
