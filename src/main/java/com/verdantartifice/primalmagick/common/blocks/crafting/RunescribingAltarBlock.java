package com.verdantartifice.primalmagick.common.blocks.crafting;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunescribingAltarTileEntity;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Block definition for the runescribing altar.  May be used to apply combinations of runes to
 * items in order to imbue them with controllable enchantments.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarBlock extends BaseEntityBlock implements ITieredDevice {
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(PrimalMagick.resource("block/runescribing_altar_basic"));
    
    protected final DeviceTier tier;

    public RunescribingAltarBlock(DeviceTier tier) {
        super(Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE));
        this.tier = tier;
    }
    
    @Override
    public DeviceTier getDeviceTier() {
        return this.tier;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RunescribingAltarTileEntity(pos, state);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide && player instanceof ServerPlayer serverPlayer) {
            // Open the GUI for the altar
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof RunescribingAltarTileEntity altarTile) {
                serverPlayer.openMenu(altarTile, tile.getBlockPos());
            }
        }
        return InteractionResult.SUCCESS;
    }
}
