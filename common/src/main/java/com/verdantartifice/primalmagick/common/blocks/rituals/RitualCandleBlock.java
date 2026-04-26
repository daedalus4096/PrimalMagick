package com.verdantartifice.primalmagick.common.blocks.rituals;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualCandleTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Block definition for a ritual candle.  Ritual candles serve as props in magickal rituals; lighting
 * them at the right time can allow a ritual to progress to the next stage.  They can also be dyed
 * different colors.
 * 
 * @author Daedalus4096
 */
public class RitualCandleBlock extends BaseEntityBlock implements IRitualPropBlock {
    public static final MapCodec<RitualCandleBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DyeColor.CODEC.fieldOf("color").forGetter(b -> b.color),
            propertiesCodec()
    ).apply(instance, RitualCandleBlock::new));
    
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(ResourceUtils.loc("block/ritual_candle"));
    
    protected static final List<RitualCandleBlock> REGISTRY = new ArrayList<>();

    protected final DyeColor color;
    
    public RitualCandleBlock(DyeColor colorIn, Block.Properties properties) {
        super(properties);
        this.color = colorIn;
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.FALSE));
        REGISTRY.add(this);
    }
    
    public DyeColor getColor() {
        return this.color;
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
    protected void createBlockStateDefinition(@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }
    
    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        // Show flame particles if lit
        if (stateIn.getValue(LIT)) {
            double x = pos.getX() + 0.5D;
            double y = pos.getY() + 0.7D;
            double z = pos.getZ() + 0.5D;
            worldIn.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
            if (rand.nextFloat() < 0.17F) {
                worldIn.playLocalSound(x, y, z, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + rand.nextFloat(), 0.3F + rand.nextFloat() * 0.7F, false);
            }
        }
        
        // Show spell sparkles if receiving salt power
        if (this.isBlockSaltPowered(worldIn, pos)) {
            FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Color.WHITE.getRGB());
        }
    }
    
    @Override
    @NotNull
    protected InteractionResult useWithoutItem(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                               @NotNull Player pPlayer, @NotNull BlockHitResult pHitResult) {
        if (pState.getValue(LIT)) {
            // If using an empty hand on a lit candle, snuff it
            pLevel.playSound(pPlayer, pPos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!pLevel.isClientSide()) {
                pLevel.setBlock(pPos, pState.setValue(LIT, Boolean.FALSE), Block.UPDATE_ALL_IMMEDIATE);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    @NotNull
    protected InteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel,
                                          @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand,
                                          @NotNull BlockHitResult pHitResult) {
        if (pStack.is(Items.FLINT_AND_STEEL) && !pState.getValue(LIT)) {
            // If using a flint-and-steel on an unlit candle, light it
            pLevel.playSound(pPlayer, pPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 0.8F + (pLevel.getRandom().nextFloat() * 0.4F));
            if (!pLevel.isClientSide()) {
                pLevel.setBlock(pPos, pState.setValue(LIT, Boolean.TRUE), Block.UPDATE_ALL_IMMEDIATE);
                pPlayer.getItemInHand(pHand).hurtAndBreak(1, pPlayer, pHand.asEquipmentSlot());
                
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
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new RitualCandleTileEntity(pos, state);
    }
    
    @Override
    public boolean isPropActivated(BlockState state, Level world, BlockPos pos) {
        return state != null && state.hasProperty(LIT) && state.getValue(LIT);
    }
    
    @Override
    public String getPropTranslationKey() {
        return "ritual.primalmagick.prop.ritual_candle";
    }
    
    public float getUsageStabilityBonus() {
        return 5.0F;
    }
    
    @Override
    public float getStabilityBonus(Level world, BlockPos pos) {
        return 0.01F;
    }
    
    @Override
    public float getSymmetryPenalty(Level world, BlockPos pos) {
        return 0.01F;
    }
    
    public static Collection<RitualCandleBlock> getAllCandles() {
        return Collections.unmodifiableList(REGISTRY);
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
