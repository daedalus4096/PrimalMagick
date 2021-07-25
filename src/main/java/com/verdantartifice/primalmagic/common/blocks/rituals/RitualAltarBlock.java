package com.verdantartifice.primalmagic.common.blocks.rituals;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.rituals.ISaltPowered;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualAltarTileEntity;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

/**
 * Block definition for the ritual altar.  It is the central component of magical rituals, providing
 * the salt "power" that enables the functionality of other ritual props.  It's also where the ritual
 * output appears.
 * 
 * @author Daedalus4096
 */
public class RitualAltarBlock extends Block implements ISaltPowered {
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/ritual_altar"));
    
    public RitualAltarBlock() {
        super(Block.Properties.of(Material.STONE, MaterialColor.QUARTZ).strength(1.5F, 6.0F).sound(SoundType.STONE));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        for (Direction dir : Direction.values()) {
            worldIn.updateNeighborsAt(pos.relative(dir), this);
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving) {
            for (Direction dir : Direction.values()) {
                worldIn.updateNeighborsAt(pos.relative(dir), this);
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
    
    public int getMaxSaltPower() {
        return 15;
    }
    
    public int getMaxSafeSalt() {
        return 16;
    }
    
    @Override
    public int getStrongSaltPower(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return (side != Direction.UP) ? this.getMaxSaltPower() : 0;
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new RitualAltarTileEntity();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean triggerEvent(BlockState state, Level worldIn, BlockPos pos, int id, int param) {
        // Pass any received events on to the tile entity and let it decide what to do with it
        super.triggerEvent(state, worldIn, pos, id, param);
        BlockEntity tile = worldIn.getBlockEntity(pos);
        return (tile == null) ? false : tile.triggerEvent(id, param);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide && handIn == InteractionHand.MAIN_HAND) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof RitualAltarTileEntity) {
                RitualAltarTileEntity altarTile = (RitualAltarTileEntity)tile;
                if (!altarTile.getItem(0).isEmpty() && player.getItemInHand(handIn).isEmpty()) {
                    // When activating a full altar with an empty hand, pick up the item
                    ItemStack stack = altarTile.getItem(0).copy();
                    altarTile.setItem(0, ItemStack.EMPTY);
                    player.setItemInHand(handIn, stack);
                    player.inventory.setChanged();
                    worldIn.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }
}
