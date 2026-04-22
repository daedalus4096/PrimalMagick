package com.verdantartifice.primalmagick.common.blocks.devices;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.blocks.misc.GlowFieldBlock;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.devices.SunlampTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Block definition for a sunlamp.  Sunlamps are like normal lanterns, but they spawn glow fields
 * nearby in sufficiently dark spaces.  It can also be attached to any side of a block instead of
 * just the top or bottom.
 *
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.blocks.misc.GlowFieldBlock
 */
public class SunlampBlock extends BaseEntityBlock {
    public static final MapCodec<SunlampBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(Registries.BLOCK).fieldOf("glowBlockKey").forGetter(b -> b.glowBlockKey),
            propertiesCodec()
    ).apply(instance, SunlampBlock::new));

    protected static final Logger LOGGER = LogManager.getLogger();
    public static final EnumProperty<Direction> ATTACHMENT = EnumProperty.create("attachment", Direction.class, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    
    protected static final VoxelShape GROUND_SHAPE = VoxelShapeUtils.fromModel(ResourceUtils.loc("block/sunlamp_ground_base"));
    protected static final VoxelShape HANGING_SHAPE = VoxelShapeUtils.fromModel(ResourceUtils.loc("block/sunlamp_hanging_base"));

    private static final int RADIUS = 15;

    protected final ResourceKey<Block> glowBlockKey;
    
    public SunlampBlock(ResourceKey<Block> glowBlockKey, Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ATTACHMENT, Direction.DOWN));
        this.glowBlockKey = glowBlockKey;
    }
    
    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(ATTACHMENT) == Direction.DOWN ? GROUND_SHAPE : HANGING_SHAPE;
    }
    
    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(ATTACHMENT);
    }
    
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState().setValue(ATTACHMENT, context.getClickedFace().getOpposite());
        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
    }
    
    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader worldIn, @NotNull BlockPos pos) {
        Direction dir = state.getValue(ATTACHMENT);
        return Block.canSupportCenter(worldIn, pos.relative(dir), dir.getOpposite());
    }

    @Override
    @NotNull
    public BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess,
                                  @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState,
                                  @NotNull RandomSource random) {
        return state.getValue(ATTACHMENT) == direction && !state.canSurvive(level, pos) ?
                Blocks.AIR.defaultBlockState() :
                super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }
    
    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType type) {
        return false;
    }
    
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SunlampTileEntity(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityTypesPM.SUNLAMP.get(), SunlampTileEntity::tick);
    }

    public Optional<GlowFieldBlock> getGlowField(RegistryAccess registryAccess) {
        return registryAccess.lookupOrThrow(Registries.BLOCK).getOptional(this.glowBlockKey).filter(GlowFieldBlock.class::isInstance).map(GlowFieldBlock.class::cast);
    }

    @Override
    protected void affectNeighborsAfterRemoval(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, boolean movedByPiston) {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        this.getGlowField(level.registryAccess()).ifPresent(glow -> {
            BlockPos.betweenClosedStream(pos.offset(-RADIUS, -RADIUS, -RADIUS), pos.offset(RADIUS, RADIUS, RADIUS)).forEach(bp -> {
                if (level.getBlockState(bp).is(glow)) {
                    level.removeBlock(bp, false);
                }
            });
        });
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
