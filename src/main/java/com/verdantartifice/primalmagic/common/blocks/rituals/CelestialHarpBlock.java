package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.awt.Color;
import java.util.Random;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.client.fx.particles.NoteEmitterParticleData;
import com.verdantartifice.primalmagic.common.misc.HarvestLevel;
import com.verdantartifice.primalmagic.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.tiles.rituals.CelestialHarpTileEntity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

/**
 * Block definition for a celestial harp.  Celestial harps serve as props in magical rituals;
 * playing them at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class CelestialHarpBlock extends Block implements IRitualPropBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    
    protected static final VoxelShape SHAPE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 16.0D));
    
    public CelestialHarpBlock() {
        super(Block.Properties.of(Material.WOOD, MaterialColor.GOLD).strength(2.5F).sound(SoundType.WOOD).harvestTool(ToolType.AXE).harvestLevel(HarvestLevel.NONE.getLevel()));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
    
    @Override
    public float getStabilityBonus(Level world, BlockPos pos) {
        return 0.04F;
    }

    @Override
    public float getSymmetryPenalty(Level world, BlockPos pos) {
        return 0.04F;
    }

    @Override
    public boolean isPropActivated(BlockState state, Level world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        return (tile instanceof CelestialHarpTileEntity && ((CelestialHarpTileEntity)tile).isPlaying());
    }

    @Override
    public String getPropTranslationKey() {
        return "primalmagic.ritual.prop.celestial_harp";
    }

    @Override
    public float getUsageStabilityBonus() {
        return 20.0F;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (player != null && player.getItemInHand(handIn).isEmpty() && !this.isPropActivated(state, worldIn, pos)) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            final double noteHue = 2.0D / 24.0D;
            worldIn.playSound(player, pos, SoundsPM.HARP.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            worldIn.addParticle(new NoteEmitterParticleData(noteHue, CelestialHarpTileEntity.TICKS_PER_PLAY), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
            if (!worldIn.isClientSide && tile instanceof CelestialHarpTileEntity) {
                // Start the harp tile entity playing
                ((CelestialHarpTileEntity)tile).startPlaying();
                
                // If this block is awaiting activation for an altar, notify it
                if (this.isPropOpen(state, worldIn, pos)) {
                    this.onPropActivated(state, worldIn, pos);
                }
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
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        // Show spell sparkles if receiving salt power
        if (this.isBlockSaltPowered(worldIn, pos)) {
            FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Color.WHITE.getRGB());
        }
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new CelestialHarpTileEntity();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean triggerEvent(BlockState state, Level worldIn, BlockPos pos, int id, int param) {
        // Pass any received events on to the tile entity and let it decide what to do with it
        super.triggerEvent(state, worldIn, pos, id, param);
        BlockEntity tile = worldIn.getBlockEntity(pos);
        return (tile == null) ? false : tile.triggerEvent(id, param);
    }
}
