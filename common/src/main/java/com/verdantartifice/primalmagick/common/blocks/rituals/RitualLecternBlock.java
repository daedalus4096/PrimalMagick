package com.verdantartifice.primalmagick.common.blocks.rituals;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenEnchantedBookScreenPacket;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualLecternTileEntity;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;
import com.verdantartifice.primalmagick.platform.Services;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Comparator;
import java.util.Map;

/**
 * Block definition for a ritual lectern.  Ritual lecterns serve as props in magickal rituals; placing
 * an enchanted book on one at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class RitualLecternBlock extends BaseEntityBlock implements IRitualPropBlock {
    public static final MapCodec<RitualLecternBlock> CODEC = simpleCodec(RitualLecternBlock::new);
    
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty HAS_BOOK = BlockStateProperties.HAS_BOOK;

    protected static final VoxelShape BASE_AND_STAND_SHAPE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D));
    protected static final VoxelShape COLLISION_SHAPE = Shapes.or(BASE_AND_STAND_SHAPE, Block.box(0.0D, 15.0D, 0.0D, 16.0D, 15.0D, 16.0D));
    protected static final VoxelShape SHAPE = Shapes.or(Block.box(0.0D, 10.0D, 1.0D, 16.0D, 14.0D, 5.333333D), Block.box(0.0D, 12.0D, 5.333333D, 16.0D, 16.0D, 9.666667D), Block.box(0.0D, 14.0D, 9.666667D, 16.0D, 18.0D, 14.0D), BASE_AND_STAND_SHAPE);
    protected static final Map<Direction, VoxelShape> SHAPES = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
        map.put(Direction.NORTH, SHAPE);
        map.put(Direction.SOUTH, VoxelShapeUtils.rotate(SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
        map.put(Direction.WEST, VoxelShapeUtils.rotate(SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
        map.put(Direction.EAST, VoxelShapeUtils.rotate(SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
    });
    
    public RitualLecternBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HAS_BOOK, Boolean.FALSE));
    }
    
    @Override
    @NotNull
    public VoxelShape getOcclusionShape(@NotNull BlockState state) {
        return BASE_AND_STAND_SHAPE;
    }
    
    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return true;
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    
    @Override
    @NotNull
    public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return COLLISION_SHAPE;
    }
    
    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPES.getOrDefault(state.getValue(FACING), BASE_AND_STAND_SHAPE);
    }
    
    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @NotNull
    public BlockState rotate(BlockState state, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }
    
    @Override
    @NotNull
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_BOOK);
    }
    
    @Override
    @NotNull
    protected InteractionResult useItemOn(@NotNull ItemStack handStack, @NotNull BlockState state, @NotNull Level worldIn,
                                          @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn,
                                          @NotNull BlockHitResult hit) {
        if (!worldIn.isClientSide() && handIn == InteractionHand.MAIN_HAND) {
            if (worldIn.getBlockEntity(pos) instanceof RitualLecternTileEntity lecternTile) {
                ItemStack bookStack = lecternTile.getItem();
                if (bookStack.isEmpty() && handStack.is(Items.ENCHANTED_BOOK)) {
                    // When activating an empty lectern with an enchanted book in hand, place it on the lectern
                    ItemStack stack = handStack.copyWithCount(1);
                    lecternTile.setItem(stack);
                    player.getItemInHand(handIn).shrink(1);
                    if (player.getItemInHand(handIn).getCount() <= 0) {
                        player.setItemInHand(handIn, ItemStack.EMPTY);
                    }
                    player.getInventory().setChanged();
                    worldIn.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0F, 1.0F);
                    worldIn.setBlock(pos, state.setValue(HAS_BOOK, Boolean.TRUE), Block.UPDATE_ALL);
                    
                    // If this block is awaiting activation for an altar, notify it
                    if (this.isPropOpen(state, worldIn, pos)) {
                        this.onPropActivated(state, worldIn, pos, this.getUsageStabilityBonus());
                    }

                    return InteractionResult.SUCCESS;
                } else if (!bookStack.isEmpty()) {
                    if (player.isSecondaryUseActive()) {
                        // When activating a full lectern while sneaking, pick up the book
                        ItemStack stack = bookStack.copy();
                        lecternTile.setItem(ItemStack.EMPTY);
                        if (!player.getInventory().add(stack)) {
                            player.drop(stack, false);
                        }
                        player.getInventory().setChanged();
                        worldIn.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                        worldIn.setBlock(pos, state.setValue(HAS_BOOK, Boolean.FALSE), Block.UPDATE_ALL);
                    } else {
                        // When activating a full lectern while not sneaking, read the book
                        if (player instanceof ServerPlayer serverPlayer) {
                            EnchantmentHelper.getEnchantmentsForCrafting(bookStack).entrySet().stream().min(Comparator.comparing(Object2IntMap.Entry::getIntValue)).ifPresent(entry -> {
                                PacketHandler.sendToPlayer(new OpenEnchantedBookScreenPacket(entry.getKey(), player.registryAccess()), serverPlayer);
                            });
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(handStack, state, worldIn, pos, player, handIn, hit);
    }
    
    @Override
    @NotNull
    protected InteractionResult useWithoutItem(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                               @NotNull Player pPlayer, @NotNull BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {
            if (pLevel.getBlockEntity(pPos) instanceof RitualLecternTileEntity lecternTile) {
                ItemStack bookStack = lecternTile.getItem();
                if (!bookStack.isEmpty()) {
                    if (pPlayer.isSecondaryUseActive()) {
                        // When activating a full lectern while sneaking, pick up the book
                        ItemStack stack = bookStack.copy();
                        lecternTile.setItem(ItemStack.EMPTY);
                        if (!pPlayer.getInventory().add(stack)) {
                            pPlayer.drop(stack, false);
                        }
                        pPlayer.getInventory().setChanged();
                        pLevel.playSound(null, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                        pLevel.setBlock(pPos, pState.setValue(HAS_BOOK, Boolean.FALSE), Block.UPDATE_ALL);
                    } else {
                        // When activating a full lectern while not sneaking, read the book
                        if (pPlayer instanceof ServerPlayer serverPlayer) {
                            EnchantmentHelper.getEnchantmentsForCrafting(bookStack).entrySet().stream().min(Comparator.comparing(Object2IntMap.Entry::getIntValue)).ifPresent(entry -> {
                                PacketHandler.sendToPlayer(new OpenEnchantedBookScreenPacket(entry.getKey(), pPlayer.registryAccess()), serverPlayer);
                            });
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType type) {
        return false;
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
        return 0.02F;
    }

    @Override
    public float getSymmetryPenalty(Level world, BlockPos pos) {
        return 0.02F;
    }

    @Override
    public boolean isPropActivated(BlockState state, Level world, BlockPos pos) {
        return state != null && state.hasProperty(HAS_BOOK) && state.getValue(HAS_BOOK);
    }

    @Override
    public String getPropTranslationKey() {
        return "ritual.primalmagick.prop.ritual_lectern";
    }

    public float getUsageStabilityBonus() {
        return 10.0F;
    }
    
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return Services.BLOCK_ENTITY_PROTOTYPES.ritualLectern().create(pos, state);
    }

    @Override
    protected void affectNeighborsAfterRemoval(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, boolean movedByPiston) {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        level.updateNeighbourForOutputSignal(pos, state.getBlock());
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
