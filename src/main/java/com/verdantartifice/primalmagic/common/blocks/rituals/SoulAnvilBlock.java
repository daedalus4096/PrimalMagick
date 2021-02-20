package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.awt.Color;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.misc.HarvestLevel;
import com.verdantartifice.primalmagic.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagic.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagic.common.tiles.rituals.SoulAnvilTileEntity;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.Constants;

/**
 * Block definition for a soul anvil.  Soul anvils serve as props in magical rituals; breaking a soul
 * gem on one at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class SoulAnvilBlock extends FallingBlock implements IRitualPropBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty DIRTY = BooleanProperty.create("dirty");
    
    protected static final VoxelShape BASE_SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/soul_anvil"));
    protected static final Map<Direction, VoxelShape> SHAPES = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
        map.put(Direction.NORTH, BASE_SHAPE);
        map.put(Direction.SOUTH, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
        map.put(Direction.WEST, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
        map.put(Direction.EAST, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
    });
    
    public SoulAnvilBlock() {
        super(Block.Properties.create(Material.ANVIL, MaterialColor.RED).hardnessAndResistance(5.0F, 1200.0F).sound(SoundType.ANVIL).harvestTool(ToolType.PICKAXE).harvestLevel(HarvestLevel.WOOD.getLevel()));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(DIRTY, Boolean.FALSE));
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(FACING, DIRTY);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.getOrDefault(state.get(FACING), BASE_SHAPE);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().rotateY());
    }
    
    @Override
    protected void onStartFalling(FallingBlockEntity fallingEntity) {
        fallingEntity.setHurtEntities(true);
    }
    
    @Override
    public void onEndFalling(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
    	if (!fallingBlock.isSilent()) {
            worldIn.playEvent(1031, pos, 0);
    	}
    }
    
    @Override
    public void onBroken(World worldIn, BlockPos pos, FallingBlockEntity fallingBlock) {
    	if (!fallingBlock.isSilent()) {
            worldIn.playEvent(1029, pos, 0);
    	}
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }
    
    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SoulAnvilTileEntity();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        // Pass any received events on to the tile entity and let it decide what to do with it
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tile = worldIn.getTileEntity(pos);
        return (tile == null) ? false : tile.receiveClientEvent(id, param);
    }
    
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (player != null && player.getHeldItem(handIn).getItem() == ItemsPM.SOUL_GEM.get() && !state.get(DIRTY)) {
            // If using a soul gem on a clean anvil, break it
            worldIn.playSound(player, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 0.8F + (RANDOM.nextFloat() * 0.4F));
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos, state.with(DIRTY, Boolean.TRUE), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                if (!player.abilities.isCreativeMode) {
                    player.getHeldItem(handIn).shrink(1);
                    if (player.getHeldItem(handIn).getCount() <= 0) {
                        player.setHeldItem(handIn, ItemStack.EMPTY);
                    }
                }
                
                // If this block is awaiting activation for an altar, notify it
                if (this.isPropOpen(state, worldIn, pos)) {
                    this.onPropActivated(state, worldIn, pos);
                }
            }
            return ActionResultType.SUCCESS;
        } else if (player != null && player.getHeldItem(handIn).getItem().isIn(ItemTagsPM.MAGICAL_CLOTH) && state.get(DIRTY)) {
            // If using a magical cloth on a dirty anvil, clean it
            worldIn.playSound(player, pos, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.BLOCKS, 1.0F, 0.8F + (RANDOM.nextFloat() * 0.4F));
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos, state.with(DIRTY, Boolean.FALSE), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            }
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        // Close out any pending ritual activity if replaced
        if (!worldIn.isRemote && state.getBlock() != newState.getBlock()) {
            this.closeProp(state, worldIn, pos);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
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
        return 0.03F;
    }

    @Override
    public float getSymmetryPenalty(World world, BlockPos pos) {
        return 0.03F;
    }

    @Override
    public boolean isPropActivated(BlockState state, World world, BlockPos pos) {
        if (state != null && state.getBlock() instanceof SoulAnvilBlock) {
            return state.get(DIRTY);
        } else {
            return false;
        }
    }

    @Override
    public String getPropTranslationKey() {
        return "primalmagic.ritual.prop.soul_anvil";
    }

    @Override
    public float getUsageStabilityBonus() {
        return 15.0F;
    }
}
