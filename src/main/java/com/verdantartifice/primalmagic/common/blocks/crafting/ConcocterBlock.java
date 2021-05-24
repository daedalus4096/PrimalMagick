package com.verdantartifice.primalmagic.common.blocks.crafting;

import java.util.List;
import java.util.Random;

import com.verdantartifice.primalmagic.common.misc.HarvestLevel;
import com.verdantartifice.primalmagic.common.sources.IManaContainer;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.tiles.crafting.ConcocterTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

/**
 * Block definition for a concocter.  Used to craft multi-dose potions.
 * 
 * @author Daedalus4096
 */
public class ConcocterBlock extends Block {
    public static final BooleanProperty HAS_BOTTLE = BlockStateProperties.HAS_BOTTLE_0;
    protected static final VoxelShape SHAPE = VoxelShapes.or(Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D), Block.makeCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 14.0D, 9.0D));

    public ConcocterBlock() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(0.5F).harvestTool(ToolType.PICKAXE).harvestLevel(HarvestLevel.WOOD.getLevel()).setLightLevel(state -> {
            return 1;
        }).notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(HAS_BOTTLE, false));
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(HAS_BOTTLE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ConcocterTileEntity();
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
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof IManaContainer) {
            CompoundNBT nbt = stack.getChildTag("ManaContainerTag");
            if (nbt != null) {
                SourceList mana = new SourceList();
                mana.deserializeNBT(nbt);
                ((IManaContainer)tile).setMana(mana);
            }
        }

        // Set the concocter tile entity's owner when placed by a player.  Needed so that the tile entity can do research checks.
        if (!worldIn.isRemote && placer instanceof PlayerEntity) {
            if (tile instanceof ConcocterTileEntity) {
                ((ConcocterTileEntity)tile).setTileOwner((PlayerEntity)placer);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        // Drop the tile entity's inventory into the world when the block is replaced
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof ConcocterTileEntity) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (ConcocterTileEntity)tile);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            // Open the GUI for the concocter
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof ConcocterTileEntity) {
                player.openContainer((ConcocterTileEntity)tile);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        CompoundNBT nbt = stack.getChildTag("ManaContainerTag");
        if (nbt != null) {
            SourceList mana = new SourceList();
            mana.deserializeNBT(nbt);
            for (Source source : Source.SORTED_SOURCES) {
                int amount = mana.getAmount(source);
                if (amount > 0) {
                    ITextComponent nameComp = new TranslationTextComponent(source.getNameTranslationKey()).mergeStyle(source.getChatColor());
                    ITextComponent line = new TranslationTextComponent("primalmagic.source.mana_container_tooltip", nameComp, (amount / 100.0D));
                    tooltip.add(line);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double)pos.getX() + 0.4D + (double)rand.nextFloat() * 0.2D;
        double d1 = (double)pos.getY() + 0.7D + (double)rand.nextFloat() * 0.3D;
        double d2 = (double)pos.getZ() + 0.4D + (double)rand.nextFloat() * 0.2D;
        worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }
}
