package com.verdantartifice.primalmagic.common.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagic.common.misc.BlockBreaker;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.stats.StatsPM;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for block related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class BlockEvents {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        World world = (event.getWorld() instanceof World) ? (World)event.getWorld() : null;
        if (!event.isCanceled() && world != null && !world.isRemote && !BlockBreaker.hasBreakerQueued(world, event.getPos())) {
            // Trigger block breakers if the player has a Reverberation tool in the main hand
            int reverbLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentsPM.REVERBERATION.get(), player.getHeldItemMainhand());
            if (reverbLevel > 0) {
                triggerReverberation(world, event.getPos(), event.getState(), player, reverbLevel);
            }
        }
    }
    
    private static void triggerReverberation(World world, BlockPos pos, BlockState state, PlayerEntity player, int level) {
        float durability = (float)Math.sqrt(100.0F * state.getBlockHardness(world, pos));
        
        // Calculate the direction from which the block was broken
        Vector3d startPos = player.getEyePosition(1.0F);
        Vector3d endPos = startPos.add(player.getLook(1.0F).scale(player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue()));
        BlockRayTraceResult rayTraceResult = world.rayTraceBlocks(new RayTraceContext(startPos, endPos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
        if (rayTraceResult.getType() == RayTraceResult.Type.MISS) {
            return;
        }
        Direction dir = rayTraceResult.getFace();
        
        // Iterate through the affected blocks
        int xLimit = level * (dir.getXOffset() == 0 ? 1 : 0);
        int yLimit = level * (dir.getYOffset() == 0 ? 1 : 0);
        int zLimit = level * (dir.getZOffset() == 0 ? 1 : 0);
        int count = 0;
        for (int dx = -xLimit; dx <= xLimit; dx++) {
            for (int dy = -yLimit; dy <= yLimit; dy++) {
                for (int dz = -zLimit; dz <= zLimit; dz++) {
                    // If this isn't the center block, schedule a block breaker for it
                    BlockPos targetPos = new BlockPos(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                    if (!targetPos.equals(pos)) {
                        BlockState targetState = world.getBlockState(targetPos);
                        float targetDurability = (float)Math.sqrt(100.0F * targetState.getBlockHardness(world, pos));
                        float newDurability = Math.max(0.0F, targetDurability - durability);
                        BlockBreaker.schedule(world, targetPos.manhattanDistance(pos), new BlockBreaker(0, targetPos, targetState, newDurability, targetDurability, player, true));
                        count++;
                    }
                }
            }
        }
        LOGGER.debug("Enqueued {} block breakers for reverb at pos ({}, {}, {}), dir {}", count, pos.getX(), pos.getY(), pos.getZ(), dir.getString());
    }
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onBlockBreakLowest(BlockEvent.BreakEvent event) {
        // Record the block break statistic
        if (!event.isCanceled() && event.getState().getBlockHardness(event.getWorld(), event.getPos()) >= 2.0F && event.getPlayer().getHeldItemMainhand().isEmpty() && 
                event.getPlayer().getHeldItemOffhand().isEmpty()) {
            StatsManager.incrementValue(event.getPlayer(), StatsPM.BLOCKS_BROKEN_BAREHANDED);
        }
    }
}
