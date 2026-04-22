package com.verdantartifice.primalmagick.common.blocks.rituals;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.damagesource.DamageSourcesPM;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagick.common.tiles.rituals.BloodletterTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @NotNull
    protected InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level worldIn,
                                          @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn,
                                          @NotNull BlockHitResult hit) {
        if (stack.is(Items.WATER_BUCKET) && state.getValue(FILLED)) {
            // If using a water bucket on a filled bloodletter, clean it out
            worldIn.playSound(player, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!worldIn.isClientSide()) {
                if (!player.hasInfiniteMaterials()) {
                    player.setItemInHand(handIn, new ItemStack(Items.BUCKET));
                }
                worldIn.setBlock(pos, state.setValue(FILLED, Boolean.FALSE), Block.UPDATE_ALL_IMMEDIATE);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }
    
    @Override
    @NotNull
    protected InteractionResult useWithoutItem(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull BlockHitResult pHitResult) {
        if (!pState.getValue(FILLED)) {
            // If using an empty hand on an unfilled bloodletter, cut the player
            if (pLevel instanceof ServerLevel serverLevel) {
                pPlayer.hurtServer(serverLevel, DamageSourcesPM.bleeding(serverLevel.registryAccess()), 2.0F);
                serverLevel.setBlock(pPos, pState.setValue(FILLED, Boolean.TRUE), Block.UPDATE_ALL_IMMEDIATE);
                
                // If this block is awaiting activation for an altar, notify it
                if (this.isPropOpen(pState, serverLevel, pPos)) {
                    this.onPropActivated(pState, serverLevel, pPos, this.getUsageStabilityBonus());
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
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
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BloodletterTileEntity(pos, state);
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
