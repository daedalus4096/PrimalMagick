package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IEventService;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventServiceForge implements IEventService {
    @Override
    public void firePlayerCraftingEvent(Player player, ItemStack crafted, Container craftMatrix) {
        ForgeEventFactory.firePlayerCraftingEvent(player, crafted, craftMatrix);
    }

    @Override
    public void firePlayerSmeltedEvent(Player player, ItemStack smelted) {
        ForgeEventFactory.firePlayerSmeltedEvent(player, smelted);
    }

    @Override
    public boolean canEntityGrief(Level level, @Nullable Entity entity) {
        return ForgeEventFactory.getMobGriefingEvent(level, entity);
    }

    @Override
    public boolean onBlockPlace(@Nullable Entity entity, @NotNull Level level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return ForgeEventFactory.onBlockPlace(entity, BlockSnapshot.create(level.dimension(), level, pos), direction);
    }

    @Override
    public SpawnGroupData finalizeMobSpawn(Mob mob, ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData) {
        return ForgeEventFactory.onFinalizeSpawn(mob, level, difficulty, spawnType, spawnData);
    }

    @Override
    public void fireLivingJumpEvent(LivingEntity entity) {
        ForgeHooks.onLivingJump(entity);
    }
}
