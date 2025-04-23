package com.verdantartifice.primalmagick.common.blocks.devices;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.tags.BlockTagsPM;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntity;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/**
 * Block definition for the scribe's table.  Allows players to play a minigame to increase their
 * comprehension of ancient languages, allowing them to translate ancient books found in the world.
 * 
 * @author Daedalus4096
 */
public class ScribeTableBlock extends BaseEntityBlock {
    public static final MapCodec<ScribeTableBlock> CODEC = simpleCodec(ScribeTableBlock::new);
    
    protected static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final List<BlockPos> BOOKSHELF_OFFSETS = BlockPos.betweenClosedStream(-2, 0, -2, 2, 1, 2)
            .filter(pos -> Math.abs(pos.getX()) == 2 || Math.abs(pos.getZ()) == 2)
            .map(BlockPos::immutable)
            .toList();

    public ScribeTableBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        // TODO Assemble more detailed shape for base table
        return Shapes.block();
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Make the block face the player when placed
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult hit) {
        if (!worldIn.isClientSide && player instanceof ServerPlayer serverPlayer) {
            // Open the GUI for the scribe table
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof ScribeTableTileEntity tableTile) {
                Services.PLAYER.openMenu(serverPlayer, tableTile, tile.getBlockPos());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return Services.BLOCK_ENTITY_PROTOTYPES.scribeTable().create(pPos, pState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        // Drop the tile entity's inventory into the world when the block is replaced
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity tile = pLevel.getBlockEntity(pPos);
            if (tile instanceof ScribeTableTileEntity castTile) {
                castTile.dropContents(pLevel, pPos);
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static boolean isValidBookshelf(Level level, BlockPos tablePos, BlockPos bookshelfPosOffset) {
        return level.getBlockState(tablePos.offset(bookshelfPosOffset)).is(BlockTagsPM.LINGUISTICS_POWER_PROVIDERS) &&
                level.getBlockState(tablePos.offset(bookshelfPosOffset.getX() / 2, bookshelfPosOffset.getY(), bookshelfPosOffset.getZ() / 2)).is(BlockTagsPM.LINGUISTICS_POWER_TRANSMITTERS);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);
        for (BlockPos offset : BOOKSHELF_OFFSETS) {
            if (pRandom.nextInt(16) == 0 && isValidBookshelf(pLevel, pPos, offset)) {
                // TODO Use ancient glyphs instead of Standard Galactic for particles
                pLevel.addParticle(ParticleTypes.ENCHANT,
                        pPos.getX() + 0.5D,
                        pPos.getY() + 2.0D,
                        pPos.getZ() + 0.5D,
                        offset.getX() + pRandom.nextDouble() - 0.5D,
                        offset.getY() - pRandom.nextDouble() - 1.0D,
                        offset.getZ() + pRandom.nextDouble() - 0.5D);
            }
        }
    }
}
