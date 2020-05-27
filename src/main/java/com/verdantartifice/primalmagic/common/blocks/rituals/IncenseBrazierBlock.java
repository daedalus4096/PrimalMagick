package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.awt.Color;
import java.util.Random;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.rituals.IRitualProp;
import com.verdantartifice.primalmagic.common.tiles.rituals.IncenseBrazierTileEntity;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
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
 * Block definition for an incense brazier.  Incense braziers serve as props in magical rituals; placing
 * incense in them at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class IncenseBrazierBlock extends Block implements IRitualProp {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/incense_brazier"));

    public IncenseBrazierBlock() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.METAL).lightValue(7));
        this.setDefaultState(this.getDefaultState().with(LIT, Boolean.FALSE));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(LIT);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public int getLightValue(BlockState state) {
        // Only give off light if the brazier is lit
        return state.get(LIT) ? super.getLightValue(state) : 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        // Show flame particles if lit
        if (stateIn.get(LIT)) {
            double x = pos.getX() + 0.5D;
            double y = pos.getY() + 0.8D;
            double z = pos.getZ() + 0.65D;
            worldIn.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
        }
        
        // Show spell sparkles if receiving salt power
        if (this.isBlockSaltPowered(worldIn, pos)) {
            FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Color.WHITE.getRGB());
        }
    }
    
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (player != null && player.getHeldItem(handIn).getItem() == ItemsPM.INCENSE_STICK.get() && !state.get(LIT)) {
            // If using an incense stick on an unlit brazier, light it
            worldIn.playSound(player, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F + (RANDOM.nextFloat() * 0.4F));
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos, state.with(LIT, Boolean.TRUE), Constants.BlockFlags.DEFAULT_AND_RERENDER);
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
        } else if (player != null && player.getHeldItem(handIn).isEmpty() && state.get(LIT)) {
            // If using an empty hand on a lit brazier, snuff it
            worldIn.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos, state.with(LIT, Boolean.FALSE), Constants.BlockFlags.DEFAULT_AND_RERENDER);
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
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new IncenseBrazierTileEntity();
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
    public float getStabilityBonus(World world, BlockPos pos) {
        return 0.01F;
    }

    @Override
    public float getSymmetryPenalty(World world, BlockPos pos) {
        return 0.01F;
    }

    @Override
    public boolean isPropActivated(BlockState state, World world, BlockPos pos) {
        if (state != null && state.getBlock() instanceof RitualCandleBlock) {
            return state.get(LIT);
        } else {
            return false;
        }
    }

    @Override
    public String getPropTranslationKey() {
        return "primalmagic.ritual.prop.incense_brazier";
    }

    @Override
    public float getUsageStabilityBonus() {
        return 5.0F;
    }

}
