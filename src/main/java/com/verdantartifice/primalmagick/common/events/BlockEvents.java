package com.verdantartifice.primalmagick.common.events;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookType;
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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for block related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
public class BlockEvents {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level world = (event.getLevel() instanceof Level) ? (Level)event.getLevel() : null;
        if (!event.isCanceled() && world != null && !world.isClientSide && !player.isSecondaryUseActive() && !BlockBreaker.hasBreakerQueued(world, event.getPos())) {
            triggerReverberation(world, event.getPos(), event.getState(), player, player.getMainHandItem());
            triggerDisintegration(world, event.getPos(), event.getState(), player, player.getMainHandItem());
        }
    }
    
    private static void triggerReverberation(Level world, BlockPos pos, BlockState state, Player player, ItemStack tool) {
        // Trigger block breakers if the player has a Reverberation tool in the main hand
        int level = tool.getEnchantmentLevel(EnchantmentsPM.REVERBERATION.get());
        if (level <= 0) {
            return;
        }
        
        // Calculate the direction from which the block was broken
        InteractionRecord interact = PlayerEvents.LAST_BLOCK_LEFT_CLICK.get(player.getUUID());
        Direction dir;
        if (interact == null) {
            Vec3 startPos = player.getEyePosition(1.0F);
            Vec3 endPos = startPos.add(player.getViewVector(1.0F).scale(player.getAttribute(ForgeMod.BLOCK_REACH.get()).getValue()));
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
        int xLimit = level * (dir.getStepX() == 0 ? 1 : 0);
        int yLimit = level * (dir.getStepY() == 0 ? 1 : 0);
        int zLimit = level * (dir.getStepZ() == 0 ? 1 : 0);
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
        int level = tool.getEnchantmentLevel(EnchantmentsPM.DISINTEGRATION.get());
        if (level <= 0) {
            return;
        }
        
        float durability = (float)Math.sqrt(100.0F * state.getDestroySpeed(world, pos));
        int breakerCount = (10 * level) - 1;
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
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onBlockBreakLowest(BlockEvent.BreakEvent event) {
        // Record the block break statistic
        if (!event.isCanceled() && event.getState().getDestroySpeed(event.getLevel(), event.getPos()) >= 2.0F && event.getPlayer().getMainHandItem().isEmpty()) {
            StatsManager.incrementValue(event.getPlayer(), StatsPM.BLOCKS_BROKEN_BAREHANDED);
        }
    }
    
    @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult result = InteractionResult.PASS;
        BlockPos pos = event.getHitVec().getBlockPos();
        BlockEntity tileEntity = event.getLevel().getBlockEntity(pos);
        
        if (tileEntity instanceof LecternBlockEntity lecternEntity) {
            result = handleLecternRightClick(lecternEntity, event.getEntity(), event.getHand());
        }
        
        if (result.consumesAction()) {
            event.setCanceled(true);
            event.setCancellationResult(result);
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
            } else if (lecternEntity.getBook().is(ItemsPM.STATIC_BOOK.get())) {
                // Open the static book screen
                if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                    PacketHandler.sendToPlayer(new OpenStaticBookScreenPacket(lecternEntity.getBook(), BookType.BOOK), serverPlayer);
                }
                return InteractionResult.SUCCESS;
            } else if (lecternEntity.getBook().is(Items.ENCHANTED_BOOK)) {
                // Open the enchanted book screen
                if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                    EnchantmentHelper.getEnchantments(lecternEntity.getBook()).entrySet().stream().sorted((e1, e2) -> -Integer.compare(e1.getValue(), e2.getValue())).findFirst().ifPresent(entry -> {
                        PacketHandler.sendToPlayer(new OpenEnchantedBookScreenPacket(entry.getKey()), serverPlayer);
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
