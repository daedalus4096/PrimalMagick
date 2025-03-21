package com.verdantartifice.primalmagick.common.blocks.rituals;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.rituals.EntropySinkTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.awt.Color;

/**
 * Block definition for an entropy sink.  Entropy sinks are optional props used in rituals to
 * reduce instability at the cost of essence.
 * 
 * @author Daedalus4096
 */
public class EntropySinkBlock extends BaseEntityBlock implements IRitualPropBlock {
    public static final MapCodec<EntropySinkBlock> CODEC = simpleCodec(EntropySinkBlock::new);
    
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(ResourceUtils.loc("block/entropy_sink"));
    
    public EntropySinkBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, LIT);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Make the block face the player when placed
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
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
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EntropySinkTileEntity(pos, state);
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        // Show spell sparkles if receiving salt power
        if (this.isBlockSaltPowered(worldIn, pos)) {
            FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Color.WHITE.getRGB());
        }
        
        // Show redstone particles if glowing
        if (stateIn.getValue(LIT)) {
            double x = (double)pos.getX() + (0.625D * rand.nextDouble()) + 0.1875D;
            double y = (double)pos.getY() + 1.0D;
            double z = (double)pos.getZ() + (0.625D * rand.nextDouble()) + 0.1875D;
            worldIn.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public float getStabilityBonus(Level world, BlockPos pos) {
        return 0.02F;
    }

    @Override
    public float getSymmetryPenalty(Level world, BlockPos pos) {
        return 0.02F;
    }

    @Override
    public boolean isPropActivated(BlockState state, Level world, BlockPos pos) {
        return world.getBlockEntity(pos) instanceof EntropySinkTileEntity sink && sink.isGlowing();
    }

    @Override
    public String getPropTranslationKey() {
        return "ritual.primalmagick.prop.entropy_sink";
    }

    public float getUsageStabilityBonus(EssenceItem item) {
        // Determine amount based on type of essence used
        return (float)item.getEssenceType().getAffinity();
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (player != null && stack.getItem() instanceof EssenceItem essenceItem && !this.isPropActivated(state, worldIn, pos)) {
            if (!worldIn.isClientSide && worldIn.getBlockEntity(pos) instanceof EntropySinkTileEntity sink) {
                // Start the sink glowing
                worldIn.setBlock(pos, state.setValue(EntropySinkBlock.LIT, Boolean.TRUE), Block.UPDATE_ALL_IMMEDIATE);
                sink.startGlowing();
                
                // If this block is awaiting activation for an altar, notify it
                if (this.isPropOpen(state, worldIn, pos)) {
                    this.onPropActivated(state, worldIn, pos, this.getUsageStabilityBonus(essenceItem));
                }
                
                // Consume the used essence
                if (!player.hasInfiniteMaterials()) {
                    stack.shrink(1);
                    if (stack.getCount() <= 0) {
                        player.setItemInHand(handIn, ItemStack.EMPTY);
                    }
                }
            }
            return ItemInteractionResult.SUCCESS;
        } else {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        // Close out any pending ritual activity if replaced
        if (!worldIn.isClientSide && state.getBlock() != newState.getBlock()) {
            this.closeProp(state, worldIn, pos);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityTypesPM.ENTROPY_SINK.get(), EntropySinkTileEntity::tick);
    }

    @Override
    public boolean isUniversal() {
        return true;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
