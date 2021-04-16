package com.verdantartifice.primalmagic.common.blocks.devices;

import java.util.List;

import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.tiles.devices.HoneyExtractorTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * Block definition for a honey extractor.  Uses sky mana to spin the honey out of honeycombs.
 * 
 * @author Daedalus4096
 */
public class HoneyExtractorBlock extends Block {
    protected static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public HoneyExtractorBlock() {
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.WOOD));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // Make the block face the player when placed
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new HoneyExtractorTileEntity();
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
        if (!worldIn.isRemote && player instanceof ServerPlayerEntity) {
            // Open the GUI for the honey extractor
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof HoneyExtractorTileEntity) {
                player.openContainer((HoneyExtractorTileEntity)tile);
            }
        }
        return ActionResultType.SUCCESS;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        // Drop the tile entity's inventory into the world when the block is replaced
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof HoneyExtractorTileEntity) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (HoneyExtractorTileEntity)tile);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        // TODO Fix up and abstract
        super.addInformation(stack, worldIn, tooltip, flagIn);
        CompoundNBT nbt = stack.getChildTag("ManaContainerTag");
        if (nbt != null) {
            SourceList mana = new SourceList();
            mana.deserializeNBT(nbt);
            int count = 0;
            for (Source source : Source.SORTED_SOURCES) {
                int amount = mana.getAmount(source);
                if (amount > 0) {
                    ITextComponent nameComp = new TranslationTextComponent(source.getNameTranslationKey()).mergeStyle(source.getChatColor());
                    ITextComponent line = new TranslationTextComponent("primalmagic.source.mana_gauge_tooltip", nameComp.getString(), amount / 100.0D, "100");
                    tooltip.add(line);
                    count++;
                }
            }
            if (count == 0) {
                tooltip.add(new StringTextComponent("Empty mana container"));
            }
        } else {
            tooltip.add(new StringTextComponent("No mana contained"));
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        // TODO Fix up and abstract
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof HoneyExtractorTileEntity) {
            CompoundNBT nbt = stack.getChildTag("ManaContainerTag");
            if (nbt != null) {
                SourceList mana = new SourceList();
                mana.deserializeNBT(nbt);
                for (Source source : Source.SORTED_SOURCES) {
                    int amount = mana.getAmount(source);
                    if (amount > 0) {
                        ((HoneyExtractorTileEntity)tile).setMana(source, amount);
                    }
                }
            }
        }
    }
}
