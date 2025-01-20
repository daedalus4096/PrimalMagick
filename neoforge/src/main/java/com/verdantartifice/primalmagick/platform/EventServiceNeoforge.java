package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.misc.GrindstoneChangeRecord;
import com.verdantartifice.primalmagick.platform.services.IEventService;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EventServiceNeoforge implements IEventService {
    @Override
    public void firePlayerCraftingEvent(Player player, ItemStack crafted, Container craftMatrix) {
        EventHooks.firePlayerCraftingEvent(player, crafted, craftMatrix);
    }

    @Override
    public void firePlayerSmeltedEvent(Player player, ItemStack smelted) {
        EventHooks.firePlayerSmeltedEvent(player, smelted);
    }

    @Override
    public boolean canEntityGrief(Level level, @Nullable Entity entity) {
        return EventHooks.canEntityGrief(level, entity);
    }

    @Override
    public boolean onBlockPlace(@Nullable Entity entity, @NotNull Level level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return EventHooks.onBlockPlace(entity, BlockSnapshot.create(level.dimension(), level, pos), direction);
    }

    @Override
    public SpawnGroupData finalizeMobSpawn(Mob mob, ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData) {
        return EventHooks.finalizeMobSpawn(mob, level, difficulty, spawnType, spawnData);
    }

    @Override
    public void fireLivingJumpEvent(LivingEntity entity) {
        CommonHooks.onLivingJump(entity);
    }

    @Override
    public void setCraftingPlayer(Player player) {
        CommonHooks.setCraftingPlayer(player);
    }

    @Override
    public Optional<Vec3> attemptEnderEntityTeleport(LivingEntity entity, Vec3 target) {
        EntityTeleportEvent.EnderEntity event = new EntityTeleportEvent.EnderEntity(entity, target.x, target.y, target.z);
        NeoForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            return Optional.of(event.getTarget());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        return stack.getBurnTime(recipeType);
    }

    @Override
    public int fireBlockBreakEvent(Level level, GameType gameType, ServerPlayer entityPlayer, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockEvent.BreakEvent event = CommonHooks.fireBlockBreak(level, gameType, entityPlayer, pos, state);
        return event.isCanceled() ? -1 : state.getExpDrop(level, pos, level.getBlockEntity(pos), entityPlayer, entityPlayer.getMainHandItem());
    }

    @Override
    public boolean isCorrectToolForDrops(Player player, BlockState state, Level level, BlockPos pos) {
        return !state.requiresCorrectToolForDrops() ?
                EventHooks.doPlayerHarvestCheck(player, state, level, pos) :
                player.hasCorrectToolForDrops(state, level, pos);
    }

    @Override
    public GrindstoneChangeRecord onGrindstoneChange(ItemStack top, ItemStack bottom, Container outputSlot, int xp) {
        var finalXp = CommonHooks.onGrindstoneChange(top, bottom, outputSlot, xp);
        return new GrindstoneChangeRecord(top, bottom, outputSlot.getItem(0), finalXp, finalXp == -1);
    }
}
