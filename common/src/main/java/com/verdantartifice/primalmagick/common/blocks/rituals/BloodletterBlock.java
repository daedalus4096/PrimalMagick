package com.verdantartifice.primalmagick.common.blocks.rituals;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.damagesource.DamageSourcesPM;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagick.common.tiles.rituals.BloodletterTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.awt.Color;

/**
 * Block definition for a bloodletter.  Bloodletters serve as props in magickal rituals; cutting
 * yourself on them at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class BloodletterBlock extends BaseEntityBlock implements IRitualPropBlock {
    public static final MapCodec<BloodletterBlock> CODEC = simpleCodec(BloodletterBlock::new);
    
    public static final BooleanProperty FILLED = BooleanProperty.create("filled");
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(ResourceUtils.loc("block/bloodletter"));
    
    public BloodletterBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FILLED, Boolean.FALSE));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FILLED);
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (player != null && stack.is(Items.WATER_BUCKET) && state.getValue(FILLED)) {
            // If using a water bucket on a filled bloodletter, clean it out
            worldIn.playSound(player, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!worldIn.isClientSide) {
                if (!player.hasInfiniteMaterials()) {
                    player.setItemInHand(handIn, new ItemStack(Items.BUCKET));
                }
                worldIn.setBlock(pos, state.setValue(FILLED, Boolean.FALSE), Block.UPDATE_ALL_IMMEDIATE);
            }
            return ItemInteractionResult.SUCCESS;
        } else {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }
    
    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pPlayer != null && !pState.getValue(FILLED)) {
            // If using an empty hand on an unfilled bloodletter, cut the player
            if (!pLevel.isClientSide) {
                pPlayer.hurt(DamageSourcesPM.bleeding(pLevel.registryAccess()), 2.0F);
                pLevel.setBlock(pPos, pState.setValue(FILLED, Boolean.TRUE), Block.UPDATE_ALL_IMMEDIATE);
                
                // If this block is awaiting activation for an altar, notify it
                if (this.isPropOpen(pState, pLevel, pPos)) {
                    this.onPropActivated(pState, pLevel, pPos, this.getUsageStabilityBonus());
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
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
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        // Show spell sparkles if receiving salt power
        if (this.isBlockSaltPowered(worldIn, pos)) {
            FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Color.WHITE.getRGB());
        }
    }
    
    @Override
    public float getStabilityBonus(Level world, BlockPos pos) {
        return 0.03F;
    }

    @Override
    public float getSymmetryPenalty(Level world, BlockPos pos) {
        return 0.03F;
    }

    @Override
    public boolean isPropActivated(BlockState state, Level world, BlockPos pos) {
        return state != null && state.hasProperty(FILLED) && state.getValue(FILLED);
    }

    @Override
    public String getPropTranslationKey() {
        return "ritual.primalmagick.prop.bloodletter";
    }

    public float getUsageStabilityBonus() {
        return 15.0F;
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BloodletterTileEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
