package com.verdantartifice.primalmagick.common.blocks.devices;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.common.blocks.misc.CarvedBookshelfBlock;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.tags.BlockTagsPM;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntity;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public static final int MAX_LINGUISTICS_POWER = 90;

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

    /**
     * Determines whether the block at the given offset from the scribe's table at the given position is a valid
     * possibility for granting linguistics power.  Used to filter offsets for calculating total linguistics power and
     * to determine which surrounding blocks should emit glyph particles towards the scribe's table during animation.
     *
     * @param level the level in which the scribe's table resides
     * @param tablePos the position of the scribe's table to be queried
     * @param bookshelfPosOffset the position offset to be queried, relative to {@code tablePos}
     * @return whether the indicated block is a valid possibility for granting linguistics power
     */
    public static boolean isValidBookshelf(Level level, BlockPos tablePos, BlockPos bookshelfPosOffset) {
        BlockPos bookshelfPos = tablePos.offset(bookshelfPosOffset);
        return level.getBlockState(bookshelfPos).is(BlockTagsPM.LINGUISTICS_POWER_PROVIDERS) &&
                level.getBlockState(tablePos.offset(bookshelfPosOffset.getX() / 2, bookshelfPosOffset.getY(), bookshelfPosOffset.getZ() / 2)).is(BlockTagsPM.LINGUISTICS_POWER_TRANSMITTERS) &&
                hasBook(level, bookshelfPos);
    }

    /**
     * Determines whether the block at the given position is a bookshelf with <strong>any</strong> books on its
     * shelves.  Safe to run on either client or server side.
     *
     * @param level the level in which the scribe's table resides
     * @param bookshelfPos the world position to be queried
     * @return true if the indicated block has any books on its shelves, false otherwise
     */
    public static boolean hasBook(Level level, BlockPos bookshelfPos) {
        BlockState state = level.getBlockState(bookshelfPos);
        return CarvedBookshelfBlock.SLOT_OCCUPIED_PROPERTIES.stream().anyMatch(prop -> state.hasProperty(prop) && state.getValue(prop));
    }

    /**
     * Calculates the total linguistics power available to this scribe's table from surrounding bookshelves.  One point
     * is awarded for each unique ancient book which has been studied at least once, to a given maximum.  Linguistics
     * power is used to grant experience discounts when studying vocabulary at the table.
     *
     * @param level the level in which the scribe's table resides
     * @param tablePos the position of the scribe's table to be queried
     * @param player the player doing the querying
     * @return the total linguistics power available to the scribe's table
     */
    public static int getLinguisticsPower(Level level, BlockPos tablePos, Player player) {
        Set<Pair<Holder<BookDefinition>, Holder<BookLanguage>>> uniqueBookData = BOOKSHELF_OFFSETS.stream()
                .filter(offset -> isValidBookshelf(level, tablePos, offset))
                .flatMap(offset -> getAncientBookAttributes(level, tablePos, offset).stream())
                .collect(Collectors.toSet());
        int totalPower = uniqueBookData.stream()
                .mapToInt(pair -> LinguisticsManager.getTimesStudied(player, pair.getFirst(), pair.getSecond()) > 0 ? 1 : 0)
                .sum();
        return Math.min(totalPower, MAX_LINGUISTICS_POWER);
    }

    private static Set<Pair<Holder<BookDefinition>, Holder<BookLanguage>>> getAncientBookAttributes(Level level, BlockPos tablePos, BlockPos bookshelfPosOffset) {
        Set<Pair<Holder<BookDefinition>, Holder<BookLanguage>>> retVal = new HashSet<>();
        Services.CAPABILITIES.itemHandler(level, tablePos.offset(bookshelfPosOffset), Direction.UP).ifPresent(handler -> {
            for (int index = 0; index < Math.min(handler.getSlots(), CarvedBookshelfBlock.MAX_BOOKS); index++) {
                ItemStack bookStack = handler.getStackInSlot(index);
                StaticBookItem.getBookDefinition(bookStack).ifPresent(bookDefinition -> {
                    StaticBookItem.getBookLanguage(bookStack).ifPresent(bookLanguage -> {
                        // If the item stack is a static book with a registered definition and an ancient language, save it
                        if (bookLanguage.value().isComplex()) {
                            retVal.add(Pair.of(bookDefinition, bookLanguage));
                        }
                    });
                });
            }
        });
        return retVal;
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);
        for (BlockPos offset : BOOKSHELF_OFFSETS) {
            if (pRandom.nextInt(16) == 0 && isValidBookshelf(pLevel, pPos, offset)) {
                pLevel.addParticle(ParticleTypesPM.LINGUISTICS.get(),
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
