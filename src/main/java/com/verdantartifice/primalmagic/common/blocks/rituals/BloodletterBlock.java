package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.awt.Color;
import java.util.Random;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.misc.DamageSourcesPM;
import com.verdantartifice.primalmagic.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagic.common.tiles.rituals.BloodletterTileEntity;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

/**
 * Block definition for a bloodletter.  Bloodletters serve as props in magical rituals; cutting
 * yourself on them at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class BloodletterBlock extends Block implements IRitualPropBlock {
    public static final BooleanProperty FILLED = BooleanProperty.create("filled");
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/bloodletter"));
    
    public BloodletterBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.OBSIDIAN).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE));
        this.setDefaultState(this.getDefaultState().with(FILLED, Boolean.FALSE));
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(FILLED);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (player != null && player.getHeldItem(handIn).isEmpty() && !state.get(FILLED)) {
            // If using an empty hand on an unfilled bloodletter, cut the player
            if (!worldIn.isRemote) {
                player.attackEntityFrom(DamageSourcesPM.BLEEDING, 2.0F);
                worldIn.setBlockState(pos, state.with(FILLED, Boolean.TRUE), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                
                // If this block is awaiting activation for an altar, notify it
                if (this.isPropOpen(state, worldIn, pos)) {
                    this.onPropActivated(state, worldIn, pos);
                }
            }
            return ActionResultType.SUCCESS;
        } else if (player != null && player.getHeldItem(handIn).getItem() == Items.WATER_BUCKET && state.get(FILLED)) {
            // If using a water bucket on a filled bloodletter, clean it out
            worldIn.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!worldIn.isRemote) {
                if (!player.abilities.isCreativeMode) {
                    player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                }
                worldIn.setBlockState(pos, state.with(FILLED, Boolean.FALSE), Constants.BlockFlags.DEFAULT_AND_RERENDER);
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
        if (state != null && state.getBlock() instanceof BloodletterBlock) {
            return state.get(FILLED);
        } else {
            return false;
        }
    }

    @Override
    public String getPropTranslationKey() {
        return "primalmagic.ritual.prop.bloodletter";
    }

    @Override
    public float getUsageStabilityBonus() {
        return 15.0F;
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BloodletterTileEntity();
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
