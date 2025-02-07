package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.misc.BlockBreaker;
import com.verdantartifice.primalmagick.common.misc.InteractionRecord;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenEnchantedBookScreenPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenGrimoireScreenPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenStaticBookScreenPacket;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Handlers for block related events.
 * 
 * @author Daedalus4096
 */
public class BlockEvents {
    public static void onBlockBreak(Player player, Level level, BlockPos pos, BlockState state) {
        if (level != null && !level.isClientSide && !player.isSecondaryUseActive() && !BlockBreaker.hasBreakerQueued(level, pos)) {
            triggerReverberation(level, pos, state, player, player.getMainHandItem());
            triggerDisintegration(level, pos, state, player, player.getMainHandItem());
        }
    }
    
    private static void triggerReverberation(Level world, BlockPos pos, BlockState state, Player player, ItemStack tool) {
        // Trigger block breakers if the player has a Reverberation tool in the main hand
        int enchLevel = EnchantmentHelperPM.getEnchantmentLevel(tool, EnchantmentsPM.REVERBERATION, world.registryAccess());
        if (enchLevel <= 0) {
            return;
        }
        
        // Calculate the direction from which the block was broken
        InteractionRecord interact = PlayerEvents.LAST_BLOCK_LEFT_CLICK.get(player.getUUID());
        Direction dir;
        if (interact == null) {
            Vec3 startPos = player.getEyePosition(1.0F);
            Vec3 endPos = startPos.add(player.getViewVector(1.0F).scale(player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue()));
            BlockHitResult rayTraceResult = world.clip(new ClipContext(startPos, endPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
            if (rayTraceResult.getType() == HitResult.Type.MISS) {
                return;
            }
            dir = rayTraceResult.getDirection();
        } else {
            dir = interact.getFace();
        }
        
        // Iterate through the affected blocks
        float durability = (float)Math.sqrt(100.0F * state.getDestroySpeed(world, pos));
        int xLimit = enchLevel * (dir.getStepX() == 0 ? 1 : 0);
        int yLimit = enchLevel * (dir.getStepY() == 0 ? 1 : 0);
        int zLimit = enchLevel * (dir.getStepZ() == 0 ? 1 : 0);
        for (int dx = -xLimit; dx <= xLimit; dx++) {
            for (int dy = -yLimit; dy <= yLimit; dy++) {
                for (int dz = -zLimit; dz <= zLimit; dz++) {
                    // If this isn't the center block, schedule a block breaker for it
                    BlockPos targetPos = new BlockPos(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                    if (!targetPos.equals(pos)) {
                        BlockState targetState = world.getBlockState(targetPos);
                        float targetDurability = (float)Math.sqrt(100.0F * targetState.getDestroySpeed(world, pos));
                        float newDurability = Math.max(0.0F, targetDurability - durability);
                        BlockBreaker breaker = new BlockBreaker.Builder().target(targetPos, targetState).durability(newDurability, targetDurability).player(player).tool(tool).oneShot().skipEvent().build();
                        BlockBreaker.schedule(world, targetPos.distManhattan(pos), breaker);
                    }
                }
            }
        }
    }
    
    private static void triggerDisintegration(Level world, BlockPos pos, BlockState state, Player player, ItemStack tool) {
        // Trigger block breakers if the player has a Reverberation tool in the main hand
        int enchLevel = EnchantmentHelperPM.getEnchantmentLevel(tool, EnchantmentsPM.DISINTEGRATION, world.registryAccess());
        if (enchLevel <= 0) {
            return;
        }
        
        float durability = (float)Math.sqrt(100.0F * state.getDestroySpeed(world, pos));
        int breakerCount = (10 * enchLevel) - 1;
        Set<BlockPos> examinedPositions = new HashSet<>();
        Queue<BlockPos> processingQueue = new LinkedList<>();
        
        // Set up initial conditions
        examinedPositions.add(pos);
        for (Direction dir : Direction.values()) {
            BlockPos setupPos = pos.relative(dir);
            examinedPositions.add(setupPos);
            processingQueue.offer(setupPos);
        }

        // Iterate through the affected blocks
        while (!processingQueue.isEmpty() && breakerCount > 0) {
            BlockPos curPos = processingQueue.poll();
            BlockState curState = world.getBlockState(curPos);
            if (curState.getBlock().equals(state.getBlock())) {
                // If the currently examined block is of the same type as the original block, schedule a breaker and enqueue its neighbors for examination
                breakerCount--;
                BlockBreaker breaker = new BlockBreaker.Builder().target(curPos, curState).durability(0.0F, durability).player(player).tool(tool).oneShot().skipEvent().build();
                BlockBreaker.schedule(world, curPos.distManhattan(pos), breaker);
                for (Direction dir : Direction.values()) {
                    BlockPos nextPos = curPos.relative(dir);
                    if (!examinedPositions.contains(nextPos)) {
                        examinedPositions.add(nextPos);
                        processingQueue.offer(nextPos);
                    }
                }
            }
        }
    }
    
    public static void onBlockBreakLowest(Player player, LevelAccessor level, BlockPos pos, BlockState state) {
        // Record the block break statistic
        if (state.getDestroySpeed(level, pos) >= 2.0F && player.getMainHandItem().isEmpty()) {
            StatsManager.incrementValue(player, StatsPM.BLOCKS_BROKEN_BAREHANDED);
        }
    }
    
    public static InteractionResult onBlockRightClick(Player player, LevelAccessor level, BlockHitResult hitResult, InteractionHand hand) {
        if (level.getBlockEntity(hitResult.getBlockPos()) instanceof LecternBlockEntity lecternEntity) {
            return handleLecternRightClick(lecternEntity, player, hand);
        } else {
            return InteractionResult.PASS;
        }
    }
    
    private static InteractionResult handleLecternRightClick(LecternBlockEntity lecternEntity, Player player, InteractionHand hand) {
        Level level = lecternEntity.getLevel();
        if (lecternEntity.getBlockState().getValue(LecternBlock.HAS_BOOK)) {
            if (player.isSecondaryUseActive()) {
                // Take the book
                ItemStack bookStack = lecternEntity.getBook();
                lecternEntity.setBook(ItemStack.EMPTY);
                LecternBlock.resetBookState(player, level, lecternEntity.getBlockPos(), lecternEntity.getBlockState(), false);
                if (!player.getInventory().add(bookStack)) {
                    player.drop(bookStack, false);
                }
                return InteractionResult.SUCCESS;
            } else if (lecternEntity.getBook().is(ItemsPM.GRIMOIRE.get())) {
                // Open the grimoire screen
                if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                    StatsManager.incrementValue(player, StatsPM.GRIMOIRE_READ);
                    PacketHandler.sendToPlayer(new OpenGrimoireScreenPacket(), serverPlayer);
                }
                return InteractionResult.SUCCESS;
            } else if (lecternEntity.getBook().is(ItemTagsPM.STATIC_BOOKS)) {
                // Open the static book screen
                if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                    PacketHandler.sendToPlayer(new OpenStaticBookScreenPacket(lecternEntity.getBook(), BookType.BOOK, player.registryAccess()), serverPlayer);
                }
                return InteractionResult.SUCCESS;
            } else if (lecternEntity.getBook().is(Items.ENCHANTED_BOOK)) {
                // Open the enchanted book screen
                if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                    EnchantmentHelper.getEnchantmentsForCrafting(lecternEntity.getBook()).entrySet().stream().sorted(Comparator.comparing(Object2IntMap.Entry::getIntValue)).findFirst().ifPresent(entry -> {
                        PacketHandler.sendToPlayer(new OpenEnchantedBookScreenPacket(entry.getKey(), player.registryAccess()), serverPlayer);
                    });
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            // Place the grimoire
            ItemStack handStack = player.getItemInHand(hand);
            if (handStack.is(ItemTags.LECTERN_BOOKS) && LecternBlock.tryPlaceBook(player, level, lecternEntity.getBlockPos(), lecternEntity.getBlockState(), handStack)) {
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
